/*******************************************************************************
 * Copyright (c) 2014 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Team8s
 ******************************************************************************/
package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.UUID;

import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.gui.CreatePokerSessionErrors;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession.SessionState;


/**
 * @author rossfoley and Rick Wight (fmwight) 
 *
 */
public class PlanningPokerSessionTest {
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
		UUID testuuid = UUID.randomUUID();
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
	 */
	@Test
	public void testReqIDs() {
		assertNotNull(testSession.getRequirementIDs()); 
		testSession.addRequirement(5); 
		testSession.addRequirement(4); 
		testSession.addRequirement(5); 
		assertEquals(2, testSession.getRequirementIDs().size()); 
		assertTrue(testSession.getRequirementIDs().contains(4)); 
		assertTrue(testSession.getRequirementIDs().contains(5)); 
	}
	
	/** 
	 * test if the toJSON() and fromJson() methods preserve the session information 
	 * by checking that the ID and name fields retain the same values 
	 */
	@Test 
	public void testToAndFromJSONSessionNameID() {
		UUID testuuid = UUID.randomUUID();
		testSession.setID(testuuid); 
		testSession.setName("The name is Test Session"); 
		String json = testSession.toJSON(); 
		assertEquals(testuuid, testSession.fromJson(json).getID()); 
		assertEquals("The name is Test Session", testSession.fromJson(json).getName()); 
	} 
	
	/**
	 * test getting and setting the EndDate field 
	 */
	@Test
	public void testGetSetEndDate() {
		int month = 8; 
		int day = 20; 
		int year = 2002; 
		GregorianCalendar testDate = new GregorianCalendar(year, month, day); 
		testSession.setEndDate(testDate); 
		assertEquals(month, testSession.getEndDate().get(GregorianCalendar.MONTH)); 
		assertEquals(day, testSession.getEndDate().get(GregorianCalendar.DAY_OF_MONTH));
		assertEquals(year, testSession.getEndDate().get(GregorianCalendar.YEAR));
	}
	
	@Test
	public void testGameStateHelpers() {
		PlanningPokerSession session = new PlanningPokerSession();
		GregorianCalendar endDate = new GregorianCalendar();
		
		// By default, sessions are pending
		assertTrue(session.isPending());
		assertFalse(session.hasEndDate());
		
		// Add an end date and set the state to open
		endDate.add(Calendar.DAY_OF_MONTH, 1);
		session.setEndDate(endDate);
		session.setGameState(SessionState.OPEN);
		assertFalse(session.isPending());
		assertTrue(session.isOpen());
		assertTrue(session.hasEndDate());
		
		// Change the end date and ensure that the session knows it has ended
		endDate.add(Calendar.DAY_OF_MONTH, -3);
		session.setEndDate(endDate);
		assertFalse(session.isOpen());
		assertTrue(session.isEnded());
		
		session.setGameState(SessionState.CLOSED);
		assertTrue(session.isClosed());
	}
	
	@Test
	public void testSettingNameAndDescription() {
		testSession.setName(" needs a trim   ");
		assertEquals(testSession.getName(), "needs a trim");
		
		testSession.setDescription("  space space    ");
		assertEquals(testSession.getDescription(), "space space");
	}
	
	@Test
	public void testFieldValidations() {
		PlanningPokerSession session = new PlanningPokerSession();
		GregorianCalendar endDate = new GregorianCalendar();
		endDate.add(Calendar.DAY_OF_MONTH, -3);
		
		session.setName("");
		session.setDescription("");
		session.setEndDate(endDate);
		
		ArrayList<CreatePokerSessionErrors> errors = session.validateFields(true, true);
		assertTrue(errors.contains(CreatePokerSessionErrors.EndDateTooEarly));
		assertTrue(errors.contains(CreatePokerSessionErrors.NoDescription));
		assertTrue(errors.contains(CreatePokerSessionErrors.NoName));
		
		errors = session.validateFields(true, false);
		assertTrue(errors.contains(CreatePokerSessionErrors.NoDateSelected));
	}
}
