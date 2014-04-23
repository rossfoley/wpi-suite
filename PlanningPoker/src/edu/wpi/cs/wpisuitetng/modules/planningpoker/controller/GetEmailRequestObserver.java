/***S****************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.planningpoker.controller;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.EmailAddress;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;

/**
 * This observer handles responses to request for retrieving email addresses
 * 
 * @author Andrew Leonard
 *
 */
public class GetEmailRequestObserver implements RequestObserver {
	
	private GetEmailController controller;
	
	
	/**
	 * Constructs the observer given a GetEmailController
	 * @param controller the controller used to retrieve emails
	 */
	public GetEmailRequestObserver(GetEmailController controller){
		this.controller = controller;
	}
	
	/**
	 * Parse the emails out of the response body and pass them to the controller
	 * 
	 * @see edu.wpi.cs.wpisuitetng.network.RequestObserver#responseSuccess(edu.wpi.cs.wpisuitetng.network.models.IRequest)
	 */
	@Override
	public void responseSuccess(IRequest iReq) {
		// Convert the JSON array of decks to an email object array
		EmailAddress[] emailAddresses = EmailAddress.fromJsonArray(iReq.getResponse().getBody());
		
		// Pass these email addresses to the controller
		controller.receivedEmailAddresses(emailAddresses);
	}
	
	/**
	 * @see edu.wpi.cs.wpisuitetng.network.RequestObserver#responseError(edu.wpi.cs.wpisuitetng.network.models.IRequest)
	 */
	@Override
	public void responseError(IRequest iReq) {
		fail(iReq, null);
	}
	
	/**
	 * handle request fails
	 * @param ireq network request
	 * @param exception 
	 *
	 * @see edu.wpi.cs.wpisuitetng.network.RequestObserver#fail(edu.wpi.cs.wpisuitetng.network.models.IRequest, java.lang.Exception)
	 */
	@Override
	public void fail(IRequest iReq, Exception exception) {
		System.err.println("The request to retrieve email failed. FAIL!");
	}
	

}
