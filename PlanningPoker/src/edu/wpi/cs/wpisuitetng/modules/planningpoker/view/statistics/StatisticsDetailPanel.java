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

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JScrollPane;
import javax.swing.SpringLayout;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;


public class StatisticsDetailPanel extends JSplitPane {
	private PlanningPokerSession currentSession;
	private StatisticsUserTable userTable;
	private JScrollPane tablePanel;
	private StatisticsInfoPanel infoPanel;

	private JButton submitFinalEstimatesBtn = new JButton("Submit Final Estimates");
	private JPanel reqOverviewTablePanel = new JPanel();
	private SpringLayout reqOverviewLayout = new SpringLayout();

	public StatisticsDetailPanel (PlanningPokerSession session) {

		currentSession = session;
		setOrientation(JSplitPane.VERTICAL_SPLIT);
		
		// Create the voted user table
		userTable = new StatisticsUserTable(currentSession, -1);
		userTable.getColumnModel().getColumn(0).setMinWidth(200); // User name
		userTable.getColumnModel().getColumn(1).setMaxWidth(100); // User Vote
		tablePanel = new JScrollPane(userTable);

		reqOverviewTablePanel.setLayout(reqOverviewLayout);
		
		reqOverviewLayout.putConstraint(SpringLayout.SOUTH, submitFinalEstimatesBtn, -10, SpringLayout.SOUTH, reqOverviewTablePanel);
		reqOverviewLayout.putConstraint(SpringLayout.EAST, submitFinalEstimatesBtn, -10, SpringLayout.EAST, reqOverviewTablePanel);
		reqOverviewLayout.putConstraint(SpringLayout.SOUTH, tablePanel, -10, SpringLayout.NORTH, submitFinalEstimatesBtn);
		reqOverviewLayout.putConstraint(SpringLayout.EAST, tablePanel, -10, SpringLayout.EAST, reqOverviewTablePanel);
		reqOverviewLayout.putConstraint(SpringLayout.NORTH, tablePanel, 10, SpringLayout.NORTH, reqOverviewTablePanel);
		reqOverviewLayout.putConstraint(SpringLayout.WEST, tablePanel, 10, SpringLayout.WEST, reqOverviewTablePanel);
		
		reqOverviewTablePanel.add(tablePanel);
		reqOverviewTablePanel.add(submitFinalEstimatesBtn);
		
		// Create the info panel (display requirement details and voting statistics
		infoPanel = new StatisticsInfoPanel(currentSession);
	
		// Put the info panel and table panel into the split pane
		setLeftComponent(infoPanel);
		setRightComponent(reqOverviewTablePanel);
		setResizeWeight(0.5); 
		
		// Makes the split pane divide 50/50 for each portion
		Dimension d = new Dimension(100, 100);
        infoPanel.setMinimumSize(d);
        infoPanel.setPreferredSize(d);
        tablePanel.setMinimumSize(d);
        
        updatePanel(-1);	// Initialize with no requirement selected
	}
	
	/**
	 * Updates all panels with details for the input requirement
	 * @param requirementID	The ID of the requirement to display details for
	 */
	public void updatePanel(int requirementID)	{
		// update each part of the split panel
		infoPanel.refresh(currentSession);
		infoPanel.setRequirementID(requirementID);
		userTable.updateRequirement(requirementID);
	}
	
}
