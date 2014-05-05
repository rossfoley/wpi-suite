package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.overview;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.buttons.PlanningPokerSessionButtonsPanel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.ViewEventController;

public class OverviewTreePanelTest {
	@Test
	public void OverviewTreePanelNullTest() {
		ViewEventController.getInstance().setPlanningPokerSessionButtonsPanel(new PlanningPokerSessionButtonsPanel());
		OverviewTreePanel overviewTreePanel = new OverviewTreePanel();
		assertNotNull(overviewTreePanel);
	}
}
