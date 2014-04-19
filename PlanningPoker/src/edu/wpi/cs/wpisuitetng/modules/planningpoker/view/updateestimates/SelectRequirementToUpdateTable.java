/*******************************************************************************
 * Copyright (c) 2014 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Team8s
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.updateestimates;

import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DropMode;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.RequirementModel;

/**
 * @author amandaadkins
 * this class is the table for displaying and selecting which final estimates from a planning poker session should be sent to requirement manager
 */
public class SelectRequirementToUpdateTable extends JTable {
	private DefaultTableModel tableModel = null;
	private boolean initialized;
	private boolean changedByRefresh = false;
	private Border paddingBorder = BorderFactory.createEmptyBorder(0, 4, 0, 0);
	private List<Integer> requirementsToDisplay;
	private HashMap<Integer, Integer> finalEstimates;

	/**
	 * Sets initial table view
	 * @param data	Initial data to fill OverviewTable
	 * @param columnNames	Column headers of OverviewTable
	 */
	public SelectRequirementToUpdateTable(Object[][] data, String[] columnNames, List<Integer> displayRequirementIDs, 
			HashMap<Integer, Integer> finalEstimates)	{
		this.tableModel = new DefaultTableModel(data, columnNames);
		this.setModel(tableModel);
		this.setDefaultRenderer(Object.class, new DefaultTableCellRenderer());
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.setDragEnabled(true);
		this.setDropMode(DropMode.ON);
		this.requirementsToDisplay = displayRequirementIDs;
		this.finalEstimates = finalEstimates;

		this.getTableHeader().setReorderingAllowed(false);
		this.setAutoCreateRowSorter(true);
		setFillsViewportHeight(true);

		initialized = false;

		/* Create double-click event listener */
		this.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {

				if(getRowCount() > 0) {
					int mouseY = e.getY();
					Rectangle lastRow = getCellRect(getRowCount() - 1, 0, true);
					int lastRowY = lastRow.y + lastRow.height;

					if(mouseY > lastRowY) {
						getSelectionModel().clearSelection();
						repaint();
					}
				}
			}
		});
	}

	/**
	 * refresh the table containing the requirements that can be selected
	 * @param session
	 */
	public void refresh() {
		RequirementModel reqs = RequirementModel.getInstance();
		// clear the table
		tableModel.setRowCount(0);		

		for (Integer requirementID : requirementsToDisplay) {
			Requirement req = reqs.getRequirement(requirementID);
			String reqName = req.getName();

			Integer reqEstimate = finalEstimates.get(requirementID);

			tableModel.addRow(new Object[]{
					reqName,
					reqEstimate
					});	
		}
		// indicate that refresh is no longer affecting the table
		setChangedByRefresh(false);
	}

	/**
	 * @return the changedByRefresh 
	 */
	public boolean wasChangedByRefresh() {
		return changedByRefresh;
	}

	/**
	 * @param changedByRefresh the changedByRefresh to set
	 */
	public void setChangedByRefresh(boolean changedByRefresh) {
		this.changedByRefresh = changedByRefresh;
	}

	/**
	 * Overrides the isCellEditable method to ensure no cells are editable.
	 * @param row	row of OverviewReqTable cell is located
	 * @param col	column of OverviewReqTable cell is located
	 * @return boolean */
	@Override
	public boolean isCellEditable(int row, int col)	{
		if (col == 0){
			return false;
		}
		else {
			return true;
		}
	}
	
	/**
	 * @return returns a list of the ids of the requirements 
	 * that have been selected to update in requirement manager
	 */
	public List<Integer> getSelectedRequirements(){
		return new ArrayList<Integer>();	
	}
}