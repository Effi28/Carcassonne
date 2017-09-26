package server.model.game;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import server.model.PlayerThread;
import server.model.serverCommunication.execution.ServerControl;
import server.model.serverCommunication.utility.ServerMessageBuilder;
import shared.enums.*;
import shared.model.*;

import java.util.*;

/**
 * Manages one game.
 * <p/>
 * Implements the logic for the game. It creates the initial SingleAreas on the StartCard and manages the following
 * SingleAreas in a list. Class holds methods for placing a Card on a current Position, creating the SingleAreas for the
 * card and melt the SingleAreas together by overwrite SingleAreas with higher index by SingleAreas with lower index in
 * the list.
 *
 * @version 24.01.2014
 * @see AbstractSingleArea
 * @see Game
 * @see ServerControl
 * @see PlayerThread
 */
public final class ServerGame extends Game {

	/**
	 * Logger that logs the exception-messages
	 */
	private static Logger log = LogManager.getLogger("ERROR");
	/**
	 * Info-logger
	 */
	private static Logger infoLog = LogManager.getLogger("INFO");
	/**
	 * Warn-log
	 */
	private static Logger warnLog = LogManager.getLogger("WARN");
	/**
	 * the constant instance host of the User-class
	 */
	private final User host;

	/**
	 * represents all the single areas that are needed to evaluate the scores
	 */
	private List<AbstractSingleArea> singleAreas;

	/**
	 * the instance of the PlayerThread that counts down the time that every player has to make a move
	 */
	private PlayerThread thread;

	/**
	 * Reference to the server
	 */
	private final ServerControl server;

	/**
	 * The time which a Player has to make a move
	 */
	private int turnTime;

	/**
	 * Standard constructor for this class.
	 *
	 * @param gameName
	 *            name of the game
	 * @param host
	 *            User who hosts this game
	 * @param gameID
	 *            ID of the game
	 * @param server
	 *            reference to the server
	 * @param extensions
	 *            extension selected for the game
	 * @param turnTime
	 *            turntime of a single turn
	 */
	public ServerGame(String gameName, Player host, int gameID,
			ServerControl server, int turnTime, Set<CapabilitiesType> extensions) {
		super(gameName, gameID, extensions);
		super.getPlayerList().put(host.getNick(), host);
		this.singleAreas = super.getSingleAreas();
		this.host = host;
		this.server = server;
		this.turnTime = turnTime;

		createInitialCardAndSingleAreas();
	}

	/**
	 * Starts the Thread that counts the time a user has in order to make a move.
	 *
	 * @see PlayerThread
	 */
	public void startThread() {
		thread = new PlayerThread(this, turnTime);
	}

	public void remove() {
		if (!(thread == null)) {
			thread.remove();
			thread = null;
		}
	}

	/**
	 * This method is the interface between serverCommunication and model.
	 * <p/>
	 * It calls the methods of ServerGame that checks whether a move is legal or not. If a move is legal the method
	 * checks if a singleArea is finished. When an area is finished the points are calculated.
	 *
	 * @param cardPosition
	 *            the position on which the player wants to put the card
	 * @param rotation
	 *            the value of the card rotation
	 * @param placement
	 *            the index of the area on which a meeple is placed, -1 if no meeple placed
	 * @param nick
	 *            Nick of the player that made a move
	 * @return returns a String which indicates if the move was legal or not; It's not your turn/move is
	 *         illegal/legal/legalButMeeple
	 */
	public String checkCardPlacement(Position cardPosition, int rotation,
			Placement placement, String nick) {

		// Gets the player who made the move
		Player player = null;
		for (Player tempPlayer : getPlayerList().values()) {
			if (tempPlayer.getNick().equals(nick)) {
				player = tempPlayer;
			}
		}

		// If a player made a move but it's not his turn
		if (!thread.getCurrentPlayer().getNick().equals(player.getNick())) {
			infoLog.info(player.getNick()
					+ "made a move but it's not his turn.");
			return Reason.ITSNOTYOURTURN.toString();
		}

		Card tmpCard = getCurrentCard(false);
		Card card = tmpCard.deepCopy();
		card.rotate(rotation);

		if (placeCard(cardPosition, card)) {
			// If a meeple was set
			if (placement.getCommunicationPlacement() != -1) {
				if (meeplePlaced(cardPosition, placement, player)) {
					if (checkCompletion()) {
						checkScore();
					}
					infoLog.info("Legal move on position x: "
							+ cardPosition.getX() + " y: "
							+ cardPosition.getY());
					return "legal"; // Legal move
				} else {
					if (checkCompletion()) {
						checkScore();
					}
				}
				infoLog.info("Legal move but Meeple was deleted on position x: "
						+ cardPosition.getX() + " y: " + cardPosition.getY());
				return "legalButMeeple"; // Meeple was deleted
			} else { // If no meeple was set
				if (checkCompletion()) {
					checkScore();
				}
				infoLog.info("Legal move on position x: " + cardPosition.getX()
						+ " y: " + cardPosition.getY() + "Card: "
						+ card.toString());
				return "legal";
			}
		} else { // if placeCard failed the move was illegal
			infoLog.info("Illegal move on position x: " + cardPosition.getX()
					+ " y: " + cardPosition.getY() + "Card: " + card.toString());
			return Reason.MOVEISILLEGAL.toString();
		}
	}

