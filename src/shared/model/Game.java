package shared.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shared.enums.CapabilitiesType;
import shared.enums.CardAreaType;
import shared.enums.GameStatus;
import shared.enums.SpecialMeepleType;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Superclass of ServerGame and ClientGameC
 * <p>
 * Provides all methods and fields every instance of the child classes has to
 * implement.
 * 
 * 
 * @version 01.12.2013
 * @see GameField
 * @see CardDeck
 */
public class Game {

	private Logger log = LogManager.getLogger("WARN");
	/**
	 * ID of the game which is controlled by this instance
	 */
	final int gameID;
	/**
	 * Reference to the only gamefield of this game
	 */
	private GameField gameField;

	/**
	 * Map of the watching users, the key is the nick of a user
	 */
	private volatile Map<String, Spectator> spectatorList;

	/**
	 * Map of playing users, the key is the nick of a user
	 */
	private volatile Map<String, Player> playerList;

	/**
	 * The current state of the game
	 */
	private GameStatus state = GameStatus.NOTSTARTED;

	/**
	 * The instance of the current CardDeck
	 */
	private CardDeck cardDeck;

	/**
	 * The instance of the current Card
	 */
	private Card currentCard;

	/**
	 * The instance of the current Position
	 */
	private Position currentPosition;

	/**
	 * Name of the game
	 */
	final String gameName;

	/**
	 * Extensions of the game
	 */
	private Set<CapabilitiesType> extensions;
	
	private List<AbstractSingleArea> singleAreas;

	/**
	 * the constructor that creates a new instance of the class Game
	 * 
	 * @param gameName
	 *            the name of this game
	 * @param gameID
	 *            the id of this game
	 * @param extensions
	 */
	public Game(String gameName, int gameID, Set<CapabilitiesType> extensions) {
		this.gameName = gameName;
		this.gameID = gameID;
		this.gameField = new GameField();
		this.singleAreas = new ArrayList<>();
		this.spectatorList = new ConcurrentHashMap<>();
		this.playerList = new ConcurrentHashMap<>();
		this.extensions = extensions;
		this.cardDeck = new CardDeck(extensions);
		cardDeck.shuffleCardDeck();
	}

	/**
	 * This method checks if you are allowed to put the card on this position by
	 * checking the different edges if they fit or not
	 * 
	 * @param currentCard
	 *            current Card
	 * @param currentPosition
	 *            current Position
	 * @return true if the edges fit and false if not
	 */
	public boolean matchingEdges(Card currentCard,
			Position currentPosition) {
		if (getGameField().checkNeighbours(currentPosition)
				&& !getGameField().isPositionTaken(currentPosition)
				&& matchingTopEdge(currentCard, currentPosition)
				&& matchingLeftEdge(currentCard, currentPosition)
				&& matchingBottomEdge(currentCard, currentPosition)
				&& matchingRightEdge(currentCard, currentPosition)) {
			return true;
		}
		return false;

	}

	/**
	 * Checks if edges of rightCard and currentCard fit together
	 * 
	 * @param currentCard
	 *            Card to set
	 * @param currentPosition
	 *            position where the current card should be set
	 * @return boolean if edges fit or not
	 */
	public boolean matchingRightEdge(Card currentCard, Position currentPosition) {
		if (getGameField().getCardEast(currentPosition) != null) {
			Card eastCard = getGameField().getCardEast(currentPosition);
			Integer westIndex = eastCard.getEdges().get(1).size() == 3 ? eastCard
					.getEdges().get(1).get(1)
					: eastCard.getEdges().get(1).get(0);
			CardAreaType west = eastCard.getAreas().get(westIndex);
			Integer currentIndex = currentCard.getEdges().get(3).size() == 3 ? currentCard
					.getEdges().get(3).get(1)
					: currentCard.getEdges().get(3).get(0);
			CardAreaType current = currentCard.getAreas().get(currentIndex);
			if (west != current) {
				return false;
			}
		}
		return true;
	}

