package server.model.serverCommunication.clientHandling;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import server.model.game.ServerGame;
import server.model.serverCommunication.utility.ServerMessageBuilder;
import shared.enums.CapabilitiesType;
import shared.enums.Reason;
import shared.model.Card;
import shared.model.Placement;
import shared.model.Position;

/**
 * This class sends the messages to the client.
 * <p>
 * For every message sent by the server there is a method provided by this class. These methods call the
 * ServerMessageBuilder which returns a jsonObject with the specific message. Then the message is sent to the client.
 * Every ClientHandler references one instance of this class.
 * 
 * @Version 16.01.2014
 * @see ClientHandler
 * @see ServerMessageBuilder
 * @see ServerMessageReceiver
 */
public final class ServerMessageSender {

	/**
	 * Error logger
	 */
	private static Logger log = LogManager.getLogger("ERROR");

	/**
	 * Info logger
	 */
	private static Logger infoLog = LogManager.getLogger("INFO");

	/**
	 * Reference to the handler which controls this sender
	 */
	private final ClientHandler handler;

	/**
	 * Standard constructor
	 * 
	 * @param handler
	 *            handler that controls this instance
	 */
	ServerMessageSender(ClientHandler handler) {
		this.handler = handler;
	}

	/**
	 * Sends the message to the client.
	 * Is invoked by every other method that wants to send a message to the client.
	 * 
	 * @param json message to be sent to client
	 */
	public void sendMessageGeneral(JSONObject json) {

		infoLog.info("SERVER ---> CLIENT : " + json);
		try {
			if (!handler.getSocket().isClosed()
					|| handler.getSocket().isInputShutdown()
					|| handler.getSocket().isOutputShutdown()) {
				handler.getOut().write(json.toString() + "\n");
				handler.getOut().flush();
			}
		} catch (IOException e) {
			handler.performLogout();
			log.error(e.getMessage());
		}
	}

//	/**
//	 * Sends the message to the client that the login succeeded.
//	 * 
//	 * @param nick
//	 *            nickname of the player
//	 * @param games
//	 *            array of the available games
//	 * @param nickArray
//	 *            array of the logged in users
//	 * @param sharedCapa
//	 *            capabilities the server provides
//	 */
//	void sendLoginSuccess(String nick, Map<Integer, ServerGame> games,
//			List<String> nickArray, List<CapabilitiesType> sharedCapa) {
//
//		sendMessageGeneral(ServerMessageBuilder.loginSucceeded(nick, games,
//				nickArray, sharedCapa));
//	}
//
//	/**
//	 * Sends to all logged in clients that a new player joined the ChatLobby.
//	 * 
//	 * @param nick
//	 *            new player which joined the ChatLobby
//	 */
//	void sendPlayerAdded(String nick) {
//		sendMessageGeneral(ServerMessageBuilder.playerAdded(nick));
//	}
//
//	/**
//	 * Sends that the message can't be read from the Server.
//	 * 
//	 * @param reason sent Message
//	 */
//	void sendInvalidMessage(Reason reason) {
//		sendMessageGeneral(ServerMessageBuilder.invalidMessage(reason));
//	}
//
//	/**
//	 * Sends the Message to the Client that the login failed.
//	 * 
//	 * @param reason
//	 *            why the login failed
//	 */
//	void sendLoginFailed(Reason reason) {
//		sendMessageGeneral(ServerMessageBuilder.loginFailed(reason));
//	}
//
//	/**
//	 * Sends a message to a single user.
//	 * 
//	 * @param senderID
//	 *            name of the message sender
//	 * @param msg
//	 *            message content
//	 * @param receiverID
//	 *            name of the message receiver
//	 */
//	void sendMessageToSpecificUser(String senderID, String msg,
//			String receiverID) {
//		sendMessageGeneral(ServerMessageBuilder.chatToSpecific(senderID, msg,
//				receiverID));
//	}
//
//	/**
//	 * Sends a message to all users of a game.
//	 * 
//	 * @param senderID
//	 *            message sender
//	 * @param msg
//	 *            message content
//	 * @param gameID
//	 *            game receiver
//	 */
//	void sendMessage(String senderID, String msg, int gameID) {
//
//		sendMessageGeneral(ServerMessageBuilder.chatToGame(senderID, msg,
//				gameID));
//	}
//
//	/**
//	 * Sends a successful logout-attempt message to the disconnecting client.
//	 * 
//	 */
//	void sendLogoutSpecific() {
//
//		sendMessageGeneral(ServerMessageBuilder.userDisconnected());
//	}
//
//	/**
//	 * Sends a logout message to every client.
//	 * 
//	 * @param userName
//	 *            user which logged out
//	 */
//	void sendLogout(String userName) {
//
//		sendMessageGeneral(ServerMessageBuilder.playerRemoved(userName));
//	}
//
//	/**
//	 * Sends a message to the client that the creation of a new game failed.
//	 * 
//	 * @param gameName
//	 *            name of the game that failed to create
//	 * @param reason
//	 *            why it failed to create the game
//	 */
//	void newGameFailed(String gameName, Reason reason) {
//		sendMessageGeneral(ServerMessageBuilder.gameCreationFailed(gameName,
//				reason));
//	}
//
//	/**
//	 * Sends a message to the client that joining a game failed.
//	 * 
//	 * @param gameID
//	 *            id of the game the client wanted to join
//	 * @param reason
//	 *            why it failed to join the game
//	 */
//	void sendJoinFailed(int gameID, Reason reason) {
//		sendMessageGeneral(ServerMessageBuilder.joinGameFailed(gameID, reason));
//	}
//
//	/**
//	 * Sends a message to the specific receiver that the joining the game was successful.
//	 * 
//	 * @param gameID
//	 *            ID of the game the client wants to join
//	 */
//	void sendJoinSuccess(int gameID) {
//		sendMessageGeneral(ServerMessageBuilder.joinGameSuccess(gameID));
//	}
//
//	/**
//	 * Sends a message to the client that watch game has failed.
//	 * 
//	 * @param gameID
//	 *            ID of the game the client wants to watch
//	 * @param reason
//	 *            why it failed to watch the game
//	 */
//	void sendWatchGameFailed(int gameID, Reason reason) {
//		sendMessageGeneral(ServerMessageBuilder.watchGameFailed(gameID, reason));
//	}
//
//	/**
//	 * Sends a message to the client that watch game was successful.
//	 * 
//	 * @param gameID
//	 *            gameID of the game the client wants to watch
//	 */
//	void sendWatchGameSuccess(int gameID) {
//		sendMessageGeneral(ServerMessageBuilder.watchGameSuccess(gameID));
//	}
//
//	/**
//	 * Sends a "tile drawn" message to the client.
//	 * 
//	 * @param gameID
//	 *            ID of the game where the tile was drawn
//	 * @param nick
//	 *            nick of the player who's turn it is
//	 * @param card
//	 *            tile that was drawn
//	 * @param remaining
//	 *            number of remaining cards
//	 * @param turnTime time the player has to make a move
//	 */
//	void sendTileDrawn(int gameID, String nick, Card card, int remaining,
//			int turnTime) {
//		sendMessageGeneral(ServerMessageBuilder.tileDrawn(gameID, nick, card,
//				remaining, turnTime));
//	}
//
//	/**
//	 * Sends a 'move-failed' message to the client.
//	 * 
//	 * @param gameID If of the game the creation failed
//	 * @param position Position of the Card that failed to be set
//	 * @param rotation Roation of the Card that failed to be set
//	 * @param placement Placement of the Meeple
//	 * @param reason Reason why it failed to set the Card
//	 */
//	void sendMoveFailed(int gameID, Position position, int rotation,
//			Placement placement, Reason reason) {
//		sendMessageGeneral(ServerMessageBuilder.moveFailed(gameID,
//				ServerMessageBuilder
//						.createAction(position, rotation, placement), reason));
//	}
//
//	/**
//	 * Sends a 'move-made' message to the client
//	 * 
//	 * @param gameID ID of the game where a move was made
//	 * @param nick Nick of the player who made a move
//	 * @param card Card the player set
//	 * @param position Position of the Card the player set
//	 * @param rotation Rotation of the Card the player set
//	 * @param placement Placement of the meeple
//	 * @return JSONObject containing the information for a 'move made' message
//	 */
//	JSONObject sendMoveMade(int gameID, String nick, Card card,
//			Position position, int rotation, Placement placement) {
//
//		JSONObject moveMade = ServerMessageBuilder.moveMade(gameID, nick, card,
//				ServerMessageBuilder
//						.createAction(position, rotation, placement));
//		sendMessageGeneral(moveMade);
//
//		return moveMade;
//	}
//
//	/**
//	 * Sends a 'game removed' message.
//	 * 
//	 * @param gameID gameID of the removed game
//	 */
//	void sendGameRemoved(int gameID) {
//		sendMessageGeneral(ServerMessageBuilder.gameRemoved(gameID));
//	}
}
