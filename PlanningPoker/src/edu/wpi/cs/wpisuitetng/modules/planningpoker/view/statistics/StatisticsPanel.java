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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SpringLayout;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.ISessionTab;

/** 
 *  This class is the panel that is shown when viewing statistics on a session
 */
public class StatisticsPanel extends JSplitPane implements ISessionTab {

	/**
	 * Sets up directory tree of all planning poker sessions
	 */
	private StatisticsDetailPanel detailPanel;
	private StatisticsReqTable reqTable;
	private JScrollPane tablePanel;
	
	int selectedReqID;
	JButton submitFinalEstimatesBtn = new JButton("Submit Final Estimates");
	JPanel reqOverviewTablePanel = new JPanel();
	SpringLayout reqOverviewLayout = new SpringLayout();
	
	private final PlanningPokerSession activeSession;
	
	public StatisticsPanel(PlanningPokerSession statisticsSession)
	{
		activeSession = statisticsSession;
		
		this.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		final String[] reqColumnNames = {"Requirement Name", "Your Vote", "Final Estimate"};
		final Object[][] reqData = {};

		// Create the user table panel and detail panel
		detailPanel = new StatisticsDetailPanel(activeSession, null);
		reqTable = new StatisticsReqTable(reqData, reqColumnNames, activeSession);
		tablePanel = new JScrollPane(reqTable);
		
		//set infoPanel to get estimate information for statistics
		reqTable.setDetailPanel(detailPanel);
		
		reqTable.getColumnModel().getColumn(0).setMinWidth(200); // Requirement Name
		reqTable.getColumnModel().getColumn(1).setMinWidth(100); // User Vote
		reqTable.getColumnModel().getColumn(1).setMaxWidth(100); // User Vote
		reqTable.getColumnModel().getColumn(2).setMinWidth(100); // Final Estimate
		reqTable.getColumnModel().getColumn(2).setMaxWidth(100); // Final Estimate

		reqOverviewTablePanel.setLayout(reqOverviewLayout);
		
		reqOverviewLayout.putConstraint(SpringLayout.SOUTH, submitFinalEstimatesBtn, -10, SpringLayout.SOUTH, reqOverviewTablePanel);
		reqOverviewLayout.putConstraint(SpringLayout.EAST, submitFinalEstimatesBtn, -10, SpringLayout.EAST, reqOverviewTablePanel);
		reqOverviewLayout.putConstraint(SpringLayout.SOUTH, tablePanel, -10, SpringLayout.NORTH, submitFinalEstimatesBtn);
		reqOverviewLayout.putConstraint(SpringLayout.EAST, tablePanel, -10, SpringLayout.EAST, reqOverviewTablePanel);
		reqOverviewLayout.putConstraint(SpringLayout.NORTH, tablePanel, 10, SpringLayout.NORTH, reqOverviewTablePanel);
		reqOverviewLayout.putConstraint(SpringLayout.WEST, tablePanel, 10, SpringLayout.WEST, reqOverviewTablePanel);
		
		submitFinalEstimatesBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				reqTable.updateFinalEstimates(activeSession);
			}
		});
		
		
		reqOverviewTablePanel.add(tablePanel);
		reqOverviewTablePanel.add(submitFinalEstimatesBtn);
		
		// Put the overview table and sidebar into the tab
		setLeftComponent(tablePanel);
		setRightComponent(detailPanel);
		
		updatePanel();
		setDividerLocation(400);
        setEnabled(true);
	}
	
	public void updatePanel()	{	
		// update each part of the split panel
		updateReqTable(activeSession);
	}

	private void updateReqTable(PlanningPokerSession session) {
		reqTable.refresh(session);
	}
	
	/**
	 * @return the detailPanel
	 */
	public StatisticsDetailPanel getDetailPanel() {
		return detailPanel;
	}
	
	/**
	 * @param detailPanel the detailPanel to set
	 */
	public void setDetailPanel(StatisticsDetailPanel detailPanel) {
		this.detailPanel = detailPanel;
	}
	
	/**
	 * @return the statistics user tree panel
	 */
	public StatisticsReqTable getStatisticsReqTable() {
		return reqTable;
	}
	
	/**
	 * @param endVotePanel the endVotePanel to set
	 */
	public void setStatisticsUserTable(StatisticsReqTable reqTable) {
		this.reqTable = reqTable;
	}
	
	/**
	 * Get the session that is being viewed 
	 * @return the session being voted on in this panel
	 */
	public PlanningPokerSession getDisplaySession() {
		return activeSession;
	}
	
}
