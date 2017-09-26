package client.model.clientCommunication;

import java.net.Socket;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Set;
import java.util.TreeSet;

import javafx.application.Platform;
import javafx.stage.Stage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;

import shared.enums.CapabilitiesType;
import shared.enums.GameStatus;
import shared.enums.JsonType;
import shared.enums.SpecialMeepleType;
import shared.model.Card;
import shared.model.Failure;
import shared.model.Meeple;
import shared.model.Placement;
import shared.model.Player;
import shared.model.Position;
import shared.model.Spectator;
import shared.view.ErrorView;
import ai.PossiblePlacement;
import client.controller.CarcassonneController;
import client.model.game.ClientGame;
import client.model.game.ClientMoveMadeStorage;
import client.view.ChatLobby;
import client.view.GameView;
import client.view.ObserverView;
import client.view.PrivateTab;

/**
 * Main control for the client. This is the the central point for the communication of the whole client.
 * <p/>
 * Delegates information information received from the server to the view and the client model. Manages all client
 * related classes. It also contains general information about this client.
 * <p/>
 * Implements the SINGLETON-Pattern, because there should only be one instance of this class for one client in order to
 * have exactly one controlling instance.
 * 
 * @version 06.01.2014
 * @see CarcassonneController
 * @see ChatLobby
 * @see ServerHandler
 */
public final class ClientControl extends Observable {

	/**
	 * Error logger
	 */
	private static Logger log = LogManager.getLogger("ERROR");

	/**
	 * Represents the nickname of the client
	 */
	private String nick;

	/**
	 * Shows the capabilities of the client.
	 */
	private Set<CapabilitiesType> capabilities;

	/**
	 * Socket for the communication between client and server
	 */
	private Socket clientSocket;
	/**
	 * Handler waits for incoming messages from the server and sends out messages to the server
	 */
	private ServerHandler handler;
	/**
	 * Reference to the controller which provides the connection between model and view
	 */
	private final CarcassonneController controller;
	/**
	 * Instance of the ClientControl because its a Singleton
	 */
	private static ClientControl instance;
	/**
	 * Set with all players currently connected
	 */
	private final Set<String> playerList;

	private Set<Position> meeplePositions;

	private Set<Position> deletedMeeples;
	/**
	 * Set with all games currently created
	 */
	private Map<Integer, ClientGame> games;

	/**
	 * Standard and only private constructor because its SINGLETON
	 * 
	 * @param controller
	 *            reference to the controller for MVC-Pattern
	 * @param adress
	 *            adress of the server
	 * @param port
	 *            port of the Server
	 */
	private ClientControl(CarcassonneController controller, String adress,
			int port) {

		this.controller = controller;
		try {
			clientSocket = new Socket(adress, port);
		} catch (Exception e) {
			new ErrorView(e.getMessage());
			log.error(e.getMessage());
		}

		// Start the ServerHandler in order to get message from the server
		handler = ServerHandler.getInstance(this);
		handler.setDaemon(true);
		handler.start();

		// initialize variables
		games = new HashMap<>();
		playerList = new TreeSet<>();
		meeplePositions = new HashSet<>();
		deletedMeeples = new HashSet<>();

		capabilities = EnumSet.of(CapabilitiesType.CHAT,
				CapabilitiesType.BIGMEEPLE, CapabilitiesType.BISHOP,
				CapabilitiesType.INNS);

	}

	/**
	 * Returns the instance, because of SINGLETON
	 * 
	 * @param controller
	 *            controller between model and view of the client, MVC-Pattern
	 * @param address
	 *            address of the server
	 * @param port
	 *            port of the server
	 * @return instance of the ClientControl
	 */
	public synchronized static ClientControl getInstance(
			CarcassonneController controller, String address, int port) {
		if (instance == null) {
			instance = new ClientControl(controller, address, port);
		}
		return instance;
	}

