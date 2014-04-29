package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.overview;

import static org.junit.Assert.*;

import org.junit.Test;

public class OverviewPanelTest {
	@Test
	public void overViewPanelNullTest() {
		OverviewPanel overviewPanel = new OverviewPanel();
		assertNotNull(overviewPanel);
		assertNotNull(overviewPanel.getTreePanel());
		assertNotNull(overviewPanel.getDetailPanel());
	}
}