	/**
	 * Tries to place card at the given position.
	 * <p/>
	 * It is determined by first checking if the card matches with the surrounding cards on the specified position. If
	 * it does, it merges with the neighbourCards.
	 *
	 * @param pos
	 *            Position of the card to be placed
	 * @param card
	 *            Card to be placed
	 * @return true if successful, false if placing failed for some reason
	 */
	private boolean placeCard(Position pos, Card card) {
		if (matchingEdges(card, pos)) {
			getGameField().addCard(pos, card);
			createSingleAreasForCard(pos, card);

			// merge Edges
			Position top = new Position(pos.getX(), pos.getY() - 1);
			if (getGameField().isPositionTaken(top)) {
				mergeEdges(pos, top, 0);
			}

			Position left = new Position(pos.getX() - 1, pos.getY());
			if (getGameField().isPositionTaken(left)) {
				mergeEdges(pos, left, 1);
			}

			Position bottom = new Position(pos.getX(), pos.getY() + 1);
			if (getGameField().isPositionTaken(bottom)) {
				mergeEdges(pos, bottom, 2);
			}

			Position right = new Position(pos.getX() + 1, pos.getY());
			if (getGameField().isPositionTaken(right)) {
				mergeEdges(pos, right, 3);
			}
			return true;
		}
		return false;
	}

	/**
	 * Creates the four singleAreas on the startCard and puts it on the initialized singleAreaMap.
	 */
	private void createInitialCardAndSingleAreas() {
		singleAreas.add(new SingleAreaMeadow(null, null, new Position(0, 0)));
		singleAreas.add(new SingleAreaRoad(false, new Position(0, 0), 0));
		singleAreas.add(new SingleAreaMeadow(null, null, new Position(0, 0)));
		singleAreas.add(new SingleAreaTown(1, false, new Position(0, 0), 2));
		((SingleAreaMeadow) singleAreas.get(2)).getTowns().add(
				(SingleAreaTown) singleAreas.get(3));

		Card firstCard = super.getCardDeck().getStartCard();
		firstCard.getSingleAreaMap().put(1, singleAreas.get(0));
		firstCard.getSingleAreaMap().put(2, singleAreas.get(1));
		firstCard.getSingleAreaMap().put(3, singleAreas.get(2));
		firstCard.getSingleAreaMap().put(0, singleAreas.get(3));

		getGameField().addCard(new Position(0, 0), firstCard);
	}

