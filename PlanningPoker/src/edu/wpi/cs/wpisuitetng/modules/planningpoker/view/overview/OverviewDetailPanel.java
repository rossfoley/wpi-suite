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

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JScrollPane;
import javax.swing.SpringLayout;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.ViewEventController;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.updateestimates.SelectRequirementToUpdateTable;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.RequirementModel;

/**
 * This is the right half of the overview panel, and contains
 * the OverviewDetailInfoPanel and the OverviewReqTable
 * @author Randy Acheson
 * @version 4/18/14
 */
public class OverviewDetailPanel extends JSplitPane {
	static PlanningPokerSession currentSession;
	OverviewDetailInfoPanel infoPanel;
	OverviewReqTable reqTable;
	SelectRequirementToUpdateTable selectToUpdateTable;
	JScrollPane tablePanel;
	boolean onSelectionTable;

	public OverviewDetailPanel () {
		onSelectionTable = false;

		this.setOrientation(JSplitPane.VERTICAL_SPLIT);
		
		final String[] columnNames = {"Requirement Name", "Your Vote", "Final Estimate"};
		final Object[][] data = {};
		
		// Create the info panel and table panel
		infoPanel = new OverviewDetailInfoPanel();
		reqTable = new OverviewReqTable(data, columnNames);
		tablePanel = new JScrollPane(reqTable);
		
		reqTable.getColumnModel().getColumn(0).setMinWidth(200); // Requirement Name
		reqTable.getColumnModel().getColumn(1).setMinWidth(100); // User Vote
		reqTable.getColumnModel().getColumn(1).setMaxWidth(100); // User Vote
		reqTable.getColumnModel().getColumn(2).setMinWidth(100); // Final Estimate
		reqTable.getColumnModel().getColumn(2).setMaxWidth(100); // Final Estimate
		
		// Put the info panel and table panel into the split pane
		this.setTopComponent(infoPanel);
		this.setBottomComponent(tablePanel);
		this.setResizeWeight(0.5); 

		ViewEventController.getInstance().setOverviewDetailInfoPanel(infoPanel);
		ViewEventController.getInstance().setOverviewReqTable(reqTable);
		
		// Makes the split pane divide 50/50 for each portion
		final Dimension d = new Dimension(200, 200);
        infoPanel.setMinimumSize(d);
        infoPanel.setPreferredSize(d);
        tablePanel.setMinimumSize(d);
        
        // Disable the split pane from being movable
        // Why that's the method name I have no idea
        this.setEnabled(false);
	}

	/**
	 * Updates each part of this split panel
	 * @param session The given session to update each panel with
	 */
	public void updatePanel(final PlanningPokerSession session) {

		currentSession = session;
		
		// update each part of the split panel
		updateInfoPanel(session);
		updateReqTable(session);
	}
	
	private void updateInfoPanel(PlanningPokerSession session) {
		infoPanel.refresh(session);
	}

	private void updateReqTable(PlanningPokerSession session) {
		reqTable.refresh(session);
	}
	
	public PlanningPokerSession getCurrentSession() {
		return currentSession;
	}

	/** 
	 * Sends all of the requirements' new estimation values to the requirement manager. 
	 */
	public static void sendAllEstimates() {
		LinkedList<Requirement> sessionRequirementList = new LinkedList<Requirement>();
		Set<Integer> requirementIDs;
		requirementIDs = currentSession.getRequirementIDs();
		Object[] arrayOfIDs = requirementIDs.toArray();
		int i;
		for (i = 0; i < arrayOfIDs.length; i++) {
			sessionRequirementList.addLast(RequirementModel.getInstance().getRequirement((int) arrayOfIDs[i]));
		}
		int j;
		for (j = 0; j < sessionRequirementList.size(); i++) {
			// if the requirement's estimate has not yet been sent.
			if (!getReqsWithExportedEstimatesList().contains(sessionRequirementList.get(j))) { 
			sessionRequirementList.get(j).setEstimate(currentSession.getFinalEstimates().get(sessionRequirementList.get(j)));
			currentSession.addRequirementToExportedList((int) arrayOfIDs[j]);
			}	
			// otherwise, do nothing and go to the next element.
		}
>>>>>>> Send new requirements' final estimation values to the requirement manager to overwrite the old estimation values. Brian Flynn
	}
	
	/**
	 * Sends a single requirement's final estimate to the requirement manager.
	 * 
	 * @param reqToSendFinalEstimate the requirement which is to have its final estimate sent to the
	 * requirement manager.
	 */
	public static void sendSingleEstimate(Requirement reqToSendFinalEstimate) {
		// if the requirement's estimate has not yet been sent
		if (!getReqsWithExportedEstimatesList().contains(reqToSendFinalEstimate)) { 
			reqToSendFinalEstimate.setEstimate(currentSession.getFinalEstimates().get(reqToSendFinalEstimate));
			currentSession.addRequirementToExportedList((int) reqToSendFinalEstimate.getId());
		}
		// otherwise, do nothing.
	}

	private static LinkedList<Requirement> getReqsWithExportedEstimatesList() {
		return currentSession.getReqsWithExportedEstimatesList();
	}
	
