package server.model;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import server.model.game.ServerGame;
import shared.enums.GameStatus;
import shared.model.Player;

/**
 * This class extends the class Thread and is used to count down the time that the user has to make a move.
 * <p>
 * It provides the run-method of the thread, in which is defined who's turn it is and how much time a user has to make a
 * move. It checks for an incoming move, if the move isn't received in time the next player in the list can make a move.
 */
public final class PlayerThread extends Thread {

	/**
	 * Error-Logger
	 */
	private static Logger log = LogManager.getLogger("ERROR");
	/**
	 * Info-logger
	 */
	private static Logger infoLog = LogManager.getLogger("INFO");
	/**
	 * Reference to the game in which the thread is used
	 */
	private ServerGame game;

	/**
	 * Map which contains the nickName of a user as key and the user-object as value.
	 */
	private Map<String, Player> playerList;

	/**
	 * Indicates whether the currentUser has made a move or not, initially set to false
	 */
	private volatile boolean moveMade = false;

	/**
	 * Player-object whose turn it is
	 */
	private Player currentPlayer;

	/**
	 * the integer-value of the time that is count down in the thread
	 */
	private final int turnTime;
	/**
	 * Boolean flag which indicates whether the Thread should stop or not
	 */
	private volatile boolean stop;
	
	private volatile boolean moveFailed = false;

	/**
	 * Constructor that sets the current game and playerlist. Sets the time and game for which the thread was started.
	 * 
	 * @param game
	 *            Game which is controlled by the thread.
	 * @param turnTime
	 *            Time a player has time to make a move.
	 */
	public PlayerThread(ServerGame game, int turnTime) {
		this.stop = false;
		this.game = game;
		this.playerList = game.getPlayerList();
		this.turnTime = turnTime;
		this.setName("PlayerThread: " + game.getGameName());
		this.setDaemon(true);
		start();
	}

	/**
	 * Deletes all references of this Thread and sets the stop flag to true, so that the Thread can terminate.
	 */
	public void remove() {
		this.stop = true;
		currentPlayer = null;
		playerList = null;
		game = null;
	}

	/**
	 * Checks if the game is ongoing.
	 * <p>
	 * When it is ongoing it iterates through the playerList. For every player, it sets the counter to 180, sends the
	 * tile drawn message and checks every second if a move was made. If not it counts down the timer and if a move is
	 * made it switches to the next player.
	 */
	@Override
	public void run() {
		while (game != null && game.getState().equals(GameStatus.ONGOING)
				&& !stop) {

			boolean checkScoreNotCalledYet = true;
			for (Player p : playerList.values()) {
				if (game.getCardDeck().getRemainingSize() == 0) {
					if (checkScoreNotCalledYet) {
						game.checkEndScore();
						game.setState(GameStatus.ENDED);
						infoLog.info("Game ended");
						checkScoreNotCalledYet = false;
					}
				}
				if (game.getState() == GameStatus.ENDED) {
					game.delegateGameUpdateFromPlayerThread(game);
					infoLog.info("Final 'game-update' sent");
					Thread.currentThread().interrupt();
					return;

				} else {
					int timer = turnTime;
					moveMade = false;
					currentPlayer = p;
					game.tileDrawn(p.getNick(), timer);

					while (true) {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							log.error(e.getMessage());
						}
						if (moveMade || timer == 0) {
							break;
						}
						timer--;
						if(moveFailed){
							game.tileDrawn(p.getNick(), timer);
							moveFailed = false;							
						}
					}
				}
			}

		}
		Thread.currentThread().interrupt();
	}

	/*
	 * GETTER and SETTER below
	 */

	/**
	 * Get-method for the currentPlayer
	 * 
	 * @return currentPlayer
	 */
	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	/**
	 * Set-method for the moveMade-variable
	 * 
	 * @param moveMade
	 *            true if a move was made, false if not
	 */
	public void setMoveMade(boolean moveMade) {
		this.moveMade = moveMade;
	}
	
	public void setMoveFailed(boolean moveFailed){
		this.moveFailed = moveFailed;
	}
}
