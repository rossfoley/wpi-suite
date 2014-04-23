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
import java.util.HashMap;
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

import edu.wpi.cs.wpisuitetng.janeway.config.ConfigManager;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.controller.GetSessionController;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.Estimate;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.RequirementModel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.ViewEventController;


/**
 * The table containing all requirements for the given session 
 * that is being displayed in the overview detail panel
 * 
 * Bottom half of the overviewDetailPanel split pane
 * @author Randy Acheson
 * @version 4/18/14
 */
public class OverviewReqTable extends JTable {
	private DefaultTableModel tableModel = null;
	private boolean initialized;
	private boolean changedByRefresh = false;
	private final Border paddingBorder = BorderFactory.createEmptyBorder(0, 0, 0, 0);

	/**
	 * Sets initial table view
	 * @param data	Initial data to fill OverviewReqTable
	 * @param columnNames	Column headers of OverviewReqTable
	 */
	public OverviewReqTable(Object[][] data, String[] columnNames) {
		tableModel = new DefaultTableModel(data, columnNames);
		this.setModel(tableModel);
		this.setDefaultRenderer(Object.class, new DefaultTableCellRenderer());
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.setDragEnabled(true);
		this.setDropMode(DropMode.ON);

		ViewEventController.getInstance().setOverviewReqTable(this);

		this.getTableHeader().setReorderingAllowed(false);
		this.setAutoCreateRowSorter(true);
		setFillsViewportHeight(true);

		initialized = false;

		/* Create double-click event listener */
		this.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {

				if(getRowCount() > 0) {
					final int mouseY = e.getY();
					final Rectangle lastRow = getCellRect(getRowCount() - 1, 0, true);
					final int lastRowY = lastRow.y + lastRow.height;

					if(mouseY > lastRowY) {
						getSelectionModel().clearSelection();
						repaint();
					}
				}
			}
		});
	}

	/**
	 * Updates OverviewReqTable with the contents of the requirement model
	 * @param session The session to display the requirements of
	 */
	public void refresh(PlanningPokerSession session) {
		// TODO Implement Your Vote, Estimate columns
		// Currently is 0 for every estimate

		final Set<Integer> requirementIDs = session.getRequirementIDs();
		final RequirementModel reqs = RequirementModel.getInstance();
		int vote = 0;
		String estimate;
		final List<Estimate> estimates = session.getEstimates();

		// clear the table
		tableModel.setRowCount(0);

		for (Integer requirementID : requirementIDs) {
			Requirement req = reqs.getRequirement(requirementID);
			String reqName = req.getName();
			vote = 0;
			estimate = getFinalEstimate(req, session);
			for (Estimate e : estimates) {
				if (e.getRequirementID() == requirementID && e.getOwnerName()
						.equals(ConfigManager.getConfig().getUserName())) {
					vote = e.getVote();
				}
			}

			tableModel.addRow(new Object[]{
					reqName,
					vote,
					estimate});
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
	public boolean isCellEditable(int row, int col) {
		return false;
	}

	/** 
	 * @param reqToFind requirement to find the final estimate for
	 * @param session session to look for final estimates in
	 * @return a string representing the final estimate 
	 */
	private String getFinalEstimate(Requirement reqToFind, PlanningPokerSession session){
		HashMap<Integer, Integer> finalEstimates = session.getFinalEstimates();
		if (finalEstimates.containsKey(reqToFind.getId())){
			return finalEstimates.get(reqToFind).toString();
		}
		return "-";		
	}
}