	/**
	 * Creates the initial SingleAreas for a given card.
	 * <p/>
	 * By going through the areas array a AbstractSingleArea specific for the CardAreaType is created.
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
				SingleAreaCloister singleAreaMonastry = new SingleAreaCloister(
						pos);
				card.getSingleAreaMap().put(i, singleAreaMonastry);
				singleAreas.add(singleAreaMonastry);
				break;

			case MEADOW:
				SingleAreaMeadow singleAreaMeadow = new SingleAreaMeadow(null,
						null, pos);

				// add towns which are adjacent to the meadow of the placed
				// card to the list of towns of the SingleArea
				for (int j = 0; j < card.getAdjacency().get(i).size(); j++) {
					int index = card.getAdjacency().get(i).get(j);
					if (card.getAreas().get(index) == CardAreaType.TOWN) {
						singleAreaMeadow.getTowns().add(
								(SingleAreaTown) card.getSingleAreaMap().get(
										index));
					}
				}

				// add cloisters which are adjacent to the meadow of the placed
				// card to the list of cloisters of the SingleArea
				for (int j = 0; j < card.getAdjacency().get(i).size(); j++) {
					int index = card.getAdjacency().get(i).get(j);
					if (card.getAreas().get(index) == CardAreaType.CLOISTER) {
						singleAreaMeadow.getCloisters().add(
								(SingleAreaCloister) card.getSingleAreaMap()
										.get(index));
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
				for (Iterator<CardAreaType> iterator = card.getAreas()
						.iterator(); iterator.hasNext();) {
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
				ArrayList<Integer> roadAdjacentToTownTemp = card.getAdjacency()
						.get(indexOfRoad);
				for (Iterator<Integer> iterator = roadAdjacentToTownTemp
						.iterator(); iterator.hasNext();) {
					Integer indexOfTown = iterator.next();
					if (card.getAreas().get(indexOfTown) == CardAreaType.TOWN) {
						townAdjacentToRoad = true;
						break;
					}
				}

				int multiplierRoad = 1;
				if (card.getMultiplier().size() != 0
						&& card.getMultiplier().get(0) == i) {
					multiplierRoad = 2;
				}

				SingleAreaRoad singleAreaRoad;
				if (townAdjacentToRoad || hasMonastry || roadCount >= 2) {
					singleAreaRoad = new SingleAreaRoad(true, pos,
							multiplierRoad);
				} else {
					singleAreaRoad = new SingleAreaRoad(false, pos,
							multiplierRoad);
				}

				card.getSingleAreaMap().put(i, singleAreaRoad);
				singleAreas.add(singleAreaRoad);
				break;

			case TOWN:
				// count open edges of SingleArea
				// count edges with cities on card
				int openEdges = 0;
				int edgesWithTowns = 0;
				for (Iterator<ArrayList<Integer>> iterator = card.getEdges()
						.iterator(); iterator.hasNext();) {
					ArrayList<Integer> edge = (ArrayList<Integer>) iterator
							.next();
					if (edge.size() == 1
							&& card.getAreas().get(edge.get(0)) == CardAreaType.TOWN) {
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
					for (Iterator<CardAreaType> iterator = card.getAreas()
							.iterator(); iterator.hasNext();) {
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
				if (card.getMultiplier().size() != 0
						&& card.getMultiplier().get(0) == i) {
					multiplierTown = 3;
				}

				// check for bonus
				SingleAreaTown singleAreaTown;
				if (card.getBonus().size() != 0) {
					singleAreaTown = new SingleAreaTown(openEdges, true, pos,
							multiplierTown);
				} else {
					singleAreaTown = new SingleAreaTown(openEdges, false, pos,
							multiplierTown);
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

	/**
	 * Delegates the information about the game to the server.
	 * <p/>
	 * This method is only called when the playerThread has drawn all tiles and the game has come to an end.
	 *
	 * @param game
	 *            Information about the game that just ended
	 */
	public void delegateGameUpdateFromPlayerThread(ServerGame game) {
		server.delegateGameUpdateFromPlayerThread(game);

	}

	/**
	 * Checks if it is possible to put the card anywhere on the gameField.
	 * <p/>
	 * By checking the positions around the outer cards and by checking if there are any free spaces between the cards
	 * that are already set on the field this is determined.
	 *
	 * @param card
	 *            Card to be set
	 * @return true if it is possible to set the card, false if not
	 */
	private boolean checkPossiblePlacement(Card card) {
		Set<Position> possiblePositions = new HashSet<>();
		Set<Position> positions = super.getGameField().getGameField().keySet();
		Set<Integer> xValues = new HashSet<>();
		Set<Integer> yValues = new HashSet<>();

		for (Position position : positions) {
			xValues.add(position.getX());
			yValues.add(position.getY());
		}
		for (int i = Collections.min(yValues); i < Collections.max(yValues) + 1; i++) {
			Set<Integer> rowXValues = new HashSet<>();
			int minX;
			int maxX;
			for (Position position : positions) {
				if (position.getY() == i) {
					rowXValues.add(position.getX());
				}
			}

			minX = Collections.min(rowXValues);
			maxX = Collections.max(rowXValues);

			for (int j = minX; j < maxX + 1; j++) {
				if (!super.getGameField().getGameField()
						.containsKey(new Position(j, i))
						&& super.getGameField().checkNeighbours(
								new Position(j, i))) {
					possiblePositions.add(new Position(j, i));
				}
			}

			possiblePositions.add(new Position(minX - 1, i));
			possiblePositions.add(new Position(maxX + 1, i));
		}

		for (int i = Collections.min(xValues); i < Collections.max(xValues) + 1; i++) {
			Set<Integer> columnYValues = new HashSet<>();
			int minY;
			int maxY;
			for (Position position : positions) {
				if (position.getX() == i) {
					columnYValues.add(position.getY());
				}
			}

			minY = Collections.min(columnYValues);
			maxY = Collections.max(columnYValues);

			possiblePositions.add(new Position(i, minY - 1));
			possiblePositions.add(new Position(i, maxY + 1));
		}

		for (Position position : possiblePositions) {
			for (int i = 1; i < 4; i++) {
				if (matchingEdges(card, position)) {
					card.rotate(0);
					return true;
				} else {
					card.rotate(i);
				}
			}
		}
		return false;
	}

