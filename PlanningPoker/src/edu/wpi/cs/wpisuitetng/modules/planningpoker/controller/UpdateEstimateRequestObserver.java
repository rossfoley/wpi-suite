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

import javax.swing.JOptionPane;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.MainView;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.ViewEventController;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;

/**
 * This observer handles responses to request for all estimates
 * 
 * @author TheTeam8s
 * @version 1.0
 */
public class UpdateEstimateRequestObserver implements RequestObserver {
	
	private final UpdateEstimateController controller;
	
	/**
	 * Constructs the observer given an UpdateEstimateController
	 * @param controller the controller used to add an Estimate
	 */
	public UpdateEstimateRequestObserver(UpdateEstimateController controller) {
		this.controller = controller;
	}
	
	/**
	 * Parse the request that was received from the server then pass them to
	 * the controller.
	 * 
	 * @see edu.wpi.cs.wpisuitetng.network.RequestObserver#responseSuccess
	 * (edu.wpi.cs.wpisuitetng.network.models.IRequest)
	 */
	@Override
	public void responseSuccess(IRequest iReq) {
		// Get the response to the given request
		final String response = iReq.getResponse().getBody();
		if (response != null && !response.trim().equals("")) {
			// Close the voting tab and show a popup
			MainView main = ViewEventController.getInstance().getMainView();
			main.removeTabAt(main.getSelectedIndex());
			JOptionPane.showMessageDialog(null, response);
		}
	}
	
	/**
	 * Takes an action if the response results in an error.
	 * Specifically, outputs that the request failed.
	 * @param iReq IRequest
	
	 * @see edu.wpi.cs.wpisuitetng.network.RequestObserver#responseError(IRequest) */
	@Override
	public void responseError(IRequest iReq) {
		System.err.println(iReq.getResponse().getStatusMessage());
		System.err.println("The request to update an Estimate failed.");
	}

	/**
	 * Takes an action if the response fails.
	 * Specifically, outputs that the request failed.
	 * @param iReq IRequest
	 * @param exception Exception
	
	 * @see edu.wpi.cs.wpisuitetng.network.RequestObserver#fail(IRequest, Exception) */
	@Override
	public void fail(IRequest iReq, Exception exception) {
		System.err.println("The request to update an Estimate failed.");
	}
}
