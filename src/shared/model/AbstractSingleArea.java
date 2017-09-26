package shared.model;

import shared.enums.CardAreaType;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class serving as superclass for each type of SingleArea.
 * <p/>
 * This class is an abstract class serving the SingleArea methods which are
 * the same for all specific SingleAreas.
 * E.g. calculatePoints(), and it provides a list of
 * meeples which are put on the singleArea.
 *
 * @see CardAreaType
 * @see SingleAreaTown
 * @see SingleAreaMeadow
 * @see SingleAreaRoad
 * @see SingleAreaCloister
 * @see Meeple
 */
public abstract class AbstractSingleArea implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * AreaType of an instance of this class
	 */
	private CardAreaType cardAreaType;

	/**
	 * Meeples that are set on this single area
	 */
	private List<Meeple> meeples;

	/**
	 * Variable that is true, if single area is finished, false if not
	 */
	private boolean finished;

	/**
	 * Indicates if the points were added to the score
	 */
	private boolean pointsGiven;

	/**
	 * Standard constructor of a SingleArea
	 */
	public AbstractSingleArea() {
		meeples = new ArrayList<>();
		finished = false;
		pointsGiven = false;
	}

	/**
	 * Constructor which is called if a meeple is put on the singleArea
	 *
	 * @param meeple Meeple which is added
	 */
	public AbstractSingleArea(Meeple meeple) {
		this();
		meeples.add(meeple);
	}

	/**
	 * Returns a deep copy of the instance on which it is invoked.
	 * Has to be overridden by each type of single area.
	 *
	 * @return deep copy of the instance.
	 */
	public abstract AbstractSingleArea deepCopy();

	@Override
	public AbstractSingleArea clone() {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			new ObjectOutputStream(baos).writeObject(this);

			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

			return (AbstractSingleArea) new ObjectInputStream(bais).readObject();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Calculates the point of the SingleArea.
	 * Has to be overridden by every subclass.
	 *
	 * @return score the SingleArea gives
	 */
	public abstract int calculatePoints();

	
	
	/*
	 * Getter and Setter
	 */

	public void setCardAreaType(CardAreaType cardAreaType) {
		this.cardAreaType = cardAreaType;
	}

	public CardAreaType getCardAreaType() {
		return cardAreaType;
	}

	public List<Meeple> getMeeples() {
		return meeples;
	}

	public void setMeeples(List<Meeple> meepleList) {
		meeples = meepleList;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished() {
		finished = true;
	}
}
