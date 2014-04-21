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

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.util.List;
import java.util.Set;

import java.awt.Dimension;

import javax.swing.JSplitPane;
import javax.swing.JScrollPane;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession.SessionState;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSessionModel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.ViewEventController;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.RequirementModel;

public class StatisticsDetailPanel extends JSplitPane {
	PlanningPokerSession currentSession;
	StatisticsReqTable reqTable;
	StatisticsInfoPanel infoPanel;
	JScrollPane tablePanel;

	public StatisticsDetailPanel () {

		this.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		
		String[] columnNames = {"Requirement Name", "Your Vote", "Final Estimate"};
		Object[][] data = {};
		
		// Create the info panel and table panel
		reqTable = new StatisticsReqTable(data, columnNames);
		infoPanel = new StatisticsInfoPanel();
		tablePanel = new JScrollPane(reqTable);
		
		reqTable.getColumnModel().getColumn(0).setMinWidth(200); // Requirement Name
		reqTable.getColumnModel().getColumn(1).setMinWidth(100); // User Vote
		reqTable.getColumnModel().getColumn(1).setMaxWidth(100); // User Vote
		reqTable.getColumnModel().getColumn(2).setMinWidth(100); // Final Estimate
		reqTable.getColumnModel().getColumn(2).setMaxWidth(100); // Final Estimate
		
		// Put the info panel and table panel into the split pane
		this.setLeftComponent(tablePanel);
		this.setRightComponent(infoPanel);
		this.setResizeWeight(0.5); 

		ViewEventController.getInstance().setStatisticsInfoPanel(infoPanel);
		ViewEventController.getInstance().setStatisticsReqTable(reqTable);
		
		// Makes the split pane divide 50/50 for each portion
		Dimension d = new Dimension(100, 100);
        infoPanel.setMinimumSize(d);
        infoPanel.setPreferredSize(d);
        tablePanel.setMinimumSize(d);
        
        // Disable the split pane from being movable
        // Why that's the method name I have no idea
        //this.setEnabled(false);
	}
	
	public void updatePanel(final PlanningPokerSession session)	{

		this.currentSession = session;
		
		// update each part of the split panel
		updateInfoPanel(session);
		updateReqTable(session);
		
		// change the visibility of the top buttons
		setButtonVisibility(session);
	}	
	
	private void updateInfoPanel(PlanningPokerSession session) {
		infoPanel.refresh(session);
	}

	private void updateReqTable(PlanningPokerSession session) {
		reqTable.refresh(session);
	}

	private void setButtonVisibility(PlanningPokerSession session) {
		// TODO
		// Check if the buttons should appear
		/*
			btnEdit.isVisible(false);
			if (session.isEditable()) {
				btnEdit.isVisible(true);
			}
		*/
	}
	
	public PlanningPokerSession getCurrentSession() {
		
		return this.currentSession;
	}
}
