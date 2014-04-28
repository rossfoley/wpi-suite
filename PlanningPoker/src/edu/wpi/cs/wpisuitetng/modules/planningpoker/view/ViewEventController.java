/*******************************************************************************
 * Copyright (c) 2014 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Team8s
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.planningpoker.view;

import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.buttons.PlanningPokerSessionButtonsPanel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.overview.OverviewDetailPanel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.overview.OverviewPanel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.overview.OverviewReqTable;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.overview.OverviewTreePanel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.session.PlanningPokerSessionTab;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.statistics.StatisticsPanel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.voting.VotingPage;

/**
 * Provides an interface for interaction with the main GUI elements
 * All actions on GUI elements should be conducted through this controller.
 * @version $Revision: 1.0 $
 */

public class ViewEventController {
	private static ViewEventController instance = null;
	private MainView main = null;
	private ToolbarView toolbar = null;
	private OverviewTreePanel overviewTreePanel = null;
	private OverviewDetailPanel overviewDetailPanel = null;
	private OverviewReqTable overviewReqTable;
	private PlanningPokerSessionButtonsPanel planningPokerSessionButtonsPanel;
	private Map<PlanningPokerSession, JComponent> openSessionTabHashTable = new HashMap<>();

	/**
	 * Default constructor for ViewEventController.  Is protected to prevent instantiation.
	 */
	private ViewEventController() {}

	/**
	 * Returns the singleton instance of the vieweventcontroller.
	 * @return The instance of this controller.
	 */
	public static ViewEventController getInstance() {
		if (instance == null) {
			instance = new ViewEventController();
		}
		return instance;
	}
	
	/**
	 * Sets the overview tree for the given view
	 * @param overviewTreePanel	The overview tree
	 */
	public void setOverviewTree(OverviewTreePanel overviewTreePanel) {
		this.overviewTreePanel = overviewTreePanel;
	}
	
	/**
	 * Sets the OverviewDetailPanel for the given view.
	 * @param overviewDetailPanel	The detail panel
	 */
	public void setOverviewDetailPanel(OverviewDetailPanel overviewDetailPanel) {
		this.overviewDetailPanel = overviewDetailPanel;
	}
	
	/**
	 * Sets the requirement table for the given view.
	 * @param overviewReqTable	The requirement table to set
	 */	
	public void setOverviewReqTable(OverviewReqTable overviewReqTable) {
		this.overviewReqTable = overviewReqTable;
	}
	
	/**
	 * Gets the requirement table for the given view.
	 * @return The requirement table
	 */
	public OverviewReqTable getOverviewReqTable(){
		return overviewReqTable;
	}
	
	/**
	 * Sets the toolbar button panel for the given view.
	 * @param buttonsPanel	The buttons panel to set
	 */	
	public void setPlanningPokerSessionButtonsPanel(PlanningPokerSessionButtonsPanel buttonsPanel) {
		planningPokerSessionButtonsPanel = buttonsPanel;
	}
	
	/**
	 * Sets the main view to the given view.
	 * @param mainview MainView
	 */
	public void setMainView(MainView mainview) {
		main = mainview;
	}

	/**
	 * Sets the toolbarview to the given toolbar
	 * @param tb the toolbar to be set as active.
	 */
	public void setToolBar(ToolbarView tb) {
		toolbar = tb;
		toolbar.repaint();
	}
	
	/**
	 * Opens a new tab for creating poker session
	 * This code is a mock-up of RequirementManager.view.ViewEventController#creatRequirement
	 */
	public void createPlanningPokerSession() {
		final PlanningPokerSessionTab panel = new PlanningPokerSessionTab();
		main.addTab("New Session.", null, panel, "New Session");
		main.invalidate(); //force the tabbedpane to redraw.
		main.repaint();
		main.setSelectedComponent(panel);
	}

	/**
	 * @return toolbar	The instance of the toolbar currently being displayed
	 */
	public ToolbarView getToolbar() {
		return toolbar;
	}

	/**
	 * @return OverviewTreePanel
	 */
	public OverviewTreePanel getOverviewTreePanel() {
		return overviewTreePanel;
	}
	
	/**
	 * @return OverviewDetailPanel
	 */
	public OverviewDetailPanel getOverviewDetailPanel() {
		return overviewDetailPanel;
	}
	
	/**
	 * @param overviewEndVotePanel
	 */
	public PlanningPokerSessionButtonsPanel getPlanningPokerSessionButtonsPanel() {
		return planningPokerSessionButtonsPanel;
	}

	/**
	 * Removes the tab for the given JComponent
	 * @param comp the component to remove
	 */
	public void removeTab(JComponent comp)
	{
		// Check if the tab is a PlanningPokerSession create/edit tab
		if (comp instanceof PlanningPokerSessionTab) {
			// Only remove if it is ready to remove
			if(!((PlanningPokerSessionTab)comp).readyToRemove()) {
				return;
			}
			openSessionTabHashTable.remove(((PlanningPokerSessionTab)comp).getDisplaySession());
		}
		// Check if the tab is a session voting tab
		if (comp instanceof VotingPage) {
			planningPokerSessionButtonsPanel.enableVoteButton();
			planningPokerSessionButtonsPanel.enableEndVoteButton();
			openSessionTabHashTable.remove(((VotingPage)comp).getDisplaySession());
		}
		// Check if the tab is viewing a sessions statistics
		if (comp instanceof StatisticsPanel) {
			openSessionTabHashTable.remove(((StatisticsPanel)comp).getDisplaySession());
		}
		
		main.remove(comp);
	}

