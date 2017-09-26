package shared.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the different areas a card can have.
 * <p>
 * A card consists out of different areas that a have a specific type.
 * These different types are represented by this enum.
 * 
 * @version 09.12.2013
 */
public enum CardAreaType {
	MEADOW("Meadow"), 
	TOWN("Town"), 
	ROAD("Road"), 
	CLOISTER("Cloister");

	/**
	 * name as String for a CardAreaType
	 */
	private final String name;

	/**
	 * Map of all existing CardAreTypes
	 */
	private static final Map<String, CardAreaType> stringToCardAreaType = new HashMap<>();

	static {
		for (CardAreaType f : CardAreaType.values()) {
			stringToCardAreaType.put(f.toString(), f);
		}
	}

	/**
	 * Standard constructor of a CardAreaType
	 * 
	 * @param name which will be added to a single CardAreaType
	 */
	private CardAreaType(String name) {
		this.name = name;
	}

	/**
	 * Returns a String for CardAreaType
	 */
	@Override
	public String toString() {
		return name;
	}

	/**
	 * Returns a CardAreaType for a String
	 * 
	 * @param s String
	 *            which will be transformed into a CardAreaType
	 * @return the CardAreType
	 */
	public static CardAreaType fromString(String s) {
		return CardAreaType.stringToCardAreaType.get(s);
	}
}
