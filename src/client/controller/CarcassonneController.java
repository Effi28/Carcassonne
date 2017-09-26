package client.controller;

import java.util.Set;

import javafx.application.Platform;
import shared.model.Card;
import shared.model.Meeple;
import shared.model.Placement;
import shared.model.Position;
import shared.model.Spectator;
import client.model.clientCommunication.ClientControl;
import client.model.game.ClientMoveMadeStorage;
import client.view.ChatLobby;
import client.view.GameView;
import client.view.Login;
import client.view.ObserverView;

/**
 * This class represents the controller for the client.
 * <p/>
 * It forwards events from the view to the model and the other way round. It implements the SINGLETON-Pattern, because
 * there should only be one controller.
 * <p/>
 * Implementing the Model-View-Controller Pattern.
 * 
 * @version 27.11.2013
 * @see Login
 * @see GameView
 * @see ObserverView
 * @see ChatLobby
 * @see ClientControl
 * @see Meeple
 * @see Card
 */
public final class CarcassonneController {

	private Login loginView;
	private GameView gameView;
	private ChatLobby chatLobby;
	private ClientControl control;
	private ObserverView observerView;

	/**
	 * Instance of the controller
	 */
	private static CarcassonneController instance;

	/**
	 * This is the primarily used constructor and implements SINGLETON
	 * 
	 * @param control
	 *            reference to the ClientControl, which manages the whole model
	 * @param loginView
	 *            reference to the first dialog of the view
	 */
	private CarcassonneController(ClientControl control, Login loginView) {
		this.control = control;
		this.loginView = loginView;
	}

	/**
	 * Forwards the event that a message has to be sent to all clients to the model
	 * 
	 * @param msg
	 *            message to be sent
	 */
	public void sendMessage(String msg) {
		control.sendMessage(msg);
	}

	/**
	 * Forwards the event that a message has to be sent to a specific game
	 * 
	 * @param msg
	 *            message to be sent
	 * @param gameID
	 *            gameID of the game
	 */
	public void sendMessage(String msg, int gameID) {
		control.sendMessage(msg, gameID);
	}

	/**
	 * Sends a message to a specific receiver client
	 * 
	 * @param msg
	 *            message to be sent
	 * @param receiver
	 *            name of the client which should receive the message
	 */
	public void sendMessage(String msg, String receiver) {
		control.getHandler().getSender().sendChatMessage(msg, receiver);
	}

	/**
	 * Returns the instance of the CarcassonneController because of SINGLETON
	 * 
	 * @param control
	 *            type ClientControl
	 * @param loginView
	 *            type Login
	 * @return instance of this class
	 */
	public synchronized static CarcassonneController getInstance(
			ClientControl control, Login loginView) {
		if (instance == null) {
			instance = new CarcassonneController(control, loginView);
		}
		return instance;
	}

	/**
	 * Forwards join event of the player to the model
	 * 
	 * @param gameID
	 *            gameID of the game which should be joined
	 * @param color
	 *            color which the gamer has choosen
	 */
	public void setupJoin(int gameID, String color) {
		control.setupJoin(gameID, color);
	}

	/**
	 * Manages the start of a game
	 * 
	 * @param gameID
	 *            gameID of the game which should be started
	 */
	public void setupGame(int gameID) {
		control.startGame(gameID);
	}

	/**
	 * Forwards the watch game event
	 * 
	 * @param gameID
	 *            ID of the game to be watched
	 */
	public void watchGame(int gameID) {
		control.watchGame(gameID);
	}

