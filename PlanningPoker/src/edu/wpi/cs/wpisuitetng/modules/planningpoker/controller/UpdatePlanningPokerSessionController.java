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

import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

/**
 * This controller responds when the user clicks the Update button by
 * adding the contents of the planning poker session text fields to the model as a new
 * planning poker session.
 * @version $Revision: 1.0 $
 * @author rossfoley
 */
public class UpdatePlanningPokerSessionController {
	
	private static final UpdatePlanningPokerSessionController instance = 
			new UpdatePlanningPokerSessionController();
	private final UpdatePlanningPokerSessionRequestObserver observer;
	
	/**
	 * Construct an UpdateRequirementController for the given model, view pair
	 */
	private UpdatePlanningPokerSessionController() {
		observer = new UpdatePlanningPokerSessionRequestObserver(this);
	}
	
	/**
	
	 * @return the instance of the UpdateRequirementController or creates one if it does not
	 * exist. */
	public static UpdatePlanningPokerSessionController getInstance() {
		return instance;
	}

	/**
	 * This method updates a planning poker session to the server.
	 * @param newSession is the planning poker session to be updated to the server.
	 */
	public void updatePlanningPokerSession(PlanningPokerSession newSession) {
		// POST is update
		final Request request = Network.getInstance()
				.makeRequest("planningpoker/planningpokersession", HttpMethod.POST);
		request.setBody(newSession.toJSON()); // put the new session in the body of the request
		request.addObserver(observer); // add an observer to process the response
		request.send(); 
	}
}
