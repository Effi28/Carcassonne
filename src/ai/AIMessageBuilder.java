package ai;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import shared.enums.JsonType;
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
public final class AIMessageBuilder {

	// Prevents instantiation of this class because it provides only static
	// utility methods
	private AIMessageBuilder() {
		throw new AssertionError("MessageBuilder was instantiated");
	}

	/**
	 * Helper-method that creates a JSON-Object with a specific type
	 * 
	 * @param type
	 *            type of the JSON-Object
	 * 
	 * @return returns an object with a specific type
	 */
	private static JSONObject createWithType(JsonType type) {
		JSONObject msg = new JSONObject();
		try {
			msg.put("type", type);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return msg;
	}

	/**
	 * Writes a startgame-message
	 * 
	 * @param gameID
	 *            ID of the game to be started
	 * @return json-message
	 */
	public static JSONObject startGame(int gameID) {
		JSONObject msg = createWithType(JsonType.STARTGAME);
		try {
			msg.put("game id", gameID);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return msg;
	}

	/**
	 * Helper-method that puts a nick to the jsonobject
	 * 
	 * @param msg
	 *            JSONObject with the type
	 * @param nick
	 *            nick that should be added to the jsonmessage
	 * @return json message with the nick added
	 */
	private static JSONObject addNick(JSONObject msg, String nick) {
		try {
			msg.put("nick", nick);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return msg;
	}

	/**
	 * This method writes a loginMessage
	 * 
	 * @param nick
	 * @param capa
	 * @return the jsonObject
	 */
	public static JSONObject writeLoginMessage(String nick, JSONArray capa) {

		JSONObject login = new JSONObject();
		login = createWithType(JsonType.LOGIN);
		login = addNick(login, nick);
		try {
			login.put("capabilities", capa);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return login;
	}

	/**
	 * This method is called by every writeChatMessage method independent if the
	 * message is send to a game, to a specific user, or to all Users.
	 * 
	 * @param senderID name of the message sender
	 * @param msg
	 *            message content
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
			e.printStackTrace();
		}

		return chat;
	}

	/**
	 * Writes a message to all users
	 * 
	 * @param SenderID
	 *            nickname of the sender
	 * @param msg
	 *            message
	 * @return
	 */
	public static JSONObject writeChatMessage(String SenderID, String msg) {

		JSONObject message = writeChatMessageGeneral(SenderID, msg);
		return message;
	}

	/**
	 * Writes a message to a specific receiver.
	 * 
	 * @param msg
	 *            message
	 * @param senderID
	 *            nickname of the sender
	 * @param receiverID
	 *            nickname of the receiver
	 * @return the jsonObject
	 */
	public static JSONObject writeChatMessage(String msg, String senderID,
			String receiverID) {

		JSONObject message = writeChatMessageGeneral(senderID, msg);
		try {
			message.put("receiver id", receiverID);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return message;
	}

	/**
	 * Writes a message to a specific gamechat
	 * 
	 * @param SenderID
	 *            nickname of the sender
	 * @param msg
	 *            message
	 * @param gameID
	 *            ID of the game
	 * @return the jsonObject
	 */
	public static JSONObject writeChatMessage(String SenderID, String msg,
			int gameID) {

		JSONObject message = writeChatMessageGeneral(SenderID, msg);
		try {
			message.put("game id", String.valueOf(gameID));
		} catch (JSONException e) {
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
	 * @param extensions 
	 * @param turntime 
	 * @return json-message
	 */
	public static JSONObject writeNewGameMessage(String gameName, String color, int turntime, List<String> extensions) {
		JSONObject msg = createWithType(JsonType.NEWGAME);
		try {
			msg.put("name", gameName);
			msg.put("color", color.toString());
			msg.put("turntime", turntime);
			msg.put("extensions", changeListToJsonArray(extensions));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return msg;
	}

	private static JSONArray changeListToJsonArray(List<String> extensions) {
		JSONArray arr = new JSONArray();
		for(int i = 0; i < extensions.size(); i++){
			arr.put(extensions.get(i));
		}
		return arr;
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
	public synchronized static JSONObject move(int gameID, JSONObject move) {
		JSONObject msg = createWithType(JsonType.MOVE);

		try {
			msg.put("game id", gameID);
			msg.put("action", move);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return msg;
	}

	public static JSONObject move(int gameID, Position p, int rotation,
			int placement) {
		JSONObject msg = createWithType(JsonType.MOVE);
		JSONObject action = new JSONObject();
		try {
			action.put("x", p.getX());
			action.put("y", p.getY());
			action.put("rotation", rotation);

			if (placement == -1) {
			} else {
				action.put("placement", placement);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		try {
			msg.put("game id", gameID);
			msg.put("action", action);
		} catch (JSONException e) {
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
			e.printStackTrace();
		}
		return msg;
	}
}
