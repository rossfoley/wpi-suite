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

import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.UUID;

import javax.swing.BorderFactory;
import javax.swing.DropMode;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.Estimate;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.ViewEventController;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.overview.OverviewTable;

/**
 * @author cassiehamlin
 * @version $Revision: 1.0 $
 */
public class StatisticsPanel extends JSplitPane {

	/**
	 * Sets up directory tree of all planning poker sessions
	 */
	StatisticsDetailPanel detailPanel;
	StatisticsUserTable userTable;
	JScrollPane tablePanel; 
	
	private final PlanningPokerSession activeSession;
	
	private Requirement requirement;
	private final LinkedList<Estimate> estimates = new LinkedList<Estimate>();
	private JPanel reqDetails;
	
	public StatisticsPanel(PlanningPokerSession statisticsSession)
	{
		activeSession = statisticsSession;
		
		this.setOrientation(JSplitPane.VERTICAL_SPLIT);
		final String[] userColumnNames = {"User", "Estimate"};
		final Object[][] userData = {};

		// Create the user table panel and detail panel
		detailPanel = new StatisticsDetailPanel(activeSession);
		userTable = new StatisticsUserTable(userData, userColumnNames);
		tablePanel = new JScrollPane(userTable);
		
		userTable.getColumnModel().getColumn(0).setMinWidth(200); // User Name
		userTable.getColumnModel().getColumn(1).setMinWidth(300); // User Vote
		userTable.getColumnModel().getColumn(1).setMaxWidth(500); // User Vote

		
		// Put the overview table and sidebar into the tab
		this.setTopComponent(detailPanel);
		this.setBottomComponent(tablePanel);

		ViewEventController.getInstance().setStatisticsUserTable(userTable);
		ViewEventController.getInstance().setStatisticsDetailPanel(detailPanel);
		
		// Makes the split pane divide 50/50 for each portion
		final Dimension d = new Dimension(200, 200);
        detailPanel.setMinimumSize(d);
        detailPanel.setPreferredSize(d);
        userTable.setMinimumSize(d);

        this.setEnabled(false);
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
	public StatisticsUserTable getStatisticsUserTable() {
		return userTable;
	}
	/**
	 * @param endVotePanel the endVotePanel to set
	 */
	public void setStatisticsUserTable(StatisticsUserTable userTable) {
		this.userTable = userTable;
	}
	public PlanningPokerSession getDisplaySession() {
		return activeSession;
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
