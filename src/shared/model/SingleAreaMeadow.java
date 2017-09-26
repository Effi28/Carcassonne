package shared.model;

import shared.enums.CardAreaType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Represents a SingleArea Meadow.
 * <p/>
 * Represents a specific SingleArea with CardAreaType Meadow. Overwrites
 * methods that need to be overridden from the superclass AbstractSingleArea.
 * The constructor provides a list of towns which are adjacent to the meadow.
 *
 * @see AbstractSingleArea
 * @see SingleAreaCloister
 * @see SingleAreaRoad
 * @see SingleAreaTown
 * @see SingleAreaType
 */
public final class SingleAreaMeadow extends AbstractSingleArea implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * List of towns that are neighbored to this singleArea
	 */
	private List<SingleAreaTown> towns;

	/**
	 * List of cloisters in this meadow (for the Bishop extension)
	 */
	private List<SingleAreaCloister> cloisters;

	/**
	 * List of positions of the cards that belong to this singleArea
	 */
	private List<Position> meadowPositions;

	/**
	 * Constructor which is invoked if no meeple was set on this area.
	 *
	 * @param towns     towns adjacent to meadow
	 * @param pos       Position of the towns
	 * @param cloisters cloister within meadow
	 */
	public SingleAreaMeadow(List<SingleAreaTown> towns, List<SingleAreaCloister> cloisters, Position pos) {
		setupSingleArea(towns, cloisters, pos);
	}

	/**
	 * Constructor which is invoked if the user set a meeple on the singleArea.
	 *
	 * @param meeple
	 * @param towns
	 * @param pos
	 */
	public SingleAreaMeadow(Meeple meeple, List<SingleAreaTown> towns, List<SingleAreaCloister> cloisters, Position pos) {
		super(meeple);
		setupSingleArea(towns, cloisters, pos);
	}

	@Override
	public SingleAreaMeadow deepCopy() {
		boolean newFinished = super.isFinished();
		List<Meeple> newMeeples = new ArrayList<>();
		for (Meeple meeple : super.getMeeples()) {
			newMeeples.add(meeple);
		}
		CardAreaType newAreaType = super.getCardAreaType();

		List<SingleAreaTown> newTowns = new ArrayList<>();
		for (SingleAreaTown town : towns) {
			newTowns.add(town.deepCopy());
		}

		List<SingleAreaCloister> newCloisters = new ArrayList<>();
		for (SingleAreaCloister cloister : cloisters) {
			newCloisters.add(cloister.deepCopy());
		}

		List<Position> newPositions = new ArrayList<>();
		for (Position position : meadowPositions) {
			newPositions.add(position.deepCopy());
		}

		SingleAreaMeadow newArea = new SingleAreaMeadow(newTowns, newCloisters, null);
		if (newFinished) {
			newArea.setFinished();
		}

		newArea.setMeeples(newMeeples);
		newArea.setCardAreaType(newAreaType);
		newArea.setTowns(newTowns);
		newArea.setMeadowPositions(newPositions);

		return newArea;
	}

	/**
	 * Evaluates the score when the game is finished.
	 * Increments the points
	 * by 3 if the area contains a finished SingleAreaTown.
	 *
	 * @return points the calculated points for this single area
	 */
	@Override
	public int calculatePoints() {
		int points = 0;
		for (Iterator<SingleAreaTown> iterator = towns.iterator(); iterator.hasNext(); ) {
			SingleAreaTown town = iterator.next();
			if (town.isFinished()) {
				points += 3;
			}
		}
		return points;
	}

	/**
	 * Overwritten toString() method for checking for equality instead of using
	 * 'instanceof'.
	 */
	@Override
	public String toString() {
		return "Meadow";
	}

	/**
	 * Sets up the SingleAreaMeadow.
	 * It is invoked by both constructors because the only difference between them is that
	 * one constructor is invoked when a meeple was set and the other one when no meeple was set.
	 *
	 * @param towns
	 * @param cloisters
	 * @param pos
	 */
	private void setupSingleArea(List<SingleAreaTown> towns, List<SingleAreaCloister> cloisters, Position pos) {
		if (towns != null) {
			this.towns = towns;
		} else {
			this.towns = new ArrayList<SingleAreaTown>();
		}

		if (cloisters != null) {
			this.cloisters = cloisters;
		} else {
			this.cloisters = new ArrayList<SingleAreaCloister>();
		}

		meadowPositions = new ArrayList<>();
		meadowPositions.add(pos);
		super.setCardAreaType(CardAreaType.MEADOW);
	}

	/*
	 * Getter and setter
	 */

	public List<SingleAreaTown> getTowns() {
		return towns;
	}

	public void setTowns(List<SingleAreaTown> newTowns) {
		towns = newTowns;
	}

	public List<Position> getMeadowPositions() {
		return meadowPositions;
	}

	public void setMeadowPositions(List<Position> positions) {
		meadowPositions = positions;
	}

	public List<SingleAreaCloister> getCloisters() {
		return cloisters;
	}
}
