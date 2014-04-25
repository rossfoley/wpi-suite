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

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JScrollPane;
import javax.swing.SpringLayout;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession.SessionState;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSessionModel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.ViewEventController;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.RequirementModel;

public class StatisticsDetailPanel extends JSplitPane {
	PlanningPokerSession currentSession;
	StatisticsUserTable userTable;
	StatisticsInfoPanel infoPanel;
	JScrollPane tablePanel;

	int selectedReqID;
	JButton submitFinalEstimatesBtn = new JButton("Submit Final Estimates");
	JPanel reqOverviewTablePanel = new JPanel();
	SpringLayout reqOverviewLayout = new SpringLayout();

	public StatisticsDetailPanel (PlanningPokerSession session) {

		currentSession = session;
		this.setOrientation(JSplitPane.VERTICAL_SPLIT);
		
		final String[] userColumnNames = {"User", "Estimate"};
		final Object[][] userData = {};
		
		// Create the info panel and table panel
		userTable = new StatisticsUserTable(userData, userColumnNames);
		tablePanel = new JScrollPane(userTable);
		infoPanel = new StatisticsInfoPanel(currentSession);
		
		
		userTable.getColumnModel().getColumn(0).setMinWidth(200); // Requirement Name
		userTable.getColumnModel().getColumn(1).setMinWidth(100); // User Vote
		userTable.getColumnModel().getColumn(1).setMaxWidth(100); // User Vote

		reqOverviewTablePanel.setLayout(reqOverviewLayout);
		
		reqOverviewLayout.putConstraint(SpringLayout.SOUTH, submitFinalEstimatesBtn, -10, SpringLayout.SOUTH, reqOverviewTablePanel);
		reqOverviewLayout.putConstraint(SpringLayout.EAST, submitFinalEstimatesBtn, -10, SpringLayout.EAST, reqOverviewTablePanel);
		reqOverviewLayout.putConstraint(SpringLayout.SOUTH, tablePanel, -10, SpringLayout.NORTH, submitFinalEstimatesBtn);
		reqOverviewLayout.putConstraint(SpringLayout.EAST, tablePanel, -10, SpringLayout.EAST, reqOverviewTablePanel);
		reqOverviewLayout.putConstraint(SpringLayout.NORTH, tablePanel, 10, SpringLayout.NORTH, reqOverviewTablePanel);
		reqOverviewLayout.putConstraint(SpringLayout.WEST, tablePanel, 10, SpringLayout.WEST, reqOverviewTablePanel);
		
		/*submitFinalEstimatesBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				StatisticsReqTable reqTable = 
				//reqTable.updateFinalEstimates(currentSession);
			}
		});*/
		
		
		reqOverviewTablePanel.add(tablePanel);
		reqOverviewTablePanel.add(submitFinalEstimatesBtn);
	
		// Put the info panel and table panel into the split pane
		this.setLeftComponent(infoPanel);
		this.setRightComponent(reqOverviewTablePanel);
		this.setResizeWeight(0.5); 

		ViewEventController.getInstance().setStatisticsInfoPanel(infoPanel);
		ViewEventController.getInstance().setStatisticsUserTable(userTable);
		
		// Makes the split pane divide 50/50 for each portion
		final Dimension d = new Dimension(100, 100);
        infoPanel.setMinimumSize(d);
        infoPanel.setPreferredSize(d);
        tablePanel.setMinimumSize(d);
        
        this.updatePanel();
        
	}
	
	public void updatePanel()	{

		
		// update each part of the split panel
		updateInfoPanel(currentSession);
		updateReqTable(currentSession);
		
		// change the visibility of the top buttons
		setButtonVisibility(currentSession);
	}	
	
	private void updateInfoPanel(PlanningPokerSession session) {
		infoPanel.refresh(session);
	}

	private void updateReqTable(PlanningPokerSession session) {
		userTable.refresh(session);
		
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
		
		return currentSession;
	}
	
	public void setRequirementID(int ID) {
		infoPanel.setRequirementID(ID);
	}

	public StatisticsInfoPanel getInfoPanel() {
		return infoPanel;
	}
	
	
}
