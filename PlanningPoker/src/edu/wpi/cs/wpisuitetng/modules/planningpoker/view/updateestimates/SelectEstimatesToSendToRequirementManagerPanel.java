/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.updateestimates;

import java.awt.Panel;
import java.util.HashMap;
import java.util.List;

import javax.swing.SpringLayout;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;
import javax.swing.JList;
import javax.swing.JCheckBox;

/**
 * @author Amanda
 *
 */
public class SelectEstimatesToSendToRequirementManagerPanel extends Panel {
	private PlanningPokerSession currentSession;
	private List<Integer> reqsWithExportedEstimates;
	private HashMap<Integer, Integer> finalEstimates;
	private SpringLayout selectionLayout;
	
	public SelectEstimatesToSendToRequirementManagerPanel(PlanningPokerSession currentSession){
		this.currentSession = currentSession;
		
		JCheckBox chckbxNewCheckBox = new JCheckBox("New check box");
		add(chckbxNewCheckBox);
		
		JList list = new JList();
		
		add(list);
		reqsWithExportedEstimates = currentSession.getRequirementsWithExportedEstimates();
		// replace with actual final estimates 
		finalEstimates = new HashMap<Integer, Integer>();
		
		buildSelectionPanel();
		
	}
	
	public void buildSelectionPanel(){
		selectionLayout = new SpringLayout();
		
		
	}
	
	
}
