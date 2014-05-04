/*******************************************************************************
 * Copyright (c) 2014 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Team8s
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.voting;

import java.awt.Color;
import java.awt.Dimension;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import edu.wpi.cs.wpisuitetng.janeway.config.ConfigManager;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.Estimate;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSessionModel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.ISessionTab;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.RequirementModel;

/** 
 *  This class is the panel that is shown when the user goes to vote  
 */
public class VotingPage extends JSplitPane implements ISessionTab {
	private final JPanel voteOnReqPanel = new JPanel();

	private VotingManager reqsView;

	private final SpringLayout layout = new SpringLayout();
	private JPanel reqDetailPanel;
	private DeckVotingPanel votingPanel;

	private PlanningPokerSession activeSession;

	private Requirement requirement;
	private final List<Estimate> estimates = new LinkedList<Estimate>();
	private JPanel reqDetails;

	private VoterTable thetable;

	private JScrollPane thetablePanel;

	public VotingPage(PlanningPokerSession votingSession){
		activeSession = votingSession;

		buildReqPanel(null);

		reqsView = new VotingManager(getSessionReqs(), activeSession, ConfigManager.getConfig().getUserName());
		reqsView.addSelectionListener(new SelectionListener() {
			@Override
			public void selectionMade(SelectionEvent e){
				requirement = e.getRequirement();
				buildReqPanel(requirement);
				final JScrollPane tablePanel = new JScrollPane();
				tablePanel.setViewportView(reqsView);

				tablePanel.setMinimumSize(new Dimension(200, 300));
				voteOnReqPanel.setMinimumSize(new Dimension(300, 300));

				setLeftComponent(tablePanel);
				setRightComponent(voteOnReqPanel);

				setDividerLocation(225);
			}

		});
		final JScrollPane tablePanel = new JScrollPane();
		tablePanel.setViewportView(reqsView);

		tablePanel.setMinimumSize(new Dimension(200, 300));
		voteOnReqPanel.setMinimumSize(new Dimension(300, 300));

		setLeftComponent(tablePanel);
		setRightComponent(voteOnReqPanel);
		setDividerLocation(225);		
	}

	/**
	 * Get the session that is being voted on 
	 * @return the session being voted on in this panel
	 */
	public PlanningPokerSession getDisplaySession(){
		return activeSession;
	}

	public JPanel makeReqDetailPanel(Requirement reqToVoteOn) {

		reqDetails = new JPanel();
		final SpringLayout sl_reqDetails = new SpringLayout();
		reqDetails.setLayout(sl_reqDetails);
		final JLabel nameLabel = new JLabel("Requirement Name:");
		final JLabel descriptionLabel = new JLabel("Requirement Description:");
		final JLabel requirementEstimated = new JLabel("Estimation of this requirement is complete");
		
		final String[] columnNames = {"Users That Have Voted"};
		final Object[][] data = {};
		thetable = new VoterTable(data, columnNames, activeSession, reqToVoteOn);
		thetablePanel = new JScrollPane(thetable);
		thetable.getColumnModel().getColumn(0).setMinWidth(100); // Username
		final Dimension d = new Dimension(150, 80);
        thetablePanel.setMinimumSize(d);
        
        thetable.populateVotePanel();
        
		final JTextField nameField = new JTextField();
		nameField.setBackground(Color.WHITE);
		nameField.setEditable(false);

		final JScrollPane descrScroll = new JScrollPane();
		final JTextArea descriptionField = new JTextArea("");
		descriptionField.setBackground(Color.WHITE);
		descrScroll.setPreferredSize(new Dimension(300, 300));
		descriptionField.setEditable(false);
		descrScroll.setViewportView(descriptionField);
		descriptionField.setLineWrap(true);


		boolean estimationComplete;

		if (reqToVoteOn != null) {		
			nameField.setText(reqToVoteOn.getName());
			descriptionField.setText(reqToVoteOn.getDescription());
			estimationComplete = activeSession.getReqsWithCompleteEstimates().contains(reqToVoteOn.getId());
		}
		else{
			estimationComplete = false;	
		}

		final boolean userIsModerator = ConfigManager.getConfig().getUserName().equals(activeSession.getSessionCreatorName());

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

		sl_reqDetails.putConstraint(SpringLayout.NORTH, descrScroll, 6, SpringLayout.SOUTH, descriptionLabel);
		sl_reqDetails.putConstraint(SpringLayout.WEST, descrScroll, 0, SpringLayout.WEST, nameLabel);
		sl_reqDetails.putConstraint(SpringLayout.EAST, descrScroll, 0, SpringLayout.EAST, nameField);
		sl_reqDetails.putConstraint(SpringLayout.SOUTH, descrScroll, -10, SpringLayout.SOUTH, reqDetails);

		
		sl_reqDetails.putConstraint(SpringLayout.WEST, thetablePanel, 10, SpringLayout.HORIZONTAL_CENTER, reqDetails);
		sl_reqDetails.putConstraint(SpringLayout.EAST, thetablePanel, -10, SpringLayout.EAST, reqDetails);
		sl_reqDetails.putConstraint(SpringLayout.NORTH, thetablePanel, 10, SpringLayout.NORTH, reqDetails);
		sl_reqDetails.putConstraint(SpringLayout.SOUTH, thetablePanel, -10, SpringLayout.SOUTH, reqDetails);
		
		
		System.out.println("currently displayed description:" + descriptionField.getText());

		reqDetails.add(requirementEstimated);
		reqDetails.add(nameLabel);
		reqDetails.add(descriptionLabel);
		reqDetails.add(nameField);
		reqDetails.add(thetablePanel);
		reqDetails.add(descrScroll);

		return reqDetails; 
	}



