package ai;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;

import ai.AI;
import shared.model.Position;

public final class AISender {

	AI ai;

	public AISender(AI ai) {
		this.ai = ai;
	}

	public void sendLogin(String nick, JSONArray capa) {
		sendGeneralMessage(AIMessageBuilder.writeLoginMessage(nick, capa));
	}

	private void sendGeneralMessage(JSONObject json) {
		try {
			ai.getOut().write(json.toString() + "\n");
			ai.getOut().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendJoinGame(int gameID) {
		sendGeneralMessage(AIMessageBuilder.writeJoinMessage(gameID, ai.getPlayer().getColor()));

	}

	public void leaveGame(int gameID) {
		sendGeneralMessage(AIMessageBuilder.writeLogoutMessage());

	}

	public void sendMove(int gameID, Position p, int rotation, int placement) {
		sendGeneralMessage(AIMessageBuilder
				.move(gameID, p, rotation, placement));
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
		sendGeneralMessage(AIMessageBuilder.writeChatMessage(senderID, msg));
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
		sendGeneralMessage(AIMessageBuilder.writeChatMessage(senderID, msg,
				gameID));
	}

	/**
	 * Writes a message to a specific user
	 * 
	 * @param msg
	 *            message
	 * @param receiver
	 *            user who should receive the message
	 * @param sender
	 *            user who send the message
	 */
	public void sendChatMessage(String msg, String receiver, String sender) {
		sendGeneralMessage(AIMessageBuilder.writeChatMessage(msg, ai
				.getPlayer().getNick(), receiver));
	}

	public void sendExit(int gameID) {
		sendGeneralMessage(AIMessageBuilder.leaveGame(gameID));
		sendGeneralMessage(AIMessageBuilder.writeLogoutMessage());
	}
}