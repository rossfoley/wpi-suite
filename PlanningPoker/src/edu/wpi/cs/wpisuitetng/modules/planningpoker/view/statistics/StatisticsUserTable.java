/*******************************************************************************
 * Copyright (c) 2014 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Team8s
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.statistics;

import java.awt.Component;
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

import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.Estimate;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;

/**
 * The table containing all the votes for the input requirement and session.
 * A vote consists of the voter name and their estimate
 *
 */
public class StatisticsUserTable extends JTable {
	private DefaultTableModel tableModel;
	private final Border paddingBorder = BorderFactory.createEmptyBorder(0, 0, 0, 0);
	private PlanningPokerSession planningPokerSession;

	/**
	 * Sets initial table view
	 * @param data	Initial data to fill OverviewReqTable
	 * @param columnNames	Column headers of OverviewReqTable
	 */
	public StatisticsUserTable(Object[][] data, String[] columnNames, PlanningPokerSession pps) {
		planningPokerSession = pps;
		tableModel = new DefaultTableModel(data, columnNames);

		setModel(tableModel);
		setDefaultRenderer(Object.class, new DefaultTableCellRenderer());
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setDropMode(DropMode.ON);
		getTableHeader().setReorderingAllowed(false);
		setAutoCreateRowSorter(true);
		setFillsViewportHeight(true);
		
		populateVotePanel();
	}

	/**
	 * Populates the table with voter names and their votes
	 */
	private void populateVotePanel() {
		tableModel.setRowCount(0);

		Set<Integer> requirements= planningPokerSession.getRequirementIDs();
		List<Estimate> estimates = planningPokerSession.getEstimates();

		// Loop through all requirements in this session
		for (Integer req : requirements) {
			for (Estimate el : estimates) {
				// If the estimate is for this requirement
				if (el.getRequirementID() == req) {
					// Get the requirement name
					tableModel.addRow(new Object[] {el.getOwnerName(), el.getVote()});
				}
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

	/**
	 * Refreshes the table with the input session
	 * @param session	The session to update the panel to display
	 */
	public void refresh(PlanningPokerSession session) {
		planningPokerSession = session;
		populateVotePanel();
	}
}
