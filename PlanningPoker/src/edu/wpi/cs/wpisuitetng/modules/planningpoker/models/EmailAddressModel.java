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

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;




import edu.wpi.cs.wpisuitetng.modules.planningpoker.controller.AddEmailController;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.controller.GetEmailController;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.controller.UpdateEmailController;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.notifications.Mailer;
/**
 * @author theteam8s
 * Interacts with the controller for adding, updating and removing email addresses.
 */
public class EmailAddressModel extends AbstractListModel {

	/** 
	 * the list in which all the emails for a project are contained
	 */
	private final List<EmailAddress> emailAddresses;

	
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
	 * Goes into the updateEmailController and calls updateEmailAddress with the email. 
	 * While it says currentEmail it goes through and finds an email address with the same user as 
	 * currentEmail and removes it, adding currentEmail in its place.
	 * @param currentEmail the email address you wish to update
	 */
	public void updateEmailAddress(EmailAddress currentEmail){
		// add the emailAddress
		removeEmailAddress(currentEmail.getOwnerName());
		emailAddresses.add(currentEmail);
		try
		{
			UpdateEmailController.getInstance().updateEmailAddress(currentEmail);
		}
		catch(Exception e)
		{

		}
	}
	
	
	/**
	 * Removes the Email with the given ID
	 * 
	 * @param user user of the email address to be removed.
	 */
	public void removeEmailAddress(String user){
		// iterate through list of PlanningPokerSessions until id of project is found
		for (int i=0; i < emailAddresses.size(); i++){
			if (emailAddresses.get(i).getOwnerName().equals(user)) {
				// remove the id
				emailAddresses.remove(i);
				break;
			}
		}
	}
	
	
	/**
	 * Adds the given array of email Addresses to the list
	 * @param emails the array of email addresses to add
	 */
	public void addEmailAddresses(EmailAddress[] emails) {
		for (int i = 0; i < emails.length; i++) {
			emailAddresses.add(emails[i]);
		}
	}
	
	/**
	 * Finds the email address with the given string as its address
	 * 
	 * @param emailAddress to look for. Formated as a string.
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
