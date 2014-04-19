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

/**
 * This observer handles responses to request for all decks
 * 
 * @author amandaadkins
 * @version 1.0
 */
public class GetDeckRequestObserver implements RequestObserver {
	
	private final GetDeckController controller;
	
	
	/**
	 * Constructs the observer given a GetDckController
	 * @param controller the controller used to retrieve decks
	 */
	public GetDeckRequestObserver(GetDeckController controller) {
		this.controller = controller;
	}
	
	/**
	 * Parse the decks out of the response body and pass them to the controller
	 * 
	 * @see edu.wpi.cs.wpisuitetng.network.RequestObserver#responseSuccess
	 * (edu.wpi.cs.wpisuitetng.network.models.IRequest)
	 */
	@Override
	public void responseSuccess(IRequest iReq) {
		// Convert the JSON array of decks to a Deck object array
		final Deck[] decks= Deck.fromJsonArray(iReq.getResponse().getBody());
		
		// Pass these Decks to the controller
		controller.receivedDecks(decks);
	}
	
	/**
	 * @see edu.wpi.cs.wpisuitetng.network.RequestObserver#responseError
	 * (edu.wpi.cs.wpisuitetng.network.models.IRequest)
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
	 * @see edu.wpi.cs.wpisuitetng.network.RequestObserver#fail
	 * (edu.wpi.cs.wpisuitetng.network.models.IRequest, java.lang.Exception)
	 */
	@Override
	public void fail(IRequest iReq, Exception exception) {

	}
	

}
