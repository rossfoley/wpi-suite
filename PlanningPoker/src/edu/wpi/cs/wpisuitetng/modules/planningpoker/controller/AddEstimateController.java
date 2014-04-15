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

import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.Estimate;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;

public class AddEstimateController {
	
	private static AddEstimateController instance;
	private AddEstimateRequestObserver observer;
	
	/**
	 * Construct an AddSessionController for the given model, view pair
	
	
	 */
	private AddEstimateController() {
		observer = new AddEstimateRequestObserver(this);
	}
	
	/**
	
	 * @return the instance of the AddSessionController or creates one if it does not
	 * exist. */
	public static AddEstimateController getInstance()
	{
		if(instance == null)
		{
			instance = new AddEstimateController();
		}
		
		return instance;
	}

	/**
	 * This method adds an Estimate to the server.
	 * @param newEstimate is the Estimate to be added to the server.
	 */
	public void addEstimate(Estimate newEstimate) 
	{
		final Request request = Network.getInstance().makeRequest("planningpoker/estimate", HttpMethod.PUT); // PUT == create
		request.setBody(newEstimate.toJSON()); // put the new estimate in the body of the request
		request.addObserver(observer); // add an observer to process the response
		request.send(); 
	}
	
	
}