	/**
	 * Sets the nick and the capabilities of the current user.
	 * 
	 * @param nick
	 *            name of the user of this client
	 */
	public void setupClient(String nick) {
		this.nick = nick;

		JSONArray strCapa = new JSONArray();
		for (CapabilitiesType t : capabilities) {
			strCapa.put(t.toString());
		}
		handler.getSender().sendLoginMessage(nick, strCapa);
	}

	/**
	 * Notifies the observers when the login failed so that an error can be displayed
	 * 
	 * @param text
	 *            why the login failed
	 */
	public void loginFailed(String text) {
		setChanged();
		notifyObservers(new Failure(text));
	}

	/**
	 * Adds a player to the set where all connected players are saved. Notifies the observers if the adding was correct.
	 * 
	 * @param player
	 *            nick of the player who joined
	 * @return was the adding successful?
	 */
	public boolean addPlayer(String player) {
		if (playerList.add(player)) {
			setChanged();
			notifyObservers(JsonType.LOGINSUCCESS);
			setChanged();
			notifyObservers(JsonType.PLAYERADDED);
			return true;
		}
		return false;
	}

	/**
	 * Is called when the client receives a 'game-update' message.
	 * <p/>
	 * Changes the information about a game and notifies observers that a 'game-update' method was received. Performs
	 * all changes that are indicated by a newly received 'game-update' message.
	 * 
	 * @param gameID
	 *            gameID of the game
	 * @param state
	 *            state of the game
	 * @param playerListP
	 *            Map of all players
	 * @param spectatorListP
	 *            Map of all spectators
	 */
	void gameUpdate(int gameID, GameStatus state,
			Map<String, Player> playerListP,
			Map<String, Spectator> spectatorListP) {

		ClientGame game = games.get(gameID);
		// Checks whether to start a view or not depending on the gameState of
		// the game and the state delivered by this message
		boolean startView = game.getState().equals(GameStatus.NOTSTARTED)
				&& state.equals(GameStatus.ONGOING);

		Map<String, Player> playerList = game.getPlayerList();
		Map<String, Spectator> spectatorList = spectatorListP;

		game.setSpectatorList(spectatorList);

		// starts GameView for players
		if (playerList.containsKey(nick) && startView) {
			final Player player = playerList.get(nick);
			startGameView(game.getGameID(), player, playerList);
		}

		// starts GameView for spectators
		if (spectatorList.containsKey(nick) && startView) {
			final Spectator spectator = spectatorList.get(nick);
			startSpectatorView(game.getGameID(), spectator);
		}

		// Updates the playerlist
		game.setPlayerList(playerListP);

		// Sets the new meeple for each player
		Set<Position> currentMeeplePositions = new HashSet<>();
		for (Player p : game.getPlayerList().values()) {
			for (Meeple m : p.getMeeplesSet()) {
				currentMeeplePositions.add(m.getPosition());
			}
		}

		// If a meeple was deleted send to controller that a meeple was deleted
		deletedMeeples = new HashSet<>();
		for (Position pos : meeplePositions) {
			if (!currentMeeplePositions.contains(pos)) {
				deletedMeeples.add(pos);
			}
		}

		for (Position position : deletedMeeples) {
			meeplePositions.remove(position);
		}

		// Update the state of the game
		game.setState(state);

		setChanged();
		notifyObservers(JsonType.GAMEUPDATE);
	}

	public Set<Position> getDeletedMeeples() {
		return deletedMeeples;
	}

	/**
	 * Creates a new game.
	 * <p/>
	 * When a new JSON-Message is delivered which says that a new game was created this method executes the command for
	 * the client and creates a new game with the given parameters. Notifies the observers that a new game was created.
	 * 
	 * @param gameName
	 *            name of the game created.
	 * @param gameID
	 *            ID of the newly created game.
	 * @param host
	 *            name of the host who created the new game.
	 * @param playerList
	 *            List of all players which are joined the game.
	 * @param specList
	 *            List of all spectators which are observing the game.
	 * @param extensions
	 *            Extensions the new game has
	 */
	void createNewGame(String gameName, int gameID, String host,
			Map<String, Player> playerList, Map<String, Spectator> specList,
			Set<CapabilitiesType> extensions) {

		// Create a new game for the client and add it to the current games
		ClientGame game = new ClientGame(gameName, gameID, host, playerList,
				specList, extensions);
		games.put(gameID, game);

		setChanged();
		notifyObservers(JsonType.NEWGAME);
	}

