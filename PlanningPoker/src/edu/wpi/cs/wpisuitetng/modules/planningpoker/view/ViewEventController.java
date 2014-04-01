/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team Rolling Thunder
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.planningpoker.view;

import java.awt.Component;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JPanel;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.gui.NewGameTab;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.overview.OverviewPanel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.overview.OverviewTable;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.overview.OverviewTreePanel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.session.SessionPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.controller.UpdateRequirementController;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.iterations.Iteration;


/**
 * Provides an interface for interaction with the main GUI elements
 * All actions on GUI elements should be conducted through this controller.
 * @version $Revision: 1.0 $
 * @author justinhess
 */

public class ViewEventController {
	private static ViewEventController instance = null;
	private MainView main = null;
	private ToolbarView toolbar = null;
	private OverviewTable overviewTable = null;
	private OverviewTreePanel overviewTree = null;
	private ArrayList<SessionPanel> listOfEditingPanels = new ArrayList<SessionPanel>();
	
	/**
	 * Sets the OverviewTable for the controller
	 * @param sessionTable a given OverviewTable
	 */
	public void setOverviewTable(OverviewTable sessionTable) {
		this.overviewTable = sessionTable;
	}

	/**
	 * Default constructor for ViewEventController.  Is protected to prevent instantiation.
	 */
	private ViewEventController() {}

