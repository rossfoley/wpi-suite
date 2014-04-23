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

import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.Deck;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.DeckListModel;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

import java.awt.event.ActionListener;

/**
 *  This controller coordinates retrieving all of the decks
 * from the server.
 * @author amandaadkins
 * @version 1.0
 */
public class GetDeckController implements ActionListener {
	private static final GetDeckController instance = new GetDeckController();
	private final GetDeckRequestObserver observer;
	
	/**
	 * Constructs the controller given a Deck
	 */
	private GetDeckController() {
		observer = new GetDeckRequestObserver(this);
		GetUserController.getInstance().retrieveUsers();

	}
	
	
	/**
	 * @return the instance of the GetDeckController
	 */
	public static GetDeckController getInstance() {
		return instance;
	}
	
	/**
	 * Sends an HTTP request to store a requirement when the
	 * update button is pressed
	 * @param e ActionEvent
	
	 * @see java.awt.event.ActionListener#actionPerformed(ActionEvent) */
	@Override
	public void actionPerformed(ActionEvent e) {
		// Send a request to the core to save this requirement
		final Request request = Network.getInstance()
				.makeRequest("planningpoker/deck", HttpMethod.GET); // GET is read
		request.addObserver(observer); // add an observer to process the response
		request.send(); // send the request
	}
	
	/**
	 * Sends an HTTP request to retrieve all decks
	 */
	public void retrieveDecks() {
		final Request request = Network.getInstance()
				.makeRequest("planningpoker/deck", HttpMethod.GET); // GET is read
		request.addObserver(observer); // add an observer to process the response
		request.send(); // send the request
	}
	
	/**
	 * Add the given decks to the local model (they were received from the core).
	 * This method is called by the GetRequirementsRequestObserver
	 * @param pokerDecks list of decks received from the server
	 */
	
	public void receivedDecks(Deck[] pokerDecks) {
		// Empty the local model to eliminate duplications
		DeckListModel.getInstance().emptyModel();
		
		// Make sure the response was not null
		if (pokerDecks != null) {
			
			// add the requirements to the local model
			DeckListModel.getInstance().addDecks(pokerDecks);
		}
	}

}
