package ai;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ai.AI;
import client.model.game.CardReceiver;
import server.model.game.LogicCardCreator;
import shared.Configuration;
import shared.enums.GameStatus;
import shared.enums.JsonType;
import shared.enums.SpecialMeepleType;
import shared.model.BuildCard;
import shared.model.Card;
import shared.model.Meeple;
import shared.model.Player;
import shared.model.Position;

public final class AIReceiver implements Runnable {

	AI ai;
	private int gameMessageCounter = 0;
	private int generalMessageCounter = 0;
	private int specificMessageCounter = 0;

	public AIReceiver(AI ai) {
		this.ai = ai;
	}

	/**
	 * Method of the thread that always checks for incoming messages
	 */
	public void run() {
		while (!Thread.interrupted())
			try {
				readMessage();
			} catch (Exception e) {
				e.printStackTrace();
			}

	}

	/**
	 * Reads the message from the server
	 * <p>
	 * Gets the message from the server and divides it into its parts. After
	 * that the different types of messages are handled differently by using
	 * different delegation-methods.
	 * 
	 */
	public void readMessage() {

		String jsonText;

		try {

			// get the message
			jsonText = ai.getIn().readLine();
			if (jsonText == null) {
				Thread.currentThread().interrupt();
			}
			System.out.println("SERVER ---> AI : " + jsonText);

			JSONObject jsonObject = new JSONObject(jsonText);
			String s = jsonObject.optString("type");
				
			// gets the type of the json-message for the switch-case-construct
			JsonType t = JsonType.fromString(s);

			// handles the different types of json-messages
			switch (t) {

			case TILEDRAWN:
				tileDrawn(jsonObject);
				break;

			case GAMEUPDATE:
				gameUpdate(jsonObject);
				break;

			case MOVEMADE:
				moveMade(jsonObject);
				break;

			case MOVEFAILED:
				moveFailed(jsonObject);
				break;

			case LOGINSUCCESS:
				loginSuccess(jsonObject);
				break;

			case LOGINFAILED:
				loginFailed(jsonObject);
				break;

			case PLAYERADDED:
				playerAdded(jsonObject);
				break;

			case CHAT:
				chat(jsonObject);
				break;

			case GAMECREATED:
				gameCreated(jsonObject);
				break;

			case ACKNOWLEDGEDISCONNECT:
				acknowledgedDisconnect();
				break;

			case PLAYERREMOVED:
				playerRemoved(jsonObject);
				break;

			case INVALIDMESSAGE:
				invalidMessage(jsonObject);
				break;

			case JOINGAMEFAILED:
				joinGameFailed(jsonObject);
				break;

			case JOINGAMESUCCESS:
				joinGameSuccess(jsonObject);
				break;

			case PLAYERLEFT:
				playerLeft(jsonObject);
				break;

			case GAMEREMOVED:
				gameRemoved(jsonObject);
				break;

			default:
			}
		} catch (IOException | JSONException e) {
			e.printStackTrace();
		}
	}

