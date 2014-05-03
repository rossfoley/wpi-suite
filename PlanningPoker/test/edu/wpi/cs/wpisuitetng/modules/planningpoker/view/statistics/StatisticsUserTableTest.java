package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.statistics;

import static org.junit.Assert.*;

import java.util.UUID;

import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.Estimate;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;

public class StatisticsUserTableTest {
	StatisticsUserTable userTable;
	
	public StatisticsUserTableTest() {
		// Create planning poker session with some estimates
		PlanningPokerSession session = new PlanningPokerSession();
		Estimate testEst = new Estimate(3, 5, UUID.randomUUID());
		testEst.setOwnerName("Admin");
		session.addEstimate(testEst);
		userTable = new StatisticsUserTable(session, -1);
	}

	@Test
	public void testIsCellEditable() {
		// Check combinations of rows and cols
		for (int row = -1; row < 5; row++) {
			for (int col = -1; col < 5; col++) {
				assertFalse(userTable.isCellEditable(row, col));
			}
		}
	}

	@Test
	public void testUpdateTable() {
		// Update with a requirement with no estimate
		userTable.updateTable(1);
		assertEquals(0, userTable.getModel().getRowCount());
		// Update with a requirment with an estimate
		userTable.updateTable(3);
		assertEquals(1, userTable.getModel().getRowCount());
	}

}
