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

import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.buttons.PlanningPokerSessionButtonsPanel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.overview.OverviewDetailPanel;
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
	 * Sets the toolbar button panel for the given view.
	 * @param buttonsPanel	The buttons panel to set
	 */	
	public void setPlanningPokerSessionButtonsPanel(PlanningPokerSessionButtonsPanel buttonsPanel) {
		planningPokerSessionButtonsPanel = buttonsPanel;
	}
	
	/** 
	 * disable all session specific buttons
	 */
	public void disableButtons(){
		planningPokerSessionButtonsPanel.disableAllButtons();
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
	public void setToolbar(ToolbarView tb) {
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
		planningPokerSessionButtonsPanel.disableAllButtons();
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
	 * Getter for the buttons panel toolbar
	 * @return	The Buttons panel toolbar
	 */
	public PlanningPokerSessionButtonsPanel getPlanningPokerSessionButtonsPanel() {
		return planningPokerSessionButtonsPanel;
	}

	/**
	 * Removes the tab for the given JComponent
	 * @param comp the component to remove
	 * @return	If the tab was successfully removed
	 */
	public boolean removeTab(JComponent comp)
	{
		// Check if the tab is a PlanningPokerSession create/edit tab
		if (comp instanceof PlanningPokerSessionTab) {
			// Only remove if it is ready to remove
			if(!((PlanningPokerSessionTab)comp).readyToRemove()) {
				return false;
			}
		}
		// Check if the tab contains a planning poker session
		if (comp instanceof ISessionTab) {
			openSessionTabHashTable.remove(((ISessionTab)comp).getDisplaySession());
		}
		
		main.remove(comp);
		disableButtons();
		return true;
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
	 * Displays the details of the session that is clicked on 
	 */
	public void displayDetailedSession(PlanningPokerSession displaySession)
	{
		if (overviewDetailPanel.isOnSelectionTable()){
			overviewDetailPanel.putReqTableBack();
		}
		
		overviewDetailPanel.updatePanel(displaySession);
	}
	
	/**
	 * Opens the input session for the specified operation
	 * @param session	The session to open
	 * @param tabType	The desired tab type
	 */
	private void openNewSessionTab(PlanningPokerSession session, ViewMode tabType) {
		final JComponent sessionComp;
		// Create the panel based on the input ViewMode
		switch(tabType) {
			case EDITING:
				sessionComp = new PlanningPokerSessionTab(session);
				break;
			case VOTING:
				sessionComp = new VotingPage(session);
				break;
			case STATISTICS:
				sessionComp = new StatisticsPanel(session);
				break;
			default:	// The view type is invalid,
				throw new IllegalArgumentException("Cannot create session tab. ViewMode is invalid");
		}
		
		final StringBuilder tabName = new StringBuilder();
		final int subStringLength = session.getName().length() > 6 ? 7 : session.getName().length();
		tabName.append(session.getName().substring(0,subStringLength));
		if (session.getName().length() > 6) {
			tabName.append("..");
		}
		
		main.addTab(tabName.toString(), null, sessionComp, session.getName());
		openSessionTabHashTable.put(((ISessionTab) sessionComp).getDisplaySession(), sessionComp);
		main.invalidate(); //force the tabbedpane to redraw.
		main.repaint();
		main.setSelectedComponent(sessionComp);
		
		planningPokerSessionButtonsPanel.disableAllButtons();
	}
	
	/**
	 * Opens the input session for the specified operation
	 * This will close the existing tab if it is of a different ViewMode.
	 * If they are the same ViewMode, it will simply set that tab as the selected component
	 * @param session	The session to open
	 * @param tabType	The desired tab type
	 */
	public void openSessionTab(PlanningPokerSession session, ViewMode tabType) {
		// If the session is already opened
		if (openSessionTabHashTable.containsKey(session)) {
			// Retrieve the current JComponent and ViewMode for this session
			JComponent sessionTab = openSessionTabHashTable.get(session);
			ViewMode currViewMode = getComponentSessionViewMode(sessionTab);
			// If the current and desired ViewModes are the same, open as desired
			main.setSelectedComponent(sessionTab);
			// Otherwise close the existing tab, and reopen as desired
			if (!currViewMode.equals(tabType)) {
				// Only open the new tab if the existing tab is successfully removed
				if (removeTab(sessionTab)) {
					openNewSessionTab(session, tabType);
					planningPokerSessionButtonsPanel.disableAllButtons();
				}
			}
		}
		// Otherwise the tab does not exist yet. Create it and open
		else {
			openNewSessionTab(session, tabType);
		}
	}

	/**
	 * Getter for the hash table of tabs open for sessions
	 * @return	The hash table mapping sessions to the display components
	 */
	public Map<PlanningPokerSession, JComponent> getOpenSessionTabHashTable() {
		return openSessionTabHashTable;
	}
	
	/**
	 * Returns what type of tab a session is open as
	 * @param comp	The JComponent to check
	 * @return	The session ViewMode of the component
	 */
	public ViewMode getComponentSessionViewMode(JComponent comp) {
		// Make sure the component exists
		if (comp == null) {
			return ViewMode.NONE;
		}
		// If the session is open for editing 
		if (comp instanceof PlanningPokerSessionTab) {
			return ViewMode.EDITING;
		}
		// If the session is open for voting
		if (comp instanceof VotingPage) {
			return ViewMode.VOTING;
		}
		// If the session is open for statistics viewing 
		if (comp instanceof StatisticsPanel) {
			return ViewMode.STATISTICS;
		}
		// Otherwise it is not a planning poker session related component!
		return ViewMode.NONE;
	}
	
}
