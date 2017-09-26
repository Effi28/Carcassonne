package server.model.serverCommunication.execution;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import server.model.game.ServerGame;
import server.model.serverCommunication.clientHandling.ClientHandler;
import server.model.serverCommunication.queues.InputQueue;
import server.model.serverCommunication.queues.OutputQueue;
import server.model.serverCommunication.utility.ServerMessageBuilder;
import server.model.serverCommunication.utility.WrappedJSONObject;
import shared.enums.CapabilitiesType;
import shared.enums.JsonType;
import shared.enums.SpecialMeepleType;
import shared.model.Placement;
import shared.model.Player;
import shared.model.Position;

/**
 * Provides the unwrapping of the messages contained by the MoveQueue.
 * <p>
 * Provides methods for unwrapping the messages stored in the BlockingQueue in MoveQueue. After unwrapping each message
 * a specific method for the type of messages is invoked. This specific method call other methods in ServerControl which
 * perform some calculations. All methods in this class are invoked by the Executer of ServerControl.
 * 
 * This class implements the Singleton-Pattern.
 * 
 * @version 29.01.14
 * @see InputQueue
 * @see ServerControl
 * @see Executor
 * @see BlockingQueue
 */
public final class MessageClassification {

	/**
	 * User logger
	 */
	private static Logger userLog = LogManager.getLogger("USER");
	/**
	 * Instance of this class because of the Singleton Pattern.
	 */
	private static MessageClassification instance;
	/**
	 * Reference to the ServerControl which performs all calculations.
	 */
	private final ServerControl control;

	/**
	 * Only constructor of this class. Should only be invoked by the getInstance() method because this class implements
	 * the Singleton Pattern.
	 * 
	 * @param control
	 *            Reference to ServerControl
	 */
	private MessageClassification(ServerControl control) {
		this.control = control;
	}

	/**
	 * Returns the instance of this class.
	 * 
	 * @param control
	 *            Reference to the ServerControl
	 * @return instance of this class
	 */
	public static MessageClassification getInstance(ServerControl control) {
		if (instance == null) {
			instance = new MessageClassification(control);
		}
		return instance;
	}

	/**
	 * Takes the messages of the BlockingQueue and unwraps them. After that they will be classified by their type and a
	 * specific method is invoked which handles the message.
	 */
	public void performMessageClassification() {
		WrappedJSONObject message = InputQueue.getNextElement();

		JSONObject json = message.getJSONObject();
		String s = json.optString("type");
		if (s.equals("")) {
			invalidFormattedMessage(message.getHandler());
			return;
		}

		JsonType t = JsonType.fromString(s);

		switch (t) {

		case LOGIN:
			login(message);
			break;

		case CHAT:
			chat(message);
			break;

		case MOVE:
			move(message);
			break;

		case NEWGAME:
			newGame(message);
			break;

		case JOINGAME:
			joinGame(message);
			break;

		case LEAVEGAME:
			leaveGame(message);
			break;

		case WATCHGAME:
			watchGame(message);
			break;

		case STARTGAME:
			startGame(message);
			break;

		case DISCONNECT:
			disconnect(message);
			break;

		default:
			noCaseMatched(message);
			break;
		}
	}

	private void noCaseMatched(WrappedJSONObject message) {
		ClientHandler handler = message.getHandler();
		control.noCaseMatched(handler);
	}

