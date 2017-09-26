package server.model.serverCommunication.utility;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import server.model.game.ServerGame;
import server.model.serverCommunication.execution.ServerControl;
import server.model.serverCommunication.queues.ServerMoveMadeStorage;

/**
 * Cleans the garbage in ServerControl.
 * <p>
 * This thread deletes all ended games and games with no players that are saved in the server.
 * It also checks if the clients that are saved in ServerControl are still connected.
 * 
 * @version 22.01.2014
 *
 * @see ServerControl
 */
public final class CleaningThread extends Thread{

	/**
	 * Info-logger
	 */
	private static Logger infoLog = LogManager.getLogger("INFO");
	/**
	 * Error-logger
	 */
	private static Logger errorLog = LogManager.getLogger("ERROR");
	/**
	 * Instance of this CleaningThread because of the Singleton-Pattern
	 */
	private static CleaningThread instance;
	/**
	 * Reference to the ServerControl
	 */
	private ServerControl control;
	
	
	/**
	 * Only constructor of this class.
	 * Is private because of the Singleton-Pattern
	 * 
	 * @param control Reference to the ServerControl
	 */
	private CleaningThread(ServerControl control){
		this.control = control;
	}
	
	/**
	 * Returns the instance of this class because of the Singleton-Pattern.
	 * 
	 * @param control Reference to the ServerControl
	 * @return instance of this class
	 */
	public synchronized static CleaningThread getInstance(ServerControl control){
		if(instance == null){
			instance = new CleaningThread(control);
		}
		return instance;
	}
	
	@Override
	public void run(){
		while(!Thread.interrupted()){
			checkGames(control.getGames());
			try {
				Thread.sleep(15000);
			} catch (InterruptedException e) {
				errorLog.error(e.getMessage());
				e.printStackTrace();
			}			
		}
	}

	/**
	 * Checks if the games saved in the ServerControl have players in it.
	 * If one doesn't it will be deleted.
	 * 
	 * @param games Map of the games saved in ServerControl
	 */
	private void checkGames(Map<Integer, ServerGame> games) {
		List<Integer> gamesToRemove = new ArrayList<>();
		
		for(ServerGame g: games.values()){
			if(g.getPlayerList().size() == 0 && g.getSpectatorList().size() == 0){
				gamesToRemove.add(g.getGameID());
				infoLog.info("Game with ID: " + g.getGameID() + " will be deleted.");
			}
		}
		
		for(int i : gamesToRemove){
			ServerMoveMadeStorage.deleteQueue(i);
			games.remove(i);
		}
	}	
}
