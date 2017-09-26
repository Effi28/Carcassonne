package server.model.serverCommunication.utility;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import server.model.game.ServerGame;
import shared.enums.CapabilitiesType;
import shared.enums.CardAreaType;
import shared.enums.GameStatus;
import shared.enums.JsonType;
import shared.enums.Reason;
import shared.model.Card;
import shared.model.Placement;
import shared.model.Position;


/**
 * Utility class for the creation of JSONMessages.
 * <p>
 * Creates JSONObjects wrapping the information according to the specification.
 * For each message to be sent there is one static method provided by this class.
 * Because it provides only static methods this class musn't be instantiated.
 * 
 * @version 16.01.2014
 */
public final class ServerMessageBuilder {

	/**
	 * Error-logger
	 */
	private static Logger log = LogManager.getLogger("ERROR");
	
	/**
	 * Should not be instantiated
	 */
	private ServerMessageBuilder() {
		log.error("MessageBuilder was instantiated");
		throw new AssertionError("MessageBuilder was instantiated");
	}

	/**
	 * Creates meeple information of a single player.
	 * 
	 * @param x x - coordinate
	 * @param y y - coordinate
	 * @param placement placement on single card
	 
	 * @return meepleInfos
	 */
	 public static JSONObject createMeepleInformation(int x, int y, int placement) {
		JSONObject msg = new JSONObject();
		try {
			msg.put("x", x);
			msg.put("y", y);
			msg.put("placement", placement);
		} catch (JSONException e) {
			log.error(e.getMessage());
		}
		return msg;
	}
	
	
	/**
	 * Creates a JSONObject which represents an action.
	 * 
	 * @param p Position where the card was set
	 * @param rotation rotation of the card
	 * @param placement where the meeple was set (optional)
	 * @return action
	 */
	 public static JSONObject createAction(Position p, int rotation, Placement placement){
		
		JSONObject msg = createAction(p, rotation);
		
		try {
			if(placement.getCommunicationPlacement() >= 0){
				msg.put("placement", placement.getCommunicationPlacement());				
			}
			if(placement.getSpecialMeeple() != null){
				msg.put("special meeple", placement.getSpecialMeeple().toString());
			}
			
		} catch (JSONException e) {
			log.error(e.getMessage());
		}
		return msg;
	}
	
	/**
	 * Creates a JSONObject which represents an action.
	 * 
	 * @param p Position where the card was set
	 * @param rotation rotation of the card
	 * @return action
	 */
	 public static JSONObject createAction(Position p, int rotation){
		JSONObject msg = new JSONObject();
		try {
			msg.put("x", p.getX());
			msg.put("y", p.getY());
			msg.put("rotation", rotation);
		} catch (JSONException e) {
			log.error(e.getMessage());
		}
		return msg;
	}
	
	/**
	 * Helper method which creates player informations.
	 * 
	 * @param nick Nick of a Player
	 * @param color Color of a Player
	 * @param score	 Score of a Player
	 * @param meeplesLeft Amount of available Meeple of a User
	 * @param meepleArr Already set Meeple of a User
	 * 
	 * @return gameInfos
	 */
	 public static JSONObject createPlayerInfo(String nick, String color, int score, int meeplesLeft, JSONArray meepleArr) {
	
	JSONObject gameInfos = new JSONObject();
	
	try {
		gameInfos.put("nick", nick);
		gameInfos.put("color", color);
		gameInfos.put("score", score);
		gameInfos.put("remaining meeples", meeplesLeft);
		gameInfos.put("meeple-position array", meepleArr);
		
	} catch (JSONException e) {
		log.error(e.getMessage());
	}
	return gameInfos;
	}
	
	
	
