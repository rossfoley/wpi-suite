
package edu.wpi.cs.wpisuitetng.modules.planningpoker.view;

import java.awt.Component;
import java.util.ArrayList;

import javax.swing.JComponent;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.gui.PlanningPokerSessionTab;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.gui.voting.VotingPage;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.buttons.PlanningPokerSessionButtonsPanel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.overview.OverviewDetailInfoPanel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.overview.OverviewDetailPanel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.overview.OverviewPanel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.overview.OverviewReqTable;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.overview.OverviewTreePanel;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.updateestimates.SelectEstimatesToSendToReqManagerPane;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.iterations.Iteration;
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
	private OverviewTreePanel overviewTreePanel = null;
	private OverviewDetailPanel overviewDetailPanel = null;
	private ArrayList<PlanningPokerSessionTab> listOfEditingPanels = new ArrayList<PlanningPokerSessionTab>();
	private OverviewDetailInfoPanel overviewDetailInfoPanel;
	private ArrayList<VotingPage> listOfVotingPanels = new ArrayList<VotingPage>();
	private OverviewReqTable overviewReqTable;
	private PlanningPokerSessionButtonsPanel planningPokerSessionButtonsPanel;
	private ArrayList<SelectEstimatesToSendToReqManagerPane> listOfUpdateRequirementPages = new ArrayList<SelectEstimatesToSendToReqManagerPane>();

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
	
	public void setOverviewTree(OverviewTreePanel overviewTreePanel) {
		this.overviewTreePanel = overviewTreePanel;
	}
	
	public void setOverviewDetailPanel(OverviewDetailPanel overviewDetailPanel) {
		this.overviewDetailPanel = overviewDetailPanel;
	}
	
	public void setOverviewDetailInfoPanel(OverviewDetailInfoPanel infoPanel) {
		this.overviewDetailInfoPanel = infoPanel;
	}
	
	public void setOverviewReqTable(OverviewReqTable overviewReqTable) {
		this.overviewReqTable = overviewReqTable;
	}	
	
	public void setPlanningPokerSessionButtonsPanel(PlanningPokerSessionButtonsPanel buttonsPanel) {
		this.planningPokerSessionButtonsPanel = buttonsPanel;
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
	 * @return OverviewTreePanel
	 */
	public OverviewTreePanel getOverviewTreePanel() {
		return this.overviewTreePanel;
	}
	
	/**
	 * @return OverviewDetailPanel
	 */
	public OverviewDetailPanel getOverviewDetailPanel() {
		return this.overviewDetailPanel;
	}
	/**
	 * 
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
			planningPokerSessionButtonsPanel.enableVoteButton();
			planningPokerSessionButtonsPanel.enableEndVoteButton();
		}		
		
		main.remove(comp);
	}

	/**Tells the table to update its listings based on the data in the requirement model
	 * 
	 */
	public void refreshTable() {
		this.overviewTreePanel.refresh();
	}
	
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

		for (int i = tabCount - 1; i >= 0; i--) {
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
		overviewDetailPanel.updatePanel(displaySession);
	}

	/**
	 * Opens a new tab for the editing of a session
	 * @param toEdit the session to edit
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
	
	public void sendEstimates(PlanningPokerSession sendEstimatesFrom){
		SelectEstimatesToSendToReqManagerPane exists = null;
		
		for(SelectEstimatesToSendToReqManagerPane page : listOfUpdateRequirementPages){
			if(page.getDisplaySession() == sendEstimatesFrom){
				exists = page;
				break;
			}
		}
		if (exists == null)	{
			SelectEstimatesToSendToReqManagerPane sendPane = new SelectEstimatesToSendToReqManagerPane(sendEstimatesFrom);
			
			StringBuilder tabName = new StringBuilder();
			int subStringLength = sendEstimatesFrom.getName().length() > 6 ? 7 : sendEstimatesFrom.getName().length();
			tabName.append(sendEstimatesFrom.getName().substring(0,subStringLength));
			if(sendEstimatesFrom.getName().length() > 6) tabName.append("..");
			
			main.addTab(tabName.toString(), null, sendPane, sendEstimatesFrom.getName());
			this.listOfUpdateRequirementPages.add(sendPane);
			main.invalidate();
			main.repaint();
			main.setSelectedComponent(sendPane);
		}
		else
		{
			main.setSelectedComponent(exists);
		}
		
		
	}
}
