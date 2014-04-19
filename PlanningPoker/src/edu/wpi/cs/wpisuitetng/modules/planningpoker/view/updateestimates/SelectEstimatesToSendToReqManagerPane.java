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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.swing.SpringLayout;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;

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
	private List<Integer> reqsWithExportedEstimates;
	private HashMap<Integer, Integer> finalEstimates;
	private SpringLayout summaryLayout = new SpringLayout();
	private JTable selectToUpdateTable;
	private JList noFinalEstimateList;
	private JTable alreadySentTable;
	private JPanel summaryPanel = new JPanel();
	private Object[][] data = {};
	private String[] columnNames  = {"Send Estimate?", "Requirement Name", "Final Estimate"};
	private LinkedList<Integer> selectableRequirementIDs;
	
	public SelectEstimatesToSendToReqManagerPane(PlanningPokerSession currentSession){
		this.currentSession = currentSession;
		reqsWithExportedEstimates = currentSession.getRequirementsWithExportedEstimates();
		
		// replace with actual final estimates 
		finalEstimates = new HashMap<Integer, Integer>();
		selectableRequirementIDs = determineSelectableRequirements();
		
		selectToUpdateTable = new SelectRequirementToUpdateTable(data, columnNames, selectableRequirementIDs, finalEstimates);

		buildSummaryPanel();		
		
		summaryPanel.setMinimumSize(new Dimension(200, 200));
		selectToUpdateTable.setMinimumSize(new Dimension(300, 350));
		
		setTopComponent(summaryPanel);
		setBottomComponent(selectToUpdateTable);
		
		setDividerLocation(250);	
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
