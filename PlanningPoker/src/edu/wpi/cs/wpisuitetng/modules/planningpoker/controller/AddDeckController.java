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
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

/**
 * This controller responds when a deck is added 
 * and sends a request over the network to save the deck 
 * @author amandaadkins
 * @version 1.0
 */
public class AddDeckController {
	
	private static final AddDeckController instance = new AddDeckController();
	private final AddDeckRequestObserver observer;
	
	/**
	 * Construct an AddDeckController for the given model, view pair
	 */
	private AddDeckController() {
		observer = new AddDeckRequestObserver(this);
	}
	
	/**
	 * @return the instance of the AddSessionController or creates one if it does not
	 * exist. */
	public static AddDeckController getInstance() {
		return instance;
	}
	
	/**
	 * This method adds a Deck to the server.
	 * @param newDeck is the Deck to be added to the server.
	 */
	public void addDeck(Deck newDeck) {
		final Request request = Network.getInstance()
				.makeRequest("planningpoker/deck", HttpMethod.PUT); // PUT is create
		request.setBody(newDeck.toJSON()); // put the new session in the body of the request
		request.addObserver(observer); // add an observer to process the response
		request.send(); 
	}
}
