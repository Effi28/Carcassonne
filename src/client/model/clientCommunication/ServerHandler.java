package client.model.clientCommunication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Checks for incoming messages from the server.
 * <p>
 * This thread subclass checks for incoming messages from the server 
 * by telling ClientMessageReceiver to look for incoming messages.
 * Implements the SINGLETON-Pattern because a client should only be connected to one server.
 * 
 * @version 07.01.2014
 * 
 * @see ClientControl
 * @see ClientMessageReceiver
 * @see ClientMessageSender
 */
public final class ServerHandler extends Thread {

	/**
	 * Error logger
	 */
	private static Logger log = LogManager.getLogger("ERROR");
	/**
	 * Reference to the receiver which receives all messages
	 */
	private final ClientMessageReceiver receiver;
	/**
	 * Reference to the sender which sends out messages to the server
	 */
	private final ClientMessageSender sender;
	/**
	 * Reference to the ClientControl which interacts with the model
	 */
	private final ClientControl control;
	
	/**
	 * Input Stream reader
	 */
	private BufferedReader in;
	/**
	 * Output Stream writer
	 */
	private OutputStreamWriter out;
	
	/**
	 * Instance of this class, because of the Singleton-Pattern
	 */
	private static ServerHandler instance;

	/**
	 * Only constructur.
	 * Private constructor because this class implements the Singleton-Pattern.
	 * 
	 * @param control Reference to the ClientControl
	 */
	private ServerHandler(ClientControl control) {
		this.receiver = new ClientMessageReceiver(this);
		this.sender = new ClientMessageSender(this);
		this.control = control;
		
		try {
			in = new BufferedReader(new InputStreamReader(getControl()
					.getClientSocket().getInputStream()));
			out = new OutputStreamWriter(getControl().getClientSocket()
					.getOutputStream(), "UTF-8");
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns the instance of the ServerHandler.
	 * 
	 * @param control Reference to the ClientControl
	 * @return instance of the ServerHandler
	 */
	public synchronized static ServerHandler getInstance(ClientControl control){
		if(instance == null){
			ServerHandler.instance = new ServerHandler(control);
		}
		return instance;
	}

	/**
	 * Method of the thread that always checks for incoming messages
	 */
	@Override
	public void run() {
			try {
				while (!Thread.interrupted())
					receiver.readMessage();
			} catch (Exception e) {
				try {
					control.getClientSocket().close();
				} catch (IOException e1) {
					log.error(e1.getMessage());
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
	}

	/*
	 * SETTER AND GETTER BELOW
	 */

	public ClientControl getControl() {
		if(control == null){
			log.error("CLIENTCONTROL NULL");
			throw new NullPointerException("CLIENTCONTROL NULL");
		}
		return control;
	}

	public ClientMessageSender getSender() {
		if (sender == null){
			log.error("CLIENTCONTROL NULL");
			throw new NullPointerException("CLIENTMESSAGESENDER NULL");
		}
		return sender;
	}

	public BufferedReader getIn() {
		if (in == null){
			log.error("INPUTSTREAM NULL");
			throw new NullPointerException("INPUTSTREAM NULL");
		}
		return in;
	}

	public OutputStreamWriter getOut() {
		if (out == null){
			log.error("OUTPUTSTREAM NULL");
			throw new NullPointerException("OUTPUTSTREAM NULL");
		}
		return out;
	}
}
