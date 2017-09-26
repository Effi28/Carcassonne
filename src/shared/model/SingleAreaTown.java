package shared.model;

import shared.enums.CardAreaType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Class represents a SingleArea Town
 * <p/>
 * This class represents a specific SingleArea with CardAreaType Town.
 * Overwrites the abstract methods of AbstractSingleArea. Checks if a town is
 * finished by counting the openEdges which contain town.
 *
 * @see AbstractSingleArea
 * @see SingleAreaMeadow
 * @see SingleAreaRoad
 * @see SingleAreaCloister
 * @see SingleAreaType
 */
public final class SingleAreaTown extends AbstractSingleArea implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Integer-value of the score that the player will get if he is the owner of
	 * this SingleArea
	 */
	private int pointCounter;

	/**
	 * Integer value that indicates the state of the singleArea. Represents the
	 * edges of the SingleAreaTown that are still open. In order to finish the
	 * SingleArea the counter has to be 0 so every edge of a town card is
	 * closed.
	 */
	private int openEdgesCounter;

	/**
	 * List of positions of the cards that belong to this singleArea.
	 */
	private List<Position> townPositions;

	private int multiplier;

	/**
	 * Constructor is invoked when no meeple was set on this SingleArea.
	 *
	 * @param openEdgesCounter
	 * @param bonus
	 * @param position
	 */
	public SingleAreaTown(int openEdgesCounter, boolean bonus, Position position, int multiplier) {
		setupSingleArea(openEdgesCounter, bonus, position, multiplier);
	}

	/**
	 * Constructor which is invoked if a meeple was set.
	 *
	 * @param meeple
	 * @param openEdgesCounter
	 * @param bonus
	 * @param position
	 */
	public SingleAreaTown(Meeple meeple, int openEdgesCounter, boolean bonus, Position position, int multiplier) {
		super(meeple);
		setupSingleArea(openEdgesCounter, bonus, position, multiplier);
	}

	@Override
	public SingleAreaTown deepCopy() {
		int newPointCounter = pointCounter;
		boolean newFinished = super.isFinished();
		List<Meeple> newMeeples = new ArrayList<>();
		for (Meeple meeple : super.getMeeples()) {
			newMeeples.add(meeple);
		}
		CardAreaType newAreaType = super.getCardAreaType();
		List<Position> newPositions = new ArrayList<>();
		for (Position position : townPositions) {
			newPositions.add(position.deepCopy());
		}

		SingleAreaTown newArea = new SingleAreaTown(openEdgesCounter, false, null, multiplier);
		newArea.setPointCounter(newPointCounter);
		if (newFinished) {
			newArea.setFinished();
		}

		newArea.setMeeples(newMeeples);
		newArea.setCardAreaType(newAreaType);
		newArea.setTownPosition(newPositions);

		return newArea;
	}

	/**
	 * Calculates the points for towns. Checks if start and finish of town is on
	 * same card and decreases counter by one.
	 */
	@Override
	public int calculatePoints() {

		int points = 0;

		int length = townPositions.size();
		Set<Position> townPositionsNoDuplicates = new HashSet<>(townPositions);
		int duplicates = (length - townPositionsNoDuplicates.size());

		if (isFinished()) {
			points = multiplier * pointCounter;
			points -= duplicates * multiplier;
		} else if (multiplier == 2) {
			points = pointCounter;
			points -= duplicates;
		}

		return points;
	}

	/**
	 * Overwritten toString() method for checking for equality instead of using
	 * 'instanceof'.
	 */
	@Override
	public String toString() {
		return "Town";
	}

	/**
	 * Sets up this SingleAreaTown. Is invoked by both constructor because they
	 * almost do the same.
	 *
	 * @param openEdgesCounter
	 * @param bonus
	 * @param position
	 * @param multiplier
	 */
	private void setupSingleArea(int openEdgesCounter, boolean bonus, Position position, int multiplier) {
		this.openEdgesCounter = openEdgesCounter;
		if (bonus) {
			pointCounter = 2;
		} else {
			pointCounter = 1;
		}

		this.multiplier = multiplier;
		townPositions = new ArrayList<>();
		townPositions.add(position);
		super.setCardAreaType(CardAreaType.TOWN);
	}

	/*
	 * Getter and Setter
	 */

	public List<Position> getTownPosition() {
		return townPositions;
	}

	public void setTownPosition(List<Position> townPosition) {
		this.townPositions = townPosition;
	}

	public int getPointCounter() {
		return pointCounter;
	}

	/**
	 * Sets the points. If the SingleArea is finished the points will be
	 * doubled.
	 *
	 * @param pointCounter new points if this SingleArea
	 */
	public void setPointCounter(int pointCounter) {
		this.pointCounter = pointCounter;
		if (isFinished()) {
			pointCounter *= 2;
		}
	}

	public int getOpenEdgesCounter() {
		return openEdgesCounter;
	}

	public void setOpenEdgesCounter(int openEdgesCounter) {
		this.openEdgesCounter = openEdgesCounter;
		if (openEdgesCounter == 0) {
			setFinished();
		}
	}

	public int getMultiplier() {
		return multiplier;
	}

	public void setMultiplier(int multiplier) {
		this.multiplier = multiplier;
	}
}
