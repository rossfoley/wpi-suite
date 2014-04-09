/*******************************************************************************
 * Copyright (c) 2014 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Team8s
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.overview;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSessionModel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.ViewEventController;

/**
 * @author Randy
 *
 */
public class ClosedOverviewTable extends OverviewTable {

	OverviewDetailPanel detailPanel;
	/**
	 * @param data
	 * @param columnNames
	 * @param openSessions
	 */
	public ClosedOverviewTable(Object[][] data, String[] columnNames, OverviewDetailPanel detailPanel) {
		super(data, columnNames);
		this.detailPanel = detailPanel;
		// TODO Auto-generated constructor stub
	}
	/**
	 * Retrieves all closed sessions
	 * @author randyacheson
	 */

	public List<PlanningPokerSession> getSessions() {
		List<PlanningPokerSession> sessions = PlanningPokerSessionModel.getInstance().getPlanningPokerSessions();
		List<PlanningPokerSession> openSessions = new ArrayList<PlanningPokerSession>();
		for (PlanningPokerSession session : sessions) {
			if (!session.isOpen()){
				openSessions.add(session);
			}
		}
		return openSessions;
	}
	
	/**
	 * @return This panel's OverviewDetailPanel
	 */
	public OverviewDetailPanel getDetailPanel() {
		return this.detailPanel;
	}
	
	/*
	public void displaySession() {

		ViewEventController.getInstance().displayDetailedSession(detailPanel);
		
	}
*/
}

