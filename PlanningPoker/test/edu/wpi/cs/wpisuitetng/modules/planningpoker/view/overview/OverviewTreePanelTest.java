package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.overview;

import static org.junit.Assert.*;

import org.junit.Test;

public class OverviewTreePanelTest {
	@Test
	public void OverviewTreePanelNullTest() {
		OverviewTreePanel overviewTreePanel = new OverviewTreePanel();
		assertNotNull(overviewTreePanel);
	}
}
