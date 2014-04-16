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

import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.Estimate;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

public class UpdateEstimateController {
	
	private static UpdateEstimateController instance;
	private UpdateEstimateRequestObserver observer;
	
	/**
	 * Construct an UpdateRequirementController for the given model, view pair
	 */
	private UpdateEstimateController() {
		observer = new UpdateEstimateRequestObserver(this);
	}
	
	/**
	
	 * @return the instance of the UpdateRequirementController or creates one if it does not
	 * exist. */
	public static UpdateEstimateController getInstance()
	{
		if(instance == null)
		{
			instance = new UpdateEstimateController();
		}
		
		return instance;
	}

	/**
	 * This method updates an estimate to the server.
	 * @param newSession is the estimate to be updated to the server.
	 */
	public void updateEstimate(Estimate newEstimate) 
	{
		final Request request = Network.getInstance().makeRequest("Advanced/planningpoker/planningpokersession/update-estimate", HttpMethod.POST); // POST == update
		request.setBody(newEstimate.toJSON()); // put the new estimate in the body of the request
		request.addObserver(observer); // add an observer to process the response
		request.send(); 
	}

}
