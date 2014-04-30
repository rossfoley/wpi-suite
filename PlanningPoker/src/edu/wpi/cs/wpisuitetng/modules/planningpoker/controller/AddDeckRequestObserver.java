/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.planningpoker.controller;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.Deck;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;
import edu.wpi.cs.wpisuitetng.network.models.ResponseModel;

/**
 * This observer is called when a response is received from a request
 * to the server to add a deck.
 * @author amandaadkins
 * @version 1.0
 */
public class AddDeckRequestObserver implements RequestObserver {
	private final AddDeckController controller;
	
	/**
	 * Adds the given controller to the observer
	 * @param controller The given controller
	 */
	public AddDeckRequestObserver(AddDeckController controller) {
		this.controller = controller;
	}
	
	/**
	 * Parse the deck that was received from the server then pass them to
	 * the controller.
	 * 
	 * @see edu.wpi.cs.wpisuitetng.network.RequestObserver#responseSuccess
	 * (edu.wpi.cs.wpisuitetng.network.models.IRequest)
	 */
	public void responseSuccess(IRequest iReq) {
		// Get the response to the given request
		final ResponseModel response = iReq.getResponse();
		System.out.println("Sent successfully");
		// Parse the session out of the response body
		final Deck addedDeck = Deck.fromJson(response.getBody());
	}
	
	/**
	 * Takes an action if the response results in an error.
	 * Specifically, outputs that the request failed.
	 * @param iReq IRequest
	
	 * @see edu.wpi.cs.wpisuitetng.network.RequestObserver#responseError(IRequest) */
	//@Override
	public void responseError(IRequest iReq) {
		System.err.println("The request to add a deck failed. Response Error");
	}
	
	/**
	 * Takes an action if the response fails.
	 * Specifically, outputs that the request failed.
	 * @param iReq IRequest
	 * @param exception Exception
	
	 * @see edu.wpi.cs.wpisuitetng.network.RequestObserver#fail(IRequest, Exception) */
	//@Override
	public void fail(IRequest iReq, Exception exception) {
		System.err.println("The request to add a deck failed. FAIL!");
	}
	
	
	
}
