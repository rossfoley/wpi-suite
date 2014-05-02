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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DropMode;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
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
	private PlanningPokerSession currentSession;
	private transient Vector<SelectedRequirementListener> listeners;
	
	/**
	 * Sets initial table view
	 * @param data	Initial data to fill StatisticsReqTable
	 * @param columnNames	Column headers of OverviewReqTable
	 */
	public StatisticsReqTable(Object[][] data, String[] columnNames, PlanningPokerSession aSession) {
		currentSession = aSession; 
		
		tableModel = new DefaultTableModel(data, columnNames);
		setModel(tableModel);
		setDefaultRenderer(Object.class, new DefaultTableCellRenderer());
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setDropMode(DropMode.ON);
    
		getTableHeader().setReorderingAllowed(false);
		setAutoCreateRowSorter(true);
		setFillsViewportHeight(true);
		initialized = false;
		
		/* Create double-click event listener */
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int rowNumber = rowAtPoint(getMousePosition());
				// If the row exists
				if (tableRows.containsKey(rowNumber)) {
					fireSelectedRequirementEvent(tableRows.get(rowNumber));
				}
			}
		});
	}
	
	/**
	 * updates StatistcsReqTable with the contents of the requirement model
	 */
	public void refresh(PlanningPokerSession session) {
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
		super.editingStopped(e);
		boolean isInteger = true;
		int numberEst = -1;
		try {
			numberEst = Integer.parseInt(((TableCellEditor) e.getSource()).getCellEditorValue().toString());
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
			}
			if (isInteger){
				if (numberEst >= 0){
					int reqID = tableRows.get(i);
					currentSession.addFinalEstimate(reqID, numberEst);
				}
			}
		}
		PlanningPokerSessionModel.getInstance().updatePlanningPokerSession(currentSession);
		refresh(currentSession);
	}

	
	/**
	 * Adds a listener for SelectedRequirementEvents
	 */
	synchronized public void addSelectedRequirementListener(SelectedRequirementListener l) {
		if (listeners == null) {
			listeners = new Vector<SelectedRequirementListener>();
		}
		listeners.addElement(l);
	}  

	/**
	 * Remove a listener for SelectedRequirementEvents
	 */
	synchronized public void removeSelectedRequirementListener(SelectedRequirementListener l) {
		if (listeners == null) {
			listeners = new Vector<SelectedRequirementListener>();
		}
		else {
			listeners.removeElement(l);
		}
	}

	/**
	 * Fire an EstimateEvent to all registered listeners
	 */
	protected void fireSelectedRequirementEvent(int requirementID) {
		// Do nothing if we have no listeners
		if (listeners != null && !listeners.isEmpty()) {
			// Create the event object to send
			final SelectedRequirementEvent event = 
					new SelectedRequirementEvent(this, requirementID);

			// Make a copy of the listener list in case anyone adds/removes listeners
			final Vector<SelectedRequirementListener> targets;
			synchronized (this) {
				targets = (Vector<SelectedRequirementListener>) listeners.clone();
			}

			// Walk through the listener list and call the estimateSubmitted method in each
			final Enumeration<SelectedRequirementListener> e = targets.elements();
			while (e.hasMoreElements()) {
				SelectedRequirementListener l = e.nextElement();
				l.setSelectedRequirement(event);
			}
		}
	}
	
}
