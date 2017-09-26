package shared.model;

import server.model.game.LogicCardCreator;
import shared.Configuration;
import shared.enums.CapabilitiesType;
import shared.enums.CardAreaType;

import java.util.*;

/**
 * Class representing a cardDeck
 * <p>
 * This Class represents a whole CardDeck of tiles, which is used during the
 * game. The CardDeck is a stack which gives a tile back through popping it from
 * the stack. The startCard is hold in a final variable.
 * 
 * @see Card
 * @see LogicCardCreator
 * @see BuildCard
 */
public final class CardDeck {

	/**
	 * the actual cardDeck as a Stack of the type Card
	 */
	private Stack<Card> cardDeck;

	/**
	 * the constante object of the card class that represents the startCard,
	 * which is the same in every game
	 */
	private final Card STARTCARD;

	/**
	 * constructor of cardDeck: creates a new CardDeck out of the utility class
	 * LogicCardCreator and instanciates the startCard
	 * @param extensions 
	 */
	public CardDeck(Set<CapabilitiesType> extensions) {
		if(extensions.contains(CapabilitiesType.INNS)){
			cardDeck = LogicCardCreator.buildDeck(Configuration.GAMECARDSPATHINNS);
		}
		else{
			cardDeck = LogicCardCreator.buildDeck(Configuration.BISHOPTEST);

		}
		
		STARTCARD = CardDeck.createStartCard();
	}

	/**
	 * this method creates the startCard separated from the cardDeck. the card
	 * is created by manually adding to the arrays
	 * 
	 * @return STARTCARD
	 */
	public static Card createStartCard() {
		List<CardAreaType> areas = new ArrayList<>();

		// "areas": ["Town", "Meadow", "Road", "Meadow"],
		areas.add(CardAreaType.TOWN);
		areas.add(CardAreaType.MEADOW);
		areas.add(CardAreaType.ROAD);
		areas.add(CardAreaType.MEADOW);

		List<ArrayList<Integer>> edges = new ArrayList<>();

		// "edges": [[1,2,3],[1],[3,2,1],[0]]

		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(1);
		singleEdge1.add(2);
		singleEdge1.add(3);
		singleEdge2.add(1);
		singleEdge3.add(3);
		singleEdge3.add(2);
		singleEdge3.add(1);
		singleEdge4.add(0);

		edges.add(singleEdge1);
		edges.add(singleEdge2);
		edges.add(singleEdge3);
		edges.add(singleEdge4);

		// "adjacency": [[3], [2], [1,3], [2,0]]

		List<ArrayList<Integer>> adjacency = new ArrayList<>();

		ArrayList<Integer> singleAdjacency1 = new ArrayList<>();
		ArrayList<Integer> singleAdjacency2 = new ArrayList<>();
		ArrayList<Integer> singleAdjacency3 = new ArrayList<>();
		ArrayList<Integer> singleAdjacency4 = new ArrayList<>();

		singleAdjacency1.add(3);
		singleAdjacency2.add(2);
		singleAdjacency3.add(1);
		singleAdjacency3.add(3);
		singleAdjacency4.add(2);
		singleAdjacency4.add(0);

		adjacency.add(singleAdjacency1);
		adjacency.add(singleAdjacency2);
		adjacency.add(singleAdjacency3);
		adjacency.add(singleAdjacency4);

		return Card.buildStandardCard(areas, edges, adjacency);

	}

	/**
	 * gets the card on top of the deck
	 * 
	 * @param remove
	 *            boolean which indicates if a card should be removed from the
	 *            cardDeck or not.(true if the card should be removed, false if
	 *            not)
	 * @return card the card that lays on top of the deck
	 */
	public Card getColonelCard(boolean remove) {
		if (remove) {
			return cardDeck.pop();
		}
		return cardDeck.peek();
	}

	/**
	 * Shuffles the cardDeck four times.
	 */
	public void shuffleCardDeck() {
		for(int i = 0; i< 4; i++){
			Collections.shuffle(cardDeck);			
		}
	}

	/*
	 * Getter & Setter
	 */

	public Stack<Card> getCardDeck() {
		return cardDeck;
	}

	public void setCardDeck(Stack<Card> cardDeck) {
		this.cardDeck = cardDeck;
	}

	public Card getStartCard() {
		return STARTCARD;
	}

	public int getRemainingSize() {
		return cardDeck.size();
	}
}
