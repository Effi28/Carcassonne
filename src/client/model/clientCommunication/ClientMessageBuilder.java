package client.model.clientCommunication;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import shared.enums.CapabilitiesType;
import shared.enums.JsonType;
import shared.model.Placement;
import shared.model.Position;

/**
 * This utility class creates all messages needed by the client for the
 * Client-/Server-Communication.
 * <p>
 * Provides static methods for all kinds of messages that will be sent to the
 * server. Musn't be initialized because it just provides static builder
 * methods.
 * 
 * @version 27.11.2013
 * 
 */
public final class ClientMessageBuilder {

	
	/**
	 * Error logger
	 */
	private static Logger log = LogManager.getLogger("ERROR");
	
	/**
	 * Only constructor of this class.
	 * Is private and musn't be instantiated because this class is a utility class.
	 * 
	 * @throws AssertionError when constructor is invoked
	 */
	private ClientMessageBuilder() {
		log.error("MessageBuilder was instantiated");
		throw new AssertionError("MessageBuilder was instantiated");
	}

	/**
	 * Helper-method that creates a JSON-Object with a specific type
	 * 
	 * @param type type of the JSON-Object
	 * 
	 * @return returns an object with a specific type
	 */
	private static JSONObject createWithType(JsonType type) {
		JSONObject msg = new JSONObject();
		try {
			msg.put("type", type);
		} catch (JSONException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return msg;
	}

	/**
	 * Writes a startgame-message
	 * 
	 * @param gameID ID of the game to be started
	 * @return json-message
	 */
	public static JSONObject startGame(int gameID) {
		JSONObject msg = createWithType(JsonType.STARTGAME);
		try {
			msg.put("game id", gameID);
		} catch (JSONException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return msg;
	}

	/**
	 * Helper-method that puts a nick to the jsonobject
	 * 
	 * @param msg JSONObject with the type
	 * @param nick nick that should be added to the jsonmessage
	 * @return json message with the nick added
	 */
	private static JSONObject addNick(JSONObject msg, String nick) {
		try {
			msg.put("nick", nick);
		} catch (JSONException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return msg;
	}

	/**
	 * This method writes a loginMessage
	 * 
	 * @param nick Nick of the player logging in
	 * @param capa Capabilities of the client
	 * @return the jsonObject
	 */
	public static JSONObject writeLoginMessage(String nick, JSONArray capa) {

		JSONObject login = new JSONObject();
		login = createWithType(JsonType.LOGIN);
		login = addNick(login, nick);
		try {
			login.put("capabilities", capa);
		} catch (JSONException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return login;
	}

	/**
	 * This method is called by every writeChatMessage method independent if the
	 * message is send to a game, to a specific user, or to all Users.
	 * 
	 * @param senderID name of the sender which sends a chat message
	 * @param msg message content
	 * @return the jsoNObject
	 */
	public static JSONObject writeChatMessageGeneral(String senderID, String msg) {
		JSONObject chat = new JSONObject();

		try {
			chat.put("type", JsonType.CHAT);
			chat.put("sender id", senderID);
			chat.put("stamp", TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
			chat.put("message", msg);
		} catch (JSONException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}

		return chat;
	}

	/**
	 * Writes a message to all users
	 * 
	 * @param SenderID nickname of the sender
	 * @param msg message
	 * @return the jsonObject
	 */
	public static JSONObject writeChatMessage(String SenderID, String msg) {

		JSONObject message = writeChatMessageGeneral(SenderID, msg);
		return message;
	}

	/**
	 * Writes a message to a specific receiver.
	 * 
	 * @param msg message
	 * @param senderID nickname of the sender
	 * @param receiverID nickname of the receiver
	 * @return the jsonObject
	 */
	public static JSONObject writeChatMessage(String msg, String senderID,
			String receiverID) {

		JSONObject message = writeChatMessageGeneral(senderID, msg);
		try {
			message.put("receiver id", receiverID);
		} catch (JSONException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return message;
	}

	/**
	 * Writes a message to a specific gamechat
	 * 
	 * @param SenderID nickname of the sender
	 * @param msg message
	 * @param gameID ID of the game
	 * @return the jsonObject
	 */
	public static JSONObject writeChatMessage(String SenderID, String msg,
			int gameID) {

		JSONObject message = writeChatMessageGeneral(SenderID, msg);
		try {
			message.put("game id", String.valueOf(gameID));
		} catch (JSONException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return message;
	}

	/**
	 * writes a LogoutMessage
	 * 
	 * @return the jsonObject
	 */
	public static JSONObject writeLogoutMessage() {
		JSONObject message = new JSONObject();
		message = createWithType(JsonType.DISCONNECT);

		return message;
	}

	/**
	 * Writes a newGame message
	 * 
	 * @param gameName
	 *            name of the game to be created
	 * @param color
	 *            color of the player
	 * @param extension extension selected for the game
	 * @param turntime available turntime
	 * @return json-message
	 */
	public static JSONObject writeNewGameMessage(String gameName, String color, int turntime, List<CapabilitiesType> extension) {
		JSONObject msg = createWithType(JsonType.NEWGAME);
		JSONArray ext = new JSONArray();
		if(!extension.isEmpty()) {
			for(CapabilitiesType t: extension){
				ext.put(t.toString());
			}
		}
		try {
			msg.put("name", gameName);
			msg.put("color", color.toString());
			msg.put("turntime", turntime);
			if(extension != null && !extension.isEmpty()){
				msg.put("extensions", ext);				
			}
		} catch (JSONException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return msg;
	}

	/**
	 * Creates a JSONObject with the joinmessage
	 * 
	 * @param gameID
	 *            ID of the game the user joined
	 * @param color
	 *            color the user chose
	 * @return message to be sent to the client
	 */
	public static JSONObject writeJoinMessage(int gameID, String color) {
		JSONObject msg = createWithType(JsonType.JOINGAME);

		try {
			msg.put("game id", gameID);
			msg.put("color", color);
		} catch (JSONException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return msg;
	}

	/**
	 * Writes a watch game message
	 * 
	 * @param gameID
	 *            ID of the game to be watched
	 * @return json-message
	 */
	public static JSONObject watchGame(int gameID) {
		JSONObject msg = createWithType(JsonType.WATCHGAME);

		try {
			msg.put("game id", gameID);
		} catch (JSONException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return msg;
	}

	/**
	 * Writes a move message with the information about the move
	 * 
	 * @param gameID
	 *            ID of the game where the move was made
	 * @param move
	 *            information about the move
	 * @return json-message
	 */
	public static JSONObject move(int gameID, JSONObject move) {
		JSONObject msg = createWithType(JsonType.MOVE);

		try {
			msg.put("game id", gameID);
			msg.put("action", move);
		} catch (JSONException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return msg;
	}

	/**
	 * Creates a JSONObject for a 'move' message.
	 * 
	 * @param gameID ID of the game where the move was made
	 * @param p Position of the Card
	 * @param rotation Rotation of the Card
	 * @param currentPlacement Placement of the Meeple, -1 if no Meeple was set
	 * @return JSONObject 'move' message wrapped in a JSONObject
	 */
	public static JSONObject move(int gameID, Position p, int rotation,
			Placement currentPlacement) {
		JSONObject msg = createWithType(JsonType.MOVE);
		JSONObject action = new JSONObject();
		
		try {
			action.put("x", p.getX());
			action.put("y", p.getY());
			action.put("rotation", rotation);

			if (currentPlacement.getCommunicationPlacement() != -1) {
				action.put("placement", currentPlacement.getCommunicationPlacement());
			}
		} catch (JSONException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		try {
			msg.put("game id", gameID);
			if(currentPlacement.getSpecialMeeple() != null){
				action.put("special meeple", currentPlacement.getSpecialMeeple().toString());
			}
			msg.put("action", action);
		} catch (JSONException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return msg;
	}

	/**
	 * Writes a leaveGame message
	 * 
	 * @param gameID
	 *            ID of the game that the user left
	 * @return json-message
	 */
	public static JSONObject leaveGame(int gameID) {
		JSONObject msg = createWithType(JsonType.LEAVEGAME);

		try {
			msg.put("game id", gameID);
		} catch (JSONException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return msg;
	}
}
