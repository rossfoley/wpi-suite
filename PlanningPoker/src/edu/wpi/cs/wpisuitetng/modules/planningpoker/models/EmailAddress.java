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



import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.modules.AbstractModel;
/**
 * Class for email adresses. It contains two strings: an emailAdress and a userName
 * @author theTeam8s
 * @version 1.0
 *
 */
public class EmailAddress extends AbstractModel {
	private String emailAddress;
	private String userName;
	
	/**
	 * default constructor for an emailAddress. Takes no values and sets the email address and owner to empty strings.
	 */
	public EmailAddress() {
		emailAddress = "";
		userName = "";
	}
	/** 
	 * converts email address object to a JSON
	 * @see edu.wpi.cs.wpisuitetng.modules.Model#toJSON()
	 */
	public String toJSON() {
		return new Gson().toJson(this, EmailAddress.class);
	}
	
	/** parses an email address object from a Json string
	 * 
	 * @param json json-encoded email address 
	 * @return EmailAddress object that was encoded in json string
	 */
	public static EmailAddress fromJson(String json) {
		final Gson parser = new Gson();
		return parser.fromJson(json, EmailAddress.class);
	}
	
	/**
	 * Returns an array of Email Addresses parsed from the given JSON-encoded
	 * string.
	 * 
	 * @param json
	 *            string containing a JSON-encoded array of EmailAddress
	
	 * @return an array of Email Addresses deserialized from the given JSON string */
	public static EmailAddress[] fromJsonArray(String json) {
		final Gson parser = new Gson();
		return parser.fromJson(json, EmailAddress[].class);
	}

	@Override
	public void save() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Boolean identify(Object o) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 
	 * @return the email address
	 */
	public String getEmail() {
		return emailAddress;
	}
	/**
	 * 
	 * @param emailAddress what to set the new emailAddress to
	 */
	public void setEmail(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	
	
	/**
	 * 
	 * @return the name of the user who owns this email address
	 */
	public String getOwnerName() {
		return userName;
	}
	/**
	 *
	 * @param name of the user to associate with this email
	 */
	public void setOwnerName(String name) {
		userName = name;
	}
	/**
	 * Takes an email address and changes the values of this to the given EmailAddress
	 * @param updatedEmail Email to copy from
	 */
	public void copyFrom(EmailAddress updatedEmail) {
		emailAddress = updatedEmail.getEmail();
		userName = updatedEmail.getOwnerName();
		
	}
	
	
	
}
