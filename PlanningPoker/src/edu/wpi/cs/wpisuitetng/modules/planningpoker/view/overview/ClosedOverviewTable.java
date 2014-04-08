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

/**
 * @author Randy
 *
 */
public class ClosedOverviewTable extends OverviewTable {

	/**
	 * @param data
	 * @param columnNames
	 * @param openSessions
	 */
	public ClosedOverviewTable(Object[][] data, String[] columnNames) {
		super(data, columnNames);
		// TODO Auto-generated constructor stub
	}
	/**
	 * description
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
}

