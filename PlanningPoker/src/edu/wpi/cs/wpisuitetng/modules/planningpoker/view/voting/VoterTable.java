/*******************************************************************************
 * Copyright (c) 2014 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Team8s
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.voting;

import java.awt.Component;
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

import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.ViewEventController;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;

public class VoterTable extends JTable {
	private DefaultTableModel tableModel;
	private final Border paddingBorder = BorderFactory.createEmptyBorder(0, 0, 0, 0);
	private final PlanningPokerSession planningPokerSession;
	private final Requirement requirement;
	
	/**
	 * Sets initial table view
	 * @param data	Initial data to fill OverviewReqTable
	 * @param columnNames	Column headers of OverviewReqTable
	 */
	public VoterTable(Object[][] data, String[] columnNames, PlanningPokerSession pps, Requirement r) {
		planningPokerSession = pps;
		requirement = r;
		tableModel = new DefaultTableModel(data, columnNames);
		
		setModel(tableModel);
		setDefaultRenderer(Object.class, new DefaultTableCellRenderer());
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setDragEnabled(true);
        setDropMode(DropMode.ON);
		getTableHeader().setReorderingAllowed(false);
		setAutoCreateRowSorter(true);
		setFillsViewportHeight(true);
		
	}

	public void populateVotePanel() {
		tableModel.setRowCount(0);
		
		if (requirement != null) {
			Set<String> voters = planningPokerSession.getVotersForRequirement(requirement.getId());
			
			for (String username : voters) {
				tableModel.addRow(new Object[] {username});	
			}
		}
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
        final Component comp = super.prepareRenderer(renderer, row, column);

        if (JComponent.class.isInstance(comp)) {
            ((JComponent)comp).setBorder(paddingBorder);
        }
		return comp;

    }
	
	/**
	 * Overrides the isCellEditable method to ensure no cells are editable.
	 * @param row	row of OverviewReqTable cell is located
	 * @param col	column of OverviewReqTable cell is located
	 * @return boolean */
	@Override
	public boolean isCellEditable(int row, int col)	{
		return false;
	}
}
