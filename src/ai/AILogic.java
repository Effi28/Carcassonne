package ai;

import shared.enums.CardAreaType;
import shared.model.*;

import java.util.*;

public class AILogic {

	private AIGame aiGame;

	AI ai;

	public AILogic(AI ai) {
		this.ai = ai;
		aiGame = new AIGame(this);
	}

	/**
	 * Pseudocode: 1: check for legal placements (should be not more than 20
	 * possibilities in each game situation) 2: check for legal meeple
	 * placement(possibilities 1-3) not placing a meeple also one of the
	 * possibilities 3: for each legal placement and for each legal meeple
	 * placement check the score of each player including the AI 4: calculate
	 * the delta of each Player and score after the placement and before. Best
	 * delta = best move
	 */

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
		Set<Position> emptyPositions = aiGame.getGameField().getPositionsFromSetCards();
		for (Position p : emptyPositions) {
			for (int i = 0; i < 4; i++) {
				if (aiGame.matchingEdges(card, p)) {
					availablePositions.add(new PossiblePlacement(i, p));
				}
				ArrayList<Integer> tmp = card.getEdges().get(0);
				card.getEdges().remove(0);
				card.getEdges().add(tmp);
			}
		}
		return availablePositions;
	}

	/**
	 * method checks the different possibilities of meeple placement by
	 * iterating through the possible CardPlacements and checking the
	 * singleAreas of the surroundingCard, wheather they already contain a
	 * meeple or not
	 * 
	 * @param card
	 *            the current card
	 * @param placements
	 *            a list of all possible placements for the current card
	 * @return a set of integers that represents the indices in the areasArray
	 *         of the card
	 */
	public Set<PossiblePlacement> checkForMeeplePlacement(Card card,
			Set<PossiblePlacement> placements) {
		Set<PossiblePlacement> possiblePlacements = new HashSet<>();
		for (PossiblePlacement placement : placements) {
			card.rotate(placement.getRotation());
			if (!(aiGame.getGameField().getCardNorth(placement.getPos()) == null)) {
				Card northCard = aiGame.getGameField().getCardNorth(placement.getPos());
				for (int i = 0; i < northCard.getEdges().get(2).size(); i++) {
					if (northCard.getSingleAreaMap()
							.get(northCard.getEdges().get(2).get(i))
							.getMeeples().isEmpty()) {
						List<Integer> currentIndices = card.getEdges().get(0);
						if (currentIndices.size() == 1) {
							int tmp = currentIndices.get(0);
							currentIndices.add(tmp);
							currentIndices.add(tmp);
						}
						Collections.reverse(currentIndices);
						possiblePlacements.add(new PossiblePlacement(placement
								.getRotation(), placement.getPos(),
								currentIndices.get(i)));
					}
				}
			} else {
				for (int index : card.getEdges().get(0)) {
					possiblePlacements.add(new PossiblePlacement(placement
							.getRotation(), placement.getPos(), index));
				}
			}

			if (!(aiGame.getGameField().getCardEast(placement.getPos()) == null)) {
				Card eastCard = aiGame.getGameField().getCardEast(placement.getPos());
				for (int i = 0; i < eastCard.getEdges().get(1).size(); i++) {
					List<Integer> currentIndices = card.getEdges().get(3);
					if (currentIndices.size() == 1) {
						int tmp = currentIndices.get(0);
						currentIndices.add(tmp);
						currentIndices.add(tmp);
					}
					Collections.reverse(currentIndices);
					if (eastCard.getSingleAreaMap()
							.get(eastCard.getEdges().get(1).get(i))
							.getMeeples().isEmpty()) {
						possiblePlacements.add(new PossiblePlacement(placement
								.getRotation(), placement.getPos(),
								currentIndices.get(i)));
					} else {
						PossiblePlacement posPlacement = new PossiblePlacement(
								placement.getRotation(), placement.getPos(),
								currentIndices.get(i));
						if (possiblePlacements.contains(posPlacement)) {
							possiblePlacements.remove(posPlacement);
						}
					}
				}
			} else {
				for (int index : card.getEdges().get(3)) {
					possiblePlacements.add(new PossiblePlacement(placement
							.getRotation(), placement.getPos(), index));
				}
			}

			if (!(aiGame.getGameField().getCardSouth(placement.getPos()) == null)) {
				Card southCard = aiGame.getGameField().getCardSouth(placement.getPos());
				List<Integer> currentIndices = card.getEdges().get(2);
				if (currentIndices.size() == 1) {
					int tmp = currentIndices.get(0);
					currentIndices.add(tmp);
					currentIndices.add(tmp);
				}
				Collections.reverse(currentIndices);
				for (int i = 0; i < southCard.getEdges().get(0).size(); i++) {
					if (southCard.getSingleAreaMap()
							.get(southCard.getEdges().get(0).get(i))
							.getMeeples().isEmpty()) {
						possiblePlacements.add(new PossiblePlacement(placement
								.getRotation(), placement.getPos(),
								currentIndices.get(i)));
					} else {
						PossiblePlacement posPlacement = new PossiblePlacement(
								placement.getRotation(), placement.getPos(),
								currentIndices.get(i));
						if (possiblePlacements.contains(posPlacement)) {
							possiblePlacements.remove(posPlacement);
						}
					}
				}
			} else {
				for (int index : card.getEdges().get(2)) {
					possiblePlacements.add(new PossiblePlacement(placement
							.getRotation(), placement.getPos(), index));
				}
			}

			if (!(aiGame.getGameField().getCardWest(placement.getPos()) == null)) {
				Card westCard = aiGame.getGameField().getCardWest(placement.getPos());
				List<Integer> currentIndices = card.getEdges().get(1);
				if (currentIndices.size() == 1) {
					int tmp = currentIndices.get(0);
					currentIndices.add(tmp);
					currentIndices.add(tmp);
				}
				Collections.reverse(currentIndices);
				for (int i = 0; i < westCard.getEdges().get(3).size(); i++) {
					if (westCard.getSingleAreaMap()
							.get(westCard.getEdges().get(3).get(i))
							.getMeeples().isEmpty()) {
						possiblePlacements.add(new PossiblePlacement(placement
								.getRotation(), placement.getPos(),
								currentIndices.get(i)));
					} else {
						PossiblePlacement posPlacement = new PossiblePlacement(
								placement.getRotation(), placement.getPos(),
								currentIndices.get(i));
						if (possiblePlacements.contains(posPlacement)) {
							possiblePlacements.remove(posPlacement);
						}
					}
				}
			} else {
				for (int index : card.getEdges().get(1)) {
					possiblePlacements.add(new PossiblePlacement(placement
							.getRotation(), placement.getPos(), index));
				}
			}
			
			if(card.getAreas().contains(CardAreaType.CLOISTER)){
				int index = 0;
				for(int i = 0; i < card.getAreas().size(); i++){
					if(card.getAreas().get(i).equals(CardAreaType.CLOISTER)){
						index = i;
					}
				}
				possiblePlacements.add(new PossiblePlacement(placement.getRotation(), placement.getPos(), index));
			}
		}

		return possiblePlacements;
	}

	/**
	 * method gets different placement possibilities and choose the best by
	 * calculating the best score
	 * 
	 * @param card
	 *            current Card
	 * @param placements
	 *            set of all placement infos which are possible included also
	 *            the meeple info
	 * 
	 * @return best Placement on gameField for currentCard
	 */
	public PossiblePlacement getBestPlacement(Card card,
			Set<PossiblePlacement> placements) {
		Set<PossiblePlacement> badPlacements = new HashSet<>();
		PossiblePlacement bestPlacement = null;
		for (PossiblePlacement placement : placements) {
			canAreaBeFinished(placement, card, badPlacements);
		}

		for (PossiblePlacement placement : placements) {
			if (bestPlacement != null
					&& placement.getScore() > bestPlacement.getScore()) {
				bestPlacement = placement;
			} else if (bestPlacement == null) {
				bestPlacement = placement;
			}
		}
		if (bestPlacement.getScore() == 0){
			for(PossiblePlacement placement : placements) {
				checkForGoodPlacements(placement, card);
			}
			for (PossiblePlacement placement : placements) {
				if (bestPlacement != null
						&& placement.getPriority() > bestPlacement.getPriority()) {
					bestPlacement = placement;
				} else if (bestPlacement == null) {
					bestPlacement = placement;
				}
			}
		}
		if(bestPlacement.getPriority() == 0 && bestPlacement.getScore() == 0){
			if(card.getAreas().contains(CardAreaType.CLOISTER)){
				for(PossiblePlacement placement : placements){
					if(card.getAreas().get(placement.getMeeplePlacements()).equals(CardAreaType.CLOISTER)
							&& !badPlacements.contains(placement)){
						return placement;
					}
				}
			}
			if(card.getAreas().contains(CardAreaType.TOWN)){
				for(PossiblePlacement placement : placements){
					if(card.getAreas().get(placement.getMeeplePlacements()).equals(CardAreaType.TOWN)
							&& !badPlacements.contains(placement)){
						return placement;
					}
				}
			}
			if(card.getAreas().contains(CardAreaType.ROAD)){
				for(PossiblePlacement placement : placements){
					if(card.getAreas().get(placement.getMeeplePlacements()).equals(CardAreaType.ROAD)
							&& !badPlacements.contains(placement)){
							if(ai.getPlayer().getMeeplesLeft() > 4){
								return placement;
							}
							else if(ai.getRemainingCards() <= 4){
								return placement;
							}
					}
				}
			}
		}
		return bestPlacement;

	}
	
	public PossiblePlacement getBestPlacementEasy(Card card,
			Set<PossiblePlacement> placements) {
		PossiblePlacement bestPlacement = null;
			for(PossiblePlacement placement : placements) {
				checkForGoodPlacements(placement, card);
			}
			for (PossiblePlacement placement : placements) {
				if (bestPlacement != null
						&& placement.getPriority() > bestPlacement.getPriority()) {
					bestPlacement = placement;
				} else if (bestPlacement == null) {
					bestPlacement = placement;
				}
			}
		if(bestPlacement.getPriority() == 0){
			if(card.getAreas().contains(CardAreaType.CLOISTER)){
				for(PossiblePlacement placement : placements){
					if(card.getAreas().get(placement.getMeeplePlacements()).equals(CardAreaType.CLOISTER)){
						return placement;
					}
				}
			}
			if(card.getAreas().contains(CardAreaType.TOWN)){
				for(PossiblePlacement placement : placements){
					if(card.getAreas().get(placement.getMeeplePlacements()).equals(CardAreaType.TOWN)){
						return placement;
					}
				}
			}
			if(card.getAreas().contains(CardAreaType.ROAD)){
				for(PossiblePlacement placement : placements){
					if(card.getAreas().get(placement.getMeeplePlacements()).equals(CardAreaType.ROAD)){
							if(ai.getPlayer().getMeeplesLeft() > 4){
								return placement;
							}
							else if(ai.getRemainingCards() <= 4){
								return placement;
							}
					}
				}
			}
		}
		return bestPlacement;

	}

	private void canAreaBeFinished(PossiblePlacement placement, Card card, Set<PossiblePlacement> badPlacements) {
		GameField oldGameField = aiGame.getGameField().deepCopy();
		int oldSize = aiGame.getGameField().getGameField().entrySet().size();
		List<AbstractSingleArea> oldSingleAreas = new ArrayList<>();
		for (int i = 0; i < aiGame.getSingleAreas().size(); i++) {
			AbstractSingleArea tmp = aiGame.getSingleAreas().get(i).clone();
			oldSingleAreas.add(i, tmp);
			for(Card tmpCard : oldGameField.getGameField().values()){
				for(AbstractSingleArea tmpArea : tmpCard.getSingleAreaMap().values()){
					if(aiGame.getSingleAreas().get(i) == tmpArea){
						tmpCard.setSingleAreaEntry(aiGame.getSingleAreas().get(i), tmp);
					}
				}
			}
		}
		Card tmpCard = card.deepCopy();

		tmpCard.rotate(placement.getRotation());
		aiGame.placeCard(placement.getPos(), tmpCard);
		List<AbstractSingleArea> finishedAreas = aiGame
				.checkCompletionAlternative();
		AbstractSingleArea meepleArea = tmpCard.getSingleAreaMap().get(
				placement.getMeeplePlacements());

		if (!finishedAreas.isEmpty()) {
			for (AbstractSingleArea area : finishedAreas) {
				if (CardAreaType.TOWN.equals(area.getCardAreaType())
						&& getAreaOwner(area).contains(ai.getPlayer().getNick())) {
					if (area.getMeeples().size() != 0) {
						int score = card.getBonus().size() == 0 ? area
								.calculatePoints() + 2
								: area.calculatePoints() + 4;
						if (score > placement.getScore()) {
							placement.setScore(score);
							placement.setPlacement(-1);
						}
					}
					if (area.getMeeples().size() == 0 && meepleArea.equals(area)) {
						int score = card.getBonus().size() == 0 ? area
								.calculatePoints() + 2
								: area.calculatePoints() + 4;
						if (score > placement.getScore()) {
							placement.setScore(score);
						}
					}
				}
				if (CardAreaType.ROAD.equals(area.getCardAreaType())
						&& getAreaOwner(area).contains(ai.getPlayer().getNick())) {
					if (area.getMeeples().size() != 0) {
						int score = area.calculatePoints() + 1;
						if (score > placement.getScore()) {
							placement.setScore(score);
							placement.setPlacement(-1);
						}
					}
					if (area.getMeeples().size() == 0 && meepleArea.equals(area)) {
						int score = area.calculatePoints() + 1;
						placement.getMeeplePlacements();
						if (score > placement.getScore()) {
							placement.setScore(score);
						}
					}
				}
				if (CardAreaType.CLOISTER.equals(area.getCardAreaType())
						&& getAreaOwner(area).contains(ai.getPlayer().getNick())) {
					if (area.getMeeples().size() != 0) {
						int score = 8;
						if (score > placement.getScore()) {
							placement.setScore(score);
							placement.setPlacement(-1);
						}
					}
				}
				if(!getAreaOwner(area).contains(ai.getPlayer().getNick())){
					badPlacements.add(placement);
				}
			}
		}
		if(oldSize != oldGameField.getGameField().entrySet().size()){
			System.out.println("GameField hat sich verändert!");
		}
		aiGame.setGameField(oldGameField);
		aiGame.setSingleAreas(oldSingleAreas);
	}
	
	private void checkForGoodPlacements(PossiblePlacement placement, Card card){
		GameField oldGameField = aiGame.getGameField().deepCopy();
		int oldGameFieldSize = aiGame.getGameField().getGameField().entrySet().size();
		List<AbstractSingleArea> oldSingleAreas = new ArrayList<>();
		for (int i = 0; i < aiGame.getSingleAreas().size(); i++) {
			AbstractSingleArea tmp = aiGame.getSingleAreas().get(i).clone();
			oldSingleAreas.add(i, tmp);
			for(Card tmpCard : oldGameField.getGameField().values()){
				for(AbstractSingleArea tmpArea : tmpCard.getSingleAreaMap().values()){
					if(aiGame.getSingleAreas().get(i) == tmpArea){
						tmpCard.setSingleAreaEntry(aiGame.getSingleAreas().get(i), tmp);
					}
				}
			}
		}
		Card tmpCard = card.deepCopy();
		tmpCard.rotate(placement.getRotation());
		
		Map<SingleAreaTown, Integer> townPositions = new HashMap<>();
		Map<SingleAreaRoad, Integer> roadPositions = new HashMap<>();
		for(AbstractSingleArea area : aiGame.getSingleAreas()){
			if(CardAreaType.CLOISTER.equals(area.getCardAreaType()) 
					&& getAreaOwner(area).contains(ai.getPlayer().getNick()) && getAreaOwner(area).size() == 1) {
				SingleAreaCloister cloister = (SingleAreaCloister) area;
				Position pos = placement.getPos();
				if(cloister.getPosition().equals(new Position(pos.getX(), pos.getY()-1))
						|| cloister.getPosition().equals(new Position(pos.getX()-1, pos.getY()-1))
						|| cloister.getPosition().equals(new Position(pos.getX()-1, pos.getY()))
						|| cloister.getPosition().equals(new Position(pos.getX()-1, pos.getY()+1))
						|| cloister.getPosition().equals(new Position(pos.getX(), pos.getY()+1))
						|| cloister.getPosition().equals(new Position(pos.getX()+1, pos.getY()+1))
						|| cloister.getPosition().equals(new Position(pos.getX()+1, pos.getY()))
						|| cloister.getPosition().equals(new Position(pos.getX()+1, pos.getY()-1))){
					placement.setPriority(10);
					if(!(cloister == card.getSingleAreaMap().get(placement.getMeeplePlacements()))){
						placement.setPlacement(-1);
					}
				}
			}
			if(CardAreaType.TOWN.equals(area.getCardAreaType())){
				SingleAreaTown town = (SingleAreaTown) area;
				int tmp = town.getTownPosition().size();
				townPositions.put(town, tmp);
			}
			if(CardAreaType.ROAD.equals(area.getCardAreaType())){
				SingleAreaRoad road = (SingleAreaRoad) area;
				int tmp = road.getRoadPosition().size();
				roadPositions.put(road, tmp);
			}
		}
		aiGame.placeCard(placement.getPos(), tmpCard);
		for(SingleAreaTown town : townPositions.keySet()){
			if(getAreaOwner(town).contains(ai.getPlayer().getNick()) 
					&& town.getTownPosition().size() > townPositions.get(town)){
				if(town.getMeeples().size() != 0){
					if(placement.getPriority() < 9){
						placement.setPriority(9);
						placement.setPlacement(-1);
					}
				}
				else if(town.getMeeples().size() == 0 &&
						town == card.getSingleAreaMap().get(placement.getMeeplePlacements())){
					if(placement.getPriority() < 9){
						placement.setPriority(9);
					}
				}
			}
		}
		for(SingleAreaRoad road : roadPositions.keySet()){
			if(getAreaOwner(road).contains(ai.getPlayer().getNick()) 
					&& road.getRoadPosition().size() > roadPositions.get(road)){
				if(road.getMeeples().size() != 0){
					if(placement.getPriority() < 8){
						placement.setPriority(8);
						placement.setPlacement(-1);
					}
				}
				else if(road.getMeeples().size() == 0 
						&& road == card.getSingleAreaMap().get(placement.getMeeplePlacements())){
					if(placement.getPriority() < 8){
						placement.setPriority(8);
					}
				}
			}
		}
		if(oldGameFieldSize != oldGameField.getGameField().entrySet().size()){
			System.out.println("Größe des GameFields hat sich verändert!");
		}
		aiGame.setGameField(oldGameField);
		aiGame.setSingleAreas(oldSingleAreas);
	}

	/**
	 * 
	 * This method tests if the AI is the Player with the most meeple in the
	 * SingleArea.
	 * 
	 * @param singleArea
	 *            singleArea to check
	 * @return true if he owns the most meeple, false if not
	 */
	private ArrayList<String> getAreaOwner(AbstractSingleArea singleArea) {
		List<String> meepleOwners = new ArrayList<>();
		for (Meeple singleMeeple : singleArea.getMeeples()) {

			// gets the owner of a specific meeple
			meepleOwners.add(singleMeeple.getOwner().getNick());
				}
		Map<Integer, ArrayList<String>> frequencyMap = new HashMap<>();
		for (Player player : getPlayerList().values()) {

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
		return areaOwners;
	}

	public Map<String, Player> getPlayerList() {
		return ai.getPlayerList();
	}

	public AIGame getAIGame() {
		return aiGame;
	}
}
