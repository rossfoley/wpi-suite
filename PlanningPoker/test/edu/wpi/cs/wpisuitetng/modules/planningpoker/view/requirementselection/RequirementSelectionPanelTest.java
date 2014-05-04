package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.requirementselection;

import static org.junit.Assert.*;

import java.util.AbstractSet;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JList;

import org.jfree.ui.action.ActionButton;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.core.models.Project;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSessionModel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.requirementselection.RequirementSelectionPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.RequirementModel;

public class RequirementSelectionPanelTest {
	boolean debug = false;
	int timeoutMax = 50;
	
	@Test
	public void testRequirementSelectionPanel()
		throws Exception {
		RequirementModel reqs = RequirementModel.getInstance();
		reqs.emptyModel();
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
		reqs.emptyModel();
	
	}

	
	@Test
	public void testFilterOutRequirementsInUse()
		throws Exception {
		RequirementModel reqs = RequirementModel.getInstance();
		reqs.emptyModel();
		LinkedList<Requirement> data = new LinkedList<Requirement>();
		LinkedList<Requirement> data2 = new LinkedList<Requirement>();
		Requirement req1 = new Requirement();
		req1.setName("Requirement 1-2");
		req1.setId(4);
		req1.setDescription("Test 2");
		reqs.addRequirement(req1);
		data.add(req1);
		data2.add(req1);
		req1 = new Requirement();
		req1.setName("Requirement 2-2");
		req1.setId(5);
		req1.setDescription("Test 2");
		reqs.addRequirement(req1);
		data.add(req1);
		req1 = new Requirement();
		req1.setName("Requirement 3-2");
		req1.setId(6);
		req1.setDescription("Test 2");
		reqs.addRequirement(req1);
		req1 = new Requirement();
		req1.setName("Requirement 4-2");
		req1.setId(7);
		req1.setDescription("Test 2");
		reqs.addRequirement(req1);
		data.add(req1);
		data2.add(req1);
		
		PlanningPokerSession session = new PlanningPokerSession();
		Set<Integer> requirementIDs = new HashSet<Integer>();
		requirementIDs.add(4);
		requirementIDs.add(5);
		requirementIDs.add(7);
		session.setRequirementIDs(requirementIDs);
		
		PlanningPokerSession session2 = new PlanningPokerSession();
		Set<Integer> requirementIDs2 = new HashSet<Integer>();
		requirementIDs2.add(5);
		requirementIDs2.add(6);
		session2.setRequirementIDs(requirementIDs2);
		
		RequirementSelectionView parent = new RequirementSelectionView(session);
		RequirementSelectionPanel requirementSelectionPanel = new RequirementSelectionPanel(parent, session);
		requirementSelectionPanel.setSelectedRequirements(requirementIDs);
		List<Requirement> selected = requirementSelectionPanel.getSelected();
		assertEquals(data, selected);
		
		PlanningPokerSessionModel sessions = PlanningPokerSessionModel.getInstance();
		sessions.emptyModel();
		sessions.addCachedPlanningPokerSession(session);
		sessions.addCachedPlanningPokerSession(session2);
		
		RequirementSelectionPanel requirementSelectionPanel2 = new RequirementSelectionPanel(parent, session);
		requirementSelectionPanel2.setSelectedRequirements(requirementIDs);
		List<Requirement> selected2 = requirementSelectionPanel2.getSelected();
		assertEquals(data, selected2);
		
		session2.setGameState(PlanningPokerSession.SessionState.OPEN);
		
		RequirementSelectionPanel requirementSelectionPanel3 = new RequirementSelectionPanel(parent, session);
		requirementSelectionPanel3.setSelectedRequirements(requirementIDs);
		List<Requirement> selected3 = requirementSelectionPanel3.getSelected();
		assertEquals(data2, selected3);
		reqs.emptyModel();
		sessions.emptyModel();
		
	}
	
