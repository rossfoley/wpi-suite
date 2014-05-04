package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.statistics;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;

public class StatisticsReqTableTest {
	private StatisticsReqTable reqTable;
	
	public StatisticsReqTableTest() {
		PlanningPokerSession activeSession = new PlanningPokerSession();
		final String[] reqColumnNames = {"Requirement Name", "Your Vote", "Final Estimate"};
		final Object[][] reqData = {};
		reqTable = new StatisticsReqTable(reqData, reqColumnNames);
	}

	@Test
	public void testWasChangedByRefresh() {
		reqTable.setChangedByRefresh(true);
		assertTrue(reqTable.wasChangedByRefresh());
		
		reqTable.setChangedByRefresh(false);
		assertFalse(reqTable.wasChangedByRefresh());
	}

	@Test
	public void testIsCellEditable() {
		// Check combinations of rows and cols
		for (int row = -1; row < 5; row++) {
			for (int col = -1; col < 5; col++) {
				if (col == 2) {
					assertTrue(reqTable.isCellEditable(row, col));
				}
				else {
					assertFalse(reqTable.isCellEditable(row, col));
				}
			}
		}
	}
	
	@Test
	public void testFireSelectedRequirementEvent() {
		SelectedRequirementListener listener = new SelectedRequirementListener();
		reqTable.addSelectedRequirementListener(listener);
		
		// Test fire selection with listener
		assertEquals(-1, listener.getSelectedRequirement());
		reqTable.fireSelectedRequirementEvent(5);
		assertEquals(5, listener.getSelectedRequirement());
		
		// Test fire selection without a listener
		reqTable.removeSelectedRequirementListener(listener);
		listener.setSelectedRequirement(new SelectedRequirementEvent(this, -1));
		reqTable.fireSelectedRequirementEvent(5);
		assertEquals(-1, listener.getSelectedRequirement());
		
		// Try to remove a listener not in the list
		reqTable.removeSelectedRequirementListener(listener);
		reqTable.fireSelectedRequirementEvent(5);
		assertEquals(-1, listener.getSelectedRequirement());
	}

}
