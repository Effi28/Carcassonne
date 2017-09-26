package server.model.serverCommunication.queues;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import server.model.serverCommunication.clientHandling.ServerMessageSender;

/**
 * This utility class saves all 'move made' messages sent by the server.
 * If a new spectator joins a game all messages saved in this class will be sent to the spectator.
 * Because this class is a utility-class it musn't be instantiated.
 * 
 * @version 13.12.2013
 * @see client.model.game.ClientMoveMadeStorage
 * @see ServerMessageSender
 */
public final class ServerMoveMadeStorage {
	
	/**
	 * Error logger
	 */
	private static Logger log = LogManager.getLogger("ERROR");
	/**
	 * Info logger
	 */
	private static Logger infoLog = LogManager.getLogger("INFO");
	

	/**
	 * Map which saves all 'move-made' messages.
	 * <p>
	 * The key of this map is the gameID of the game where the 'move-made' messages are saved.
	 * The value is a queue which contains all 'move-made' messages as a JSONObject,
	 * they are saved in the correct order.
	 */
	private static Map<Integer, ConcurrentLinkedQueue<JSONObject>> moveQueue = new HashMap<Integer, ConcurrentLinkedQueue<JSONObject>>();
	

	/**
	 * Should never be instantiated.
	 */
	private ServerMoveMadeStorage(){
		log.error("MoveMadeStorage should not be instantiated");
		throw new AssertionError("MoveMadeStorage should not be instantiated");
	}
	
	/**
	 * Creates a new queue in the hashmap the key is the ID of the game.
	 * Must be called if a new game is created
	 * 
	 * @param gameID ID of the game that was created
	 */
	private static void addQueue(int gameID){
		moveQueue.put(gameID, new ConcurrentLinkedQueue<JSONObject>());
		infoLog.info("A new queue was added for the game with the ID: "+gameID);
	}
	
	/**
	 * Deletes the queue with the given GameID.
	 * Is invoked when a game was removed.
	 * @param gameID ID of the game that was removed
	 */
	public static void deleteQueue(int gameID){
		moveQueue.remove(gameID);
		infoLog.info("The queue for the game with the ID: "+gameID+ " was removed.");
	}
	
	/**
	 * Adds a 'move made' message to the queue for the specific game.
	 * 
	 * @param gameID ID of the game where the move was made.
	 * @param move 'move made' message
	 */
	 public static void addMoveToQueue(int gameID, JSONObject move){
		ConcurrentLinkedQueue<JSONObject> queue = moveQueue.get(gameID);
		if(queue == null){
			addQueue(gameID);
			queue = moveQueue.get(gameID);
		}
		queue.add(move);
	}
	
	/**
	 * Is called when a spectator successfully joins a game.
	 * Sends all 'move made' message done in this game to the spectator.
	 * 
	 * @param sender Sender that sends the messages to the spectator
	 * @param gameID ID of the game the spectator joined
	 */
	 public static void sendAllMoveMade(ServerMessageSender sender, int gameID){
		ConcurrentLinkedQueue<JSONObject> queue = moveQueue.get(gameID);
		if(queue == null){
			return;
		}
		Iterator<JSONObject> queueIterator = queue.iterator();
		while(queueIterator.hasNext()){
			JSONObject moveMade = queueIterator.next();			
			sender.sendMessageGeneral(moveMade);
		}
	}
}