	/**
	 * checks a requirement in the session to vote on to see if it has been voted on
	 * by the current user
	 * @param checkReq requirement to check for an estimate for
	 * @return true if the current user estimated the requirement, false otherwise
	 */
	public boolean isEstimated(Requirement checkReq){
		final List<Estimate> sessionEstimates  = activeSession.getEstimates();

		final String currentUser = ConfigManager.getConfig().getUserName();
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
		final Set<Integer> sessionReqIds = activeSession.getRequirementIDs();
		final List<Requirement> sessionReqs = new LinkedList<Requirement>();
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
		if (!activeSession.getVoterNameList().contains(username)) {
			activeSession.getVoterNameList().add(username);
		}
		PlanningPokerSessionModel.getInstance().updatePlanningPokerSession(activeSession);
	}
	
	public void addVoterNameToEstimatesList(Estimate ev) {
		for (Estimate e :activeSession.getEstimates()) {
			if( e.getRequirementID() == ev.getRequirementID() && e.getOwnerName().equals(ev.getOwnerName())) {
				e.setVote(ev.getVote());
				return ;
			}
		}
		activeSession.getEstimates().add(ev);
		PlanningPokerSessionModel.getInstance().updatePlanningPokerSession(activeSession);
	}
	

	/**
	 * returns the list of users in a project
	 */
	public String getVoterName() {
		final String name = ConfigManager.getInstance().getConfig().getUserName();
		name.toLowerCase();
		return name;
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
				if (requirement != null) {
					Estimate estimate = new Estimate();
					
					for (Estimate e2: estimates) {
						if (e2.getRequirementID() == requirement.getId()) {
							estimate = e2;
							break;
						}
					}
					estimate.setOwnerName(ConfigManager.getConfig().getUserName());
					estimate.setRequirementID(requirement.getId());
					estimate.setSessionID(activeSession.getID());
					estimate.setVote((int)e.getEstimate());
					addVoterNameToEstimatesList(estimate);

					estimates.add(estimate);
					activeSession = PlanningPokerSessionModel.getInstance().addEstimateToPlanningPokerSession(estimate);
					reqsView = new VotingManager(getSessionReqs(), activeSession , ConfigManager.getConfig().getUserName());
					reqsView.addSelectionListener(new SelectionListener() {
						@Override
						public void selectionMade(SelectionEvent e){
							requirement = e.getRequirement();
							buildReqPanel(requirement);
							final JScrollPane tablePanel = new JScrollPane();
							tablePanel.setViewportView(reqsView);

							tablePanel.setMinimumSize(new Dimension(200, 300));
							voteOnReqPanel.setMinimumSize(new Dimension(300, 300));

							setLeftComponent(tablePanel);
							setRightComponent(voteOnReqPanel);

							setDividerLocation(225);
						}

					});
					final JScrollPane tablePanel = new JScrollPane();
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
