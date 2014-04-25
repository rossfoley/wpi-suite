/*******************************************************************************
 * Copyright (c) 2014 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Team8s
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.overview;

import java.awt.Component;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Set;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DropMode;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.controller.GetSessionController;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.controller.GetUserController;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.Estimate;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.UserModel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.ViewEventController;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.RequirementModel;

public class OverviewVoterTable extends JTable {
	private DefaultTableModel tableModel = null;
	private boolean initialized;
	private final Border paddingBorder = BorderFactory.createEmptyBorder(0, 0, 0, 0);
	private final PlanningPokerSession planningPokerSession;
	Requirement selectedRequirement;
	
	/**
	 * Sets initial table view
	 * @param data	Initial data to fill OverviewReqTable
	 * @param columnNames	Column headers of OverviewReqTable
	 */
	public OverviewVoterTable(Object[][] data, String[] columnNames, PlanningPokerSession pps) {
		planningPokerSession = pps;
		tableModel = new DefaultTableModel(data, columnNames);
		this.setModel(tableModel);
		this.setDefaultRenderer(Object.class, new DefaultTableCellRenderer());
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.setDragEnabled(true);
        this.setDropMode(DropMode.ON);
		this.getTableHeader().setReorderingAllowed(false);
		this.setAutoCreateRowSorter(true);
		setFillsViewportHeight(true);
		initialized = false;
		ViewEventController.getInstance().setOverviewVoterTable(this);

	}

	/**
	 * 
	 * @return
	 */
	public Requirement getSelectedRequirement() {
		return this.selectedRequirement;
	}
	/**
	 * 
	 * @return
	 */
	public List<String> getAllVoterNamesList() {
		final List<String> allVoters = new ArrayList<String>();
		GetUserController.getInstance().retrieveUsers();
		final List<User> user = UserModel.getInstance().getUsers();
		for(User u : user) {
			try {
				allVoters.add(u.getUsername());
			} catch (Exception E) {}
		}
		return allVoters;
	}
	/**
	 * 
	 * @return
	 */
	public List<Requirement> getSessionReqs(){
		final Set<Integer> sessionReqIds = planningPokerSession.getRequirementIDs();
		final List<Requirement> sessionReqs = new LinkedList<Requirement>();
		for (Integer id : sessionReqIds) {
			Requirement current = RequirementModel.getInstance().getRequirement(id);
			sessionReqs.add(current);			
		}
		return sessionReqs;
	}
	public boolean EstimateContains(List<Estimate> estimateList, String name) {
		for (Estimate e : estimateList) {
			if (name.equals(e.getOwnerName())) {
				return true;
			}
		}
		return false;		
	}
	public int getVote (Requirement reqToVoteOn) {
		if(reqToVoteOn == null) {
			return -1;
		}
		final List<Estimate> ListofEstiamte =  planningPokerSession.getEstimates();
		for(Estimate e : ListofEstiamte) {
			if(reqToVoteOn.getId() == e.getRequirementID()) {
				return e.getVote();
			}
		}
		return -1;
	}
	public void populateVotePanel(Requirement reqToVoteOn) {
		if(reqToVoteOn == null) {
			return ;	
		}
		StringBuilder sb = new StringBuilder();
		System.out.println(reqToVoteOn.getId());
		System.out.println(reqToVoteOn.getName());
		tableModel.setRowCount(0);	
		this.selectedRequirement = reqToVoteOn;
		final List<String> allUserList = getAllVoterNamesList();
		final List<Requirement> ListOfRequirements =  getSessionReqs();
		for (String s : allUserList) {
			if(EstimateContains(planningPokerSession.getEstimates(), s)){
				System.out.println(s + "populatePanel");
				int reqID = reqToVoteOn.getId();
				String reqName = reqToVoteOn.getName();
				String username = s;
				String vote = String.valueOf(getVote(reqToVoteOn));
				tableModel.addRow(new Object[]{
						reqID,
						reqName,
						username,
						vote});					
			} else {
				System.out.println(s + "populatePanel");
				int reqID = reqToVoteOn.getId();
				String reqName = reqToVoteOn.getName();
				String username = s;
				String vote = "FALSE";
				tableModel.addRow(new Object[]{
						reqID,
						reqName,
						username,
						vote});					
			}
		}
	}
	// bottom one doenst work for most people excpet me :(
	
/*
	public void populateVotePanel(Requirement reqToVoteOn) {
		tableModel.setRowCount(0);	
		final Set<Integer> requirementIDs = planningPokerSession.getRequirementIDs();
		this.selectedRequirement = reqToVoteOn;
		final List<String> allUserList = getAllVoterNamesList();
		final List<Requirement> ListOfRequirements =  getSessionReqs();
		for (Requirement r : ListOfRequirements) {
			int reqID = r.getId();
			String reqName = r.getName();
			boolean vote = false;
			for (int i = 0; i < planningPokerSession.getEstimateVoterList().size(); i++) {
				if(planningPokerSession.getEstimateVoterList().get(i).getRequirementID() == r.getId()) {
					for (String s : allUserList) {
						vote = false;
						String username = s;
						if(planningPokerSession.getEstimateVoterList().get(i).getVoterNameList().contains(s)) {
							vote = true; //mean voted
							tableModel.addRow(new Object[]{
									reqID,
									reqName,
									username,
									vote});	
						} else {
							tableModel.addRow(new Object[]{
									reqID,
									reqName,
									username,
									vote});	
						}
					}
				}
			}
		}
	}
	*/
	/**DOESNT WORK CARE
	 *  populates the panel
	 *  
	 */
	public void populateVotePanelNOTCOMPLETE() {
		tableModel.setRowCount(0);	
		final Set<Integer> requirementIDs = planningPokerSession.getRequirementIDs();
		
		final List<String> allUserList = getAllVoterNamesList();
		final List<Requirement> ListOfRequirements =  getSessionReqs();
		for (Requirement r : ListOfRequirements) {
			int reqID = r.getId();
			String reqName = r.getName();
			int vote = -1;
			for (Estimate ev : planningPokerSession.getEstimates()) {
				if (ev.getRequirementID() == reqID) {
					for (String s : allUserList) {
						String username = s;
						for (Estimate evs : planningPokerSession.getEstimates()) {
							if (evs.getOwnerName().equals(s)) {
								vote = evs.getVote();
								tableModel.addRow(new Object[]{
										reqID,
										reqName,
										username,
										vote});
								break;
							} else {
								vote = -1;
								tableModel.addRow(new Object[]{
										reqID,
										reqName,
										username,
										vote});
								break;
							}
						}
					}
				}
			}
		}
	}
	public void paintComponent(Graphics g)
	{
		if(!initialized) {
			try {
				GetSessionController.getInstance().retrieveSessions();
				initialized = true;
			} catch (Exception e) {}
		}

		super.paintComponent(g);
	}
	
	/**
	 * Method prepareRenderer.
	 * @param renderer TableCellRenderer
	 * @param row int
	 * @param column int
	 * @return Component
	 */
	@Override
    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
        final Component comp = super.prepareRenderer(renderer, row, column);

        if (JComponent.class.isInstance(comp)) {
            ((JComponent)comp).setBorder(paddingBorder);
        }
		return comp;

    }
	
	/**
	 * Overrides the isCellEditable method to ensure no cells are editable.
	 * @param row	row of OverviewReqTable cell is located
	 * @param col	column of OverviewReqTable cell is located
	 * @return boolean */
	@Override
	public boolean isCellEditable(int row, int col)	{
		return false;
	}

}
