package ai;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import shared.enums.CardAreaType;
import shared.enums.SpecialMeepleType;
import shared.model.*;

import java.util.*;

public class AIGame {
	
	private Logger log = LogManager.getLogger("ERROR");

	private List<AbstractSingleArea> singleAreas;

	private AILogic aiLogic;
	
	private GameField gameField;

	public AIGame(AILogic aiLogic) {
		this.aiLogic = aiLogic;
		this.gameField = new GameField();
		createInitialCardAndSingleAreas();
	}

	public void checkCardPlacement(Position cardPosition, int rotation,
			int placement, Player player, Card card) {
		card.rotate(rotation);
		placeCard(cardPosition, card);
		if (placement != -1) {
			meeplePlaced(cardPosition, placement, player);
			if (checkCompletion()) {
				checkScore();
			}
		} else {
			if (checkCompletion()) {
				checkScore();
			}
		}
	}

	public void placeCard(Position pos, Card card) {
		if (matchingEdges(card, pos)) {
			gameField.addCard(pos, card);
			createSingleAreasForCard(pos, card);

			// merge Edges
			Position top = new Position(pos.getX(), pos.getY() - 1);
			if (gameField.isPositionTaken(top)) {
				mergeEdges(pos, top, 0);
			}

			Position left = new Position(pos.getX() - 1, pos.getY());
			if (gameField.isPositionTaken(left)) {
				mergeEdges(pos, left, 1);
			}

			Position bottom = new Position(pos.getX(), pos.getY() + 1);
			if (gameField.isPositionTaken(bottom)) {
				mergeEdges(pos, bottom, 2);
			}

			Position right = new Position(pos.getX() + 1, pos.getY());
			if (gameField.isPositionTaken(right)) {
				mergeEdges(pos, right, 3);
			}
		}
	}

	private void createInitialCardAndSingleAreas() {
		singleAreas = new ArrayList<>();
		singleAreas.add(new SingleAreaMeadow(null, null, new Position(0, 0)));
		singleAreas.add(new SingleAreaRoad(false, new Position(0, 0), 1));
		singleAreas.add(new SingleAreaMeadow(null, null, new Position(0, 0)));
		singleAreas.add(new SingleAreaTown(1, false, new Position(0, 0), 1));
		((SingleAreaMeadow) singleAreas.get(2)).getTowns().add(
				(SingleAreaTown) singleAreas.get(3));

		Card firstCard = CardDeck.createStartCard();
		firstCard.getSingleAreaMap().put(1, singleAreas.get(0));
		firstCard.getSingleAreaMap().put(2, singleAreas.get(1));
		firstCard.getSingleAreaMap().put(3, singleAreas.get(2));
		firstCard.getSingleAreaMap().put(0, singleAreas.get(3));

		gameField.addCard(new Position(0, 0), firstCard);
	}

