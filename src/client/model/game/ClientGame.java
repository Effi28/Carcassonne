package client.model.game;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ai.PossiblePlacement;
import client.model.clientCommunication.ClientControl;
import server.model.game.ServerGame;
import shared.enums.CapabilitiesType;
import shared.enums.CardAreaType;
import shared.enums.GameStatus;
import shared.model.Card;
import shared.model.Game;
import shared.model.Player;
import shared.model.Position;
import shared.model.Spectator;

/**
 * This class delegates the Model-Communication and manages the game for the client.
 * <p>
 * For every game, that's created a new ClientGame will be created. Later on this class might implement some logic for
 * the client e.g. where can I put my card.
 * 
 * @Version 01.12.2013
 * @see Game
 * @see ServerGame
 * @see ClientControl
 */
public final class ClientGame extends Game {

	/**
	 * Name of the client that hosts this game
	 */
	private final String host;

	/**
	 * Only contructor for ClientGame
	 * 
	 * @param gameName
	 *            name of the game this instance should control
	 * @param gameID
	 *            ID that identifies the game
	 * @param host
	 *            nick of the player who hosts this game
	 * @param playerList
	 *            list of players attending this game
	 * @param specList
	 *            list of spectators watching this game
	 * @param extensions extensions of a game
	 */
	public ClientGame(String gameName, int gameID, String host,
			Map<String, Player> playerList, Map<String, Spectator> specList,
			Set<CapabilitiesType> extensions) {
		super(gameName, gameID, extensions);
		super.setPlayerList(playerList);
		super.setSpectatorList(specList);
		this.host = host;
	}

	/**
	 * Is called when a game update message is received.
	 * 
	 * @param state
	 *            state of the game
	 * @param playerList
	 *            list of players
	 * @param specList
	 *            list of spectators
	 */
	public void gameUpdate(GameStatus state, Map<String, Player> playerList,
			Map<String, Spectator> specList) {
		super.setState(state);
		super.setPlayerList(playerList);
		super.setSpectatorList(specList);
	}

	/**
	 * method transforms the given areaPlacement from the view into an index representing a area matching the example
	 * specification for client-server-communication
	 * 
	 * @param areaPlacement
	 *            placement of the meeple from the view
	 * @param rotation
	 *            rotation of the current Card
	 * @return index of the current area of the card
	 */
	public int meeplePlaced(int areaPlacement, int rotation) {
		int indexOfArea = -1;
		Card card = getCurrentCard();
		card.rotate(rotation + 4 - (card.getRotation()));
		switch (areaPlacement) {
		case -1:
			indexOfArea = -1;
			break;
		case 0:
		case 1:
		case 2:
			if (card.getEdges().get(0).size() == 3) {
				indexOfArea = card.getEdges().get(0).get(areaPlacement);
			} else {
				indexOfArea = card.getEdges().get(0).get(0);
			}
			break;
		case 3:
		case 4:
		case 5:
			if (card.getEdges().get(1).size() == 3) {
				indexOfArea = card.getEdges().get(1).get(areaPlacement - 3);
			} else {
				indexOfArea = card.getEdges().get(1).get(0);
			}
			break;
		case 6:
		case 7:
		case 8:
			if (card.getEdges().get(2).size() == 3) {
				indexOfArea = card.getEdges().get(2).get(areaPlacement - 6);
			} else {
				indexOfArea = card.getEdges().get(2).get(0);
			}
			break;
		case 9:
		case 10:
		case 11:
			if (card.getEdges().get(3).size() == 3) {
				indexOfArea = card.getEdges().get(3).get(areaPlacement - 9);
			} else {
				indexOfArea = card.getEdges().get(3).get(0);
			}
			break;
		case 12:
			if (card.getAreas().contains(CardAreaType.CLOISTER)) {
				indexOfArea = card.getAreas().indexOf(CardAreaType.CLOISTER);
			} else {
				int roadCounter = 0;
				int townCounter = 0;
				int meadowCounter = 0;
				int townAdjacentMeadowCounter = 0;
				int townAdjacentMeadow = -1;
				for (int i = 0; i < card.getEdges().size(); i++) {
					for (int j = 0; j < card.getEdges().get(i).size(); j++) {
						int temp = card.getEdges().get(i).get(j);
						CardAreaType cardAreaType = card.getAreas().get(temp);
						if (cardAreaType == CardAreaType.ROAD) {
							roadCounter++;
						} else if (cardAreaType == CardAreaType.TOWN) {
							townCounter++;
						} else if (cardAreaType == CardAreaType.MEADOW) {
							meadowCounter++;
							ArrayList<Integer> meadowAdjacencies = card
									.getAdjacency().get(temp);
							for (Integer index : meadowAdjacencies) {
								if (card.getAreas().get(index) == CardAreaType.TOWN) {
									townAdjacentMeadow = temp;
									townAdjacentMeadowCounter++;
								}
							}
						}
					}
				}
				if (roadCounter == 2 && townCounter > 0) {
					indexOfArea = townAdjacentMeadow;
				} else if (roadCounter == 3 && townCounter == 1) {
					indexOfArea = townAdjacentMeadow;
				} else if (roadCounter == 2) {
					indexOfArea = card.getAreas().indexOf(CardAreaType.ROAD);
				} else if (roadCounter == 3 || roadCounter == 4) {
					indexOfArea = -1;
				} else if (townCounter == 2 && meadowCounter == 2
						&& townAdjacentMeadowCounter == 2) {
					indexOfArea = card.getAreas().indexOf(CardAreaType.TOWN);
				} else if (townCounter == 3 || townCounter == 4) {
					indexOfArea = card.getAreas().indexOf(CardAreaType.TOWN);
				} else if (townCounter < 3) {
					indexOfArea = card.getAreas().indexOf(CardAreaType.MEADOW);
				}
			}
			break;

		default:
			indexOfArea = -1;
			break;
		}
		card.rotate(4 - rotation);
		return indexOfArea;
	}

