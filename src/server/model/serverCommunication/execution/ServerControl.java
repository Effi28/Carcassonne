package server.model.serverCommunication.execution;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import server.model.PlayerThread;
import server.model.game.ServerGame;
import server.model.serverCommunication.clientHandling.ClientHandler;
import server.model.serverCommunication.queues.OutputQueue;
import server.model.serverCommunication.queues.ServerMoveMadeStorage;
import server.model.serverCommunication.utility.CleaningThread;
import server.model.serverCommunication.utility.ServerMessageBuilder;
import server.model.serverCommunication.utility.WrappedJSONObject;
import shared.Configuration;
import shared.enums.CapabilitiesType;
import shared.enums.GameStatus;
import shared.enums.Reason;
import shared.model.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Represents the connection between all connected clients and the logic for the game.
 * <p>
 * Manages all connected clients (represented by their handler-Threads -> ClientHandler) and all created games. Creates
 * new games and handles the communication between a game and its players as well as sending messages to all clients.
 * Therefore it performs the checks for capabilities and extensions of a game. It performs the calculation of the games
 * each client should see. A CleaningThread is started when the constructor is invoked, it checks whether a game has no
 * players and no spectators in it and deletes it then.
 * 
 * It implements the SINGLETON-Pattern because there should only be one instance of this class in order to manage all
 * games and clients correctly.
 * 
 * @version 16.01.2014
 * @see ClientHandler
 * @see ServerGame
 * @see CleaningThread
 */
public final class ServerControl {

	/**
	 * Error logger
	 */
	private static Logger log = LogManager.getLogger("ERROR");
	/**
	 * User logger
	 */
	private static Logger userLog = LogManager.getLogger("USER");
	/**
	 * ServerSocket that listens for incoming connections
	 */
	private ServerSocket listen;
	/**
	 * Counts the number of games and will give the gameID
	 */
	private int gameIDCounter = 1;
	/**
	 * Map of connected users
	 */
	private volatile Map<String, ClientHandler> connectedHandler;
	/**
	 * Map of games created
	 */
	private volatile Map<Integer, ServerGame> games;
	/**
	 * Instance of the ServerControl because of SINGLETON
	 */
	private static ServerControl instance;
	/**
	 * Maximum number of players that can join a game
	 */
	private static int maximumPlayers;
	/**
	 * Represents the capabilities this server can handle
	 */
	private final Set<CapabilitiesType> serverCapabilities;

	/**
	 * Executer which performs the calculations the received messages indicate.
	 */
	private final ExecutorService exec;
	/**
	 * Reference to the class which performs the classification of the incoming messages
	 */
	private final MessageClassification classification;

	/**
	 * Only constructor of this class. Sets the ServerSocket and creates HashSets for the connected handler and for the
	 * games.
	 * 
	 * @param listen
	 *            the ServerSocket on which the server will listen for incoming connections
	 */
	private ServerControl(ServerSocket listen) {
		this.listen = listen;
		connectedHandler = new HashMap<String, ClientHandler>();
		games = new HashMap<Integer, ServerGame>();
		maximumPlayers = Configuration.MAXUSER;
		exec = Executors.newSingleThreadExecutor();
		classification = MessageClassification.getInstance(this);

		// Add the capabilities of the server
		serverCapabilities = EnumSet.of(CapabilitiesType.CHAT,
				CapabilitiesType.BISHOP, CapabilitiesType.BIGMEEPLE,
				CapabilitiesType.INNS);

		// Starts the cleaning Thread
		CleaningThread.getInstance(this).setDaemon(true);
		CleaningThread.getInstance(this).start();

		exec.execute(new Runnable() {
			@Override
			public void run() {
				while (true) {
					classification.performMessageClassification();
				}
			}
		});
	}

	/**
	 * Returns the instance, because of SINGLETON
	 * 
	 * @param listen
	 *            ServerSocket -> listening port for incoming connections
	 * @return instance of this class
	 */
	public synchronized static ServerControl getInstance(ServerSocket listen) {
		if (instance == null) {
			instance = new ServerControl(listen);
		}
		return instance;
	}

