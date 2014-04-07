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
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;
//import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSessionModel;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

import java.awt.event.ActionListener;

/**
 * @author Amanda Adkins
 *
 */
public class GetDeckController implements ActionListener {
	private GetDeckRequestObserver observer;
	private static GetDeckController instance;
	
	/**
	 * Constructs the controller given a Deck
	 */
	private GetDeckController() {
		observer = new GetDeckRequestObserver(this);
	}
	
	
	/**
	
	 * @return the instance of the GetDeckController or creates one if it does not
	 * exist. */
	public static GetDeckController getInstance()
	{
		if(instance == null)
		{
			instance = new GetDeckController();
		}
		
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
		final Request request = Network.getInstance().makeRequest("planningpoker/deck", HttpMethod.GET); // GET == read
		request.addObserver(observer); // add an observer to process the response
		request.send(); // send the request
	}
	
	/**
	 * Sends an HTTP request to retrieve all decks
	 */
	public void retrieveDecks() {
		final Request request = Network.getInstance().makeRequest("planningpoker/deck", HttpMethod.GET); // GET == read
		request.addObserver(observer); // add an observer to process the response
		request.send(); // send the request
	}
	
	/**
	 * Add the given requirements to the local model (they were received from the core).
	 * This method is called by the GetRequirementsRequestObserver
	 * 
	 * @param requirements array of requirements received from the server
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