	/**
	 * Helper method witch creates GameInfos.
	 * 
	 * @param gameID GameID of the game
	 * @param gameName Name of the game
	 * @param specInfo Infos about the Spectator
	 * @param playerInfo Infos about the Player
	 * @param extensions Infos about the supported addons
	 * @param state State of the game
	 * @return JSONObject containing the game information
	 */
	 public static JSONObject createGameInfos(int gameID, String gameName,
			GameStatus state, JSONArray specInfo, JSONArray playerInfo, JSONArray extensions){
		JSONObject msg = new JSONObject();
		try {
			msg.put("game id", gameID);
			msg.put("name", gameName);
			msg.put("state", state);
			msg.put("player array", playerInfo);
			msg.put("spectator array", specInfo);
			if(!(extensions.optString(0).equals(""))){
				msg.put("extensions", extensions);				
			}
			
		} catch (JSONException e) {
			log.error(e.getMessage());
		}
		
		return msg;
	}
	
	/**
	 * Return a JSONObject for a failed login
	 * 
	 * @param reason reason why the login failed.
	 * @return login failed json-message
	 */
	 public static JSONObject loginFailed(Reason reason){
		JSONObject msg = createWithType(JsonType.LOGINFAILED);
		try {
			msg.put("reason", reason);
		} catch (JSONException e) {
			log.error(e.getMessage());
		}
		return msg;
	}
	
	/**
	 * Returns a JSONObject for a successful login attempt
	 * 
	 * @param nick name of the successfully connected user
	 * @param games information about all existing games
	 * @param nickArray names of all connected player
	 * @param capa capabilities of the server
	 * @return successful login json-message
	 */
	 public static JSONObject loginSucceeded(String nick, Map<Integer, ServerGame> games, Collection<String> nickArray, List<CapabilitiesType> capa){
		JSONObject msg = createWithType(JsonType.LOGINSUCCESS);
		msg = addNick(msg, nick);
		
		Collection<ServerGame> gameArr = games.values();
		JSONArray arr = new JSONArray();
		Iterator<ServerGame> i = gameArr.iterator();
		while(i.hasNext()){
			arr.put(i.next().getGameInformation());
		}
		JSONArray capabilities = new JSONArray();
		for(CapabilitiesType t : capa){
			capabilities.put(t.toString());
		}
		
		try {
			msg.put("game array", arr);
			msg.put("nick array", nickArray);
			msg.put("capabilities", capabilities);
		} catch (JSONException e) {
			log.error(e.getMessage());
		}
		return msg;
	}
	
	/**
	 * Returns a json message that is sent to all connected client which says that a new player joined
	 * 
	 * @param nick name of the newly connected client
	 * @return json message with the nick of the newly jouned player
	 */
	 public static JSONObject playerAdded(String nick){
		JSONObject msg = createWithType(JsonType.PLAYERADDED);
		msg = addNick(msg, nick);
		
		return msg;
	}
	
	/**
	 * Returns a json message that is sent to all connected clients which says that a new game was created
	 * 
	 * @param jsonObject information about the game
	 * @return json message about the newly created game
	 */
	 public static JSONObject gameCreated(JSONObject jsonObject){
		JSONObject msg = createWithType(JsonType.GAMECREATED);
		
		try {
			msg.put("game", jsonObject);
		} catch (JSONException e) {
			log.error(e.getMessage());
		}
		return msg;
	}
	
	/**
	 * Returns a json-message that tells the specific client that the game could not be created
	 * 
	 * @param name name of the game
	 * @param reason reason why the game could not be created
	 * @return a json message with information why the gamecreation failed
	 */
	 public static JSONObject gameCreationFailed(String name, Reason reason) {
		JSONObject msg = createWithType(JsonType.NEWGAMEFAILED);
		try {
			msg.put("name", name);
			msg.put("reason", reason);
		} catch (JSONException e) {
			log.error(e.getMessage());
		}
		return msg;
	}
	
	/**
	 * Returns a json-message that joining the game failed
	 * 
	 * @param gameID ID of the game the user wanted to join
	 * @param reason why did it fail to join the game
	 * @return a json message why it failed to join the game
	 */
	 public static JSONObject joinGameFailed(int gameID, Reason reason) {
		JSONObject msg = createWithType(JsonType.JOINGAMEFAILED);
		try {
			msg.put("reason", reason);
			msg.put("game id", gameID);
		} catch (JSONException e) {
			log.error(e.getMessage());
		}
		return msg;
	}
	
