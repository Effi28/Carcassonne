package client.model.clientCommunication;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import shared.enums.CapabilitiesType;
import shared.model.Placement;
import shared.model.Position;

/**
 * This class sends messages to the server.
 * <p>
 * It provides methods for every message to be sent to the server. These methods will send specific json-Objects to the
 * server.
 * 
 * @Version 23.01.2014
 * @see ClientMessageReceiver
 * @see ServerHandler
 * @see ClientControl
 */
public final class ClientMessageSender {

	/**
	 * Info logger
	 */
	private static Logger log = LogManager.getLogger("INFO");
	/**
	 * Reference to the handler which controls the communication between server and client
	 */
	private final ServerHandler handler;

	/**
	 * Standard constructor for the ClientMessageSender which takes the ServerHandler as an argument
	 * 
	 * @param handler
	 *            handler that handles the communication between server and this client
	 */
	public ClientMessageSender(ServerHandler handler) {
		this.handler = handler;
	}

	/**
	 * Sends a message to the server that the client wants to connect
	 * 
	 * @param nick
	 *            nick of the client that wants to connect
	 * @param capa
	 *            capabilites of the client that wants to connect
	 */
	public void sendLoginMessage(String nick, JSONArray capa) {
		handler.getSender().sendGeneralMessage(
				ClientMessageBuilder.writeLoginMessage(nick, capa));
	}

	/**
	 * Writes a message to all connected Clients.
	 * 
	 * @param senderID
	 *            name of the sender which sends a chatmessage.
	 * @param msg
	 *            message to be sent
	 */
	public void sendChatMessageGeneral(String msg, String senderID) {
		sendGeneralMessage(ClientMessageBuilder.writeChatMessage(senderID, msg));
	}

	/**
	 * Writes a message to a specific game
	 * 
	 * @param senderID
	 *            id of the client which sends a message
	 * 
	 * @param msg
	 *            message to be displayed
	 * 
	 * @param gameID
	 *            gameID of the game which the message will get
	 */
	public void sendChatMessage(String senderID, String msg, int gameID) {
		sendGeneralMessage(ClientMessageBuilder.writeChatMessage(senderID, msg,
				gameID));
	}

	/**
	 * Writes a message to a specific user
	 * 
	 * @param msg
	 *            message
	 * @param receiver
	 *            user who should receive the message
	 */
	public void sendChatMessage(String msg, String receiver) {
		sendGeneralMessage(ClientMessageBuilder.writeChatMessage(msg, handler
				.getControl().getNick(), receiver));
	}

	/**
	 * Writes a logout Message
	 * 
	 */
	public void sendLogoutMessage() {
		sendGeneralMessage(ClientMessageBuilder.writeLogoutMessage());
	}

	/**
	 * Sends a 'game created' message to the server
	 * 
	 * @param gameName
	 *            name of the game to be created
	 * @param color
	 *            color of the player
	 * @param extension
	 *            extension selected for the game
	 * @param turntime
	 *            Time every Player has to make a move
	 */
	public void sendNewGameCreatedMessage(String gameName, String color,
			int turntime, List<CapabilitiesType> extension) {
		sendGeneralMessage(ClientMessageBuilder.writeNewGameMessage(gameName,
				color, turntime, extension));
	}

	/**
	 * Sends a joinMessage to the Server
	 * 
	 * @param gameID
	 *            id of the game which want to be joined
	 * @param color
	 *            color of the user
	 */
	public void sendJoinMessage(int gameID, String color) {
		sendGeneralMessage(ClientMessageBuilder.writeJoinMessage(gameID, color));
	}

	/**
	 * Sends a startGameMessage to the Server
	 * 
	 * @param gameID
	 *            id of the game which want to be started
	 */
	public void sendStartGameMessage(int gameID) {
		sendGeneralMessage(ClientMessageBuilder.startGame(gameID));
	}

	/**
	 * Writes a watch game message to the server
	 * 
	 * @param gameID
	 *            ID of the game to be watched
	 */
	public void sendWatchMessage(int gameID) {
		sendGeneralMessage(ClientMessageBuilder.watchGame(gameID));
	}

	/**
	 * Writes a message to the Server if someone leaves a game
	 * 
	 * @param gameID
	 *            game which the player leaves
	 */
	public void leaveGame(int gameID) {
		sendGeneralMessage(ClientMessageBuilder.leaveGame(gameID));
	}

	/**
	 * Sends a 'move' message to the server.
	 * 
	 * @param gameID ID of the game where the move was made
	 * @param p Position of the Card that was set
	 * @param rotation Rotation of the Card
	 * @param currentPlacement Placement of the Meeple
	 */
	public void sendMove(int gameID, Position p, int rotation,
			Placement currentPlacement) {
		sendGeneralMessage(ClientMessageBuilder.move(gameID, p, rotation,
				currentPlacement));
	}

	/**
	 * Sends a general message to the Server.
	 * <p>
	 * Is used for all messages to be written to the server. It writes the given JSONObject to the OutputStream.
	 * 
	 * @param json
	 *            message to be sent
	 */
	private void sendGeneralMessage(JSONObject json) {
		log.info("ClIENT ---> SERVER : " + json);
		try {
			handler.getOut().write(json.toString() + "\n");
			handler.getOut().flush();
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
}