	/**
	 * Calculates the score at the end of a game.
	 * <p/>
	 * Is called at the end of a game. Checks meadow singleAreas and not finished cloister, town and road singleAreas.
	 * Gives the calculated points to the the player.
	 *
	 * @return true if the method finished
	 */
	public boolean checkEndScore() {
		for (AbstractSingleArea singleArea : singleAreas) {
			// If a meeple was set
			if (singleArea != null && singleArea.getMeeples().size() != 0) {
				Set<Player> meepleOwners = new HashSet<>();
				Set<Player> bishopOwners = new HashSet<>();
				for (Meeple singleMeeple : singleArea.getMeeples()) {

					Player owner = singleMeeple.getOwner();

					// If a meeple is a special meeple (e.g. with the extensions
					// Bishop of Big meeple
					if (singleMeeple.getSpecialType() != null) {
						switch (singleMeeple.getSpecialType()) {
						case BISHOP:
							bishopOwners.add(owner);
							break;

						case BIGMEEPLE:
							meepleOwners.add(owner);
							meepleOwners.add(owner);
							break;

						default:
							try {
								throw new IllegalAccessException(
										"No case matched!");
							} catch (IllegalAccessException e) {
								warnLog.warn("No case matched");
								e.printStackTrace();
							}
						}
					} else {
						// gets the owner of a specific meeple
						meepleOwners.add(owner);
					}
				}

				// Who has the most meeple set on this area?
				Map<Integer, ArrayList<Player>> frequencyMap = new HashMap<>();
				for (Player player : getPlayerList().values()) {

					// gets the highest frequency of meepleObjects with
					// parameter of meeple = nick of owner out of the
					// meepleOwners-List

					int frequency = Collections.frequency(meepleOwners, player);
					if (frequencyMap.containsKey(frequency)) {
						frequencyMap.get(frequency).add(player);
					} else {
						frequencyMap.put(frequency, new ArrayList<Player>());
						frequencyMap.get(frequency).add(player);
					}
				}

				ArrayList<Player> areaOwners;
				if (Collections.max(frequencyMap.keySet()) != 0) {
					areaOwners = frequencyMap.get(Collections.max(frequencyMap
							.keySet()));
				} else {
					areaOwners = new ArrayList<>();
				}

				Map<Integer, ArrayList<Player>> bishopFrequencyMap = new HashMap<>();
				for (Player player : getPlayerList().values()) {

					// gets the highest frequency of meepleObjects with
					// parameter of meeple = nick of owner out of the
					// meepleOwners-List

					int frequency = Collections.frequency(bishopOwners, player);
					if (bishopFrequencyMap.containsKey(frequency)) {
						bishopFrequencyMap.get(frequency).add(player);
					} else {
						bishopFrequencyMap.put(frequency,
								new ArrayList<Player>());
						bishopFrequencyMap.get(frequency).add(player);
					}
				}

				ArrayList<Player> areaBishopOwners;
				if (Collections.max(bishopFrequencyMap.keySet()) != 0) {
					areaBishopOwners = bishopFrequencyMap.get(Collections
							.max(bishopFrequencyMap.keySet()));
				} else {
					areaBishopOwners = new ArrayList<>();
				}

				// checks for specific type of singleArea and if its finished
				// when finished = calculate score and delete the specific
				// singleArea out of the list of areas

				if (CardAreaType.ROAD.equals(singleArea.getCardAreaType())) {
					SingleAreaRoad road = (SingleAreaRoad) singleArea;
					for (Player owner : areaOwners) {
						owner.addToScore(road.calculatePoints());
					}
				} else if (CardAreaType.TOWN.equals(singleArea
						.getCardAreaType())) {
					SingleAreaTown town = (SingleAreaTown) singleArea;
					for (Player owner : areaOwners) {
						owner.addToScore(town.calculatePoints());
					}

					if (bishopOwners.size() != 0) {
						int cloisters = 0;
						for (AbstractSingleArea area : singleAreas) {
							if (area.getCardAreaType() == CardAreaType.MEADOW) {
								SingleAreaMeadow meadow = (SingleAreaMeadow) area;
								if (meadow.getTowns().contains(town)) {
									cloisters += meadow.getCloisters().size();
								}
							}

						}

						for (Player bishopOwner : areaBishopOwners) {
							bishopOwner.addToScore(4 * cloisters);
						}
					}
				} else if (CardAreaType.MEADOW.equals(singleArea
						.getCardAreaType())) {
					SingleAreaMeadow meadow = (SingleAreaMeadow) singleArea;
					for (Player owner : areaOwners) {
						Set<SingleAreaTown> townsNoDuplicates = new HashSet<>(
								meadow.getTowns());
						int finishedTowns = 0;
						for (SingleAreaTown town : townsNoDuplicates) {
							if (town.isFinished()) {
								finishedTowns++;
							}
						}
						owner.addToScore(finishedTowns * 3);
					}
				} else {
					SingleAreaCloister cloister = (SingleAreaCloister) singleArea;
					for (Player owner : areaOwners) {
						owner.addToScore(getGameField().countSurroundingCards(
								cloister.getPosition()) + 1);
					}
				}
			}
		}
		return true;
	}

