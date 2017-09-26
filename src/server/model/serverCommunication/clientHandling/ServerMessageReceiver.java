package server.model.serverCommunication.clientHandling;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import client.model.clientCommunication.ClientMessageBuilder;
import server.model.serverCommunication.queues.InputQueue;
import server.model.serverCommunication.utility.WrappedJSONObject;
import shared.enums.JsonType;

/**
 * Receives messages from the client.
 * <p>
 * It checks with the receiveInputGeneral method all incoming messages, sorts it by its type and then calls a specific
 * method for each type of message. Every instance of this class is controlled by its ClientHandler which handles the
 * whole communication between a client. This class provides just the receiving functionalities.
 * 
 * @Version 16.01.2014
 * @see JsonType
 * @see ClientHandler
 * @see ServerMessageSender
 */
public final class ServerMessageReceiver {

	/**
	 * Reference to the handler controlling this class
	 */
	private ClientHandler handler;

	/**
	 * Error logger
	 */
	private static Logger log = LogManager.getLogger("ERROR");
	
	/**
	 * Warn logger
	 */
	private static Logger warnLog = LogManager.getLogger("WARN");

	/**
	 * Info logger
	 */
	private static Logger infoLog = LogManager.getLogger("INFO");

	/**
	 * Boolean flag to indicate whether the client sent null or not.
	 */
	private boolean nullInput;

	/**
	 * Constructs a receiver for one connected client.
	 * 
	 * @param clientHandler
	 *            thread which handles the communication between server and a specific client represented by this
	 *            handler
	 */
	ServerMessageReceiver(ClientHandler clientHandler) {
		this.handler = clientHandler;
		nullInput = false;
	}

	/**
	 * Receives all messages from the client and sorts it out by its type. Then calls a specific method for each type
	 */
	public void receiveInputGeneral() {
		String jsonText = null;
		try {
			
			if((jsonText = handler.getIn().readLine()) != null){
				infoLog.info("CLIENT ---> SERVER : " + jsonText);
				
				JSONObject jsonObject = new JSONObject(jsonText);
				InputQueue.addElement(new WrappedJSONObject(jsonObject, handler));	
				
				if(JsonType.fromString(jsonObject.optString("type")).equals(JsonType.DISCONNECT)){
					nullInput = true;
				}				
			}
			else{
				if (!nullInput) {
					InputQueue.addElement(new WrappedJSONObject(ClientMessageBuilder
							.writeLogoutMessage(), handler));
					warnLog.warn("Received null input, disconnecting client...");
				}
				nullInput = true;
				warnLog.warn("Received null input");
			}

		} catch (JSONException | IOException e) {
			log.error(e.getMessage() + "text: " + jsonText);
		}
	}
	
	public boolean getNullInput(){
		return nullInput;
	}
}