	/**
	 * Removes a game.
	 * <p/>
	 * Removes the game with the given ID from the list saved in the client. Notifies the observers that a game was
	 * removed.
	 * 
	 * @param gameID
	 *            ID of the game that is going to be removed.
	 */
	void deleteGame(int gameID) {
		if (games.containsKey(gameID)) {
			games.remove(gameID);
			setChanged();
			notifyObservers(JsonType.GAMEREMOVED);
		}
	}

	/**
	 * Removes a player.
	 * <p/>
	 * The player with the given nick is deleted from the playerlist of the game with the given ID. Notifies the
	 * observers that a player has left.
	 * 
	 * @param gameID
	 *            ID of the game the player left
	 * @param nick
	 *            Nickname of the player that left
	 */
	void playerLeft(int gameID, String nick) {
		games.get(gameID).getPlayerList().remove(nick);

		setChanged();
		notifyObservers(JsonType.PLAYERLEFT);
	}

	/**
	 * Notifies the observers that the creation of the game was not successful.
	 * <p/>
	 * Notifies the observers so that the error message can be displayed and the user is informed. The error message is
	 * displayed with the Failure View.
	 * 
	 * @param reason
	 *            why the gamecreation failed
	 * @see Failure
	 */
	void sendGameCreationFailed(String reason) {
		setChanged();
		notifyObservers(new Failure(reason));
	}

	/**
	 * Notifies the view that the watching of a game failed.
	 * <p/>
	 * Sends a message to the observers that the watching of the game failed and the user is informed with an
	 * ObserverErrorView.
	 * 
	 * @param reason
	 *            why the watching failed
	 * @param gameID
	 *            ID of the game which failed to observe
	 */
	void sendGameWatchingFailed(String reason, int gameID) {
		setChanged();
		notifyObservers(new Failure(reason));
	}

	/**
	 * Notifies the view that the joining of a game was successful
	 */
	void sendGameJoiningSucceded() {
		setChanged();
		notifyObservers(JsonType.JOINGAMESUCCESS);
	}

	/**
	 * Notifies the view that the joining of a game failed.
	 * 
	 * @param reason
	 *            why it failed to join
	 */
	void sendGameJoiningFailed(String reason) {
		setChanged();
		notifyObservers(new Failure(reason));
	}

	/**
	 * Delegates the logout message to the sender which sends it to the server.
	 */
	public void doLogout() {
		handler.getSender().sendLogoutMessage();
	}

	/**
	 * Sends a message to the server that a game was started.
	 * 
	 * @param gameID
	 *            ID of the game that has been started
	 */
	public void startGame(int gameID) {
		handler.getSender().sendStartGameMessage(gameID);
	}

	/**
	 * Sends a message to the server that the user wants to observe a game.
	 * 
	 * @param gameID
	 *            ID of the game the user wants to observe
	 */
	public void watchGame(int gameID) {
		handler.getSender().sendWatchMessage(gameID);
	}

	/**
	 * Sends a message to the server that the user wants to join a game.
	 * 
	 * @param gameID
	 *            ID of the game the user wants to join
	 * @param color
	 *            color the user chose
	 */
	public void setupJoin(int gameID, String color) {
		handler.getSender().sendJoinMessage(gameID, color);
	}

	/**
	 * Sends a message from this client to all connected clients.
	 * 
	 * @param msg
	 *            message to be sent
	 */
	public void sendMessage(String msg) {
		handler.getSender().sendChatMessageGeneral(msg, nick);
	}

