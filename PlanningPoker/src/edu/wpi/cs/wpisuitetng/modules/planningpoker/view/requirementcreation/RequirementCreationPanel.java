/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team Rolling Thunder
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.requirementcreation;

import java.awt.BorderLayout;
//import java.awt.Graphics;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
//import javax.swing.JSplitPane;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.requirementselection.RequirementSelectionView;
//import edu.wpi.cs.wpisuitetng.modules.requirementmanager.controller.UpdateRequirementController;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;
//import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.characteristics.RequirementStatus;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.ViewEventController;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.requirements.RequirementButtonListener;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.requirements.RequirementPanelListener;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.requirements.ViewMode;

/**
 * @version $Revision: 1.0 $
 * @author Rolling Thunder
 */
public class RequirementCreationPanel extends JPanel implements RequirementButtonListener
{
	private final List<RequirementPanelListener> listeners = new LinkedList<RequirementPanelListener>();
	private final Requirement displayRequirement;
	private final ViewMode viewMode = ViewMode.CREATING;
	
	private RequirementCreationInformationPanel infoPanel;
	private RequirementCreationButtonPanel buttonPanel;
	private final RequirementSelectionView parent;
	
	private boolean readyToClose = false;
	private boolean readyToRemove = true;
	
	
	
	/**
	 * Constructor for Planning Poker Requirement Creation Panel
	 * @param parentID the parent id, or -1 if no parent.
	 * @param myParent the parent view which contains this panel
	 */
	public RequirementCreationPanel(int parentID, RequirementSelectionView myParent)
	{
		
		parent = myParent;
		
		displayRequirement = new Requirement();
		displayRequirement.setId(-2);
		
		try 
		{
			displayRequirement.setParentID(parentID);
		} 
		catch (Exception e) 
		{
			System.out.println(e.getMessage());
		}
		this.buildLayout();
	}
	
	/**
	 * Builds the layout of the panel.
	 */
	private void buildLayout()
	{
		buttonPanel = new RequirementCreationButtonPanel(this, viewMode, displayRequirement);
		listeners.add(buttonPanel);
		infoPanel = new RequirementCreationInformationPanel(this, viewMode, displayRequirement);
		listeners.add(infoPanel);
		
		this.setLayout(new BorderLayout());
		this.add(infoPanel, BorderLayout.CENTER); // Add scroll pane to panel
		this.add(buttonPanel, BorderLayout.SOUTH);
	}
	/**
	 * Method OKPressed.
	 * @see edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.requirements.RequirementButtonListener#OKPressed()
	 */
	@Override
	public void OKPressed() {
		if (infoPanel.validateFields(true)) {
			infoPanel.update();
			parent.newRequirementCreated();
			readyToClose = true;
		}
	}

	/**
	 * Method clearPressed.
	 * @see edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.requirements.RequirementButtonListener#clearPressed()
	 */
	@Override
	public void clearPressed() 
	{
		infoPanel.clearInfo();
	}

	/**
	 * Method cancelPressed.
	 * @see edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.requirements.RequirementButtonListener#cancelPressed()
	 */
	@Override
	public void cancelPressed() 
	{
		ViewEventController.getInstance().refreshTable();
		ViewEventController.getInstance().refreshTree();
		parent.closeCreationPanel();
	}
	
	/**
	 * Deletes the requirement. Sets all fields uneditable, sets status to
	 * deleted and closes the tab.
	 * @see edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.requirements.RequirementButtonListener#deletePressed()
	 */
	public void deletePressed() 
	{
		
	}

	/**
	 * Fires to all listeners whether the requirement has been deleted or not
	 * @param b whether the requirement has been deleted or not.
	 */
	public void fireDeleted(boolean b) {
		for(RequirementPanelListener listener : listeners)
		{
			listener.fireDeleted(b);
		}
	}

	/**
	 * Fires to all listeners whether the requirement is valid or not
	 * @param b whether the requirement is valid or not.
	 */
	public void fireValid(boolean b) {	
		for(RequirementPanelListener listener : listeners)
		{
			listener.fireValid(b);
		}
	}
	
	/**
	 * Fires to all listeners whether changes have occured
	 * @param b whether changes have occured.
	 */
	public void fireChanges(boolean b) {
		for(RequirementPanelListener listener : listeners)
		{
			listener.fireChanges(b);
		}
	}
	
	/**
	 * Fires to all listeners to refresh.
	 */
	public void fireRefresh()
	{
		for(RequirementPanelListener listener : listeners)
		{
			listener.fireRefresh();
		}
	}

	/**
	 * 		displays the given error message
	 * @param msg the message to display.
	 */
	public void displayError(String msg)
	{
		buttonPanel.getErrorPanel().displayError(msg);
	}
	
	/**
	 * 		Removes the given error message
	 * @param msg the message to display.
	 */
	public void removeError(String msg)
	{
		buttonPanel.getErrorPanel().removeError(msg);
	}
	
	/**
	
	 * @return whether the requirement panel as a whole is ready to be removed. */
	public boolean readyToRemove() {
		if(readyToClose) return true;
		
		
		for(RequirementPanelListener listener : listeners)
		{
			readyToRemove &= listener.readyToRemove();
		}
		
		if(readyToRemove)
		{
			return true;
		}
		else
		{
			final int result = JOptionPane.showConfirmDialog(this, "Discard unsaved changes and close tab?", "Discard Changes?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			
			return result == 0;
		}
	}
	
	/**
	
	 * @return the requirement information panel. */
	public RequirementCreationInformationPanel getInfoPanel()
	{
		return infoPanel;
	}
	
	/**
	
	 * @return the button panel */
	public RequirementCreationButtonPanel getButtonPanel()
	{
		return buttonPanel;
	}
	
	/**
	
	 * @return the display requirement. */
	public Requirement getDisplayRequirement() {
		return displayRequirement;
	}

	/**
	 * Method isReadyToRemove.
	 * @return boolean
	 */
	public boolean isReadyToRemove() {
		return readyToRemove;
	}

	/**
	 * Method setReadyToRemove.
	 * @param readyToRemove boolean
	 */
	public void setReadyToRemove(boolean readyToRemove) {
		this.readyToRemove = readyToRemove;
	}
}
