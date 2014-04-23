/*******************************************************************************
 * Copyright (c) 2014 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Team8s
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.planningpoker.controller;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.EmailAddress;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

public class UpdateEmailController {
	
	private static UpdateEmailController instance;
	private final UpdateEmailRequestObserver observer;
	
	/**
	 * Construct an UpdateEmailController for the given model, view pair
	 */
	private UpdateEmailController() {
		observer = new UpdateEmailRequestObserver(this);
	}
	
	/**
	
	 * @return the instance of the UpdateEmailController or creates one if it does not
	 * exist. */
	public static UpdateEmailController getInstance()
	{
		if(instance == null)
		{
			instance = new UpdateEmailController();
		}
		
		return instance;
	}

	/**
	 * This method updates an email to the server.
	 * @param newEmail is the email to be updated to the server.
	 */
	public void updateEmailAddress(EmailAddress newEmailAddress) 
	{
		final Request request = Network.getInstance().makeRequest("planningpoker/emailAddress", HttpMethod.POST); // POST == update
		request.setBody(newEmailAddress.toJSON()); // put the new estimate in the body of the request
		request.addObserver(observer); // add an observer to process the response
		request.send(); 
	}

}
