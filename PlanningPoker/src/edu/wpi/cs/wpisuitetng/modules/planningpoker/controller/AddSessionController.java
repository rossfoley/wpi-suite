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
 * adding the contents of the Session text fields to the model as a new
 * Session.
 * @version $Revision: 1.0 $
 * @author randyacheson and kevinbarry
 */
public class AddSessionController{
	
	private static AddSessionController instance;
	private AddSessionRequestObserver observer;
	
	/**
	 * Construct an AddSessionController for the given model, view pair
	
	
	 */
	private AddSessionController() {
		observer = new AddSessionRequestObserver(this);
	}
	
	/**
	
	 * @return the instance of the AddSessionController or creates one if it does not
	 * exist. */
	public static AddSessionController getInstance()
	{
		if(instance == null)
		{
			instance = new AddSessionController();
		}
		
		return instance;
	}

	/**
	 * This method adds a Session to the server.
	 * @param newSession is the PlanningPokerSession to be added to the server.
	 */
	public void addSession(PlanningPokerSession newSession) 
	{
		final Request request = Network.getInstance().makeRequest("planningpoker/PlanningPokerSession", HttpMethod.PUT); // PUT == create
		request.setBody(newSession.toJSON()); // put the new Planning Poker Session in the body of the request
		request.addObserver(observer); // add an observer to process the response
		request.send(); 
	}
}