	/**
	 * Forwards the 'move-made' message to the view.
	 * <p/>
	 * Starts a new JavaFx-Thread in order to change sth. in the View.
	 * 
	 * @param gameID
	 *            ID of the game where the move was made
	 * @param cardID
	 *            ID of the Card that was played
	 * @param position
	 *            Position of the Card
	 * @param rotation
	 *            Rotation of the Card
	 * @param placement
	 *            Placement of the Meeple
	 * @param moveMadeNick
	 *            Nick of the player who made the move
	 */
	public void moveMade(int gameID, final String cardID,
			final Position position, final int rotation,
			final Placement placement, final String moveMadeNick) {

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				gameView.moveMade(cardID, position, rotation, placement,
						moveMadeNick);
			}
		});
		Platform.setImplicitExit(true);
	}

	/**
	 * Forwards the 'move-made' message to the ObserverView.
	 * <p/>
	 * Starts a new JavaFx-Thread in order to change sth. in the View.
	 * 
	 * @param gameID
	 *            ID of the game where the move was made
	 * @param cardID
	 *            ID of the Card that was played
	 * @param position
	 *            Position of the Card
	 * @param rotation
	 *            Rotation of the Card
	 * @param moveMadeNick
	 *            Nick of the player who made the move
	 * @param area
	 *            Where the Meeple was set
	 * @param spec
	 *            Spectator is needed in order to start an ObserverView
	 */
	public void moveMadeSpectator(int gameID, final String cardID,
			final Position position, final int rotation, final Placement area,
			final String moveMadeNick, Spectator spec, Set<String> playerlist) {

		assert control.getHandler() != null;

		final ObserverView obsView = ObserverView.getInstance(gameID, this,
				spec, playerlist,
				ClientMoveMadeStorage.getInstance(control.getHandler()));
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				obsView.moveMade(cardID, position, rotation, area, moveMadeNick);
			}
		});
		Platform.setImplicitExit(true);
	}

	/**
	 * Forwards if a user leaves a Game
	 * 
	 * @param gameID
	 *            ID of the game which the user wants to leave
	 */
	public void leaveGame(int gameID) {
		control.leaveGame(gameID);
	}

	/**
	 * Sends the message to be displayed in the view to the ChatLobby.
	 * 
	 * @param messageToSend
	 *            message to be displayed
	 */
	public void writeChatMessageGeneral(String messageToSend) {
		getChatLobby().addText(messageToSend);
	}

	/**
	 * Delegates a message to the GameView if a game message was sent.
	 * 
	 * @param gameID
	 *            ID of the game where the message was sent
	 * @param messageToSend
	 *            Message to be displayed in the View.
	 */
	public void delegateGameMessage(int gameID, String messageToSend) {

		if (gameView == null) {
			if (observerView == null) {
			} else {
				observerView.addText(messageToSend);
			}
		} else {
			gameView.addText(messageToSend);
		}
	}

	/**
	 * Adds a text when a Player left.
	 * 
	 * @param message
	 *            Message to be displayed in the View
	 */
	public void handlePlayerRemoved(String message) {
		getChatLobby().addText(message);
	}

	/**
	 * Delegates to the GameView that a move failed.
	 * 
	 * @param reason
	 *            Reason why the move failed.
	 */
	public void moveFailed(String reason) {
		gameView.moveFailed(reason);
	}

	/**
	 * Delegates a 'tile-drawn' message for a Player.
	 * 
	 * @param nick
	 *            Nick of the Player whose turn it is.
	 * @param cardID
	 *            ID of the Card that was drawn
	 * @param rotation
	 *            Rotation of the Card that was received
	 * @param remaining
	 *            Remaining tiles
	 * @param remainingTime
	 *            Time the Player has to make a move
	 * @param possiblePositions
	 */
	public void tileDrawnPlayer(String nick, String cardID, int rotation,
			int remaining, int remainingTime, Set<Position> possiblePositions) {
		gameView.tileDrawn(nick, cardID, rotation, remaining, remainingTime,
				possiblePositions);

	}

	/**
	 * Delegates a 'tile-drawn' message for a Player.
	 * 
	 * @param nick
	 *            Nick of the Player whose turn it is.
	 * @param cardID
	 *            ID of the Card that was drawn
	 * @param rotation
	 *            Rotation of the Card that was received
	 * @param remaining
	 *            Remaining tiles
	 */
	public void tileDrawnSpectator(String nick, String cardID, int rotation,
			int remaining, int remainingTime) {
		observerView
				.tileDrawn(nick, cardID, rotation, remaining, remainingTime);

	}

	/**
	 * Delegates a 'move' to the ClientControl.
	 * 
	 * @param gameID
	 *            ID of the game where the move was made
	 * @param p
	 *            Position of the Card
	 * @param rotation
	 *            Rotation of the Card
	 * @param currentPlacement
	 *            Placement of the Meeple
	 */
	public void delegateMove(int gameID, Position p, int rotation,
			Placement currentPlacement) {
		control.delegateMove(gameID, p, rotation, currentPlacement);
	}

	/**
	 * Set card in client GameField when set in view
	 * 
	 * @param gameID
	 *            ID of the game where the card was set
	 * @param p
	 *            Position of the Card that was set
	 * @param rotation
	 *            Rotation of the Card that was set
	 */
	public void setCard(int gameID, Position p, int rotation) {
		control.setCard(gameID, p, rotation);
	}

	// GETTER and SETTER below

	public Login getLoginView() {
		return loginView;
	}

	public void setLoginView(Login loginView) {
		this.loginView = loginView;
	}

	public GameView getGameView(int gameID) {
		return gameView;
	}

	public ChatLobby getChatLobby() {
		if (chatLobby == null) {
			chatLobby = ChatLobby.getInstance(this, control.getPlayerList());
		}
		return chatLobby;
	}

	public ClientControl getControl() {
		return control;
	}

	public void setControl(ClientControl control) {
		this.control = control;
	}

	public void setChatLobby(ChatLobby chatLobby) {
		this.chatLobby = chatLobby;
	}

	public void setupClient(String nick) {
		control.setupClient(nick);
	}

	public void setGameView(GameView gameView) {
		this.gameView = gameView;
	}

	public void setObserveView(ObserverView observerView) {
		this.observerView = observerView;
	}
}
