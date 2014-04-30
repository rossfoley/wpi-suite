package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.statistics;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.Estimate;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;

public class StatisticsInfoPanelTest {
	RequirementEstimateStats testStats = new RequirementEstimateStats(1, new ArrayList<Estimate>());
	PlanningPokerSession testSession = new PlanningPokerSession();
	StatisticsInfoPanel testStatInfoPanel = new StatisticsInfoPanel(testSession);
	
	@Test
	public void testFormatMean() {
		testStats.addAndRefresh(new Estimate(1, 1, UUID.randomUUID()));
		assertEquals("1.0", testStatInfoPanel.formatMean(testStats));
		testStats.addAndRefresh(new Estimate(2, 2, UUID.randomUUID()));
		assertEquals("1.5", testStatInfoPanel.formatMean(testStats));
		testStats.addAndRefresh(new Estimate(4, 4, UUID.randomUUID()));
		assertEquals("2.3", testStatInfoPanel.formatMean(testStats));
	}
	
	@Test
	public void testFormatMedian() {
		testStats.addAndRefresh(new Estimate(1, 1, UUID.randomUUID()));
		assertEquals("1.0", testStatInfoPanel.formatMedian(testStats));
		testStats.addAndRefresh(new Estimate(2, 2, UUID.randomUUID()));
		assertEquals("1.5", testStatInfoPanel.formatMedian(testStats));
		testStats.addAndRefresh(new Estimate(4, 4, UUID.randomUUID()));
		assertEquals("2.0", testStatInfoPanel.formatMedian(testStats));
	}
	
	@Test
	public void testFormateStdDev() {
		testStats.addAndRefresh(new Estimate(1, 1, UUID.randomUUID()));
		assertEquals("0.0", testStatInfoPanel.formatStdDev(testStats));
		testStats.addAndRefresh(new Estimate(2, 2, UUID.randomUUID()));
		assertEquals("0.5", testStatInfoPanel.formatStdDev(testStats));
	}

}
