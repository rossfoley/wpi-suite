/** 
 * 
 */

package tests;

import static org.junit.Assert.*;

import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;

/**
 * Tests of the PlanningPokerSession functions 
 * 
 * @author Rick Wight (fmwight) 
 *
 */
public class PlanningPokerTests {
	
	PlanningPokerSession testSession = new PlanningPokerSession(); 

	/**
	 * test to see that the test session is created and that it's fields were modified correctly
	 */
	@Test
	public void testIfSessionExists() {
		assertNotNull(testSession); 
		assertFalse(testSession.isOpen()); 
		assertNotNull(testSession.getName()); 
	}
	
	@Test 
	public void testGetSetID() {
		final UUID testuuid = UUID.randomUUID();
		testSession.setID(testuuid); 
		assertEquals(testuuid, testSession.getID()); 
	}
	
	@Test 
	public void testGetSetName() {
		testSession.setName("The name of a Test Session"); 
		assertEquals("The name of a Test Session", testSession.getName()); 
	}
	
	/**
	 * test if the get/set/add requirementID functions work correctly 
	 * NOTE: right now this is flawed, in that setRequirementIDs MUST be called before
	 * addRequirement, because the set of requirements is not initialized in the 
	 * constructor. This will be revisited after updated code is merged in. 
	 */
	@Test
	public void testReqIDs() {
		final Set<Integer> aSet = new HashSet<Integer>(); 
		testSession.setRequirementIDs(aSet); 
		assertNotNull(testSession.getRequirementIDs()); 
		testSession.addRequirement(5); 
		testSession.addRequirement(4); 
		testSession.addRequirement(5); 
		assertEquals(2, testSession.getRequirementIDs().size()); 
		assertTrue(testSession.getRequirementIDs().contains(4)); 
		assertTrue(testSession.getRequirementIDs().contains(5)); 
	}
	
	/**
	 * test get/add requirement functions
	 * NOTE: these are currently incomplete. there is a method to get a requirement by
	 * it's ID number, but not to set the requirement in the first place. test will be
	 * written when that function exists, for now it has a placeholder
	 */
	@Test
	public void testReqs() {
		assertTrue(true); 
	}
	
	/** 
	 * test if the toJSON() and fromJson() methods preserve the session information 
	 * by checking that the ID and name fields retain the same values 
	 */
	@Test 
	public void testToAndFromJSONSessionNameID() {
		final UUID testuuid = UUID.randomUUID();
		testSession.setID(testuuid); 
		testSession.setName("The name is Test Session"); 
		final String json = testSession.toJSON(); 
		assertEquals(testuuid, testSession.fromJson(json).getID()); 
		assertEquals("The name is Test Session", testSession.fromJson(json).getName()); 
	} 
	
	/**
	 * test getting and setting the EndDate field 
	 */
	@Test
	public void testGetSetEndDate() {
		final int month = 8; 
		final int day = 20; 
		final int year = 2002; 
		final GregorianCalendar testDate = new GregorianCalendar(year, month, day); 
		testSession.setEndDate(testDate); 
		assertEquals(month, testSession.getEndDate().get(GregorianCalendar.MONTH)); 
		assertEquals(day, testSession.getEndDate().get(GregorianCalendar.DAY_OF_MONTH));
		assertEquals(year, testSession.getEndDate().get(GregorianCalendar.YEAR));
	}

}
