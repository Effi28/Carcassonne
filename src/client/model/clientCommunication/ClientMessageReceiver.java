package client.model.clientCommunication;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import shared.enums.CapabilitiesType;
import shared.enums.GameStatus;
import shared.enums.JsonType;
import shared.enums.SpecialMeepleType;
import shared.model.BuildCard;
import shared.model.Card;
import shared.model.Meeple;
import shared.model.Player;
import shared.model.Position;
import shared.model.Spectator;
import client.model.game.CardReceiver;
import client.model.game.ClientMoveMadeStorage;
import client.view.ChatLobby;

/**
 * Receives the different messages from the server.
 * <p>
 * It receives all messages from the server and extracts the orders out of the json-Object. After that it delegates the
 * commands to the handler and to the model.
 * 
 * @Version 27.11.2013
 * 
 */
public final class ClientMessageReceiver {

	/**
	 * Error logger
	 */
	private static Logger log = LogManager.getLogger("ERROR");

	/**
	 * Info logger
	 */
	private static Logger infoLog = LogManager.getLogger("INFO");

	/**
	 * Reference to the control-class of the client
	 */
	private final ServerHandler handler;
	/**
	 * Formatting of the date for the time stamp of a message
	 */
	private final SimpleDateFormat sdfmt;

	/**
	 * Constructor of the ClientMessageReceiver, it sets the control-reference.
	 * 
	 * @param handler
	 *            Handler-thread which reads the input from the server
	 */
	public ClientMessageReceiver(ServerHandler handler) {
		this.sdfmt = new SimpleDateFormat("hh:mm");
		this.handler = handler;
	}

