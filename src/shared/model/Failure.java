package shared.model;

/**
 * Wraps a String with a fail message.
 * This class is helpful because it's possible to display
 * the message at the correct spot in the view with the observer-pattern.
 * 
 * @version 07.01.2014
 *
 */
public final class Failure {

	private String name;

	public Failure(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