	/**
	 * method checks if edges of currentCard and top card fit together
	 * 
	 * @param currentCard
	 * @param currentPosition
	 * @return boolean if edges fit or not
	 */
	private boolean matchingTopEdge(Card currentCard, Position currentPosition) {
		if (getGameField().getCardNorth(currentPosition) != null) {
			Card northCard = getGameField().getCardNorth(currentPosition);
			Integer northIndex = northCard.getEdges().get(2).size() == 3 ? northCard
					.getEdges().get(2).get(1)
					: northCard.getEdges().get(2).get(0);
			CardAreaType north = northCard.getAreas().get(northIndex);
			Integer currentIndex = currentCard.getEdges().get(0).size() == 3 ? currentCard
					.getEdges().get(0).get(1)
					: currentCard.getEdges().get(0).get(0);
			CardAreaType current = currentCard.getAreas().get(currentIndex);
			if (north != current) {
				return false;
			}
		}
		return true;
	}

	/**
	 * method checks if edges of leftCard and currentCard fit together
	 * 
	 * @param currentCard
	 * @param currentPosition
	 * @return boolean if edges fit or not
	 */
	private boolean matchingLeftEdge(Card currentCard, Position currentPosition) {

		if (getGameField().getCardWest(currentPosition) != null) {
			Card westCard = getGameField().getCardWest(currentPosition);
			Integer eastIndex = westCard.getEdges().get(3).size() == 3 ? westCard
					.getEdges().get(3).get(1)
					: westCard.getEdges().get(3).get(0);
			CardAreaType east = westCard.getAreas().get(eastIndex);
			Integer currentIndex = currentCard.getEdges().get(1).size() == 3 ? currentCard
					.getEdges().get(1).get(1)
					: currentCard.getEdges().get(1).get(0);
			CardAreaType current = currentCard.getAreas().get(currentIndex);
			if (east != current) {
				return false;
			}
		}
		return true;
	}

