package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.requirementselection;

import java.util.List;
import java.util.Set;

import javax.swing.JSplitPane;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.gui.RequirementSelectionPanel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.requirementcreation.RequirementCreationInformationPanel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.requirementcreation.RequirementCreationPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.requirements.RequirementInformationPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.requirements.ViewMode;

public class RequirementSelectionView extends JSplitPane{
	
	private RequirementSelectionPanel reqPanel;
	private RequirementCreationPanel infoPanel;
	
	public RequirementSelectionView(){
		reqPanel = new RequirementSelectionPanel(this);
		infoPanel = new RequirementCreationPanel(-1);
		
		setLeftComponent(reqPanel);
		setRightComponent(null);
		setDividerLocation(600);
	}
	
	public List<Requirement> getSelected(){
		return reqPanel.getSelected();
	}
	
	public void setSelectedRequirements(Set<Integer> selectedRequirements){
		reqPanel.setSelectedRequirements(selectedRequirements);
	}
	
}