	/**
	 * Sends a message to the game with gameID.
	 * 
	 * @param msg
	 *            message to be sent
	 * @param gameID
	 *            ID of the game where the message should be displayed
	 */
	public void sendMessage(String msg, int gameID) {
		handler.getSender().sendChatMessage(nick, msg, gameID);
	}

	/**
	 * Sends a message to the server that this client wants to leave the game. Notifies the observers about the leaving
	 * of the game.
	 * 
	 * @param gameID
	 *            ID of the client wants to leave
	 */
	public void leaveGame(int gameID) {
		setChanged();
		notifyObservers(JsonType.LEAVEGAME);
		handler.getSender().leaveGame(gameID);
	}

	/**
	 * Is called when a client receives a private chat message. Creates a new privateChat tab with the message.
	 * 
	 * @param message
	 *            Message to be displayed
	 * @param senderID
	 *            ID of the sender of this private message
	 * @param receiverID
	 *            ID of the receiver of this private message
	 */
	public void handleChatMessageSpecific(String message, String senderID,
			String receiverID) {

		Iterator<PrivateTab> i = handler.getControl().getController()
				.getChatLobby().getTabSet().iterator();
		PrivateTab tab = null;

		while (i.hasNext()) {
			tab = i.next();
			if (tab.getText().equals(receiverID)) {
				break;
			}
		}

		if (tab == null) {
			tab = handler.getControl().getController().getChatLobby()
					.createNewPrivateChatTab((senderID));

		}
		tab.addText(message);
	}

	/**
	 * Is called in order to display a game message in the gameView.
	 * 
	 * @param gameID
	 *            ID of the game where the message was sent
	 * @param messageToSend
	 *            message that should be displayed
	 */
	public void handleGameMessage(int gameID, String messageToSend) {
		controller.delegateGameMessage(gameID, messageToSend);
	}

	/**
	 * Delegates the 'move made' message when the client receives such a message
	 * 
	 * @param gameID
	 *            ID of the game where the move was made
	 * @param cardID
	 *            ID of the cardPicture that was set
	 * @param position
	 *            position of the card that was set
	 * @param rotation
	 *            rotation of the card
	 * @param placement
	 *            placement of the meeple that was set (optional) if no meeple was set is -1
	 * @param tile
	 *            current card
	 * @param moveMadeNick
	 *            Nick of the player wo made the move
	 * @param specialMeeple
	 *            Type of the special meeple
	 */
	public void moveMade(int gameID, String cardID, Position position,
			int rotation, int placement, Card tile, String moveMadeNick,
			SpecialMeepleType specialMeeple) {

		ClientGame game = games.get(gameID);
		int area = game.getMeepleLocation(tile, placement);
		Placement place = new Placement(area);
		place.setCommunicationPlacement(placement);
		place.setSpecialMeeple(specialMeeple);

		if (game.getPlayerList().containsKey(nick)) {

			controller.moveMade(gameID, cardID, position, rotation, place,
					moveMadeNick);
			game.setCardOnPosition(position, rotation, tile);

		} else if (game.getSpectatorList().containsKey(nick)) {

			controller.moveMadeSpectator(gameID, cardID, position, rotation,
					place, moveMadeNick, new Spectator(nick), games.get(gameID)
							.getPlayerList().keySet());
			game.setCardOnPosition(position, rotation, tile);
		}

		if (placement != -1) {
			meeplePositions.add(position);
		}
	}

	/**
	 * Is called when a 'player removed' message is received. Removes the player that has left from the playerlist.
	 * 
	 * @param message
	 *            to be displayed in the chatlobby
	 * @param nick
	 *            of the leaving player
	 */
	public void handlePlayerRemoved(String message, String nick) {
		controller.handlePlayerRemoved(message);

		for (ClientGame c : games.values())

		// removes the nick of the leaving player if he has joined a game
		{
			if (c.getPlayerList().containsKey(nick)) {
				c.getPlayerList().remove(nick);

				// removes the game if the host is the leaving player
				if (c.getHost().equals(nick)) {
					games.remove(c);
					setChanged();
					notifyObservers(JsonType.GAMEREMOVED);
				}
				setChanged();
				notifyObservers(JsonType.PLAYERLEFT);
			}
		}
		playerList.remove(nick);
		setChanged();
		notifyObservers(JsonType.PLAYERREMOVED);
	}