	/**
	 * Creates the initial SingleAreas for a given card.
	 * <p>
	 * By going through the areas array a AbstractSingleArea specific for the
	 * CardAreaType is created.
	 * 
	 * @param card
	 *            the current card singleAreas should be created for
	 * @param pos
	 *            the current position of the card
	 * @see AbstractSingleArea
	 * @see CardAreaType
	 */
	private void createSingleAreasForCard(Position pos, Card card) {
		for (int i = 0; i < card.getAreas().size(); i++) {
			CardAreaType type = card.getAreas().get(i);

			switch (type) {
			case CLOISTER:
				SingleAreaCloister singleAreaMonastry = new SingleAreaCloister(pos);
				card.getSingleAreaMap().put(i, singleAreaMonastry);
				singleAreas.add(singleAreaMonastry);
				break;

			case MEADOW:
				SingleAreaMeadow singleAreaMeadow = new SingleAreaMeadow(null, null, pos);

				// add towns which are adjacent to the meadow of the placed
				// card to the list of towns of the SingleArea
				for (int j = 0; j < card.getAdjacency().get(i).size(); j++) {
					int index = card.getAdjacency().get(i).get(j);
					if (card.getAreas().get(index) == CardAreaType.TOWN) {
						singleAreaMeadow.getTowns().add((SingleAreaTown) card.getSingleAreaMap().get(index));
					}
				}

				// add cloisters which are adjacent to the meadow of the placed
				// card to the list of cloisters of the SingleArea
				for (int j = 0; j < card.getAdjacency().get(i).size(); j++) {
					int index = card.getAdjacency().get(i).get(j);
					if (card.getAreas().get(index) == CardAreaType.CLOISTER) {
						singleAreaMeadow.getCloisters().add((SingleAreaCloister) card.getSingleAreaMap().get(index));
					}
				}

				card.getSingleAreaMap().put(i, singleAreaMeadow);
				singleAreas.add(singleAreaMeadow);
				break;

			case ROAD:
				// checks if road has an end
				// has end if cloister exists
				boolean hasMonastry = false;
				// or card has at least 2 roads
				int roadCount = 0;
				for (Iterator<CardAreaType> iterator = card.getAreas().iterator(); iterator.hasNext();) {
					CardAreaType type2 = (CardAreaType) iterator.next();
					if (type2 == CardAreaType.CLOISTER) {
						hasMonastry = true;
						break;
					}
					if (type2 == CardAreaType.ROAD) {
						roadCount++;
					}
				}

				// or a town is adjacent to road
				boolean townAdjacentToRoad = false;
				int indexOfRoad = card.getAreas().indexOf(CardAreaType.ROAD);
				ArrayList<Integer> roadAdjacentToTownTemp = card.getAdjacency().get(indexOfRoad);
				for (Iterator<Integer> iterator = roadAdjacentToTownTemp.iterator(); iterator.hasNext();) {
					Integer indexOfTown = iterator.next();
					// TODO Abgleich auf index stimmt nicht
					if (card.getAreas().get(indexOfTown) == CardAreaType.TOWN) {
						townAdjacentToRoad = true;
						break;
					}
				}

				int multiplierRoad = 0;
				if (card.getMultiplier().size() != 0) {
					multiplierRoad = card.getMultiplier().get(0);
				}

				SingleAreaRoad singleAreaRoad;
				if (townAdjacentToRoad || hasMonastry || roadCount >= 2) {
					singleAreaRoad = new SingleAreaRoad(true, pos, multiplierRoad);
				} else {
					singleAreaRoad = new SingleAreaRoad(false, pos, multiplierRoad);
				}

				card.getSingleAreaMap().put(i, singleAreaRoad);
				singleAreas.add(singleAreaRoad);
				break;

			case TOWN:
				// count open edges of SingleArea
				// count edges with cities on card
				int openEdges = 0;
				int edgesWithTowns = 0;
				for (Iterator<ArrayList<Integer>> iterator = card.getEdges().iterator(); iterator.hasNext();) {
					ArrayList<Integer> edge = (ArrayList<Integer>) iterator.next();
					if (edge.size() == 1 && card.getAreas().get(edge.get(0)) == CardAreaType.TOWN) {
						edgesWithTowns++;
					}
				}
				// case 1, 3 or 4 edges with cities: there are as many open
				// edges as there are edges with towns
				switch (edgesWithTowns) {
				case 1:
				case 3:
				case 4:
					openEdges = edgesWithTowns;
					break;

				// case 2 edges with cities: check if edges are connected
				case 2:
					int numberOfTowns = 0;
					for (Iterator<CardAreaType> iterator = card.getAreas().iterator(); iterator.hasNext();) {
						CardAreaType type3 = (CardAreaType) iterator.next();
						if (type3 == CardAreaType.TOWN) {
							numberOfTowns++;
						}
					}
					// if one town then there are two open edges else there is
					// one connected city
					if (numberOfTowns == 1) {
						openEdges = edgesWithTowns;
					} else {
						openEdges = 1;
					}
					break;

				default:
					log.error("No case matched");
					throw new IllegalArgumentException("No case matched");
				}

				int multiplierTown = 2;
				if (card.getMultiplier().size() != 0) {
					multiplierRoad = card.getMultiplier().get(0);
				}

				// check for bonus
				SingleAreaTown singleAreaTown;
				if (card.getBonus().size() != 0) {
					singleAreaTown = new SingleAreaTown(openEdges, true, pos, multiplierTown);
				} else {
					singleAreaTown = new SingleAreaTown(openEdges, false, pos, multiplierTown);
				}
				card.getSingleAreaMap().put(i, singleAreaTown);
				singleAreas.add(singleAreaTown);
				break;

			default:
				log.error("No case matched");
				throw new IllegalArgumentException("No case matched");
			}
		}
	}

