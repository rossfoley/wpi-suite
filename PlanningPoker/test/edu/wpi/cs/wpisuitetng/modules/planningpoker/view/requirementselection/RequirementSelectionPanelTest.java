package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.requirementselection;

import static org.junit.Assert.*;

import java.util.AbstractSet;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.requirementselection.RequirementSelectionPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.RequirementModel;

public class RequirementSelectionPanelTest {

	@Test
	public void testRequirementSelectionPanel()
		throws Exception {
		RequirementModel reqs = RequirementModel.getInstance();
		LinkedList<Requirement> data = new LinkedList<Requirement>();
		Requirement req1 = new Requirement();
		req1.setName("Requirement 1");
		req1.setId(0);
		req1.setDescription("Test 1");
		reqs.addRequirement(req1);
		data.add(req1);
		req1 = new Requirement();
		req1.setName("Requirement 2");
		req1.setId(1);
		req1.setDescription("Test 2");
		reqs.addRequirement(req1);
		data.add(req1);
		req1 = new Requirement();
		req1.setName("Requirement 3");
		req1.setId(2);
		req1.setDescription("Test 3");
		reqs.addRequirement(req1);
		req1 = new Requirement();
		req1.setName("Requirement 4");
		req1.setId(3);
		req1.setDescription("Test 4");
		reqs.addRequirement(req1);
		data.add(req1);
		PlanningPokerSession session = new PlanningPokerSession();
		Set<Integer> requirementIDs = new HashSet<Integer>();
		requirementIDs.add(0);
		requirementIDs.add(1);
		requirementIDs.add(3);
		session.setRequirementIDs(requirementIDs);
		RequirementSelectionView parent = new RequirementSelectionView(session);
		RequirementSelectionPanel requirementSelectionPanel = new RequirementSelectionPanel(parent, session);
		requirementSelectionPanel.setSelectedRequirements(requirementIDs);
		List<Requirement> selected = requirementSelectionPanel.getSelected();
		assertEquals(data, selected);
	
	}

	
	public void test() {
		fail("Not yet implemented");
	}

}
