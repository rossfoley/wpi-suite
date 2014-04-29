package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.overview;

import static org.junit.Assert.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.GregorianCalendar;

import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.Deck;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession.SessionState;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSessionModel;

public class OverviewTableTest {
	@Test
	public void OverviewTableNullTest() {
		final String[] columnNames = {"Requirement Name", "Your Vote", "Final Estimate"};
		final Object[][] data = {};
		
		OverviewTable overviewTable = new OverviewTable(data, columnNames);
		assertNotNull(overviewTable);
	}
	
	@Test
	public void refreshIsNotNullWithNullSessionTest() {
		final String[] columnNames = {"Requirement Name", "Your Vote", "Final Estimate"};
		final Object[][] data = {};
		
		OverviewTable overviewTable = new OverviewTable(data, columnNames);
		
		PlanningPokerSession session = new PlanningPokerSession();
		session.setSessionCreatorName(null);
		PlanningPokerSessionModel.getInstance().addPlanningPokerSession(session);
		
		overviewTable.refresh();
		assertNotNull(overviewTable);
		assertFalse(overviewTable.wasChangedByRefresh());
	}
	
	@Test
	public void refreshIsNotNullWithPopulatedSessionTest() {
		final String[] columnNames = {"Requirement Name", "Your Vote", "Final Estimate"};
		final Object[][] data = {};
		
		OverviewTable overviewTable = new OverviewTable(data, columnNames);
		
		PlanningPokerSession session = new PlanningPokerSession();
		session.setEndDate((GregorianCalendar)Calendar.getInstance());
		session.setSessionCreatorName("Jimmy");
		Deck testDeck = new Deck();
		testDeck.setDeckName("Jimmy's Deck");
		session.setSessionDeck(testDeck);
		PlanningPokerSessionModel.getInstance().addPlanningPokerSession(session);
		
		overviewTable.refresh();
		assertNotNull(overviewTable);
	}
	@Test
	public void getOpenSessionsTest() {
		final String[] columnNames = {"Requirement Name", "Your Vote", "Final Estimate"};
		final Object[][] data = {};
		
		OverviewTable overviewTable = new OverviewTable(data, columnNames);
		
		PlanningPokerSession session = new PlanningPokerSession();
		session.setGameState(SessionState.OPEN);
		List<PlanningPokerSession> listOfOpenSessions = new ArrayList<PlanningPokerSession>();
		listOfOpenSessions.add(session);
		PlanningPokerSessionModel.getInstance().addPlanningPokerSession(session);
		assertEquals(overviewTable.getOpenSessions(), listOfOpenSessions);
	}
}
