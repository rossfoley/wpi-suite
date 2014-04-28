/*******************************************************************************
 * Copyright (c) 2014 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Team8s
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.requirementselection;

import java.util.List;
import java.util.Set;

import javax.swing.JSplitPane;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.requirementcreation.RequirementCreationPanel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.session.RequirementSelectionPanel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.session.RequirementsSelectedListener;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;

public class RequirementSelectionView extends JSplitPane{
	
	private final RequirementSelectionPanel reqPanel;
	private final RequirementCreationPanel createPanel;
	
	public RequirementSelectionView(PlanningPokerSession session){
		reqPanel = new RequirementSelectionPanel(this, session);
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
	
	/**
	 * Gets the list of selected requirements from the 
	 * RequirementSelectionPanel.
	 * @return List<Requirements>
	 */
	public List<Requirement> getSelected(){
		return reqPanel.getSelected();
	}
	
	/**
	 * Pass the selected Requirements into the requirementSelectionPanel
	 * @param selectedRequirements
	 */
	public void setSelectedRequirements(Set<Integer> selectedRequirements){
		reqPanel.setSelectedRequirements(selectedRequirements);
	}
	
	//This is a Hack - Perry
	synchronized public void addRequirementsSelectedListener(RequirementsSelectedListener l){
		reqPanel.addRequirementsSelectedListener(l);
	}
	
}
