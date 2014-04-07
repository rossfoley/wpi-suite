package tests;

import static org.junit.Assert.*;
import notification.Mailer;

import org.junit.Test;

public class MailerTests {

	
	@Test
	public void testSending(){
		Mailer mail = new Mailer();
		assertTrue(mail.MailTo("kjb594@yahoo.com", "Cakes", "If you see this eat cake"));
		assertFalse(mail.MailTo("people at string", "Pie", "If you see this eat pie"));
		assertTrue(mail.MailTo("kjb594@yahoo.com", "Blue", "If you see look at blue"));
	}
	
}
