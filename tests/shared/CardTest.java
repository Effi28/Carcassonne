package shared;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.junit.Test;

import server.model.game.LogicCardCreator;
import shared.Configuration;
import shared.enums.CardAreaType;
import shared.model.Card;

public class CardTest {

	@Test
	public void getAreas_getFirstArea_OnFirstCard() {
		Stack<Card> cardTestDeck = LogicCardCreator
				.buildDeck(Configuration.GAMECARDSPATH);
		Card testCard = cardTestDeck.pop();
		assertEquals(CardAreaType.MEADOW, testCard.getAreas().get(0));
	}

	@Test
	public void getAreas_getSecondArea_OnFifthCard() {
		Stack<Card> cardTestDeck = LogicCardCreator
				.buildDeck(Configuration.GAMECARDSPATH);
		Card testCard = cardTestDeck.get(5);
		assertEquals(CardAreaType.CLOISTER, testCard.getAreas().get(1));
	}

	@Test
	public void getEdges_getFirstEdge_OnFirstCard() {
		Stack<Card> cardTestDeck = LogicCardCreator
				.buildDeck(Configuration.GAMECARDSPATH);
		Card testCard = cardTestDeck.pop();
		List<Integer> testList = new ArrayList<>();
		testList.add(6);
		testList.add(5);
		testList.add(4);
		assertEquals(testList, testCard.getEdges().get(2));
	}

	@Test
	public void rotate_startCard_oneRotation() {

		// Build rotated edges with the factor 1

		// "edges": [[4,3,0], [6,5,4], [2,7,6], [0,1,2]],

		List<ArrayList<Integer>> expectedEdges = new ArrayList<>();

		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge1.add(1);
		singleEdge1.add(2);

		singleEdge2.add(4);
		singleEdge2.add(3);
		singleEdge2.add(0);

		singleEdge3.add(6);
		singleEdge3.add(5);
		singleEdge3.add(4);

		singleEdge4.add(2);
		singleEdge4.add(7);
		singleEdge4.add(6);

		expectedEdges.add(singleEdge2);
		expectedEdges.add(singleEdge3);
		expectedEdges.add(singleEdge4);
		expectedEdges.add(singleEdge1);

		Stack<Card> cardTestDeck = LogicCardCreator
				.buildDeck(Configuration.GAMECARDSPATH);
		Card cardBeforeRotation = cardTestDeck.pop();
		cardBeforeRotation.rotate(1);
		
		List<ArrayList<Integer>> realEdges = cardBeforeRotation.getEdges();

		// startCard should have edges not rotate [[0,1,2], [4,3,0], [6,5,4],
		// [2,7,6]],

		// testCard should have edges with rotation1 [[4,3,0], [6,5,4], [2,7,6],
		// [0,1,2]],

		assertEquals(expectedEdges, realEdges);

	}
}
