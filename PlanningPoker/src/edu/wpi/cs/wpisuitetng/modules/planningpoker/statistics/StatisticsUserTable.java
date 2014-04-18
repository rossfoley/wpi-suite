/*******************************************************************************
 * Copyright (c) 2014 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Team8s
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.planningpoker.statistics;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
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

import edu.wpi.cs.wpisuitetng.modules.planningpoker.controller.GetSessionController;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSessionModel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.ViewEventController;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.controller.GetRequirementsController;

/**
 * @author rossfoley
 */
public class StatisticsUserTable extends JTable
{
	private DefaultTableModel tableModel = null;
	private boolean initialized;
	private boolean changedByRefresh = false;
	private Border paddingBorder = BorderFactory.createEmptyBorder(0, 4, 0, 0);
	private OverviewDetailPanel detailPanel;
	
	/**
	 * Sets initial table view
	 * @param data	Initial data to fill OverviewTable
	 * @param columnNames	Column headers of OverviewTable
	 */
	public OverviewTable(Object[][] data, String[] columnNames)
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

		//ViewEventController.getInstance().setOverviewTable(this);
		initialized = false;

		/* Create double-click event listener */
		this.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				
				if(getRowCount() > 0)
				{
					int mouseY = e.getY();
					Rectangle lastRow = getCellRect(getRowCount() - 1, 0, true);
					int lastRowY = lastRow.y + lastRow.height;

					if(mouseY > lastRowY) 
					{
						getSelectionModel().clearSelection();
						repaint();
					}

					// Open detail overview panel
					//displaySession();
				}
				
			}
		});
		
		 System.out.println("finished constructing the table");
	}
	/*
	protected void displaySession() {
		ViewEventController.getInstance().displayDetailedSession(this);
	}
*/
	/**
	 * Retrieves the list of sessions
	 */
	
	public List<PlanningPokerSession> getSessions() {
		return PlanningPokerSessionModel.getInstance().getPlanningPokerSessions();
	}
	
	/**
	 * Retrieves all open sessions
	 * @author randyacheson
	 */

	public List<PlanningPokerSession> getOpenSessions() {
		List<PlanningPokerSession> sessions = PlanningPokerSessionModel.getInstance().getPlanningPokerSessions();
		List<PlanningPokerSession> openSessions = new ArrayList<PlanningPokerSession>();
		for (PlanningPokerSession session : sessions) {
			if (session.isOpen()){
				openSessions.add(session);
			}
		}
		return openSessions;
	}
	
	
	/**
	 * updates OverviewTable with the contents of the requirement model
	 */
	public void refresh() {
				
		List<PlanningPokerSession> pokerSessions = getSessions();
				
		// clear the table
		tableModel.setRowCount(0);		

		for (PlanningPokerSession pokerSession : pokerSessions) {
			String endDate, createrName, deckName;
			// Handle if there was no end date set
			try {
				endDate = DateFormat.getDateInstance(DateFormat.MEDIUM).format(pokerSession.getEndDate().getTime());
			} catch (NullPointerException ex) {
				endDate = new String("No end date");
			}
			
			// Handle if there was no owner
			try {
				createrName = pokerSession.getSessionCreatorName();
			} catch (NullPointerException ex) {
				createrName = new String("Creater not set");
			}
			
			// Handle if there was no deck set
			try {
				deckName = pokerSession.getSessionDeck().getDeckName();
			} catch (NullPointerException ex) {
				deckName = new String("None");
			}

			tableModel.addRow(new Object[]{
					pokerSession.getID(),
					pokerSession.getName(),
					endDate,
					Integer.toString(pokerSession.requirementsGetSize()), 
					deckName,
					createrName});	
		}
		// indicate that refresh is no longer affecting the table
		setChangedByRefresh(false);
		
		System.out.println("finished refreshing the table");		
	}
	
	/**
	
	 * @return the changedByRefresh */
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
	 * 
	 * @param g	The component object to paint
	 */
	@Override
	public void paintComponent(Graphics g)
	{
		if(!initialized) {
			try {
				GetSessionController.getInstance().retrieveSessions();
				GetRequirementsController.getInstance().retrieveRequirements();
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

        if (JComponent.class.isInstance(comp)){
            ((JComponent)comp).setBorder(paddingBorder);
        }
		return comp;

    }
	
	
	/**
	 * Overrides the isCellEditable method to ensure no cells are editable.
	 * 
	 * @param row	row of OverviewTable cell is located
	 * @param col	column of OverviewTable cell is located
	
	 * @return boolean */
	@Override
	public boolean isCellEditable(int row, int col)
	{
		return false;
	}
	
	public OverviewDetailPanel getDetailPanel() {
		return this.detailPanel;
	}
}

