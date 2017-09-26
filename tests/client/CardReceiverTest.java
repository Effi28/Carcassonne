package client;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import client.model.game.CardReceiver;
import shared.enums.CardAreaType;

public class CardReceiverTest {

	/**
	 * provides constructor for cards with one area, placeOfBonus have to be -1
	 * if there is no bonus
	 * 
	 * @param area1
	 * @param edge1
	 * @param edge2
	 * @param edge3
	 * @param edge4
	 * @param adja1
	 * @param placeOfBonus
	 * @return
	 */
	public JSONObject getJsonCard(CardAreaType area1, ArrayList<Integer> edge1,
			ArrayList<Integer> edge2, ArrayList<Integer> edge3,
			ArrayList<Integer> edge4, ArrayList<Integer> adja1, int placeOfBonus) {

		ArrayList<String> areas = new ArrayList<>();
		areas.add(area1.toString());

		ArrayList<ArrayList<Integer>> edges = new ArrayList<>();
		edges.add(edge1);
		edges.add(edge2);
		edges.add(edge3);
		edges.add(edge4);

		ArrayList<ArrayList<Integer>> adjacency = new ArrayList<>();
		adjacency.add(adja1);

		if (placeOfBonus == -1) {
			return fillCard(areas, edges, adjacency);
		} else {
			ArrayList<Integer> bonus = new ArrayList<>();
			bonus.add(placeOfBonus);
			return fillCard(areas, edges, adjacency, bonus);
		}

	}

	/**
	 * provides constructor for cards with two areas, placeOfBonus have to be -1
	 * if there is no bonus
	 * 
	 * @param area1
	 * @param area2
	 * @param edge1
	 * @param edge2
	 * @param edge3
	 * @param edge4
	 * @param adja1
	 * @param adja2
	 * @param placeOfBonus
	 * @return
	 */
	public JSONObject getJsonCard(CardAreaType area1, CardAreaType area2,
			ArrayList<Integer> edge1, ArrayList<Integer> edge2,
			ArrayList<Integer> edge3, ArrayList<Integer> edge4,
			ArrayList<Integer> adja1, ArrayList<Integer> adja2, int placeOfBonus) {

		ArrayList<String> areas = new ArrayList<>();
		areas.add(area1.toString());
		areas.add(area2.toString());

		ArrayList<ArrayList<Integer>> edges = new ArrayList<>();
		edges.add(edge1);
		edges.add(edge2);
		edges.add(edge3);
		edges.add(edge4);

		ArrayList<ArrayList<Integer>> adjacency = new ArrayList<>();
		adjacency.add(adja1);
		adjacency.add(adja2);

		if (placeOfBonus == -1) {
			return fillCard(areas, edges, adjacency);
		} else {
			ArrayList<Integer> bonus = new ArrayList<>();
			bonus.add(placeOfBonus);
			return fillCard(areas, edges, adjacency, bonus);
		}
	}

	/**
	 * provides constructor for cards with three areas, placeOfBonus have to be
	 * -1 if there is no bonus
	 * 
	 * @param area1
	 * @param area2
	 * @param area3
	 * @param edge1
	 * @param edge2
	 * @param edge3
	 * @param edge4
	 * @param adja1
	 * @param adja2
	 * @param adja3
	 * @param placeOfBonus
	 * @return
	 */
	public JSONObject getJsonCard(CardAreaType area1, CardAreaType area2,
			CardAreaType area3, ArrayList<Integer> edge1,
			ArrayList<Integer> edge2, ArrayList<Integer> edge3,
			ArrayList<Integer> edge4, ArrayList<Integer> adja1,
			ArrayList<Integer> adja2, ArrayList<Integer> adja3, int placeOfBonus) {

		ArrayList<String> areas = new ArrayList<>();
		areas.add(area1.toString());
		areas.add(area2.toString());
		areas.add(area3.toString());

		ArrayList<ArrayList<Integer>> edges = new ArrayList<>();
		edges.add(edge1);
		edges.add(edge2);
		edges.add(edge3);
		edges.add(edge4);

		ArrayList<ArrayList<Integer>> adjacency = new ArrayList<>();
		adjacency.add(adja1);
		adjacency.add(adja2);
		adjacency.add(adja3);

		if (placeOfBonus == -1) {
			return fillCard(areas, edges, adjacency);
		} else {
			ArrayList<Integer> bonus = new ArrayList<>();
			bonus.add(placeOfBonus);
			return fillCard(areas, edges, adjacency, bonus);
		}
	}

	/**
	 * provides constructor for cards with four areas, placeOfBonus have to be
	 * -1 if there is no bonus
	 * 
	 * @param area1
	 * @param area2
	 * @param area3
	 * @param area4
	 * @param edge1
	 * @param edge2
	 * @param edge3
	 * @param edge4
	 * @param adja1
	 * @param adja2
	 * @param adja3
	 * @param adja4
	 * @param placeOfBonus
	 * @return
	 */
	public JSONObject getJsonCard(CardAreaType area1, CardAreaType area2,
			CardAreaType area3, CardAreaType area4, ArrayList<Integer> edge1,
			ArrayList<Integer> edge2, ArrayList<Integer> edge3,
			ArrayList<Integer> edge4, ArrayList<Integer> adja1,
			ArrayList<Integer> adja2, ArrayList<Integer> adja3,
			ArrayList<Integer> adja4, int placeOfBonus) {

		ArrayList<String> areas = new ArrayList<>();
		areas.add(area1.toString());
		areas.add(area2.toString());
		areas.add(area3.toString());
		areas.add(area4.toString());

		ArrayList<ArrayList<Integer>> edges = new ArrayList<>();
		edges.add(edge1);
		edges.add(edge2);
		edges.add(edge3);
		edges.add(edge4);

		ArrayList<ArrayList<Integer>> adjacency = new ArrayList<>();
		adjacency.add(adja1);
		adjacency.add(adja2);
		adjacency.add(adja3);
		adjacency.add(adja4);

		if (placeOfBonus == -1) {
			return fillCard(areas, edges, adjacency);
		} else {
			ArrayList<Integer> bonus = new ArrayList<>();
			bonus.add(placeOfBonus);
			return fillCard(areas, edges, adjacency, bonus);
		}
	}

	/**
	 * provides constructor for cards which six area, placeOfBonus have to be -1
	 * if there is no bonus
	 * 
	 * @param area1
	 * @param area2
	 * @param area3
	 * @param area4
	 * @param area5
	 * @param area6
	 * @param edge1
	 * @param edge2
	 * @param edge3
	 * @param edge4
	 * @param adja1
	 * @param adja2
	 * @param adja3
	 * @param adja4
	 * @param adja5
	 * @param adja6
	 * @param placeOfBonus
	 * @return
	 */
	public JSONObject getJsonCard(CardAreaType area1, CardAreaType area2,
			CardAreaType area3, CardAreaType area4, CardAreaType area5,
			CardAreaType area6, ArrayList<Integer> edge1,
			ArrayList<Integer> edge2, ArrayList<Integer> edge3,
			ArrayList<Integer> edge4, ArrayList<Integer> adja1,
			ArrayList<Integer> adja2, ArrayList<Integer> adja3,
			ArrayList<Integer> adja4, ArrayList<Integer> adja5,
			ArrayList<Integer> adja6,

			int placeOfBonus) {

		ArrayList<String> areas = new ArrayList<>();
		areas.add(area1.toString());
		areas.add(area2.toString());
		areas.add(area3.toString());
		areas.add(area4.toString());
		areas.add(area5.toString());
		areas.add(area6.toString());

		ArrayList<ArrayList<Integer>> edges = new ArrayList<>();
		edges.add(edge1);
		edges.add(edge2);
		edges.add(edge3);
		edges.add(edge4);

		ArrayList<ArrayList<Integer>> adjacency = new ArrayList<>();
		adjacency.add(adja1);
		adjacency.add(adja2);
		adjacency.add(adja3);
		adjacency.add(adja4);
		adjacency.add(adja5);
		adjacency.add(adja6);

		if (placeOfBonus == -1) {
			return fillCard(areas, edges, adjacency);
		} else {
			ArrayList<Integer> bonus = new ArrayList<>();
			bonus.add(placeOfBonus);
			return fillCard(areas, edges, adjacency, bonus);
		}
	}

	/**
	 * provides constructor for cards which seven area, placeOfBonus have to be
	 * -1 if there is no bonus
	 * 
	 * @param area1
	 * @param area2
	 * @param area3
	 * @param area4
	 * @param area5
	 * @param area6
	 * @param area7
	 * @param edge1
	 * @param edge2
	 * @param edge3
	 * @param edge4
	 * @param adja1
	 * @param adja2
	 * @param adja3
	 * @param adja4
	 * @param adja5
	 * @param adja6
	 * @param adja7
	 * @param placeOfBonus
	 * @return
	 */
	public JSONObject getJsonCard(CardAreaType area1, CardAreaType area2,
			CardAreaType area3, CardAreaType area4, CardAreaType area5,
			CardAreaType area6, CardAreaType area7, ArrayList<Integer> edge1,
			ArrayList<Integer> edge2, ArrayList<Integer> edge3,
			ArrayList<Integer> edge4, ArrayList<Integer> adja1,
			ArrayList<Integer> adja2, ArrayList<Integer> adja3,
			ArrayList<Integer> adja4, ArrayList<Integer> adja5,
			ArrayList<Integer> adja6, ArrayList<Integer> adja7, int placeOfBonus) {

		ArrayList<String> areas = new ArrayList<>();
		areas.add(area1.toString());
		areas.add(area2.toString());
		areas.add(area3.toString());
		areas.add(area4.toString());
		areas.add(area5.toString());
		areas.add(area6.toString());
		areas.add(area7.toString());

		ArrayList<ArrayList<Integer>> edges = new ArrayList<>();
		edges.add(edge1);
		edges.add(edge2);
		edges.add(edge3);
		edges.add(edge4);

		ArrayList<ArrayList<Integer>> adjacency = new ArrayList<>();
		adjacency.add(adja1);
		adjacency.add(adja2);
		adjacency.add(adja3);
		adjacency.add(adja4);
		adjacency.add(adja5);
		adjacency.add(adja6);
		adjacency.add(adja7);

		if (placeOfBonus == -1) {
			return fillCard(areas, edges, adjacency);
		} else {
			ArrayList<Integer> bonus = new ArrayList<>();
			bonus.add(placeOfBonus);
			return fillCard(areas, edges, adjacency, bonus);
		}
	}

	/**
	 * provides constructor for cards with eight areas, placeOfBonus have to be
	 * -1 if there is no bonus
	 * 
	 * @param area1
	 * @param area2
	 * @param area3
	 * @param area4
	 * @param area5
	 * @param area6
	 * @param area7
	 * @param area8
	 * @param edge1
	 * @param edge2
	 * @param edge3
	 * @param edge4
	 * @param adja1
	 * @param adja2
	 * @param adja3
	 * @param adja4
	 * @param adja5
	 * @param adja6
	 * @param adja7
	 * @param adja8
	 * @param placeOfBonus
	 * @return
	 */
	public JSONObject getJsonCard(CardAreaType area1, CardAreaType area2,
			CardAreaType area3, CardAreaType area4, CardAreaType area5,
			CardAreaType area6, CardAreaType area7, CardAreaType area8,
			ArrayList<Integer> edge1, ArrayList<Integer> edge2,
			ArrayList<Integer> edge3, ArrayList<Integer> edge4,
			ArrayList<Integer> adja1, ArrayList<Integer> adja2,
			ArrayList<Integer> adja3, ArrayList<Integer> adja4,
			ArrayList<Integer> adja5, ArrayList<Integer> adja6,
			ArrayList<Integer> adja7, ArrayList<Integer> adja8, int placeOfBonus) {

		ArrayList<String> areas = new ArrayList<>();
		areas.add(area1.toString());
		areas.add(area2.toString());
		areas.add(area3.toString());
		areas.add(area4.toString());
		areas.add(area5.toString());
		areas.add(area6.toString());
		areas.add(area7.toString());
		areas.add(area8.toString());

		ArrayList<ArrayList<Integer>> edges = new ArrayList<>();
		edges.add(edge1);
		edges.add(edge2);
		edges.add(edge3);
		edges.add(edge4);

		ArrayList<ArrayList<Integer>> adjacency = new ArrayList<>();
		adjacency.add(adja1);
		adjacency.add(adja2);
		adjacency.add(adja3);
		adjacency.add(adja4);
		adjacency.add(adja5);
		adjacency.add(adja6);
		adjacency.add(adja7);
		adjacency.add(adja8);

		if (placeOfBonus == -1) {
			return fillCard(areas, edges, adjacency);
		} else {
			ArrayList<Integer> bonus = new ArrayList<>();
			bonus.add(placeOfBonus);
			return fillCard(areas, edges, adjacency, bonus);
		}
	}

