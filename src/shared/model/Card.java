package shared.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.scene.image.Image;

import shared.enums.CardAreaType;

/**
 * Represents a single card in the game.
 * <p>
 * It provides four factory-methods for creating an object with or without bonus or
 * multiplier a rotate-method and a method that gets the image of a card by the
 * ID.
 * 
 * @see CardAreaType
 */
public final class Card{


	/**
	 * List of CardAreaTypes represents the different areas of the card
	 */
	private final List<CardAreaType> areas;

	/**
	 * this list of Integer-ArrayLists represent the edges using the indices of
	 * the areas-array
	 */
	private List<ArrayList<Integer>> edges;

	/**
	 * this list of Integer-ArrayLists represent the adjacency of the different
	 * areas with the indices of the areas-array
	 */
	private final List<ArrayList<Integer>> adjacency;

	/**
	 * this array of the type integer represents whether a card has a
	 * bonus-field and if it has one it contains the index of the bonus-area
	 */
	private List<Integer> bonus;

	private List<Integer> multiplier;

	/**
	 * the value of this integer is the number of times that the card is rotated
	 * clockwise
	 */
	private int rotation;

	/**
	 * this map has indices of the areas-array as key and an instance of
	 * AbstractSingleArea as value
	 */
	private Map<Integer, AbstractSingleArea> singleAreaMap = new HashMap<>();

	/**
	 * constructor that creates an instance of the class Card
	 * 
	 * @param areas
	 *            the areas-array of this card
	 * @param edges
	 *            the edges-array of this card
	 * @param adjacency
	 *            the adjacency-array of this card
	 */
	private Card(List<CardAreaType> areas, List<ArrayList<Integer>> edges, List<ArrayList<Integer>> adjacency) {
		this.areas = areas;
		this.edges = edges;
		this.adjacency = adjacency;
		this.bonus = new ArrayList<>();
		this.multiplier = new ArrayList<>();
		this.rotation = 0;
	}

	/**
	 * alternative constructor that is called when the card has a bonus-area
	 * 
	 * @param areas
	 *            the areas-array of this card
	 * @param edges
	 *            the edges-array of this card
	 * @param adjacency
	 *            the adjacency-array of this card
	 * @param bonus
	 *            the bonus-array of this card
	 */
	private Card(List<CardAreaType> areas, List<ArrayList<Integer>> edges, List<ArrayList<Integer>> adjacency,
			List<Integer> bonus) {
		this(areas, edges, adjacency);
		this.bonus = bonus;
	}

	private Card(List<CardAreaType> areas, List<ArrayList<Integer>> edges, List<ArrayList<Integer>> adjacency,
			List<Integer> bonus, List<Integer> multiplier) {
		this(areas, edges, adjacency, bonus);
		this.multiplier = multiplier;
	}

	/**
	 * Returns a deepCopy of the actual card.
	 * 
	 * @return deepCopy of the card
	 */
	public Card deepCopy() {
		// Copy the areas
		List<CardAreaType> areasCopy = new ArrayList<>();
		for (CardAreaType a : this.areas) {
			areasCopy.add(a);
		}

		// Copy the edges
		List<ArrayList<Integer>> edgesCopy = new ArrayList<>();
		for (ArrayList<Integer> l : this.edges) {
			ArrayList<Integer> oneEdgeCopy = new ArrayList<Integer>();

			for (Integer i : l) {
				oneEdgeCopy.add(i);
			}
			edgesCopy.add(oneEdgeCopy);
		}

		// Copy the adjacencies
		List<ArrayList<Integer>> adjacencyCopy = new ArrayList<>();
		for (ArrayList<Integer> l : this.adjacency) {
			ArrayList<Integer> oneAdjacencyCopy = new ArrayList<Integer>();

			for (Integer i : l) {
				oneAdjacencyCopy.add(i);
			}
			adjacencyCopy.add(oneAdjacencyCopy);
		}

		// Copy the bonus
		List<Integer> bonusCopy = new ArrayList<Integer>();
		for (Integer i : this.bonus) {
			bonusCopy.add(i);
		}

		// Copy the multiplier
		List<Integer> multiplierCopy = new ArrayList<>();
		for (Integer i : this.multiplier) {
			multiplierCopy.add(i);
		}
		
		//Copy the singleAreaMap
		List<Integer> keySet = new ArrayList<>();
		for(Integer i : this.singleAreaMap.keySet()){
			keySet.add(i);
		}
		List<AbstractSingleArea> values = new ArrayList<>();
		for(AbstractSingleArea area : this.singleAreaMap.values()){
			values.add(area.deepCopy());
		}
		Map<Integer, AbstractSingleArea> newSingleAreaMap = new HashMap<>();
		for(int i = 0; i < keySet.size(); i++){
			newSingleAreaMap.put(keySet.get(i), values.get(i));
		}

		Card newCard = new Card(areasCopy, edgesCopy, adjacencyCopy, bonusCopy, multiplierCopy);
		newCard.setSingleAreaMap(newSingleAreaMap);
		
		return newCard;
	}

