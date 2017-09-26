package shared;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import shared.enums.CardAreaType;
import shared.model.Card;
import shared.model.CardDeck;
import shared.model.Game;
import shared.model.GameField;
import shared.model.Position;

public class GameTest {

	Game game = new Game(null, 0, null);

	GameField g = new GameField();
	CardDeck cardDeck = new CardDeck(null);
	Card cB = Card.buildStandardCard(null, null, null);

	@Before
	public void setup() {

		GameField g = new GameField();

		{

			// "areas": ["Meadow", "Cloister"],

			List<CardAreaType> areas = new ArrayList<>();

			areas.add(CardAreaType.MEADOW);
			areas.add(CardAreaType.CLOISTER);

			// "edges": [[0], [0], [0], [0]],

			List<ArrayList<Integer>> edges = new ArrayList<>();

			ArrayList<Integer> edge1 = new ArrayList<>();
			ArrayList<Integer> edge2 = new ArrayList<>();
			ArrayList<Integer> edge3 = new ArrayList<>();
			ArrayList<Integer> edge4 = new ArrayList<>();

			edge1.add(0);
			edge2.add(0);
			edge3.add(0);
			edge4.add(0);

			edges.add(edge1);
			edges.add(edge2);
			edges.add(edge3);
			edges.add(edge4);

			// "adjacency": [[1], [0]],

			List<ArrayList<Integer>> adjacency = new ArrayList<>();

			ArrayList<Integer> adja1 = new ArrayList<>();
			ArrayList<Integer> adja2 = new ArrayList<>();

			adja1.add(1);
			adja2.add(0);

			Card cB = Card.buildStandardCard(areas, edges, adjacency);

			this.g = g;
			this.cB = cB;

		}
	}

	@Test
	public void metchingEdgesRight_startCard_CardB() {

		setup();

		g.addCard(new Position(0, 0), cardDeck.getStartCard());

		game.setCurrentCard(cB);

		assertEquals(true, game.matchingRightEdge(cB, new Position(-1, 0)));

	}
}
