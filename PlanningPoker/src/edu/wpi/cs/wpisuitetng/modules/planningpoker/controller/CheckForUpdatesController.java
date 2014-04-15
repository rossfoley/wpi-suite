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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSessionModel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.ViewEventController;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;

/**
 * @author rossfoley
 *
 */
public class CheckForUpdatesController implements ActionListener {
	
	private static CheckForUpdatesController instance;
	
	private CheckForUpdatesController() {
		
	}
	
	/**
	 * @return the instance of the CheckForUpdatesController or creates one if it does not
	 * exist. */
	public static CheckForUpdatesController getInstance()
	{
		if (instance == null) {
			instance = new CheckForUpdatesController();
		}
		
		return instance;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		checkForUpdates();
	}
	
	/**
	 * Check with the server to see if there are any updates to Planning Poker Sessions
	 */
	public void checkForUpdates() {
		final Request request = Network.getInstance().makeRequest("Advanced/planningpoker/planningpokersession/check-for-updates", HttpMethod.GET);
		request.addObserver(new RequestObserver() {
			@Override
			public void responseSuccess(IRequest iReq) {
				String response = iReq.getResponse().getBody();
				PlanningPokerSession[] updates = PlanningPokerSession.fromJsonArray(response);
				for (PlanningPokerSession update : updates) {
					PlanningPokerSessionModel model = PlanningPokerSessionModel.getInstance();
					PlanningPokerSession existing = model.getPlanningPokerSession(update.getUuid());
					if (existing != null) {
						model.removePlanningPokerSession(update.getUuid());
					}
					model.addCachedPlanningPokerSession(update);
				}
				
				if (updates.length > 0) {
					ViewEventController.getInstance().getOverviewTreePanel().refresh();
				}
			}
			
			@Override
			public void responseError(IRequest iReq) {
				System.out.println("Check for updates failed!");
			}
			
			@Override
			public void fail(IRequest iReq, Exception exception) {
				// TODO Auto-generated method stub
				
			}
		}); // add an observer to process the response
		request.send();
	}

}