	/**
	 * Returns a successful json-message to the specific client who wants to join a game 
	 * @param gameID ID of the game the user wants to join
	 * @return successful game join message
	 */
	 public static JSONObject joinGameSuccess(int gameID) {
		JSONObject msg = createWithType(JsonType.JOINGAMESUCCESS);
		try {
			msg.put("game id", gameID);
		} catch (JSONException e) {
			log.error(e.getMessage());
		}
		return msg;
	}
	
	/**
	 * Returns a json-message with all the game information
	 * 
	 * @param gameInfo all import information of the game 
	 * @return message with all the gameInformation
	 */
	 public static JSONObject gameUpdate(JSONObject gameInfo) {
		JSONObject msg = createWithType(JsonType.GAMEUPDATE);
		try {
			msg.put("game", gameInfo);
		} catch (JSONException e) {
			log.error(e.getMessage());
		}
		return msg;
	}
	
	/**
	 * Returns a fail-message for a specific client that wants to observe a game
	 * 
	 * @param gameID ID of the game to be observed
	 * @param reason why the observing of the game failed
	 * @return fail message for observing the game
	 */
	 public static JSONObject watchGameFailed(int gameID, Reason reason) {
		JSONObject msg = createWithType(JsonType.WATCHGAMEFAILED);
		try {
			msg.put("game id", gameID);
			msg.put("reason", reason);
		} catch (JSONException e) {
			log.error(e.getMessage());
		}
		return msg;
	}
	
	/**
	 * Returns a success-message for a specific client that wants to observe a game
	 * 
	 * @param gameID ID of the game to be observed
	 * @return success message for observing the game
	 */
	 public static JSONObject watchGameSuccess(int gameID) {
		JSONObject msg = createWithType(JsonType.WATCHGAMESUCCESS);
		try {
			msg.put("game id", gameID);
		} catch (JSONException e) {
			log.error(e.getMessage());
		}
		return msg;
	}

	/**
	 * Returns a message to the specific client that tried to start a game but it failed
	 * 
	 * @param gameID ID of the game the client tried to create
	 * @param reason why the creation of the game failed
	 * @return fail message for the specific client that the game could not be created
	 */
	 public static JSONObject startGameFailed(int gameID, Reason reason) {
		JSONObject msg = createWithType(JsonType.STARTGAMEFAILED);
		try {
			msg.put("game id", gameID);
			msg.put("reason", reason);
		} catch (JSONException e) {
			log.error(e.getMessage());
		}
		return msg;
	}
	
