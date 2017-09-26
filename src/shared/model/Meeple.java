package shared.model;

import java.io.Serializable;

import org.json.JSONObject;

import server.model.serverCommunication.utility.ServerMessageBuilder;
import shared.enums.SpecialMeepleType;

/**
 * This immutable class provides the Meeples for the Player
 * 
 * @Version 20.11.2013
 * 
 */
public final class Meeple implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Indicates on which card the meeple is set.
	 */
	private Position position;

	/**
	 * Indicated where on the card the meeple is set.
	 */
	private int placement;

	/**
	 * Indicates who is the owner of the meeple.
	 */
	private Player owner;

	/**
	 * special type of meeple
	 */
	private SpecialMeepleType specialType;

	/**
	 * Standard constructor of a Meeple
	 * 
	 * @param position
	 *            Position of the Card where the Meeple is set.
	 * @param placement
	 *            Placement of the Meeple in the specific Card.
	 * @param owner
	 *            Owner of the Meeple.
	 */
	public Meeple(Position position, int placement, Player owner, SpecialMeepleType type) {
		this.position = position;
		this.placement = placement;
		this.owner = owner;
		this.specialType = type;
	}

	/**
	 * Returns a deep copy of the meeple instance.
	 * 
	 * @return deep copy of the instance
	 */
	public Meeple deepCopy() {
		Position newPosition = position.deepCopy();
		Player newPlayer = owner.deepCopy();
		return new Meeple(newPosition, placement, newPlayer, specialType);
	}

	/**
	 * method creates a JSONObject that contains the information about a meeple
	 * placement by calling the createMeepleInformation-method of the
	 * ServerMessageBuilder
	 * 
	 * @return JSONObject
	 */
	public JSONObject getMeepleInformation() {
		return ServerMessageBuilder.createMeepleInformation(position.getX(), position.getY(), placement);
	}

	/**
	 * get-method for the position of a meeple
	 * 
	 * @return position of the Meeple
	 */
	public Position getPosition() {
		return position;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((owner == null) ? 0 : owner.hashCode());
		result = prime * result + placement;
		result = prime * result + ((position == null) ? 0 : position.hashCode());
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
		Meeple other = (Meeple) obj;
		if (owner == null) {
			if (other.owner != null)
				return false;
		} else if (!owner.equals(other.owner))
			return false;
		if (placement != other.placement)
			return false;
		if (position == null) {
			if (other.position != null)
				return false;
		} else if (!position.equals(other.position))
			return false;
		return true;
	}

	public Player getOwner() {
		return owner;

	}

	public SpecialMeepleType getSpecialType() {
		return specialType;
	}

	@Override
	public String toString() {
		return owner.getNick();
	}
}
