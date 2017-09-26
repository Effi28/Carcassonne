package shared.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the different types of capabilities a server or client could support.
 * 
 * @version 06.01.2014
 *
 */
public enum CapabilitiesType {
	/**
	 * Standard capability a client must provide in order to play the fundamental game and to chat
	 */
	CHAT("Chat"),
	/**
	 * The Big Meeple extension of the Team Lannister
	 */
	BIGMEEPLE("Big meeple Lannister"),
	/**
	 * The Bishop extension of the Team Lannister
	 */
	BISHOP("Bishop Lannister"),
	/**
	 * Inns and Cathedrals extension
	 */
	INNS("Inns and Cathedrals");

	/**
	 * Name as String for a CapabilitiesType
	 */
	private final String name;

	/**
	 * Map of all existing CardAreTypes
	 */
	private static final Map<String, CapabilitiesType> stringToCapabilitiesType = new HashMap<>();

	static {
		for (CapabilitiesType f : CapabilitiesType.values()) {
			stringToCapabilitiesType.put(f.toString(), f);
		}
	}

	/**
	 * Standard constructor of CapabilitiesType
	 * 
	 * @param name which will be added to a single CapabilitiesType
	 */
	private CapabilitiesType(String name) {
		this.name = name;
	}

	/**
	 * Returns a String for the CapabilitiesType
	 */
	@Override
	public String toString() {
		return name;
	}

	/**
	 * Returns a CapabilitiesType for a String
	 * 
	 * @param s String which will be transformed into a CapabilitiesType
	 * @return the CapabilitiesType
	 */
	public static CapabilitiesType fromString(String s) {
		return CapabilitiesType.stringToCapabilitiesType.get(s);
	}
}