	/**
	 * Checks if a cloister is surrounded by 8 cards.
	 * <p/>
	 * If it is surrounded finished is set true. Checks for each SingleArea in the list if it's finished or not.
	 *
	 * @return if singleArea is finished or not
	 */
	private boolean checkCompletion() {
		for (AbstractSingleArea abstractArea : singleAreas) {
			if (abstractArea != null && abstractArea.getMeeples().size() != 0) {
				if (CardAreaType.CLOISTER
						.equals(abstractArea.getCardAreaType())) {
					SingleAreaCloister cloisterArea = (SingleAreaCloister) abstractArea;
					if (getGameField().checkSurroundingCards(
							cloisterArea.getPosition())) {
						cloisterArea.setFinished();
						return true;
					}
				}
				if (abstractArea.isFinished()) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Checks the score of the different singleAreas by looking through each of them.
	 * <p/>
	 * Get the meepleList on the singleArea and iterate through the meepleList and get the user of each meeple. If
	 * scoring is finished the user gets his meeples back and the singleArea is deleted on the list. User with the most
	 * meeple on the area gets the whole points. If more Players have the same count of meeple on an area all of them
	 * get full points.
	 */
	private void checkScore() {

		for (AbstractSingleArea singleArea : singleAreas) {

			if (singleArea != null
					&& !CardAreaType.MEADOW
							.equals(singleArea.getCardAreaType())
					&& singleArea.getMeeples().size() != 0
					&& singleArea.isFinished()) {

				boolean notOnlyBishops = false;

				if (CardAreaType.TOWN.equals(singleArea.getCardAreaType())) {
					for (Meeple meeple : singleArea.getMeeples()) {
						if (meeple.getSpecialType() == null
								|| meeple.getSpecialType() == SpecialMeepleType.BIGMEEPLE) {
							notOnlyBishops = true;
							break;
						}
					}
				} else {
					notOnlyBishops = true;
				}

				if (notOnlyBishops) {
					List<Player> meepleOwners = new ArrayList<>();
					for (Meeple singleMeeple : singleArea.getMeeples()) {

						if (singleMeeple.getSpecialType() != SpecialMeepleType.BISHOP) {
							if (singleMeeple.getSpecialType() == SpecialMeepleType.BIGMEEPLE) {
								meepleOwners.add(singleMeeple.getOwner());
							}
							meepleOwners.add(singleMeeple.getOwner());
						}

					}
					Map<Integer, ArrayList<Player>> frequencyMap = new HashMap<>();
					for (Player player : getPlayerList().values()) {

						// gets the highest frequency of meepleObjects with
						// parameter of
						// meeple = nick of owner
						// out of the meepleOwners-List

						int frequency = Collections.frequency(meepleOwners,
								player);
						if (frequencyMap.containsKey(frequency)) {
							frequencyMap.get(frequency).add(player);
						} else {
							frequencyMap
									.put(frequency, new ArrayList<Player>());
							frequencyMap.get(frequency).add(player);
						}
					}

					ArrayList<Player> areaOwners;
					if (Collections.max(frequencyMap.keySet()) != 0) {
						areaOwners = frequencyMap.get(Collections
								.max(frequencyMap.keySet()));
						scoreHelper(singleArea, areaOwners);
					}
				}
			}
		}
	}

	/**
	 * Sends information about the next move to make('tile drawn') message to the server.
	 * <p/>
	 * it checks first if the deck is empty or if there is a possible move with the card that is about to be sent.
	 *
	 * @param nick
	 *            player who's turn it is
	 * @param timer 
	 */
	public synchronized void tileDrawn(String nick, int timer) {
		CardDeck deck = super.getCardDeck();
		
		if (!deck.getCardDeck().isEmpty()
				&& checkPossiblePlacement(deck.getColonelCard(false))) {
			server.tileDrawn(getGameID(), nick, deck.getColonelCard(false),
					deck.getRemainingSize(), timer);
		} else {
			while (!checkPossiblePlacement(deck.getColonelCard(false))) {
				Collections.shuffle(deck.getCardDeck());
			}
			server.tileDrawn(getGameID(), nick, deck.getColonelCard(false),
					deck.getRemainingSize(), timer);
		}
	}

	/**
	 * Creates a JSONObject with all information about the game controlled by this instance of ServerGame.
	 * <p/>
	 * This method will be used to gather information about the game that are needed to send a 'game-update' message.
	 *
	 * @return information about the game
	 */
	public JSONObject getGameInformation() {

		// Adding the Players to the JSONARRAY
		JSONArray userInfo = new JSONArray();
		for (Player k : getPlayerList().values()) {
			userInfo.put(k.getGameInformation());
		}

		// Adding the spectators to the JSONARRAY
		JSONArray specInfo = new JSONArray();
		for (Spectator k : getSpectatorList().values()) {
			specInfo.put(k.getNick());
		}

		JSONArray extArr = new JSONArray();
		for (CapabilitiesType t : getExtensions()) {
			extArr.put(t.toString());
		}

		if (getState().equals(GameStatus.NOTSTARTED)
				|| getState().equals(GameStatus.ONGOING)
				|| getState().equals(GameStatus.ENDED)) {
			return ServerMessageBuilder
					.createGameInfos(super.getGameID(), super.getGameName(),
							getState(), specInfo, userInfo, extArr);
		} else {
			log.error("State must not be null!");
			throw new IllegalAccessError("state must not be null!");
		}
	}

	/**
	 * Gets the startCard by invoking the getter in the cardDeck-class
	 *
	 * @return startCard Card which is the first Card drawn
	 */
	public Card getStartCard() {
		Card startCard = super.getCardDeck().getStartCard();
		return startCard;
	}

	/**
	 * Gets the currentCard out of the card deck. Flag if card should be removed from stack or not.
	 *
	 * @param remove
	 *            boolean which indicates if a card should be removed from the cardDeck or not.(true if the card should
	 *            be removed, false if not)
	 * @return returns the card.
	 */
	public Card getCurrentCard(boolean remove) {

		return super.getCardDeck().getColonelCard(remove);
	}

	/**
	 * Utility method for calculating the points of the different singleAreas.
	 *
	 * @param singleArea
	 *            SingleArea where the score should be calculated
	 * @param areaOwners
	 *            Player who own the area
	 */
	private void scoreHelper(AbstractSingleArea singleArea,
			ArrayList<Player> areaOwners) {

		// Iterates through the list of areaOwners
		for (Player owner : areaOwners) {

			// gets a specific owner and adds the calculated score for the area
			// to his score
			owner.addToScore(singleArea.calculatePoints());
		}

		// Iterates through the list of meeples on specific singleArea
		// gets meeple back to the user and deletes meeple out of the set
		List<Meeple> meepleList = singleArea.getMeeples();
		for (Meeple meeple : meepleList) {
			Player meepleOwner = meeple.getOwner();
			meepleOwner.getMeeplesSet().remove(meeple);
			if (meeple.getSpecialType() != null
					&& meeple.getSpecialType() == SpecialMeepleType.BIGMEEPLE) {
				meepleOwner.setBigMeepleUsed(false);
			} else {
				meepleOwner.addToMeeplesLeft(1);
			}
		}
		singleArea.getMeeples().clear();
	}


	@Override
	public String toString() {
		return super.toString();
	}

	/*
	 * Getters and Setter below
	 */
	public List<AbstractSingleArea> getSingleAreas() {
		return singleAreas;
	}

	public User getHost() {
		return host;
	}

	public PlayerThread getThread() {
		return thread;
	}

	public ServerControl getServer() {
		return server;
	}

	public Set<CapabilitiesType> getExtension() {
		return super.getExtensions();
	}

}
