package shared.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import server.model.serverCommunication.utility.ServerMessageBuilder;

/**
 * This class represents one Player.
 * <p>
 * It manages the nickname and score of each player and it has a boolean flag
 * which shows whether the user can play or not.
 * 
 * @version 04.12.2013
 * 
 */
public final class Player extends User implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * this integer-value represents the score of the player in the game
	 */
	private int score;

	/**
	 * this string represents the meeple-color of the player in the game
	 */
	private String color;

	/**
	 * this integer-value represents how many remaining meeples the player has
	 * got
	 */
	private int meeplesLeft;

	/**
	 * the set of meeples the player already set on the gameField
	 */
	private Set<Meeple> meeplesSet;

	/**
	 * indicates if bigmeeple has already been used or not
	 */
	private boolean bigMeepleUsed;

	/**
	 * Constructor is called when a new client connects and a new ClientHandler
	 * is created
	 * 
	 * @param nick
	 *            name of the Player
	 * @param color
	 *            color of the Player
	 */
	public Player(String nick, String color) {
		super(nick);
		this.color = color;
		this.meeplesSet = new HashSet<>();
		this.meeplesLeft = 7;
		this.bigMeepleUsed = false;
	}

	/**
	 * Creates gameInformation about a specific game.
	 * 
	 * @return json-Object with the game information
	 */
	public JSONObject getGameInformation() {

		JSONArray arr = new JSONArray();

		for (Meeple m : meeplesSet) {
			arr.put(m.getMeepleInformation());
		}

		JSONObject msg = ServerMessageBuilder.createPlayerInfo(super.getNick(), color, score, meeplesLeft, arr);

		return msg;
	}

	/**
	 * Adds a meeple to the meeplesSet
	 * 
	 * @param meeple
	 *            meeple which should be added.
	 * @return true if it was successful.
	 */
	public boolean addMeeple(Meeple meeple) {
		return meeplesSet.add(meeple);
	}

	/**
	 * Removes a meeple from meeplesSet.
	 * 
	 * @param list Position of the Meeple which should be deleted.
	 */
	public void removeMeeple(List<Meeple> list) {

		for (Meeple meepleToRemove : list) {
			for (Meeple meepleWhereToBeRemoved : meeplesSet) {
				if (meepleWhereToBeRemoved.equals(meepleToRemove)) {
					System.out.print("REMOVE MEEPLE");
					meeplesSet.remove(meepleWhereToBeRemoved);
				}
			}
		}
	}

	public void addToMeeplesLeft(int left) {
		this.meeplesLeft += left;
	}

	/**
	 * Places a meeple.
	 * Checks whether the player has enough meeples left.
	 * 
	 * @param meeple
	 * @param bigMeeple
	 * @return true if the player has enough meeples to set one, false if not
	 */
	public boolean placeMeeple(Meeple meeple, boolean bigMeeple) {
		if ((bigMeeple && bigMeepleUsed) || (!bigMeeple && meeplesLeft <= 0)) {
			return false;
		} else if (bigMeeple) {
			bigMeepleUsed = true;
		} else {
			this.meeplesLeft--;
		}
		addMeeple(meeple);
		return true;
	}

	/**
	 * Adds the given points to the score of the player.
	 * 
	 * @param points Points which should be added to the score
	 */
	public void addToScore(int points) {
		this.score += points;
	}

	/*
	 * GETTER and SETTER below
	 */

	@Override
	public String getNick() {
		return super.getNick();
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public int getScore() {
		return score;
	}

	public int getMeeplesLeft() {
		return meeplesLeft;
	}

	public Set<Meeple> getMeeplesSet() {
		return meeplesSet;
	}

	public void setScore(int score) {
		this.score = score;

	}

	public void setMeeplesLeft(int meeplesLeft) {
		this.meeplesLeft = meeplesLeft;
	}

	public Player deepCopy() {
		Player newPlayer = new Player(super.getNick(), color);
		newPlayer.score = score;
		newPlayer.meeplesLeft = meeplesLeft;
		Set<Meeple> newMeepleSet = new HashSet<>();
		for (Meeple meeple : meeplesSet) {
			newMeepleSet.add(meeple);
		}
		newPlayer.meeplesSet = newMeepleSet;

		return newPlayer;
	}

	@Override
	public String toString() {
		return super.getNick() + "(" + score + ")" + "(" + meeplesLeft + ")";
	}

	public boolean isBigMeepleUsed() {
		return bigMeepleUsed;
	}

	public void setBigMeepleUsed(boolean bigMeepleUsed) {
		this.bigMeepleUsed = bigMeepleUsed;
	}
}
