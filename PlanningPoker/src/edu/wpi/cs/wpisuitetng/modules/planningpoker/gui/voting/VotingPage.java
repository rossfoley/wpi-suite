

package edu.wpi.cs.wpisuitetng.modules.planningpoker.gui.voting;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import edu.wpi.cs.wpisuitetng.janeway.config.ConfigManager;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.Estimate;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.RequirementModel;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/** 
 *  This class is the panel that is shown when the user goes to vote
 *  @author amandaadkins
 */

public class VotingPage extends JSplitPane {
	private JTable reqsTable;

	private JPanel voteOnReqPanel;

	private SpringLayout layout = new SpringLayout();
	private JPanel reqDetailPanel;
	private JPanel votingPanel;

	private PlanningPokerSession activeSession;
	private List<Requirement> reqsToVoteOn;
	
	private DefaultTableModel tableModel;


	public VotingPage(PlanningPokerSession votingSession){
		this.activeSession = votingSession;		
		reqsToVoteOn = getSessionReqs();

		buildReqPanel(null);

		buildReqTable();
		reqsTable.getColumnModel().getColumn(0).setMaxWidth(40); // voted on check
		reqsTable.getColumnModel().getColumn(1).setMinWidth(250); // Name of req

		JScrollPane tablePanel = new JScrollPane(reqsTable);

		this.setLeftComponent(tablePanel);
		this.setRightComponent(voteOnReqPanel);
		this.setDividerLocation(225);		
	}

	public void buildReqTable(){
		Object[][] data = {};
		String[] columnNames = {"Voted On?", "Requirement Name"};
		tableModel = new DefaultTableModel(data, columnNames);
		reqsTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer());
		reqsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		reqsTable.getTableHeader().setReorderingAllowed(false);
		reqsTable.setAutoCreateRowSorter(true);
		reqsTable.setFillsViewportHeight(true);
		
		reqsTable.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				
				if(reqsTable.getRowCount() > 0)
				{
					int mouseY = e.getY();
					Rectangle lastRow = reqsTable.getCellRect(reqsTable.getRowCount() - 1, 0, true);
					int lastRowY = lastRow.y + lastRow.height;

					if(mouseY > lastRowY) 
					{
						//reqsTable.getSelectionModel().clearSelection();
						repaint();
					}


					
					// rebuild req panel with selected req
				}
				
			}
		});
		

	}
	/**
	 * get the session that is being voted on 
	 * @return the session being voted on in this panel
	 */
	public PlanningPokerSession getDisplaySession(){
		return activeSession;
	}

	public JPanel makeReqDetailPanel(Requirement reqToVoteOn){
		JPanel reqDetails = new JPanel();
		SpringLayout reqDetailLayout = new SpringLayout();
		reqDetails.setLayout(reqDetailLayout);

		JLabel nameLabel = new JLabel("Requirement Name");
		JLabel descriptionLabel = new JLabel("Description Name");

		JTextField nameField = new JTextField();
		nameField.setBackground(Color.WHITE);
		JTextArea descriptionField = new JTextArea();

		nameField.setEditable(false);
		descriptionField.setEditable(false);

		if (reqToVoteOn!=null){		
			nameField.setText(reqToVoteOn.getName());
			descriptionField.setText(reqToVoteOn.getDescription());
		}
		reqDetailLayout.putConstraint(SpringLayout.NORTH, nameLabel, 10, SpringLayout.NORTH, reqDetails);
		reqDetailLayout.putConstraint(SpringLayout.WEST, nameLabel, 10, SpringLayout.WEST, reqDetails);

		reqDetailLayout.putConstraint(SpringLayout.NORTH, nameField, 6, SpringLayout.SOUTH, nameLabel);
		reqDetailLayout.putConstraint(SpringLayout.WEST, nameField, 0, SpringLayout.WEST, nameLabel);
		reqDetailLayout.putConstraint(SpringLayout.EAST, nameField, -10, SpringLayout.EAST, reqDetails);

		reqDetailLayout.putConstraint(SpringLayout.NORTH, descriptionLabel, 6, SpringLayout.SOUTH, nameField);
		reqDetailLayout.putConstraint(SpringLayout.WEST, descriptionLabel, 0, SpringLayout.WEST, nameLabel);

		reqDetailLayout.putConstraint(SpringLayout.NORTH, descriptionField, 6, SpringLayout.SOUTH, descriptionLabel);
		reqDetailLayout.putConstraint(SpringLayout.WEST, descriptionField, 0, SpringLayout.WEST, nameLabel);
		reqDetailLayout.putConstraint(SpringLayout.EAST, descriptionField, 0, SpringLayout.EAST, nameField);
		reqDetailLayout.putConstraint(SpringLayout.SOUTH, descriptionField, -10, SpringLayout.SOUTH, reqDetails);

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
			// if estimate matches current user and req
			return true;
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
		// fill in with Jon's stuff
		votingPanel = new JPanel();

		voteOnReqPanel.setLayout(layout);

		layout.putConstraint(SpringLayout.NORTH, reqDetailPanel, 5, SpringLayout.NORTH, voteOnReqPanel);
		layout.putConstraint(SpringLayout.WEST, reqDetailPanel, 5, SpringLayout.WEST, voteOnReqPanel);
		layout.putConstraint(SpringLayout.EAST, reqDetailPanel, -5, SpringLayout.EAST, voteOnReqPanel);

		layout.putConstraint(SpringLayout.SOUTH, votingPanel, -5, SpringLayout.SOUTH, voteOnReqPanel);
		layout.putConstraint(SpringLayout.WEST, votingPanel, 0, SpringLayout.WEST, reqDetailPanel);
		layout.putConstraint(SpringLayout.EAST, votingPanel, 0, SpringLayout.EAST, reqDetailPanel);

		layout.putConstraint(SpringLayout.SOUTH, reqDetailPanel, -10, SpringLayout.NORTH, votingPanel);

		voteOnReqPanel.add(reqDetailPanel);
		voteOnReqPanel.add(votingPanel);
	}


}
