package ai;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;

import shared.Configuration;
import shared.enums.CapabilitiesType;
import shared.enums.GameStatus;
import shared.model.Card;
import shared.model.Player;
import shared.model.Position;

/**
 * This class represents the artifitial intelligence.
 * <p>
 * 
 * It contains all the information about the game in which the ai is taking part. Also
 * it contains references to the classes AILogic and AIGame which are necessary to determine
 * which moves the simulated player should make.
 * 
 *
 */
public final class AI {
	
	private Logger log = LogManager.getLogger("ERROR");
	
	/**
	 * Socket for the communication between AI and server
	 */
	private static Socket aiSocket;
	
	/**
	 * BufferReader that reads the input that is sent from the server through the socket
	 */
	private static BufferedReader in;
	
	/**
	 * OutputStreamWriter for sending messages through the socket to the server
	 */
	private static OutputStreamWriter out;
	
	/**
	 * Represents the instance of the AIReceiver-class that delegates the different messages to
	 * the methods.
	 */
	private static AIReceiver receiver;
	
	/**
	 * Represents the instance of the AISender-class that contains the methods for sending
	 * the messages to the server
	 */
	private AISender sender;
	
	/**
	 * Represents the instance of the AILogic-class that contains the methods that decide the
	 * behaviour of the ai
	 */
	private AILogic aiLogic;
	
	/**
	 * This represents the ai in an player-object
	 */
	private Player ai;
	
	/**
	 * The string that represents the gameName
	 */
	private String gameName;
	
	/**
	 * Contains all the extensions that are supported by the artificial intelligence
	 */
	private Set<CapabilitiesType> extensions;
	
	/**
	 * Value that is equals the remaining cards on the deck
	 */
	private int remainingCards;
	
	/**
	 * Represents the gameID as an integer-value
	 */
	private int gameID;
	
	/**
	 * the value is equals the strength of the ai; the value should be between 0 and 1000
	 */
	private int strength;
	
	private Map<String, Player> playerList;
	
	private boolean playerListSet;
	
	private Card currentCard;
	
	private int moveFailedCounter;

	/**
	 * the main-method that is called when the ai is started. It should be
	 * started with the following parameters:
	 * &lt;nick&gt; &lt;server&gt;:&lt;port&gt; &lt;gamename&gt; &lt;turntime&gt; &lt;strength&gt; &lt;color&gt;
	 * 
	 * @param args contains the variables that are named above
	 */
	public synchronized static void main(String[] args) {

		try {
			String name = args[0];
			String[] server = args[1].split(":");
			String gameName = args[2];
			int timeOut = Integer.valueOf(args[3]);
			int strength = Integer.valueOf(args[4]);
			String color = args[5];

			AI ai = new AI(name, server[0], Integer.valueOf(server[1]),
					gameName, timeOut, strength, color);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Ungueltige Parameter!");
			System.exit(0);
		}
	}

	/**
	 * this is the constructor of the AI-class. It sets up the socket-connection,
	 * and initializes all the variables of the AI.
	 * @param name 		nickName
	 * @param adress 	server-address where the game is hosted
	 * @param port 		the portnumber of this server
	 * @param gameName	the name of the game the ai should take part in
	 * @param timeout
	 * @param strength 	the strength of the ai
	 * @param color 	the meeple-color of the ai
	 */
	public AI(String name, String adress, int port, String gameName,
			int timeout, int strength, String color) {

		try {
			aiSocket = new Socket(adress, port);
			in = new BufferedReader(new InputStreamReader(aiSocket.getInputStream()));
			out = new OutputStreamWriter(aiSocket.getOutputStream(), "UTF-8");
		} catch (IOException e) {
			System.out.println("Ungueltige Serververbindung!");
			System.exit(0);
		}
		
		
		moveFailedCounter = 0;
		playerListSet = false;
		receiver = new AIReceiver(this);
		sender = new AISender(this);
		aiLogic = new AILogic(this);
		Thread t = new Thread(receiver);
		t.start();
		JSONArray capa = new JSONArray();
		capa.put(CapabilitiesType.CHAT);
		extensions = EnumSet.of(CapabilitiesType.CHAT);
		sender.sendLogin(name, capa);
		this.ai = new Player(name, color);
		this.gameName = gameName;
		this.strength = strength;
		playerList = new HashMap<>();
		playerList.put(ai.getNick(), ai);
	}

	/**
	 * this method is called when the server sends the loginSucces-method.
	 * it joins the game that is specified with the parameters of the main-method, if this
	 * game is already created
	 * @param gameName 	the gameName of the sent game	
	 * @param gameID	the gameID of this game
	 * @param state		the current state of the game
	 */
	public void loginSuccess(String gameName, int gameID, String state) {
		System.out.println(this);
		if (this.gameName.equals(gameName)) {
			this.gameID = gameID;
			sender.sendJoinGame(gameID);
		}
	}

	/**
	 * this method is called when the server sends the gameCreated-messgae.
	 * If the game that's newly created is equals the game that is specified in the
	 * parameters of the main-method, the ai joins this game. 
	 * @param gameName	the gameName of the created game
	 * @param gameID	the gameID of the created game
	 */
	public void gameCreated(String gameName, int gameID) {
		if (this.gameName.equals(gameName)) {
			this.gameID = gameID;
			sender.sendJoinGame(gameID);
		}
	}

