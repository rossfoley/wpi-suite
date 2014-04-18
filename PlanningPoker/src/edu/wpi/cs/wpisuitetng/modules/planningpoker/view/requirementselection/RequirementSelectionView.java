package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.requirementselection;

import java.util.List;
import java.util.Set;

import javax.swing.JSplitPane;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.gui.RequirementSelectionPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.requirements.RequirementInformationPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.requirements.RequirementPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.requirements.ViewMode;

public class RequirementSelectionView extends JSplitPane{
	
	private RequirementSelectionPanel reqPanel;
	private RequirementInformationPanel infoPanel;
	
	public RequirementSelectionView(){
		reqPanel = new RequirementSelectionPanel(this);
		//infoPanel = new RequirementInformationPanel(new RequirementPanel(0),ViewMode.CREATING,new Requirement());
		
		setLeftComponent(reqPanel);
		//setRightComponent(voteOnReqPanel);
		setDividerLocation(600);
	}
	
	public List<Requirement> getSelected(){
		return reqPanel.getSelected();
	}
	
	public void setSelectedRequirements(Set<Integer> selectedRequirements){
		reqPanel.setSelectedRequirements(selectedRequirements);
	}
	
}
