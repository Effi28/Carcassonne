package server.model.serverCommunication.clientHandling;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import server.model.serverCommunication.execution.ServerControl;
import shared.enums.CapabilitiesType;
import shared.model.User;

/**
 * Handles one connected client.
 * <p>
 * For every newly connected client a new instance of this (Thread-)class is
 * created. It handles the communication between the server and its specific
 * client through its own ServerMessageReceiver and -sender. It references a
 * user.
 * 
 * @version 16.01.2014
 * @see ServerControl
 * @see ServerMessageReceiver
 * @see ServerMessageSender
 */
public final class ClientHandler extends Thread {

	/**
	 * Error logger
	 */
	private static Logger log = LogManager.getLogger("ERROR");
	/**
	 * Info logger
	 */
	private static Logger infoLog = LogManager.getLogger("INFO");
	/**
	 * Socket for the communication with the specific client
	 */
	private final Socket socket;
	/**
	 * Reference to the server
	 */
	private final ServerControl server;
	/**
	 * Reference to the receiver
	 */
	private final ServerMessageReceiver receiver;
	/**
	 * Reference to the sender
	 */
	private final ServerMessageSender sender;
	/**
	 * Reference to the user represented by this clientHandler
	 */
	private User user;
	/**
	 * Capabilities of the client
	 */
	private Set<CapabilitiesType> capabilities;
	/**
	 * Input reader
	 */
	private BufferedReader in;
	/**
	 * Output writer
	 */
	private OutputStreamWriter out;
	private volatile boolean stop;
	

	/**
	 * Standard constructor for ClientHandler. It takes the socket the server
	 * gave the client for the communication as an argument and a reference to
	 * the server.
	 * 
	 * @param accept new Socket for communication
	 * @param serverControl Reference to the ServerControl
	 */
	public ClientHandler(Socket accept, ServerControl serverControl) {
		super("ClientHandler");
		stop = false;
		receiver = new ServerMessageReceiver(this);
		sender = new ServerMessageSender(this);
		
		this.socket = accept;
		this.server = serverControl;

		try {
			out = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
			in = new BufferedReader(new InputStreamReader(
					socket.getInputStream(), "UTF-8"));
		} catch (IOException e) {
			out = null;
			in = null;
			log.error(e.getMessage());
		}
	}

	/**
	 * Receives Input from the Client as long as the Thread is running
	 */
	@Override
	public void run() {
		while (!stop && !receiver.getNullInput()) {
			try {
				receiver.receiveInputGeneral();
			} catch (Exception e) {
				try {
					this.socket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
		}
		Thread.currentThread().interrupt();
	}

	/**
	 * Performs the logout of this client.
	 * <p>
	 * Closes In- and Outputstream as well as the Socket and sets the terminate-flag for the ClientHandler-Thread.
	 */
	public void performLogout(){
		try {
//			receiver.setNullInput(true);
			// Closes the Input/Output Stream and the Socket
			this.stop = true;
			this.getOut().close();
			this.getIn().close();
			this.getSocket().close();
			infoLog.info("Killed the ClientHandler Thread.");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	/*
	 * GETTER and SETTER and EQUALS and HASHCODE below
	 */

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClientHandler other = (ClientHandler) obj;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}

	public User getUser() {
		if (user == null) {
			throw new NullPointerException("USER NULL");
		}
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
		this.setName("ClientHandler: " + user.getNick());
	}

	public String getNick() {
		return user.getNick();
	}

	Socket getSocket() {
		return socket;
	}

	ServerControl getServer() {
		if (server == null) {
			log.error("SERVER NULL");
			throw new NullPointerException("SERVER NULL");
		}
		return server;
	}

	public ServerMessageReceiver getReceiver() {
		if (receiver == null) {
			log.error("RECEIVER NULL");
			throw new NullPointerException("RECEIVER NULL");
		}
		return receiver;
	}

	public ServerMessageSender getSender() {
		if (sender == null) {
			log.error("SENDER NULL");
			throw new NullPointerException("SENDER NULL");
		}
		return sender;
	}

	public Set<CapabilitiesType> getCapabilities() {
		return capabilities;
	}

	public void setCapabilities(Set<CapabilitiesType> capa) {
		this.capabilities = capa;
	}

	BufferedReader getIn() {
		return in;
	}

	OutputStreamWriter getOut() {
		return out;
	}

	public void sendMessage(JSONObject message) {
		sender.sendMessageGeneral(message);
	}

}