	/**
	 * Handles an incoming 'move' message.
	 * 
	 * @param message
	 *            WrappedJSONObject containing the message.
	 */
	private void move(WrappedJSONObject message) {

		JSONObject jsonObject = message.getJSONObject();
		ClientHandler handler = message.getHandler();

		if (jsonObject.length() != 3) {
			invalidFormattedMessage(handler);
			return;
		}

		int gameID = jsonObject.optInt("game id");

		JSONObject action = jsonObject.optJSONObject("action");
		int x = action.optInt("x");
		int y = action.optInt("y");
		int rotation = action.optInt("rotation");
		String tempPlacement = action.optString("placement");
		SpecialMeepleType specialMeeple = SpecialMeepleType.fromString(action
				.optString("special meeple"));

		// checks the Placement of a meeple, if no placement is send the String
		// will be null, otherwise it will be the int
		int placement;
		if (tempPlacement.equals("")) {
			placement = -1;
		} else {
			placement = Integer.parseInt(tempPlacement);
		}
		Placement place = new Placement(-1);
		place.setCommunicationPlacement(placement);
		place.setSpecialMeeple(specialMeeple);

		control.move(gameID, new Position(x, y), rotation, place, handler);
	}

	/**
	 * Handles an incoming leaveGame message. Forwards the leaveGame command to the handler.
	 * 
	 * @param message
	 *            Message
	 */
	private void leaveGame(WrappedJSONObject message) {

		JSONObject jsonObject = message.getJSONObject();
		ClientHandler handler = message.getHandler();

		if (jsonObject.length() != 2) {
			invalidFormattedMessage(handler);
			return;
		}

		int gameID = jsonObject.optInt("game id");

		control.leaveGame(handler.getUser(), gameID);
	}

	/**
	 * Handles an incoming watchGame Message. Forwards the watchGame command to the handler.
	 * 
	 * @param message
	 *            message wrapped in a WrappedJSONObject
	 */
	private void watchGame(WrappedJSONObject message) {

		JSONObject jsonObject = message.getJSONObject();
		ClientHandler handler = message.getHandler();

		if (jsonObject.length() != 2) {
			invalidFormattedMessage(handler);
			return;
		}

		int gameID = jsonObject.optInt("game id");

		control.checkWatchGame(handler, gameID);
	}

	/**
	 * Handles an incoming joinGame Message. Forwards the joinGame command to the handler.
	 * 
	 * @param message
	 *            message wrapped in a WrappedJSONObject
	 */
	private void joinGame(WrappedJSONObject message) {
		JSONObject jsonObject = message.getJSONObject();
		ClientHandler handler = message.getHandler();

		if (jsonObject.length() != 3) {
			invalidFormattedMessage(handler);
			return;
		}

		String color = jsonObject.optString("color");
		int gameID = jsonObject.optInt("game id");
		handler.setUser(new Player(handler.getUser().getNick(), color));

		control.checkJoin(handler, gameID, color);
	}

	/**
	 * Forwards the newGame command to the handler.
	 * 
	 * @param message
	 *            message wrapped in a WrappedJSONObject
	 */
	private void newGame(WrappedJSONObject message) {
		JSONObject jsonObject = message.getJSONObject();
		ClientHandler handler = message.getHandler();

		if (!(jsonObject.length() >= 3 && jsonObject.length() <= 5)) {
			invalidFormattedMessage(handler);
			return;
		}

		String gameName = jsonObject.optString("name");
		String color = jsonObject.optString("color");
		JSONArray extArr = jsonObject.optJSONArray("extensions");
		int turnTime = jsonObject.optInt("turntime");

		Set<CapabilitiesType> extensions = EnumSet
				.noneOf(CapabilitiesType.class);
		if (extArr != null) {
			for (int i = 0; i < extArr.length(); i++) {
				String extension = extArr.optString(i);
				if (!extension.equals("")) {
					extensions.add(CapabilitiesType.fromString(extension));
				}
			}
		}

		control.checkGameCreation(
				new Player(handler.getUser().getNick(), color), gameName,
				color, turnTime, extensions, handler);
	}

	/**
	 * Forwards the start game command to the handler.
	 * 
	 * @param message
	 *            message wrapped in a WrappedJSONObject
	 */
	private void startGame(WrappedJSONObject message) {
		JSONObject jsonObject = message.getJSONObject();
		ClientHandler handler = message.getHandler();

		if (jsonObject.length() != 2) {
			invalidFormattedMessage(handler);
			return;
		}

		int gameID = jsonObject.optInt("game id");

		control.checkGameStart(gameID, handler);
	}

