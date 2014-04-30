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
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.DropMode;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.RowSorterEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import edu.wpi.cs.wpisuitetng.janeway.config.ConfigManager;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.controller.GetSessionController;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.Estimate;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSessionModel;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.RequirementModel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.ViewEventController;


/**
 * The table containing all requirements for the given session 
 * that is being displayed in the overview detail panel
 * 
 * Bottom half of the overviewDetailPanel split pane
 */
public class StatisticsReqTable extends JTable {
	private DefaultTableModel tableModel = null;
	private boolean initialized;
	private boolean changedByRefresh = false;
	private final Border paddingBorder = BorderFactory.createEmptyBorder(0, 0, 0, 0);
	private final HashMap<Integer, Integer> tableRows = new HashMap<Integer, Integer>();
	private int rowNumber = 0;
	private StatisticsDetailPanel detailPanel;
	private StatisticsInfoPanel infoPanel;
	private PlanningPokerSession currentSession; 
	
	/**
	 * Sets initial table view
	 * @param data	Initial data to fill StatisticsReqTable
	 * @param columnNames	Column headers of OverviewReqTable
	 */
	public StatisticsReqTable(Object[][] data, String[] columnNames, PlanningPokerSession aSession) {
		currentSession = aSession; 
		
		tableModel = new DefaultTableModel(data, columnNames);
		this.setModel(tableModel);
		this.setDefaultRenderer(Object.class, new DefaultTableCellRenderer());
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.setDragEnabled(true);
        this.setDropMode(DropMode.ON);
    
		this.getTableHeader().setReorderingAllowed(false);
		this.setAutoCreateRowSorter(true);
		setFillsViewportHeight(true);
		initialized = false;
		
		/* Create double-click event listener */
		this.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				try {
					rowNumber = convertRowIndexToModel(rowAtPoint(getMousePosition()));
					//System.out.println("rowNumber = " + rowNumber);
					//infoPanel.currentReqID = tableRows.get(rowNumber);
					try {
						infoPanel.setCurrentReqID(tableRows.get(rowNumber));
					}
					catch (NullPointerException ex) {}
				}
				catch (IndexOutOfBoundsException obe) {
					//System.out.println("There is no row at current mouse position");
				}
			}
		});
	}
	
	/**
	 * updates StatistcsReqTable with the contents of the requirement model
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
			int rowID = tableModel.getRowCount();
			vote = 0;
			for (Estimate e : estimates) {
				if (e.getRequirementID() == requirementID && e.getOwnerName().equals(ConfigManager.getConfig().getUserName())) {
					vote = e.getVote();
				}
			}
			
			estimate = getFinalEstimate(req, session);

			tableModel.addRow(new Object[]{
					reqName,
					vote,
					estimate});
			
			tableRows.put(rowID, requirementID);
		}
		// indicate that refresh is no longer affecting the table
		setChangedByRefresh(false);
	}
	
	@Override 
	public boolean isCellEditable(int row, int col) {
		return col == 2;
	}
	
	@Override 
	public void editingStopped(ChangeEvent e) {
		//System.out.println("Editing Stopped");
		String estimate = (String) tableModel.getValueAt(editingRow, editingColumn);
		//System.out.println("Estimate = " + estimate + ", editingRow = " + editingRow + ", editingColumn = " + editingColumn);
		super.editingStopped(e);
		boolean isInteger = true;
		int numberEst = -1;
		try {
			numberEst = Integer.parseInt(((TableCellEditor) e.getSource()).getCellEditorValue().toString());
			//System.out.println("Edited value = " + numberEst);
		}
		catch (NumberFormatException ne) {
			isInteger = false;
			// add error message for nonnegative integers only
		}
		super.editingStopped(e);
		if (isInteger) {
			if (numberEst >= 0) {
				int reqID = tableRows.get(editingRow + 1);
				currentSession.addFinalEstimate(reqID, numberEst);
			}
		}
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
	
	
	private String getFinalEstimate(Requirement reqToFind, PlanningPokerSession session){
		HashMap<Integer, Integer> finalEstimates = session.getFinalEstimates();
		int reqID = reqToFind.getId();
		if (finalEstimates.containsKey(reqID)){
			Integer est = finalEstimates.get(reqID);
			return est.toString();
		}
		return "-";		
	}

	public int getSelectedReq() {
		return tableRows.get(rowNumber);
	}
	public void setInfoPanel(StatisticsInfoPanel panel) {
		infoPanel = panel;
	}
	
	public void setSession(PlanningPokerSession aSession) {
		currentSession = aSession; 
	}
	
	/**
	 * update the current session with the newly entered final estimates
	 * @param currentSession
	 */
	public void updateFinalEstimates(PlanningPokerSession currentSession){
		for (int i = 0; i<tableModel.getRowCount(); i++){
			String estimate = (String) tableModel.getValueAt(i, 2);
			boolean isInteger = true;
			int numberEst = -1;
			try {
				numberEst = Integer.parseInt(estimate);
			}
			catch (NumberFormatException e) {
				isInteger = false;
				// add error message for nonnegative integers only
			}
			if (isInteger){
				if (numberEst >= 0){
					int reqID = tableRows.get(i);
					currentSession.addFinalEstimate(reqID, numberEst);
				}
				/* else {
					add error message for nonnegative integers only
				} */
			}
		}
		PlanningPokerSessionModel.getInstance().updatePlanningPokerSession(currentSession);
		refresh(currentSession);
	}
}
