package shared.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Class represents the logical GameField of the game.
 * <p>
 * This Class represents the GameField of one specific game by using a Map with
 * a Position as key and the Card as Value. Obtains methods like checkNeighbors
 * of currentCard, add a current Card to the Map and a method which checks if
 * the current Position is taken
 * 
 * @see Card
 * @see Game
 * @see ServerGame
 * @see ClientGame
 */
public final class GameField implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * This map represents the gameField, which means the cards and their
	 * position on the field
	 */
	private Map<Position, Card> gameField;

	/**
	 * the constructor that creates a new instance of the class gameField
	 */
	public GameField() {
		this.gameField = new HashMap<Position, Card>();

	}
	
	public GameField deepCopy(){
		List<Position> positions = new ArrayList<>();
		for(Position pos : gameField.keySet()){
			positions.add(pos.deepCopy());
		}
		
		List<Card> cards = new ArrayList<>();
		for(Card card : gameField.values()){
			cards.add(card.deepCopyWithoutSingleAreas());
		}
		
		Map<Position, Card> newGameFieldMap = new HashMap<>();
		for(int i = 0; i < positions.size(); i++){
			newGameFieldMap.put(positions.get(i), cards.get(i));
		}
		
		GameField newGameField = new GameField();
		newGameField.setGameField(newGameFieldMap);
		
		return newGameField;
	}
	
	@Override
	public GameField clone(){
		try{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			new ObjectOutputStream( baos ).writeObject(this);

			ByteArrayInputStream bais =
					new ByteArrayInputStream( baos.toByteArray() );

			return (GameField) new ObjectInputStream(bais).readObject();
		}
		catch(ClassNotFoundException | IOException e){
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * returns the card that is the corresponding value to the upper position in
	 * the gameFieldMap
	 * 
	 * @param pos
	 *            the position from which is started
	 * @return Card the card on the upper position, null if there is no card
	 */
	public Card getCardNorth(Position pos) {
		Position tmp = new Position(pos.getX(), pos.getY() - 1);
		if (gameField.containsKey(tmp)) {
			return gameField.get(tmp);
		}
		return null;
	}

	/**
	 * returns the card that is the corresponding value to the lower position in
	 * the gameFieldMap
	 * 
	 * @param pos
	 *            the position from which is started
	 * @return Card the card of the lower position, null if the is no card
	 */
	public Card getCardSouth(Position pos) {
		Position tmp = new Position(pos.getX(), pos.getY() + 1);
		if (gameField.containsKey(tmp)) {
			return gameField.get(tmp);
		}
		return null;
	}

	/**
	 * returns the card that is the corresponding value to the left position in
	 * the gameFieldMap
	 * 
	 * @param pos
	 *            the position from which is started
	 * @return Card the card of the left position, null if there is no card
	 */
	public Card getCardWest(Position pos) {
		Position tmp = new Position(pos.getX() - 1, pos.getY());
		if (gameField.containsKey(tmp)) {
			return gameField.get(tmp);
		}
		return null;
	}

	/**
	 * returns the card that is the corresponding value to the right position in
	 * the gameFieldMap
	 * 
	 * @param pos
	 *            the position from which is started
	 * @return Card the card of the right position, null if there is no card
	 */
	public Card getCardEast(Position pos) {
		Position tmp = new Position(pos.getX() + 1, pos.getY());
		if (gameField.containsKey(tmp)) {
			return gameField.get(tmp);
		}
		return null;
	}

	/**
	 * method checks neighbors of card user want to put on the map. If there is
	 * no neighbor method returns false else true
	 * 
	 * @param pos
	 * @return boolean if there is a neighbour
	 */
	public boolean checkNeighbours(Position pos) {
		if (getCardNorth(pos) != null || getCardWest(pos) != null
				|| getCardSouth(pos) != null || getCardEast(pos) != null) {
			return true;
		}
		return false;
	}

	/**
	 * checks if card on the position pos is fully surrounded by cards by
	 * calling the getCardEast, -West, -North and -South methods and by checking
	 * if the gameFieldMap contains card on the corner-positions
	 * 
	 * @param pos
	 *            the position of the card that should be checked
	 * @return boolean true if surrounded by eight cards, false if not
	 */
	public boolean checkSurroundingCards(Position pos) {
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				if (!(i == 0 && j == 0)) {
					Position temp = new Position(pos.getX() + i, pos.getY() + j);
					if (!(gameField.containsKey(temp))) {
						return false;
					}
				}
			}
		}
		return true;

	}

	public int countSurroundingCards(Position pos) {
		int counter = 0;
		Position tmp1 = new Position(pos.getX() - 1, pos.getY() - 1);
		Position tmp2 = new Position(pos.getX() + 1, pos.getY() - 1);
		Position tmp3 = new Position(pos.getX() - 1, pos.getY() + 1);
		Position tmp4 = new Position(pos.getX() + 1, pos.getY() + 1);
		if (getCardSouth(pos) != null) {
			counter += 1;
		}
		if (getCardNorth(pos) != null) {
			counter += 1;
		}
		if (getCardWest(pos) != null) {
			counter += 1;
		}
		if (getCardEast(pos) != null) {
			counter += 1;
		}
		if (gameField.containsKey(tmp1)) {
			counter += 1;
		}
		if (gameField.containsKey(tmp2)) {
			counter += 1;
		}
		if (gameField.containsKey(tmp3)) {
			counter += 1;
		}
		if (gameField.containsKey(tmp4)) {
			counter += 1;
		}
		return counter;
	}

	/**
	 * method returns the value of the gameFieldMap at a specific position(key)
	 * 
	 * @param pos
	 * @return the value of the gameFieldMap at a specific position(key)
	 */
	public Card getCard(Position pos) {
		return gameField.get(pos);
	}

	/**
	 * method puts a card in the map with the position as key
	 * 
	 * @param pos
	 * @param currentCard
	 */
	public void addCard(Position pos, Card currentCard) {
		gameField.put(pos, currentCard);
	}

	/**
	 * method deletes a card in the map with the position as key
	 * 
	 * @param pos
	 *            which should be deleted
	 */
	public void deleteCard(Position pos) {
		gameField.remove(pos);
	}

	/**
	 * method checks if Map contains a specific key
	 * 
	 * @param pos
	 * @return boolean if map contains key or not
	 */
	public boolean isPositionTaken(Position pos) {
		return gameField.containsKey(pos);
	}

	/**
	 * method returns positions in current game which are empty and adjacent to
	 * a Card
	 * 
	 * @return empty positions which are adjacent to a Card
	 */
	public Set<Position> getPositionsFromSetCards() {
		Set<Position> fieldsToPlaceCard = new HashSet<>();
		Set<Position> positions = gameField.keySet();
		for (Position p : positions) {

			Position right = new Position(p.getX() + 1, p.getY());
			if (!isPositionTaken(right)) {
				fieldsToPlaceCard.add(right);
			}
			Position bottom = new Position(p.getX(), p.getY() + 1);
			if (!isPositionTaken(bottom)) {
				fieldsToPlaceCard.add(bottom);
			}
			Position top = new Position(p.getX(), p.getY() - 1);
			if (!isPositionTaken(top)) {
				fieldsToPlaceCard.add(top);
			}
			Position left = new Position(p.getX() - 1, p.getY());
			if (!isPositionTaken(left)) {
				fieldsToPlaceCard.add(left);
			}
		}
		return fieldsToPlaceCard;

	}

	/*
	 * getter for the carcassonne ai to know about the gameField
	 */

	public Map<Position, Card> getGameField() {
		return gameField;
	}

	public void setGameField(Map<Position, Card> gameField){
		this.gameField = gameField;
	}
}
