

package edu.wpi.cs.wpisuitetng.modules.planningpoker.gui.voting;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.*;

import edu.wpi.cs.wpisuitetng.janeway.config.ConfigManager;
import edu.wpi.cs.wpisuitetng.modules.EntityManager;
import edu.wpi.cs.wpisuitetng.modules.Model;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.UserModel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.controller.GetUserController;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.controller.UpdatePlanningPokerSessionController;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.Estimate;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.EstimateVoters;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSessionModel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.ViewEventController;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.UserModel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.overview.OverviewReqTable;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.overview.OverviewVoterTable;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.voting.DeckVotingPanel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.voting.EstimateEvent;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.voting.EstimateListener;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.voting.SelectionEvent;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.voting.SelectionListener;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.voting.VotingManager;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.RequirementModel;
import edu.wpi.cs.wpisuitetng.database.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

/** 
 *  This class is the panel that is shown when the user goes to vote
 *  @author amandaadkins
 */

public class VotingPage extends JSplitPane {
	private JPanel voteOnReqPanel = new JPanel();

	private VotingManager reqsView;

	private SpringLayout layout = new SpringLayout();
	private JPanel reqDetailPanel;
	private DeckVotingPanel votingPanel;

	private PlanningPokerSession activeSession;

	private Requirement requirement;
	private LinkedList<Estimate> estimates = new LinkedList<Estimate>();
	private JPanel reqDetails;

	private OverviewVoterTable thetable;

	private JScrollPane thetablePanel;

	public VotingPage(PlanningPokerSession votingSession){
		this.activeSession = votingSession;

		// Disable the vote button in the planning poker module toolbar
		ViewEventController.getInstance().getPlanningPokerSessionButtonsPanel().disableVoteButton();
		ViewEventController.getInstance().getPlanningPokerSessionButtonsPanel().disableEndVoteButton();


		buildReqPanel(null);


		reqsView = new VotingManager(getSessionReqs(), activeSession, ConfigManager.getConfig().getUserName());
		reqsView.addSelectionListener(new SelectionListener() {
			@Override
			public void selectionMade(SelectionEvent e){
				requirement = e.getRequirement();
				buildReqPanel(requirement);
				JScrollPane tablePanel = new JScrollPane();
				tablePanel.setViewportView(reqsView);

				tablePanel.setMinimumSize(new Dimension(200, 300));
				voteOnReqPanel.setMinimumSize(new Dimension(300, 300));

				setLeftComponent(tablePanel);
				setRightComponent(voteOnReqPanel);

				setDividerLocation(225);
			}

		});
		JScrollPane tablePanel = new JScrollPane();
		tablePanel.setViewportView(reqsView);

		tablePanel.setMinimumSize(new Dimension(200, 300));
		voteOnReqPanel.setMinimumSize(new Dimension(300, 300));

		setLeftComponent(tablePanel);
		setRightComponent(voteOnReqPanel);
		setDividerLocation(225);		
	}

	/**
	 * get the session that is being voted on 
	 * @return the session being voted on in this panel
	 */
	public PlanningPokerSession getDisplaySession(){
		return this.activeSession;
	}