	/**
	 * Sets a Card on a certain Position on the GameField
	 * 
	 * @param pos
	 *            the position on the gameField on which the card should be set
	 * @param rotation
	 *            the rotation of the card that should be set on the field
	 * @param card
	 *            the card which should be set on the field
	 * 
	 */
	public void setCardOnPosition(Position pos, int rotation, Card card) {
		card.setRotation(rotation);
		super.getGameField().addCard(pos, card);
	}

	/**
	 * Computes the position of a meeple in the GUI from the placement sent by the server.
	 * 
	 * @param card
	 *            current Card
	 * @param placement
	 *            of the from the meeple
	 * 
	 * @return gui placement
	 */
	public int getMeepleLocation(Card card, int placement) {

		int area = -1;
		int edgeIndex = -1;
		int areaIndex = -1;

		int edgeCounter = 0;
		outerloop: for (ArrayList<Integer> edge : card.getEdges()) {
			int areaCounter = 0;
			for (Integer integer : edge) {
				if (integer.intValue() == placement) {
					edgeIndex = edgeCounter;
					areaIndex = areaCounter;
					break outerloop;
				}
				areaCounter++;
			}
			edgeCounter++;
		}
		if (edgeIndex > -1) {
			area = areaIndex + edgeIndex * 3;
		} else if (placement != -1) {
			area = 12;
		}
		return area;
	}
	
	/**
	 * method should check for possibilities of card placement by iterating
	 * through the gameField and check surrounding positions and compare them to
	 * the 4 edges of the current card and if its possible to merge one of them.
	 * 
	 * @param card
	 *            the current Card.
	 * @return a List with all possible Placements for the current Card.
	 */
	public Set<PossiblePlacement> checkForLegalPlacements(Card card) {
		Set<PossiblePlacement> availablePositions = new HashSet<>();
		Set<Position> emptyPositions = super.getGameField().getPositionsFromSetCards();
		for (Position p : emptyPositions) {
			for (int i = 0; i < 4; i++) {
				if (matchingEdges(card, p)) {
					availablePositions.add(new PossiblePlacement(i, p));
				}
				ArrayList<Integer> tmp = card.getEdges().get(0);
				card.getEdges().remove(0);
				card.getEdges().add(tmp);
			}
		}
		return availablePositions;
	}
	

	@Override
	public Map<String, Player> getPlayerList() {
		return super.getPlayerList();
	}

	@Override
	public Set<CapabilitiesType> getExtensions() {
		return super.getExtensions();
	}
	
	/**
	 * Returns the host of the game.
	 * 
	 * @return Host of the game
	 */
	public String getHost() {
		return host;
	}
}
