/*******************************************************************************
 * Copyright (c) 2014 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Team8s
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.statistics;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.ViewEventController;

/**
 * @author rossfoley
 * @version $Revision: 1.0 $
 */
public class StatisticsPanel extends JSplitPane {

	/**
	 * Sets up directory tree of all planning poker sessions
	 */
	private StatisticsDetailPanel detailPanel;
	private StatisticsUserTable userTable;
	public StatisticsPanel()
	{

		// Create the user table panel and detail panel
		detailPanel = new StatisticsDetailPanel();
		userTable = new StatisticsUserTable();

		
		// Put the overview table and sidebar into the tab
		this.setLeftComponent(detailPanel);
		this.setRightComponent(userTable);

		ViewEventController.getInstance().setStatisticsUserTable(userTable);
		ViewEventController.getInstance().setOverviewDetailPanel(detailPanel);
		
		

	}
	/**
	 * @return the detailPanel
	 */
	public OverviewDetailPanel getDetailPanel() {
		return detailPanel;
	}
	/**
	 * @param detailPanel the detailPanel to set
	 */
	public void setDetailPanel(OverviewDetailPanel detailPanel) {
		this.detailPanel = detailPanel;
	}
	/**
	 * @return the endVotePanel
	 */
	public OverviewEndVotePanel getEndVotePanel() {
		return EndVotePanel;
	}
	/**
	 * @param endVotePanel the endVotePanel to set
	 */
	public void setEndVotePanel(OverviewEndVotePanel endVotePanel) {
		EndVotePanel = endVotePanel;
	}

/*
	public void setRightComponentToEndVotePanel() {
		
		this.setRightComponent(EndVotePanel);
		ViewEventController.getInstance().setOverviewEndVotePanel(EndVotePanel);
		this.repaint();
		this.updateUI();
	}
	public void setRightComponentToDetailPanel() {
		this.setRightComponent(detailPanel);
		ViewEventController.getInstance().setOverviewDetailPanel(detailPanel);
		this.repaint();
		this.updateUI();
		}
*/
	
}
