package shared.model;

import shared.enums.SpecialMeepleType;

/**
 * Represents the placement of the meeple on a card.
 * Indicates if the meeple is a special meeple or not.
 *
 * @version 15.01.2014
 */
public final class Placement {

	/**
	 * Represents the placement in the 13 area representation in the client when meeple was placed there
	 */
	private int placement;
	/**
	 * Represents the placement for the client/server communication.
	 * Indicating the index of the area where the meeple was set.
	 */
	private int communicationPlacement;
	/**
	 * The type of special meeple the meeple is.
	 * Null if it is no special meeple.
	 */
	private SpecialMeepleType specialMeeple;

	public Placement(int placement) {
		this.placement = placement;
	}

	public void setSpecialMeeple(SpecialMeepleType specialMeeple) {
		this.specialMeeple = specialMeeple;
	}

	/**
	 * Gets the placement of the meeple.
	 *
	 * @return placement of the meeple
	 */
	public int getPlacement() {
		return placement;
	}

	public void setPlacement(int area) {
		placement = area;
	}

	/**
	 * Returns the type of the special meeple if a special meeple was set.
	 *
	 * @return type of the meeple
	 */
	public SpecialMeepleType getSpecialMeeple() {
		return specialMeeple;
	}

	public void setCommunicationPlacement(int placement) {
		this.communicationPlacement = placement;
	}

	public int getCommunicationPlacement() {
		return communicationPlacement;
	}
}
