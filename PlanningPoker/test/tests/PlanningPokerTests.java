/** 
 * 
 */

package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;

/**
 * 
 * @author Rick Wight (fmwight) 
 *
 */
public class PlanningPokerTests {
	
	PlanningPokerSession testSession = new PlanningPokerSession(); 

	@Test
	public void testIfSessionExists() {
		assertNotNull(testSession); 
	}
	
	@Test 
	public void testGetSetID() {
		testSession.setID(5); 
		assertEquals(5, testSession.getID()); 
	}
	
	@Test 
	public void testGetSetName() {
		testSession.setName("The name is Test Session"); 
		assertEquals("The name is Test Session", testSession.getName()); 
	}
	
	/** 
	 * test if the toJSON() and fromJson() methods preserve the session information 
	 * by checking that the ID and name fields retain the same values  
	 */
	@Test 
	public void testToAndFromJSONSessionNameID() {
		testSession.setID(5); 
		testSession.setName("The name is Test Session"); 
		String json = testSession.toJSON(); 
		assertEquals(5, testSession.fromJson(json).getID()); 
		assertEquals("The name is Test Session", testSession.fromJson(json).getName()); 
	} 

}