	public JPanel makeReqDetailPanel(Requirement reqToVoteOn) {

		reqDetails = new JPanel();
		SpringLayout sl_reqDetails = new SpringLayout();
		reqDetails.setLayout(sl_reqDetails);
		JLabel nameLabel = new JLabel("Requirement Name:");
		JLabel descriptionLabel = new JLabel("Requirement Description:");
		JLabel requirementEstimated = new JLabel("Estimation of this requirement is complete");
		
		
		//
		String[] columnNames = {"Requirement ID","Requirement Name", "Username", "Votes"};
		Object[][] data = {};
		thetable = new OverviewVoterTable(data, columnNames, activeSession);
		thetablePanel = new JScrollPane(thetable);
		thetable.getColumnModel().getColumn(0).setMinWidth(5); // Requirement ID
		thetable.getColumnModel().getColumn(1).setMinWidth(100); // Requirement Name	
		thetable.getColumnModel().getColumn(2).setMinWidth(55); // Username
		thetable.getColumnModel().getColumn(3).setMinWidth(15); // Votes
		Dimension d = new Dimension(150, 80);
        thetablePanel.setMinimumSize(d);
        /////
        /////
        
        thetable.populateVotePanel();
        
		//
		JTextField nameField = new JTextField();
		nameField.setBackground(Color.WHITE);
		nameField.setEditable(false);

		JTextField descriptionField = new JTextField("");
		descriptionField.setBackground(Color.WHITE);
		descriptionField.setPreferredSize(new Dimension(300, 300));
		descriptionField.setEditable(false);
		descriptionField.setColumns(10);
		

		boolean estimationComplete;

		if (reqToVoteOn != null) {		
			nameField.setText(reqToVoteOn.getName());
			descriptionField.setText(reqToVoteOn.getDescription());
			estimationComplete = activeSession.getReqsWithCompleteEstimates().contains(reqToVoteOn.getId());
		}
		else{
			estimationComplete = false;	
		}

		boolean userIsModerator = ConfigManager.getConfig().getUserName().equals(activeSession.getSessionCreatorName());

		if (estimationComplete && userIsModerator) {
			requirementEstimated.setVisible(true);

			sl_reqDetails.putConstraint(SpringLayout.NORTH, requirementEstimated, 10, SpringLayout.NORTH, reqDetails);
			sl_reqDetails.putConstraint(SpringLayout.HORIZONTAL_CENTER, requirementEstimated, 0, SpringLayout.HORIZONTAL_CENTER, reqDetails);

			sl_reqDetails.putConstraint(SpringLayout.NORTH, nameLabel, 10, SpringLayout.NORTH, requirementEstimated);
			sl_reqDetails.putConstraint(SpringLayout.WEST, nameLabel, 10, SpringLayout.WEST, reqDetails);	
		}
		else{		
			requirementEstimated.setVisible(false);

			sl_reqDetails.putConstraint(SpringLayout.NORTH, nameLabel, 10, SpringLayout.NORTH, reqDetails);
			sl_reqDetails.putConstraint(SpringLayout.WEST, nameLabel, 10, SpringLayout.WEST, reqDetails);	
		}

		sl_reqDetails.putConstraint(SpringLayout.NORTH, nameField, 6, SpringLayout.SOUTH, nameLabel);
		sl_reqDetails.putConstraint(SpringLayout.WEST, nameField, 0, SpringLayout.WEST, nameLabel);
		sl_reqDetails.putConstraint(SpringLayout.EAST, nameField, -10, SpringLayout.HORIZONTAL_CENTER, reqDetails);

		sl_reqDetails.putConstraint(SpringLayout.NORTH, descriptionLabel, 6, SpringLayout.SOUTH, nameField);
		sl_reqDetails.putConstraint(SpringLayout.WEST, descriptionLabel, 0, SpringLayout.WEST, nameLabel);

		sl_reqDetails.putConstraint(SpringLayout.NORTH, descriptionField, 6, SpringLayout.SOUTH, descriptionLabel);
		sl_reqDetails.putConstraint(SpringLayout.WEST, descriptionField, 0, SpringLayout.WEST, nameLabel);
		sl_reqDetails.putConstraint(SpringLayout.EAST, descriptionField, 0, SpringLayout.EAST, nameField);
		sl_reqDetails.putConstraint(SpringLayout.SOUTH, descriptionField, -10, SpringLayout.SOUTH, reqDetails);

		
		sl_reqDetails.putConstraint(SpringLayout.WEST, thetablePanel, 10, SpringLayout.HORIZONTAL_CENTER, reqDetails);
		sl_reqDetails.putConstraint(SpringLayout.EAST, thetablePanel, -10, SpringLayout.EAST, reqDetails);
		
		
		System.out.println("currently displayed description:" + descriptionField.getText());
		System.out.println(descriptionField.getText());

		reqDetails.add(requirementEstimated);
		reqDetails.add(nameLabel);
		reqDetails.add(descriptionLabel);
		reqDetails.add(nameField);
		reqDetails.add(descriptionField);
		reqDetails.add(thetablePanel);

		return reqDetails; 
	}



