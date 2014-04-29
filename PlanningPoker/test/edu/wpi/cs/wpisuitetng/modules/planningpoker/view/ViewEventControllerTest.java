package edu.wpi.cs.wpisuitetng.modules.planningpoker.view;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.buttons.PlanningPokerSessionButtonsPanel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.overview.OverviewDetailPanel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.overview.OverviewTreePanel;

public class ViewEventControllerTest {

	@Test
	public void testGetInstance() {
		assertNotNull(ViewEventController.getInstance());
	}

	@Test
	public void testGetOverviewTreePanel() {
		OverviewTreePanel overviewTree = new OverviewTreePanel();
		
		ViewEventController.getInstance().setOverviewTree(overviewTree);
		assertEquals(overviewTree, ViewEventController.getInstance().getOverviewTreePanel());
	}

	@Test
	public void testGetOverviewDetailPanel() {
		OverviewDetailPanel overviewDetail = new OverviewDetailPanel();
		
		ViewEventController.getInstance().setOverviewDetailPanel(overviewDetail);
		assertEquals(overviewDetail, ViewEventController.getInstance().getOverviewDetailPanel());
	}

	@Test
	public void testGetPlanningPokerSessionButtonsPanel() {
		PlanningPokerSessionButtonsPanel buttonsPanel = new PlanningPokerSessionButtonsPanel();
		
		ViewEventController.getInstance().setPlanningPokerSessionButtonsPanel(buttonsPanel);
		assertEquals(buttonsPanel,
				ViewEventController.getInstance().getPlanningPokerSessionButtonsPanel());
	}

	@Test
	public void testGetMainView() {
		MainView mainPanel = new MainView();
		
		ViewEventController.getInstance().setMainView(mainPanel);
		assertEquals(mainPanel, ViewEventController.getInstance().getMainView());
	}

}
