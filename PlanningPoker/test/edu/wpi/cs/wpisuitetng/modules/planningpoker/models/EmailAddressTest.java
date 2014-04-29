package edu.wpi.cs.wpisuitetng.modules.planningpoker.models;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.gson.Gson;

public class EmailAddressTest {
	
	@Test
	public void createEmailAddressTest() {
		assertNotNull(new EmailAddress());
	}
	@Test
	public void toJsonTest() {
		EmailAddress emailAdressToTest = new EmailAddress();
		emailAdressToTest.setEmail("TestEmail@TestEmail.edu");
		emailAdressToTest.setOwnerName("Test Owner");
		String gson = emailAdressToTest.toJSON();
		EmailAddress eTest = EmailAddress.fromJson(gson);
		assertEquals(emailAdressToTest.getEmail(), EmailAddress.fromJson(gson).getEmail());
		assertEquals(emailAdressToTest.getOwnerName(), EmailAddress.fromJson(gson).getOwnerName());

	}
	
}
