/*******************************************************************************
 * Copyright (c) 2014 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Team8s
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.overview;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.util.List;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSessionModel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.ViewEventController;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.RequirementModel;

import javax.swing.JSplitPane;

public class OverviewDetailPanel extends JSplitPane {
	PlanningPokerSession currentSession;

	public OverviewDetailPanel () {

		this.setOrientation(JSplitPane.VERTICAL_SPLIT);
		
		// Create the info panel and table panel
		OverviewDetailInfoPanel infoPanel = new OverviewDetailInfoPanel();
		OverviewReqTable reqTable = new OverviewReqTable();
		
		// Put the overview table and sidebar into the tab
		this.setTopComponent(infoPanel);
		this.setBottomComponent(reqTable);
		this.setResizeWeight(0.2);  // set the right screen to not show by default

		ViewEventController.getInstance().setOverviewTree(treePanel);
		ViewEventController.getInstance().setOverviewDetailPanel(detailPanel);

				
	}
	
	public void updatePanel(final PlanningPokerSession session)	{
		
		this.currentSession = session;
		String endDate = "No end date";
		Set<Integer> requirements = session.getRequirementIDs();
		
		// Change name
		this.lblSessionName.setText(session.getName());
		
		// Change requirements list
		this.listModel.clear();
		if (session.requirementsGetSize() > 0) 
		{
			for (Integer id : requirements) {
				Requirement requirement = RequirementModel.getInstance().getRequirement(id);
				if (requirement != null) {
					this.listModel.addElement(requirement);
				}
			}
		}
		
		requirementsList = new JList<Requirement>(listModel);

		// Change end date
		try {
			endDate = DateFormat.getDateInstance(DateFormat.MEDIUM).format(session.getEndDate().getTime());
		} catch (NullPointerException ex) {
			endDate = new String("No end date");
		}
		
		this.lblEndDate.setText(endDate);

		// change the visibility of the top buttons
		setButtonVisibility(session);
		
		// redraw panel
		infoPanel.revalidate();
		infoPanel.repaint();
	}	
	
	private void setButtonVisibility(PlanningPokerSession session) {
		// Check if the buttons should appear
		/*
			btnEdit.isVisible(false);
			if (session.isEditable()) {
				btnEdit.isVisible(true);
			}
		*/
	}


	
	
	public PlanningPokerSession getCurrentSession() {
		
		return this.currentSession;
	}
}
