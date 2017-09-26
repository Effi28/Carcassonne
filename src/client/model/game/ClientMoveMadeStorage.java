package client.model.game;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.json.JSONObject;

import client.model.clientCommunication.ClientControl;
import client.model.clientCommunication.ServerHandler;
import client.view.ObserverView;
import shared.enums.SpecialMeepleType;
import shared.model.BuildCard;
import shared.model.Position;

/**
 * Saves all 'move made' messages received by the client. If the client is an
 * observer it is possible to step through all moves that were made. 
 * 
 * This class implements the Singleton-Pattern.
 * 
 * 
 * @version 04.01.2014
 * @see ObserverView
 * @see ClientControl
 */
public final class ClientMoveMadeStorage {

	/**
	 * Instance of this class because of Singleton-Pattern
	 */
	private static ClientMoveMadeStorage instance;

	/**
	 * Queue that contains all 'move made' messages received by this client. Is
	 * used to display all moves that were made for observer clients.
	 */
	private Queue<JSONObject> moveMadeQueue;

	/**
	 * Reference to the ServerHandler
	 */
	private ServerHandler handler;

	/**
	 * Only constructor.
	 * Is private because this class implements the Singleton-Pattern.
	 * 
	 * @param handler Reference to the ServerHandler of this client
	 */
	private ClientMoveMadeStorage(ServerHandler handler) {
		moveMadeQueue = new ConcurrentLinkedQueue<>();
		this.handler = handler;
	}

	/**
	 * Returns the instance of this class.
	 * 
	 * @param handler Reference to the ServerHandler of the client
	 * @return instance of this class
	 */
	public synchronized static ClientMoveMadeStorage getInstance(ServerHandler handler) {
		if (instance == null) {
			instance = new ClientMoveMadeStorage(handler);
		}
		return instance;
	}

	/**
	 * Adds a 'move made' message to the queue.
	 * 
	 * @param move
	 *            'move made' message
	 *            
	 * @return true if it was successful, false if not
	 */
	public boolean addMoveToQueue(JSONObject move) {
		return moveMadeQueue.add(move);
	}

	/**
	 * Performs all moves that were made in the game. So the game in its actual
	 * state is shown.
	 */
	public void performAllMoves() {
		while (moveMadeQueue.size() != 0) {
			performMove();
		}
	}

	/**
	 * Performs the move indicated by the first 'move made' message in the
	 * queue.
	 */
	public void performMove() {
		JSONObject move = moveMadeQueue.poll();
		JSONObject card = null;
		if (move != null) {
			card = move.optJSONObject("tile");
		}
		int gameID = move.optInt("game id");
		String moveMadeNick = move.optString("nick");
		JSONObject action = move.optJSONObject("action");

		assert !moveMadeNick.equals("");

		String cardID = CardReceiver.receiveCard(card);

		assert !cardID.equals("");

		int x = action.optInt("x");
		int y = action.optInt("y");
		int rotation = action.optInt("rotation");
		String specialMeeple = move.optString("special meeple");

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
		
		handler.getControl().moveMade(gameID, cardID, new Position(x, y),
				rotation, placement, BuildCard.buildCard(card), moveMadeNick, SpecialMeepleType.fromString(specialMeeple));
	}

	/**
	 * Is the Queue empty?
	 * 
	 * @return true if the queue is empty, false if not
	 */
	public boolean isMoveMadeQueueEmpty() {
		return moveMadeQueue.isEmpty();
	}
}
