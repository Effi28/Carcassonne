package shared.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the different kinds of special meeples.
 * Currently there are two different kinds of special meeple.
 * One for the bishop extension and the other one for Big meeple.
 * 
 * 
 * @version %I%, %G%
 *
 */
public enum SpecialMeepleType {

	BISHOP("Bishop"),
	BIGMEEPLE("Big meeple");
	
	
	/**
	 * Name as String for a SpecialMeeple
	 */
	private final String name;

	/**
	 * Map of all existing SpecialMeepleTypes
	 */
	private static final Map<String, SpecialMeepleType> stringToSpecialMeepleType = new HashMap<>();

	static {
		for (SpecialMeepleType f : SpecialMeepleType.values()) {
			stringToSpecialMeepleType.put(f.toString(), f);
		}
	}

	/**
	 * Standard constructor of SpecialMeepleType
	 * 
	 * @param name which will be added to a single SpecialMeepleType
	 */
	private SpecialMeepleType(String name) {
		this.name = name;
	}

	/**
	 * Returns a String for the SpecialMeepleType
	 */
	@Override
	public String toString() {
		return name;
	}

	/**
	 * Returns a SpecialMeepleType for a String
	 * 
	 * @param s String which will be transformed into a SpecialMeepleType
	 * @return the SpecialMeepleType
	 */
	public static SpecialMeepleType fromString(String s) {
		return SpecialMeepleType.stringToSpecialMeepleType.get(s);
	}
}
