/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
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
	public OverviewPanel()
	{
		// Set the data for the overview table
		String[] columnNames = {"", "Name", "End Date", "Requirements, Deck, Creator Username"};
		Object[][] data = {};
		
		// Create the overview table and put it in a scroll pane
		OverviewTable table = new OverviewTable(data, columnNames);
		JScrollPane tablePanel = new JScrollPane(table);
		
		// Create the detail panel
		OverviewDetailPanel detailPanel = new OverviewDetailPanel(new PlanningPokerSession());
			
		// Set the widths of the columns
		table.getColumnModel().getColumn(0).setMaxWidth(0); // ID
		table.getColumnModel().getColumn(1).setMinWidth(150); // Name
		table.getColumnModel().getColumn(2).setMaxWidth(80); // End Date
		table.getColumnModel().getColumn(3).setMaxWidth(90); // Number of Requirements
		table.getColumnModel().getColumn(4).setMinWidth(90);
		table.getColumnModel().getColumn(5).setMaxWidth(90);
		
		
		// Put the overview table and sidebar into the tab
		this.setLeftComponent(tablePanel);
		this.setRightComponent(detailPanel);
		this.setResizeWeight(0);  // set the right screen to not show by default
		
		// Tell the ViewEventController what the overview table and the detail panel are
		ViewEventController.getInstance().setOverviewTable(table);
		ViewEventController.getInstance().setOverviewDetailPanel(detailPanel);

	}

}