	/**
	 * method checks if edges of bottomCard and currentCard fit together
	 * 
	 * @param currentCard
	 * @param currentPosition
	 * @return boolean if edges fit or not
	 */
	private boolean matchingBottomEdge(Card currentCard,
			Position currentPosition) {
		if (getGameField().getCardSouth(currentPosition) != null) {
			Card southCard = getGameField().getCardSouth(currentPosition);
			Integer southIndex = southCard.getEdges().get(0).size() == 3 ? southCard
					.getEdges().get(0).get(1)
					: southCard.getEdges().get(0).get(0);
			CardAreaType south = southCard.getAreas().get(southIndex);
			Integer currentIndex = currentCard.getEdges().get(2).size() == 3 ? currentCard
					.getEdges().get(2).get(1)
					: currentCard.getEdges().get(2).get(0);
			CardAreaType current = currentCard.getAreas().get(currentIndex);
			if (south != current) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Merges edges into one singleArea.
	 * <p/>
	 * By getting the two cards which should be merged by looking on their positions on the gameField these cards are
	 * united to one singleArea. The older SingleArea will always be merged with the new SingleArea so the older
	 * SingleArea will exist.
	 * 
	 * @param newCardsPosition
	 *            Position of the new Card that was just set
	 * @param oldCardsPosition
	 *            Position of the Card that was already set when the new Card was set
	 * @param edge
	 *            index of the edge that should be merged
	 */
	public void mergeEdges(Position newCardsPosition,
			Position oldCardsPosition, int edge) {

		Card placed = getGameField().getCard(oldCardsPosition); // old card
		Card toBePlaced = getGameField().getCard(newCardsPosition); // new Card

		List<Integer> edgeToBeMerged = null;
		List<Integer> edgeToBeMergedWith = null;

		// Check which edges should be merged
		int newEdge = (edge + 2) > 3 ? (edge + 2) % 4 : edge + 2;

		edgeToBeMerged = toBePlaced.getEdges().get(edge);
		edgeToBeMergedWith = placed.getEdges().get(newEdge);

		AbstractSingleArea areaToAdapt = null;
		AbstractSingleArea areaToMerge = null;

		// repeat for all areas of edge
		for (int j = 0; j < edgeToBeMerged.size(); j++) {

			// index of single area on other card
			int k = j;
			if (edgeToBeMerged.size() != 1) {
				k = 2 - j;
			}

			// pick SingleArea that was created first
			int areaIndexNew = 0;
			int areaIndexOld = 0;
			for (int i = 0; i < singleAreas.size(); i++) {
				if (singleAreas.get(i) == toBePlaced.getSingleAreaMap().get(
						edgeToBeMerged.get(j))) {
					areaIndexNew = i;
				} else if (singleAreas.get(i) == placed.getSingleAreaMap().get(
						edgeToBeMergedWith.get(k))) {
					areaIndexOld = i;
				}
			}

			// new card has the newest singleAreas
			if (areaIndexNew >= areaIndexOld) {
				switch (j) {
				case 0:
					areaToAdapt = placed.getSingleAreaMap().get(
							edgeToBeMergedWith.get(j));
					areaToMerge = toBePlaced.getSingleAreaMap().get(
							edgeToBeMerged.get(k));

					if (areaToAdapt.getCardAreaType().equals(CardAreaType.TOWN)) {
						// merge town area
						mergeTownArea((SingleAreaTown) areaToAdapt,
								(SingleAreaTown) areaToMerge);

					} else {
						// merge meadow area
						mergeMeadowArea(0, (SingleAreaMeadow) areaToAdapt,
								(SingleAreaMeadow) areaToMerge);
					}

					break;

				case 1:
					areaToAdapt = (SingleAreaRoad) placed.getSingleAreaMap()
							.get(edgeToBeMergedWith.get(j));
					areaToMerge = (SingleAreaRoad) toBePlaced
							.getSingleAreaMap().get(edgeToBeMerged.get(k));

					// merge road area
					mergeRoadArea((SingleAreaRoad) areaToAdapt,
							(SingleAreaRoad) areaToMerge);

					break;

				case 2:
					areaToAdapt = (SingleAreaMeadow) placed.getSingleAreaMap()
							.get(edgeToBeMergedWith.get(j));
					areaToMerge = (SingleAreaMeadow) toBePlaced
							.getSingleAreaMap().get(edgeToBeMerged.get(k));

					// merge second meadow area
					mergeMeadowArea(2, (SingleAreaMeadow) areaToAdapt,
							(SingleAreaMeadow) areaToMerge);

					break;

				default:
					log.warn("No case matched");
					throw new IllegalArgumentException("no case matched");
				}

			} // new card has older SingleAreas than the old card
			else {
				switch (j) {
				case 0:
					areaToAdapt = toBePlaced.getSingleAreaMap().get(
							edgeToBeMerged.get(k));
					areaToMerge = placed.getSingleAreaMap().get(
							edgeToBeMergedWith.get(j));

					if (areaToAdapt.getCardAreaType().equals(CardAreaType.TOWN)) {
						// merge town area
						mergeTownArea((SingleAreaTown) areaToAdapt,
								(SingleAreaTown) areaToMerge);

					} else {
						// merge meadow area
						mergeMeadowArea(0, (SingleAreaMeadow) areaToAdapt,
								(SingleAreaMeadow) areaToMerge);
					}

					break;

				case 1:
					areaToAdapt = (SingleAreaRoad) toBePlaced
							.getSingleAreaMap().get(edgeToBeMerged.get(k));
					areaToMerge = (SingleAreaRoad) placed.getSingleAreaMap()
							.get(edgeToBeMergedWith.get(j));

					// merge road area
					mergeRoadArea((SingleAreaRoad) areaToAdapt,
							(SingleAreaRoad) areaToMerge);

					break;

				case 2:
					areaToAdapt = (SingleAreaMeadow) toBePlaced
							.getSingleAreaMap().get(edgeToBeMerged.get(k));
					areaToMerge = (SingleAreaMeadow) placed.getSingleAreaMap()
							.get(edgeToBeMergedWith.get(j));

					// merge second meadow area
					mergeMeadowArea(2, (SingleAreaMeadow) areaToAdapt,
							(SingleAreaMeadow) areaToMerge);

					break;

				default:
					log.warn("No case matched");
					throw new IllegalArgumentException("no case matched");
				}
			}
		}

		// check for town
		// if (firstAreaToAdapt.getCardAreaType().equals(CardAreaType.TOWN)) {
		//
		// // merge town area
		// mergeTownArea((SingleAreaTown) firstAreaToAdapt, (SingleAreaTown)
		// firstAreaToMerge);
		//
		// } else {
		//
		// // merge meadow area
		// mergeMeadowArea(0, (SingleAreaMeadow) firstAreaToAdapt,
		// (SingleAreaMeadow) firstAreaToMerge);
		//
		// // edge has more than one area (can only consist of meadow road
		// // meadow, in this order)
		// if (edgeToBeMergedWith.size() == 3) {
		//
		// // merge road area
		// mergeRoadArea(secondAreaToAdapt, secondAreaToMerge);
		//
		// // merge second meadow area
		// mergeMeadowArea(2, thirdAreaToAdapt, thirdAreaToMerge);
		// }
		// }

	}

	/**
	 * Merges two meadow areas.
	 * 
	 * @param edgeSpot
	 *            index of the edge that should be merged
	 * @param newMeadowArea
	 *            New Meadow Area that should be merged with the old MeadowArea
	 * @param oldMeadowArea
	 *            Old Meadow Area that should be merged with the new MeadowArea
	 */
	private void mergeMeadowArea(int edgeSpot, SingleAreaMeadow newMeadowArea,
			SingleAreaMeadow oldMeadowArea) {

		if (oldMeadowArea != newMeadowArea) {
			// add meeples from old to new
			for (Iterator<Meeple> iterator = oldMeadowArea.getMeeples()
					.iterator(); iterator.hasNext();) {
				Meeple meeple = iterator.next();
				newMeadowArea.getMeeples().add(meeple);
			}

			// add towns which are adjacent to the meadow of the placed
			// card to the list of towns of the SingleArea
			for (int i = 0; i < oldMeadowArea.getTowns().size(); i++) {
				newMeadowArea.getTowns().add(oldMeadowArea.getTowns().get(i));
			}

			// add cloisters which are adjacent to the meadow of the placed
			// card to the list of towns of the SingleArea
			for (int i = 0; i < oldMeadowArea.getCloisters().size(); i++) {
				newMeadowArea.getCloisters().add(
						oldMeadowArea.getCloisters().get(i));
			}

			// add positions of old SingleArea
			for (Iterator<Position> iterator = oldMeadowArea
					.getMeadowPositions().iterator(); iterator.hasNext();) {
				Position meadowPos = (Position) iterator.next();
				newMeadowArea.getMeadowPositions().add(meadowPos);
			}

			// change areas on all cards that were connected to the old
			for (Iterator<Position> iterator = oldMeadowArea
					.getMeadowPositions().iterator(); iterator.hasNext();) {
				Card card = getGameField().getCard((Position) iterator.next());
				List<Integer> meadowKeys = new ArrayList<>();
				for (int i = 0; i < card.getAreas().size(); i++) {
					if (card.getAreas().get(i) == CardAreaType.MEADOW) {
						meadowKeys.add(i);
					}
				}
				for (Iterator<Integer> iterator2 = meadowKeys.iterator(); iterator2
						.hasNext();) {
					Integer key = (Integer) iterator2.next();
					if (card.getSingleAreaMap().get(key) == oldMeadowArea) {
						card.getSingleAreaMap().put(key, newMeadowArea);
					}
				}
			}
			singleAreas.remove(oldMeadowArea);
		}
	}

	/**
	 * Merges two road areas.
	 * 
	 * @param newRoadArea
	 *            New Road Area that should be merged with the old RoadArea
	 * @param oldRoadArea
	 *            Old Road Area that should be merged with the new RoadArea
	 */
	private void mergeRoadArea(SingleAreaRoad newRoadArea,
			SingleAreaRoad oldRoadArea) {

		if (newRoadArea != oldRoadArea) {

			// add meeples from old to new
			for (Iterator<Meeple> iterator = oldRoadArea.getMeeples()
					.iterator(); iterator.hasNext();) {
				Meeple meeple = (Meeple) iterator.next();
				newRoadArea.getMeeples().add(meeple);
			}

			// add points from old area to new one
			newRoadArea.setPointCounter(newRoadArea.getPointCounter()
					+ oldRoadArea.getPointCounter());

			// use bigger multiplier
			if (oldRoadArea.getMultiplier() > newRoadArea.getMultiplier()) {
				newRoadArea.setMultiplier(oldRoadArea.getMultiplier());
			}

			// add positions of old SingleArea
			for (Iterator<Position> iterator = oldRoadArea.getRoadPosition()
					.iterator(); iterator.hasNext();) {
				Position meadowPos = (Position) iterator.next();
				newRoadArea.getRoadPosition().add(meadowPos);
			}

			// combines closed counter of new and old area
			newRoadArea.setClosed(newRoadArea.getClosed()
					+ oldRoadArea.getClosed());

			// change areas on all cards that were connected to the old area
			for (Iterator<Position> iterator = oldRoadArea.getRoadPosition()
					.iterator(); iterator.hasNext();) {
				Card card = getGameField().getCard((Position) iterator.next());
				List<Integer> roadKeys = new ArrayList<>();
				for (int i = 0; i < card.getAreas().size(); i++) {
					if (card.getAreas().get(i) == CardAreaType.ROAD) {
						roadKeys.add(i);
					}
				}
				for (Iterator<Integer> iterator2 = roadKeys.iterator(); iterator2
						.hasNext();) {
					Integer key = (Integer) iterator2.next();
					if (card.getSingleAreaMap().get(key) == oldRoadArea) {
						card.getSingleAreaMap().put(key, newRoadArea);
					}
				}
			}
			singleAreas.remove(oldRoadArea);
		} else {
			newRoadArea.setClosed(2);
		}
	}

	/**
	 * Merges two town areas.
	 * 
	 * @param newTownArea
	 *            new Town Area that should be merged with the old Town Area
	 * @param oldTownArea
	 *            old Town Area that should be merged with the new Town Area
	 */
	private void mergeTownArea(SingleAreaTown newTownArea,
			SingleAreaTown oldTownArea) {

		if (newTownArea != oldTownArea) {

			// add meeples from old to new
			for (Iterator<Meeple> iterator = oldTownArea.getMeeples()
					.iterator(); iterator.hasNext();) {
				Meeple meeple = (Meeple) iterator.next();
				newTownArea.getMeeples().add(meeple);
			}

			// add points from old area to new one
			newTownArea.setPointCounter(newTownArea.getPointCounter()
					+ oldTownArea.getPointCounter());

			// use bigger multiplier
			if (oldTownArea.getMultiplier() > newTownArea.getMultiplier()) {
				newTownArea.setMultiplier(oldTownArea.getMultiplier());
			}

			// add positions of old SingleArea
			for (Iterator<Position> iterator = oldTownArea.getTownPosition()
					.iterator(); iterator.hasNext();) {
				Position townPos = (Position) iterator.next();
				newTownArea.getTownPosition().add(townPos);
			}

			// combine open edges of old and new single area
			newTownArea
					.setOpenEdgesCounter((newTownArea.getOpenEdgesCounter() + oldTownArea
							.getOpenEdgesCounter()) - 2);

			// change areas on all cards that were connected to the old
			for (Iterator<Position> iterator = oldTownArea.getTownPosition()
					.iterator(); iterator.hasNext();) {
				Card card = getGameField().getCard((Position) iterator.next());
				List<Integer> townKeys = new ArrayList<>();
				for (int i = 0; i < card.getAreas().size(); i++) {
					if (card.getAreas().get(i) == CardAreaType.TOWN) {
						townKeys.add(i);
					}
				}
				for (Iterator<Integer> iterator2 = townKeys.iterator(); iterator2
						.hasNext();) {
					Integer key = (Integer) iterator2.next();
					if (card.getSingleAreaMap().get(key) == oldTownArea) {
						card.getSingleAreaMap().put(key, newTownArea);
					}
				}
			}

			// Override old SingleArea Town in MeadowSingleArea with newly merged SingleAreaTown
			for (AbstractSingleArea singleArea : singleAreas) {
				if (CardAreaType.MEADOW.equals(singleArea.getCardAreaType())) {
					SingleAreaMeadow meadow = (SingleAreaMeadow) singleArea;

					List<SingleAreaTown> tempTowns = new ArrayList<>();
					for (SingleAreaTown town : meadow.getTowns()) {
						tempTowns.add(town);
					}
					for (SingleAreaTown town : tempTowns) {
						if (town == oldTownArea) {
							meadow.getTowns().add(newTownArea);
							meadow.getTowns().remove(town);
						}
					}
				}
			}
			singleAreas.remove(oldTownArea);
		} else {
			newTownArea
					.setOpenEdgesCounter((newTownArea.getOpenEdgesCounter() - 2));
		}
	}
	
	/**
	 * Connects the information of the meeple placement in the view with the meeple placement of the logic.
	 * <p/>
	 * Adds a meeple to a singleArea. Checks whether it is a special meeple. If it is a Big Meeple (in case the
	 * extension Big meeple is activated) there are two meeples added to the SingleArea.
	 * 
	 * @param cardPosition
	 *            Position of the Card
	 * @param placement
	 *            Placement of the Meeple
	 * @param player
	 *            Player that placed the Meeple
	 * @return true if meeplePlacement was successful, false if not.
	 */
	public boolean meeplePlaced(Position cardPosition, Placement placement,
			Player player) {

		Card currentCard = getGameField().getCard(cardPosition);
		AbstractSingleArea currentSingleArea = currentCard.getSingleAreaMap()
				.get(placement.getCommunicationPlacement());
		Meeple meeple = new Meeple(cardPosition,
				placement.getCommunicationPlacement(), player,
				placement.getSpecialMeeple());

		boolean canPlaceMeeple = true;
		boolean canPlaceBishop = true;
		boolean bigMeeple = false;

		// can't place bishop if singleArea is not a town
		if (currentSingleArea.getClass() != SingleAreaTown.class) {
			canPlaceBishop = false;
		}

		// can't place meeple/bishop if there is already a meeple/ bishop on singleArea
		for (Meeple tempMeeple : currentSingleArea.getMeeples()) {
			if (tempMeeple.getSpecialType() == SpecialMeepleType.BISHOP) {
				canPlaceBishop = false;
			} else {
				canPlaceMeeple = false;
			}
		}

		// Does a meadowSingleArea already contain the town and is there a bishop in this town?
		if (canPlaceBishop) {
			outerloop: for (AbstractSingleArea singleArea : singleAreas) {
				if (singleArea.getCardAreaType() == CardAreaType.MEADOW) {
					SingleAreaMeadow meadow = (SingleAreaMeadow) singleArea;
					if (meadow.getTowns().contains(currentSingleArea)) {
						for (SingleAreaTown town : meadow.getTowns()) {
							for (Meeple townMeeple : town.getMeeples()) {
								if (meeple.getSpecialType() == SpecialMeepleType.BISHOP) {
									canPlaceBishop = false;
									break outerloop;
								}
							}
						}
					}
				}
			}
		}

		// Set bishop if possible
		if (placement.getSpecialMeeple() != null
				&& placement.getSpecialMeeple()
						.equals(SpecialMeepleType.BISHOP)) {
			if (canPlaceBishop && player.placeMeeple(meeple, bigMeeple)) {
				currentSingleArea.getMeeples().add(meeple);
				return true;
			}
			return false;
		} else if (placement.getSpecialMeeple() != null
				&& placement.getSpecialMeeple().equals(
						SpecialMeepleType.BIGMEEPLE)) {
			bigMeeple = true;
		}

		if (canPlaceMeeple && player.placeMeeple(meeple, bigMeeple)) {
			currentSingleArea.getMeeples().add(meeple);
			return true;
		}
		return false;
	}
	
	/*
	 * Getter and setter
	 */

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((gameName == null) ? 0 : gameName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Game other = (Game) obj;
		if (gameName == null) {
			if (other.gameName != null)
				return false;
		} else if (!gameName.equals(other.gameName))
			return false;
		return true;
	}

	public void setState(GameStatus state) {
		this.state = state;
	}

	public CardDeck getCardDeck() {
		return cardDeck;
	}

	public void setPlayerList(Map<String, Player> playerList) {
		this.playerList = playerList;
	}

	public void setSpectatorList(Map<String, Spectator> specList) {
		this.spectatorList = specList;
	}

	public int getGameID() {
		return gameID;
	}

	public String getGameName() {
		return gameName;
	}

	public Map<String, Spectator> getSpectatorList() {
		return spectatorList;
	}

	public Map<String, Player> getPlayerList() {
		return playerList;
	}

	public GameField getGameField() {
		return gameField;
	}
	
	public void setGameField(GameField gameField){
		this.gameField = gameField;
	}

	public Card getCurrentCard() {
		return currentCard;
	}

	public void setCurrentCard(Card card) {
		currentCard = card;
	}

	public Position getCurrentPosition() {
		return currentPosition;
	}

	public GameStatus getState() {
		return state;
	}

	public Set<CapabilitiesType> getExtensions() {
		return extensions;
	}
	
	public void setSingleAreas(List<AbstractSingleArea> singleAreas){
		this.singleAreas = singleAreas;
	}
	
	public List<AbstractSingleArea> getSingleAreas(){
		return singleAreas;
	}
}