	/**
	 * Returns a json message to all players in the specific game which tells which move can be made
	 * 
	 * 
	 * @param gameID ID of the game where the move can be made
	 * @param nick name of the player whose next
	 * @param tile card that has to be set
	 * @param remaining remaining cards to be set
	 * @param turnTime Time the user has to make a move
	 * @return message to all players in the specific game which move can be made
	 */
	 public static JSONObject tileDrawn(int gameID, String nick, Card tile, int remaining, int turnTime) {
		JSONObject msg = createWithType(JsonType.TILEDRAWN);
		msg = addNick(msg, nick);
		JSONObject card = createCardInfo(tile.getAreas(), tile.getEdges(), tile.getAdjacency(), tile.getBonus(), tile.getMultiplier());
		
		try {
			msg.put("tile", card);
			msg.put("game id", gameID);
			msg.put("remaining", remaining);
			msg.put("nick", nick);
			msg.put("timeout", TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) + turnTime);
		} catch (JSONException e) {
			log.error(e.getMessage());
		}
		return msg;
	}
	
	/**
	 * Returns a json message to the client which tried to make a move
	 * 
	 * @param gameID ID of the game where the move was tried to do
	 * @param jsonObject the move that failed
	 * @param reason reason why the move has failed
	 * @return message for the specific client that made an invalid move
	 */
	 public static JSONObject moveFailed(int gameID, JSONObject jsonObject, Reason reason) {
		JSONObject msg = createWithType(JsonType.MOVEFAILED);
		try {
			msg.put("game id", gameID);
			msg.put("action", jsonObject);
			msg.put("reason", reason.toString());
		} catch (JSONException e) {
			log.error(e.getMessage());
		}
		return msg;
	}
	
	/**
	 * Returns a json message for all clients that a move was made
	 * 
	 * @param gameID ID of the game where the move was made
	 * @param nick name of the player that made the move
	 * @param card card which was set
	 * @param action JSONObject which contains x, y, placement and rotation of a Card
	 * @return message for all clients in the specific game with the new game stuff
	 */
	 public static JSONObject moveMade(int gameID, String nick, Card card, JSONObject action) {
		JSONObject msg = createWithType(JsonType.MOVEMADE);
		msg = addNick(msg, nick);
		try {
			msg.put("game id", gameID);
			msg.put("tile", ServerMessageBuilder.createCardInfo(card.getAreas(), card.getEdges(), card.getAdjacency(), card.getBonus(), card.getMultiplier()));
			msg.put("action", action);
		} catch (JSONException e) {
			log.error(e.getMessage());
		}
		return msg;
	}
	 
	/**
	 * Returns a json message for all player that an observer or player in a specific game disconnected
	 * 
	 * @param gameID ID of the game where the player disconnected
	 * @param nick name of the player that disconnected
	 * @return message for all player in the game that the player with the name nick disconnected
	 */
	 public static JSONObject playerLeft(int gameID, String nick) {
		JSONObject msg = createWithType(JsonType.PLAYERLEFT);
		msg = addNick(msg, nick);
		try {
			msg.put("game id", gameID);
		} catch (JSONException e) {
			log.error(e.getMessage());
		}
		return msg;
	}
	
	/**
	 * Returns a json message for the player that disconnected
	 * 
	 * @return message for the disconnecting client that the server granted the logout
	 */
	 public static JSONObject userDisconnected() {
		return createWithType(JsonType.ACKNOWLEDGEDISCONNECT);
	}
	
	/**
	 * Returns a json message that a player disconnected
	 * 
	 * @param nick name of the player that disconnected
	 * @return message for all clients that the player with the nick disconnected
	 */
	 public static JSONObject playerRemoved(String nick) {
		JSONObject msg = createWithType(JsonType.PLAYERREMOVED);
		msg = addNick(msg, nick);
		
		return msg;
	}
	
	/**
	 * Returns a json message for a game that the server removed
	 * 
	 * @param gameID ID of the game that was removed
	 * @return message to be sent to the clients with the gameID
	 */
	 public static JSONObject gameRemoved(int gameID){
		JSONObject json = null;
		try {
			json = createWithType(JsonType.GAMEREMOVED).put("game id", gameID);
			return json;
		} catch (JSONException e) {
			log.error(e.getMessage());
		}
		return json;
	}
	
	/**
	 * Returns a json message for an invalid json-message
	 * 
	 * @param reason why is the message invalid
	 * @return message to be sent to the client which sent the invalid message
	 */
	 public static JSONObject invalidMessage(Reason reason){
		JSONObject json = null;
		try {
			json =  createWithType(JsonType.INVALIDMESSAGE).put("reason", reason.toString());
		} catch (JSONException e) {
			log.error(e.getMessage());
		}
		return json;
	}
	
	/**
	 * Returns a json-message for the lobby-chat, the message will be sent to all connected clients
	 * 
	 * @param senderID ID of the sender
	 * @param msg message to be sent
	 * @return message to be sent
	 */
	 public static JSONObject chatToAll(String senderID, String msg ){
		JSONObject message = createWithType(JsonType.CHAT);
		try {
			message.put("sender id", senderID);
			message.put("stamp", TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
			message.put("message", msg);
		} catch (JSONException e) {
			log.error(e.getMessage());
		}
		return message;
	}
	
	/**
	 * Returns a json-message for a private chat
	 * 
	 * @param senderID name of the sender
	 * @param receiverID name of the receiver
	 * @param message message which will be send
	 * 
	 * @return message which will be sent to the specific receiver
	 */
	 public static JSONObject chatToSpecific(String senderID, String message, String receiverID){
		JSONObject msg = chatToAll(senderID, message);
		try {
			msg.put("receiver id", receiverID);
		} catch (JSONException e) {
			log.error(e.getMessage());
		}
		return msg;
	}
	
	/**
	 * Returns a json-message for a chat in a game
	 * 
	 * @param senderID name of the sender
	 * @param message message which will be send
	 * @param gameID ID of the game
	 * @return message to be displayed in the game
	 */
	 public static JSONObject chatToGame(String senderID, String message, int gameID){
		JSONObject msg = chatToAll(senderID, message);
		try {
			msg.put("game id", gameID);
		} catch (JSONException e) {
			log.error(e.getMessage());
		}
		return msg;
	}
	
	/**
	 * Helper-method that creates a JSON-Object with a specific type
	 * 
	 * @param type type of the JSON-Object
	 * 
	 * @return returns an object with a specific type
	 */
	private static JSONObject createWithType(JsonType type){
		JSONObject msg = new JSONObject();
		try {
			msg.put("type", type);
		} catch (JSONException e) {
			log.error(e.getMessage());
		}
		return msg;
	}

	/** 
	 * Helper-method that puts a nick to the jsonobject
	 * 
	 * @param msg JSONObject with the type
	 * @param nick nick that should be added to the jsonmessage
	 * @return json message with the nick added
	 */
	private static JSONObject addNick(JSONObject msg, String nick) {
		try {
			msg.put("nick", nick);
		} catch (JSONException e) {
			log.error(e.getMessage());
		}
		return msg;
	}
	
	/**
	 * Puts a card into a JSONObject
	 * 
	 * @param areas Areas array 
	 * @param edges Edges Array
	 * @param adjacency Adjacency Array
	 * @param bonus Bonus Array
	 * @param multiplier Multiplier Array
	 * @return jsonObject with the information about a card
	 */
	private static JSONObject createCardInfo(List<CardAreaType> areas, List<ArrayList<Integer>> edges, List<ArrayList<Integer>> adjacency, List<Integer> bonus, List<Integer> multiplier) {
		JSONObject msg = new JSONObject();
		
		JSONArray areasArr = new JSONArray();
		JSONArray edgesArr = new JSONArray();
		JSONArray adjacencyArr = new JSONArray();
		JSONArray bonusArr = new JSONArray();
		JSONArray multiplierArr = new JSONArray();
		
		//Put bonus in JSONArray
		for(Integer i : bonus){
			bonusArr.put(i);
		}
		
		// Put multiplier in JSONArray
		for(Integer i : multiplier){
			multiplierArr.put(i);
		}
		
		// Put areas in JSONArray
		for(CardAreaType c : areas){
			areasArr.put(c.toString());
		}
		
		// Put edges in edge and put every edge in JSONArray edges
		for(ArrayList<Integer> list : edges){
			JSONArray edge = new JSONArray();
			for(Integer i : list){
				edge.put(i);
			}
			edgesArr.put(edge);
		}
		
		// Put all adjacencies in JSONArray
		for(ArrayList<Integer> l : adjacency){
			JSONArray arr = new JSONArray();
			for(Integer i : l){
				arr.put(i);
			}
			adjacencyArr.put(arr);
		}
		if(adjacency.isEmpty()){
			adjacencyArr.put(new JSONArray());
		}
		
		try {
			msg.put("areas", areasArr);
			msg.put("edges", edgesArr);
			msg.put("adjacency", adjacencyArr);		
			
			if(bonusArr.length() != 0){
				msg.put("bonus", bonusArr);				
			}
			if(multiplierArr.length() != 0){
				msg.put("multiplier", multiplierArr);				
			}
		} catch (JSONException e) {
			log.error(e.getMessage());
		}
		return msg;
	}
}