	/**
	 * checks a requirement in the session to vote on to see if it has been voted on
	 * by the current user
	 * @param checkReq requirement to check for an estimate for
	 * @return true if the current user estimated the requirement, false otherwise
	 */
	public boolean isEstimated(Requirement checkReq){
		List<Estimate> sessionEstimates  = activeSession.getEstimates();

		String currentUser = ConfigManager.getConfig().getUserName();
		// match current user and given requirement
		for (Estimate e:sessionEstimates){
			if ((checkReq.getId()==e.getRequirementID())&&(e.getOwnerName().equals(currentUser))){
				return true;
			}
		}
		return false;
	}

	/**
	 * gets the requirements that have been selected for the given session
	 * @return a list of requirements that have been selected for the given session
	 */
	public List<Requirement> getSessionReqs(){
		Set<Integer> sessionReqIds = activeSession.getRequirementIDs();
		List<Requirement> sessionReqs = new LinkedList<Requirement>();
		for (Integer id : sessionReqIds) {
			Requirement current = RequirementModel.getInstance().getRequirement(id);
			sessionReqs.add(current);			
		}
		return sessionReqs;
	}
	
	/**
	 * adds the user id to the voted people's list
	 * @param id
	 */
	public void addVoterNameToList(String username) {
		System.out.println(activeSession.getVoterNameList().size() + " is the size");
		if (activeSession.getVoterNameList().contains(username)) {
			System.out.println("Username exist");
		} else {
			activeSession.getVoterNameList().add(username);
			System.out.println(username + " has voted and is added to the list");
		}
		PlanningPokerSessionModel.getInstance().updatePlanningPokerSession(activeSession);
		//UpdatePlanningPokerSessionController.getInstance().updatePlanningPokerSession(activeSession);
	}

	public void addVoterNameToEstimateVotersList(EstimateVoters ev) {
		System.out.println("addVoterNameToEstimateVotersList");
		for (EstimateVoters e :activeSession.getEstimateVoterList()) {
			if( e.getRequirementID() == ev.getRequirementID() && e.getVoterUsername().equals(ev.getVoterUsername())) {
				e.setOwnerName(ev.getOwnerName());
				e.setID(ev.getID());
				e.setRequirementID(ev.getRequirementID());
				e.setVoterUsername(ev.getVoterUsername());
				e.setVote(ev.getVote());
				System.out.println("Username exist replacing with a new vote" + ev.getVote() + " " + e.getVote());
				return ;
			}
		}
		activeSession.getEstimateVoterList().add(ev);
		System.out.println(ev.getVoterUsername() + " has voted and is added to the list");
		PlanningPokerSessionModel.getInstance().updatePlanningPokerSession(activeSession);
		//UpdatePlanningPokerSessionController.getInstance().updatePlanningPokerSession(activeSession);
	}
	public void addVoterNameToEstimatesList(Estimate ev) {
		System.out.println("newTest");
		for (Estimate e :activeSession.getEstimates()) {
			if( e.getRequirementID() == ev.getRequirementID() && e.getOwnerName().equals(ev.getOwnerName())) {
				e.setVote(ev.getVote());
				System.out.println("Username exist replacing with a new vote" + ev.getVote() + " " + e.getVote());
				return ;
			}
		}
		activeSession.getEstimates().add(ev);
		System.out.println(ev.getOwnerName() + " has voted and is added to the list");
		PlanningPokerSessionModel.getInstance().updatePlanningPokerSession(activeSession);
		//UpdatePlanningPokerSessionController.getInstance().updatePlanningPokerSession(activeSession);
	}
	/**
	 * 
	 * @param username
	 */
	public void addVoterNameToEstimateVotersList(String username, int requirementID, EstimateVoters NewEstimateVoter) {
		System.out.println(activeSession.getEstimateVoterList().size() + " is the size");
		for (int i = 0; i < activeSession.getEstimateVoterList().size(); i++) {
			System.out.println(activeSession.getEstimateVoterList().get(i).getRequirementID());
			if(activeSession.getEstimateVoterList().get(i).getRequirementID() == requirementID) {
				if(activeSession.getEstimateVoterList().get(i).getVoterNameList().contains(username)) {
					System.out.println("Username exist");
					PlanningPokerSessionModel.getInstance().updatePlanningPokerSession(activeSession);
					UpdatePlanningPokerSessionController.getInstance().updatePlanningPokerSession(activeSession);
					return;
				} else {
					activeSession.getEstimateVoterList().get(i).getVoterNameList().add(username);
					System.out.println(username + " has voted ON REQ " + requirementID + " and is added to the list");
					PlanningPokerSessionModel.getInstance().updatePlanningPokerSession(activeSession);
					UpdatePlanningPokerSessionController.getInstance().updatePlanningPokerSession(activeSession);
					return;
				}
			}
		}
		NewEstimateVoter.getVoterNameList().add(username);
		activeSession.getEstimateVoterList().add(NewEstimateVoter);
		System.out.println("New estimation is created and added to the list " + username);
		PlanningPokerSessionModel.getInstance().updatePlanningPokerSession(activeSession);
		UpdatePlanningPokerSessionController.getInstance().updatePlanningPokerSession(activeSession);
	}
	/**
	 * 
	 */
	public void displayAllEstimateVotersList() {
		List<String> allUserList = getAllVoterNamesList();
		List<Requirement> ListOfRequirements =  getSessionReqs();
		for (Requirement r : ListOfRequirements) {
			System.out.println(r.getId());
		}
		for (Requirement r : ListOfRequirements) {
			for (int i = 0; i < activeSession.getEstimateVoterList().size(); i++) {
				if(activeSession.getEstimateVoterList().get(i).getRequirementID() == r.getId()) {
					System.out.println(activeSession.getEstimateVoterList().get(i).getRequirementID() + " requirement ID");
					for (String s : allUserList) {
						if(activeSession.getEstimateVoterList().get(i).getVoterNameList().contains(s)) {
							System.out.println(s + " has voted on req " + r.getId());
						} else {
							System.out.println(s + " has NOT voted on req " + r.getId());
						}
					}
				}
			}
		}
	}
	public void test () {
		for (EstimateVoters e : activeSession.getEstimateVoterList()) {
			for (String S : e.getVoterNameList()) {
				System.out.println(S + " in test");
			}
		}
	}
	/**
	 * returns the list of users in a project
	 */
	public String getVoterName() {
		String name = ConfigManager.getInstance().getConfig().getUserName();
		System.out.println(name + " is the current voter");
		name.toLowerCase();
		return name;
	}
	public List<String> getAllVoterNamesList() {
		List<String> allVoters = new ArrayList<String>();
		GetUserController.getInstance().retrieveUsers();
		List<User> user = UserModel.getInstance().getUsers();
		System.out.println(user.size() + "IS THE SIZE user");
		for(User u : user) {
			try {
				allVoters.add(u.getUsername());
				System.out.println(u.getUsername() + " is added to the list");
			} catch (Exception E) {
				System.out.println("User is null");
			}
		}
		return allVoters;
	}
	
