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
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.DropMode;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.controller.GetSessionController;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSessionModel;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.RequirementModel;

public class OverviewReqTable extends JTable {
	private DefaultTableModel tableModel = null;
	private boolean initialized;
	private boolean changedByRefresh = false;
	private Border paddingBorder = BorderFactory.createEmptyBorder(0, 0, 0, 0);
	
	/**
	 * Sets initial table view
	 * @param data	Initial data to fill OverviewReqTable
	 * @param columnNames	Column headers of OverviewReqTable
	 */
	public OverviewReqTable(Object[][] data, String[] columnNames)
	{
		this.tableModel = new DefaultTableModel(data, columnNames);
		this.setModel(tableModel);
		this.setDefaultRenderer(Object.class, new DefaultTableCellRenderer());
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.setDragEnabled(true);
        this.setDropMode(DropMode.ON);
    
		this.getTableHeader().setReorderingAllowed(false);
		this.setAutoCreateRowSorter(true);
		setFillsViewportHeight(true);

		//ViewEventController.getInstance().setOverviewReqTable(this);
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
	 * updates OverviewReqTable with the contents of the requirement model
	 */
	public void refresh(PlanningPokerSession session) {
		// TODO Implement Estimate column
		// Currently is 0 for every estimate
		
		Set<Integer> requirementIDs = session.getRequirementIDs();
		RequirementModel reqs = RequirementModel.getInstance();
		int estimate = 0;
		//List<Estimate> estimates = session.getEstimates();
				
		// clear the table
		tableModel.setRowCount(0);		

		for (Integer requirementID : requirementIDs) {
			Requirement req = reqs.getRequirement(requirementID);

			tableModel.addRow(new Object[]{
					req.getName(),
					estimate});	
		}
		// indicate that refresh is no longer affecting the table
		setChangedByRefresh(false);
		
		System.out.println("finished refreshing the table");		
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
	 * Overrides the paintComponent method to retrieve the requirements on the first painting.
	 * @param g	The component object to paint
	 */
	@Override
	public void paintComponent(Graphics g)
	{
		if(!initialized) {
			try {
				GetSessionController.getInstance().retrieveSessions();
				initialized = true;
			} catch (Exception e) {}
		}

		super.paintComponent(g);
	}
	

	
	/**
	 * Method prepareRenderer.
	 * @param renderer TableCellRenderer
	 * @param row int
	 * @param column int
	 * @return Component
	 */
	@Override
    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
        Component comp = super.prepareRenderer(renderer, row, column);

        if (JComponent.class.isInstance(comp)) {
            ((JComponent)comp).setBorder(paddingBorder);
        }
		return comp;

    }
	
	/**
	 * Overrides the isCellEditable method to ensure no cells are editable.
	 * 
	 * @param row	row of OverviewReqTable cell is located
	 * @param col	column of OverviewReqTable cell is located
	
	 * @return boolean */
	@Override
	public boolean isCellEditable(int row, int col)	{
		return false;
	}
}
