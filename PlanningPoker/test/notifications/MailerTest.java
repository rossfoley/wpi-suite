/*******************************************************************************
 * Copyright (c) 2012 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kevin J. Barry
 *******************************************************************************/

package notifications;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;







import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.notifications.Mailer;

/**
 * Tests for the Mailer class in notifications
 * @author Kevin Barry
 * @version 1.0
 *
 */
public class MailerTest {

	/**
	 * Tests sending to a singular email Address
	 */
	@Test
	public void testSendingSolo(){
		final Mailer mail = new Mailer();
		assertTrue(mail.mailTo("emailAddress@Somehost.com", "Test1", "This is a test message"));
		assertFalse(mail.mailTo("people at string", "Test2", "This is a test message"));
		assertTrue(mail.mailTo("emailAddres2s@Somehost.com", "Test2", "This is a test message"));
	}
	/**
	 * Tests sending to a list of people
	 * should only "fail" at sending to one person
	 */
	@Test
	public void testSendingGroup() {
		final List<String> recipients = new LinkedList<String>();
		recipients.add("emailAddress@Somehost.com");
		recipients.add("emailAddress2@Somehost.com");
		recipients.add("emailAddress3@Somehost.com");
		recipients.add("people at string");
		
		final Mailer mail = new Mailer();
		final List<String> failedToSendTo = mail.mailToGroup(recipients, 
				"This is a test message", "This is a test message");
		final List<String> failedTestAgainst = new LinkedList<String>();
		failedTestAgainst.add("people at string");
		assertEquals(failedToSendTo, failedTestAgainst);
	}
	/**
	 * Tests sending a message at the start of a PlanningPokerSession
	 * should only "fail" at sending to one person
	 */
	@Test
	public void testSendPlanningPokerStart() {
		final PlanningPokerSession session = new PlanningPokerSession();
		session.setName("Name");
		
		final List<String> recipients = new LinkedList<String>();
		recipients.add("emailAddress@Somehost.com");
		recipients.add("emailAddress2@Somehost.com");
		recipients.add("emailAddress3@Somehost.com");
		recipients.add("people at string");
		
		final Mailer mail = new Mailer();
		final List<String> failedToSendTo = mail.notifyOfPlanningPokerSessionStart
				(recipients, session);
		final List<String> failedTestAgainst = new LinkedList<String>();
		failedTestAgainst.add("people at string");
		assertEquals(failedToSendTo, failedTestAgainst);
	}
	/**
	 * Tests sending a message at the end of a PlanningPokerSession
	 * should only "fail" at sending to one person
	 */
	@Test
	public void testSendPlanningPokerClose() {
		final PlanningPokerSession session = new PlanningPokerSession();
		session.setName("Name");
		
		final List<String> recipients = new LinkedList<String>();
		recipients.add("emailAddress@Somehost.com");
		recipients.add("emailAddress2@Somehost.com");
		recipients.add("emailAddress3@Somehost.com");
		recipients.add("people at string");
		
		final Mailer mail = new Mailer();
		final List<String> failedToSendTo = mail.notifyOfPlanningPokerSessionClose
				(recipients, session);
		final List<String> failedTestAgainst = new LinkedList<String>();
		failedTestAgainst.add("people at string");
		assertEquals(failedToSendTo, failedTestAgainst);
	}
}
