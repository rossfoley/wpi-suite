
package edu.wpi.cs.wpisuitetng.modules.planningpoker.view;

import java.awt.Component;
import java.util.ArrayList;
import java.util.UUID;

import javax.swing.JComponent;
import javax.swing.JPanel;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.gui.PlanningPokerSessionTab;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.gui.voting.VotingPage;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSessionModel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.overview.ClosedOverviewTable;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.overview.OverviewDetailPanel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.overview.OverviewPanel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.overview.OverviewTable;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.requirements.RequirementPanel;


/**
 * Provides an interface for interaction with the main GUI elements
 * All actions on GUI elements should be conducted through this controller.
 * @version $Revision: 1.0 $
 */

public class ViewEventController {
	private static ViewEventController instance = null;
	private MainView main = null;
	private ToolbarView toolbar = null;
	private OverviewTable openOverviewTable = null;
	private OverviewTable closedOverviewTable = null;
	private OverviewDetailPanel openOverviewDetailPanel = null;
	private OverviewDetailPanel closedOverviewDetailPanel = null;
	private ArrayList<PlanningPokerSessionTab> listOfEditingPanels = new ArrayList<PlanningPokerSessionTab>();
	private ArrayList<VotingPage> listOfVotingPanels = new ArrayList<VotingPage>();
	
	/**
	 * Sets the OpenOverviewTable for the controller
	 * @param sessionTable a given OpenOverviewTable
	 */
	public void setOpenOverviewTable(OverviewTable sessionTable) {
		this.openOverviewTable = sessionTable;
	}

	/**
	 * Sets the ClosedOverviewTable for the controller
	 * @param sessionTable a given ClosedOverviewTable
	 */
	public void setClosedOverviewTable(OverviewTable sessionTable) {
		this.closedOverviewTable = sessionTable;
	}
	
	/**
	 * Sets the OpenOverviewDetailPanel for the controller
	 * @param sessionTable a given OverviewDetailPanel
	 */
	public void setOpenOverviewDetailPanel(OverviewDetailPanel sessionPanel) {
		this.openOverviewDetailPanel = sessionPanel;
	}
	
	/**
	 * Sets the ClosedOverviewDetailPanel for the controller
	 * @param sessionTable a given OverviewDetailPanel
	 */
	public void setClosedOverviewDetailPanel(OverviewDetailPanel sessionPanel) {
		this.closedOverviewDetailPanel= sessionPanel;
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
		PlanningPokerSessionTab panel = new PlanningPokerSessionTab();
		main.addTab("New Session.", null, panel, "New Session");
		main.invalidate(); //force the tabbedpane to redraw.
		main.repaint();
		main.setSelectedComponent(panel);
	}

	/**
	
	 * @return toolbar */
	public ToolbarView getToolbar() {
		return toolbar;
	}
	
	/** 
	 * @return openOverviewTable */
	public OverviewTable getOpenOverviewTable(){
		return openOverviewTable;
	}
	
	/** 
	 * @return ClosedOverviewTable */
	public OverviewTable getClosedOverviewTable(){
		return closedOverviewTable;
	}

	/**
	 * Removes the tab for the given JComponent
	 * @param comp the component to remove
	 */
	public void removeTab(JComponent comp)
	{
		// Check if the tab is a planningPokerSession tab
		if (comp instanceof PlanningPokerSessionTab) {
			// Only remove if it is ready to remove
			if(!((PlanningPokerSessionTab)comp).readyToRemove())
				return;
			this.listOfEditingPanels.remove(comp);
		}
		// Check if the tab is a voteOnSession tab
		if (comp instanceof VotingPage) {
			this.listOfVotingPanels.remove(comp);
		}		
		
		main.remove(comp);
	}

	/**Tells the table to update its listings based on the data in the requirement model
	 * 
	 */
	public void refreshTable() {
		openOverviewTable.refresh();
		closedOverviewTable.refresh();
	}

	/**
	 * Returns an array of the currently selected rows in the table.
	 * @return the currently selected rows in the table */
	
	/*
	public int[] getTableSelection()
	{
		return OverviewTable.getSelectedRows();
	}
	*/
	
	/**
	 * Returns the main view
	 * @return the main view */
	public MainView getMainView() {
		return main;
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


			main.removeTabAt(i);
		}
		main.repaint();

	}

	
	/**
	 * Displays the details of the session that is clicked on 
	 */
	public void displayDetailedSession(OverviewTable table)
	{
		//JComponent selected = (JComponent) MainView.getSelectedComponent();
		int[] selection = table.getSelectedRows();
		
		if (selection.length != 1) return;
		
		UUID sessionID = (UUID) table.getValueAt(selection[0],0);
		PlanningPokerSession displaySession = PlanningPokerSessionModel.getInstance().getPlanningPokerSession(sessionID);
		
		OverviewDetailPanel detailPanel = table.getDetailPanel();
		detailPanel.updatePanel(displaySession);
		
	}

	
	/**
	 * Opens a new tab for the editing of a session
	 * @param toEdit the req to edit
	 */
	public void editSession(PlanningPokerSession toEdit)
	{
		PlanningPokerSessionTab exists = null;
		
		// Check if the session is already open in a tab
		for(PlanningPokerSessionTab panel : listOfEditingPanels)
		{
			if(panel.getDisplaySession() == toEdit)
			{
				exists = panel;
				break;
			}
		}
		
		if (exists == null)
		{
			// eventually want to add session to edit as an argument
			PlanningPokerSessionTab editPanel = new PlanningPokerSessionTab(toEdit);
			
			StringBuilder tabName = new StringBuilder();
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
	 * this opens the voting page for the given session
	 * @param toVoteOn session that has been selected to vote in
	 */
	public void voteOnSession(PlanningPokerSession toVoteOn){
		VotingPage exists = null;
		
		// Check if the session is already open in a tab
		for(VotingPage panel : listOfVotingPanels)
		{
			if(panel.getDisplaySession() == toVoteOn)
			{
				exists = panel;
				break;
			}
		}
		
		if (exists == null)
		{
			VotingPage votingPanel = new VotingPage(toVoteOn);
			
			StringBuilder tabName = new StringBuilder();
			int subStringLength = toVoteOn.getName().length() > 6 ? 7 : toVoteOn.getName().length();
			tabName.append(toVoteOn.getName().substring(0,subStringLength));
			if(toVoteOn.getName().length() > 6) tabName.append("..");
			
			main.addTab(tabName.toString(), null, votingPanel, toVoteOn.getName());
			this.listOfVotingPanels.add(votingPanel);
			main.invalidate();
			main.repaint();
			main.setSelectedComponent(votingPanel);
		}
		else
		{
			main.setSelectedComponent(exists);
		}
	}

}