	@Test
	public void testAddRequirement(){
		RequirementModel reqs = RequirementModel.getInstance();
		reqs.emptyModel();
		LinkedList<Requirement> data = new LinkedList<Requirement>();
		Requirement req1 = new Requirement();
		req1.setName("Requirement 1-3");
		req1.setId(8);
		req1.setDescription("Test 3");
		reqs.addRequirement(req1);
		data.add(req1);
		req1 = new Requirement();
		req1.setName("Requirement 2-3");
		req1.setId(9);
		req1.setDescription("Test 3");
		reqs.addRequirement(req1);
		data.add(req1);
		req1 = new Requirement();
		req1.setName("Requirement 3-3");
		req1.setId(10);
		req1.setDescription("Test 3");
		reqs.addRequirement(req1);
		req1 = new Requirement();
		req1.setName("Requirement 4-3");
		req1.setId(11);
		req1.setDescription("Test 3");
		//reqs.addRequirement(req1);
		data.add(req1);
		PlanningPokerSession session = new PlanningPokerSession();
		Set<Integer> requirementIDs = new HashSet<Integer>();
		requirementIDs.add(8);
		requirementIDs.add(9);
		session.setRequirementIDs(requirementIDs);
		RequirementSelectionView parent = new RequirementSelectionView(session);
		RequirementSelectionPanel requirementSelectionPanel = new RequirementSelectionPanel(parent, session);
		requirementSelectionPanel.setSelectedRequirements(requirementIDs);
		requirementSelectionPanel.addRequirement(req1);
		List<Requirement> selected = requirementSelectionPanel.getSelected();
		assertEquals(data, selected);
		reqs.emptyModel();
	}
	
