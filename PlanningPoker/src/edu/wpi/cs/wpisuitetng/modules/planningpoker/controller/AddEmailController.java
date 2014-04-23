/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.planningpoker.controller;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.EmailAddress;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

/**
 * This controller responds when an email is added and sends a request over the network to save the deck 
 * 
 * @author Andrew Leonard
 */
public class AddEmailController {
	private static AddEmailController instance;
	private AddEmailRequestObserver observer;
	
	/**
	 * Construct an AddEmailController for the given model, view pair
	
	 */
	private AddEmailController() {
		observer = new AddEmailRequestObserver(this);
	}
	/**
	
	 * @return the instance of the AddEmailController or creates one if it does not
	 * exist. */
	public static AddEmailController getInstance()
	{
		if(instance == null)
		{
			instance = new AddEmailController();
		}
		
		return instance;
	}
	
	/**
	 * This method adds an email to the server.
	 * @param newEmail is the email to be added to the server.
	 */
	public void addEmail(EmailAddress newEmail) 
	{
		final Request request = Network.getInstance().makeRequest("planningpoker/emailAddress", HttpMethod.PUT); // PUT == create
		request.setBody(newEmail.toJSON()); // put the new Planning Poker Session in the body of the request
		request.addObserver(observer); // add an observer to process the response
		request.send(); 
	}
	
	

}