	/**
	 * Reads the message from the server
	 * <p>
	 * Gets the message from the server and divides it into its parts. After that the different types of messages are
	 * handled differently by using different delegation-methods.
	 */
	public void readMessage() {

		String jsonText;

		try {

			// get the message
			jsonText = handler.getIn().readLine();

			infoLog.info("SERVER ---> CLIENT : " + jsonText);

			JSONObject jsonObject = new JSONObject(jsonText);
			String s = jsonObject.optString("type");

			// gets the type of the json-message for the switch-case-construct
			JsonType t = JsonType.fromString(s);

			// handles the different types of json-messages
			switch (t) {

			case TILEDRAWN:
				tileDrawn(jsonObject);
				break;

			case GAMEUPDATE:
				gameUpdate(jsonObject);
				break;

			case MOVEMADE:
				moveMade(jsonObject);
				break;

			case MOVEFAILED:
				moveFailed(jsonObject);
				break;

			case LOGINSUCCESS:
				loginSuccess(jsonObject);
				break;

			case LOGINFAILED:
				loginFailed(jsonObject);
				break;

			case PLAYERADDED:
				playerAdded(jsonObject);
				break;

			case WATCHGAMESUCCESS:
				watchGameSuccess(jsonObject);
				break;

			case WATCHGAMEFAILED:
				watchGameFailed(jsonObject);
				break;

			case CHAT:
				chat(jsonObject);
				break;

			case GAMECREATED:
				gameCreated(jsonObject);
				break;

			case NEWGAMEFAILED:
				newGameFailed(jsonObject);
				break;

			case ACKNOWLEDGEDISCONNECT:
				acknowledgedDisconnect();
				break;

			case PLAYERREMOVED:
				playerRemoved(jsonObject);
				break;

			case INVALIDMESSAGE:
				invalidMessage(jsonObject);
				break;

			case JOINGAMEFAILED:
				joinGameFailed(jsonObject);
				break;

			case JOINGAMESUCCESS:
				joinGameSuccess(jsonObject);
				break;

			case PLAYERLEFT:
				playerLeft(jsonObject);
				break;

			case GAMEREMOVED:
				gameRemoved(jsonObject);
				break;

			default:
				log.error("No message-case matched");
				throw new IllegalArgumentException("No message-case matched");
			}
		} catch (IOException | JSONException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Handles a 'game removed' message.
	 * 
	 * @param jsonObject
	 *            JSONObject containing the message
	 */
	private void gameRemoved(JSONObject jsonObject) {
		assert jsonObject != null;

		int gameID = jsonObject.optInt("game id");

		assert gameID > 0;

		handler.getControl().deleteGame(gameID);
	}

	/**
	 * Handles a 'move failed' message.
	 * 
	 * @param jsonObject
	 *            JSONObject containing the message
	 */
	private void moveFailed(JSONObject jsonObject) {
		assert jsonObject != null;

		int gameID = jsonObject.optInt("game id");
		String reason = jsonObject.optString("reason");
		JSONObject action = jsonObject.optJSONObject("action");

		int x = action.optInt("x");
		int y = action.optInt("y");
		int rotation = action.optInt("rotation");

		// checks the Placement of a meeple, if no placement is send the String
		// will be null, otherwise it will be the int
		String placementTemp = action.optString("placement");

		int placement;

		if (placementTemp.equals("") || placementTemp.equals("-1")) {
			placement = -1;
		} else {
			placement = Integer.parseInt(placementTemp);
		}

		handler.getControl().moveFailed(gameID, new Position(x, y), rotation,
				placement, reason);

	}

	/**
	 * Handles a 'move made' message.
	 * 
	 * @param jsonObject
	 *            JSONObject containing the message
	 */
	private void moveMade(JSONObject jsonObject) {

		JSONObject card = jsonObject.optJSONObject("tile");
		int gameID = jsonObject.optInt("game id");
		String moveMadeNick = jsonObject.optString("nick");
		JSONObject action = jsonObject.optJSONObject("action");

		assert !moveMadeNick.equals("");

		String cardID = CardReceiver.receiveCard(card);

		assert !cardID.equals("");

		int x = action.optInt("x");
		int y = action.optInt("y");
		int rotation = action.optInt("rotation");
		String specialMeeple = action.optString("special meeple");

		int sentCardRotation = CardReceiver.getRotation(cardID, card);
		Card builtCard = BuildCard.buildCard(card);

		if (sentCardRotation == 0 && rotation != 0) {
			builtCard.rotate(rotation);
		}
		System.out.println("Cardrotation: " +sentCardRotation + " rotation: " + rotation);

		// checks the Placement of a meeple if no placement is send the String
		// will be null
		// otherwise it will be the int
		String placementTemp = action.optString("placement");

		int placement;

		if (placementTemp.equals("") || placementTemp.equals("-1")) {
			placement = -1;
		} else {
			placement = Integer.parseInt(placementTemp);
		}

		assert handler != null;
		ClientMoveMadeStorage.getInstance(handler).addMoveToQueue(jsonObject);
		SpecialMeepleType meeple = null;
		// If a special meeple was sent
		if (!(specialMeeple.equals(""))) {
			meeple = SpecialMeepleType.fromString(specialMeeple);
		}
		
		int rotationInView = (sentCardRotation + rotation)%4;
		
		handler.getControl().moveMade(gameID, cardID, new Position(x, y),
				rotationInView, placement, builtCard, moveMadeNick, meeple);
	}

	/**
	 * Handles a 'tile-drawn' message.
	 * 
	 * @param jsonObject
	 *            JSONObject containing the message
	 */
	private void tileDrawn(JSONObject jsonObject) {

		int gameID = jsonObject.optInt("game id");
		String nick = jsonObject.optString("nick");
		int remainingTime = jsonObject.optInt("timeout");

		remainingTime = (int) (remainingTime - (System.currentTimeMillis() / 1000));
		int remaining = jsonObject.optInt("remaining");
		JSONObject card = jsonObject.optJSONObject("tile");
		String cardID = CardReceiver.receiveCard(card);
		int rotation = CardReceiver.getRotation(cardID, card);

		assert gameID > 0;
		assert nick != null;
		assert cardID != null;
		assert rotation >= 0 && rotation < 5;

		handler.getControl().tileDrawn(nick, cardID, BuildCard.buildCard(card),
				gameID, rotation, remaining, remainingTime);
	}

	/**
	 * Handles a 'player left' message.
	 * 
	 * @param jsonObject
	 *            JSONObject containing the message
	 */
	private void playerLeft(JSONObject jsonObject) {
		int gameID = jsonObject.optInt("game id");
		String nick = jsonObject.optString("nick");

		assert gameID > 0;
		assert nick != null;

		handler.getControl().playerLeft(gameID, nick);
	}

	/**
	 * Handles a 'watch game failed' message.
	 * 
	 * @param jsonObject
	 *            JSONObject containing the message
	 */
	private void watchGameFailed(JSONObject jsonObject) {
		String reason = jsonObject.optString("reason");
		int gameID = jsonObject.optInt("game id");

		handler.getControl().sendGameWatchingFailed(reason, gameID);
	}

	/**
	 * Handles a 'watch game success' message
	 * 
	 * @param jsonObject
	 *            JSONObject containing the message
	 */
	private void watchGameSuccess(JSONObject jsonObject) {
		int gameID = jsonObject.optInt("game id");

		handler.getControl().sendGameWatchingSuccess(gameID);
	}

	/**
	 * Performs the execution of the gameUpdate message.
	 * <p>
	 * The state of the game, the playerlist and the spectatorlist are updated so far.
	 * 
	 * @param jsonObject
	 *            information about the game
	 */
	private void gameUpdate(JSONObject jsonObject) {

		// Gets the JSONObject with all game information from the message
		JSONObject game = jsonObject.optJSONObject("game");
		GameStatus state = GameStatus.fromString(game.optString("state"));
		JSONArray playerArray = game.optJSONArray("player array");
		JSONArray spectatorArray = game.optJSONArray("spectator array");

		int gameID = game.optInt("game id");
		try {
			handler.getControl().gameUpdate(gameID, state,
					JSONArrToMapPlayer(playerArray),
					JSONArrToMapSpectator(spectatorArray));
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Handles a successful 'join game' message.
	 * 
	 * @param jsonObject
	 *            JSONObject containing the message
	 */
	private void joinGameSuccess(JSONObject jsonObject) {
		handler.getControl().sendGameJoiningSucceded();
	}

	/**
	 * Handles a 'join game failed' message.
	 * 
	 * @param jsonObject
	 *            JSONObject containing the message
	 */
	private void joinGameFailed(JSONObject jsonObject) {

		String reason = jsonObject.optString("reason");
		handler.getControl().sendGameJoiningFailed(reason);
	}

	/**
	 * Handles the event that the creation of a game failed
	 * 
	 * @param jsonObject
	 *            message received from the server
	 */
	private void newGameFailed(JSONObject jsonObject) {
		String reason = jsonObject.optString("reason");
		handler.getControl().sendGameCreationFailed(reason);
	}

	/**
	 * Creates a new game when the message arrives at the client
	 * 
	 * @param json
	 *            message received from the server
	 */
	private void gameCreated(JSONObject json) {

		JSONObject game = json.optJSONObject("game");

		int gameID = game.optInt("game id");
		String gameName = game.optString("name");
		JSONArray playerArray = game.optJSONArray("player array");
		JSONArray specArray = game.optJSONArray("spectator array");
		JSONArray extensionsArr = game.optJSONArray("extensions");

		Set<CapabilitiesType> extensions = JSONArrToExtensionsSet(extensionsArr);

		JSONObject host = null;
		try {
			host = playerArray.getJSONObject(0);
		} catch (JSONException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}

		handler.getControl().createNewGame(gameName, gameID,
				host.optString("nick"), JSONArrToMapPlayer(playerArray),
				JSONArrToMapSpectator(specArray), extensions);
	}

	/**
	 * Helper-method converts a JSONArray containing extensions into a Set of CapabilitiesTypes.
	 * 
	 * @param extensionsArr
	 *            JSONArray with all extensions sent in the message
	 * @return Set with all extensions as CapabilitiesType
	 */
	private Set<CapabilitiesType> JSONArrToExtensionsSet(JSONArray extensionsArr) {
		Set<CapabilitiesType> extensions = EnumSet.noneOf(CapabilitiesType.class);

		if (extensionsArr != null) {
			for (int i = 0; i < extensionsArr.length(); i++) {
				extensions.add(CapabilitiesType.fromString(extensionsArr
						.optString(i)));
			}
		}
		return extensions;
	}

	/**
	 * 
	 * Helper method which creates a Map with Key Nick and Value Player from a JSON-Array
	 * 
	 * @param arr
	 *            Array which should be transformed to a Map
	 * @return the Map of Players
	 */
	public static Map<String, Player> JSONArrToMapPlayer(JSONArray arr) {

		Map<String, Player> playerList = new HashMap<>();

		for (int i = 0; i < arr.length(); i++) {
			JSONObject json = arr.optJSONObject(i);
			String nick = json.optString("nick");
			String color = json.optString("color");
			int score = json.optInt("score");
			int remainingMeeples = json.optInt("remaining meeples");
			JSONArray meeplePositions = json
					.optJSONArray("meeple-position array");
			Player u = new Player(nick, color);
			u.setScore(score);
			u.setMeeplesLeft(remainingMeeples);

			for (int j = 0; j < meeplePositions.length(); j++) {
				JSONObject meeplePosition = meeplePositions.optJSONObject(j);
				int x = meeplePosition.optInt("x");
				int y = meeplePosition.optInt("y");
				int placement = meeplePosition.optInt("placement");

				Meeple meeple = new Meeple(new Position(x, y), placement, u,
						null);
				u.addMeeple(meeple);
			}

			playerList.put(u.getNick(), u);
		}
		return playerList;
	}

	/**
	 * 
	 * Helper method which creates a Map with Key Nick and Value Spectator from a JSON-Array
	 * 
	 * @param arr
	 *            Array which should be transformed to a Map
	 * @return the Map of Spectators
	 */
	public static Map<String, Spectator> JSONArrToMapSpectator(JSONArray arr) {
		Map<String, Spectator> spectatorList = new HashMap<>();

		for (int i = 0; i < arr.length(); i++) {
			Spectator u = new Spectator(arr.optString(i));
			spectatorList.put(u.getNick(), u);
		}
		return spectatorList;
	}

	/**
	 * Adds a player to the list of active users in the ChatLobby and displays a message that the specific user
	 * connected.
	 * 
	 * @param jsonObject
	 *            message received from the server
	 * @see ChatLobby
	 */
	private void playerAdded(JSONObject jsonObject) {
		String nick = jsonObject.optString("nick");
		String message = sdfmt.format(new Date()) + " " + nick
				+ " joined the Chat-Lobby";

		if (!(handler.getControl().getNick().equals(nick))) {
			handler.getControl().getController().getChatLobby()
					.addText(message);
		}

		handler.getControl().addPlayer(nick);
	}

	/**
	 * Sends a Message to the Chat-Lobby that a player left and removes the user from the list of connected users.
	 * 
	 * @param jsonObject
	 *            message received from the server
	 */
	private void playerRemoved(JSONObject jsonObject) {

		String nick = jsonObject.optString("nick");

		String message = sdfmt.format(new Date()) + " " + nick
				+ " left the Chat-Lobby";

		handler.getControl().handlePlayerRemoved(message, nick);

	}

	/**
	 * Closes the streams and the socket, kills the thread. Is called when a client disconnects.
	 */
	private void acknowledgedDisconnect() {
		try {
			handler.getIn().close();
			handler.getOut().close();
			handler.getControl().getClientSocket().close();
			Thread.currentThread().interrupt();
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Sends a message to the view where it is displayed at the chatlobby
	 * 
	 * @param json
	 *            JSONObject that was sent
	 */
	private void chat(JSONObject json) {
		String receiverID = json.optString("receiver id");
		String senderID = json.optString("sender id");
		int gameID = json.optInt("game id");
		String message = json.optString("message");
		String stamp = json.optString("stamp");
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

		long time = Long.valueOf(stamp);
		Date resultdate = new Date(time * 1000);

		String messageToSend = sdf.format(resultdate) + " " + senderID + ": "
				+ message;

		// Handles an incoming specific message --> private chat
		if (!(receiverID.equals(""))) {

			handler.getControl().handleChatMessageSpecific(messageToSend,
					senderID, receiverID);

			// Handles an incoming game message
		} else if (gameID != 0) {
			handler.getControl().handleGameMessage(gameID, messageToSend);
		}
		// Handles an incoming general message
		else {
			handler.getControl().handleChatMessageGeneral(messageToSend);
		}

	}

	/**
	 * Delegates when the login failed so that the client will be informed why the login failed.
	 * 
	 * @param jsonObject
	 *            message received from the server
	 */
	private void loginFailed(JSONObject jsonObject) {

		String reason = jsonObject.optString("reason");
		handler.getControl().loginFailed(reason);
	}

	/**
	 * Is called when a new client connects and contains all information about active users and games.
	 * <p>
	 * Adds all players and games to the collection in the model.
	 * 
	 * @param json
	 *            message received from the client
	 */
	private void loginSuccess(JSONObject json) {

		String nick = json.optString("nick");
		JSONArray nickArr = json.optJSONArray("nick array");
		JSONArray gameArr = json.optJSONArray("game array");
		JSONArray capabilitiesArr = json.optJSONArray("capabilities");

		// Add player currently logged in
		for (int i = 0; i < nickArr.length(); i++) {
			try {
				handler.getControl().addPlayer(nickArr.getString(i));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		// Create list with capabilities
		Set<CapabilitiesType> capabilities = EnumSet.noneOf(CapabilitiesType.class);
		for (int i = 0; i < capabilitiesArr.length(); i++) {
			capabilities.add(CapabilitiesType.fromString(capabilitiesArr
					.optString(i)));
		}

		// Add games
		if (gameArr != null) {
			for (int i = 0; i < gameArr.length(); i++) {
				JSONObject game = null;
				String host = "";

				try {
					game = gameArr.getJSONObject(i);
					host = game.optJSONArray("player array").getJSONObject(0)
							.optString("nick");
				} catch (JSONException e) {
					log.error(e.getMessage());
				}
				int gameID = game.optInt("game id");
				String gameName = game.optString("name");
				JSONArray extensionsArr = game.optJSONArray("extensions");

				Set<CapabilitiesType> extensions = JSONArrToExtensionsSet(extensionsArr);

				handler.getControl().setCapabilities(capabilities);
				handler.getControl().createNewGame(
						gameName,
						gameID,
						host,
						JSONArrToMapPlayer(game.optJSONArray("player array")),
						JSONArrToMapSpectator(game
								.optJSONArray("spectator array")), extensions);
			}
		}
		handler.getControl().addPlayer(nick);
	}

	/**
	 * Prints out that a specific type of message doesn't exist so it's an invalid message.
	 * 
	 * @param jsonObject
	 *            JSONObject which didn't match
	 */
	private void invalidMessage(JSONObject jsonObject) {
		infoLog.info("Falsche Nachricht von Server");
		infoLog.info(jsonObject.toString());
	}
}
