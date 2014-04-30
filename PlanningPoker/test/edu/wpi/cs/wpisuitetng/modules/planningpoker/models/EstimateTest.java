package edu.wpi.cs.wpisuitetng.modules.planningpoker.models;

import static org.junit.Assert.*;

import java.util.UUID;

import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;

import org.junit.Test;

import com.google.gson.Gson;

public class EstimateTest {
	
	Requirement rec1 = new Requirement();
	Requirement rec2 = new Requirement();
	Requirement rec3 = new Requirement();
	
	UUID UUID;
	UUID UUID1 = java.util.UUID.randomUUID();
	UUID UUID2 = java.util.UUID.randomUUID();
	UUID UUID3 = java.util.UUID.randomUUID();
	UUID UUID11 = java.util.UUID.randomUUID();
	UUID UUID22 = java.util.UUID.randomUUID();
	UUID UUID33 = java.util.UUID.randomUUID();
	
	Estimate est1 = new Estimate(1, 10, UUID);
	Estimate est2 = new Estimate(2, 20, UUID);
	Estimate est3 = new Estimate(3, 30, UUID);
	

	@Test
	public void testCompareTo() {
		assertEquals(est1.compareTo(est2), -1);
		assertEquals(est3.compareTo(est1), 1);
		assertEquals(est1.compareTo(est1), 0);
	}
	
	@Test
	public void testGetRequirementID() {
		est1.setRequirementID(11);
		est2.setRequirementID(22);
		est3.setRequirementID(33);
		assertEquals(est1.getRequirementID(), 11);
		assertEquals(est2.getRequirementID(), 22);
		assertEquals(est3.getRequirementID(), 33);
	}
	
	@Test
	public void testGetID() {
		est1.setID(UUID1);
		est2.setID(UUID2);
		est3.setID(UUID3);
		assertEquals(est1.getID(), UUID1);
		assertEquals(est2.getID(), UUID2);
		assertEquals(est3.getID(), UUID3);
	}
	
	@Test
	public void testGetVote() {
		est1.setVote(15);
		est2.setVote(25);
		est3.setVote(35);
		assertEquals(est1.getVote(), 15);
		assertEquals(est2.getVote(), 25);
		assertEquals(est3.getVote(), 35);
	}

	@Test
	public void testGetOwnerName() {
		est1.setOwnerName("Cam");
		est2.setOwnerName("Jon");
		est3.setOwnerName("Pat");
		assertEquals(est1.getOwnerName(), "Cam");
		assertEquals(est2.getOwnerName(), "Jon");
		assertEquals(est3.getOwnerName(), "Pat");
	}
	
	@Test
	public void testGetSessionID() {
		est1.setSessionID(UUID11);
		est2.setSessionID(UUID22);
		est3.setSessionID(UUID33);
		assertEquals(est1.getSessionID(), UUID11);
		assertEquals(est2.getSessionID(), UUID22);
		assertEquals(est3.getSessionID(), UUID33);
	}
	
	@Test 
	public void testToJSON() {
		Gson gsontest = new Gson();
		String jsontest = gsontest.toJson(est1);
		
		assertEquals(est1.toJSON(), jsontest);
	}
	
	@Test 
	public void testFromJson() {
		Gson gsontest = new Gson();
		String jsontest = gsontest.toJson(est1);
		
		assertEquals(est1.compareTo(Estimate.fromJson(jsontest)), 0);
	}
	
	@Test public void testFromJsonArray() {
		Gson gsontest = new Gson(); 
		Estimate[] testEstimateArray = new Estimate[]{est1};
		String jsontest = gsontest.toJson(testEstimateArray);
		Estimate[] estimatearray = Estimate.fromJsonArray(jsontest);
		
		Estimate estimate = estimatearray[0];
		assertEquals(0, est1.compareTo(estimate));
	}
}