package edu.wpi.cs.wpisuitetng.modules.planningpoker.models;

import static org.junit.Assert.*;

import java.util.UUID;

import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.requirements.*;

import org.junit.Test;

public class EstimateTest {
	
	Requirement rec1 = new Requirement(1, "rec1", "description1");
	Requirement rec2 = new Requirement(2, "rec2", "description2");
	Requirement rec3 = new Requirement(3, "rec3", "description3");
	
	UUID UUID1 = UUID.randomUUID();
	UUID UUID2 = UUID.randomUUID();
	UUID UUID3 = UUID.randomUUID();
	
	Estimate est1 = new Estimate(1, 10, UUID1);
	Estimate est2 = new Estimate(2, 20, UUID2);
	Estimate est3 = new Estimate(3, 30, UUID3);

	@Test
	public void testCompareTo() {
		assertEquals(est1.compareTo(est2), -1);
		assertEquals(est3.compareTo(est1), 1);
		assertEquals(est1.compareTo(est1), 0);
	}
	
	@Test
	public void testGetRequirementID() {
		assertEquals(est1.getRequirementID(), 1);
		assertEquals(est2.getRequirementID(), 2);
		assertEquals(est3.getRequirementID(), 3);
	}
	
	@Test
	public void testGetID() {
		assertEquals(est1.getID(), UUID1);
		assertEquals(est2.getID(), UUID2);
		assertEquals(est3.getID(), UUID3);
	}
	
	@Test
	public void testGetVote() {
		assertEquals(est1.getVote(), 10);
		assertEquals(est2.getVote(), 20);
		assertEquals(est3.getVote(), 30);
	}
	
}
