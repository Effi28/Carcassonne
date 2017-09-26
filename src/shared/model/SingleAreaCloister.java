package shared.model;

import shared.enums.CardAreaType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a SingleArea Cloister
 * <p/>
 * Represents a specific SingleArea with CardAreaType Cloister.
 * Overwrites methods of the superclass AbstractSingleArea.
 * In this type of singleArea the method calculatePoints() checks the surrounding 8 cards.
 *
 * @see AbstractSingleArea
 * @see SingleAreaMeadow
 * @see SingleAreaRoad
 * @see SingleAreaTown
 * @see SingleAreaType
 */
public final class SingleAreaCloister extends AbstractSingleArea implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Position of the SingleArea
	 */
	private Position position;

	/**
	 * Displays how many points the owner of this SingleArea will get
	 */
	private int pointCounter;

	/**
	 * Constructor which gets a specific position
	 *
	 * @param position the position of the SingleArea
	 */
	public SingleAreaCloister(Position position) {
		this.pointCounter = 1;
		this.setPosition(position);
		super.setCardAreaType(CardAreaType.CLOISTER);
	}

	/**
	 * Constructor that is invoked when a meeple is set.
	 *
	 * @param meeple   The meeple that is set
	 * @param position position of the singleArea
	 */
	public SingleAreaCloister(Meeple meeple, Position position) {
		//TODO macht der konstruktor nicht fast des gleiche wie der andre aus mit dem meeple??
		// warum fehlt pointcounter = 1;
		super(meeple);
		this.setPosition(position);
		super.setCardAreaType(CardAreaType.CLOISTER);
	}

	public SingleAreaCloister deepCopy() {
		Position newPosition = position.deepCopy();
		int newPointCounter = pointCounter;
		boolean newFinished = super.isFinished();
		List<Meeple> newMeeples = new ArrayList<>();
		for (Meeple meeple : super.getMeeples()) {
			newMeeples.add(meeple);
		}
		CardAreaType newAreaType = super.getCardAreaType();

		SingleAreaCloister newCloister = new SingleAreaCloister(newPosition);
		newCloister.setPointCounter(newPointCounter);
		if (newFinished) {
			newCloister.setFinished();
		}

		newCloister.setMeeples(newMeeples);
		newCloister.setCardAreaType(newAreaType);

		return newCloister;
	}

	/**
	 * Evaluates the score of this area, when it's finished
	 */
	@Override
	public int calculatePoints() {
		return pointCounter;
	}

	/**
	 * overwritten toString method for checking for equality instead of using
	 * instanceof
	 */
	@Override
	public String toString() {
		return "Cloister";
	}

	/*
	 * Getter and Setter
	 */

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	@Override
	public void setFinished() {
		super.setFinished();
		pointCounter = 9;
	}

	public void setPointCounter(int pointC) {
		this.pointCounter = pointC;
	}
}