	/**
	 * Forwards the logout request to the handler.
	 * 
	 * @param message
	 *            message wrapped in a WrappedJSONObject
	 */
	private void disconnect(WrappedJSONObject message) {

		ClientHandler handler = message.getHandler();

		// Sends messages to all other clients that a User logged out
		control.broadcast(handler,
				ServerMessageBuilder.playerRemoved(handler.getNick()));

		// Sends a message to the client which is logging out
		OutputQueue.addElement(new WrappedJSONObject(ServerMessageBuilder
				.userDisconnected(), handler));

		userLog.info(handler.getNick() + " logged out.");

		// Removes the Client from the connectedHandler List
		control.removeHandler(handler);

		Collection<ServerGame> games = control.getGames().values();
		for (ServerGame g : games) {
			if (g.getPlayerList().containsKey(handler.getNick())) {
				control.leaveGame(handler.getUser(), g.getGameID());
			}
		}

	}

	/**
	 * Delegates the chatMessages if a chatMessage arrives.
	 * <p>
	 * Checks whether a message is to the ChatLobby, to a specific player or to a game.
	 * 
	 * @param message
	 *            message wrapped in a WrappedJSONObject
	 */
	private void chat(WrappedJSONObject message) {

		JSONObject jsonObject = message.getJSONObject();
		ClientHandler handler = message.getHandler();

		if (!(jsonObject.length() >= 4 && jsonObject.length() <= 6)) {
			invalidFormattedMessage(handler);
			return;
		}

		// Get information out of the JSON-Object
		String receiverID = jsonObject.optString("receiver id");
		String msg = jsonObject.optString("message").toString();
		String senderID = jsonObject.optString("sender id");
		int gameID = jsonObject.optInt("game id");

		// Sends messages to all if there is no receiverID and the gameID is 0
		if (receiverID.equals("") && gameID == 0) {
			control.sendChatMessageToAll(senderID, msg,
					handler.getCapabilities(), handler);
		}

		// Sends message to a specific receiver if receiver ID is an empty
		// String
		else if (!(receiverID.equals(""))) {

			// Sends the message to the sender

			// Sends the message to the private Chat receiver
			control.sendMessageToSpecificUser(senderID, msg, receiverID,
					handler, handler.getCapabilities());
		}

		// Sends messages to a specific game
		else if (receiverID.equals("") && gameID != 0) {
			control.sendGameChatMessage(senderID, msg, gameID, handler,
					handler.getCapabilities());
		}
	}

	/**
	 * Handles an incoming login request and delegates it.
	 * 
	 * @param message
	 *            message wrapped in a WrappedJSONObject
	 */
	private void login(WrappedJSONObject message) {

		JSONObject jsonObject = message.getJSONObject();
		ClientHandler handler = message.getHandler();

		if (jsonObject.length() != 3) {
			invalidFormattedLoginMessage(handler);
			return;
		}

		String nick = jsonObject.optString("nick").toString();

		JSONArray capa = jsonObject.optJSONArray("capabilities");
		Set<CapabilitiesType> capabilities = EnumSet.noneOf(CapabilitiesType.class);

		for (int i = 0; i < capa.length(); i++) {
			String capability = capa.optString(i);
			capabilities.add(CapabilitiesType.fromString(capability));
		}
		control.checkLogin(nick, capabilities, handler);
	}

	/**
	 * Handles a false formatted login attempt and kicks the user.
	 * 
	 * @param handler
	 */
	private void invalidFormattedLoginMessage(ClientHandler handler) {
		control.invalidFormattedLoginMessage(handler);
	}

	/**
	 * Delegates to the server that the message that was sent is incorrect and doesn't contain the types that should be
	 * contained.
	 */
	private void invalidFormattedMessage(ClientHandler handler) {
		control.noCaseMatched(handler);
	}
}
