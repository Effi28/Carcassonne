package server.model.serverCommunication.queues;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;

import server.model.serverCommunication.clientHandling.ClientHandler;
import server.model.serverCommunication.utility.WrappedJSONObject;

/**
 * Provides a BlockingQueue for all messages that were sent from each client.
 * <p>
 * All ClientHandler-Threads put the messages they receive in a JSONObject, after that they 
 * wrap this JSONObject in an object of the type WrappedJSONObject which also contains a
 * reference to the ClientHandler which received the message.
 * This Wrapperobject is stored in this BlockingQueue until an Executor takes the 
 * object out of the queue and performs the calculations.
 * 
 * @version 29.01.14
 * @see BlockingQueue
 * @see ClientHandler
 * @see Executor
 * @see WrappedJSONObject
 */
public final class InputQueue {

	/**
	 * Blocking queue containing all elements received by all ClientHandler
	 */
	private static BlockingQueue<WrappedJSONObject> jobQueue = new LinkedBlockingQueue<>();
	
	/**
	 * Only constructor musn't be invoked throws Exception otherwise.
	 * 
	 * @throws IllegalAccessException is thrown if constructor is invoked.
	 */
	private InputQueue() throws IllegalAccessException{
		throw new IllegalAccessException("Musn't be instantiated.");
	}
	
	/**
	 * Gets the next element of the queue.
	 * Is invoked by the Executer which performs the calculations in ServerControl.
	 * 
	 * @return Next message which should be executed.
	 */
	public static WrappedJSONObject getNextElement(){
		try {
			return jobQueue.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Adds an element to the queue.
	 * Is invoked by the ClientHandler Threads.
	 * 
	 * @param element Element containing the message and the reference to the handler which received the message.
	 */
	public static void addElement(WrappedJSONObject element){
		try {
			jobQueue.put(element);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