	private void loginSuccess(JSONObject jsonObject) {
		JSONArray gameArray = jsonObject.optJSONArray("game array");
		for (int i = 0; i < gameArray.length(); i++) {
			int gameID;
			try {
				JSONObject game = gameArray.getJSONObject(i);
				gameID = game.optInt("game id");
				String state = game.optString("state");
				String gameName = game.optString("name");
				ai.loginSuccess(gameName, gameID, state);

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	private void gameCreated(JSONObject jsonObject) {
		JSONObject game = jsonObject.optJSONObject("game");
		int gameID = game.optInt("game id");
		String gameName = game.optString("name");
		ai.gameCreated(gameName, gameID);
	}

	/**
	 * Closes the streams and the socket, kills the thread. Is called when the
	 * ai disconnects.
	 */
	private void acknowledgedDisconnect() {
		try {
			ai.getIn().close();
			ai.getOut().close();
			ai.getAiSocket().close();
			Thread.currentThread().interrupt();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void moveMade(JSONObject jsonObject) {

		JSONObject card = jsonObject.optJSONObject("tile");
		int gameID = jsonObject.optInt("game id");
		String moveMadeNick = jsonObject.optString("nick");
		JSONObject action = jsonObject.optJSONObject("action");

		assert !moveMadeNick.equals("");

		String cardID = CardReceiver.receiveCard(card);

		assert !cardID.equals("");

		int x = action.optInt("x");
		int y = action.optInt("y");
		int rotation = action.optInt("rotation");
		String specialMeeple = action.optString("special meeple");

		int sentCardRotation = CardReceiver.getRotation(cardID, card);
		Card builtCard = BuildCard.buildCard(card);

		// checks the Placement of a meeple if no placement is send the String
		// will be null
		// otherwise it will be the int
		String placementTemp = action.optString("placement");

		int placement;

		if (placementTemp.equals("") || placementTemp.equals("-1")) {
			placement = -1;
		} else {
			placement = Integer.parseInt(placementTemp);
		}

		SpecialMeepleType meeple = null;
		// If a special meeple was sent
		if (!(specialMeeple.equals(""))) {
			meeple = SpecialMeepleType.fromString(specialMeeple);
		}
		
		int rotationInView = (sentCardRotation + rotation)%4;
		
		ai.moveMade(moveMadeNick, rotationInView, builtCard, new Position(x, y),
				placement);
	}

	private void gameUpdate(JSONObject jsonObject) {
		// Gets the JSONObject with all game information from the message
				JSONObject game = jsonObject.optJSONObject("game");
				GameStatus state = GameStatus.fromString(game.optString("state"));
				JSONArray playerArray = game.optJSONArray("player array");
				JSONArray spectatorArray = game.optJSONArray("spectator array");

				int gameID = game.optInt("game id");
				
				try {
					ai.gameUpdate(gameID, JSONArrToMapPlayer(playerArray), state);
				} catch (Exception e) {
					e.printStackTrace();
				}
		
	}

	private void tileDrawn(JSONObject jsonObject) {

		String nick = jsonObject.optString("nick");
		int remaining = jsonObject.optInt("remaining");
		int timeout = jsonObject.optInt("timeout");
		JSONObject tile = jsonObject.optJSONObject("tile");
		String cardID = CardReceiver.receiveCard(tile);
		int rotation = CardReceiver.getRotation(cardID, tile);
		Card card = BuildCard.buildCard(tile);
		ai.tileDrawn(nick, timeout, card, rotation, remaining);
	}

	/**
	 * 
	 * Helper method which creates a Map with Key Nick and Value Player from a
	 * JSON-Array
	 * 
	 * @param arr
	 *            Array which should be transformed to a Map
	 * @param singleExtension
	 * @return the Map of Players
	 */
	public static Map<String, Player> JSONArrToMapPlayer(JSONArray arr) {

		Map<String, Player> playerList = new HashMap<>();

		for (int i = 0; i < arr.length(); i++) {
			JSONObject json = arr.optJSONObject(i);
			String nick = json.optString("nick");
			String color = json.optString("color");
			int score = json.optInt("score");
			int remainingMeeples = json.optInt("remaining meeples");
			JSONArray meeplePositions = json
					.optJSONArray("meeple-position array");
			Player u = new Player(nick, color);
			u.setScore(score);
			u.setMeeplesLeft(remainingMeeples);

			for (int j = 0; j < meeplePositions.length(); j++) {
				JSONObject meeplePosition = meeplePositions.optJSONObject(j);
				int x = meeplePosition.optInt("x");
				int y = meeplePosition.optInt("y");
				int placement = meeplePosition.optInt("placement");

				Meeple meeple = new Meeple(new Position(x, y), placement,
						u, null);
				u.addMeeple(meeple);
			}

			playerList.put(u.getNick(), u);
		}
		return playerList;
	}

	private void gameRemoved(JSONObject jsonObject) {
		// TODO Auto-generated method stub
	}

	private void playerLeft(JSONObject jsonObject) {
		// TODO Auto-generated method stub
	}

	private void joinGameSuccess(JSONObject jsonObject) {
		System.out.println("joinGameSucccess");
	}

	private void joinGameFailed(JSONObject jsonObject) {
		// TODO Auto-generated method stub
	}

	private void invalidMessage(JSONObject jsonObject) {
		// TODO Auto-generated method stub
	}

	private void playerRemoved(JSONObject jsonObject) {
		// TODO Auto-generated method stub
	}

	private void chat(JSONObject jsonObject) {
		String receiverID = jsonObject.optString("receiver id");
		String senderID = jsonObject.optString("sender id");
		int gameID = jsonObject.optInt("game id");
		String message = jsonObject.optString("message");
		String stamp = jsonObject.optString("stamp");
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

		long time = Long.valueOf(stamp);
		Date resultdate = new Date(time * 1000);

		String messageToSend = sdf.format(resultdate) + " " + senderID + ": "
				+ message;

		// Handles an incoming specific message --> private chat
		if ((!(receiverID.equals(""))) && specificMessageCounter == 0) {

			ai.handleChatMessageSpecific(messageToSend, senderID, receiverID);
			specificMessageCounter++;
			return;
			// Handles an incoming game message & just the first time the ai
			// should send a message
		} else if (gameID != 0 && gameMessageCounter == 0) {
			ai.handleGameMessage(gameID, messageToSend);
			gameMessageCounter++;
			return;
		}
		// Handles an incoming general message & just the first time the ai
		// should send a message
		else {
			if (generalMessageCounter == 0) {
				ai.handleChatMessageGeneral(messageToSend);
			}
			generalMessageCounter++;
			return;
		}

	}

	private void playerAdded(JSONObject jsonObject) {
		// TODO Auto-generated method stub
	}

	private void loginFailed(JSONObject jsonObject) {
	}

	private void moveFailed(JSONObject jsonObject) {
		ai.moveFailed();
	}
}
