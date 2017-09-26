package server;

import java.io.IOException;
import java.net.ServerSocket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import server.model.serverCommunication.execution.ServerControl;

/**
 * Provides the main Method for the Server
 * 
 * @version 07.11.2013
 * 
 */
public final class Server {

	/**
	 * Error logger
	 */
	private static Logger log = LogManager.getLogger("ERROR");
	
	public static void main(String[] args) {

		int port = Integer.valueOf(args[0]);

		try {
			ServerControl.getInstance(new ServerSocket(port)).getConnections();
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}

}
