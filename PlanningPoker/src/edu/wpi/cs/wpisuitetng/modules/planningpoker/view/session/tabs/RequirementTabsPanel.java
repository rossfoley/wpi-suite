/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team Rolling Thunder
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.session.tabs;

import java.awt.Dimension;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;

import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.session.SessionPanel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.session.SessionPanelListener;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.session.ViewMode;

/**
 * @author justinhess
 * @version $Revision: 1.0 $
 */
public class RequirementTabsPanel extends JTabbedPane implements SessionPanelListener
{
	private final List<SessionPanelListener> listeners = new LinkedList<SessionPanelListener>();
	private final ViewMode viewMode;
	private final Requirement currentRequirement;
	private final RequirementHistoryPanel historyPanel;
//	private final RequirementNotePanel notePanel;
//	private final RequirementTestPanel testPanel;
//	private final SubrequirementPanel subReqPanel;
	
	/**
	 * Constructor for the requirement tabs panel
	 * @param parentPanel parent panel
	 * @param viewMode view mode
	 * @param currentRequirement current requirement
	 */
	public RequirementTabsPanel(SessionPanel parentPanel, ViewMode viewMode, Requirement currentRequirement) 
	{
		this.viewMode = viewMode;
		this.currentRequirement = currentRequirement;
		
		historyPanel = new RequirementHistoryPanel(this, this.viewMode, this.currentRequirement);
		listeners.add(historyPanel);
//		notePanel = new RequirementNotePanel(this, this.viewMode, this.currentRequirement);
//		listeners.add(notePanel);
//		testPanel = new RequirementTestPanel(this, this.viewMode, this.currentRequirement);
//		listeners.add(testPanel);
//		subReqPanel = new SubrequirementPanel(this, this.viewMode, this.currentRequirement);
//		listeners.add(subReqPanel);
		
		ImageIcon noteIcon = null;
		ImageIcon historyIcon = null;
		ImageIcon acceptanceIcon = null;
		ImageIcon childrenIcon = null;
		try {
		    noteIcon = new ImageIcon(ImageIO.read(getClass().getResource("note-icon.png")));
		    historyIcon = new ImageIcon(ImageIO.read(getClass().getResource("history-icon.png")));
		    acceptanceIcon = new ImageIcon(ImageIO.read(getClass().getResource("acceptance-icon.png")));
		    childrenIcon = new ImageIcon(ImageIO.read(getClass().getResource("children-icon.png")));
		} catch (IOException ex) {}
		
//		this.addTab("Notes", noteIcon, notePanel);
		this.addTab("Transaction History", historyIcon, historyPanel);
//		this.addTab("Acceptance Tests", acceptanceIcon, testPanel);
//		this.addTab("Subrequirements", childrenIcon, subReqPanel);
		
		this.setMinimumSize(new Dimension(500,100));		
	}

	/**
	 * Method readyToRemove.
	
	
	 * @return boolean * @see edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.requirements.RequirementPanelListener#readyToRemove() * @see edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.requirements.RequirementPanelListener#readyToRemove()
	 */
	public boolean readyToRemove() {
		boolean readyToRemove = true;
		for(SessionPanelListener listener : listeners)
		{
			readyToRemove &= listener.readyToRemove();
		}
		return readyToRemove;
	}
	/**
	 * Method fireDeleted.
	 * @param b boolean
	
	 * @see edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.requirements.RequirementPanelListener#fireDeleted(boolean) 
	 */
	public void fireDeleted(boolean b)
	{
		for(SessionPanelListener listener : listeners)
		{
			listener.fireDeleted(b);
		}	
	}
	/**
	 * Method fireValid.
	 * @param b boolean
	
	 * @see edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.requirements.RequirementPanelListener#fireValid(boolean) */
	@Override
	public void fireValid(boolean b) {
		for(SessionPanelListener listener : listeners)
		{
			listener.fireValid(b);
		}		
	}
	
	/**
	 * Method fireChanges.
	 * @param b boolean
	
	 * @see edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.requirements.RequirementPanelListener#fireChanges(boolean) */
	@Override
	public void fireChanges(boolean b) {
		for(SessionPanelListener listener : listeners)
		{
			listener.fireChanges(b);
		}		
	}

	/**
	 * Method fireRefresh.
	 * @see edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.requirements.RequirementPanelListener#fireRefresh()
	 */
	@Override
	public void fireRefresh() {
		for(SessionPanelListener listener : listeners)
		{
			listener.fireRefresh();
		}
	}

	/**
	
//	 * @return the note panel */
//	public RequirementNotePanel getNotePanel() {
//		return notePanel;
//	}
//
//	/**
//	 * Method getTestPanel.
//	 * @return RequirementTestPanel
//	 */
//	public RequirementTestPanel getTestPanel() {
//		return testPanel;
//	}
}
