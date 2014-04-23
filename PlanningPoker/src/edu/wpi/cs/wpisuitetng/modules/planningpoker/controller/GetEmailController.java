/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.planningpoker.controller;

import java.awt.event.ActionEvent;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.EmailAddress;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.EmailAddressModel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

import java.awt.event.ActionListener;

/**
 *  This controller coordinates retrieving all of the email addresses
 * from the server.
 * 
 * @author Andrew Leonard
 *
 */
public class GetEmailController implements ActionListener {
	private final GetEmailRequestObserver observer;
	private static GetEmailController instance;
	
	/**
	 * Constructs the controller to get an email address
	 */
	private GetEmailController() {
		observer = new GetEmailRequestObserver(this);
	}
	
	
	/**
	
	 * @return the instance of the GetEmailController or creates one if it does not
	 * exist. */
	public static GetEmailController getInstance()
	{
		if(instance == null)
		{
			instance = new GetEmailController();
		}
		
		return instance;
	}
	
	/**
	 * Sends an HTTP request to store an email when the
	 * update button is pressed
	 * @param e ActionEvent
	
	 * @see java.awt.event.ActionListener#actionPerformed(ActionEvent) */
	@Override
	public void actionPerformed(ActionEvent e) {
		// Send a request to the core to save this requirement
		final Request request = Network.getInstance().makeRequest("planningpoker/emailAddress", HttpMethod.GET); // GET == read
		request.addObserver(observer); // add an observer to process the response
		request.send(); // send the request
	}
	
	/**
	 * Sends an HTTP request to retrieve all emails
	 */
	public void retrieveEmails() {
		final Request request = Network.getInstance().makeRequest("planningpoker/emailAddress", HttpMethod.GET); // GET == read
		request.addObserver(observer); // add an observer to process the response
		request.send(); // send the request
	}
	
	/**
	 * Add the email addresses to the local model (they were received from the core).
	 * This method is called by the GetEmailRequestObserver
	 * 
	 * @param emailAddresses array of requirements received from the server
	 */
	
	public void receivedEmailAddresses(EmailAddress[] emailAddresses) {
		// Empty the local model to eliminate duplications
		EmailAddressModel.getInstance().emptyModel();
		
		// Make sure the response was not null
		if (emailAddresses != null) {
			
			// add the email addresses to the local model
			EmailAddressModel.getInstance().addEmailAddresses(emailAddresses);
		}
	}

}