	/**
	 * Listens for incoming clients
	 */
	public void getConnections() {
		while (!Thread.interrupted()) {
			Socket socket = null;
			try {
				socket = listen.accept();
				if (socket != null) {
					ClientHandler handler = new ClientHandler(socket, this);
					handler.setDaemon(true);
					handler.start();
					handler = null;
				}
				socket = null;
			} catch (IOException e) {
				try {
					listen.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				log.error(e.getMessage()
						+ "Server was shutdown because of an IOException.");
			}
		}
	}

	/**
	 * Adds handler to the connectedHandlerSet, when a client logs in.
	 * 
	 * @param handler
	 *            which is added
	 */
	void addHandler(ClientHandler handler) {
		connectedHandler.put(handler.getUser().getNick(), handler);
	}

	/**
	 * Removes a handler from the connectedHanderSet, when a client logs out
	 * 
	 * @param handler
	 *            which is removed
	 */
	void removeHandler(ClientHandler handler) {
		connectedHandler.remove(handler.getUser().getNick());
	}

	/**
	 * Broadcasts a message to all clients except the one sending
	 * 
	 * @param from
	 *            ClientHandler which send the message
	 * @param msg
	 *            message which will be send
	 */
	void broadcast(ClientHandler from, JSONObject msg) {
		for (ClientHandler h : connectedHandler.values()) {
			if (!h.equals(from)) {
				OutputQueue.addElement(new WrappedJSONObject(msg, h));
			}
		}
	}

	/**
	 * Returns the specific clientHandler with the specific nick
	 * 
	 * @param nick
	 *            Name of the User which will be returned
	 * @return specific client handler
	 */
	ClientHandler getClientHandler(String nick) {
		return connectedHandler.get(nick);
	}

	/**
	 * This method sends a chatMessage to all clients. If the client doesn't have the capability to chat an invalid
	 * message will be sent to him.
	 * 
	 * @param senderID
	 *            sender of the message
	 * @param msg
	 *            msg to be send
	 * @param capabilities
	 *            Capabilities of the client
	 * @param handler
	 *            Handler which represents the client which sent the message
	 */
	void sendChatMessageToAll(String senderID, String msg,
			Set<CapabilitiesType> capabilities, ClientHandler handler) {
		if (capabilities.contains(CapabilitiesType.CHAT)) {
			broadcastToAll(ServerMessageBuilder.chatToAll(senderID, msg));
		} else {
			OutputQueue.addElement(new WrappedJSONObject(ServerMessageBuilder
					.invalidMessage(Reason.CHATNOTAVAILABLE), handler));
		}
	}

	/**
	 * This method checks whether the checking client's nickname is already taken or not and tells the client.
	 * 
	 * 
	 * @param nick
	 *            name of the new client
	 * @param capa
	 *            capabilities of the client
	 * @param handler
	 *            specific handler of the client
	 * 
	 */
	void checkLogin(String nick, Set<CapabilitiesType> capa,
			ClientHandler handler) {

		// Check whether the nick is alphanumeric
		if (nick.matches("^.*[^a-zA-Z0-9_ ].*$")) {
			OutputQueue.addElement(new WrappedJSONObject(ServerMessageBuilder
					.loginFailed(Reason.ILLEGALNICK), handler));
			return;
		} else {
			handler.setUser(new User(nick));
		}

		// If the handler's nick is already taken --> send login failed
		if (connectedHandler.containsKey(handler.getUser().getNick())) {
			OutputQueue.addElement(new WrappedJSONObject(ServerMessageBuilder
					.loginFailed(Reason.NICKALREADYTAKEN), handler));
			return;
		}
		// If the nick is not taken
		else {
			ArrayList<String> nicks = new ArrayList<String>();
			for (String u : connectedHandler.keySet()) {
				nicks.add(u);
			}

			// Get the capabilities server and client share
			List<CapabilitiesType> sharedCapa = new ArrayList<>();
			for (CapabilitiesType t : capa) {
				if (serverCapabilities.contains(t)) {
					sharedCapa.add(t);
				}
			}

			// Send only the games with the extensions the specific client can
			// handle
			Map<Integer, ServerGame> gamesToBeSent = new HashMap<>();

			for (ServerGame game : games.values()) {
				if (!game.getExtension().isEmpty()) {
					if (sharedCapa.containsAll(game.getExtension())) {
						gamesToBeSent.put(game.getGameID(), game);
					}
				} else {
					gamesToBeSent.put(game.getGameID(), game);
				}

			}

			addHandler(handler); // adds this handler to the HashSet with logged
			// send the client that the login was successful
			OutputQueue.addElement(new WrappedJSONObject(ServerMessageBuilder
					.loginSucceeded(nick, gamesToBeSent, nicks, sharedCapa),
					handler));
			broadcast(handler, ServerMessageBuilder.playerAdded(nick));
			handler.setCapabilities(capa);
			// in clients
			userLog.info(handler.getNick() + " logged in.");
		}
	}

	/**
	 * Delivers a tile-drawn message to the clients .
	 * <p>
	 * Informs the clients about the next tile that will be set on the gamefield.
	 * 
	 * @param gameID
	 *            ID of the game where the tile will be played
	 * @param nick
	 *            nick of the player who's turn it is
	 * @param card
	 *            tile that was drawn
	 * @param remaining
	 *            remaining cards in this game
	 * @param turnTime
	 *            time the player has to make a move
	 */
	public synchronized void tileDrawn(int gameID, String nick, Card card,
			int remaining, int turnTime) {
		ServerGame game = games.get(gameID);
		// Send to all player in this game
		for (Player p : game.getPlayerList().values()) {
			OutputQueue.addElement(new WrappedJSONObject(ServerMessageBuilder
					.tileDrawn(gameID, nick, card, remaining, turnTime),
					getClientHandler(p.getNick())));
		}
		// Send to all spectator in this game
		for (Spectator s : game.getSpectatorList().values()) {
			OutputQueue.addElement(new WrappedJSONObject(ServerMessageBuilder
					.tileDrawn(gameID, nick, card, remaining, turnTime),
					getClientHandler(s.getNick())));
		}
	}

	/**
	 * Checks if the gameStart is possible.
	 * 
	 * <p>
	 * 
	 * At least two Players have to joined a game before it can starts. Also a game can only started if the Host wants
	 * it. Finally a game can only started if the game wasn't already started.
	 * 
	 * @param gameID
	 *            game ID of the game that will be started
	 * @param from
	 *            client that wants to start the game
	 */
	void checkGameStart(int gameID, ClientHandler from) {

		ServerGame game = games.get(gameID);

		// Tests if a game has too few players
		if (game.getPlayerList().size() < 2) {
			broadcastToAll(ServerMessageBuilder.startGameFailed(gameID,
					Reason.NOTENOUGHPLAYERS));
		}

		// Tests if a game is started by his owner
		else if (game.getHost().equals(from.getNick())) {
			broadcastToAll(ServerMessageBuilder.startGameFailed(gameID,
					Reason.NOTGAMEOWNER));
		}

		// Tests if a game is already started
		else if (game.getState() == GameStatus.ONGOING) {
			broadcastToAll(ServerMessageBuilder.startGameFailed(gameID,
					Reason.GAMEALREADYSTARTED));

			// Success
		} else {

			game.setState(GameStatus.ONGOING);

			broadcastToAllIfExtensionFits(game,
					ServerMessageBuilder.gameUpdate(game.getGameInformation()));

			broadcastToAll(ServerMessageBuilder.moveMade(gameID, game.getHost()
					.getNick(), game.getStartCard(), ServerMessageBuilder
					.createAction(new Position(0, 0), game.getStartCard()
							.getRotation())));

			ServerMoveMadeStorage.addMoveToQueue(gameID, ServerMessageBuilder
					.moveMade(gameID, game.getHost().getNick(), game
							.getStartCard(), ServerMessageBuilder.createAction(
							new Position(0, 0), game.getStartCard()
									.getRotation())));
			game.startThread();
		}
	}

	/**
	 * Checks whether it is legal to create a new game. If it is legal a new game is created.
	 * 
	 * @param gameName
	 *            name of the game
	 * @param color
	 *            color of the new created game
	 * @param host
	 *            Host of the Game
	 * @param extensions
	 *            chosen extension of a game
	 * @param turnTime
	 *            Time the player has to make a move
	 * @param handler
	 *            Handler which represents the player that wants to create a game
	 */
	void checkGameCreation(Player host, String gameName, String color,
			int turnTime, Set<CapabilitiesType> extensions,
			ClientHandler handler) {

		// Checks whether the turntime is ok
		int time = 0;
		if (turnTime == 0) {
			time = Configuration.STANDARDTURNTIME;
		} else if (turnTime < 0) {
			OutputQueue.addElement(new WrappedJSONObject(ServerMessageBuilder
					.gameCreationFailed(gameName, Reason.INVALIDTURNTIMELIMIT),
					handler));
		} else {
			time = turnTime;
		}

		ServerGame newGame = new ServerGame(gameName, host, gameIDCounter,
				this, time, extensions);

		for (CapabilitiesType capa : extensions) {
			if ((!handler.getCapabilities().contains(capa) || !(serverCapabilities
					.contains(capa))) && extensions != null) {
				OutputQueue.addElement(new WrappedJSONObject(
						ServerMessageBuilder.gameCreationFailed(gameName,
								Reason.INVALIDEXTENSION), handler));
			}
		}

		if (gameName.matches("^.*[^a-zA-Z0-9_ ].*$")) {
			OutputQueue.addElement(new WrappedJSONObject(ServerMessageBuilder
					.gameCreationFailed(gameName, Reason.ILLEGALGAMENAME),
					handler));
		} else if (games.containsValue(newGame)) {
			OutputQueue.addElement(new WrappedJSONObject(ServerMessageBuilder
					.gameCreationFailed(gameName, Reason.NAMEALREADYINUSE),
					handler));
		} else {
			for (ServerGame s : games.values()) {
				if (host.getNick().equals(s.getHost().getNick())) {
					OutputQueue.addElement(new WrappedJSONObject(
							ServerMessageBuilder.gameCreationFailed(gameName,
									Reason.ALREADYHOSTING), handler));
					return;
				}
			}

			// add the game to the Map with the ID the counter gives
			games.put(gameIDCounter, newGame);

			broadcastToAllIfExtensionFits(newGame,
					ServerMessageBuilder.gameCreated(newGame
							.getGameInformation()));
			gameIDCounter++;
		}
	}

	/**
	 * Handles an invalid message.
	 * 
	 * @param handler
	 *            Reference to the handler which sent the invalid message.
	 */
	void noCaseMatched(ClientHandler handler) {
		OutputQueue.addElement(new WrappedJSONObject(ServerMessageBuilder
				.invalidMessage(Reason.INVALIDMESSAGE), handler));
	}

	/**
	 * Checks whether the player can join the game or not and sends the right messages.
	 * 
	 * @param handler
	 *            handler that represents the user that wants to connect
	 * @param gameID
	 *            ID of the game the user wants to join
	 * @param color
	 *            color the player chose
	 */
	void checkJoin(ClientHandler handler, int gameID, String color) {

		Collection<Player> playerList = games.get(gameID).getPlayerList()
				.values();

		ServerGame game = games.get(gameID);

		// Check whether the color is taken, if it is taken send join failed
		for (Player p : playerList) {
			if (color.equals(p.getColor())) {
				OutputQueue.addElement(new WrappedJSONObject(
						ServerMessageBuilder.joinGameFailed(gameID,
								Reason.COLORTAKEN), handler));
				return;
			}
		}

		// Tests if the gameID is in the games Map if not send game not found
		if (!(games.containsKey(gameID))) {
			OutputQueue.addElement(new WrappedJSONObject(ServerMessageBuilder
					.joinGameFailed(gameID, Reason.GAMENOTFOUND), handler));
			return;
		}

		// Tests if the game is full if true then send game is full
		else if (playerList.size() >= maximumPlayers) {
			OutputQueue.addElement(new WrappedJSONObject(ServerMessageBuilder
					.joinGameFailed(gameID, Reason.GAMEISFULL), handler));
			return;
		}

		// Tests if the user is already joined the game if true then send
		// already joined
		else if (game.getPlayerList().containsKey(handler.getNick())) {
			OutputQueue.addElement(new WrappedJSONObject(ServerMessageBuilder
					.joinGameFailed(gameID, Reason.ALREADYJOINED), handler));
			return;
		}

		// Tests if the game has already begun if true then send alreadystarted
		else if (game.getState().equals(GameStatus.ONGOING)) {
			OutputQueue.addElement(new WrappedJSONObject(ServerMessageBuilder
					.joinGameFailed(gameID, Reason.ALREADYSTARTED), handler));
			return;
		}

		// Tests if the user is already joined as an observer
		else if (game.getSpectatorList().containsKey(handler.getNick())) {
			OutputQueue.addElement(new WrappedJSONObject(ServerMessageBuilder
					.joinGameFailed(gameID, Reason.ALREADYWATCHING), handler));
			return;
		}

		// Success
		else {
			OutputQueue.addElement(new WrappedJSONObject(ServerMessageBuilder
					.joinGameSuccess(gameID), handler));
			String nick = handler.getNick();

			game.getPlayerList().put(nick, new Player(nick, color));
			broadcastToAllIfExtensionFits(game,
					ServerMessageBuilder.gameUpdate(game.getGameInformation()));
		}
	}

	/**
	 * Removes the leaving player from the playerList of the game if he has joined a game.
	 * 
	 * @param clientHandler
	 *            Player which wants to leave
	 */
	void removePlayer(ClientHandler clientHandler) {
		String nick = clientHandler.getNick();
		Collection<ServerGame> test = games.values();
		for (ServerGame c : test) {
			if (c.getPlayerList().containsKey(nick)) {
				c.getPlayerList().remove(nick);
				if (c.getPlayerList().size() == 0) {
					ServerMoveMadeStorage.deleteQueue(c.getGameID());
					c.remove();
					games.remove(c.getGameID());
				}
			}
		}
	}

	/**
	 * Checks if the user can watch a specific game
	 * 
	 * @param handler
	 *            User which wants to watch a game
	 * @param gameID
	 *            GameID of the game which the user wants to watch
	 */
	void checkWatchGame(ClientHandler handler, int gameID) {

		ServerGame game = games.get(gameID);

		// Tests if the game is available in the games collection
		if (!games.containsKey(gameID)) {
			OutputQueue.addElement(new WrappedJSONObject(ServerMessageBuilder
					.watchGameFailed(gameID, Reason.GAMENOTFOUND), handler));
			return;
		}

		// Tests if the user has already joined the game as a Player
		else if (game.getPlayerList().containsKey(handler.getNick())) {
			OutputQueue.addElement(new WrappedJSONObject(ServerMessageBuilder
					.watchGameFailed(gameID, Reason.ALREADYJOINED), handler));
			return;
		}

		// Tests if the User is already watching this game
		else if (game.getSpectatorList().containsKey(handler.getNick())) {
			OutputQueue.addElement(new WrappedJSONObject(ServerMessageBuilder
					.watchGameFailed(gameID, Reason.ALREADYWATCHING), handler));
		}

		// Manages the watch process if it was successful
		else if (games.containsKey(gameID)) {
			game.getSpectatorList().put(handler.getNick(),
					new Spectator(handler.getUser().getNick()));
			OutputQueue.addElement(new WrappedJSONObject(ServerMessageBuilder
					.watchGameSuccess(gameID), handler));
			if (game.getState().equals(GameStatus.ONGOING)) {
				ServerMoveMadeStorage.sendAllMoveMade(handler.getSender(),
						gameID);
			}
			broadcastToAllIfExtensionFits(game,
					ServerMessageBuilder.gameUpdate(game.getGameInformation()));
		}
	}

	/**
	 * Checks if the user leaves a started game or not started game
	 * 
	 * @param user
	 *            User which leaves
	 * @param gameID
	 *            id of the game which the user leaves
	 */
	void leaveGame(User user, int gameID) {
		ServerGame game = games.get(gameID);
		Map<String, Player> playerList = game.getPlayerList();
		Map<String, Spectator> spectatorList = game.getSpectatorList();
		String nick = user.getNick();

		// Tests if the game is not STARTED and the leaving User is a Player
		if (game.getState().equals(GameStatus.NOTSTARTED)
				&& playerList.containsKey(nick)) {
			playerList.remove(nick);
			// if the leaving user is the host
			if (game.getHost().getNick().equals(user.getNick())) {
				broadcastToAllIfExtensionFits(game,
						ServerMessageBuilder.gameUpdate(game
								.getGameInformation()));
				ServerMoveMadeStorage.deleteQueue(game.getGameID());
				game.remove();
				games.remove(game.getGameID());
				broadcastToAll(ServerMessageBuilder.gameRemoved(gameID));
			} else {
				broadcastToAllIfExtensionFits(game,
						ServerMessageBuilder.gameUpdate(game
								.getGameInformation()));
			}

		}

		// Tests if the game is STARTED and the leaving User is a Player
		else if (game.getState().equals(GameStatus.ONGOING)
				&& playerList.containsKey(nick)) {
			for (User s : playerList.values()) {
				broadcast(getClientHandler(s.getNick()),
						ServerMessageBuilder.playerLeft(gameID, nick));
			}
			playerList.remove(nick);
			if (playerList.size() == 0 && spectatorList.size() == 0) {
				ServerMoveMadeStorage.deleteQueue(game.getGameID());
				game.remove();
				games.remove(gameID);
				broadcastToAll(ServerMessageBuilder.gameRemoved(gameID));
			}
		}
		// Tests if the leaving User is a Spectator no matter if the game is
		// ongoing or not
		else if (spectatorList.containsKey(nick)) {
			spectatorList.remove(nick);
			broadcastToAllIfExtensionFits(games.get(gameID),
					ServerMessageBuilder.gameUpdate(games.get(gameID)
							.getGameInformation()));
		}

		// If the game is ended
		else if (game.getState().equals(GameStatus.ENDED)) {
			playerList.remove(user.getNick());
			for (User s : playerList.values()) {
				broadcast(getClientHandler(s.getNick()),
						ServerMessageBuilder.playerLeft(gameID, nick));
			}
			if (playerList.size() == 0) {
				ServerMoveMadeStorage.deleteQueue(game.getGameID());
				game.remove();
				games.remove(gameID);
				broadcastToAll(ServerMessageBuilder.gameRemoved(gameID));
			}
		}
	}

	/**
	 * Sends a message to a specific game.
	 * 
	 * @param senderID
	 *            Sender of the message
	 * @param msg
	 *            message which will be send
	 * @param gameID
	 *            GameID of the Game which will receive the message
	 * @param capabilities
	 *            Capabilities of the client
	 * @param handler
	 *            Reference to the handler which receives the messages from the client
	 */
	void sendGameChatMessage(String senderID, String msg, int gameID,
			ClientHandler handler, Set<CapabilitiesType> capabilities) {
		Collection<Player> pList = games.get(gameID).getPlayerList().values();
		Collection<Spectator> sList = games.get(gameID).getSpectatorList()
				.values();

		if (!capabilities.contains(CapabilitiesType.CHAT)) {
			OutputQueue.addElement(new WrappedJSONObject(ServerMessageBuilder
					.invalidMessage(Reason.CHATNOTAVAILABLE), handler));
		} else {
			// sends messages to the players
			for (Player p : pList) {
				OutputQueue.addElement(new WrappedJSONObject(
						ServerMessageBuilder.chatToGame(senderID, msg, gameID),
						getClientHandler(p.getNick())));
			}

			// sends messages to the spectators
			for (Spectator s : sList) {
				OutputQueue.addElement(new WrappedJSONObject(
						ServerMessageBuilder.chatToGame(senderID, msg, gameID),
						getClientHandler(s.getNick())));
			}
		}
	}

	/**
	 * Is invoked when a client made a move.
	 * <p>
	 * Checks whether the move is legal. Sends 'move failed' when the game is not found or the game hasn't been started.
	 * Forwards move request to the logic that checks whether the move is legal.
	 * 
	 * @param gameID
	 *            ID of the game where the move was made
	 * @param position
	 *            Position of the card that is set in this move
	 * @param rotation
	 *            Rotation of the card that should be set
	 * @param placement
	 *            Placement of the meeple that should be set, is -1 if no meeple was set
	 * @param handler
	 *            Reference to the handler which receives the message from the client
	 */
	void move(int gameID, Position position, int rotation, Placement placement,
			ClientHandler handler) {

		ServerGame game = games.get(gameID);

		if (game == null) {
			OutputQueue.addElement(new WrappedJSONObject(
					ServerMessageBuilder.moveFailed(gameID,
							ServerMessageBuilder.createAction(position,
									rotation, placement), Reason.GAMENOTFOUND),
					handler));
		} else if (game.getState().equals(GameStatus.NOTSTARTED)) {
			OutputQueue.addElement(new WrappedJSONObject(ServerMessageBuilder
					.moveFailed(gameID, ServerMessageBuilder.createAction(
							position, rotation, placement),
							Reason.GAMEISNOTYETSTARTED), handler));
		} else {

			// string displays whether the move was legal or not
			String cardPlacing = game.checkCardPlacement(position, rotation,
					placement, handler.getNick());
			if (cardPlacing.equals(Reason.MOVEISILLEGAL.toString())) {

				OutputQueue.addElement(new WrappedJSONObject(
						ServerMessageBuilder.moveFailed(gameID,
								ServerMessageBuilder.createAction(position,
										rotation, placement),
								Reason.MOVEISILLEGAL), handler));
				game.getThread().setMoveFailed(true);
				
			} else if (cardPlacing.equals(Reason.ITSNOTYOURTURN)) {

				OutputQueue.addElement(new WrappedJSONObject(
						ServerMessageBuilder.moveFailed(gameID,
								ServerMessageBuilder.createAction(position,
										rotation, placement),
								Reason.ITSNOTYOURTURN), handler));
				game.getThread().setMoveFailed(true);
				
			} else if (cardPlacing.equals("legal")) { // if legal

				if (handler.getNick().equals(
						game.getThread().getCurrentPlayer().getNick())) {
					game.getThread().setMoveMade(true);
				}

				JSONObject moveMade = null;
				// send move made to all player in this game
				for (Player p : game.getPlayerList().values()) {
					moveMade = ServerMessageBuilder.moveMade(gameID, handler
							.getNick(), game.getCurrentCard(false),
							ServerMessageBuilder.createAction(position,
									rotation, placement));
					OutputQueue.addElement(new WrappedJSONObject(moveMade,
							getClientHandler(p.getNick())));
				}

				// send move made to all spectator in this game
				for (Spectator s : game.getSpectatorList().values()) {
					OutputQueue.addElement(new WrappedJSONObject(
							ServerMessageBuilder.moveMade(gameID, handler
									.getNick(), game.getCurrentCard(false),
									ServerMessageBuilder.createAction(position,
											rotation, placement)),
							getClientHandler(s.getNick())));
				}

				// Add the move to the MoveMadeStorage in order to let
				// spectators watch the game if the join after the game has
				// started
				if (moveMade != null) {
					ServerMoveMadeStorage.addMoveToQueue(gameID, moveMade);
				}

				broadcastToAllIfExtensionFits(game,
						ServerMessageBuilder.gameUpdate(game
								.getGameInformation()));
				game.getCurrentCard(true);

			} else if (cardPlacing.equals("legalButMeeple")) { // legal but no
																// meeple set

				// Placement where no meeple is set
				Placement place = new Placement(-1);
				place.setCommunicationPlacement(-1);

				if (handler.getNick().equals(
						game.getThread().getCurrentPlayer().getNick())) {
					game.getThread().setMoveMade(true);
				}
				JSONObject moveMade = null;
				for (Player p : game.getPlayerList().values()) {
					moveMade = ServerMessageBuilder.moveMade(gameID, handler
							.getNick(), game.getCurrentCard(false),
							ServerMessageBuilder.createAction(position,
									rotation, place));
					OutputQueue.addElement(new WrappedJSONObject(moveMade,
							getClientHandler(p.getNick())));
				}
				for (Spectator s : game.getSpectatorList().values()) {
					OutputQueue.addElement(new WrappedJSONObject(
							ServerMessageBuilder.moveMade(gameID, handler
									.getNick(), game.getCurrentCard(false),
									ServerMessageBuilder.createAction(position,
											rotation, place)),
							getClientHandler(s.getNick())));
				}

				// Add the move to the MoveMadeStorage in order to let
				// spectators watch the game if the join after the game has
				// started
				if (moveMade != null) {
					ServerMoveMadeStorage.addMoveToQueue(gameID, moveMade);
				}
				broadcastToAllIfExtensionFits(game,
						ServerMessageBuilder.gameUpdate(game
								.getGameInformation()));
				game.getCurrentCard(true);
			}
		}
	}

	/**
	 * Sends the final 'game-update' message when the game has ended. Is called by the PlayerThread.
	 * 
	 * @param gameUpdate
	 *            message to be sent
	 * 
	 * @see PlayerThread
	 */
	public void delegateGameUpdateFromPlayerThread(ServerGame game) {
		broadcastToAll(ServerMessageBuilder.gameUpdate(game
				.getGameInformation()));
	}

	/**
	 * Sends a message to a specific user and checks whether the client is able to chat.
	 * 
	 * @param senderID
	 *            Name of the sender
	 * @param msg
	 *            Chat message
	 * @param receiverID
	 *            Name of the receiver
	 * @param handler
	 *            Reference to the handler which receives the message from the client
	 * @param capabilities
	 *            Capabilities of the client
	 */
	void sendMessageToSpecificUser(String senderID, String msg,
			String receiverID, ClientHandler handler,
			Set<CapabilitiesType> capabilities) {
		if (capabilities.contains(CapabilitiesType.CHAT)) {
			// Send to sender
			OutputQueue.addElement(new WrappedJSONObject(ServerMessageBuilder
					.chatToSpecific(senderID, msg, receiverID), handler));

			// Send to receiver
			OutputQueue.addElement(new WrappedJSONObject(ServerMessageBuilder
					.chatToSpecific(senderID, msg, senderID),
					getClientHandler(receiverID)));
		} else {
			OutputQueue.addElement(new WrappedJSONObject(ServerMessageBuilder
					.invalidMessage(Reason.CHATNOTAVAILABLE), handler));
		}
	}

	/**
	 * Broadcasts messages to all receivers
	 * 
	 * @param msg
	 *            sended Message
	 */
	private synchronized void broadcastToAll(JSONObject msg) {
		for (ClientHandler h : connectedHandler.values()) {
			OutputQueue.addElement(new WrappedJSONObject(msg, h));
		}
	}

	/**
	 * Sends a 'game-update' message to all clients if the extensions fit.
	 * 
	 * @param game
	 *            game to check
	 */
	private void broadcastToAllIfExtensionFits(ServerGame game, JSONObject msg) {
		// Send games to the clients correctly
		for (ClientHandler client : connectedHandler.values()) {
			// if the game has no extensions
			if (game.getExtension().isEmpty()) {
				OutputQueue.addElement(new WrappedJSONObject(msg, client));
			} else {
				// does the client support all extensions of the game?
				Set<CapabilitiesType> gameExtensions = game.getExtension();
				Set<CapabilitiesType> clientCapabilities = client
						.getCapabilities();
				if (clientCapabilities.containsAll(gameExtensions)) {
					OutputQueue.addElement(new WrappedJSONObject(msg, client));
				}
			}
		}
	}

	/*
	 * GETTER AND SETTER BELOW
	 */

	public Map<Integer, ServerGame> getGames() {
		return games;
	}

	ServerSocket getListen() {
		return listen;
	}

	Map<String, ClientHandler> getConnectedHandler() {
		return connectedHandler;
	}

	public void invalidFormattedLoginMessage(ClientHandler handler) {
		OutputQueue.addElement(new WrappedJSONObject(ServerMessageBuilder
				.invalidMessage(Reason.INVALIDMESSAGE), handler));
		OutputQueue.addElement(new WrappedJSONObject(ServerMessageBuilder
				.invalidMessage(Reason.LOGINFAILED), handler));
	}
}
