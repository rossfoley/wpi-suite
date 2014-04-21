/*******************************************************************************
 * Copyright (c) 2014 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Team8s
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.updateestimates;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.swing.SpringLayout;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.overview.OverviewReqTable;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTable;

/**
 * @author amandaadkins
 * this class is the pane that allows users to select final estimates to update requirements in requirements manager with
 */
public class SelectEstimatesToSendToReqManagerPane extends JSplitPane {
	private PlanningPokerSession currentSession;
	private ArrayList<Integer> reqsWithExportedEstimates;
	private HashMap<Integer, Integer> finalEstimates;
	private SpringLayout summaryLayout = new SpringLayout();
	private SpringLayout selectionLayout = new SpringLayout();
	//private SelectRequirementToUpdateTable selectToUpdateTable;
	private JTable selectToUpdateTable;
	private JList<String> noFinalEstimateList = new JList<String>();
	private JTable alreadySentTable = new JTable();
	private JPanel summaryPanel = new JPanel();
	private LinkedList<Integer> selectableRequirementIDs;
	private JPanel selectionPanel = new JPanel();
	private JButton sendEstimatesButton = new JButton("Send Estimates");
	private JButton cancelButton = new JButton("Cancel");
	
	public SelectEstimatesToSendToReqManagerPane(PlanningPokerSession currentSession){
		setOrientation(JSplitPane.VERTICAL_SPLIT);
		this.currentSession = currentSession;
		reqsWithExportedEstimates = currentSession.getRequirementsWithExportedEstimates();
		
		Object[][] data = {};
		String[] columnNames  = {"Send Estimate?", "Requirement Name", "Final Estimate"};
		
		// replace with actual final estimates 
		finalEstimates = new HashMap<Integer, Integer>();
		selectableRequirementIDs = determineSelectableRequirements();
		
		selectToUpdateTable = new SelectRequirementToUpdateTable(data, columnNames, selectableRequirementIDs, finalEstimates);
		
		buildSummaryPanel();	
		buildSelectionPanel();
		
		selectToUpdateTable.getColumnModel().getColumn(0).setMinWidth(75); 
		selectToUpdateTable.getColumnModel().getColumn(1).setMinWidth(200); 
		selectToUpdateTable.getColumnModel().getColumn(2).setMinWidth(150);
		selectToUpdateTable.getColumnModel().getColumn(2).setMaxWidth(200);
		summaryPanel.setMinimumSize(new Dimension(200, 100));
		
		//selectToUpdateTable.refresh();
		setTopComponent(summaryPanel);
		setBottomComponent(selectionPanel);
		
		selectionPanel.setMinimumSize(new Dimension(100, 100));
		
		setDividerLocation(125);	
	}

	/**
	 *  this builds the panel for the upper half of this page which contains a list of requirements still 
	 *  needing final estimates and a table of final estimates that have already been sent
	 */
	private void buildSummaryPanel(){
		summaryPanel.setLayout(summaryLayout);
		
		summaryLayout.putConstraint(SpringLayout.WEST, noFinalEstimateList, 0, SpringLayout.WEST, summaryPanel);
		summaryLayout.putConstraint(SpringLayout.EAST, noFinalEstimateList, -40, SpringLayout.HORIZONTAL_CENTER, summaryPanel);
		
		summaryLayout.putConstraint(SpringLayout.WEST, alreadySentTable, 10, SpringLayout.EAST, noFinalEstimateList);
		summaryLayout.putConstraint(SpringLayout.EAST, alreadySentTable, 0, SpringLayout.EAST, summaryPanel);
		
		summaryLayout.putConstraint(SpringLayout.NORTH, alreadySentTable, 10, SpringLayout.NORTH, summaryPanel);
		summaryLayout.putConstraint(SpringLayout.SOUTH, alreadySentTable, -10, SpringLayout.SOUTH, summaryPanel);
		
		summaryLayout.putConstraint(SpringLayout.NORTH, noFinalEstimateList, 10, SpringLayout.NORTH, summaryPanel);
		summaryLayout.putConstraint(SpringLayout.SOUTH, noFinalEstimateList, -10, SpringLayout.SOUTH, summaryPanel);
		
		summaryPanel.add(alreadySentTable);
		summaryPanel.add(noFinalEstimateList);
	}

	/**
	 * 
	 */
	private void buildSelectionPanel(){
		selectionPanel.setLayout(selectionLayout);
		
		selectionLayout.putConstraint(SpringLayout.SOUTH, sendEstimatesButton, -10, SpringLayout.SOUTH, selectionPanel);
		selectionLayout.putConstraint(SpringLayout.EAST, sendEstimatesButton, -10, SpringLayout.EAST, selectionPanel);
		
		selectionLayout.putConstraint(SpringLayout.SOUTH, cancelButton, 0, SpringLayout.SOUTH, sendEstimatesButton);
		selectionLayout.putConstraint(SpringLayout.EAST, cancelButton, -10, SpringLayout.WEST, sendEstimatesButton);
		
		selectionLayout.putConstraint(SpringLayout.SOUTH, selectToUpdateTable, -10, SpringLayout.NORTH, sendEstimatesButton);
		selectionLayout.putConstraint(SpringLayout.NORTH, selectToUpdateTable, 10, SpringLayout.NORTH, selectionPanel);
		selectionLayout.putConstraint(SpringLayout.WEST, selectToUpdateTable, 10, SpringLayout.WEST, selectionPanel);
		selectionLayout.putConstraint(SpringLayout.EAST, selectToUpdateTable, -10, SpringLayout.EAST, selectionPanel);
		
		selectionPanel.add(sendEstimatesButton);
		selectionPanel.add(cancelButton);
		selectionPanel.add(selectToUpdateTable);
	}
	
	
	/**
	 * this panel determines which requirements in the current planning poker session 
	 * the user should be able to select when picking final estimates to send to requirement manager
	 * @return a list of the ids of the requirements that the user should be able to choose
	 */
	private LinkedList<Integer> determineSelectableRequirements(){
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
	
	/**
	 * @return a list of the ids of the requirements that the user can choose from when updating estimates 
	 * in requirement manager
	 */
	private LinkedList<Integer> getSelectableRequirements(){
		return selectableRequirementIDs;	
	}
	
	/**
	 * @return gets the planning poker session that the user is currently viewing requirements from
	 */
	public PlanningPokerSession getDisplaySession(){
		return currentSession;
	}
}