	/**
	 * provides the constructor for the only town card
	 * 
	 * @return the card as a JSONObject
	 */
	private JSONObject getJsonCard(CardAreaType area, ArrayList<Integer> edge1,
			ArrayList<Integer> edge2, ArrayList<Integer> edge3,
			ArrayList<Integer> edge4, int placeOfBonus) {
		ArrayList<String> areas = new ArrayList<>();
		areas.add(area.toString());

		ArrayList<ArrayList<Integer>> edges = new ArrayList<>();
		edges.add(edge1);
		edges.add(edge2);
		edges.add(edge3);
		edges.add(edge4);

		ArrayList<Integer> bonus = new ArrayList<>();
		bonus.add(placeOfBonus);
		JSONObject card = new JSONObject();
		try {
			card.put("areas", areas);
			card.put("edges", edges);
			card.put("bonus", bonus);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return card;
	}

	/**
	 * puts the areas, edges, adjacency in a jsonObject
	 * 
	 * @param areas
	 * @param edges
	 * @param adjacency
	 * @return card as a jsonObject
	 */
	public JSONObject fillCard(ArrayList<String> areas,
			ArrayList<ArrayList<Integer>> edges,
			ArrayList<ArrayList<Integer>> adjacency) {

		JSONObject card = new JSONObject();

		try {
			card.put("areas", areas);
			card.put("edges", edges);
			card.put("adjacency", adjacency);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return card;
	}

	/**
	 * puts the areas, edges, adjacency, bonus in a jsonObject
	 * 
	 * @param areas
	 * @param edges
	 * @param adjacency
	 * @param bonus
	 * @return card as a jsonObject
	 */
	public JSONObject fillCard(ArrayList<String> areas,
			ArrayList<ArrayList<Integer>> edges,
			ArrayList<ArrayList<Integer>> adjacency, ArrayList<Integer> bonus) {

		JSONObject card = new JSONObject();

		try {
			card.put("areas", areas);
			card.put("edges", edges);
			card.put("adjacency", adjacency);
			card.put("bonus", bonus);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return card;
	}

	public JSONObject createA() {

		// "edges" : [[0], [0], [0,1,0], [0]]
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge2.add(0);
		singleEdge3.add(0);
		singleEdge3.add(1);
		singleEdge3.add(0);
		singleEdge4.add(0);

		// "adjacency" : [[1,2], [0,2], [0,1]],
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();
		ArrayList<Integer> singleAdja3 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja1.add(2);

		singleAdja2.add(0);
		singleAdja2.add(2);

		singleAdja3.add(0);
		singleAdja3.add(1);

		// "areas" : ["Meadow", "Road", "Cloister"],
		return getJsonCard(CardAreaType.MEADOW, CardAreaType.ROAD,
				CardAreaType.CLOISTER, singleEdge1, singleEdge2, singleEdge3,
				singleEdge4, singleAdja1, singleAdja2, singleAdja3, -1);

	}

	public JSONObject createB() {
		// "edges": [[0], [0], [0], [0]]
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge2.add(0);
		singleEdge3.add(0);
		singleEdge4.add(0);

		// "adjacency": [[1], [0]]
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja2.add(0);

		// "areas": ["Meadow", "Cloister"],
		return getJsonCard(CardAreaType.MEADOW, CardAreaType.CLOISTER,
				singleEdge1, singleEdge2, singleEdge3, singleEdge4,
				singleAdja1, singleAdja2, -1);

	}

	public JSONObject createC() {

		// "edges": [[0], [0], [0], [0]]
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge2.add(0);
		singleEdge3.add(0);
		singleEdge4.add(0);

		// "areas": ["TOWN"],
		// "bonus" : [0]
		return getJsonCard(CardAreaType.TOWN, singleEdge1, singleEdge2,
				singleEdge3, singleEdge4, 0);

	}

	public JSONObject createD() {
		// "edges": [[0,1,2],[0],[2,1,0],[3]]
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge1.add(1);
		singleEdge1.add(2);

		singleEdge2.add(0);

		singleEdge3.add(2);
		singleEdge3.add(1);
		singleEdge3.add(0);

		singleEdge4.add(3);

		// "adjacency": [[1], [0,2], [1,3], [2]]
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();
		ArrayList<Integer> singleAdja3 = new ArrayList<>();
		ArrayList<Integer> singleAdja4 = new ArrayList<>();

		singleAdja1.add(1);

		singleAdja2.add(0);
		singleAdja2.add(2);

		singleAdja3.add(1);
		singleAdja3.add(3);

		singleAdja4.add(2);

		// "areas": ["Meadow", "Road", "Meadow", "Town"]
		return getJsonCard(CardAreaType.MEADOW, CardAreaType.ROAD,
				CardAreaType.MEADOW, CardAreaType.TOWN, singleEdge1,
				singleEdge2, singleEdge3, singleEdge4, singleAdja1,
				singleAdja2, singleAdja3, singleAdja4, -1);

	}

	public JSONObject createE() {
		// "edges": [[1], [0], [0], [0]]
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(1);
		singleEdge2.add(0);
		singleEdge3.add(0);
		singleEdge4.add(0);

		// "adjacency": [[1], [0]]
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja2.add(0);

		// "areas": ["Meadow", "TOWN"],
		return getJsonCard(CardAreaType.MEADOW, CardAreaType.TOWN, singleEdge1,
				singleEdge2, singleEdge3, singleEdge4, singleAdja1,
				singleAdja2, -1);

	}

	public JSONObject createF() {

		// "edges": [[0], [1], [2], [1]],
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge2.add(1);
		singleEdge3.add(2);
		singleEdge4.add(1);

		// "adjacency":[[1], [0,2], [1]],
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();
		ArrayList<Integer> singleAdja3 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja2.add(0);
		singleAdja2.add(2);
		singleAdja3.add(1);

		// "areas": ["Meadow", "Town", "Meadow"],
		// "bonus":[1]
		return getJsonCard(CardAreaType.MEADOW, CardAreaType.TOWN,
				CardAreaType.MEADOW, singleEdge1, singleEdge2, singleEdge3,
				singleEdge4, singleAdja1, singleAdja2, singleAdja3, 1);

	}

	public JSONObject createG() {
		// "edges": [[1], [0], [1], [2]],
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(1);
		singleEdge2.add(0);
		singleEdge3.add(1);
		singleEdge4.add(2);

		// "adjacency": [[1], [0,2], [1]]
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();
		ArrayList<Integer> singleAdja3 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja2.add(0);
		singleAdja2.add(2);
		singleAdja3.add(1);

		// "areas": ["Meadow", "Town", "Meadow"],
		return getJsonCard(CardAreaType.MEADOW, CardAreaType.TOWN,
				CardAreaType.MEADOW, singleEdge1, singleEdge2, singleEdge3,
				singleEdge4, singleAdja1, singleAdja2, singleAdja3, -1);

	}

	public JSONObject createH() {
		// edges": [[0],[1], [0], [2]],
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge2.add(1);
		singleEdge3.add(0);
		singleEdge4.add(2);

		// "adjacency": [[1,2], [0], [0]]
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();
		ArrayList<Integer> singleAdja3 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja1.add(2);
		singleAdja2.add(0);
		singleAdja3.add(0);

		// "areas": ["Meadow", "Town", "TOWN"],
		return getJsonCard(CardAreaType.MEADOW, CardAreaType.TOWN,
				CardAreaType.TOWN, singleEdge1, singleEdge2, singleEdge3,
				singleEdge4, singleAdja1, singleAdja2, singleAdja3, -1);
	}

	public JSONObject createI() {
		// "edges": [[0], [0], [1], [2]],
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge2.add(0);
		singleEdge3.add(1);
		singleEdge4.add(2);

		// "adjacency":[[1,2], [0], [0]]
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();
		ArrayList<Integer> singleAdja3 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja1.add(2);
		singleAdja2.add(0);
		singleAdja3.add(0);

		// "areas": ["Meadow", "Town", "Town"],
		return getJsonCard(CardAreaType.MEADOW, CardAreaType.TOWN,
				CardAreaType.TOWN, singleEdge1, singleEdge2, singleEdge3,
				singleEdge4, singleAdja1, singleAdja2, singleAdja3, -1);
	}

	public JSONObject createJ() {
		// "edges": [[0], [1], [3,2,1], [1,2,3]],
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge2.add(1);
		singleEdge3.add(3);
		singleEdge3.add(2);
		singleEdge3.add(1);
		singleEdge4.add(1);
		singleEdge4.add(2);
		singleEdge4.add(3);

		// "adjacency": [[1], [0,2], [1,3], [2]]
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();
		ArrayList<Integer> singleAdja3 = new ArrayList<>();
		ArrayList<Integer> singleAdja4 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja2.add(0);
		singleAdja2.add(2);
		singleAdja3.add(1);
		singleAdja3.add(3);
		singleAdja4.add(2);

		// "areas": ["Town", "Meadow", "Road", "Meadow"]
		return getJsonCard(CardAreaType.TOWN, CardAreaType.MEADOW,
				CardAreaType.ROAD, CardAreaType.MEADOW, singleEdge1,
				singleEdge2, singleEdge3, singleEdge4, singleAdja1,
				singleAdja2, singleAdja3, singleAdja4, -1);
	}

	public JSONObject createK() {
		// edges [[0,1,2], [2,1,0], [2], [3]]
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge1.add(1);
		singleEdge1.add(2);
		singleEdge2.add(2);
		singleEdge2.add(1);
		singleEdge2.add(0);
		singleEdge3.add(2);
		singleEdge4.add(3);

		// "adjacency": [[1], [0,2], [1,3], [2]]
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();
		ArrayList<Integer> singleAdja3 = new ArrayList<>();
		ArrayList<Integer> singleAdja4 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja2.add(0);
		singleAdja2.add(2);
		singleAdja3.add(1);
		singleAdja3.add(3);
		singleAdja4.add(2);

		// "areas": ["Meadow", "Road", "Meadow", "Town"]
		return getJsonCard(CardAreaType.MEADOW, CardAreaType.ROAD,
				CardAreaType.MEADOW, CardAreaType.TOWN, singleEdge1,
				singleEdge2, singleEdge3, singleEdge4, singleAdja1,
				singleAdja2, singleAdja3, singleAdja4, -1);
	}

	public JSONObject createL() {
		// "edges": [[0,1,2], [4,3,0], [2,5,4], [6]]
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
		singleEdge3.add(2);
		singleEdge3.add(5);
		singleEdge3.add(4);
		singleEdge4.add(6);

		// "adjacency": [[1,3], [0,2], [1,5,6], [0,4], [3,5], [4,2], [2]]
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();
		ArrayList<Integer> singleAdja3 = new ArrayList<>();
		ArrayList<Integer> singleAdja4 = new ArrayList<>();
		ArrayList<Integer> singleAdja5 = new ArrayList<>();
		ArrayList<Integer> singleAdja6 = new ArrayList<>();
		ArrayList<Integer> singleAdja7 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja1.add(3);
		singleAdja2.add(0);
		singleAdja2.add(2);
		singleAdja3.add(1);
		singleAdja3.add(5);
		singleAdja3.add(6);
		singleAdja4.add(0);
		singleAdja4.add(4);
		singleAdja5.add(3);
		singleAdja5.add(5);
		singleAdja6.add(4);
		singleAdja6.add(2);
		singleAdja7.add(2);

		// "areas" : ["Meadow", "Road", "Meadow", "Road", "Meadow", "Road",
		// "Town"]
		return getJsonCard(CardAreaType.MEADOW, CardAreaType.ROAD,
				CardAreaType.MEADOW, CardAreaType.ROAD, CardAreaType.MEADOW,
				CardAreaType.ROAD, CardAreaType.TOWN, singleEdge1, singleEdge2,
				singleEdge3, singleEdge4, singleAdja1, singleAdja2,
				singleAdja3, singleAdja4, singleAdja5, singleAdja6,
				singleAdja7, -1);
	}

	public JSONObject createM() {
		// "edges": [[0], [0], [1], [1]],
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge2.add(0);
		singleEdge3.add(1);
		singleEdge4.add(1);

		// "adjacency": [[1], [0]],
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja2.add(0);

		// "areas": ["Town", "Meadow"],
		// "bonus": [0]
		return getJsonCard(CardAreaType.TOWN, CardAreaType.MEADOW, singleEdge1,
				singleEdge2, singleEdge3, singleEdge4, singleAdja1,
				singleAdja2, 0);
	}

	public JSONObject createN() {
		// "edges": [[0], [0], [1], [1]],
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge2.add(0);
		singleEdge3.add(1);
		singleEdge4.add(1);

		// "adjacency": [[1], [0]],
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja2.add(0);

		// "areas": ["Town", "Meadow"],

		return getJsonCard(CardAreaType.TOWN, CardAreaType.MEADOW, singleEdge1,
				singleEdge2, singleEdge3, singleEdge4, singleAdja1,
				singleAdja2, -1);
	}

	public JSONObject createO() {
		// "edges": [[0], [0], [3,2,1], [1,2,3]],
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge2.add(0);
		singleEdge3.add(3);
		singleEdge3.add(2);
		singleEdge3.add(1);
		singleEdge4.add(1);
		singleEdge4.add(2);
		singleEdge4.add(3);

		// "adjacency": [[1], [0,2], [1,3], [2]],
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();
		ArrayList<Integer> singleAdja3 = new ArrayList<>();
		ArrayList<Integer> singleAdja4 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja2.add(0);
		singleAdja2.add(2);
		singleAdja3.add(1);
		singleAdja3.add(3);
		singleAdja4.add(2);

		// "areas": ["Town", "Meadow", "Road", "Meadow"]
		// "bonus" : [0]
		return getJsonCard(CardAreaType.TOWN, CardAreaType.MEADOW,
				CardAreaType.ROAD, CardAreaType.MEADOW, singleEdge1,
				singleEdge2, singleEdge3, singleEdge4, singleAdja1,
				singleAdja2, singleAdja3, singleAdja4, 0);
	}

	public JSONObject createP() {
		// "edges": [[0], [0], [3,2,1], [1,2,3]],
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge2.add(0);
		singleEdge3.add(3);
		singleEdge3.add(2);
		singleEdge3.add(1);
		singleEdge4.add(1);
		singleEdge4.add(2);
		singleEdge4.add(3);

		// "adjacency": [[1], [0,2], [1,3], [2]],
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();
		ArrayList<Integer> singleAdja3 = new ArrayList<>();
		ArrayList<Integer> singleAdja4 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja2.add(0);
		singleAdja2.add(2);
		singleAdja3.add(1);
		singleAdja3.add(3);
		singleAdja4.add(2);

		// "areas": ["Town", "Meadow", "Road", "Meadow"],

		return getJsonCard(CardAreaType.TOWN, CardAreaType.MEADOW,
				CardAreaType.ROAD, CardAreaType.MEADOW, singleEdge1,
				singleEdge2, singleEdge3, singleEdge4, singleAdja1,
				singleAdja2, singleAdja3, singleAdja4, -1);
	}

	public JSONObject createQ() {
		// "edges": [[0], [0], [1], [0]],
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge2.add(0);
		singleEdge3.add(1);
		singleEdge4.add(0);

		// "adjacency": [[1], [0]],
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja2.add(0);

		// "areas": ["Town", "Meadow"],
		// "bonus":[0]
		return getJsonCard(CardAreaType.TOWN, CardAreaType.MEADOW, singleEdge1,
				singleEdge2, singleEdge3, singleEdge4, singleAdja1,
				singleAdja2, 0);
	}

	public JSONObject createR() {
		// "edges": [[0], [0], [1], [0]],
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge2.add(0);
		singleEdge3.add(1);
		singleEdge4.add(0);

		// "adjacency": [[1], [0]],
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja2.add(0);

		// "areas": ["Town", "Meadow"],

		return getJsonCard(CardAreaType.TOWN, CardAreaType.MEADOW, singleEdge1,
				singleEdge2, singleEdge3, singleEdge4, singleAdja1,
				singleAdja2, -1);
	}

	public JSONObject createS() {
		// "edges": [[0], [0], [3,2,1], [0]],
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge2.add(0);
		singleEdge3.add(3);
		singleEdge3.add(2);
		singleEdge3.add(1);
		singleEdge4.add(0);

		// "adjacency": [[1,2,3], [0,2], [0,1,3], [0,2]],
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();
		ArrayList<Integer> singleAdja3 = new ArrayList<>();
		ArrayList<Integer> singleAdja4 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja1.add(2);
		singleAdja1.add(3);
		singleAdja2.add(0);
		singleAdja2.add(2);
		singleAdja3.add(0);
		singleAdja3.add(1);
		singleAdja3.add(3);
		singleAdja4.add(0);
		singleAdja4.add(2);

		// "areas": ["Town", "Meadow", "Road", "Meadow"],
		// "bonus": [0]
		return getJsonCard(CardAreaType.TOWN, CardAreaType.MEADOW,
				CardAreaType.ROAD, CardAreaType.MEADOW, singleEdge1,
				singleEdge2, singleEdge3, singleEdge4, singleAdja1,
				singleAdja2, singleAdja3, singleAdja4, 0);
	}

	public JSONObject createT() {
		// "edges": [[0], [0], [3,2,1], [0]],
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge2.add(0);
		singleEdge3.add(3);
		singleEdge3.add(2);
		singleEdge3.add(1);
		singleEdge4.add(0);

		// "adjacency": [[1,2,3], [0,2], [0,1,3], [0,2]],
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();
		ArrayList<Integer> singleAdja3 = new ArrayList<>();
		ArrayList<Integer> singleAdja4 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja1.add(2);
		singleAdja1.add(3);
		singleAdja2.add(0);
		singleAdja2.add(2);
		singleAdja3.add(0);
		singleAdja3.add(1);
		singleAdja3.add(3);
		singleAdja4.add(0);
		singleAdja4.add(2);

		// "areas": ["Town", "Meadow", "Road", "Meadow"],

		return getJsonCard(CardAreaType.TOWN, CardAreaType.MEADOW,
				CardAreaType.ROAD, CardAreaType.MEADOW, singleEdge1,
				singleEdge2, singleEdge3, singleEdge4, singleAdja1,
				singleAdja2, singleAdja3, singleAdja4, -1);
	}

	public JSONObject createU() {
		// "edges": [[0,1,2], [0], [2,1,0], [2]],
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge1.add(1);
		singleEdge1.add(2);
		singleEdge2.add(0);
		singleEdge3.add(2);
		singleEdge3.add(1);
		singleEdge3.add(0);
		singleEdge4.add(2);

		// "adjacency": [[1], [0,2], [1]]
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();
		ArrayList<Integer> singleAdja3 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja2.add(0);
		singleAdja2.add(2);
		singleAdja3.add(1);

		// "areas": ["Meadow", "Road", "Meadow"],

		return getJsonCard(CardAreaType.MEADOW, CardAreaType.ROAD,
				CardAreaType.MEADOW, singleEdge1, singleEdge2, singleEdge3,
				singleEdge4, singleAdja1, singleAdja2, singleAdja3, -1);
	}

	public JSONObject createV() {
		// "edges": [[0], [2,1,0], [0,1,2], [0]],
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge2.add(2);
		singleEdge2.add(1);
		singleEdge2.add(0);
		singleEdge3.add(0);
		singleEdge3.add(1);
		singleEdge3.add(2);
		singleEdge4.add(0);

		// "adjacency": [[1], [0,2], [1]]
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();
		ArrayList<Integer> singleAdja3 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja2.add(0);
		singleAdja2.add(2);
		singleAdja3.add(1);

		// "areas": ["Meadow", "Road", "Meadow"],

		return getJsonCard(CardAreaType.MEADOW, CardAreaType.ROAD,
				CardAreaType.MEADOW, singleEdge1, singleEdge2, singleEdge3,
				singleEdge4, singleAdja1, singleAdja2, singleAdja3, -1);
	}

	public JSONObject createW() {
		// "edges": [[0], [2,1,0], [4,3,2], [0,5,4]],
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge2.add(2);
		singleEdge2.add(1);
		singleEdge2.add(0);
		singleEdge3.add(4);
		singleEdge3.add(3);
		singleEdge3.add(2);
		singleEdge4.add(0);
		singleEdge4.add(5);
		singleEdge4.add(4);

		// "adjacency": [[1,3], [0,2], [1,4], [2,4], [3,5], [0,4]]
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();
		ArrayList<Integer> singleAdja3 = new ArrayList<>();
		ArrayList<Integer> singleAdja4 = new ArrayList<>();
		ArrayList<Integer> singleAdja5 = new ArrayList<>();
		ArrayList<Integer> singleAdja6 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja1.add(3);
		singleAdja2.add(0);
		singleAdja2.add(2);
		singleAdja3.add(1);
		singleAdja3.add(4);
		singleAdja4.add(2);
		singleAdja4.add(4);
		singleAdja5.add(3);
		singleAdja5.add(5);
		singleAdja6.add(0);
		singleAdja6.add(4);

		// "areas": ["Meadow","Road", "Meadow", "Road", "Meadow", "Road"],

		return getJsonCard(CardAreaType.MEADOW, CardAreaType.ROAD,
				CardAreaType.MEADOW, CardAreaType.ROAD, CardAreaType.MEADOW,
				CardAreaType.ROAD, singleEdge1, singleEdge2, singleEdge3,
				singleEdge4, singleAdja1, singleAdja2, singleAdja3,
				singleEdge4, singleAdja5, singleAdja6, -1);
	}

	public JSONObject createX() {
		// "edges": [[0,1,2], [4,3,0], [6,5,4], [2,7,6]],
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

		// "adjacency": [[1,3], [0,2], [1,7], [0,4], [3,5], [4,6], [5,7], [2,6]]
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();
		ArrayList<Integer> singleAdja3 = new ArrayList<>();
		ArrayList<Integer> singleAdja4 = new ArrayList<>();
		ArrayList<Integer> singleAdja5 = new ArrayList<>();
		ArrayList<Integer> singleAdja6 = new ArrayList<>();
		ArrayList<Integer> singleAdja7 = new ArrayList<>();
		ArrayList<Integer> singleAdja8 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja1.add(3);
		singleAdja2.add(0);
		singleAdja2.add(2);
		singleAdja3.add(1);
		singleAdja3.add(7);
		singleAdja4.add(0);
		singleAdja4.add(4);
		singleAdja5.add(3);
		singleAdja5.add(5);
		singleAdja6.add(4);
		singleAdja6.add(6);
		singleAdja7.add(5);
		singleAdja7.add(7);
		singleAdja8.add(2);
		singleAdja8.add(6);

		// "areas": ["Meadow", "Road", "Meadow", "Road", "Meadow", "Road",
		// "Meadow", "Road"],

		return getJsonCard(CardAreaType.MEADOW, CardAreaType.ROAD,
				CardAreaType.MEADOW, CardAreaType.ROAD, CardAreaType.MEADOW,
				CardAreaType.ROAD, CardAreaType.MEADOW, CardAreaType.ROAD,
				singleEdge1, singleEdge2, singleEdge3, singleEdge4,
				singleAdja1, singleAdja2, singleAdja3, singleEdge4,
				singleAdja5, singleAdja6, singleAdja7, singleAdja8, -1);
	}

	public JSONObject createA_1() {

		// "edges" : [[0], [0], [0,1,0], [0]]
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge2.add(0);
		singleEdge3.add(0);
		singleEdge3.add(1);
		singleEdge3.add(0);
		singleEdge4.add(0);

		// "adjacency" : [[1,2], [0,2], [0,1]],
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();
		ArrayList<Integer> singleAdja3 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja1.add(2);

		singleAdja2.add(0);
		singleAdja2.add(2);

		singleAdja3.add(0);
		singleAdja3.add(1);

		// "areas" : ["Meadow", "Road", "Cloister"],
		return getJsonCard(CardAreaType.MEADOW, CardAreaType.ROAD,
				CardAreaType.CLOISTER, singleEdge2, singleEdge3, singleEdge4,
				singleEdge1, singleAdja1, singleAdja2, singleAdja3, -1);

	}

	public JSONObject createB_1() {
		// "edges": [[0], [0], [0], [0]]
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge2.add(0);
		singleEdge3.add(0);
		singleEdge4.add(0);

		// "adjacency": [[1], [0]]
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja2.add(0);

		// "areas": ["Meadow", "Cloister"],
		return getJsonCard(CardAreaType.MEADOW, CardAreaType.CLOISTER,
				singleEdge2, singleEdge3, singleEdge4, singleEdge1,
				singleAdja1, singleAdja2, -1);

	}

	public JSONObject createC_1() {

		// "edges": [[0], [0], [0], [0]]
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge2.add(0);
		singleEdge3.add(0);
		singleEdge4.add(0);

		// "areas": ["TOWN"],
		// "bonus" : [0]
		return getJsonCard(CardAreaType.TOWN, singleEdge2, singleEdge3,
				singleEdge4, singleEdge1, 0);

	}

	public JSONObject createD_1() {
		// "edges": [[0,1,2],[0],[2,1,0],[3]]
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge1.add(1);
		singleEdge1.add(2);

		singleEdge2.add(0);

		singleEdge3.add(2);
		singleEdge3.add(1);
		singleEdge3.add(0);

		singleEdge4.add(3);

		// "adjacency": [[1], [0,2], [1,3], [2]]
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();
		ArrayList<Integer> singleAdja3 = new ArrayList<>();
		ArrayList<Integer> singleAdja4 = new ArrayList<>();

		singleAdja1.add(1);

		singleAdja2.add(0);
		singleAdja2.add(2);

		singleAdja3.add(1);
		singleAdja3.add(3);

		singleAdja4.add(2);

		// "areas": ["Meadow", "Road", "Meadow", "Town"]
		return getJsonCard(CardAreaType.MEADOW, CardAreaType.ROAD,
				CardAreaType.MEADOW, CardAreaType.TOWN, singleEdge2,
				singleEdge3, singleEdge4, singleEdge1, singleAdja1,
				singleAdja2, singleAdja3, singleAdja4, -1);

	}

	public JSONObject createE_1() {
		// "edges": [[1], [0], [0], [0]]
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(1);
		singleEdge2.add(0);
		singleEdge3.add(0);
		singleEdge4.add(0);

		// "adjacency": [[1], [0]]
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja2.add(0);

		// "areas": ["Meadow", "TOWN"],
		return getJsonCard(CardAreaType.MEADOW, CardAreaType.TOWN, singleEdge2,
				singleEdge3, singleEdge4, singleEdge1, singleAdja1,
				singleAdja2, -1);

	}

	public JSONObject createF_1() {

		// "edges": [[0], [1], [2], [1]],
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge2.add(1);
		singleEdge3.add(2);
		singleEdge4.add(1);

		// "adjacency":[[1], [0,2], [1]],
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();
		ArrayList<Integer> singleAdja3 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja2.add(0);
		singleAdja2.add(2);
		singleAdja3.add(1);

		// "areas": ["Meadow", "Town", "Meadow"],
		// "bonus":[1]
		return getJsonCard(CardAreaType.MEADOW, CardAreaType.TOWN,
				CardAreaType.MEADOW, singleEdge2, singleEdge3, singleEdge4,
				singleEdge1, singleAdja1, singleAdja2, singleAdja3, 1);

	}

	public JSONObject createG_1() {
		// "edges": [[1], [0], [1], [2]],
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(1);
		singleEdge2.add(0);
		singleEdge3.add(1);
		singleEdge4.add(2);

		// "adjacency": [[1], [0,2], [1]]
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();
		ArrayList<Integer> singleAdja3 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja2.add(0);
		singleAdja2.add(2);
		singleAdja3.add(1);

		// "areas": ["Meadow", "Town", "Meadow"],
		return getJsonCard(CardAreaType.MEADOW, CardAreaType.TOWN,
				CardAreaType.MEADOW, singleEdge2, singleEdge3, singleEdge4,
				singleEdge1, singleAdja1, singleAdja2, singleAdja3, -1);

	}

	public JSONObject createH_1() {
		// edges": [[0],[1], [0], [2]],
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge2.add(1);
		singleEdge3.add(0);
		singleEdge4.add(2);

		// "adjacency": [[1,2], [0], [0]]
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();
		ArrayList<Integer> singleAdja3 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja1.add(2);
		singleAdja2.add(0);
		singleAdja3.add(0);

		// "areas": ["Meadow", "Town", "TOWN"],
		return getJsonCard(CardAreaType.MEADOW, CardAreaType.TOWN,
				CardAreaType.TOWN, singleEdge2, singleEdge3, singleEdge4,
				singleEdge1, singleAdja1, singleAdja2, singleAdja3, -1);
	}

	public JSONObject createI_1() {
		// "edges": [[0], [0], [1], [2]],
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge2.add(0);
		singleEdge3.add(1);
		singleEdge4.add(2);

		// "adjacency":[[1,2], [0], [0]]
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();
		ArrayList<Integer> singleAdja3 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja1.add(2);
		singleAdja2.add(0);
		singleAdja3.add(0);

		// "areas": ["Meadow", "Town", "Town"],
		return getJsonCard(CardAreaType.MEADOW, CardAreaType.TOWN,
				CardAreaType.TOWN, singleEdge2, singleEdge3, singleEdge4,
				singleEdge1, singleAdja1, singleAdja2, singleAdja3, -1);
	}

	public JSONObject createJ_1() {
		// "edges": [[0], [1], [3,2,1], [1,2,3]],
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge2.add(1);
		singleEdge3.add(3);
		singleEdge3.add(2);
		singleEdge3.add(1);
		singleEdge4.add(1);
		singleEdge4.add(2);
		singleEdge4.add(3);

		// "adjacency": [[1], [0,2], [1,3], [2]]
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();
		ArrayList<Integer> singleAdja3 = new ArrayList<>();
		ArrayList<Integer> singleAdja4 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja2.add(0);
		singleAdja2.add(2);
		singleAdja3.add(1);
		singleAdja3.add(3);
		singleAdja4.add(2);

		// "areas": ["Town", "Meadow", "Road", "Meadow"]
		return getJsonCard(CardAreaType.TOWN, CardAreaType.MEADOW,
				CardAreaType.ROAD, CardAreaType.MEADOW, singleEdge2,
				singleEdge3, singleEdge4, singleEdge1, singleAdja1,
				singleAdja2, singleAdja3, singleAdja4, -1);
	}

	public JSONObject createK_1() {
		// edges [[0,1,2], [2,1,0], [2], [3]]
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge1.add(1);
		singleEdge1.add(2);
		singleEdge2.add(2);
		singleEdge2.add(1);
		singleEdge2.add(0);
		singleEdge3.add(2);
		singleEdge4.add(3);

		// "adjacency": [[1], [0,2], [1,3], [2]]
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();
		ArrayList<Integer> singleAdja3 = new ArrayList<>();
		ArrayList<Integer> singleAdja4 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja2.add(0);
		singleAdja2.add(2);
		singleAdja3.add(1);
		singleAdja3.add(3);
		singleAdja4.add(2);

		// "areas": ["Meadow", "Road", "Meadow", "Town"]
		return getJsonCard(CardAreaType.MEADOW, CardAreaType.ROAD,
				CardAreaType.MEADOW, CardAreaType.TOWN, singleEdge2,
				singleEdge3, singleEdge4, singleEdge1, singleAdja1,
				singleAdja2, singleAdja3, singleAdja4, -1);
	}

	public JSONObject createL_1() {
		// "edges": [[0,1,2], [4,3,0], [2,5,4], [6]]
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
		singleEdge3.add(2);
		singleEdge3.add(5);
		singleEdge3.add(4);
		singleEdge4.add(6);

		// "adjacency": [[1,3], [0,2], [1,5,6], [0,4], [3,5], [4,2], [2]]
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();
		ArrayList<Integer> singleAdja3 = new ArrayList<>();
		ArrayList<Integer> singleAdja4 = new ArrayList<>();
		ArrayList<Integer> singleAdja5 = new ArrayList<>();
		ArrayList<Integer> singleAdja6 = new ArrayList<>();
		ArrayList<Integer> singleAdja7 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja1.add(3);
		singleAdja2.add(0);
		singleAdja2.add(2);
		singleAdja3.add(1);
		singleAdja3.add(5);
		singleAdja3.add(6);
		singleAdja4.add(0);
		singleAdja4.add(4);
		singleAdja5.add(3);
		singleAdja5.add(5);
		singleAdja6.add(4);
		singleAdja6.add(2);
		singleAdja7.add(2);

		// "areas" : ["Meadow", "Road", "Meadow", "Road", "Meadow", "Road",
		// "Town"]
		return getJsonCard(CardAreaType.MEADOW, CardAreaType.ROAD,
				CardAreaType.MEADOW, CardAreaType.ROAD, CardAreaType.MEADOW,
				CardAreaType.ROAD, CardAreaType.TOWN, singleEdge2, singleEdge3,
				singleEdge4, singleEdge1, singleAdja1, singleAdja2,
				singleAdja3, singleAdja4, singleAdja5, singleAdja6,
				singleAdja7, -1);
	}

	public JSONObject createM_1() {
		// "edges": [[0], [0], [1], [1]],
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge2.add(0);
		singleEdge3.add(1);
		singleEdge4.add(1);

		// "adjacency": [[1], [0]],
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja2.add(0);

		// "areas": ["Town", "Meadow"],
		// "bonus": [0]
		return getJsonCard(CardAreaType.TOWN, CardAreaType.MEADOW, singleEdge2,
				singleEdge3, singleEdge4, singleEdge1, singleAdja1,
				singleAdja2, 0);
	}

	public JSONObject createN_1() {
		// "edges": [[0], [0], [1], [1]],
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge2.add(0);
		singleEdge3.add(1);
		singleEdge4.add(1);

		// "adjacency": [[1], [0]],
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja2.add(0);

		// "areas": ["Town", "Meadow"],

		return getJsonCard(CardAreaType.TOWN, CardAreaType.MEADOW, singleEdge2,
				singleEdge3, singleEdge4, singleEdge1, singleAdja1,
				singleAdja2, -1);
	}

	public JSONObject createO_1() {
		// "edges": [[0], [0], [3,2,1], [1,2,3]],
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge2.add(0);
		singleEdge3.add(3);
		singleEdge3.add(2);
		singleEdge3.add(1);
		singleEdge4.add(1);
		singleEdge4.add(2);
		singleEdge4.add(3);

		// "adjacency": [[1], [0,2], [1,3], [2]],
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();
		ArrayList<Integer> singleAdja3 = new ArrayList<>();
		ArrayList<Integer> singleAdja4 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja2.add(0);
		singleAdja2.add(2);
		singleAdja3.add(1);
		singleAdja3.add(3);
		singleAdja4.add(2);

		// "areas": ["Town", "Meadow", "Road", "Meadow"]
		// "bonus" : [0]
		return getJsonCard(CardAreaType.TOWN, CardAreaType.MEADOW,
				CardAreaType.ROAD, CardAreaType.MEADOW, singleEdge2,
				singleEdge3, singleEdge4, singleEdge1, singleAdja1,
				singleAdja2, singleAdja3, singleAdja4, 0);
	}

	public JSONObject createP_1() {
		// "edges": [[0], [0], [3,2,1], [1,2,3]],
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge2.add(0);
		singleEdge3.add(3);
		singleEdge3.add(2);
		singleEdge3.add(1);
		singleEdge4.add(1);
		singleEdge4.add(2);
		singleEdge4.add(3);

		// "adjacency": [[1], [0,2], [1,3], [2]],
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();
		ArrayList<Integer> singleAdja3 = new ArrayList<>();
		ArrayList<Integer> singleAdja4 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja2.add(0);
		singleAdja2.add(2);
		singleAdja3.add(1);
		singleAdja3.add(3);
		singleAdja4.add(2);

		// "areas": ["Town", "Meadow", "Road", "Meadow"],

		return getJsonCard(CardAreaType.TOWN, CardAreaType.MEADOW,
				CardAreaType.ROAD, CardAreaType.MEADOW, singleEdge2,
				singleEdge3, singleEdge4, singleEdge1, singleAdja1,
				singleAdja2, singleAdja3, singleAdja4, -1);
	}

	public JSONObject createQ_1() {
		// "edges": [[0], [0], [1], [0]],
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge2.add(0);
		singleEdge3.add(1);
		singleEdge4.add(0);

		// "adjacency": [[1], [0]],
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja2.add(0);

		// "areas": ["Town", "Meadow"],
		// "bonus":[0]
		return getJsonCard(CardAreaType.TOWN, CardAreaType.MEADOW, singleEdge2,
				singleEdge3, singleEdge4, singleEdge1, singleAdja1,
				singleAdja2, 0);
	}

	public JSONObject createR_1() {
		// "edges": [[0], [0], [1], [0]],
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge2.add(0);
		singleEdge3.add(1);
		singleEdge4.add(0);

		// "adjacency": [[1], [0]],
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja2.add(0);

		// "areas": ["Town", "Meadow"],

		return getJsonCard(CardAreaType.TOWN, CardAreaType.MEADOW, singleEdge2,
				singleEdge3, singleEdge4, singleEdge1, singleAdja1,
				singleAdja2, -1);
	}

	public JSONObject createS_1() {
		// "edges": [[0], [0], [3,2,1], [0]],
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge2.add(0);
		singleEdge3.add(3);
		singleEdge3.add(2);
		singleEdge3.add(1);
		singleEdge4.add(0);

		// "adjacency": [[1,2,3], [0,2], [0,1,3], [0,2]],
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();
		ArrayList<Integer> singleAdja3 = new ArrayList<>();
		ArrayList<Integer> singleAdja4 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja1.add(2);
		singleAdja1.add(3);
		singleAdja2.add(0);
		singleAdja2.add(2);
		singleAdja3.add(0);
		singleAdja3.add(1);
		singleAdja3.add(3);
		singleAdja4.add(0);
		singleAdja4.add(2);

		// "areas": ["Town", "Meadow", "Road", "Meadow"],
		// "bonus": [0]
		return getJsonCard(CardAreaType.TOWN, CardAreaType.MEADOW,
				CardAreaType.ROAD, CardAreaType.MEADOW, singleEdge2,
				singleEdge3, singleEdge4, singleEdge1, singleAdja1,
				singleAdja2, singleAdja3, singleAdja4, 0);
	}

	public JSONObject createT_1() {
		// "edges": [[0], [0], [3,2,1], [0]],
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge2.add(0);
		singleEdge3.add(3);
		singleEdge3.add(2);
		singleEdge3.add(1);
		singleEdge4.add(0);

		// "adjacency": [[1,2,3], [0,2], [0,1,3], [0,2]],
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();
		ArrayList<Integer> singleAdja3 = new ArrayList<>();
		ArrayList<Integer> singleAdja4 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja1.add(2);
		singleAdja1.add(3);
		singleAdja2.add(0);
		singleAdja2.add(2);
		singleAdja3.add(0);
		singleAdja3.add(1);
		singleAdja3.add(3);
		singleAdja4.add(0);
		singleAdja4.add(2);

		// "areas": ["Town", "Meadow", "Road", "Meadow"],

		return getJsonCard(CardAreaType.TOWN, CardAreaType.MEADOW,
				CardAreaType.ROAD, CardAreaType.MEADOW, singleEdge2,
				singleEdge3, singleEdge4, singleEdge1, singleAdja1,
				singleAdja2, singleAdja3, singleAdja4, -1);
	}

	public JSONObject createU_1() {
		// "edges": [[0,1,2], [0], [2,1,0], [2]],
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge1.add(1);
		singleEdge1.add(2);
		singleEdge2.add(0);
		singleEdge3.add(2);
		singleEdge3.add(1);
		singleEdge3.add(0);
		singleEdge4.add(2);

		// "adjacency": [[1], [0,2], [1]]
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();
		ArrayList<Integer> singleAdja3 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja2.add(0);
		singleAdja2.add(2);
		singleAdja3.add(1);

		// "areas": ["Meadow", "Road", "Meadow"],

		return getJsonCard(CardAreaType.MEADOW, CardAreaType.ROAD,
				CardAreaType.MEADOW, singleEdge2, singleEdge3, singleEdge4,
				singleEdge1, singleAdja1, singleAdja2, singleAdja3, -1);
	}

	public JSONObject createV_1() {
		// "edges": [[0], [2,1,0], [0,1,2], [0]],
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge2.add(2);
		singleEdge2.add(1);
		singleEdge2.add(0);
		singleEdge3.add(0);
		singleEdge3.add(1);
		singleEdge3.add(2);
		singleEdge4.add(0);

		// "adjacency": [[1], [0,2], [1]]
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();
		ArrayList<Integer> singleAdja3 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja2.add(0);
		singleAdja2.add(2);
		singleAdja3.add(1);

		// "areas": ["Meadow", "Road", "Meadow"],

		return getJsonCard(CardAreaType.MEADOW, CardAreaType.ROAD,
				CardAreaType.MEADOW, singleEdge2, singleEdge3, singleEdge4,
				singleEdge1, singleAdja1, singleAdja2, singleAdja3, -1);
	}

	public JSONObject createW_1() {
		// "edges": [[0], [2,1,0], [4,3,2], [0,5,4]],
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge2.add(2);
		singleEdge2.add(1);
		singleEdge2.add(0);
		singleEdge3.add(4);
		singleEdge3.add(3);
		singleEdge3.add(2);
		singleEdge4.add(0);
		singleEdge4.add(5);
		singleEdge4.add(4);

		// "adjacency": [[1,3], [0,2], [1,4], [2,4], [3,5], [0,4]]
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();
		ArrayList<Integer> singleAdja3 = new ArrayList<>();
		ArrayList<Integer> singleAdja4 = new ArrayList<>();
		ArrayList<Integer> singleAdja5 = new ArrayList<>();
		ArrayList<Integer> singleAdja6 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja1.add(3);
		singleAdja2.add(0);
		singleAdja2.add(2);
		singleAdja3.add(1);
		singleAdja3.add(4);
		singleAdja4.add(2);
		singleAdja4.add(4);
		singleAdja5.add(3);
		singleAdja5.add(5);
		singleAdja6.add(0);
		singleAdja6.add(4);

		// "areas": ["Meadow","Road", "Meadow", "Road", "Meadow", "Road"],

		return getJsonCard(CardAreaType.MEADOW, CardAreaType.ROAD,
				CardAreaType.MEADOW, CardAreaType.ROAD, CardAreaType.MEADOW,
				CardAreaType.ROAD, singleEdge2, singleEdge3, singleEdge4,
				singleEdge1, singleAdja1, singleAdja2, singleAdja3,
				singleEdge4, singleAdja5, singleAdja6, -1);
	}

	public JSONObject createX_1() {
		// "edges": [[0,1,2], [4,3,0], [6,5,4], [2,7,6]],
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

		// "adjacency": [[1,3], [0,2], [1,7], [0,4], [3,5], [4,6], [5,7], [2,6]]
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();
		ArrayList<Integer> singleAdja3 = new ArrayList<>();
		ArrayList<Integer> singleAdja4 = new ArrayList<>();
		ArrayList<Integer> singleAdja5 = new ArrayList<>();
		ArrayList<Integer> singleAdja6 = new ArrayList<>();
		ArrayList<Integer> singleAdja7 = new ArrayList<>();
		ArrayList<Integer> singleAdja8 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja1.add(3);
		singleAdja2.add(0);
		singleAdja2.add(2);
		singleAdja3.add(1);
		singleAdja3.add(7);
		singleAdja4.add(0);
		singleAdja4.add(4);
		singleAdja5.add(3);
		singleAdja5.add(5);
		singleAdja6.add(4);
		singleAdja6.add(6);
		singleAdja7.add(5);
		singleAdja7.add(7);
		singleAdja8.add(2);
		singleAdja8.add(6);

		// "areas": ["Meadow", "Road", "Meadow", "Road", "Meadow", "Road",
		// "Meadow", "Road"],

		return getJsonCard(CardAreaType.MEADOW, CardAreaType.ROAD,
				CardAreaType.MEADOW, CardAreaType.ROAD, CardAreaType.MEADOW,
				CardAreaType.ROAD, CardAreaType.MEADOW, CardAreaType.ROAD,
				singleEdge2, singleEdge3, singleEdge4, singleEdge1,
				singleAdja1, singleAdja2, singleAdja3, singleEdge4,
				singleAdja5, singleAdja6, singleAdja7, singleAdja8, -1);
	}

	public JSONObject createA_2() {

		// "edges" : [[0], [0], [0,1,0], [0]]
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge2.add(0);
		singleEdge3.add(0);
		singleEdge3.add(1);
		singleEdge3.add(0);
		singleEdge4.add(0);

		// "adjacency" : [[1,2], [0,2], [0,1]],
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();
		ArrayList<Integer> singleAdja3 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja1.add(2);

		singleAdja2.add(0);
		singleAdja2.add(2);

		singleAdja3.add(0);
		singleAdja3.add(1);

		// "areas" : ["Meadow", "Road", "Cloister"],
		return getJsonCard(CardAreaType.MEADOW, CardAreaType.ROAD,
				CardAreaType.CLOISTER, singleEdge3, singleEdge4, singleEdge1,
				singleEdge2, singleAdja1, singleAdja2, singleAdja3, -1);

	}

	public JSONObject createB_2() {
		// "edges": [[0], [0], [0], [0]]
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge2.add(0);
		singleEdge3.add(0);
		singleEdge4.add(0);

		// "adjacency": [[1], [0]]
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja2.add(0);

		// "areas": ["Meadow", "Cloister"],
		return getJsonCard(CardAreaType.MEADOW, CardAreaType.CLOISTER,
				singleEdge3, singleEdge4, singleEdge1, singleEdge2,
				singleAdja1, singleAdja2, -1);

	}

	public JSONObject createC_2() {

		// "edges": [[0], [0], [0], [0]]
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge2.add(0);
		singleEdge3.add(0);
		singleEdge4.add(0);

		// "areas": ["TOWN"],
		// "bonus" : [0]
		return getJsonCard(CardAreaType.TOWN, singleEdge3, singleEdge4,
				singleEdge1, singleEdge2, 0);

	}

	public JSONObject createD_2() {
		// "edges": [[0,1,2],[0],[2,1,0],[3]]
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge1.add(1);
		singleEdge1.add(2);

		singleEdge2.add(0);

		singleEdge3.add(2);
		singleEdge3.add(1);
		singleEdge3.add(0);

		singleEdge4.add(3);

		// "adjacency": [[1], [0,2], [1,3], [2]]
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();
		ArrayList<Integer> singleAdja3 = new ArrayList<>();
		ArrayList<Integer> singleAdja4 = new ArrayList<>();

		singleAdja1.add(1);

		singleAdja2.add(0);
		singleAdja2.add(2);

		singleAdja3.add(1);
		singleAdja3.add(3);

		singleAdja4.add(2);

		// "areas": ["Meadow", "Road", "Meadow", "Town"]
		return getJsonCard(CardAreaType.MEADOW, CardAreaType.ROAD,
				CardAreaType.MEADOW, CardAreaType.TOWN, singleEdge3,
				singleEdge4, singleEdge1, singleEdge2, singleAdja1,
				singleAdja2, singleAdja3, singleAdja4, -1);

	}

	public JSONObject createE_2() {
		// "edges": [[1], [0], [0], [0]]
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(1);
		singleEdge2.add(0);
		singleEdge3.add(0);
		singleEdge4.add(0);

		// "adjacency": [[1], [0]]
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja2.add(0);

		// "areas": ["Meadow", "TOWN"],
		return getJsonCard(CardAreaType.MEADOW, CardAreaType.TOWN, singleEdge3,
				singleEdge4, singleEdge1, singleEdge2, singleAdja1,
				singleAdja2, -1);

	}

	public JSONObject createF_2() {

		// "edges": [[0], [1], [2], [1]],
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge2.add(1);
		singleEdge3.add(2);
		singleEdge4.add(1);

		// "adjacency":[[1], [0,2], [1]],
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();
		ArrayList<Integer> singleAdja3 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja2.add(0);
		singleAdja2.add(2);
		singleAdja3.add(1);

		// "areas": ["Meadow", "Town", "Meadow"],
		// "bonus":[1]
		return getJsonCard(CardAreaType.MEADOW, CardAreaType.TOWN,
				CardAreaType.MEADOW, singleEdge3, singleEdge4, singleEdge1,
				singleEdge2, singleAdja1, singleAdja2, singleAdja3, 1);

	}

	public JSONObject createG_2() {
		// "edges": [[1], [0], [1], [2]],
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(1);
		singleEdge2.add(0);
		singleEdge3.add(1);
		singleEdge4.add(2);

		// "adjacency": [[1], [0,2], [1]]
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();
		ArrayList<Integer> singleAdja3 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja2.add(0);
		singleAdja2.add(2);
		singleAdja3.add(1);

		// "areas": ["Meadow", "Town", "Meadow"],
		return getJsonCard(CardAreaType.MEADOW, CardAreaType.TOWN,
				CardAreaType.MEADOW, singleEdge3, singleEdge4, singleEdge1,
				singleEdge2, singleAdja1, singleAdja2, singleAdja3, -1);

	}

	public JSONObject createH_2() {
		// edges": [[0],[1], [0], [2]],
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge2.add(1);
		singleEdge3.add(0);
		singleEdge4.add(2);

		// "adjacency": [[1,2], [0], [0]]
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();
		ArrayList<Integer> singleAdja3 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja1.add(2);
		singleAdja2.add(0);
		singleAdja3.add(0);

		// "areas": ["Meadow", "Town", "TOWN"],
		return getJsonCard(CardAreaType.MEADOW, CardAreaType.TOWN,
				CardAreaType.TOWN, singleEdge3, singleEdge4, singleEdge1,
				singleEdge2, singleAdja1, singleAdja2, singleAdja3, -1);
	}

	public JSONObject createI_2() {
		// "edges": [[0], [0], [1], [2]],
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge2.add(0);
		singleEdge3.add(1);
		singleEdge4.add(2);

		// "adjacency":[[1,2], [0], [0]]
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();
		ArrayList<Integer> singleAdja3 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja1.add(2);
		singleAdja2.add(0);
		singleAdja3.add(0);

		// "areas": ["Meadow", "Town", "Town"],
		return getJsonCard(CardAreaType.MEADOW, CardAreaType.TOWN,
				CardAreaType.TOWN, singleEdge3, singleEdge4, singleEdge1,
				singleEdge2, singleAdja1, singleAdja2, singleAdja3, -1);
	}

	public JSONObject createJ_2() {
		// "edges": [[0], [1], [3,2,1], [1,2,3]],
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge2.add(1);
		singleEdge3.add(3);
		singleEdge3.add(2);
		singleEdge3.add(1);
		singleEdge4.add(1);
		singleEdge4.add(2);
		singleEdge4.add(3);

		// "adjacency": [[1], [0,2], [1,3], [2]]
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();
		ArrayList<Integer> singleAdja3 = new ArrayList<>();
		ArrayList<Integer> singleAdja4 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja2.add(0);
		singleAdja2.add(2);
		singleAdja3.add(1);
		singleAdja3.add(3);
		singleAdja4.add(2);

		// "areas": ["Town", "Meadow", "Road", "Meadow"]
		return getJsonCard(CardAreaType.TOWN, CardAreaType.MEADOW,
				CardAreaType.ROAD, CardAreaType.MEADOW, singleEdge3,
				singleEdge4, singleEdge1, singleEdge2, singleAdja1,
				singleAdja2, singleAdja3, singleAdja4, -1);
	}

	public JSONObject createK_2() {
		// edges [[0,1,2], [2,1,0], [2], [3]]
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge1.add(1);
		singleEdge1.add(2);
		singleEdge2.add(2);
		singleEdge2.add(1);
		singleEdge2.add(0);
		singleEdge3.add(2);
		singleEdge4.add(3);

		// "adjacency": [[1], [0,2], [1,3], [2]]
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();
		ArrayList<Integer> singleAdja3 = new ArrayList<>();
		ArrayList<Integer> singleAdja4 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja2.add(0);
		singleAdja2.add(2);
		singleAdja3.add(1);
		singleAdja3.add(3);
		singleAdja4.add(2);

		// "areas": ["Meadow", "Road", "Meadow", "Town"]
		return getJsonCard(CardAreaType.MEADOW, CardAreaType.ROAD,
				CardAreaType.MEADOW, CardAreaType.TOWN, singleEdge3,
				singleEdge4, singleEdge1, singleEdge2, singleAdja1,
				singleAdja2, singleAdja3, singleAdja4, -1);
	}

	public JSONObject createL_2() {
		// "edges": [[0,1,2], [4,3,0], [2,5,4], [6]]
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
		singleEdge3.add(2);
		singleEdge3.add(5);
		singleEdge3.add(4);
		singleEdge4.add(6);

		// "adjacency": [[1,3], [0,2], [1,5,6], [0,4], [3,5], [4,2], [2]]
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();
		ArrayList<Integer> singleAdja3 = new ArrayList<>();
		ArrayList<Integer> singleAdja4 = new ArrayList<>();
		ArrayList<Integer> singleAdja5 = new ArrayList<>();
		ArrayList<Integer> singleAdja6 = new ArrayList<>();
		ArrayList<Integer> singleAdja7 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja1.add(3);
		singleAdja2.add(0);
		singleAdja2.add(2);
		singleAdja3.add(1);
		singleAdja3.add(5);
		singleAdja3.add(6);
		singleAdja4.add(0);
		singleAdja4.add(4);
		singleAdja5.add(3);
		singleAdja5.add(5);
		singleAdja6.add(4);
		singleAdja6.add(2);
		singleAdja7.add(2);

		// "areas" : ["Meadow", "Road", "Meadow", "Road", "Meadow", "Road",
		// "Town"]
		return getJsonCard(CardAreaType.MEADOW, CardAreaType.ROAD,
				CardAreaType.MEADOW, CardAreaType.ROAD, CardAreaType.MEADOW,
				CardAreaType.ROAD, CardAreaType.TOWN, singleEdge3, singleEdge4,
				singleEdge1, singleEdge2, singleAdja1, singleAdja2,
				singleAdja3, singleAdja4, singleAdja5, singleAdja6,
				singleAdja7, -1);
	}

	public JSONObject createM_2() {
		// "edges": [[0], [0], [1], [1]],
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge2.add(0);
		singleEdge3.add(1);
		singleEdge4.add(1);

		// "adjacency": [[1], [0]],
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja2.add(0);

		// "areas": ["Town", "Meadow"],
		// "bonus": [0]
		return getJsonCard(CardAreaType.TOWN, CardAreaType.MEADOW, singleEdge3,
				singleEdge4, singleEdge2, singleEdge1, singleAdja1,
				singleAdja2, 0);
	}

	public JSONObject createN_2() {
		// "edges": [[0], [0], [1], [1]],
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge2.add(0);
		singleEdge3.add(1);
		singleEdge4.add(1);

		// "adjacency": [[1], [0]],
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja2.add(0);

		// "areas": ["Town", "Meadow"],

		return getJsonCard(CardAreaType.TOWN, CardAreaType.MEADOW, singleEdge3,
				singleEdge4, singleEdge1, singleEdge2, singleAdja1,
				singleAdja2, -1);
	}

	public JSONObject createO_2() {
		// "edges": [[0], [0], [3,2,1], [1,2,3]],
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge2.add(0);
		singleEdge3.add(3);
		singleEdge3.add(2);
		singleEdge3.add(1);
		singleEdge4.add(1);
		singleEdge4.add(2);
		singleEdge4.add(3);

		// "adjacency": [[1], [0,2], [1,3], [2]],
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();
		ArrayList<Integer> singleAdja3 = new ArrayList<>();
		ArrayList<Integer> singleAdja4 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja2.add(0);
		singleAdja2.add(2);
		singleAdja3.add(1);
		singleAdja3.add(3);
		singleAdja4.add(2);

		// "areas": ["Town", "Meadow", "Road", "Meadow"]
		// "bonus" : [0]
		return getJsonCard(CardAreaType.TOWN, CardAreaType.MEADOW,
				CardAreaType.ROAD, CardAreaType.MEADOW, singleEdge3,
				singleEdge4, singleEdge2, singleEdge1, singleAdja1,
				singleAdja2, singleAdja3, singleAdja4, 0);
	}

	public JSONObject createP_2() {
		// "edges": [[0], [0], [3,2,1], [1,2,3]],
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge2.add(0);
		singleEdge3.add(3);
		singleEdge3.add(2);
		singleEdge3.add(1);
		singleEdge4.add(1);
		singleEdge4.add(2);
		singleEdge4.add(3);

		// "adjacency": [[1], [0,2], [1,3], [2]],
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();
		ArrayList<Integer> singleAdja3 = new ArrayList<>();
		ArrayList<Integer> singleAdja4 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja2.add(0);
		singleAdja2.add(2);
		singleAdja3.add(1);
		singleAdja3.add(3);
		singleAdja4.add(2);

		// "areas": ["Town", "Meadow", "Road", "Meadow"],

		return getJsonCard(CardAreaType.TOWN, CardAreaType.MEADOW,
				CardAreaType.ROAD, CardAreaType.MEADOW, singleEdge3,
				singleEdge4, singleEdge1, singleEdge2, singleAdja1,
				singleAdja2, singleAdja3, singleAdja4, -1);
	}

	public JSONObject createQ_2() {
		// "edges": [[0], [0], [1], [0]],
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge2.add(0);
		singleEdge3.add(1);
		singleEdge4.add(0);

		// "adjacency": [[1], [0]],
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja2.add(0);

		// "areas": ["Town", "Meadow"],
		// "bonus":[0]
		return getJsonCard(CardAreaType.TOWN, CardAreaType.MEADOW, singleEdge3,
				singleEdge4, singleEdge1, singleEdge2, singleAdja1,
				singleAdja2, 0);
	}

	public JSONObject createR_2() {
		// "edges": [[0], [0], [1], [0]],
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge2.add(0);
		singleEdge3.add(1);
		singleEdge4.add(0);

		// "adjacency": [[1], [0]],
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja2.add(0);

		// "areas": ["Town", "Meadow"],

		return getJsonCard(CardAreaType.TOWN, CardAreaType.MEADOW, singleEdge3,
				singleEdge4, singleEdge1, singleEdge2, singleAdja1,
				singleAdja2, -1);
	}

	public JSONObject createS_2() {
		// "edges": [[0], [0], [3,2,1], [0]],
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge2.add(0);
		singleEdge3.add(3);
		singleEdge3.add(2);
		singleEdge3.add(1);
		singleEdge4.add(0);

		// "adjacency": [[1,2,3], [0,2], [0,1,3], [0,2]],
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();
		ArrayList<Integer> singleAdja3 = new ArrayList<>();
		ArrayList<Integer> singleAdja4 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja1.add(2);
		singleAdja1.add(3);
		singleAdja2.add(0);
		singleAdja2.add(2);
		singleAdja3.add(0);
		singleAdja3.add(1);
		singleAdja3.add(3);
		singleAdja4.add(0);
		singleAdja4.add(2);

		// "areas": ["Town", "Meadow", "Road", "Meadow"],
		// "bonus": [0]
		return getJsonCard(CardAreaType.TOWN, CardAreaType.MEADOW,
				CardAreaType.ROAD, CardAreaType.MEADOW, singleEdge3,
				singleEdge4, singleEdge1, singleEdge2, singleAdja1,
				singleAdja2, singleAdja3, singleAdja4, 0);
	}

	public JSONObject createT_2() {
		// "edges": [[0], [0], [3,2,1], [0]],
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge2.add(0);
		singleEdge3.add(3);
		singleEdge3.add(2);
		singleEdge3.add(1);
		singleEdge4.add(0);

		// "adjacency": [[1,2,3], [0,2], [0,1,3], [0,2]],
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();
		ArrayList<Integer> singleAdja3 = new ArrayList<>();
		ArrayList<Integer> singleAdja4 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja1.add(2);
		singleAdja1.add(3);
		singleAdja2.add(0);
		singleAdja2.add(2);
		singleAdja3.add(0);
		singleAdja3.add(1);
		singleAdja3.add(3);
		singleAdja4.add(0);
		singleAdja4.add(2);

		// "areas": ["Town", "Meadow", "Road", "Meadow"],

		return getJsonCard(CardAreaType.TOWN, CardAreaType.MEADOW,
				CardAreaType.ROAD, CardAreaType.MEADOW, singleEdge3,
				singleEdge4, singleEdge1, singleEdge2, singleAdja1,
				singleAdja2, singleAdja3, singleAdja4, -1);
	}

	public JSONObject createU_2() {
		// "edges": [[0,1,2], [0], [2,1,0], [2]],
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge1.add(1);
		singleEdge1.add(2);
		singleEdge2.add(0);
		singleEdge3.add(2);
		singleEdge3.add(1);
		singleEdge3.add(0);
		singleEdge4.add(2);

		// "adjacency": [[1], [0,2], [1]]
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();
		ArrayList<Integer> singleAdja3 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja2.add(0);
		singleAdja2.add(2);
		singleAdja3.add(1);

		// "areas": ["Meadow", "Road", "Meadow"],

		return getJsonCard(CardAreaType.MEADOW, CardAreaType.ROAD,
				CardAreaType.MEADOW, singleEdge3, singleEdge4, singleEdge1,
				singleEdge2, singleAdja1, singleAdja2, singleAdja3, -1);
	}

	public JSONObject createV_2() {
		// "edges": [[0], [2,1,0], [0,1,2], [0]],
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge2.add(2);
		singleEdge2.add(1);
		singleEdge2.add(0);
		singleEdge3.add(0);
		singleEdge3.add(1);
		singleEdge3.add(2);
		singleEdge4.add(0);

		// "adjacency": [[1], [0,2], [1]]
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();
		ArrayList<Integer> singleAdja3 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja2.add(0);
		singleAdja2.add(2);
		singleAdja3.add(1);

		// "areas": ["Meadow", "Road", "Meadow"],

		return getJsonCard(CardAreaType.MEADOW, CardAreaType.ROAD,
				CardAreaType.MEADOW, singleEdge3, singleEdge4, singleEdge1,
				singleEdge2, singleAdja1, singleAdja2, singleAdja3, -1);
	}

	public JSONObject createW_2() {
		// "edges": [[0], [2,1,0], [4,3,2], [0,5,4]],
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge2.add(2);
		singleEdge2.add(1);
		singleEdge2.add(0);
		singleEdge3.add(4);
		singleEdge3.add(3);
		singleEdge3.add(2);
		singleEdge4.add(0);
		singleEdge4.add(5);
		singleEdge4.add(4);

		// "adjacency": [[1,3], [0,2], [1,4], [2,4], [3,5], [0,4]]
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();
		ArrayList<Integer> singleAdja3 = new ArrayList<>();
		ArrayList<Integer> singleAdja4 = new ArrayList<>();
		ArrayList<Integer> singleAdja5 = new ArrayList<>();
		ArrayList<Integer> singleAdja6 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja1.add(3);
		singleAdja2.add(0);
		singleAdja2.add(2);
		singleAdja3.add(1);
		singleAdja3.add(4);
		singleAdja4.add(2);
		singleAdja4.add(4);
		singleAdja5.add(3);
		singleAdja5.add(5);
		singleAdja6.add(0);
		singleAdja6.add(4);

		// "areas": ["Meadow","Road", "Meadow", "Road", "Meadow", "Road"],

		return getJsonCard(CardAreaType.MEADOW, CardAreaType.ROAD,
				CardAreaType.MEADOW, CardAreaType.ROAD, CardAreaType.MEADOW,
				CardAreaType.ROAD, singleEdge3, singleEdge4, singleEdge1,
				singleEdge2, singleAdja1, singleAdja2, singleAdja3,
				singleEdge4, singleAdja5, singleAdja6, -1);
	}

	public JSONObject createX_2() {
		// "edges": [[0,1,2], [4,3,0], [6,5,4], [2,7,6]],
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

		// "adjacency": [[1,3], [0,2], [1,7], [0,4], [3,5], [4,6], [5,7], [2,6]]
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();
		ArrayList<Integer> singleAdja3 = new ArrayList<>();
		ArrayList<Integer> singleAdja4 = new ArrayList<>();
		ArrayList<Integer> singleAdja5 = new ArrayList<>();
		ArrayList<Integer> singleAdja6 = new ArrayList<>();
		ArrayList<Integer> singleAdja7 = new ArrayList<>();
		ArrayList<Integer> singleAdja8 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja1.add(3);
		singleAdja2.add(0);
		singleAdja2.add(2);
		singleAdja3.add(1);
		singleAdja3.add(7);
		singleAdja4.add(0);
		singleAdja4.add(4);
		singleAdja5.add(3);
		singleAdja5.add(5);
		singleAdja6.add(4);
		singleAdja6.add(6);
		singleAdja7.add(5);
		singleAdja7.add(7);
		singleAdja8.add(2);
		singleAdja8.add(6);

		// "areas": ["Meadow", "Road", "Meadow", "Road", "Meadow", "Road",
		// "Meadow", "Road"],

		return getJsonCard(CardAreaType.MEADOW, CardAreaType.ROAD,
				CardAreaType.MEADOW, CardAreaType.ROAD, CardAreaType.MEADOW,
				CardAreaType.ROAD, CardAreaType.MEADOW, CardAreaType.ROAD,
				singleEdge3, singleEdge4, singleEdge1, singleEdge2,
				singleAdja1, singleAdja2, singleAdja3, singleEdge4,
				singleAdja5, singleAdja6, singleAdja7, singleAdja8, -1);
	}

	public JSONObject createA_3() {

		// "edges" : [[0], [0,1,0], [0], [0]]
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge2.add(0);
		singleEdge3.add(0);
		singleEdge3.add(1);
		singleEdge3.add(0);
		singleEdge4.add(0);

		// "adjacency" : [[1,2], [0,2], [0,1]],
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();
		ArrayList<Integer> singleAdja3 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja1.add(2);

		singleAdja2.add(0);
		singleAdja2.add(2);

		singleAdja3.add(0);
		singleAdja3.add(1);

		// "areas" : ["Meadow", "Road", "Cloister"],
		return getJsonCard(CardAreaType.MEADOW, CardAreaType.ROAD,
				CardAreaType.CLOISTER, singleEdge4, singleEdge1, singleEdge2,
				singleEdge3, singleAdja1, singleAdja2, singleAdja3, -1);

	}

	public JSONObject createB_3() {
		// "edges": [[0], [0], [0], [0]]
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge2.add(0);
		singleEdge3.add(0);
		singleEdge4.add(0);

		// "adjacency": [[1], [0]]
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja2.add(0);

		// "areas": ["Meadow", "Cloister"],
		return getJsonCard(CardAreaType.MEADOW, CardAreaType.CLOISTER,
				singleEdge4, singleEdge1, singleEdge2, singleEdge3,
				singleAdja1, singleAdja2, -1);

	}

	public JSONObject createC_3() {

		// "edges": [[0], [0], [0], [0]]
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge2.add(0);
		singleEdge3.add(0);
		singleEdge4.add(0);

		// "areas": ["TOWN"],
		// "bonus" : [0]
		return getJsonCard(CardAreaType.TOWN, singleEdge4, singleEdge1,
				singleEdge2, singleEdge3, 0);

	}

	public JSONObject createD_3() {
		// "edges": [[0],[2,1,0],[3],[0,1,2]]
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge1.add(1);
		singleEdge1.add(2);

		singleEdge2.add(0);

		singleEdge3.add(2);
		singleEdge3.add(1);
		singleEdge3.add(0);

		singleEdge4.add(3);

		// "adjacency": [[1], [0,2], [1,3], [2]]
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();
		ArrayList<Integer> singleAdja3 = new ArrayList<>();
		ArrayList<Integer> singleAdja4 = new ArrayList<>();

		singleAdja1.add(1);

		singleAdja2.add(0);
		singleAdja2.add(2);

		singleAdja3.add(1);
		singleAdja3.add(3);

		singleAdja4.add(2);

		// "areas": ["Meadow", "Road", "Meadow", "Town"]
		return getJsonCard(CardAreaType.MEADOW, CardAreaType.ROAD,
				CardAreaType.MEADOW, CardAreaType.TOWN, singleEdge4,
				singleEdge1, singleEdge2, singleEdge3, singleAdja1,
				singleAdja2, singleAdja3, singleAdja4, -1);

	}

	public JSONObject createE_3() {
		// "edges": [[0], [0], [0],[1]]
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(1);
		singleEdge2.add(0);
		singleEdge3.add(0);
		singleEdge4.add(0);

		// "adjacency": [[1], [0]]
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja2.add(0);

		// "areas": ["Meadow", "TOWN"],
		return getJsonCard(CardAreaType.MEADOW, CardAreaType.TOWN, singleEdge4,
				singleEdge1, singleEdge2, singleEdge3, singleAdja1,
				singleAdja2, -1);

	}

	public JSONObject createF_3() {

		// "edges": [[1], [2], [1], [0]],
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge2.add(1);
		singleEdge3.add(2);
		singleEdge4.add(1);

		// "adjacency":[[1], [0,2], [1]],
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();
		ArrayList<Integer> singleAdja3 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja2.add(0);
		singleAdja2.add(2);
		singleAdja3.add(1);

		// "areas": ["Meadow", "Town", "Meadow"],
		// "bonus":[1]
		return getJsonCard(CardAreaType.MEADOW, CardAreaType.TOWN,
				CardAreaType.MEADOW, singleEdge4, singleEdge1, singleEdge2,
				singleEdge3, singleAdja1, singleAdja2, singleAdja3, 1);

	}

	public JSONObject createG_3() {
		// "edges": [[0], [1], [2],[1]],
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(1);
		singleEdge2.add(0);
		singleEdge3.add(1);
		singleEdge4.add(2);

		// "adjacency": [[1], [0,2], [1]]
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();
		ArrayList<Integer> singleAdja3 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja2.add(0);
		singleAdja2.add(2);
		singleAdja3.add(1);

		// "areas": ["Meadow", "Town", "Meadow"],
		return getJsonCard(CardAreaType.MEADOW, CardAreaType.TOWN,
				CardAreaType.MEADOW, singleEdge4, singleEdge1, singleEdge2,
				singleEdge3, singleAdja1, singleAdja2, singleAdja3, -1);

	}

	public JSONObject createH_3() {
		// edges": [[1], [0], [2][0]],
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge2.add(1);
		singleEdge3.add(0);
		singleEdge4.add(2);

		// "adjacency": [[1,2], [0], [0]]
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();
		ArrayList<Integer> singleAdja3 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja1.add(2);
		singleAdja2.add(0);
		singleAdja3.add(0);

		// "areas": ["Meadow", "Town", "TOWN"],
		return getJsonCard(CardAreaType.MEADOW, CardAreaType.TOWN,
				CardAreaType.TOWN, singleEdge4, singleEdge1, singleEdge2,
				singleEdge3, singleAdja1, singleAdja2, singleAdja3, -1);
	}

	public JSONObject createI_3() {
		// "edges": [[0], [0], [1], [2]],
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge2.add(0);
		singleEdge3.add(1);
		singleEdge4.add(2);

		// "adjacency":[[1,2], [0], [0]]
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();
		ArrayList<Integer> singleAdja3 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja1.add(2);
		singleAdja2.add(0);
		singleAdja3.add(0);

		// "areas": ["Meadow", "Town", "Town"],
		return getJsonCard(CardAreaType.MEADOW, CardAreaType.TOWN,
				CardAreaType.TOWN, singleEdge4, singleEdge1, singleEdge2,
				singleEdge3, singleAdja1, singleAdja2, singleAdja3, -1);
	}

	public JSONObject createJ_3() {
		// "edges": [[0], [1], [3,2,1], [1,2,3]],
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge2.add(1);
		singleEdge3.add(3);
		singleEdge3.add(2);
		singleEdge3.add(1);
		singleEdge4.add(1);
		singleEdge4.add(2);
		singleEdge4.add(3);

		// "adjacency": [[1], [0,2], [1,3], [2]]
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();
		ArrayList<Integer> singleAdja3 = new ArrayList<>();
		ArrayList<Integer> singleAdja4 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja2.add(0);
		singleAdja2.add(2);
		singleAdja3.add(1);
		singleAdja3.add(3);
		singleAdja4.add(2);

		// "areas": ["Town", "Meadow", "Road", "Meadow"]
		return getJsonCard(CardAreaType.TOWN, CardAreaType.MEADOW,
				CardAreaType.ROAD, CardAreaType.MEADOW, singleEdge4,
				singleEdge1, singleEdge2, singleEdge3, singleAdja1,
				singleAdja2, singleAdja3, singleAdja4, -1);
	}

	public JSONObject createK_3() {
		// edges [[0,1,2], [2,1,0], [2], [3]]
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge1.add(1);
		singleEdge1.add(2);
		singleEdge2.add(2);
		singleEdge2.add(1);
		singleEdge2.add(0);
		singleEdge3.add(2);
		singleEdge4.add(3);

		// "adjacency": [[1], [0,2], [1,3], [2]]
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();
		ArrayList<Integer> singleAdja3 = new ArrayList<>();
		ArrayList<Integer> singleAdja4 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja2.add(0);
		singleAdja2.add(2);
		singleAdja3.add(1);
		singleAdja3.add(3);
		singleAdja4.add(2);

		// "areas": ["Meadow", "Road", "Meadow", "Town"]
		return getJsonCard(CardAreaType.MEADOW, CardAreaType.ROAD,
				CardAreaType.MEADOW, CardAreaType.TOWN, singleEdge4,
				singleEdge1, singleEdge2, singleEdge3, singleAdja1,
				singleAdja2, singleAdja3, singleAdja4, -1);
	}

	public JSONObject createL_3() {
		// "edges": [[0,1,2], [4,3,0], [2,5,4], [6]]
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
		singleEdge3.add(2);
		singleEdge3.add(5);
		singleEdge3.add(4);
		singleEdge4.add(6);

		// "adjacency": [[1,3], [0,2], [1,5,6], [0,4], [3,5], [4,2], [2]]
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();
		ArrayList<Integer> singleAdja3 = new ArrayList<>();
		ArrayList<Integer> singleAdja4 = new ArrayList<>();
		ArrayList<Integer> singleAdja5 = new ArrayList<>();
		ArrayList<Integer> singleAdja6 = new ArrayList<>();
		ArrayList<Integer> singleAdja7 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja1.add(3);
		singleAdja2.add(0);
		singleAdja2.add(2);
		singleAdja3.add(1);
		singleAdja3.add(5);
		singleAdja3.add(6);
		singleAdja4.add(0);
		singleAdja4.add(4);
		singleAdja5.add(3);
		singleAdja5.add(5);
		singleAdja6.add(4);
		singleAdja6.add(2);
		singleAdja7.add(2);

		// "areas" : ["Meadow", "Road", "Meadow", "Road", "Meadow", "Road",
		// "Town"]
		return getJsonCard(CardAreaType.MEADOW, CardAreaType.ROAD,
				CardAreaType.MEADOW, CardAreaType.ROAD, CardAreaType.MEADOW,
				CardAreaType.ROAD, CardAreaType.TOWN, singleEdge4, singleEdge1,
				singleEdge2, singleEdge3, singleAdja1, singleAdja2,
				singleAdja3, singleAdja4, singleAdja5, singleAdja6,
				singleAdja7, -1);
	}

	public JSONObject createM_3() {
		// "edges": [[0], [0], [1], [1]],
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge2.add(0);
		singleEdge3.add(1);
		singleEdge4.add(1);

		// "adjacency": [[1], [0]],
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja2.add(0);

		// "areas": ["Town", "Meadow"],
		// "bonus": [0]
		return getJsonCard(CardAreaType.TOWN, CardAreaType.MEADOW, singleEdge4,
				singleEdge2, singleEdge3, singleEdge1, singleAdja1,
				singleAdja2, 0);
	}

	public JSONObject createN_3() {
		// "edges": [[0], [0], [1], [1]],
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge2.add(0);
		singleEdge3.add(1);
		singleEdge4.add(1);

		// "adjacency": [[1], [0]],
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja2.add(0);

		// "areas": ["Town", "Meadow"],

		return getJsonCard(CardAreaType.TOWN, CardAreaType.MEADOW, singleEdge4,
				singleEdge1, singleEdge2, singleEdge3, singleAdja1,
				singleAdja2, -1);
	}

	public JSONObject createO_3() {
		// "edges": [[0], [0], [3,2,1], [1,2,3]],
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge2.add(0);
		singleEdge3.add(3);
		singleEdge3.add(2);
		singleEdge3.add(1);
		singleEdge4.add(1);
		singleEdge4.add(2);
		singleEdge4.add(3);

		// "adjacency": [[1], [0,2], [1,3], [2]],
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();
		ArrayList<Integer> singleAdja3 = new ArrayList<>();
		ArrayList<Integer> singleAdja4 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja2.add(0);
		singleAdja2.add(2);
		singleAdja3.add(1);
		singleAdja3.add(3);
		singleAdja4.add(2);

		// "areas": ["Town", "Meadow", "Road", "Meadow"]
		// "bonus" : [0]
		return getJsonCard(CardAreaType.TOWN, CardAreaType.MEADOW,
				CardAreaType.ROAD, CardAreaType.MEADOW, singleEdge4,
				singleEdge1, singleEdge2, singleEdge3, singleAdja1,
				singleAdja2, singleAdja3, singleAdja4, 0);
	}

	public JSONObject createP_3() {
		// "edges": [[0], [0], [3,2,1], [1,2,3]],
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge2.add(0);
		singleEdge3.add(3);
		singleEdge3.add(2);
		singleEdge3.add(1);
		singleEdge4.add(1);
		singleEdge4.add(2);
		singleEdge4.add(3);

		// "adjacency": [[1], [0,2], [1,3], [2]],
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();
		ArrayList<Integer> singleAdja3 = new ArrayList<>();
		ArrayList<Integer> singleAdja4 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja2.add(0);
		singleAdja2.add(2);
		singleAdja3.add(1);
		singleAdja3.add(3);
		singleAdja4.add(2);

		// "areas": ["Town", "Meadow", "Road", "Meadow"],

		return getJsonCard(CardAreaType.TOWN, CardAreaType.MEADOW,
				CardAreaType.ROAD, CardAreaType.MEADOW, singleEdge4,
				singleEdge1, singleEdge2, singleEdge3, singleAdja1,
				singleAdja2, singleAdja3, singleAdja4, -1);
	}

	public JSONObject createQ_3() {
		// "edges": [[0], [0], [1], [0]],
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge2.add(0);
		singleEdge3.add(1);
		singleEdge4.add(0);

		// "adjacency": [[1], [0]],
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja2.add(0);

		// "areas": ["Town", "Meadow"],
		// "bonus":[0]
		return getJsonCard(CardAreaType.TOWN, CardAreaType.MEADOW, singleEdge4,
				singleEdge1, singleEdge2, singleEdge3, singleAdja1,
				singleAdja2, 0);
	}

	public JSONObject createR_3() {
		// "edges": [[0], [0], [1], [0]],
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge2.add(0);
		singleEdge3.add(1);
		singleEdge4.add(0);

		// "adjacency": [[1], [0]],
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja2.add(0);

		// "areas": ["Town", "Meadow"],

		return getJsonCard(CardAreaType.TOWN, CardAreaType.MEADOW, singleEdge4,
				singleEdge1, singleEdge2, singleEdge3, singleAdja1,
				singleAdja2, -1);
	}

	public JSONObject createS_3() {
		// "edges": [[0], [0], [3,2,1], [0]],
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge2.add(0);
		singleEdge3.add(3);
		singleEdge3.add(2);
		singleEdge3.add(1);
		singleEdge4.add(0);

		// "adjacency": [[1,2,3], [0,2], [0,1,3], [0,2]],
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();
		ArrayList<Integer> singleAdja3 = new ArrayList<>();
		ArrayList<Integer> singleAdja4 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja1.add(2);
		singleAdja1.add(3);
		singleAdja2.add(0);
		singleAdja2.add(2);
		singleAdja3.add(0);
		singleAdja3.add(1);
		singleAdja3.add(3);
		singleAdja4.add(0);
		singleAdja4.add(2);

		// "areas": ["Town", "Meadow", "Road", "Meadow"],
		// "bonus": [0]
		return getJsonCard(CardAreaType.TOWN, CardAreaType.MEADOW,
				CardAreaType.ROAD, CardAreaType.MEADOW, singleEdge4,
				singleEdge1, singleEdge2, singleEdge3, singleAdja1,
				singleAdja2, singleAdja3, singleAdja4, 0);
	}

	public JSONObject createT_3() {
		// "edges": [[0], [0], [3,2,1], [0]],
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge2.add(0);
		singleEdge3.add(3);
		singleEdge3.add(2);
		singleEdge3.add(1);
		singleEdge4.add(0);

		// "adjacency": [[1,2,3], [0,2], [0,1,3], [0,2]],
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();
		ArrayList<Integer> singleAdja3 = new ArrayList<>();
		ArrayList<Integer> singleAdja4 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja1.add(2);
		singleAdja1.add(3);
		singleAdja2.add(0);
		singleAdja2.add(2);
		singleAdja3.add(0);
		singleAdja3.add(1);
		singleAdja3.add(3);
		singleAdja4.add(0);
		singleAdja4.add(2);

		// "areas": ["Town", "Meadow", "Road", "Meadow"],

		return getJsonCard(CardAreaType.TOWN, CardAreaType.MEADOW,
				CardAreaType.ROAD, CardAreaType.MEADOW, singleEdge4,
				singleEdge1, singleEdge2, singleEdge3, singleAdja1,
				singleAdja2, singleAdja3, singleAdja4, -1);
	}

	public JSONObject createU_3() {
		// "edges": [[0,1,2], [0], [2,1,0], [2]],
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge1.add(1);
		singleEdge1.add(2);
		singleEdge2.add(0);
		singleEdge3.add(2);
		singleEdge3.add(1);
		singleEdge3.add(0);
		singleEdge4.add(2);

		// "adjacency": [[1], [0,2], [1]]
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();
		ArrayList<Integer> singleAdja3 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja2.add(0);
		singleAdja2.add(2);
		singleAdja3.add(1);

		// "areas": ["Meadow", "Road", "Meadow"],

		return getJsonCard(CardAreaType.MEADOW, CardAreaType.ROAD,
				CardAreaType.MEADOW, singleEdge4, singleEdge1, singleEdge2,
				singleEdge3, singleAdja1, singleAdja2, singleAdja3, -1);
	}

	public JSONObject createV_3() {
		// "edges": [[0], [2,1,0], [0,1,2], [0]],
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge2.add(2);
		singleEdge2.add(1);
		singleEdge2.add(0);
		singleEdge3.add(0);
		singleEdge3.add(1);
		singleEdge3.add(2);
		singleEdge4.add(0);

		// "adjacency": [[1], [0,2], [1]]
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();
		ArrayList<Integer> singleAdja3 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja2.add(0);
		singleAdja2.add(2);
		singleAdja3.add(1);

		// "areas": ["Meadow", "Road", "Meadow"],

		return getJsonCard(CardAreaType.MEADOW, CardAreaType.ROAD,
				CardAreaType.MEADOW, singleEdge4, singleEdge1, singleEdge2,
				singleEdge3, singleAdja1, singleAdja2, singleAdja3, -1);
	}

	public JSONObject createW_3() {
		// "edges": [[0], [2,1,0], [4,3,2], [0,5,4]],
		ArrayList<Integer> singleEdge1 = new ArrayList<>();
		ArrayList<Integer> singleEdge2 = new ArrayList<>();
		ArrayList<Integer> singleEdge3 = new ArrayList<>();
		ArrayList<Integer> singleEdge4 = new ArrayList<>();

		singleEdge1.add(0);
		singleEdge2.add(2);
		singleEdge2.add(1);
		singleEdge2.add(0);
		singleEdge3.add(4);
		singleEdge3.add(3);
		singleEdge3.add(2);
		singleEdge4.add(0);
		singleEdge4.add(5);
		singleEdge4.add(4);

		// "adjacency": [[1,3], [0,2], [1,4], [2,4], [3,5], [0,4]]
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();
		ArrayList<Integer> singleAdja3 = new ArrayList<>();
		ArrayList<Integer> singleAdja4 = new ArrayList<>();
		ArrayList<Integer> singleAdja5 = new ArrayList<>();
		ArrayList<Integer> singleAdja6 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja1.add(3);
		singleAdja2.add(0);
		singleAdja2.add(2);
		singleAdja3.add(1);
		singleAdja3.add(4);
		singleAdja4.add(2);
		singleAdja4.add(4);
		singleAdja5.add(3);
		singleAdja5.add(5);
		singleAdja6.add(0);
		singleAdja6.add(4);

		// "areas": ["Meadow","Road", "Meadow", "Road", "Meadow", "Road"],

		return getJsonCard(CardAreaType.MEADOW, CardAreaType.ROAD,
				CardAreaType.MEADOW, CardAreaType.ROAD, CardAreaType.MEADOW,
				CardAreaType.ROAD, singleEdge4, singleEdge1, singleEdge2,
				singleEdge3, singleAdja1, singleAdja2, singleAdja3,
				singleEdge4, singleAdja5, singleAdja6, -1);
	}

	public JSONObject createX_3() {
		// "edges": [[0,1,2], [4,3,0], [6,5,4], [2,7,6]],
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

		// "adjacency": [[1,3], [0,2], [1,7], [0,4], [3,5], [4,6], [5,7], [2,6]]
		ArrayList<Integer> singleAdja1 = new ArrayList<>();
		ArrayList<Integer> singleAdja2 = new ArrayList<>();
		ArrayList<Integer> singleAdja3 = new ArrayList<>();
		ArrayList<Integer> singleAdja4 = new ArrayList<>();
		ArrayList<Integer> singleAdja5 = new ArrayList<>();
		ArrayList<Integer> singleAdja6 = new ArrayList<>();
		ArrayList<Integer> singleAdja7 = new ArrayList<>();
		ArrayList<Integer> singleAdja8 = new ArrayList<>();

		singleAdja1.add(1);
		singleAdja1.add(3);
		singleAdja2.add(0);
		singleAdja2.add(2);
		singleAdja3.add(1);
		singleAdja3.add(7);
		singleAdja4.add(0);
		singleAdja4.add(4);
		singleAdja5.add(3);
		singleAdja5.add(5);
		singleAdja6.add(4);
		singleAdja6.add(6);
		singleAdja7.add(5);
		singleAdja7.add(7);
		singleAdja8.add(2);
		singleAdja8.add(6);

		// "areas": ["Meadow", "Road", "Meadow", "Road", "Meadow", "Road",
		// "Meadow", "Road"],

		return getJsonCard(CardAreaType.MEADOW, CardAreaType.ROAD,
				CardAreaType.MEADOW, CardAreaType.ROAD, CardAreaType.MEADOW,
				CardAreaType.ROAD, CardAreaType.MEADOW, CardAreaType.ROAD,
				singleEdge4, singleEdge1, singleEdge2, singleEdge3,
				singleAdja1, singleAdja2, singleAdja3, singleEdge4,
				singleAdja5, singleAdja6, singleAdja7, singleAdja8, -1);

	}

	@Test
	public void getCardID_onCards() {

		JSONObject cardA = createA();
		JSONObject cardB = createB();
		JSONObject cardC = createC();
		JSONObject cardD = createD();
		JSONObject cardE = createE();
		JSONObject cardF = createF();
		JSONObject cardG = createG();
		JSONObject cardH = createH();
		JSONObject cardI = createI();
		JSONObject cardJ = createJ();
		JSONObject cardK = createK();
		JSONObject cardL = createL();
		JSONObject cardM = createM();
		JSONObject cardN = createN();
		JSONObject cardO = createO();
		JSONObject cardP = createP();
		JSONObject cardQ = createQ();
		JSONObject cardR = createR();
		JSONObject cardS = createS();
		JSONObject cardT = createT();
		JSONObject cardU = createU();
		JSONObject cardV = createV();
		JSONObject cardW = createW();
		JSONObject cardX = createX();

		String testIDfromA = CardReceiver.receiveCard(cardA);
		String testIDfromB = CardReceiver.receiveCard(cardB);
		String testIDfromC = CardReceiver.receiveCard(cardC);
		String testIDfromD = CardReceiver.receiveCard(cardD);
		String testIDfromE = CardReceiver.receiveCard(cardE);
		String testIDfromF = CardReceiver.receiveCard(cardF);
		String testIDfromG = CardReceiver.receiveCard(cardG);
		String testIDfromH = CardReceiver.receiveCard(cardH);
		String testIDfromI = CardReceiver.receiveCard(cardI);
		String testIDfromJ = CardReceiver.receiveCard(cardJ);
		String testIDfromK = CardReceiver.receiveCard(cardK);
		String testIDfromL = CardReceiver.receiveCard(cardL);
		String testIDfromM = CardReceiver.receiveCard(cardM);
		String testIDfromN = CardReceiver.receiveCard(cardN);
		String testIDfromO = CardReceiver.receiveCard(cardO);
		String testIDfromP = CardReceiver.receiveCard(cardP);
		String testIDfromQ = CardReceiver.receiveCard(cardQ);
		String testIDfromR = CardReceiver.receiveCard(cardR);
		String testIDfromS = CardReceiver.receiveCard(cardS);
		String testIDfromT = CardReceiver.receiveCard(cardT);
		String testIDfromU = CardReceiver.receiveCard(cardU);
		String testIDfromV = CardReceiver.receiveCard(cardV);
		String testIDfromW = CardReceiver.receiveCard(cardW);
		String testIDfromX = CardReceiver.receiveCard(cardX);

		assertEquals("A", testIDfromA);
		assertEquals("B", testIDfromB);
		assertEquals("C", testIDfromC);
		assertEquals("D", testIDfromD);
		assertEquals("E", testIDfromE);
		assertEquals("F", testIDfromF);
		assertEquals("G", testIDfromG);
		assertEquals("H", testIDfromH);
		assertEquals("I", testIDfromI);
		assertEquals("J", testIDfromJ);
		assertEquals("K", testIDfromK);
		assertEquals("L", testIDfromL);
		assertEquals("M", testIDfromM);
		assertEquals("N", testIDfromN);
		assertEquals("O", testIDfromO);
		assertEquals("P", testIDfromP);
		assertEquals("Q", testIDfromQ);
		assertEquals("R", testIDfromR);
		assertEquals("S", testIDfromS);
		assertEquals("T", testIDfromT);
		assertEquals("U", testIDfromU);
		assertEquals("V", testIDfromV);
		assertEquals("W", testIDfromW);
		assertEquals("X", testIDfromX);
	}

//	@Test
//	public void getRotation0_onCards() {
//
//		JSONObject cardA = createA();
//		JSONObject cardB = createB();
//		JSONObject cardC = createC();
//		JSONObject cardD = createD();
//		JSONObject cardE = createE();
//		JSONObject cardF = createF();
//		JSONObject cardG = createG();
//		JSONObject cardH = createH();
//		JSONObject cardI = createI();
//		JSONObject cardJ = createJ();
//		JSONObject cardK = createK();
//		JSONObject cardL = createL();
//		JSONObject cardM = createM();
//		JSONObject cardN = createN();
//		JSONObject cardO = createO();
//		JSONObject cardP = createP();
//		JSONObject cardQ = createQ();
//		JSONObject cardR = createR();
//		JSONObject cardS = createS();
//		JSONObject cardT = createT();
//		JSONObject cardU = createU();
//		JSONObject cardV = createV();
//		JSONObject cardW = createW();
//		JSONObject cardX = createX();
//
//		assertEquals(0, CardReceiver.getRotation("A", cardA));
//		assertEquals(0, CardReceiver.getRotation("B", cardB));
//		assertEquals(0, CardReceiver.getRotation("C", cardC));
//		assertEquals(0, CardReceiver.getRotation("D", cardD));
//		assertEquals(0, CardReceiver.getRotation("E", cardE));
//		assertEquals(0, CardReceiver.getRotation("F", cardF));
//		assertEquals(0, CardReceiver.getRotation("G", cardG));
//		assertEquals(0, CardReceiver.getRotation("H", cardH));
//		assertEquals(0, CardReceiver.getRotation("I", cardI));
//		assertEquals(0, CardReceiver.getRotation("J", cardJ));
//		assertEquals(0, CardReceiver.getRotation("K", cardK));
//		assertEquals(0, CardReceiver.getRotation("L", cardL));
//		assertEquals(0, CardReceiver.getRotation("M", cardM));
//		assertEquals(0, CardReceiver.getRotation("N", cardN));
//		assertEquals(0, CardReceiver.getRotation("O", cardO));
//		assertEquals(0, CardReceiver.getRotation("P", cardP));
//		assertEquals(0, CardReceiver.getRotation("Q", cardQ));
//		assertEquals(0, CardReceiver.getRotation("R", cardR));
//		assertEquals(0, CardReceiver.getRotation("S", cardS));
//		assertEquals(0, CardReceiver.getRotation("T", cardT));
//		assertEquals(0, CardReceiver.getRotation("U", cardU));
//		assertEquals(0, CardReceiver.getRotation("V", cardV));
//		assertEquals(0, CardReceiver.getRotation("W", cardW));
//		assertEquals(0, CardReceiver.getRotation("X", cardX));
//
//	}
//
//	@Test
//	public void getRotation1_onCards() {
//
//		JSONObject cardA = createA_1();
//		// JSONObject cardB = createB_1();
//		// JSONObject cardC = createC_1();
//		JSONObject cardD = createD_1();
//		JSONObject cardE = createE_1();
//		// JSONObject cardF = createF_1();
//		JSONObject cardG = createG_1();
//		JSONObject cardH = createH_1();
//		JSONObject cardI = createI_1();
//		JSONObject cardJ = createJ_1();
//		JSONObject cardK = createK_1();
//		JSONObject cardL = createL_1();
//		JSONObject cardM = createM_1();
//		JSONObject cardN = createN_1();
//		JSONObject cardO = createO_1();
//		JSONObject cardP = createP_1();
//		JSONObject cardQ = createQ_1();
//		JSONObject cardR = createR_1();
//		JSONObject cardS = createS_1();
//		JSONObject cardT = createT_1();
//		// JSONObject cardU = createU_1();
//		JSONObject cardV = createV_1();
//		JSONObject cardW = createW_1();
//		// JSONObject cardX = createX_1();
//
//		// CARD B, C, U, X doesnt matter because the card is always the same if
//		// rotated
//
//		assertEquals(1, CardReceiver.getRotation("A", cardA));
//		// assertEquals(1, CardReceiver.getRotation("B", cardB));
//		// assertEquals(1, CardReceiver.getRotation("C", cardC));
//		assertEquals(1, CardReceiver.getRotation("D", cardD));
//		assertEquals(1, CardReceiver.getRotation("E", cardE));
//		// assertEquals(1, CardReceiver.getRotation("F", cardF));
//		assertEquals(1, CardReceiver.getRotation("G", cardG));
//		assertEquals(1, CardReceiver.getRotation("H", cardH));
//		assertEquals(1, CardReceiver.getRotation("I", cardI));
//		assertEquals(1, CardReceiver.getRotation("J", cardJ));
//		assertEquals(1, CardReceiver.getRotation("K", cardK));
//		assertEquals(1, CardReceiver.getRotation("L", cardL));
//		assertEquals(1, CardReceiver.getRotation("M", cardM));
//		assertEquals(1, CardReceiver.getRotation("N", cardN));
//		assertEquals(1, CardReceiver.getRotation("O", cardO));
//		assertEquals(1, CardReceiver.getRotation("P", cardP));
//		assertEquals(1, CardReceiver.getRotation("Q", cardQ));
//		assertEquals(1, CardReceiver.getRotation("R", cardR));
//		assertEquals(1, CardReceiver.getRotation("S", cardS));
//		assertEquals(1, CardReceiver.getRotation("T", cardT));
//		// assertEquals(1, CardReceiver.getRotation("U", cardU));
//		assertEquals(1, CardReceiver.getRotation("V", cardV));
//		assertEquals(1, CardReceiver.getRotation("W", cardW));
//		// assertEquals(1, CardReceiver.getRotation("X", cardX));
//	}
//
//	@Test
//	public void getRotation2_onCards() {
//
//		JSONObject cardA = createA_2();
//		// JSONObject cardB = createB_2();
//		// JSONObject cardC = createC_2();
//		JSONObject cardD = createD_2();
//		JSONObject cardE = createE_2();
//		// JSONObject cardF = createF_2();
//		// JSONObject cardG = createG_2();
//		// JSONObject cardH = createH_2();
//		JSONObject cardI = createI_2();
//		JSONObject cardJ = createJ_2();
//		JSONObject cardK = createK_2();
//		JSONObject cardL = createL_2();
//		JSONObject cardM = createM_2();
//		JSONObject cardN = createN_2();
//		JSONObject cardO = createO_2();
//		JSONObject cardP = createP_2();
//		JSONObject cardQ = createQ_2();
//		JSONObject cardR = createR_2();
//		JSONObject cardS = createS_2();
//		JSONObject cardT = createT_2();
//		// JSONObject cardU = createU_2();
//		JSONObject cardV = createV_2();
//		JSONObject cardW = createW_2();
//		// JSONObject cardX = createX_2();
//
//		// CARD B, C, F, G, H, U, X doesnt matter because the card is always
//		// the same if
//		// double rotated
//
//		assertEquals(2, CardReceiver.getRotation("A", cardA));
//		// assertEquals(2, CardReceiver.getRotation("B", cardB));
//		// assertEquals(2, CardReceiver.getRotation("C", cardC));
//		assertEquals(2, CardReceiver.getRotation("D", cardD));
//		assertEquals(2, CardReceiver.getRotation("E", cardE));
//		// assertEquals(2, CardReceiver.getRotation("F", cardF));
//		// assertEquals(2, CardReceiver.getRotation("G", cardG));
//		// assertEquals(2, CardReceiver.getRotation("H", cardH));
//		assertEquals(2, CardReceiver.getRotation("I", cardI));
//		assertEquals(2, CardReceiver.getRotation("J", cardJ));
//		assertEquals(2, CardReceiver.getRotation("K", cardK));
//		assertEquals(2, CardReceiver.getRotation("L", cardL));
//		assertEquals(2, CardReceiver.getRotation("M", cardM));
//		assertEquals(2, CardReceiver.getRotation("N", cardN));
//		assertEquals(2, CardReceiver.getRotation("O", cardO));
//		assertEquals(2, CardReceiver.getRotation("P", cardP));
//		assertEquals(2, CardReceiver.getRotation("Q", cardQ));
//		assertEquals(2, CardReceiver.getRotation("R", cardR));
//		assertEquals(2, CardReceiver.getRotation("S", cardS));
//		assertEquals(2, CardReceiver.getRotation("T", cardT));
//		// assertEquals(2, CardReceiver.getRotation("U", cardU));
//		assertEquals(2, CardReceiver.getRotation("V", cardV));
//		assertEquals(2, CardReceiver.getRotation("W", cardW));
//		// assertEquals(2, CardReceiver.getRotation("X", cardX));
//	}
//
//	@Test
//	public void getRotation3_onCards() {
//
//		JSONObject cardA = createA_3();
//		JSONObject cardB = createB_3();
//		JSONObject cardC = createC_3();
//		JSONObject cardD = createD_3();
//		JSONObject cardE = createE_3();
//		JSONObject cardF = createF_3();
//		JSONObject cardG = createG_3();
//		JSONObject cardH = createH_3();
//		JSONObject cardI = createI_3();
//		JSONObject cardJ = createJ_3();
//		JSONObject cardK = createK_3();
//		JSONObject cardL = createL_3();
//		JSONObject cardM = createM_3();
//		JSONObject cardN = createN_3();
//		JSONObject cardO = createO_3();
//		JSONObject cardP = createP_3();
//		JSONObject cardQ = createQ_3();
//		JSONObject cardR = createR_3();
//		JSONObject cardS = createS_3();
//		JSONObject cardT = createT_3();
//		JSONObject cardU = createU_3();
//		JSONObject cardV = createV_3();
//		JSONObject cardW = createW_3();
//		JSONObject cardX = createX_3();
//
//		// CARD B, C, U, X doesnt matter because the card is always the same if
//		// rotated
//
//		assertEquals(3, CardReceiver.getRotation("A", cardA));
//		// assertEquals(3, CardReceiver.getRotation("B", cardB));
//		// assertEquals(3, CardReceiver.getRotation("C", cardC));
//		assertEquals(3, CardReceiver.getRotation("D", cardD));
//		assertEquals(3, CardReceiver.getRotation("E", cardE));
//		assertEquals(3, CardReceiver.getRotation("F", cardF));
//		assertEquals(3, CardReceiver.getRotation("G", cardG));
//		assertEquals(3, CardReceiver.getRotation("H", cardH));
//		assertEquals(3, CardReceiver.getRotation("I", cardI));
//		assertEquals(3, CardReceiver.getRotation("J", cardJ));
//		assertEquals(3, CardReceiver.getRotation("K", cardK));
//		assertEquals(3, CardReceiver.getRotation("L", cardL));
//		assertEquals(3, CardReceiver.getRotation("M", cardM));
//		assertEquals(3, CardReceiver.getRotation("N", cardN));
//		assertEquals(3, CardReceiver.getRotation("O", cardO));
//		assertEquals(3, CardReceiver.getRotation("P", cardP));
//		assertEquals(3, CardReceiver.getRotation("Q", cardQ));
//		assertEquals(3, CardReceiver.getRotation("R", cardR));
//		assertEquals(3, CardReceiver.getRotation("S", cardS));
//		assertEquals(3, CardReceiver.getRotation("T", cardT));
//		// assertEquals(3, CardReceiver.getRotation("U", cardU));
//		assertEquals(3, CardReceiver.getRotation("V", cardV));
//		assertEquals(3, CardReceiver.getRotation("W", cardW));
//		// assertEquals(3, CardReceiver.getRotation("X", cardX));
//	}
}
