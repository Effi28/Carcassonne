package server.model.serverCommunication.utility;

import org.json.JSONObject;

import server.model.serverCommunication.clientHandling.ClientHandler;
import server.model.serverCommunication.execution.ServerControl;
import server.model.serverCommunication.queues.InputQueue;
import server.model.serverCommunication.queues.OutputQueue;

/**
 * Wrapper class for wrapping a JSONObject containing a message
 * and a reference to the ClientHandler which received the message.
 * Is used to store data in OutputQueue and MoveQueue.
 * 
 * @version 29.01.2014
 * @see InputQueue
 * @see OutputQueue
 * @see ClientHandler
 * @see ServerControl
 */
public final class WrappedJSONObject {

	/**
	 * JSONObject containing the message.
	 */
	private final JSONObject json;
	/**
	 * Reference to the ClientHandler which received the message.
	 */
	private final ClientHandler handler;
	
	/**
	 * Only constructor of this class.
	 * 
	 * @param json JSONObject containing the message
	 * @param handler Reference to the ClientHandler which received the message.
	 */
	public WrappedJSONObject(JSONObject json, ClientHandler handler){
		this.json = json;
		this.handler = handler;
	}
	
	/*
	 * GETTER and SETTER below
	 */
	
	public JSONObject getJSONObject(){
		return json;
	}
	
	public ClientHandler getHandler(){
		return handler;
	}
}
