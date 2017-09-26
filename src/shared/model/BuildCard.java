package shared.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import shared.enums.CardAreaType;

/**
 * This utility class provides a method to build a card out of a JSONObject. Should not be instantiated throws exception
 * otherwise.
 */
public final class BuildCard {

	/**
	 * Error logger
	 */
	private static Logger log = LogManager.getLogger("ERROR");

	/**
	 * Only constructor which should not be called otherwise an exception is thrown. This is because this utility class
	 * shouldn't be instantiated.
	 * 
	 * @throws IllegalAccessException
	 *             if the constructor is called, which shouldn't happen
	 */
	private BuildCard() throws IllegalAccessException {
		log.error("BuildCard should no be instantiated");
		throw new IllegalAccessException(
				"BuildCard should not be instantiated!");
	}

	/**
	 * this method builds a card out of a single JSONOBject by iterating through the different JSONArrays and saving the
	 * content in a normal ArrayList
	 * 
	 * @param singleCard
	 *            the JSONObject of which the card-Object should be created
	 * @return card the card that is created based on the JSONObject
	 */
	public static Card buildCard(JSONObject singleCard) {

		JSONArray areasArray = singleCard.optJSONArray("areas");
		JSONArray edgesArray = singleCard.optJSONArray("edges");
		JSONArray adjacencyArray;
		JSONArray multiplierArray;
		JSONArray bonusArray;

		if(singleCard.optJSONArray("adjacency") == null){
			adjacencyArray = new JSONArray();
		}
		else{
			adjacencyArray = singleCard.optJSONArray("adjacency");
		}
		
		if (singleCard.optJSONArray("bonus") == null) {
			bonusArray = new JSONArray();
		} else {
			bonusArray = singleCard.optJSONArray("bonus");
		}

		if (singleCard.optJSONArray("multiplier") == null) {
			multiplierArray = new JSONArray();
		} else {
			multiplierArray = singleCard.optJSONArray("multiplier");
		}

		List<CardAreaType> areas = new ArrayList<>();
		List<ArrayList<Integer>> edges = new ArrayList<>();
		List<ArrayList<Integer>> adjacency = new ArrayList<>();
		List<Integer> bonus = new ArrayList<>();
		List<Integer> multiplier = new ArrayList<>();

		// Set areas
		for (int i = 0; i < areasArray.length(); i++) {
			CardAreaType areaType = null;
			try {
				areaType = CardAreaType.fromString(areasArray.getString(i));
			} catch (JSONException e) {
				log.error(e.getMessage());
			}
			if (CardAreaType.MEADOW.equals(areaType)) {
				areas.add(CardAreaType.MEADOW);
			} else if (CardAreaType.ROAD.equals(areaType)) {
				areas.add(CardAreaType.ROAD);
			} else if (CardAreaType.CLOISTER.equals(areaType)) {
				areas.add(CardAreaType.CLOISTER);
			} else if (CardAreaType.TOWN.equals(areaType)) {
				areas.add(CardAreaType.TOWN);
			}
		}

		// Set edges
		for (int i = 0; i < edgesArray.length(); i++) {
			ArrayList<Integer> singleEdgeList = new ArrayList<Integer>();
			JSONArray singleEdge;
			try {
				singleEdge = edgesArray.getJSONArray(i);
				for (int j = 0; j < singleEdge.length(); j++) {
					int valueEdge = singleEdge.getInt(j);
					singleEdgeList.add(valueEdge);
				}
			} catch (JSONException e) {
				log.error(e.getMessage());
			}
			edges.add(singleEdgeList);
		}

		// Set adjacencies
		for (int i = 0; i < adjacencyArray.length(); i++) {
			ArrayList<Integer> singleAdjacenyList = new ArrayList<Integer>();
			JSONArray singleAdjacency;
			try {
				singleAdjacency = adjacencyArray.getJSONArray(i);
				for (int j = 0; j < singleAdjacency.length(); j++) {
					int valueAdjacency = singleAdjacency.getInt(j);
					singleAdjacenyList.add(valueAdjacency);
				}
			} catch (JSONException e) {
				log.error(e.getMessage());
			}
			adjacency.add(singleAdjacenyList);
		}

		// Set bonus
		for (int i = 0; i < bonusArray.length(); i++) {
			bonus.add(bonusArray.optInt(i));
		}

		// Set multiplier
		for (int i = 0; i < multiplierArray.length(); i++) {
			multiplier.add(multiplierArray.optInt(i));
		}

		if (bonus.size() > 0) {
			if (multiplier.size() > 0) {
				return Card.buildBonusMultiplierCard(areas, edges, adjacency,
						bonus, multiplier);
			} else {
				return Card.buildBonusCard(areas, edges, adjacency, bonus);
			}
		} else if (multiplier.size() > 0) {
			return Card
					.buildMultiplierCard(areas, edges, adjacency, multiplier);
		} else {
			return Card.buildStandardCard(areas, edges, adjacency);
		}
	}
}
