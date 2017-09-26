package shared.model;

import java.io.Serializable;

/**
 * This class provides the positioning of each card at the GameField.
 * <p>
 * It overrides hashCode() and equals() because Position works as a key for each
 * card in a HashMap
 * 
 * 
 * @Version 06.11.2013
 * 
 */
public final class Position implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * the x value of the position
	 */
	private final int x;

	/**
	 * the y value of the position
	 */
	private final int y;

	/**
	 * the constructor that creates a new instance of the class Position
	 * 
	 * @param x
	 *            the x value of the position
	 * @param y
	 *            the y value of the position
	 */
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}



	/*
	 * Getter & Setter
	 */

	/**
	 * Returns a deep copy of the instance on which it is invoked.
	 * 
	 * @return deep copy of the instance
	 */
	public Position deepCopy() {
		int newX = x;
		int newY = y;
		return new Position(newX, newY);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
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
		Position other = (Position) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}
	public Position getLocation() {
		return new Position(x, y);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	@Override
	public String toString() {
		return "(" + String.valueOf(x) + "/" + String.valueOf(y) + ")";
	}
}