	private void meeplePlaced(Position cardPosition, int areaPlacement,
			Player player) {
		Meeple meeple = new Meeple(cardPosition, areaPlacement, player, null);
		Card currentCard = gameField.getCard(cardPosition);
		if(currentCard == null || currentCard.getSingleAreaMap() == null || player == null){
			System.out.println("CARD NULL!");
		}
		AbstractSingleArea currentSingleArea;
		currentSingleArea = currentCard.getSingleAreaMap().get(areaPlacement);
		boolean bigMeeple = meeple.getSpecialType() == SpecialMeepleType.BIGMEEPLE ? true : false;
		if (currentSingleArea.getMeeples().size() == 0 && player.placeMeeple(meeple, bigMeeple)) {
			currentSingleArea.getMeeples().add(meeple);
		}
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
	public final boolean matchingEdges(Card currentCard,
			Position currentPosition) {
		if (gameField.checkNeighbours(currentPosition)
				&& !gameField.isPositionTaken(currentPosition)
				&& matchingTopEdge(currentCard, currentPosition)
				&& matchingLeftEdge(currentCard, currentPosition)
				&& matchingBottomEdge(currentCard, currentPosition)
				&& matchingRightEdge(currentCard, currentPosition)) {
			return true;
		}
		return false;

	}

	/**
	 * method checks if edges of currentCard and top card fit together
	 * 
	 * @param currentCard
	 * @param currentPosition
	 * @return boolean if edges fit or not
	 */
	private boolean matchingTopEdge(Card currentCard, Position currentPosition) {
		if (gameField.getCardNorth(currentPosition) != null) {
			Card northCard = gameField.getCardNorth(
					currentPosition);
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

		if (gameField.getCardWest(currentPosition) != null) {
			Card westCard = gameField.getCardWest(currentPosition);
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
		if (gameField.getCardSouth(currentPosition) != null) {
			Card southCard = gameField.getCardSouth(
					currentPosition);
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
	 * Checks if edges of rightCard and currentCard fit together
	 * 
	 * @param currentCard
	 *            Card to set
	 * @param currentPosition
	 *            position where the current card should be set
	 * @return boolean if edges fit or not
	 */
	private boolean matchingRightEdge(Card currentCard, Position currentPosition) {
		if (gameField.getCardEast(currentPosition) != null) {
			Card eastCard = gameField.getCardEast(currentPosition);
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
	 * Merges edges into one singleArea.
	 * <p>
	 * By getting the two cards which should be merged by looking on their positions on the gameField these cards are
	 * united to one singleArea.
	 * 
	 * @param newCardsPosition
	 *            Position of the new Card that was just set
	 * @param oldCardsPosition
	 *            Position of the Card that was already set when the new Card was set
	 * @param edge
	 *            index of the edge that should be merged
	 */
	private void mergeEdges(Position newCardsPosition,
			Position oldCardsPosition, int edge) {

		Card placed = gameField.getCard(oldCardsPosition);
		Card toBePlaced = gameField.getCard(newCardsPosition);

		List<Integer> edgeToBeMerged = null;
		List<Integer> edgeToBeMergedWith = null;

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

			} else {
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
	}

	/**
	 * Merges two town areas.
	 * 
	 * @param newTownArea
	 * @param newTownArea
	 * @param oldTownArea
	 */
	private void mergeTownArea(SingleAreaTown newTownArea, SingleAreaTown oldTownArea) {

		if (newTownArea != oldTownArea) {

			// add meeples from old to new
			for (Iterator<Meeple> iterator = oldTownArea.getMeeples().iterator(); iterator.hasNext();) {
				Meeple meeple = (Meeple) iterator.next();
				newTownArea.getMeeples().add(meeple);
			}

			// add points from old area to new one
			newTownArea.setPointCounter(newTownArea.getPointCounter() + oldTownArea.getPointCounter());

			// use bigger multiplier
			if (oldTownArea.getMultiplier() > newTownArea.getMultiplier()) {
				newTownArea.setMultiplier(oldTownArea.getMultiplier());
			}

			// add positions of old SingleArea
			for (Iterator<Position> iterator = oldTownArea.getTownPosition().iterator(); iterator.hasNext();) {
				Position townPos = (Position) iterator.next();
				newTownArea.getTownPosition().add(townPos);
			}

			// combine open edges of old and new single area
			newTownArea
					.setOpenEdgesCounter((newTownArea.getOpenEdgesCounter() + oldTownArea.getOpenEdgesCounter()) - 2);

			// change areas on all cards that were connected to the old
			for (Iterator<Position> iterator = oldTownArea.getTownPosition().iterator(); iterator.hasNext();) {
				Card card = gameField.getCard((Position) iterator.next());
				List<Integer> townKeys = new ArrayList<>();
				for (int i = 0; i < card.getAreas().size(); i++) {
					if (card.getAreas().get(i) == CardAreaType.TOWN) {
						townKeys.add(i);
					}
				}
				for (Iterator<Integer> iterator2 = townKeys.iterator(); iterator2.hasNext();) {
					Integer key = (Integer) iterator2.next();
					if (card.getSingleAreaMap().get(key) == oldTownArea) {
						card.getSingleAreaMap().put(key, newTownArea);
					}
				}
			}

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
			newTownArea.setOpenEdgesCounter((newTownArea.getOpenEdgesCounter() - 2));
		}
	}

	private void mergeMeadowArea(int edgeSpot, SingleAreaMeadow newMeadowArea, SingleAreaMeadow oldMeadowArea) {

		if (oldMeadowArea != newMeadowArea) {
			// add meeples from old to new
			for (Iterator<Meeple> iterator = oldMeadowArea.getMeeples().iterator(); iterator.hasNext();) {
				Meeple meeple = iterator.next();
				newMeadowArea.getMeeples().add(meeple);
			}

			// add towns which are adjacent to the meadow of the placed
			// card to the list of towns of the SingleArea
			for (int i = 0; i < oldMeadowArea.getTowns().size(); i++) {
				newMeadowArea.getTowns().add(oldMeadowArea.getTowns().get(i));
			}

			// add positions of old SingleArea
			for (Iterator<Position> iterator = oldMeadowArea.getMeadowPositions().iterator(); iterator.hasNext();) {
				Position meadowPos = (Position) iterator.next();
				newMeadowArea.getMeadowPositions().add(meadowPos);
			}

			// change areas on all cards that were connected to the old
			for (Iterator<Position> iterator = oldMeadowArea.getMeadowPositions().iterator(); iterator.hasNext();) {
				Card card = gameField.getCard((Position) iterator.next());
				List<Integer> meadowKeys = new ArrayList<>();
				for (int i = 0; i < card.getAreas().size(); i++) {
					if (card.getAreas().get(i) == CardAreaType.MEADOW) {
						meadowKeys.add(i);
					}
				}
				for (Iterator<Integer> iterator2 = meadowKeys.iterator(); iterator2.hasNext();) {
					Integer key = (Integer) iterator2.next();
					if (card.getSingleAreaMap().get(key) == oldMeadowArea) {
						card.getSingleAreaMap().put(key, newMeadowArea);
					}
				}
			}

			singleAreas.remove(oldMeadowArea);

		}
	}

	private void mergeRoadArea(SingleAreaRoad newRoadArea, SingleAreaRoad oldRoadArea) {

		if (newRoadArea != oldRoadArea) {

			// add meeples from old to new
			for (Iterator<Meeple> iterator = oldRoadArea.getMeeples().iterator(); iterator.hasNext();) {
				Meeple meeple = (Meeple) iterator.next();
				newRoadArea.getMeeples().add(meeple);
			}

			// add points from old area to new one
			newRoadArea.setPointCounter(newRoadArea.getPointCounter() + oldRoadArea.getPointCounter());

			// add positions of old SingleArea
			for (Iterator<Position> iterator = oldRoadArea.getRoadPosition().iterator(); iterator.hasNext();) {
				Position meadowPos = (Position) iterator.next();
				newRoadArea.getRoadPosition().add(meadowPos);
			}

			// combines closed counter of new and old area
			newRoadArea.setClosed(newRoadArea.getClosed() + oldRoadArea.getClosed());

			// change areas on all cards that were connected to the old area
			for (Iterator<Position> iterator = oldRoadArea.getRoadPosition().iterator(); iterator.hasNext();) {
				Card card = gameField.getCard((Position) iterator.next());
				List<Integer> roadKeys = new ArrayList<>();
				for (int i = 0; i < card.getAreas().size(); i++) {
					if (card.getAreas().get(i) == CardAreaType.ROAD) {
						roadKeys.add(i);
					}
				}
				for (Iterator<Integer> iterator2 = roadKeys.iterator(); iterator2.hasNext();) {
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

	public void checkScore() {

		// List of areas which are deleted after scoring
		for (AbstractSingleArea singleArea : singleAreas) {

			if (singleArea != null && singleArea.getMeeples().size() != 0 && singleArea.isFinished()) {
				List<String> meepleOwners = new ArrayList<>();
				for (Meeple singleMeeple : singleArea.getMeeples()) {

					// gets the owner of a specific meeple
					meepleOwners.add(singleMeeple.getOwner().getNick());
				}
				Map<Integer, ArrayList<String>> frequencyMap = new HashMap<>();
				for (Player player : aiLogic.getPlayerList().values()) {

					// gets the highest frequency of meepleObjects with
					// parameter of
					// meeple = nick of owner
					// out of the meepleOwners-List

					int frequency = Collections.frequency(meepleOwners,
							player.getNick());
					if (frequencyMap.containsKey(frequency)) {
						frequencyMap.get(frequency).add(player.getNick());
					} else {
						frequencyMap.put(frequency, new ArrayList<String>());
						frequencyMap.get(frequency).add(player.getNick());
					}
				}

				ArrayList<String> areaOwners = frequencyMap.get(Collections
						.max(frequencyMap.keySet()));

				// checks for specific type of singleArea and if its
				// finished
				// when finished = calculate score and delete the specific
				// singleArea out of the list of areas

				if ((!"Meadow".equals(singleArea.toString()))
						&& singleArea.isFinished()) {
					scoreHelper(singleArea, meepleOwners, frequencyMap,
							areaOwners);
				}

				// TODO
				// deletes the meeple from the meepleList if singArea is
				// finished

				// for(int i = 0; i < singleAreas.size(); i++){
				// for(Player p : getPlayerList().values()){
				// p.removeMeeple(singleAreas.get(i).getMeeples());
				// }
				// }
			}
		}
	}

	/**
	 * Utility method for calculating the points of the different singleAreas.
	 * 
	 * @param singleArea
	 * @param meepleOwners
	 * @param frequencyMap
	 * @param areaOwners
	 */
	private void scoreHelper(AbstractSingleArea singleArea,
			List<String> meepleOwners,
			Map<Integer, ArrayList<String>> frequencyMap,
			ArrayList<String> areaOwners) {

		// Iterates through the list of areaOwners
		for (String owner : areaOwners) {

			// gets a speicifc owner and adds the calculated score for the area
			// to his score
			aiLogic.getPlayerList().get(owner)
					.addToScore(singleArea.calculatePoints());
		}

		// Iterates through the list of meeples on specific singleArea
		// gets meeple back to the user and deletes meeple out of the set
		List<Meeple> meepleList = singleArea.getMeeples();
		for (Meeple meeple : meepleList) {
			String meepleOwner = meeple.getOwner().getNick();
			Player player = aiLogic.getPlayerList().get(meepleOwner);
			player.getMeeplesSet().remove(meeple);
			player.addToMeeplesLeft(Collections.max(frequencyMap.keySet()));
		}
		singleArea.getMeeples().clear();
	}

	/**
	 * the method checks if a cloister is surrounded by 8 cards. If its
	 * surrounded finished = true; method checks for each ingleArea in the list
	 * if its finished or not
	 * 
	 * @return boolean if singleArea is finished or not
	 */
	public boolean checkCompletion() {
		boolean scoreHasToBeCalculated = false;
		for (AbstractSingleArea abstractArea : singleAreas) {
			if (abstractArea != null) {
				if ("Cloister".equals(abstractArea.toString())) {
					SingleAreaCloister cloisterArea = (SingleAreaCloister) abstractArea;
					if (gameField.checkSurroundingCards(
							cloisterArea.getPosition())) {
						cloisterArea.setFinished();
					}
				}
				if (abstractArea.isFinished()) {
					scoreHasToBeCalculated = true;
				}
			}
		}
		return scoreHasToBeCalculated;
	}

	public List<AbstractSingleArea> checkCompletionAlternative() {
		List<AbstractSingleArea> finishedAreas = new ArrayList<>();
		for (AbstractSingleArea abstractArea : singleAreas) {
			if (abstractArea != null) {
				if ("Cloister".equals(abstractArea.toString())) {
					SingleAreaCloister cloisterArea = (SingleAreaCloister) abstractArea;
					if (gameField.checkSurroundingCards(
							cloisterArea.getPosition())) {
						cloisterArea.setFinished();
					}
				}
				if (abstractArea.isFinished()) {
					finishedAreas.add(abstractArea);
				}
			}
		}
		return finishedAreas;
	}

	public List<AbstractSingleArea> getSingleAreas() {
		return singleAreas;
	}

	public void setSingleAreas(List<AbstractSingleArea> areas) {
		singleAreas = areas;
	}

	public GameField getGameField() {
		return gameField;
	}

	public void setGameField(GameField gameField) {
		this.gameField = gameField;
	}
}