	@Test
	public void testBtns(){
		RequirementModel reqs = RequirementModel.getInstance();
		reqs.emptyModel();
		LinkedList<Requirement> data = new LinkedList<Requirement>();
		Requirement req1 = new Requirement();
		req1.setName("Requirement 1-4");
		req1.setId(12);
		req1.setDescription("Test 4");
		reqs.addRequirement(req1);
		req1 = new Requirement();
		req1.setName("Requirement 2-4");
		req1.setId(13);
		req1.setDescription("Test 4");
		reqs.addRequirement(req1);
		data.add(req1);
		req1 = new Requirement();
		req1.setName("Requirement 3-4");
		req1.setId(14);
		req1.setDescription("Test 4");
		reqs.addRequirement(req1);
		req1 = new Requirement();
		req1.setName("Requirement 4-4");
		req1.setId(15);
		req1.setDescription("Test 4");
		reqs.addRequirement(req1);
		data.add(req1);
		PlanningPokerSession session = new PlanningPokerSession();
		Set<Integer> requirementIDs = new HashSet<Integer>();
		requirementIDs.add(13);
		requirementIDs.add(15);
		session.setRequirementIDs(requirementIDs);
		RequirementSelectionView parent = new RequirementSelectionView(session);
		RequirementSelectionPanel requirementSelectionPanel = new RequirementSelectionPanel(parent, session);
		requirementSelectionPanel.setSelectedRequirements(requirementIDs);
		List<Requirement> selected = requirementSelectionPanel.getSelected();
		assertEquals(data, selected);
		
		JButton btnRemoveAll = requirementSelectionPanel.getRemoveAll();
		btnRemoveAll.doClick();
		selected = requirementSelectionPanel.getSelected();
		assertEquals(new LinkedList<Requirement>(), selected);
		
		JButton btnAddAll = requirementSelectionPanel.getAddAll();
		btnAddAll.doClick();
		selected = requirementSelectionPanel.getSelected();
		assertEquals(reqs.getRequirements(), selected);
		
		List<Requirement> availableReqs = reqs.getRequirements();
		if (debug) System.out.println(availableReqs.size());
		LinkedList<Integer> randomIDsRemove = new LinkedList<Integer>();
		int timeout = 0;
		while (randomIDsRemove.size() < 3 && timeout < timeoutMax) {
			int random = (int)(Math.random()*(availableReqs.size()));
			if (debug) System.out.print(random + ":");
			Requirement rqt = availableReqs.get(random);
			int ID = rqt.getId();
			if (debug) System.out.println(ID);
			if  (!randomIDsRemove.contains(ID)){
				randomIDsRemove.add(ID);
			}
			timeout++;
		}
		if (debug) System.out.println("Time Out:" + timeout + randomIDsRemove);
		
		LinkedList<Integer> randomIDsAdd = new LinkedList<Integer>();
		timeout = 0;
		while (randomIDsAdd.size() < 3 && timeout < timeoutMax) {
			int random = (int)(Math.random()*(randomIDsRemove.size()));
			Requirement rqt = availableReqs.get(random);
			int ID = rqt.getId();
			if (debug) System.out.println(ID);
			if  (!randomIDsAdd.contains(ID)){
				randomIDsAdd.add(ID);
			}
			timeout++;
		}
		if (debug) System.out.println("Time Out:" + timeout + randomIDsAdd);
		
		JList SelectedList = requirementSelectionPanel.getSelectedList();
		JList UnselectedList = requirementSelectionPanel.getUnselectedList();
		List<Integer> selectedPos = requirementSelectionPanel.getSelectedPos();
		List<Integer> unselectedPos = requirementSelectionPanel.getUnselectedPos();
		JButton btnRemove = requirementSelectionPanel.getRemoveBtn();
		JButton btnAdd = requirementSelectionPanel.getAddBtn();
		
		LinkedList<Integer> posToRemove = new LinkedList<Integer>();
		LinkedList<Requirement> dataNotRemoved = new LinkedList<Requirement>();
		for (int i : randomIDsRemove){
			Requirement req = reqs.getRequirement(i);
			int pos = availableReqs.indexOf(req);
			posToRemove.add(selectedPos.indexOf(pos));
		}
		if (debug) System.out.println(posToRemove);
		for (Requirement req : availableReqs){
			if (!randomIDsRemove.contains(req.getId())){
				dataNotRemoved.add(req);
			}
		}
		for (int i : randomIDsRemove){
			Requirement req = reqs.getRequirement(i);
			int pos = availableReqs.indexOf(req);
			selectedPos = requirementSelectionPanel.getSelectedPos();
			SelectedList.setSelectedIndex(selectedPos.indexOf(pos));
			if (debug) System.out.println(pos + ":" + SelectedList.getSelectedIndex());
			btnRemove.setEnabled(true);
			btnRemove.doClick();
			if (debug) System.out.println(pos + ":" + SelectedList.getSelectedIndex());
		}
		selected = requirementSelectionPanel.getSelected();
		assertEquals(dataNotRemoved, selected);
	
		btnRemoveAll.doClick();
		selected = requirementSelectionPanel.getSelected();
		assertEquals(new LinkedList<Requirement>(), selected);
		
		LinkedList<Integer> posToAdd = new LinkedList<Integer>();
		LinkedList<Requirement> dataAdd = new LinkedList<Requirement>();
		unselectedPos = requirementSelectionPanel.getUnselectedPos();
		for (int i : randomIDsAdd){
			Requirement req = reqs.getRequirement(i);
			int pos = availableReqs.indexOf(req);
			posToAdd.add(unselectedPos.indexOf(pos));
		}
		if (debug) System.out.println(posToAdd);
		for (Requirement req : availableReqs){
			if (randomIDsAdd.contains(req.getId())){
				dataAdd.add(req);
			}
		}
		for (int i : randomIDsAdd){
			Requirement req = reqs.getRequirement(i);
			int pos = availableReqs.indexOf(req);
			unselectedPos = requirementSelectionPanel.getUnselectedPos();
			UnselectedList.setSelectedIndex(unselectedPos.indexOf(pos));
			if (debug) System.out.println(pos + ":" + UnselectedList.getSelectedIndex());
			btnAdd.setEnabled(true);
			btnAdd.doClick();
			if (debug) System.out.println(pos + ":" + UnselectedList.getSelectedIndex());
		}
		selected = requirementSelectionPanel.getSelected();
		assertEquals(dataAdd, selected);
		reqs.emptyModel();
	}
}