	public void makeSelectionTable(){
		Object[][] data = {};
		String[] columnNames  = {"Send Estimate?", "Requirement Name", "Final Estimate"};
		HashMap<Requirement, Integer> finalEstimatesByRequirement = currentSession.getFinalEstimates();
		HashMap<Integer, Integer> finalEstimates = new HashMap<Integer, Integer>();
		
		for (Requirement r:finalEstimatesByRequirement.keySet()){
			finalEstimates.put(r.getId(), finalEstimatesByRequirement.get(r));
		}
		
		LinkedList<Integer> selectableRequirementIDs = determineSelectableRequirements(finalEstimates);
		
		selectToUpdateTable = new SelectRequirementToUpdateTable(data, columnNames, selectableRequirementIDs, finalEstimates);
	}
	
	public void replaceTable(){
		onSelectionTable = true;
		int dividerLocation = getDividerLocation();
		
		JPanel selectionPanel = new JPanel();
		SpringLayout selectionLayout = new SpringLayout();
		selectionPanel.setLayout(selectionLayout);
		JButton cancelButton = new JButton("Cancel");
		JButton sendEstimatesButton = new JButton("Send Estimates");
		
		makeSelectionTable();

		selectToUpdateTable.getColumnModel().getColumn(0).setMinWidth(125); 
		selectToUpdateTable.getColumnModel().getColumn(0).setMaxWidth(125); 
		selectToUpdateTable.getColumnModel().getColumn(2).setMinWidth(100); // Final Estimate
		selectToUpdateTable.getColumnModel().getColumn(2).setMaxWidth(100); // Final Estimate
		
		JScrollPane updatePane = new JScrollPane(selectToUpdateTable);
		
		selectToUpdateTable.refresh();
		
		selectionLayout.putConstraint(SpringLayout.SOUTH, sendEstimatesButton, -10, SpringLayout.SOUTH, selectionPanel);
		selectionLayout.putConstraint(SpringLayout.EAST, sendEstimatesButton, -10, SpringLayout.EAST, selectionPanel);
		
		selectionLayout.putConstraint(SpringLayout.SOUTH, cancelButton, 0, SpringLayout.SOUTH, sendEstimatesButton);
		selectionLayout.putConstraint(SpringLayout.EAST, cancelButton, -10, SpringLayout.WEST, sendEstimatesButton);
		
		selectionLayout.putConstraint(SpringLayout.SOUTH, updatePane, -10, SpringLayout.NORTH, sendEstimatesButton);
		selectionLayout.putConstraint(SpringLayout.EAST, updatePane, -10, SpringLayout.EAST, selectionPanel);
		selectionLayout.putConstraint(SpringLayout.WEST, updatePane, 10, SpringLayout.WEST, selectionPanel);
		selectionLayout.putConstraint(SpringLayout.NORTH, updatePane, 10, SpringLayout.NORTH, selectionPanel);
		
		selectionPanel.add(sendEstimatesButton);
		selectionPanel.add(cancelButton);
		selectionPanel.add(updatePane);
		
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				putReqTableBack();
			}
		});
		
		sendEstimatesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<Integer> selectedReqIDs = selectToUpdateTable.getSelectedReqs();
				RequirementModel reqs = RequirementModel.getInstance();
				for (Integer selectedReq:selectedReqIDs){
					System.out.println(reqs.getRequirement(selectedReq).getName());
				}
			}
		});

		setBottomComponent(selectionPanel);
		setDividerLocation(dividerLocation);
        Dimension d = new Dimension(200, 200);
        selectionPanel.setMinimumSize(d);
	}
	
	private LinkedList<Integer> determineSelectableRequirements(HashMap<Integer, Integer> finalEstimates){
		LinkedList<Requirement> reqsWithExportedEstimates = currentSession.getReqsWithExportedEstimatesList();
		LinkedList<Integer> selectableRequirements = new LinkedList<Integer>();
		for (Integer reqID:currentSession.getRequirementIDs()){
			if (!(reqsWithExportedEstimates.contains(reqID))){
				if (finalEstimates.containsKey(reqID)){
					selectableRequirements.add(reqID);
				}
			}
		}
		return selectableRequirements;
	}
	
	public void putReqTableBack(){
		onSelectionTable = false;
		int dividerLocation = getDividerLocation();
		String[] columnNames = {"Requirement Name", "Your Vote", "Final Estimate"};
		Object[][] data = {};
		
		reqTable = new OverviewReqTable(data, columnNames);
		tablePanel = new JScrollPane(reqTable);
		
		reqTable.getColumnModel().getColumn(0).setMinWidth(200); // Requirement Name
		reqTable.getColumnModel().getColumn(1).setMinWidth(100); // User Vote
		reqTable.getColumnModel().getColumn(1).setMaxWidth(100); // User Vote
		reqTable.getColumnModel().getColumn(2).setMinWidth(100); // Final Estimate
		reqTable.getColumnModel().getColumn(2).setMaxWidth(100); // Final Estimate

		this.setBottomComponent(tablePanel);
		this.setResizeWeight(0.5); 
		
		Dimension d = new Dimension(200, 200);
	    tablePanel.setMinimumSize(d);	
		setDividerLocation(dividerLocation);
		
		updateReqTable(currentSession);
	}
	
	
	public boolean isOnSelectionTable(){
		return onSelectionTable; 
	}
}


