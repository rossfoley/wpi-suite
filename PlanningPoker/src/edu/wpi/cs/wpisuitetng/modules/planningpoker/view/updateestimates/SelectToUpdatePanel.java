package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.updateestimates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.JTable;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.overview.OverviewReqTable;




public class SelectToUpdatePanel extends JPanel {
	private PlanningPokerSession currentSession;
	private ArrayList<Integer> reqsWithExportedEstimates;
	private LinkedList<Integer> selectableRequirementIDs;
	private HashMap<Integer, Integer> finalEstimates;
	//private SelectRequirementToUpdateTable selectToUpdateTable;
	private JTable selectToUpdateTable;
	private SpringLayout selectionLayout = new SpringLayout();
	
	
	public SelectToUpdatePanel(PlanningPokerSession session){
		setLayout(selectionLayout);
		this.currentSession = session;
		reqsWithExportedEstimates = currentSession.getRequirementsWithExportedEstimates();
		
		Object[][] data = {};
		String[] columnNames  = {"Send Estimate?", "Requirement Name", "Final Estimate"};
		
		// replace with actual final estimates 
		finalEstimates = new HashMap<Integer, Integer>();
		selectableRequirementIDs = determineSelectableRequirements();
		//selectToUpdateTable = new SelectRequirementToUpdateTable(data, columnNames, selectableRequirementIDs, finalEstimates);
		selectToUpdateTable = new OverviewReqTable(data, columnNames);
		
		selectToUpdateTable.getColumnModel().getColumn(0).setMinWidth(75); 
		selectToUpdateTable.getColumnModel().getColumn(1).setMinWidth(200); 
		selectToUpdateTable.getColumnModel().getColumn(2).setMinWidth(150);
		selectToUpdateTable.getColumnModel().getColumn(2).setMaxWidth(200);
		
		selectionLayout.putConstraint(SpringLayout.NORTH, selectToUpdateTable, 10, SpringLayout.NORTH, this);
		selectionLayout.putConstraint(SpringLayout.SOUTH, selectToUpdateTable, -10, SpringLayout.SOUTH, this);
		selectionLayout.putConstraint(SpringLayout.WEST, selectToUpdateTable, 10, SpringLayout.WEST, this);
		selectionLayout.putConstraint(SpringLayout.EAST, selectToUpdateTable, -10, SpringLayout.EAST, this);
		
		
		add(selectToUpdateTable);
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