	/**
	 * Returns the singleton instance of the vieweventcontroller.
	
	 * @return The instance of this controller. */
	public static ViewEventController getInstance() {
		if (instance == null) {
			instance = new ViewEventController();
		}
		return instance;
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
	 * opens a new tab for creating poker session
	 * This code is a mockup of RequirementManager.view.ViewEventController#creatRequirement
	 */
	public void createPlanningPokerSession() {
		//SessionPanel newSession = new SessionPanel(-1); // the issue is with requirementpanel.java in package
		JPanel panel = NewGameTab.createJPanel();
		main.addTab("New Session.", null, panel, "New Session");
		main.invalidate(); //force the tabbedpane to redraw.
		main.repaint();
		main.setSelectedComponent(panel);
	}


	/**
	 * Opens a child requirement panel to create the child requirement for the given parent.
	 * @param parentID
	 */
	public void createChildRequirement(int parentID) {
		SessionPanel newReq = new SessionPanel(parentID);
		main.addTab("Add Child Req.", null, newReq, "Add Child Requirement");
		main.invalidate(); //force the tabbedpane to redraw.
		main.repaint();
		main.setSelectedComponent(newReq);
	}
	/**
	 * Opens a new tab for the editing of a requirement
	 * @param toEdit the req to edit
	 */
	public void editRequirement(Requirement toEdit)
	{
		SessionPanel exists = null;
		
		// set time stamp for transactions
		toEdit.getHistory().setTimestamp(System.currentTimeMillis());
		
		
		for(SessionPanel panel : listOfEditingPanels)
		{
			if(panel.getDisplayRequirement() == toEdit)
			{
				exists = panel;
				break;
			}
		}	
		
		if(exists == null)
		{
			SessionPanel editPanel = new SessionPanel(toEdit);
			
			StringBuilder tabName = new StringBuilder();
			tabName.append(toEdit.getId()); 
			tabName.append(". ");
			int subStringLength = toEdit.getName().length() > 6 ? 7 : toEdit.getName().length();
			tabName.append(toEdit.getName().substring(0,subStringLength));
			if(toEdit.getName().length() > 6) tabName.append("..");
			
			main.addTab(tabName.toString(), null, editPanel, toEdit.getName());
			this.listOfEditingPanels.add(editPanel);
			main.invalidate();
			main.repaint();
			main.setSelectedComponent(editPanel);
		}
		else
		{
			main.setSelectedComponent(exists);
		}
	}




	

	/**
	
	 * @return toolbar */
	public ToolbarView getToolbar() {
		return toolbar;
	}
	
	/** 
	 * @return overviewTable */
	public OverviewTable getOverviewTable(){
		return overviewTable;
	}

	/**
	 * Removes the tab for the given JComponent
	 * @param comp the component to remove
	 */
	public void removeTab(JComponent comp)
	{
		if(comp instanceof SessionPanel)
		{
			if(!((SessionPanel)comp).readyToRemove()) return;
			this.listOfEditingPanels.remove(comp);

		}

		main.remove(comp);
	}

	/**Tells the table to update its listings based on the data in the requirement model
	 * 
	 */
	public void refreshTable() {
		overviewTable.refresh();
	}

	/**
	 * Returns an array of the currently selected rows in the table.
	
	 * @return the currently selected rows in the table */
	public int[] getTableSelection()
	{
		return overviewTable.getSelectedRows();
	}
	
	/**
	 * Returns the main view
	
	 * @return the main view */
	public MainView getMainView() {
		return main;
	}

	/**
	 * Assigns all currently selected rows to the backlog.
	 */
	public void assignSelectionToBacklog()
	{
		int[] selection = overviewTable.getSelectedRows();

		// Set to false to indicate the requirement is being newly created
		boolean created = false;

		for(int i = 0; i < selection.length; i++)
		{
			Requirement toSendToBacklog = (Requirement)overviewTable.getValueAt(selection[i], 1);
			toSendToBacklog.setIteration("Backlog");
			UpdateRequirementController.getInstance().updateRequirement(toSendToBacklog);
		}

		this.refreshTable();
		this.refreshTree();
	}

	/**
	 * Edits the currently selected requirement.  If more than 1 requirement is selected, does nothing.
	 */
	public void editSelectedRequirement()
	{
		int[] selection = overviewTable.getSelectedRows();

		if(selection.length != 1) return;

		Requirement toEdit = (Requirement)overviewTable.getValueAt(selection[0],1);
		
		editRequirement(toEdit);
	}

	/**
	 * Closes all of the tabs besides the overview tab in the main view.
	 */
	public void closeAllTabs() {

		int tabCount = main.getTabCount();

		for(int i = tabCount - 1; i >= 0; i--)
		{
			Component toBeRemoved = main.getComponentAt(i);

			if(toBeRemoved instanceof OverviewPanel) continue;

			if(toBeRemoved instanceof SessionPanel)
			{
				if(!((SessionPanel)toBeRemoved).readyToRemove()) continue;
				this.listOfEditingPanels.remove(toBeRemoved);
			}

			main.removeTabAt(i);
		}

		main.repaint();
	}

	/**
	 * Closes all the tabs except for the one that was clicked.
	 * 
	 */
	public void closeOthers() {
		int tabCount = main.getTabCount();
		Component selected = main.getSelectedComponent();

		for(int i = tabCount - 1; i >= 0; i--)
		{
			Component toBeRemoved = main.getComponentAt(i);

			if(toBeRemoved instanceof OverviewPanel){
				continue;}
			if(toBeRemoved == selected){
				continue;}

			if(toBeRemoved instanceof SessionPanel)
			{
				if(!((SessionPanel)toBeRemoved).readyToRemove()){
					break;}
				this.listOfEditingPanels.remove(toBeRemoved);
			}

			main.removeTabAt(i);
		}
		main.repaint();

	}
	
	/**
	 * Refreshes the EditRequirementPanel after creating a new child
	 * 
	 * @param newChild req that is being created
	 */
	public void refreshEditRequirementPanel(Requirement newChild) {
		for(SessionPanel newEditPanel : listOfEditingPanels)
		{
			if(newEditPanel.getDisplayRequirement() == newChild)
			{
				newEditPanel.fireRefresh();
				break;
			}
			
		}
		
	}

	
	/**
	 * Method getOverviewTree.
	
	 * @return OverviewTreePanel */
	public OverviewTreePanel getOverviewTree() {
		return overviewTree;
	}

	/**
	 * Method setOverviewTree.
	 * @param overviewTree OverviewTreePanel
	 */
	public void setOverviewTree(OverviewTreePanel overviewTree) {
		this.overviewTree = overviewTree;
	}
	
	public void refreshTree(){
		this.overviewTree.refresh();
	}
	
	public void editSelectedIteration() {
		/*DefaultMutableTreeNode selected = (DefaultMutableTreeNode)overviewTree.getTree().getLastSelectedPathComponent();
		if(selected.getUserObject() instanceof Iteration)
		{
			Iteration iter = ((Iteration)((DefaultMutableTreeNode)overviewTree.getTree().getLastSelectedPathComponent()).getUserObject());
		
			ViewEventController.getInstance().editIteration(iter);
		}*/
	}


	/**
	 * Method getListOfRequirementPanels.
	 * @return ArrayList<RequirementPanel>
	 */
	public ArrayList<SessionPanel> getListOfRequirementPanels() {
		return listOfEditingPanels;
	}
}
