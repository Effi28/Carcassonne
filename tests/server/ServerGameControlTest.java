package server;
import static org.junit.Assert.*;

import java.io.IOException;
import java.net.ServerSocket;

import org.junit.Before;
import org.junit.Test;

import server.model.game.ServerGame;
import server.model.serverCommunication.execution.ServerControl;
import shared.enums.CardAreaType;
import shared.model.Card;
import shared.model.Player;

public class ServerGameControlTest {
	private ServerGame serverGameControl;

	@Before
	public void setUp(){
	
		ServerControl serverControl = null;
		try {
			serverControl = ServerControl.getInstance(new ServerSocket());
		} catch (IOException e) {
			e.printStackTrace();
		}
		Player player = new Player("testString", "testString2");	
		serverGameControl = new ServerGame("unitTestGame", player, 0, serverControl, 0, null);
	}
	
		
	@Test
	public void remainingCards_returns71_onNewGame() {
		assertEquals(71, serverGameControl.getCardDeck().getRemainingSize());
	}
	
	@Test
	public void deliverCard_returnsColonelCardOnStack() {
		assertEquals(Card.class, serverGameControl.getCurrentCard(false).getClass());
	}
	
	@Test
	public void getSingleAreas_containsFourElements_OnNewGame(){
		assertEquals(4, serverGameControl.getSingleAreas().size());
	}
	
	@Test
	public void getSingleAreas_AreaOnFirstPosition_EqualsMeadow(){
		assertEquals(CardAreaType.MEADOW.toString(), serverGameControl.getSingleAreas().get(0).toString());
	}
}
