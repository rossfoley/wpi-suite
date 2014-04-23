/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.requirementselection;

import java.util.List;
import java.util.Set;

import javax.swing.JSplitPane;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.gui.RequirementSelectionPanel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.gui.RequirementsSelectedListener;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.requirementcreation.RequirementCreationPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;

/**
 * This class is used for the requirement selection screen.
 * @author theteam8s
 * @version 1.0
 */
public class RequirementSelectionView extends JSplitPane{
	
	private final RequirementSelectionPanel reqPanel;
	private final RequirementCreationPanel createPanel;
	
	/**
	 * Constructor is used to create the JSplitPane for the requirement
	 * selection screen.
	 */
	public RequirementSelectionView(){
		reqPanel = new RequirementSelectionPanel(this);
		createPanel = new RequirementCreationPanel(-1, this);
		
		setLeftComponent(reqPanel);
		setRightComponent(null);
		setDividerLocation(getSize().width - getInsets().right - getDividerSize() - 500);
		setResizeWeight(1.0);
	}
	
	/**
	 * Opens the requirement creation tab
	 */
	public void openCreationPanel(){
		setRightComponent(createPanel);
	}
	
	/**
	 * Adds the new requirement to the RequirementSelectionPanel
	 * and closes the requirement creation tab.
	 */
	public void newRequirementCreated(){
		reqPanel.newRequirementAdded(createPanel.getDisplayRequirement());
		closeCreationPanel();
	}
	
	/**
	 * Closes the requirement creation tab.
	 */
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
	/**
	 * Adds a requirement selected listener to the requirement panel.
	 * @param l
	 */
	public synchronized void addRequirementsSelectedListener(RequirementsSelectedListener l){
		reqPanel.addRequirementsSelectedListener(l);
	}
	
}