	public Card deepCopyWithoutSingleAreas() {
		// Copy the areas
		List<CardAreaType> areasCopy = new ArrayList<>();
		for (CardAreaType a : this.areas) {
			areasCopy.add(a);
		}

		// Copy the edges
		List<ArrayList<Integer>> edgesCopy = new ArrayList<>();
		for (ArrayList<Integer> l : this.edges) {
			ArrayList<Integer> oneEdgeCopy = new ArrayList<Integer>();

			for (Integer i : l) {
				oneEdgeCopy.add(i);
			}
			edgesCopy.add(oneEdgeCopy);
		}

		// Copy the adjacencies
		List<ArrayList<Integer>> adjacencyCopy = new ArrayList<>();
		for (ArrayList<Integer> l : this.adjacency) {
			ArrayList<Integer> oneAdjacencyCopy = new ArrayList<Integer>();

			for (Integer i : l) {
				oneAdjacencyCopy.add(i);
			}
			adjacencyCopy.add(oneAdjacencyCopy);
		}

		// Copy the bonus
		List<Integer> bonusCopy = new ArrayList<Integer>();
		for (Integer i : this.bonus) {
			bonusCopy.add(i);
		}

		// Copy the multiplier
		List<Integer> multiplierCopy = new ArrayList<>();
		for (Integer i : this.multiplier) {
			multiplierCopy.add(i);
		}
		
		//Copy the singleAreaMap
		List<Integer> keySet = new ArrayList<>();
		for(Integer i : this.singleAreaMap.keySet()){
			keySet.add(i);
		}
		List<AbstractSingleArea> values = new ArrayList<>();
		for(AbstractSingleArea area : this.singleAreaMap.values()){
			values.add(area);
		}
		Map<Integer, AbstractSingleArea> newSingleAreaMap = new HashMap<>();
		for(int i = 0; i < keySet.size(); i++){
			newSingleAreaMap.put(keySet.get(i), values.get(i));
		}

		Card newCard = new Card(areasCopy, edgesCopy, adjacencyCopy, bonusCopy, multiplierCopy);
		newCard.setSingleAreaMap(newSingleAreaMap);
		
		return newCard;
	}
	
	public void setSingleAreaEntry(AbstractSingleArea oldArea, AbstractSingleArea newArea){
		Map<Integer, AbstractSingleArea> newSingleAreaMap = new HashMap<>();
		List<AbstractSingleArea> newValues = new ArrayList<>();
		for(AbstractSingleArea area : singleAreaMap.values()){
			if(area == oldArea){
				newValues.add(newArea);
			}
			else{
				newValues.add(area);
			}
		}
		int counter = 0;
		for(Integer i : singleAreaMap.keySet()){
			newSingleAreaMap.put(i, newValues.get(counter++));
		}
		singleAreaMap = newSingleAreaMap;
	}
	/**
	 * this is a factory-method thats creates an object of the class card that
	 * has no bonus and no multiplier
	 * 
	 * @param areas
	 * @param edges
	 * @param adjacency
	 * @return Card
	 */
	public static Card buildStandardCard(List<CardAreaType> areas, List<ArrayList<Integer>> edges,
			List<ArrayList<Integer>> adjacency) {
		return new Card(areas, edges, adjacency);
	}

