/*******************************************************************************
 * Copyright (c) 2014 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Team8s
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.planningpoker.models;

import static org.junit.Assert.*;

import org.junit.Test;


/**
 * Tests for the EmailAddress class
 * @author Kevin
 * @version 1.0
 */
public class EmailAddressTest {
	/**
	 * Can an EmailAddress be created and not just create a null object
	 */
	@Test
	public void testCreateEmailAddress() {
		assertNotNull(new EmailAddress());
	}
	/**
	 * Tests if it is possible to take an object, convert it to json and 
	 * parse that json, yet still have the original object's information
	 */
	@Test
	public void testToJsonFromJson() {
		final EmailAddress emailAdressToTest = new EmailAddress();
		emailAdressToTest.setEmail("TestEmail@TestEmail.edu");
		emailAdressToTest.setOwnerName("Test Owner");
		final String gson = emailAdressToTest.toJSON();
		//Test to see if the object we parse has the same information as the one we passed
		assertEquals(emailAdressToTest.getEmail(), EmailAddress.fromJson(gson).getEmail());
		assertEquals(emailAdressToTest.getOwnerName(), EmailAddress.fromJson(gson).getOwnerName());
		assertNotNull(EmailAddress.fromJson(gson));
	}
	/**
	 * Tests to see if copyFrom() will actually populate the object with 
	 * the other object's information
	 */
	@Test
	public void testCopyFrom() {
		final EmailAddress email1 = new EmailAddress();
		final EmailAddress email2 = new EmailAddress();
		email1.setEmail("Someone@Host.url");
		email1.setOwnerName("Joe");
		email2.copyFrom(email1);
		
		assertEquals(email1.getEmail(), email2.getEmail());
		assertEquals(email1.getOwnerName(), email2.getOwnerName());
	}
	
}