	/**
	 * Notifies the observers that the watching of a game is successful
	 * 
	 * @param gameID
	 *            ID of the game the watching was successful
	 */
	public void sendGameWatchingSuccess(int gameID) {

		ClientGame game = games.get(gameID);
		if (game.getState().equals(GameStatus.ONGOING)) {
			startSpectatorView(gameID, new Spectator(nick));
		}

		setChanged();
		notifyObservers(JsonType.WATCHGAMESUCCESS);
	}

	/**
	 * Deletes the card that was set in the client because it was an illegal move. Starts a new JavaFX-Thread in order
	 * to display in the view that the move was illega.
	 * 
	 * @param gameID
	 *            ID of the game the illegal move was made
	 * @param position
	 *            Position where the illegal card was placed
	 * @param rotation
	 *            Rotation of the card that was illegally placed
	 * @param placement
	 *            Position of the meeple on the card
	 * @param reason
	 *            why the move was illegal
	 */
	public void moveFailed(int gameID, Position position, int rotation,
			int placement, final String reason) {
		// delete card if move failed
		games.get(gameID).getGameField().deleteCard(position);

		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				controller.moveFailed(reason);
			}
		});
		Platform.setImplicitExit(true);
	}

	/**
	 * Delegates the tileDrawn message to the controller so that the card will be displayed in the view. Starts a new
	 * JavaFX-Thread in order to change some view components.
	 * 
	 * @param nick
	 *            Nick of the Player who is allowed to place a Card
	 * @param cardID
	 *            ID of the Card to place
	 * @param card
	 *            card to place
	 * @param gameID
	 *            gameID of the game
	 * @param rotation
	 *            rotation of the Card sent by the server
	 * @param remaining
	 *            tiles
	 * @param remainingTime
	 *            Time the Player has to make a move
	 */
	public void tileDrawn(final String nick, final String cardID,
			final Card card, final int gameID, final int rotation,
			final int remaining, final int remainingTime) {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {

				ClientGame game = games.get(gameID);

				game.setCurrentCard(card);

				Iterator<PossiblePlacement> iterator = games.get(gameID)
						.checkForLegalPlacements(card).iterator();
				Set<Position> possiblePositions = new HashSet<>();

				while (iterator.hasNext()) {
					possiblePositions.add(iterator.next().getPos());
				}

				if (game.getPlayerList().containsKey(getNick())) {
					controller.tileDrawnPlayer(nick, cardID, rotation,
							remaining, remainingTime, possiblePositions);
				} else if (game.getSpectatorList().containsKey(getNick())) {
					controller.tileDrawnSpectator(nick, cardID, rotation,
							remaining, remainingTime);
				}
			}
		});
		Platform.setImplicitExit(true);

		setChanged();
		notifyObservers(JsonType.GAMEUPDATE);
	}

	/**
	 * Delegates a move made by the client to the sender which will send the move to the server.
	 * 
	 * @param gameID
	 *            ID of the game where the move was made
	 * @param p
	 *            Position of the card
	 * @param rotation
	 *            Rotation of the card
	 * @param currentPlacement
	 *            Placement of the meeple
	 */
	public void delegateMove(int gameID, Position p, int rotation,
			Placement currentPlacement) {

		handler.getSender().sendMove(gameID, p, rotation, currentPlacement);
	}

	/**
	 * Writes a message in the Chat-Lobby
	 * 
	 * @param messageToSend
	 *            message to be displayed
	 */
	public void handleChatMessageGeneral(String messageToSend) {
		controller.writeChatMessageGeneral(messageToSend);

	}

	/**
	 * Sends a message to the server to see whether the creation of a new game is legal.
	 * 
	 * @param gameName
	 *            Name of the game that should be created
	 * @param color
	 *            Color of the host
	 * @param turntime
	 *            time to make a move
	 * @param extension
	 *            extension selected for the game
	 */
	public void checkNewGameCreation(String gameName, String color,
			int turntime, List<CapabilitiesType> extension) {
		handler.getSender().sendNewGameCreatedMessage(gameName, color,
				turntime, extension);
	}

	/**
	 * Sets a card in the gameField of the client
	 * 
	 * @param gameID
	 *            ID of the game where the card should be set
	 * @param p
	 *            Position of the card
	 * @param rotation
	 *            Rotation of the card
	 */
	public void setCard(int gameID, Position p, int rotation) {
		ClientGame game = games.get(gameID);
		game.getGameField().addCard(p, game.getCurrentCard());
	}

	/**
	 * Starts a SpectatorView, is called when a game state has changed to ONGOING.
	 * <p/>
	 * Starts a new JavaFX-Thread in order to start the SpectatorView. and notifies the observers about the start of the
	 * game.
	 * 
	 * @param spectator
	 *            spectator who wants to observe a game
	 * @param gameID
	 *            ID of the game to be started
	 */
	private void startSpectatorView(final int gameID, final Spectator spectator) {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				ObserverView observerView = ObserverView.getInstance(gameID,
						controller, spectator, games.get(gameID)
								.getPlayerList().keySet(),
						ClientMoveMadeStorage.getInstance(handler));

				controller.setObserveView(observerView);
				try {
					observerView.start(new Stage());

				} catch (Exception e) {
					e.printStackTrace();
					log.error(e.getMessage());
				}
			}
		});
		Platform.setImplicitExit(true);
		setChanged();
		notifyObservers(JsonType.STARTGAME);
	}

	/**
	 * Starts a GameView, is called when a GameStatus has changed to ONGOING
	 * <p/>
	 * Starts a new JavaFX-Thread in order to start the GameView and notifies the observers about the GameStatus that
	 * has started.
	 * 
	 * @param player
	 *            Player which wants to play a Game
	 * @param gameID
	 *            ID of the game to be started
	 * @param playerList2
	 * @see Player
	 * @see GameView
	 * @see GameStatus
	 */
	private void startGameView(final int gameID, final Player player,
			Map<String, Player> playerList2) {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {

				GameView gameView = GameView.getInstance(gameID, controller,
						player, playerList);

				controller.setGameView(gameView);
				try {
					gameView.start(new Stage());

				} catch (Exception e) {
					e.printStackTrace();
					log.error(e.getMessage());
				}
			}
		});
		Platform.setImplicitExit(true);
		setChanged();
		notifyObservers(JsonType.STARTGAME);
	}

	/*
	 * GETTER AND SETTER BELOW
	 */

	public Map<Integer, ClientGame> getGames() {
		return games;
	}

	public Socket getClientSocket() {
		if (clientSocket == null) {
			log.error("CLIENTSOCKET NULL");
			throw new NullPointerException("CLIENTSOCKET NULL");
		}
		return clientSocket;
	}

	public String getNick() {
		if (nick == null) {
			log.error("NICK NULL");
			throw new NullPointerException("NICK NULL");
		}
		return nick;
	}

	public Set<CapabilitiesType> getCapabilities() {
		return capabilities;
	}

	public ServerHandler getHandler() {
		if (handler == null) {
			log.error("SERVERHANDLER NULL");
			throw new NullPointerException("SERVERHANDLER NULL");
		}
		return handler;
	}

	public CarcassonneController getController() {
		if (controller == null) {
			log.error("CONTROLLER NULL");
			throw new NullPointerException("CONTROLLER NULL");
		}
		return controller;
	}

	public Set<String> getPlayerList() {
		return playerList;
	}

	public void setCapabilities(Set<CapabilitiesType> capabilities) {
		this.capabilities = capabilities;
	}
}
