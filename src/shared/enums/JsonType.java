package shared.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the different Message-Types of a Json-Message
 * 
 * @version 09.12.2013
 *
 */
public enum JsonType {

	LOGIN("login"), 
	LOGINFAILED("login failed"), 
	LOGINSUCCESS("login success"), 
	PLAYERADDED("player added"), 
	NEWGAME("new game"),
	GAMECREATED("game created"), 
	NEWGAMEFAILED("new game failed"), 
	JOINGAME("join game"), 
	JOINGAMEFAILED("join game failed"), 
	JOINGAMESUCCESS("join game success"), 
	GAMEUPDATE("game update"), 
	WATCHGAME("watch game"), 
	WATCHGAMEFAILED("watch game failed"),
	WATCHGAMESUCCESS("watch game success"), 
	STARTGAME("start game"), 
	STARTGAMEFAILED("start game failed"), 
	TILEDRAWN("tile drawn"), 
	MOVE("move"), 
	MOVEFAILED("move failed"),
	MOVEMADE("move made"), 
	LEAVEGAME("leave game"), 
	PLAYERLEFT("player left"), 
	DISCONNECT("disconnect"), 
	ACKNOWLEDGEDISCONNECT("acknowledge disconnect"), 
	PLAYERREMOVED("player removed"), 
	GAMEREMOVED("game removed"), 
	INVALIDMESSAGE("invalid message"), 
	CHAT("chat");

	/**
	 * name as String for a JsonType
	 */
	private final String name;
	
	/**
	 * Map of all existing JsonTypes
	 */
	private static final Map<String, JsonType> stringToJsonType = new HashMap<>();

	static {
		for (JsonType f : JsonType.values()) {
			stringToJsonType.put(f.toString(), f);
		}
	}

	/**
	 * Standard constructor of a JsonType
	 * 
	 * @param name which will be added to a single JsonType
	 */
	private JsonType(String name) {
		this.name = name;

	}

	/**
	 * Returns a String for JsonType
	 */
	@Override
	public String toString() {
		return name;
	}
	
	/**
	 * Returns a JsonType for a String
	 *
	 * @param s String which will be transformed into a JsonType
	 * @return the JsonType
	 */
	public static JsonType fromString(String s){
		return JsonType.stringToJsonType.get(s);
	}
}