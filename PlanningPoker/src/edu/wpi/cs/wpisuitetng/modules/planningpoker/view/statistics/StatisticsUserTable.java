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
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;

/**
 * The table containing all the votes for the input requirement and session.
 * A vote consists of the voter name and their estimate
 *
 */
public class StatisticsUserTable extends JTable {
	private DefaultTableModel tableModel;
	private final Border paddingBorder = BorderFactory.createEmptyBorder(0, 0, 0, 0);
	private PlanningPokerSession planningPokerSession;
	private Requirement requirement;

	/**
	 * Sets initial table view
	 * @param session	The planning poker session to display
	 * @param requirement	The requirement to display voter names and votes for
	 */
	public StatisticsUserTable(PlanningPokerSession session, Requirement requirement) {
		String[] columnNames = {"User", "Estimate"};
		Object[][] data = {};
		planningPokerSession = session;
		this.requirement = requirement;
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

		List<Estimate> estimates = planningPokerSession.getEstimates();

		// Loop through all estimates in this session
		for (Estimate el : estimates) {
			// If the estimate is for this requirement
			if (el.getRequirementID() == requirement.getId()) {
				// Get the requirement name
				tableModel.addRow(new Object[] {el.getOwnerName(), el.getVote()});
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
	 * Refreshes the table to display votes for the input requirement
	 * @param requirement	The requirement to display votes for
	 */
	public void updateRequirement(Requirement requirement) {
		this.requirement = requirement;
		populateVotePanel();
	}
}