	/**
	 * this method is called when the server sends the tileDrawn message.
	 * If it's the turn of the ai, it checks for the best placement by calling
	 * the method of the aiLogic, and sends the move-message with this placement.
	 * @param nick			the nick of the player that's turn it is
	 * @param timeout		the timeout-value
	 * @param card			the card that is sent in the tileDrawn-message
	 * @param rotation		the rotation of this card
	 * @param remaining		the remaining card in the cardDeck
	 */
	public void tileDrawn(String nick, int timeout, Card card, int rotation, int remaining) {
		currentCard = card;
		if (nick.equals(ai.getNick())) {
			Set<PossiblePlacement> placements = aiLogic.checkForMeeplePlacement(card.deepCopy(),
					aiLogic.checkForLegalPlacements(card.deepCopy()));
			try {
				PossiblePlacement bestPlacement;
				if(this.strength <= 1000 && this.strength >= 500){
				bestPlacement = aiLogic.getBestPlacement(
						card, placements);
				}
				else {
					bestPlacement = aiLogic.getBestPlacementEasy(card, placements);
				}
				sender.sendMove(gameID, bestPlacement.getPos(),
						bestPlacement.getRotation(),
						bestPlacement.getMeeplePlacements());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		this.remainingCards = remaining;
		
	}

	/**
	 * this method is called when the server sends the moveMade-message.
	 * The move that is send in this message goes to the aiGame and calls the 
	 * specified methods.
	 * @param nick			the nick of the player who made that move
	 * @param rotation		the rotation of the card
	 * @param card			the card that is sent with this message
	 * @param pos			the position on the gameField on which the card was placed
	 * @param placement		the placement of the meeple on the card
	 */
	public void moveMade(String nick, int rotation, Card card, Position pos,
			int placement) {
		try{
			aiLogic.getAIGame().checkCardPlacement(pos, rotation, placement, playerList.get(nick),
				card);
		}
		catch(Exception e){
			log.error("Exception in the moveMade-method of the AI");
		}
		moveFailedCounter = 0;
	}

	/**
	 * this method is called when the server sends the gameUpdate-message.
	 * It actualizes the playerlist and closes the socket if the new gameStage is ended.
	 * @param gameID 
	 * @param playerList	the current playerList that is sent with this message
	 * @param gameStatus	the current gamestate of the game
	 */
	public void gameUpdate(int gameID, Map<String, Player> playerList, GameStatus gameStatus) {
		if (this.gameID == gameID) {
			if (!playerListSet) {
				this.playerList = playerList;
				playerListSet = true;
			}

			if (gameStatus == GameStatus.ENDED) {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				this.getSender().sendExit(this.gameID);
			}
		}

	}
	
	public void moveFailed() {
		Set<PossiblePlacement> placements = aiLogic.checkForMeeplePlacement(currentCard.deepCopy(),
				aiLogic.checkForLegalPlacements(currentCard.deepCopy()));
		int counter = 0;
		for(PossiblePlacement placement : placements){
			if(counter == moveFailedCounter){
				sender.sendMove(gameID, placement.getPos(), placement.getRotation(), placement.getMeeplePlacements());
				moveFailedCounter++;
				return;
			}
			counter++;
		}
	}

	/**
	 * this method sends a prefabricated message if someone writes something in the privatechat
	 * @param messageToSend 	the message that should be delivered
	 * @param senderID			the id of the sender of this message
	 * @param receiverID		the id of the receiver of this message
	 */
	public void handleChatMessageSpecific(String messageToSend,
			String senderID, String receiverID) {

		String aimesage = Configuration.AISTANDARDMESSAGE1 + strength
				+ Configuration.AISTANDARDMESSAGE2;
		sender.sendChatMessage(aimesage, senderID, receiverID);

	}

	/**
	 * this method sends a prefabricated message if someone writes something in the gamechat
	 * @param gameID			the gameID of the game to which the message should be sent
	 * @param messageToSend		the message that should be delivered
	 */
	public void handleGameMessage(int gameID, String messageToSend) {
		String aimesage = Configuration.AISTANDARDMESSAGE1 + strength
				+ Configuration.AISTANDARDMESSAGE2;

		sender.sendChatMessage(ai.getNick(), aimesage, gameID);

	}

	/**
	 * this method sends a prefabricated message if someone writes something in the chats
	 * @param messageToSend		the message that should be delivered
	 */
	public void handleChatMessageGeneral(String messageToSend) {
		String aimesage = Configuration.AISTANDARDMESSAGE1 + strength
				+ Configuration.AISTANDARDMESSAGE2;

		sender.sendChatMessageGeneral(aimesage, ai.getNick());

	}

	// GETTERS AND SETTERS BELOW

	public Socket getAiSocket() {
		return aiSocket;
	}

	public BufferedReader getIn() {
		return in;
	}

	public OutputStreamWriter getOut() {
		return out;
	}

	public AIReceiver getReceiver() {
		return receiver;
	}

	public AISender getSender() {
		return sender;
	}

	public Player getPlayer() {
		return ai;
	}
	
	public int getRemainingCards(){
		return remainingCards;
	}
	
	public AILogic getAiLogic(){
		return aiLogic;
	}
	
	public Map<String, Player> getPlayerList(){
		return playerList;
	}

	
}
