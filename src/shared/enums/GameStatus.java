package shared.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the different states of a game
 * 
 * @version 09.12.2013
 *
 */
public enum GameStatus {
	/**
	 * The game has not yet started
	 */
	NOTSTARTED("not started"), 
	/**
	 * The game is ongoing
	 */
	ONGOING("ongoing"), 
	/**
	 * The game has already ended
	 */
	ENDED("ended");
	
	/**
	 * Name as String for a GameStatus
	 */
	private final String name;
	
	/**
	 * Map of all existing GameStatus
	 */
	private static final Map<String, GameStatus> stringToGameStatus = new HashMap<>();

	static {
		for (GameStatus f : GameStatus.values()) {
			stringToGameStatus.put(f.toString(), f);
		}
	}
	
	/**
	 * Standard constructor of a GameStatus
	 * 
	 * @param name which will be added to a single GameStatus
	 */
	private GameStatus(String name) {
		this.name = name;
	}

	/**
	 * Returns a String for GameStatus
	 */
	@Override
	public String toString() {
		return name;
	}
	
	/**
	 * Returns a GameStatus for a String
	 *
	 * @param s String which will be transformed into a GameStatus
	 * @return the GameStatus
	 */
	public static GameStatus fromString(String s){
		return GameStatus.stringToGameStatus.get(s);
	}
}