	/**
	 * this is a factory-method thats creates an object of the class card that
	 * has a bonus but no multiplier
	 * 
	 * @param areas Areas of the Card
	 * @param edges Edges of the Card
	 * @param adjacency Adjacency of the Card
	 * @param bonus Bonus of the Card
	 * @return Card
	 */
	public static Card buildBonusCard(List<CardAreaType> areas, List<ArrayList<Integer>> edges,
			List<ArrayList<Integer>> adjacency, List<Integer> bonus) {
		return new Card(areas, edges, adjacency, bonus);
	}

	/**
	 * this is a factory-method thats creates an object of the class card that
	 * has bonus and multiplier
	 * 
	 * @param areas Areas of the Card
	 * @param edges Edges of the Card
	 * @param adjacency Adjacency of the Card
	 * @param bonus Bonus of the Card
	 * @param multiplier Multiplier of the Card
	 * @return Card
	 */
	public static Card buildBonusMultiplierCard(List<CardAreaType> areas, List<ArrayList<Integer>> edges,
			List<ArrayList<Integer>> adjacency, List<Integer> bonus, List<Integer> multiplier) {
		return new Card(areas, edges, adjacency, bonus, multiplier);
	}

	/**
	 * this is a factory-method thats creates an object of the class card that
	 * has no bonus but a multiplier
	 * 
	 * @param areas Areas of the Card
	 * @param edges Edges of the Card
	 * @param adjacency Adjacency of the Card
	 * @param multiplier Multiplier of the Card
	 * @return Card
	 */
	public static Card buildMultiplierCard(List<CardAreaType> areas, List<ArrayList<Integer>> edges,
			List<ArrayList<Integer>> adjacency, List<Integer> multiplier) {
		return new Card(areas, edges, adjacency, new ArrayList<Integer>(), multiplier);
	}

	/**
	 * this method moves the different parts of the edges-array based on the
	 * rotation-integer
	 * 
	 * @param rotation
	 *            value of the rotation. 0 <= r <= 3
	 */
	public void rotate(int rotation) {

		if (rotation != this.rotation) {
			List<ArrayList<Integer>> newEdges = new ArrayList<ArrayList<Integer>>();
			newEdges.addAll(edges);

			if (rotation > 3) {
				rotation = rotation % 4;
			}

			int oldRotation = this.rotation;
			this.rotation = rotation;
			if (rotation == 0) {
				rotation = 4;
			}
			rotation = Math.abs(oldRotation - rotation);

			for (int i = 0; i < edges.size(); i++) {
				int rotate = i - rotation;
				if (rotate < 0) {
					rotate = 4 - Math.abs(rotate);
				}
				newEdges.set(rotate, edges.get(i));
			}
			edges = newEdges;
		}
	}

	/**
	 * R 	eturns the image that is connected with the parameter ID
	 * 
	 * @param ID
	 *            the ID of a certain image of
	 * @return cardImage the Image that is referenced by the ID
	 */
	public Image getImage(String ID) {
		Image cardImage = null;
		cardImage = new Image("files/cardimages" + ID + "png");
		return cardImage;
	}

	// Getter & Setter

	public void setRotation(int i) {
		this.rotation = i;
	}

	public int getRotation() {
		return rotation;
	}

	public List<ArrayList<Integer>> getEdges() {
		return this.edges;
	}

	public Map<Integer, AbstractSingleArea> getSingleAreaMap() {
		return singleAreaMap;
	}

	public List<CardAreaType> getAreas() {
		return this.areas;
	}

	public List<Integer> getBonus() {
		return bonus;
	}

	public List<Integer> getMultiplier() {
		return multiplier;
	}

	public List<ArrayList<Integer>> getAdjacency() {
		return adjacency;
	}
	
	private void setSingleAreaMap(Map<Integer, AbstractSingleArea> newMap){
		this.singleAreaMap = newMap;
	}
}
