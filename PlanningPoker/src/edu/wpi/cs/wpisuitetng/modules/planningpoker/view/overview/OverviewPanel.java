/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team Rolling Thunder
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.overview;

import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.ViewEventController;

/**
 * @author justinhess
 * @version $Revision: 1.0 $
 */
public class OverviewPanel extends JSplitPane {
	
	/**
	 * Sets up directory table of requirements in system
	 */
	public OverviewPanel()
	{
		OverviewTreePanel filterPanel = new OverviewTreePanel();
		
		//String[] columnNames = {"ID", "Name", "Creator", "End Date", "End Time", "Estimate"};
		String[] columnNames = {"ID", "Name", "End Date"};
	
		Object[][] data = {};
		
		OverviewTable table = new OverviewTable(data, columnNames);
		
		JScrollPane tablePanel = new JScrollPane(table);
				
		table.getColumnModel().getColumn(0).setMaxWidth(40); // ID
		
		table.getColumnModel().getColumn(1).setMinWidth(250); // Name

		//table.getColumnModel().getColumn(2).setMinWidth(90); // Creator
		
		table.getColumnModel().getColumn(2).setMinWidth(90); // End Date
		
		//table.getColumnModel().getColumn(4).setMinWidth(105); // End Time
		//table.getColumnModel().getColumn(4).setMaxWidth(105); // End Time
		
		//table.getColumnModel().getColumn(5).setMinWidth(85); // Estimate
		//table.getColumnModel().getColumn(5).setMaxWidth(85); // Estimate
		
		this.setLeftComponent(filterPanel);
		this.setRightComponent(tablePanel);
		this.setDividerLocation(180);
		
		ViewEventController.getInstance().setOverviewTable(table);
	}

}