	public void getAllVoterNames() {
		GetUserController.getInstance().retrieveUsers();
		List<User> user = UserModel.getInstance().getUsers();
		System.out.println(user.size() + "IS THE SIZE user");
		for(User u : user) {
			try {
				System.out.println(u.getUsername());
			} catch (Exception E) {
				System.out.println("User is null");
			}
		}
		
	}
	public void displayVoters() {
		System.out.println("Displaying voters");
		List<String> allVoters = getAllVoterNamesList();
		String currentVoter = getVoterName();
		addVoterNameToList(currentVoter);
		for (String s : allVoters) {
			if (activeSession.getVoterNameList().contains(s)) {
				System.out.println(s + " has voted ");
			} 
			else {
				System.out.println(s + " has not voted ");
			}
		}
	}

	/**
	 * build the part of the panel that is specific to the selected requirement
	 * displays the requirement name, description, and allows the user to vote on it
	 * @param reqToVoteOn requirement to have details about in the panel
	 */
	public void buildReqPanel(Requirement reqToVoteOn){
		// Remove panels before recreating to refresh them correctly
		try {
			voteOnReqPanel.remove(reqDetailPanel);
			voteOnReqPanel.remove(votingPanel);
		} catch (NullPointerException ex) {}
		
		reqDetailPanel = makeReqDetailPanel(reqToVoteOn);

		Estimate estimateFillIn = new Estimate();
		if (activeSession.getEstimates().size() > 0 && reqToVoteOn != null) {
			for (Estimate e: activeSession.getEstimates()) {
				if (e.getRequirementID() == reqToVoteOn.getId() && e.getOwnerName().equals(ConfigManager.getInstance().getConfig().getUserName())) {
					estimateFillIn = e;
				}
			}
		}

		votingPanel = new DeckVotingPanel(activeSession.getSessionDeck(), estimateFillIn);
		votingPanel.addEstimateListener(new EstimateListener() {
			@Override
			public void estimateSubmitted(EstimateEvent e) {
				System.out.println("Estimate submitted: " + e.getEstimate());
				if (requirement != null) {
					Estimate estimate = new Estimate();
					// tracking line
					EstimateVoters estimateVoter = new EstimateVoters();
					for (Estimate e2: estimates) {
						if (e2.getRequirementID() == requirement.getId()) {
							estimate = e2;
						}
					}
					estimate.setOwnerName(ConfigManager.getConfig().getUserName());
					estimate.setRequirementID(requirement.getId());
					estimate.setSessionID(activeSession.getID());
					estimate.setVote((int)e.getEstimate());
					
					estimateVoter.setOwnerName(ConfigManager.getConfig().getUserName());
					estimateVoter.setRequirementID(requirement.getId());
					estimateVoter.setSessionID(activeSession.getID());
					estimateVoter.setVote((int)e.getEstimate());
					estimateVoter.setVoterUsername(getVoterName());
					//
					addVoterNameToEstimateVotersList(getVoterName() ,requirement.getId(),estimateVoter);
					addVoterNameToEstimatesList(estimate);

					
					
					estimates.add(estimate);
					activeSession = PlanningPokerSessionModel.getInstance().addEstimateToPlanningPokerSession(estimate);
					reqsView = new VotingManager(getSessionReqs(), activeSession , ConfigManager.getConfig().getUserName());
					reqsView.addSelectionListener(new SelectionListener() {
						@Override
						public void selectionMade(SelectionEvent e){
							requirement = e.getRequirement();
							buildReqPanel(requirement);
							JScrollPane tablePanel = new JScrollPane();
							tablePanel.setViewportView(reqsView);

							tablePanel.setMinimumSize(new Dimension(200, 300));
							voteOnReqPanel.setMinimumSize(new Dimension(300, 300));

							setLeftComponent(tablePanel);
							setRightComponent(voteOnReqPanel);

							setDividerLocation(225);
						}

					});
					JScrollPane tablePanel = new JScrollPane();
					tablePanel.setViewportView(reqsView);

					tablePanel.setMinimumSize(new Dimension(200, 300));
					voteOnReqPanel.setMinimumSize(new Dimension(300, 300));

					setLeftComponent(tablePanel);
					setRightComponent(voteOnReqPanel);

					setDividerLocation(225);
					thetable.populateVotePanel();
					PlanningPokerSessionModel.getInstance().updatePlanningPokerSession(activeSession);
				}
			}
		});

		voteOnReqPanel.setLayout(layout);

		layout.putConstraint(SpringLayout.NORTH, reqDetailPanel, 5, SpringLayout.NORTH, voteOnReqPanel);
		layout.putConstraint(SpringLayout.WEST, reqDetailPanel, 5, SpringLayout.WEST, voteOnReqPanel);
		layout.putConstraint(SpringLayout.EAST, reqDetailPanel, -5, SpringLayout.EAST, voteOnReqPanel);

		layout.putConstraint(SpringLayout.SOUTH, votingPanel, -5, SpringLayout.SOUTH, voteOnReqPanel);
		layout.putConstraint(SpringLayout.WEST, votingPanel, 0, SpringLayout.WEST, reqDetailPanel);
		layout.putConstraint(SpringLayout.EAST, votingPanel, 0, SpringLayout.EAST, reqDetailPanel);

		layout.putConstraint(SpringLayout.SOUTH, reqDetailPanel, -10, SpringLayout.NORTH, votingPanel);

		layout.putConstraint(SpringLayout.NORTH, votingPanel, -250, SpringLayout.SOUTH, voteOnReqPanel);

		if (reqToVoteOn == null) {
			votingPanel.setVisible(false);
		}
		else {
			votingPanel.setVisible(true);
		}
		voteOnReqPanel.add(reqDetailPanel);
		voteOnReqPanel.add(votingPanel);
	}

}
