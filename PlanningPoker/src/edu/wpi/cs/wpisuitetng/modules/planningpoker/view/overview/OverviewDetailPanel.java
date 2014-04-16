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


import java.awt.Dimension;
import javax.swing.JSplitPane;
import javax.swing.JScrollPane;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;

public class OverviewDetailPanel extends JSplitPane {
	PlanningPokerSession currentSession;
	OverviewDetailInfoPanel infoPanel;
	OverviewReqTable reqTable;
	JScrollPane tablePanel;

	public OverviewDetailPanel () {

		this.setOrientation(JSplitPane.VERTICAL_SPLIT);
		
		String[] columnNames = {"Requirement Name", "Your Vote", "Final Estimate"};
		Object[][] data = {};
		
		// Create the info panel and table panel
		infoPanel = new OverviewDetailInfoPanel();
		reqTable = new OverviewReqTable(data, columnNames);
		tablePanel = new JScrollPane(reqTable);
		
		reqTable.getColumnModel().getColumn(0).setMinWidth(200); // Requirement Name
		reqTable.getColumnModel().getColumn(1).setMinWidth(100); // User Vote
		reqTable.getColumnModel().getColumn(1).setMaxWidth(100); // User Vote
		reqTable.getColumnModel().getColumn(2).setMinWidth(100); // Final Estimate
		reqTable.getColumnModel().getColumn(2).setMaxWidth(100); // Final Estimate
		
		// Put the info panel and table panel into the split pane
		this.setTopComponent(infoPanel);
		this.setBottomComponent(tablePanel);
		this.setResizeWeight(0.5); 

		ViewEventController.getInstance().setOverviewDetailInfoPanel(infoPanel);
		ViewEventController.getInstance().setOverviewReqTable(reqTable);
				getCurrentSession().setSessionState(SessionState.VOTINGENDED); 
				PlanningPokerSessionModel.getInstance().updatePlanningPokerSession(getCurrentSession());
				ViewEventController.getInstance().refreshTable();
			}
		});
		
		// Makes the split pane divide 50/50 for each portion
		Dimension d = new Dimension(200, 200);
        infoPanel.setMinimumSize(d);
        infoPanel.setPreferredSize(d);
        tablePanel.setMinimumSize(d);
        
        // Disable the split pane from being movable
        // Why that's the method name I have no idea
        this.setEnabled(false);
			}
		});

				
	}
	
	public void updatePanel(final PlanningPokerSession session)	{

		this.currentSession = session;
		
		// update each part of the split panel
		updateInfoPanel(session);
		updateReqTable(session);
		
		// change the visibility of the top buttons
		setButtonVisibility(session);
	}	
	
	private void updateInfoPanel(PlanningPokerSession session) {
		infoPanel.refresh(session);
	}

	private void updateReqTable(PlanningPokerSession session) {
		reqTable.refresh(session);
	}

	private void setButtonVisibility(PlanningPokerSession session) {
		// TODO
		// Check if the buttons should appear
		/*
			btnEdit.isVisible(false);
			if (session.isEditable()) {
				btnEdit.isVisible(true);
			}
		*/
	}
	
	/**
	 * 
	 * @return endButton
	 */
	public JButton getEndVoteButton() {
		return this.btnEndVote;
	}
	
	public PlanningPokerSession getCurrentSession() {
		
		return this.currentSession;
	}
}
