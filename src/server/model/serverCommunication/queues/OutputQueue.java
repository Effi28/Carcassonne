package server.model.serverCommunication.queues;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.json.JSONObject;

import server.model.serverCommunication.clientHandling.ClientHandler;
import server.model.serverCommunication.execution.ServerControl;
import server.model.serverCommunication.utility.WrappedJSONObject;
import shared.enums.JsonType;
import shared.enums.Reason;

/**
 * Provides the BlockingQueue which saves the output messages.
 * <p>
 * All messages that need to be sent are saved in the BlockingQueue contained by this class.
 * The Executer in ServerControl puts the wrapped messages in the BlockingQueue.
 * After that another Executer takes the messages out of the queue and sends
 * the messages to the specific ClientHandler.
 * 
 * 
 * @version 19.01.14
 * @see ClientHandler
 * @see Executor
 * @see BlockingQueue
 * @see ServerControl
 */
public final class OutputQueue {

	/**
	 * BlockingQueue containing all output messages
	 */
	private final static BlockingQueue<WrappedJSONObject> outputQueue = new LinkedBlockingQueue<>();
	/**
	 * Executer performing the sending of the messages
	 */
	private final static ExecutorService executer = Executors.newSingleThreadExecutor();
	
	// Static initializer which starts the executer
	static{
		executer.execute(new Runnable(){

			@Override
			public void run() {
				while(true){
					handleMessage();					
				}
			}
		});
	}
	
	/**
	 * Only constructor of this class.
	 * Musn't be invoked throws Exception otherwise.
	 * @throws IllegalAccessException is thrown when constructor is invoked. 
	 */
	private OutputQueue() throws IllegalAccessException{
		throw new IllegalAccessException("Musn't be invoked.");
	}
	
	/**
	 * Sends a message to the client.
	 * <p>
	 * Takes the message off the outgoing queue and sends the message to the client.
	 * If the message is a disconnect message the ClientHandler closes its In- and Outputstreams,
	 * closes its Socket and the ClientHandler Thread will be interrupted.
	 * 
	 * Is invoked by the Executer Thread handling the outgoing messages. 
	 */
	private static void handleMessage(){
		WrappedJSONObject msg;
		try {
			msg = outputQueue.take();
			ClientHandler handler = msg.getHandler();
			JSONObject message = msg.getJSONObject();
			JsonType type = JsonType.fromString(message.optString("type"));
			
			handler.sendMessage(message);				
			if(type.equals(JsonType.ACKNOWLEDGEDISCONNECT) ||
					(type.equals(JsonType.INVALIDMESSAGE) && Reason.fromString(message.optString("reason")).equals(Reason.LOGINFAILED))){
				handler.performLogout();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Adds an element containing the message to send and the reference to the specific ClientHandler
	 * to the output queue.
	 * Is invoked by the Executer Thread in ServerControl.
	 * 
	 * @param element Containing the message and the reference to the ClientHandler
	 */
	public static void addElement(WrappedJSONObject element){
		try {
			outputQueue.put(element);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
