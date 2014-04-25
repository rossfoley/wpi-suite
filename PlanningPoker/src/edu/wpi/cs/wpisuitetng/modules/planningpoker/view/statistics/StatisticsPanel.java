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

import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import javax.swing.BorderFactory;
import javax.swing.DropMode;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SpringLayout;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.Estimate;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.ISessionTab;

/** 
 *  This class is the panel that is shown when viewing statistics on a session
 */
public class StatisticsPanel extends JSplitPane implements ISessionTab {

	/**
	 * Sets up directory tree of all planning poker sessions
	 */
	StatisticsDetailPanel detailPanel;
	StatisticsReqTable reqTable;
	JScrollPane tablePanel; 
	StatisticsInfoPanel infoPanel;
	
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
		detailPanel = new StatisticsDetailPanel(activeSession);
		reqTable = new StatisticsReqTable(reqData, reqColumnNames);
		tablePanel = new JScrollPane(reqTable);
		
		// initialize infoPanel
		//infoPanel = new StatisticsInfoPanel(activeSession);
		infoPanel = detailPanel.getInfoPanel();
		
		//set infoPanel to get estimate information for statistics
		reqTable.setInfoPanel(infoPanel);
		
		reqTable.getColumnModel().getColumn(0).setMinWidth(200); // Requirement Name
		reqTable.getColumnModel().getColumn(1).setMinWidth(100); // User Vote
		reqTable.getColumnModel().getColumn(1).setMaxWidth(100); // User Vote
		reqTable.getColumnModel().getColumn(2).setMinWidth(100); // Final Estimate
		reqTable.getColumnModel().getColumn(2).setMaxWidth(100); // Final Estimate

		//
		reqOverviewTablePanel.setLayout(reqOverviewLayout);
		
		reqOverviewLayout.putConstraint(SpringLayout.SOUTH, submitFinalEstimatesBtn, -10, SpringLayout.SOUTH, reqOverviewTablePanel);
		reqOverviewLayout.putConstraint(SpringLayout.EAST, submitFinalEstimatesBtn, -10, SpringLayout.EAST, reqOverviewTablePanel);
		reqOverviewLayout.putConstraint(SpringLayout.SOUTH, tablePanel, -10, SpringLayout.NORTH, submitFinalEstimatesBtn);
		reqOverviewLayout.putConstraint(SpringLayout.EAST, tablePanel, -10, SpringLayout.EAST, reqOverviewTablePanel);
		reqOverviewLayout.putConstraint(SpringLayout.NORTH, tablePanel, 10, SpringLayout.NORTH, reqOverviewTablePanel);
		reqOverviewLayout.putConstraint(SpringLayout.WEST, tablePanel, 10, SpringLayout.WEST, reqOverviewTablePanel);
		
		submitFinalEstimatesBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//StatisticsReqTable reqTable = 
				reqTable.updateFinalEstimates(activeSession);
			}
		});
		
		
		reqOverviewTablePanel.add(tablePanel);
		reqOverviewTablePanel.add(submitFinalEstimatesBtn);
		
		// Put the overview table and sidebar into the tab
		this.setLeftComponent(tablePanel);
		this.setRightComponent(detailPanel);
		
		this.updatePanel();
		
	
		// Makes the split pane divide 50/50 for each portion
		//final Dimension d = new Dimension(300, 100);
        //detailPanel.setMinimumSize(d);
       // detailPanel.setPreferredSize(d);
       // reqTable.setMinimumSize(d);
       // reqTable.setPreferredSize(d);
		
		this.setDividerLocation(400);


        this.setEnabled(true);
	}
	
	public void updatePanel()	{	
		// update each part of the split panel
		//updateInfoPanel(activeSession);
		updateReqTable(activeSession);
		
		// change the visibility of the top buttons
		//setButtonVisibility(activeSession);
		ViewEventController.getInstance().getPlanningPokerSessionButtonsPanel().disableStatisticsButton();
	}	
	
	//private void updateInfoPanel(PlanningPokerSession session) {
	//	infoPanel.refresh(session);
	//}

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
