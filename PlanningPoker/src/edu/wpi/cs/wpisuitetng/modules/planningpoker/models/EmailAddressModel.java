package edu.wpi.cs.wpisuitetng.modules.planningpoker.models;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.controller.AddDeckController;
<<<<<<< HEAD
port edu.wpi.cs.wpisuitetng.modules.planningpoker.controller.AddEmailController;
=======
import edu.wpi.cs.wpisuitetng.modules.planningpoker.controller.AddEmailController;
>>>>>>> In EmailAddressModel adds the import for addEmailController as welll as changes the addEmailAddress line to addEmail
import edu.wpi.cs.wpisuitetng.modules.planningpoker.controller.AddSessionController;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.ViewEventController;

public class EmailAddressModel extends AbstractListModel {

	/** 
	 * the list in which all the emails for a project are contained
	 */
	private List<EmailAddress> emailAddresses;
	//private int nextID; // the next available id for a EmailAddress
	
	private static EmailAddressModel instance; // static object to allow the EmailAddressModel 
	
	
	
	/**
	 * Constructs an list of EmailAddresses
	 */
	private EmailAddressModel(){
		emailAddresses = new ArrayList<EmailAddress>();
	}
	
	/** 
	 * if the EmailAddressModel has not yet been created, create it
	 * @return the instance of the EmailAddressModel 
	 */
	public static EmailAddressModel getInstance(){
		if (instance == null){
			instance = new EmailAddressModel();
		}
		return instance;
	}

	@Override
	public int getSize() {
		return emailAddresses.size();
	}

	// revisit this
	@Override
	public Object getElementAt(int index) {
		return emailAddresses.get(emailAddresses.size() - 1 - index);
	}
	
	/**
	 * @param emailAddress email address to add to the project
	 */
	public void addEmail(EmailAddress emailAddress){
		// add the EmailAddress
		emailAddresses.add(emailAddress);
		try 
		{
			AddEmailController.getInstance().addEmail(emailAddress);
		}
		catch(Exception e)
		{

		}
	}
	/**
	 * Adds the given array of email Addresses to the list
	 * @param emails the array of email addresses to add
	 */
	public void addEmailAddresses(EmailAddress[] emails) {
		for (int i = 0; i < emails.length; i++) {
			this.emailAddresses.add(emails[i]);
		};
	}
	
	/**
	 * Finds the email address with the given string as its address
	 * 
	 * @param email address to look for. Formated as a string.
	 * @return the email Address with the given String as its address
	 */
	public EmailAddress getEmailAddress(String emailAddress){
		for (EmailAddress e:emailAddresses){
			if (e.getEmail().equals(emailAddress)){
				return e;
			}
		}
		return null;
	}
	/*
	public int getNextID(){
		return this.nextID++;
	}
	*/
	/**
	 * 
	 * @return list of emails stored in the EmailAddressModel
	 */
	public List<EmailAddress> getEmailAddresses() {
		return emailAddresses;
	}
	
	/**
	 * empties the emailAddresses
	 */
	public void emptyModel(){
		emailAddresses.clear();
	}
	
}