	/**
	 * Tells the table to update its listings based on the data in the requirement model
	 * 
	 */
	public void refreshTable() {
		overviewTreePanel.refresh();
	}
	
	/**
	 * Returns the main view
	 * @return the main view
	 */
	public MainView getMainView() {
		return main;
	}

	/**
	 * Closes all of the tabs besides the overview tab in the main view.
	 * 
	 */
	public void closeAllTabs() {
		final int tabCount = main.getTabCount();

		for (int i = tabCount - 1; i >= 0; i--) {
			Component toBeRemoved = main.getComponentAt(i);
			if(toBeRemoved instanceof OverviewPanel) continue;
			main.removeTabAt(i);
		}

		openSessionTabHashTable = new HashMap<PlanningPokerSession, JComponent>();
		main.repaint();
	}

	/**
	 * Closes all the tabs except for the one that was clicked.
	 * 
	 */
	public void closeOthers() {
		final int tabCount = main.getTabCount();
		final Component selected = main.getSelectedComponent();

		for (int i = tabCount - 1; i >= 0; i--) {
			Component toBeRemoved = main.getComponentAt(i);

			if (toBeRemoved instanceof OverviewPanel) {
				continue;
			}
			
			if (toBeRemoved == selected) {
				continue;
			}

			main.removeTabAt(i);
		}
		main.repaint();

	}
	
	/**
	 * Displays the details of the session that is clicked on 
	 */
	public void displayDetailedSession(PlanningPokerSession displaySession)
	{
		if (overviewDetailPanel.isOnSelectionTable()){
			overviewDetailPanel.putReqTableBack();
		}
		
		overviewDetailPanel.updatePanel(displaySession);
	}
	
	public void sendEstimatesFromSession(){
		overviewDetailPanel.replaceTable();
	}

	/** 
	 * Opens a Edit Session tab for the input session if a tab is not already
	 * open for the session.
	 * @param toEdit	The session to edit
	 */
	public void editSession(PlanningPokerSession toEdit)
	{
		// Check if the session is already open in a tab
		if (!openSessionTabHashTable.containsKey(toEdit)) {
			// eventually want to add session to edit as an argument
			final PlanningPokerSessionTab editPanel = new PlanningPokerSessionTab(toEdit);
			
			final StringBuilder tabName = new StringBuilder();
			final int subStringLength = toEdit.getName().length() > 6 ? 7 : toEdit.getName().length();
			tabName.append(toEdit.getName().substring(0,subStringLength));
			if(toEdit.getName().length() > 6) tabName.append("..");
			
			main.addTab(tabName.toString(), null, editPanel, toEdit.getName());
			openSessionTabHashTable.put(editPanel.getDisplaySession(), editPanel);
			main.invalidate();
			main.repaint();
			main.setSelectedComponent(editPanel);
		}
		else
		{
			main.setSelectedComponent(openSessionTabHashTable.get(toEdit));
		}		
			
	}

	/**
	 * Opens a Voting tab for the input session if a tab is not already
	 * open for the session.
	 * @param toVoteOn	The session to vote on
	 */
	public void voteOnSession(PlanningPokerSession toVoteOn) {
		// Check if the session is already open in a tab
		if (!openSessionTabHashTable.containsKey(toVoteOn)) {
			final VotingPage votingPanel = new VotingPage(toVoteOn);
			
			final StringBuilder tabName = new StringBuilder();
			final int subStringLength = toVoteOn.getName().length() > 6 ? 7 : toVoteOn.getName().length();
			tabName.append(toVoteOn.getName().substring(0,subStringLength));
			if(toVoteOn.getName().length() > 6) tabName.append("..");
			
			main.addTab(tabName.toString(), null, votingPanel, toVoteOn.getName());
			openSessionTabHashTable.put(votingPanel.getDisplaySession(), votingPanel);
			main.invalidate();
			main.repaint();
			main.setSelectedComponent(votingPanel);
		}
		else {
			main.setSelectedComponent(openSessionTabHashTable.get(toVoteOn));
		}
	}
	
	/**
	 * Opens a Statistics tab for the input session if a tab is not already
	 * open for the session.
	 * @param viewStats	The session to view statistics for
	 */
	public void openStatisticsTab(PlanningPokerSession viewStats){
		// Check if the session is already open in a tab
		if (!openSessionTabHashTable.containsKey(viewStats)) {
			final StatisticsPanel statisticsPanel = new StatisticsPanel(viewStats);
			
			final StringBuilder tabName = new StringBuilder();
			final int subStringLength = viewStats.getName().length() > 6 ? 7 : viewStats.getName().length();
			tabName.append(viewStats.getName().substring(0,subStringLength));
			if (viewStats.getName().length() > 6) {
				tabName.append("..");
			}
			
			main.addTab(tabName.toString(), null, statisticsPanel, viewStats.getName());
			main.invalidate(); //force the tabbedpane to redraw.
			main.repaint();
			main.setSelectedComponent(statisticsPanel);
		} else {
			main.setSelectedComponent(openSessionTabHashTable.get(viewStats));
		}
	}
	
}
