package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.requirementselection;

import java.awt.Frame;
import java.util.List;
import java.util.Set;

import javax.swing.JSplitPane;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.gui.RequirementSelectionPanel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.gui.RequirementsSelectedListener;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.requirementcreation.RequirementCreationInformationPanel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.requirementcreation.RequirementCreationPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.requirements.RequirementInformationPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.requirements.ViewMode;

public class RequirementSelectionView extends JSplitPane{
	
	private RequirementSelectionPanel reqPanel;
	private RequirementCreationPanel createPanel;
	
	public RequirementSelectionView(){
		reqPanel = new RequirementSelectionPanel(this);
		createPanel = new RequirementCreationPanel(-1, this);
		
		setLeftComponent(reqPanel);
		setRightComponent(null);
		setDividerLocation(getSize().width - getInsets().right - getDividerSize() - 500);
		setResizeWeight(1.0);
	}
	
	public void openCreationPanel(){
		setRightComponent(createPanel);
	}
	
	public void newRequirementCreated(){
		reqPanel.newRequirementAdded(createPanel.getDisplayRequirement());
		closeCreationPanel();
	}
	
	public void closeCreationPanel(){
		setRightComponent(null);
	}
	
	public List<Requirement> getSelected(){
		return reqPanel.getSelected();
	}
	
	public void setSelectedRequirements(Set<Integer> selectedRequirements){
		reqPanel.setSelectedRequirements(selectedRequirements);
	}
	
	//This is a Hack - Perry
	synchronized public void addRequirementsSelectedListener(RequirementsSelectedListener l){
		reqPanel.addRequirementsSelectedListener(l);
	}
	
}
