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

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.ViewEventController;

/**
 * @author rossfoley
 * @version $Revision: 1.0 $
 */
public class OverviewPanel extends JSplitPane {

	/**
	 * Sets up directory table of planning poker sessions in system
	 */
	public OverviewPanel(boolean isOpen)
	{
		// Set the data for the overview table
		String[] columnNames = {"", "Name", "End Date", "Requirements", "Deck", "Creator Username"};
		Object[][] data = {};

		// Create the detail panel
		OverviewDetailPanel detailPanel = new OverviewDetailPanel(new PlanningPokerSession());

		// Create the overview table and put it in a scroll pane
		if (isOpen) { 
			table = new OpenOverviewTable(data, columnNames, detailPanel);
			ViewEventController.getInstance().setOpenOverviewTable(table);
			ViewEventController.getInstance().setOpenOverviewDetailPanel(detailPanel);
		}
		else {
			table = new ClosedOverviewTable(data, columnNames, detailPanel);
			ViewEventController.getInstance().setClosedOverviewTable(table);
			ViewEventController.getInstance().setClosedOverviewDetailPanel(detailPanel);
		}
		JScrollPane tablePanel = new JScrollPane(table);


		// Set the widths of the columns
		table.getColumnModel().getColumn(0).setMaxWidth(0); // ID
		table.getColumnModel().getColumn(1).setMinWidth(150); // Name
		table.getColumnModel().getColumn(2).setMaxWidth(80); // End Date
		table.getColumnModel().getColumn(3).setMaxWidth(90); // Number of Requirements
		table.getColumnModel().getColumn(4).setMinWidth(90);
		table.getColumnModel().getColumn(5).setMaxWidth(120);
		
		// Put the overview table and sidebar into the tab
		this.setLeftComponent(tablePanel);
		this.setRightComponent(detailPanel);
		this.setResizeWeight(0);  // set the right screen to not show by default


	}

}
