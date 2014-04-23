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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.exceptions.BadRequestException;
import edu.wpi.cs.wpisuitetng.exceptions.NotFoundException;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.EntityManager;
import edu.wpi.cs.wpisuitetng.modules.Model;

public class EmailAddressEntityManager  implements EntityManager<EmailAddress> {

	Data db;
	//HashMap<String, ArrayList<EmailAddress>> clientsUpdated = new HashMap<String, ArrayList<EmailAddress>>();

	public EmailAddressEntityManager(Data db) {
		this.db = db;
	}

	@Override
	public EmailAddress makeEntity(Session s, String content) throws WPISuiteException {
		final EmailAddress newEmail = EmailAddress.fromJson(content);
		if(!db.save(newEmail, s.getProject())) {
			throw new WPISuiteException();
		}
		return newEmail;
	}

	@Override
	public EmailAddress[] getEntity(Session s, String email) throws NotFoundException, WPISuiteException {
		try {
			return db.retrieve(EmailAddress.class, "email", email, s.getProject()).toArray(new EmailAddress[0]);
		} catch (WPISuiteException e) {
			throw new NotFoundException(email);
		}
	}

	/**
	 * Retrieves all Email Addresses from the database
	 * @param s the current session
	 * @return array of all of the emailAddresses involved in the current session
	 */
	@Override
	public EmailAddress[] getAll(Session s) throws WPISuiteException {
		EmailAddress [] allEmails = db.retrieveAll(new EmailAddress(), s.getProject()).toArray(new EmailAddress[0]);
		return allEmails;
	}

	/**
	 * Method update.
	 * @param session Session
	 * @param content String
	 * @return EmailAddress 
	 * @throws WPISuiteException
	 * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#update(Session, String)
	 */
	@Override
	public EmailAddress update(Session session, String content) throws WPISuiteException {
		EmailAddress updatedEmail = EmailAddress.fromJson(content);
		/*
		 * Because of the disconnected objects problem in db4o, we can't just save EmailAddresses.
		 * We have to get the original defect from db4o, copy properties from updatedSession,
		 * then save the original EmailAddress again.
		 */
		List<Model> oldEmails = db.retrieve(EmailAddress.class, "OwnerName", updatedEmail.getOwnerName(), session.getProject());
		if(oldEmails.size() < 1 || oldEmails.get(0) == null) {
			System.out.println("Problem with finding by the emailAddress");
			throw new BadRequestException("EmailAddress with email address does not exist.");
		}

		EmailAddress existingEmail = (EmailAddress)oldEmails.get(0);		
		existingEmail.copyFrom(updatedEmail);

		if(!db.save(existingEmail, session.getProject())) {
			System.out.println("Couldn't save the updated email");
			throw new WPISuiteException();
		}

		return existingEmail;
	}

	/* (non-Javadoc)
	 * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#save(edu.wpi.cs.wpisuitetng.Session, edu.wpi.cs.wpisuitetng.modules.Model)
	 */
	@Override
	public void save(Session s, EmailAddress model) throws WPISuiteException {
		db.save(model);
	}

	@Override
	public boolean deleteEntity(Session s, String email) throws WPISuiteException {
		return db.delete(getEntity(s, email)[0]) != null;
	}

	@Override
	public void deleteAll(Session s) throws WPISuiteException {
		db.deleteAll(new EmailAddress(), s.getProject());
	}

	@Override
	public int Count() throws WPISuiteException {
		return db.retrieveAll(new EmailAddress()).size();
	}

	@Override
	public String advancedGet(Session s, String[] args) throws WPISuiteException {
		return null;
	}


	@Override
	public String advancedPut(Session s, String[] args, String content)
			throws WPISuiteException {
		return null;
	}

	@Override
	public String advancedPost(Session s, String string, String content)
			throws WPISuiteException {
		return null;
	}
}
