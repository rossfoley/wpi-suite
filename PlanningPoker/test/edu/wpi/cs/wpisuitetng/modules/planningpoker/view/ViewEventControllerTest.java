package edu.wpi.cs.wpisuitetng.modules.planningpoker.view;

import static org.junit.Assert.*;

import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JScrollPane;

import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession.SessionState;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.buttons.PlanningPokerSessionButtonsPanel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.overview.OverviewDetailPanel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.overview.OverviewTreePanel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.session.PlanningPokerSessionTab;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.statistics.StatisticsPanel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.voting.VotingPage;

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

	@Test
	public void testGetToolbar() {
		ToolbarView toolbar = new ToolbarView(true);
		
		ViewEventController.getInstance().setToolbar(toolbar);
		assertEquals(toolbar, ViewEventController.getInstance().getToolbar());
	}
	
	@Test
	public void testGetComponentSessionViewMode() {
		// Check null
		assertEquals(ViewMode.NONE, ViewEventController.getInstance().getComponentSessionViewMode(null));
		
		JComponent tab0 = new PlanningPokerSessionTab();
		assertEquals(ViewMode.EDITING, ViewEventController.getInstance().getComponentSessionViewMode(tab0));
		
		tab0 = new VotingPage(createTestSession());
		assertEquals(ViewMode.VOTING, ViewEventController.getInstance().getComponentSessionViewMode(tab0));
		
		tab0 = new StatisticsPanel(createTestSession());
		assertEquals(ViewMode.STATISTICS, ViewEventController.getInstance().getComponentSessionViewMode(tab0));
		// Check non-planning Poker component
		tab0 = new JScrollPane();
		assertEquals(ViewMode.NONE, ViewEventController.getInstance().getComponentSessionViewMode(tab0));
	}
	
	/**
	 * Tests to ensure that only a single tab is allowed to be opened for any given session
	 */
	@Test
	public void testOpenMultipleTabsForASingleSession() {
		// Setup MainView to avoid NullPointerExcepton caused when adding a tab
		MainView mainPanel = new MainView();
		ViewEventController.getInstance().setMainView(mainPanel);
		
		PlanningPokerSession sessionA = createTestSession();
		
		// Check first to see if there are no sessions opened
		Map<PlanningPokerSession, JComponent> sessionMap = 
				ViewEventController.getInstance().getOpenSessionTabHashTable();
		assertTrue(sessionMap.isEmpty());
		
		// Open session for editing
		ViewEventController.getInstance().openSessionTab(sessionA, ViewMode.EDITING);
		assertTrue(sessionMap.containsKey(sessionA));
		assertEquals(1, sessionMap.size());
		
		// Attempt to open session for voting (it shouldn't allow it)
		ViewEventController.getInstance().openSessionTab(sessionA, ViewMode.VOTING) ;
		assertTrue(sessionMap.containsKey(sessionA));
		assertEquals(1, sessionMap.size());
		
		// Attempt to open a session for viewing (it shouldn't allow it)
		ViewEventController.getInstance().openSessionTab(sessionA, ViewMode.STATISTICS);
		assertTrue(sessionMap.containsKey(sessionA));
		assertEquals(1, sessionMap.size());		
		
		// Close editing tab and attempt to vote (it should allow this)
		ViewEventController.getInstance().removeTab(sessionMap.get(sessionA));
		assertFalse(sessionMap.containsKey(sessionA));
		assertTrue(sessionMap.isEmpty());
		ViewEventController.getInstance().openSessionTab(sessionA, ViewMode.VOTING);
		assertTrue(sessionMap.containsKey(sessionA));
		assertEquals(1, sessionMap.size());
		
		// Try to re-open the voting tab. Tab count should be the same
		ViewEventController.getInstance().openSessionTab(sessionA, ViewMode.VOTING);
		assertTrue(sessionMap.containsKey(sessionA));
		assertEquals(1, sessionMap.size());
	}
	
	/**
	 * Creates a planning poker session with a name and description for testing
	 * @return	The test session
	 */
	public PlanningPokerSession createTestSession() {
		PlanningPokerSession session0 = new PlanningPokerSession();
		
		session0.setDescription("This is a description for a session");
		session0.setName("My Test Session");
		session0.setUsingDeck(false);
		session0.setGameState(SessionState.OPEN);
		
		return session0;
	}

}
