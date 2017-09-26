package ai;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import shared.model.AbstractSingleArea;
import shared.model.Card;
import shared.model.CardDeck;
import shared.model.Position;
import shared.model.SingleAreaMeadow;
import shared.model.SingleAreaRoad;
import shared.model.SingleAreaTown;

public class AILogicTest {

	@Test
	public void checkForLegalPlacement_Test1() {

		String address = "localhost";
		String name = "ai";
		// Initialization
		AI ai = new AI(name, address, 4455, null, 0, 0, null);
		Card startCard = CardDeck.createStartCard();
		Card newCard = CardDeck.createStartCard();
		ai.getAiLogic().getAIGame().getGameField().addCard(new Position(0, 0), startCard);

		// test
		Set<PossiblePlacement> possiblePlacement = ai.getAiLogic()
				.checkForLegalPlacements(newCard);
		List<PossiblePlacement> expectedPossiblePlacement = new ArrayList<>();

		PossiblePlacement pp1 = new PossiblePlacement(2, new Position(1, 0));
		PossiblePlacement pp2 = new PossiblePlacement(0, new Position(0, 1));
		PossiblePlacement pp3 = new PossiblePlacement(2, new Position(0, 1));
		PossiblePlacement pp4 = new PossiblePlacement(0, new Position(0, -1));
		PossiblePlacement pp5 = new PossiblePlacement(2, new Position(0, -1));
		PossiblePlacement pp6 = new PossiblePlacement(2, new Position(-1, 0));

		expectedPossiblePlacement.add(pp1);
		expectedPossiblePlacement.add(pp2);
		expectedPossiblePlacement.add(pp3);
		expectedPossiblePlacement.add(pp4);
		expectedPossiblePlacement.add(pp5);
		expectedPossiblePlacement.add(pp6);

		assertEquals(expectedPossiblePlacement, possiblePlacement);
	}

	@Test
	public void checkForLegalPlacement_Test2() {

		// Initialization
		AI ai = new AI(null, null, 0, null, 0, 0, null);
		Card startCard = CardDeck.createStartCard();
		Card newCard = CardDeck.createStartCard();
		ai.getAiLogic().getAIGame().getGameField().addCard(new Position(0, 0), startCard);
		ai.getAiLogic().getAIGame().getGameField().addCard(new Position(0, 1), startCard);

		Set<PossiblePlacement> possiblePlacement = ai.getAiLogic()
				.checkForLegalPlacements(newCard);

		assertEquals(8, possiblePlacement.size());
	}

	@Test
	public void checkForLegalPlacement_Test3() {

		// Initialization
		AI ai = new AI(null, null, 0, null, 0, 0, null);
		Card startCard = CardDeck.createStartCard();
		Card newCard = CardDeck.createStartCard();
		ai.getAiLogic().getAIGame().getGameField().addCard(new Position(0, 0), startCard);
		ai.getAiLogic().getAIGame().getGameField().addCard(new Position(0, 1), startCard);
		ai.getAiLogic().getAIGame().getGameField().addCard(new Position(0, 2), startCard);

		Set<PossiblePlacement> possiblePlacement = ai.getAiLogic()
				.checkForLegalPlacements(newCard);

		assertEquals(10, possiblePlacement.size());
	}

	@Test
	public void checkForLegalPlacement_Test4() {

		// Initialization
		AI ai = new AI(null, null, 0, null, 0, 0, null);
		Card startCard = CardDeck.createStartCard();
		Card doubleRotatedCard = CardDeck.createStartCard();

		Card newCard = CardDeck.createStartCard();
		doubleRotatedCard.rotate(2);

		ai.getAiLogic().getAIGame().getGameField().addCard(new Position(0, 0), startCard);
		ai.getAiLogic().getAIGame().getGameField().addCard(new Position(1, 0), doubleRotatedCard);
		ai.getAiLogic().getAIGame().getGameField().addCard(new Position(0, 1), startCard);

		Set<PossiblePlacement> possiblePlacement = ai.getAiLogic()
				.checkForLegalPlacements(newCard);

		assertEquals(11, possiblePlacement.size());
	}

	@Test
	public void checkForMeepleePlacement_Test1(){
		
		// Initialization
		AI ai = new AI(null, null, 0, null, 0, 0, null);
		Card startCard = createStartCard();
		Card doubleRotatedCard = createStartCard();

		Card newCard = createStartCard();
		doubleRotatedCard.rotate(2);

		ai.getAiLogic().getAIGame().getGameField().addCard(new Position(0, 0), startCard);

		Set<PossiblePlacement> placements = ai.getAiLogic()
				.checkForLegalPlacements(newCard);
		Set<PossiblePlacement> possibleMeeple = ai.getAiLogic()
				.checkForMeeplePlacement(startCard, placements);

		assertEquals(24, possibleMeeple.size());
	}
	
	public Card createStartCard(){
		Card firstCard = CardDeck.createStartCard();
		
		List<AbstractSingleArea> singleAreas = new ArrayList<>();
		
		singleAreas.add(new SingleAreaMeadow(null, null, new Position(0, 0)));
		singleAreas.add(new SingleAreaRoad(false, new Position(0, 0), 1));
		singleAreas.add(new SingleAreaMeadow(null, null, new Position(0, 0)));
		singleAreas.add(new SingleAreaTown(1, false, new Position(0, 0), 1));
		((SingleAreaMeadow) singleAreas.get(2)).getTowns().add((SingleAreaTown) singleAreas.get(3));

		firstCard.getSingleAreaMap().put(1, singleAreas.get(0));
		firstCard.getSingleAreaMap().put(2, singleAreas.get(1));
		firstCard.getSingleAreaMap().put(3, singleAreas.get(2));
		firstCard.getSingleAreaMap().put(0, singleAreas.get(3));
		
		return firstCard;
	}
}
