package server;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import shared.model.Meeple;
import shared.model.Position;
import shared.model.SingleAreaTown;

public class SingleAreaTownTest {

	SingleAreaTown singleAreaTown;

	void setUp() {
		singleAreaTown = new SingleAreaTown(new Meeple(new Position(0, 0), 1,
				null, null), 1, false, new Position(0, 0), 0);
		singleAreaTown.getTownPosition().add(new Position(0, 1));
		singleAreaTown.setPointCounter(2);
		singleAreaTown.setOpenEdgesCounter(0);
	}

	@Test
	public void testCalculatePoints_ifTownIsFinished() {
		setUp();

		int expectedPoints = 2;
		int realPoints = singleAreaTown.calculatePoints();

		assertEquals(expectedPoints, realPoints);
	}

}
