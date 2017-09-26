package shared.model;

import shared.enums.CardAreaType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a SingleArea Road.
 * <p/>
 * Represents a specific SingleArea with CardAreaType Road. Overwrites methods
 * of the superclass AbstractSingleArea that need to be overridden.
 *
 * @see AbstractSingleArea
 * @see SingleAreaCloister
 * @see SingleAreaMeadow
 * @see SingleAreaTown
 * @see SingleAreaType
 */
public final class SingleAreaRoad extends AbstractSingleArea implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Integer-value of the points that the player will get if he is the owner
	 * of this SingleArea.
	 */
	private int pointCounter;

	/**
	 * Representing if the road has an end. 1 if the road has an end. 0 if not.
	 */
	private int closed;

	/**
	 * List of positions of the cards that belong to this singleArea.
	 */
	private List<Position> roadPositions;

	private int multiplier;

	/**
	 * puts the position of the road in a ArrayList checks if card represents a
	 * road with a end on it or a middle-piece of the road
	 *
	 * @param end
	 * @param position
	 */
	public SingleAreaRoad(boolean end, Position position, int multiplier) {
		setupSingleArea(end, position, multiplier);
	}

	/**
	 * constructor is called if user put a meeple on singleArea
	 *
	 * @param meeple
	 * @param end
	 * @param position
	 */
	public SingleAreaRoad(Meeple meeple, boolean end, Position position, int multiplier) {
		super(meeple);
		setupSingleArea(end, position, multiplier);
	}

	@Override
	public SingleAreaRoad deepCopy() {
		int newPointCounter = pointCounter;
		boolean newFinished = super.isFinished();
		List<Meeple> newMeeples = new ArrayList<>();
		for (Meeple meeple : super.getMeeples()) {
			newMeeples.add(meeple);
		}
		CardAreaType newAreaType = super.getCardAreaType();
		int newClosed = closed;
		List<Position> newPositions = new ArrayList<>();
		for (Position position : roadPositions) {
			newPositions.add(position.deepCopy());
		}

		SingleAreaRoad newRoad = new SingleAreaRoad(false, null, multiplier);
		newRoad.setPointCounter(newPointCounter);
		if (newFinished) {
			newRoad.setFinished();
		}
		newRoad.setMeeples(newMeeples);
		newRoad.setCardAreaType(newAreaType);
		newRoad.setClosed(newClosed);
		newRoad.setRoadPosition(newPositions);

		return newRoad;
	}

	/**
	 * Evaluates the score when this SingleArea is finished
	 */
	@Override
	public int calculatePoints() {
		int points = 0;
		Set<Position> roadPositionsNoDuplicates = new HashSet<>(roadPositions);
		pointCounter = roadPositionsNoDuplicates.size();
		if (isFinished()) {
			points = pointCounter * multiplier;
		} else if (multiplier == 1) {
			points = pointCounter;
		}

		return points;
	}

	/**
	 * Overwritten toString method for checking for equality instead of using
	 * 'instanceof'
	 */
	@Override
	public String toString() {
		return "Road";
	}

	/**
	 * Sets up the SingleAreaRoad. It is invoked by both constructors because
	 * the only difference between them is that one constructor is invoked when
	 * a meeple was set and the other one when no meeple was set .
	 *
	 * @param end
	 * @param position
	 * @param multiplier
	 */
	private void setupSingleArea(boolean end, Position position, int multiplier) {
		if (end) {
			closed = 1;
		} else {
			closed = 0;
		}
		pointCounter = 1;
		this.multiplier = multiplier;
		roadPositions = new ArrayList<>();
		roadPositions.add(position);
		super.setCardAreaType(CardAreaType.ROAD);
	}

	/*
	 * Getter and Setter
	 */

	public List<Position> getRoadPosition() {
		return roadPositions;
	}

	public void setRoadPosition(List<Position> roadPosition) {
		this.roadPositions = roadPosition;
	}

	public int getPointCounter() {
		return pointCounter;
	}

	public void setPointCounter(int pointCounter) {
		this.pointCounter = pointCounter;
	}

	public int getClosed() {
		return closed;
	}

	public void setClosed(int closed) {
		this.closed = closed;
		if (closed == 2) {
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
