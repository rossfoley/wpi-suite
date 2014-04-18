/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.updateestimates;

import java.awt.Dimension;
import java.awt.Panel;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.swing.SpringLayout;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;

import javax.swing.JList;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTable;

/**
 * @author Amanda
 *
 */
public class SelectEstimatesToSendToRequirementManagerSplitPane extends JSplitPane {
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
	
	public SelectEstimatesToSendToRequirementManagerSplitPane(PlanningPokerSession currentSession){
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
	
	private LinkedList<Integer> getSelectableRequirements(){
		return selectableRequirementIDs;	
	}
}
