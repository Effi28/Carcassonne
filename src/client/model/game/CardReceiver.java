package client.model.game;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import shared.Configuration;
import shared.enums.CardAreaType;
import shared.model.BuildCard;
import shared.model.Card;

/**
 * This utility-class provides methods to recognize a card in order to get a cardID out of the logic card so that the
 * image can be displayed in the view.
 */
public final class CardReceiver {

	/**
	 * Error logger
	 */
	private static Logger log = LogManager.getLogger("ERROR");
	/**
	 * This list represents the 12-area-representation of every card
	 */
	private final static List<ArrayList<String>> ListOfCards = createCardList();

	/**
	 * This map contains the ListOfCards as key and the ID of every card as value
	 */
	private final static Map<String, ArrayList<String>> CardMap = createCardMap();

	/**
	 * Returns the ID of the card that is put in.
	 * <p>
	 * the method creates the 12-area-representation of the card that is put in and checks if the CardMap contains this
	 * arrayList as a key; if it does it returns the ID and if not it shifts the array so that all possible rotations of
	 * the card are recognized
	 * 
	 * @param card
	 *            card where the ID of the image should be computed.
	 * @return ID ID of the image of the given logic card
	 */
	public static String receiveCard(JSONObject card) {
		List<String> areasCard = null;
		try {
			areasCard = createAreasFromCard(card);
		} catch (JSONException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		if (CardMap.containsValue(areasCard)) {
			return checkBonusCards(areasCard, card) != null ? checkBonusCards(
					areasCard, card) : getKey(areasCard);
		}
		int counter = 0;
		while (CardMap.containsKey(areasCard) == false && counter < 12) {
			String tmp = areasCard.get(0);
			areasCard.remove(0);
			areasCard.add(tmp);
			counter++;
			if (CardMap.containsValue(areasCard)) {
				return checkBonusCards(areasCard, card) != null ? checkBonusCards(
						areasCard, card) : getKey(areasCard);
			}
		}
		return null;
	}

	/**
	 * Recognizes the rotation of the card that is received in the client.
	 * 
	 * @param card
	 *            the card of which the rotation should be recognized
	 * @param id
	 *            of the card which the rotation should be recognized
	 * @return the integer value of the rotation
	 */
	public static int getRotation(String id, JSONObject card) {
		ArrayList<String> areasCard = null;
		try {
			areasCard = createAreasFromCard(card);
		} catch (JSONException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}

		int rotation = 0;
		if (!CardMap.get(id).equals(areasCard)) {

			while (!(CardMap.get(id).equals(areasCard))) {

				for (int i = 0; i < 3; i++) {
					String temp = areasCard.remove(0);
					areasCard.add(temp);
				}
				rotation++;
			}
		}

		if (rotation != 0) {
			rotation = 4 - rotation;
		}

		return rotation;
	}

	/**
	 * Checks the cases when it's basically the same card but has a bonus-field.
	 * 
	 * @param areasCard
	 *            12-area-representation of the received card
	 * @param card
	 *            the received card as JSONObject
	 * @return id the ID
	 */
	private static String checkBonusCards(List<String> areasCard,
			JSONObject card) {
		Card logicCard = BuildCard.buildCard(card);
		if ("C".equals(getKey(areasCard)) || "AF".equals(getKey(areasCard))) {
			if (logicCard.getMultiplier().isEmpty()) {
				return "C";
			} else {
				return "AF";
			}
		}
		if ("K".equals(getKey(areasCard)) || "AE".equals(getKey(areasCard))) {
			if (logicCard.getMultiplier().isEmpty()) {
				return "K";
			} else {
				return "AE";
			}
		}
		if ("V".equals(getKey(areasCard)) || "AA".equals(getKey(areasCard))) {
			if (logicCard.getMultiplier().isEmpty()) {
				return "V";
			} else {
				return "AA";
			}
		}
		if ("W".equals(getKey(areasCard)) || "AC".equals(getKey(areasCard))) {
			if (logicCard.getMultiplier().isEmpty()) {
				return "W";
			} else {
				return "AC";
			}
		}
		if ("U".equals(getKey(areasCard)) || "AB".equals(getKey(areasCard))) {
			if (logicCard.getMultiplier().isEmpty()) {
				return "U";
			} else {
				return "AB";
			}
		}
		if ("I".equals(getKey(areasCard)) || "M".equals(getKey(areasCard))
				|| "N".equals(getKey(areasCard))) {

			if (logicCard.getBonus().size() != 0) {
				return "M";
			}
			return logicCard.getAreas().size() == 3 ? "I" : "N";
		}
		if ("F".equals(getKey(areasCard)) || "G".equals(getKey(areasCard))
				|| "H".equals(getKey(areasCard))) {
			if (!logicCard.getBonus().isEmpty()) {
				return "F";
			}
			if (Collections.frequency(logicCard.getAreas(), CardAreaType.TOWN) == 2) {
				return "H";
			} else
				return "G";
		}
		if ("O".equals(getKey(areasCard)) || "P".equals(getKey(areasCard))
				|| "AD".equals(getKey(areasCard))) {
			if (!logicCard.getMultiplier().isEmpty()) {
				return "AD";
			}
			return logicCard.getBonus().isEmpty() ? "P" : "O";
		}
		if ("R".equals(getKey(areasCard)) || "Q".equals(getKey(areasCard))) {
			return logicCard.getBonus().isEmpty() ? "R" : "Q";
		}
		if ("T".equals(getKey(areasCard)) || "S".equals(getKey(areasCard))) {
			return logicCard.getBonus().isEmpty() ? "T" : "S";
		} else {
			return null;
		}
	}

	/**
	 * Creates a Map that contains ListOfCards as key and their ID as value
	 * 
	 * @return cardMap
	 */
	private static Map<String, ArrayList<String>> createCardMap() {
		Map<String, ArrayList<String>> cardMap = new HashMap<>();
		boolean extension = false;
		boolean first = true;
		char idCount = 'A';
		for (int i = 0; i < ListOfCards.size(); i++) {
			if (!extension) {
				if (idCount == 'X') {
					extension = true;
				}
				cardMap.put(String.valueOf(idCount++), ListOfCards.get(i));
			} else {
				if (first) {
					idCount = 'A';
					first = false;
				}
				String tmp = "" + 'A' + idCount++;
				cardMap.put(tmp, ListOfCards.get(i));
			}
		}
		return cardMap;
	}

	/**
	 * Creates a List that contains the 12-area-representation of every card
	 * 
	 * @return allEdgeAreas
	 */
	private static List<ArrayList<String>> createCardList() {
		List<ArrayList<String>> cardList = new ArrayList<>();
		try {
			BufferedReader in = new BufferedReader(new FileReader(
					Configuration.GAMECARDSPATHINNS));

			String s = "";

			while (in.readLine() != null) {
				s = s + in.readLine();
			}

			JSONArray tiles = new JSONArray(s);

			for (int i = 0; i < tiles.length(); i++) {
				JSONObject singleCard = tiles.getJSONObject(i);
				cardList.add(createAreasFromCard(singleCard));
			}
			in.close();
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return cardList;
	}

	/**
	 * Creates the 12-area-representation of the JSONObject card
	 * 
	 * @param card
	 *            Card from which the 12 area representation should be created
	 * @return cardAreas 12 areas representation of the Card
	 * @throws JSONException
	 *             when an error occurs extracting the JSONObject containing the card
	 */
	private static ArrayList<String> createAreasFromCard(JSONObject card)
			throws JSONException {
		JSONArray areasArray = card.optJSONArray("areas");
		JSONArray edgesArray = card.optJSONArray("edges");

		List<String> areas = new ArrayList<>();
		List<ArrayList<Integer>> edges = new ArrayList<ArrayList<Integer>>();

		String meadow = CardAreaType.MEADOW.toString();
		String road = CardAreaType.ROAD.toString();
		String town = CardAreaType.TOWN.toString();

		for (int i = 0; i < areasArray.length(); i++) {
			String areaString = areasArray.getString(i);

			areas.add(areaString);
		}

		for (int i = 0; i < edgesArray.length(); i++) {
			ArrayList<Integer> singleEdgeList = new ArrayList<Integer>();
			JSONArray singleEdge = edgesArray.getJSONArray(i);
			for (int j = 0; j < singleEdge.length(); j++) {
				int valueEdge = singleEdge.getInt(j);
				singleEdgeList.add(valueEdge);
			}
			edges.add(singleEdgeList);
		}

		int meadowCounter = 0;
		int roadCounter = 0;
		int townCounter = 0;

		ArrayList<String> cardAreas = new ArrayList<String>();
		for (int j = 0; j < edges.size(); j++) {
			Collections.reverse(edges.get(j));
			if (edges.get(j).size() == 1) {
				int index = edges.get(j).get(0);
				if (areas.get(index).contains(meadow)) {
					if (areas.get(index).equals(meadow)) {
						areas.set(index, meadow + meadowCounter++);
					}
					for (int i = 0; i < 3; i++) {
						cardAreas.add(areas.get(index));
					}
				}
				if (areas.get(index).contains(town)) {
					if (areas.get(index).equals(town)) {
						areas.set(index, town + townCounter++);
					}
					for (int i = 0; i < 3; i++) {
						cardAreas.add(areas.get(index));
					}
				}
				if (areas.get(index).contains(road)) {
					if (areas.get(index).equals(road)) {
						areas.set(index, road + roadCounter++);
					}
					for (int i = 0; i < 3; i++) {
						cardAreas.add(areas.get(index));
					}
				}
			} else {
				for (int k = 0; k < edges.get(j).size(); k++) {
					int index = edges.get(j).get(k);
					if (areas.get(index).contains(meadow)) {
						if (areas.get(index).equals(meadow)) {
							areas.set(index, meadow + meadowCounter++);
						}
						cardAreas.add(areas.get(index));
					}
					if (areas.get(index).contains(town)) {
						if (areas.get(index).equals(town)) {
							areas.set(index, town + townCounter++);
						}
						cardAreas.add(areas.get(index));
					}
					if (areas.get(index).contains(road)) {
						if (areas.get(index).equals(road)) {
							areas.set(index, road + roadCounter++);
						}
						cardAreas.add(areas.get(index));
					}
				}
			}
		}
		return cardAreas;
	}

	/**
	 * Returns the ID of a card for a given 12 area representation of a card.
	 * 
	 * @param value
	 *            12 area representation of a card.
	 * @return ID of a card
	 */
	private static String getKey(List<String> value) {
		for (String key : CardMap.keySet()) {
			if (CardMap.get(key).equals(value)) {
				return key;
			}
		}
		return null;
	}
}
