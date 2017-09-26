package server;
import static org.junit.Assert.*;

import org.junit.Test;

import shared.model.Meeple;
import shared.model.Position;
import shared.model.SingleAreaRoad;

public class SingleAreaRoadTest {
	
	SingleAreaRoad singleAreaRoad1; 

	void setUp(){
		singleAreaRoad1 = new SingleAreaRoad(new Meeple(new Position (0,0), 1, null, null), true, new Position(0,0), 0);
		singleAreaRoad1.getRoadPosition().add(new Position(0,0));
		singleAreaRoad1.setPointCounter(2);
		singleAreaRoad1.getRoadPosition().add(new Position(0,1));
		singleAreaRoad1.setPointCounter(3);
		singleAreaRoad1.getRoadPosition().add(new Position(0,2));
		singleAreaRoad1.setPointCounter(4);
		singleAreaRoad1.setClosed(2);
	}
	
	@Test
	public void testCalculatePoints_ifRoadIsFinished(){
		setUp();
		
		int expectedPoints = 4;
		int realPoints = singleAreaRoad1.calculatePoints();
		
		assertEquals(expectedPoints, realPoints);
	}
}
