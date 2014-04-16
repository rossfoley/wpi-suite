

package edu.wpi.cs.wpisuitetng.modules.planningpoker.gui.voting;

import java.text.DateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import edu.wpi.cs.wpisuitetng.janeway.config.ConfigManager;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.Estimate;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.voting.DeckVotingPanel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.voting.EstimateEvent;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.voting.EstimateListener;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.voting.SelectionEvent;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.voting.SelectionListener;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.voting.VotingManager;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.RequirementModel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/** 
 *  This class is the panel that is shown when the user goes to vote
 *  @author amandaadkins
 */

public class VotingPage extends JSplitPane {
	private JTable reqsTable = new JTable();
	private JPanel voteOnReqPanel = new JPanel();
	
	private VotingManager reqsView;

	private SpringLayout layout = new SpringLayout();
	private JPanel reqDetailPanel;
	private DeckVotingPanel votingPanel;

	private PlanningPokerSession activeSession;
	private List<Requirement> reqsToVoteOn;

	private DefaultTableModel tableModel;
	
	private transient Vector<SelectionListener> selectionListeners;
	private Requirement requirement;
	private LinkedList<Estimate> estimates = new LinkedList<Estimate>();
	private JPanel reqDetails;
	//private JTextArea descriptionField = new JTextArea();
	private JTextField descriptionField = new JTextField();

	public VotingPage(PlanningPokerSession votingSession){
		this.activeSession = votingSession;		
		reqsToVoteOn = getSessionReqs();
		
		
		buildReqPanel(null);

		
		reqsView = new VotingManager(getSessionReqs(), estimates, ConfigManager.getConfig().getUserName());
		reqsView.addSelectionListener(new SelectionListener() {
			@Override
			public void selectionMade(SelectionEvent e){
				System.out.println("Got Selection Event:" + e.getRequirement().getName());
				requirement = e.getRequirement();
				buildReqPanel(requirement);
				setRightComponent(voteOnReqPanel);
			}
			
		});
		JScrollPane tablePanel = new JScrollPane();
		tablePanel.setViewportView(reqsView);
		
		tablePanel.setMinimumSize(new Dimension(200, 300));
		voteOnReqPanel.setMinimumSize(new Dimension(300, 300));

		this.setLeftComponent(tablePanel);
		this.setRightComponent(voteOnReqPanel);
		
		this.setDividerLocation(225);		
	}

	/**
	 * get the session that is being voted on 
	 * @return the session being voted on in this panel
	 */
	public PlanningPokerSession getDisplaySession(){
		return activeSession;
	}

	public JPanel makeReqDetailPanel(Requirement reqToVoteOn){
		reqDetails = new JPanel();
		SpringLayout sl_reqDetails = new SpringLayout();
		reqDetails.setLayout(sl_reqDetails);

		JLabel nameLabel = new JLabel("Requirement Name:");
		JLabel descriptionLabel = new JLabel("Requirement Description:");
		JLabel requirementEstimated = new JLabel("Estimation of this requirement is complete");

		JTextField nameField = new JTextField();
		nameField.setBackground(Color.WHITE);
		//JTextArea descriptionField = new JTextArea();
		descriptionField.setBackground(Color.WHITE);
		descriptionField.setPreferredSize(new Dimension(300, 300));

		nameField.setEditable(false);
		descriptionField.setEditable(false);
		descriptionField.setColumns(10);

		boolean estimationComplete;
		
		if (reqToVoteOn!=null){		
			nameField.setText(reqToVoteOn.getName());
			String description = reqToVoteOn.getDescription();
			System.out.println(description);
			descriptionField.setText(description);
			estimationComplete = activeSession.getReqsWithCompleteEstimates().contains(reqToVoteOn.getId());
		}
		else{
			estimationComplete = false;	
		}
				
		boolean userIsModerator = ConfigManager.getConfig().getUserName().equals(activeSession.getSessionCreatorName());
		
		if (estimationComplete && userIsModerator){
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
		sl_reqDetails.putConstraint(SpringLayout.EAST, nameField, -10, SpringLayout.EAST, reqDetails);

		sl_reqDetails.putConstraint(SpringLayout.NORTH, descriptionLabel, 6, SpringLayout.SOUTH, nameField);
		sl_reqDetails.putConstraint(SpringLayout.WEST, descriptionLabel, 0, SpringLayout.WEST, nameLabel);

		sl_reqDetails.putConstraint(SpringLayout.NORTH, descriptionField, 6, SpringLayout.SOUTH, descriptionLabel);
		sl_reqDetails.putConstraint(SpringLayout.WEST, descriptionField, 0, SpringLayout.WEST, nameLabel);
		sl_reqDetails.putConstraint(SpringLayout.EAST, descriptionField, 0, SpringLayout.EAST, nameField);
		sl_reqDetails.putConstraint(SpringLayout.SOUTH, descriptionField, -10, SpringLayout.SOUTH, reqDetails);
		
		System.out.print("currently displayed description:");
		System.out.println(descriptionField.getText());
		
		reqDetails.add(requirementEstimated);
		reqDetails.add(nameLabel);
		reqDetails.add(descriptionLabel);
		reqDetails.add(nameField);
		reqDetails.add(descriptionField);

		return reqDetails; 
	}


	private void populateReqsTable(){
		// 2 columns
		// 1 for voted on
		// 1 for req name



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
	 * build the part of the panel that is specific to the selected requirement
	 * displays the requirement name, description, and allows the user to vote on it
	 * @param reqToVoteOn requirement to have details about in the panel
	 */
	public void buildReqPanel(Requirement reqToVoteOn){
		reqDetailPanel = makeReqDetailPanel(reqToVoteOn);

		votingPanel = new DeckVotingPanel(activeSession.getSessionDeck());
		votingPanel.addEstimateListener(new EstimateListener() {
			@Override	
			public void estimateSubmitted(EstimateEvent e) {
				System.out.println("Estimate submitted: " + e.getEstimate());
					if (requirement != null){
					Estimate estimate = new Estimate();
					estimate.setOwnerName(ConfigManager.getConfig().getUserName());
					estimate.setRequirementID(requirement.getId());
					estimate.setProject(activeSession.getProject());
					estimate.setVote((int)e.getEstimate());
					estimates.add(estimate);
					reqsView = new VotingManager(getSessionReqs(), estimates, ConfigManager.getConfig().getUserName());
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

		voteOnReqPanel.add(reqDetailPanel);
		voteOnReqPanel.add(votingPanel);
	}

	
	synchronized public void addSelectionListener(SelectionListener l) {
		if (this.selectionListeners == null) {
			this.selectionListeners = new Vector<SelectionListener>();
		}
		this.selectionListeners.addElement(l);
	}  

	/** Remove a listener for EstimateEvents */
	synchronized public void removeSelectionListener(SelectionListener l) {
		if (this.selectionListeners == null) {
			this.selectionListeners = new Vector<SelectionListener>();
		}
		else {
			this.selectionListeners.removeElement(l);
		}
	}
	
}
