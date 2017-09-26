package shared.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the different failure Reasons which can happen.
 * 
 * @version 09.12.2013
 *
 */
public enum Reason {

	ILLEGALNICK("Illegal nick!"),
	NICKALREADYTAKEN("Nick already taken!"),
	SERVERNOTREADY("Server not ready!"),
	COLORUNKNOWN("Color unknown!"),
	NAMEALREADYINUSE("Name already in use!"),
	CANNOTCREATEANEWAME("Cannot create a new game!"),
	GAMENOTFOUND ("Game not found!"),
	GAMEISFULL ("Game is full!"),
	ALREADYJOINED ("Already joined!"),
	COLORTAKEN("Color taken!"),
	ALREADYWATCHING ("Already watching!"),
	NOTENOUGHPLAYERS ("Not enough players!"),
	NOTGAMEOWNER ("You are not the game owner!"),
	GAMEALREADYSTARTED ("Game already started!"),
	GAMEISNOTYETSTARTED ("Game is not yet started!"),
	ITSNOTYOURTURN ("It's not your turn!"),
	MOVEISILLEGAL("Move is illegal!"), 
	ALREADYSTARTED("Game has already been started!"),
	INVALIDTURNTIMELIMIT("Invalid turn time limit!"),
	INVALIDEXTENSION("Invalid extension!"), 
	ALREADYHOSTING("Already hosting a game!"), 
	ILLEGALGAMENAME("Illegal name for the game!"), 
	CHATNOTAVAILABLE("You can't chat because your client doesn't have the capability to chat!"), 
	COULDNOTREAD("Invalid message! Couldn't read your message!"), 
	INVALIDMESSAGE("Invalid message."), 
	LOGINFAILED("Login failed.");
	
	
	/**
	 * name as String for a Reason
	 */
	private final String name;
	
	/**
	 * Map of all existing Reasons
	 */
	private static final Map<String, Reason> stringToReason = new HashMap<>();

	static {
		for (Reason f : Reason.values()) {
			stringToReason.put(f.toString(), f);
		}
	}

	/**
	 * Standard constructor of a Reason
	 * 
	 * @param name which will be added to a single Reason
	 */
	private Reason(String name) {
		this.name = name;
	}

	/**
	 * Returns a String for Reason
	 */
	@Override
	public String toString() {
		return name;
	}
	/**
	 * Returns a Reason for a String
	 *
	 * @param s String which will be transformed into a Reason
	 * @return the Reason
	 */
	public static Reason fromString(String s){
		return Reason.stringToReason.get(s);
	}
}