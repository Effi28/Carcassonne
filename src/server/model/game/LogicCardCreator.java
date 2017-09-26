package server.model.game;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import shared.model.BuildCard;
import shared.model.Card;

/**
 * Utility-class that creates gameCards out of the informations in a JSON-File.
 * <p>
 * Provides the method buildDeck that creates the logical CardDeck with the type
 * Stack<Card>
 * @see shared.model.CardDeck
 * @see Card
 */
public final class LogicCardCreator {

	/**
	 * Error logger
	 */
	private static Logger log = LogManager.getLogger("ERROR");

	/**
	 * Only constructor for this class.
	 * Should never be called because this class is a utility class, throws an exception if it is called. 
	 */
	private LogicCardCreator() {
		log.error("LogicCardCreator should not be instantiated!");
		throw new AssertionError("LogicCardCreator should not be instantiated!");
	}

	/**
	 * Creates a logical cardDeck
	 * <p>
	 * Gets a name of a JSON-File and creates a cardDeck of tiles by
	 * reading the different cards of the JSON-File and calling the
	 * buildCard-method of the utility-class BuildCard
	 * 
	 * @param fileName
	 *            the filename of the JSON-File in which the cards are saved
	 * @return the finished build Deck
	 */
	public static Stack<Card> buildDeck(String fileName) {
		Stack<Card> cardDeck = new Stack<Card>();
		BufferedReader in = null;
		try {

			in = new BufferedReader(new FileReader(fileName));

			String s = "";

			while (in.readLine() != null) {
				s = s + in.readLine();
			}

			JSONArray tiles = new JSONArray(s);

			for (int i = 0; i < tiles.length(); i++) {
				if (i == 9 || i == 10) {
				}
				JSONObject singleCard = tiles.getJSONObject(i);
				int cardOccurrence = singleCard.optInt("occurrence");
				for (int j = 0; j < cardOccurrence; j++) {
					cardDeck.add(BuildCard.buildCard(singleCard));
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				log.error(e.getMessage());
			}
		}
		return cardDeck;
	}
	
	public static Map<String, Card> buildIDMap(String fileName) {
		Map<String, Card> idMap = new HashMap<>();
		List<Card> cards = new ArrayList<Card>();
		boolean extension = false;
		boolean first = true;
		char idCount = 'A';
		BufferedReader in = null;
		try {

			in = new BufferedReader(new FileReader(fileName));

			String s = "";

			while (in.readLine() != null) {
				s = s + in.readLine();
			}

			JSONArray tiles = new JSONArray(s);

			for (int i = 0; i < tiles.length(); i++) {
				if (i == 9 || i == 10) {
				}
				JSONObject singleCard = tiles.getJSONObject(i);
				cards.add(BuildCard.buildCard(singleCard));
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				log.error(e.getMessage());
			}
		}
		
		for (int i = 0; i < cards.size(); i++) {
			if (!extension) {
				if (idCount == 'X') {
					extension = true;
				}
				idMap.put(String.valueOf(idCount++), cards.get(i));
			} else {
				if (first) {
					idCount = 'A';
					first = false;
				}
				String tmp = "" + 'A' + idCount++;
				idMap.put(tmp, cards.get(i));
			}
		}
		return idMap;
	}
}
