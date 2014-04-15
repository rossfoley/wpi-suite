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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.util.List;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession.SessionState;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSessionModel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.ViewEventController;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.RequirementModel;

public class OverviewDetailPanel extends JPanel {
	private JPanel infoPanel;
	private JLabel lblSessionName;
	private JLabel lblEndDate;
	private DefaultListModel<Requirement> listModel;
	private JList<Requirement> requirementsList;
	private PlanningPokerSession.SessionState isOpen;
	private JButton btnOpen;
	private JButton btnVote;
	private JButton btnEndVote; 
	private JButton btnEdit;
	private PlanningPokerSession currentSession;
	
	public OverviewDetailPanel() {
		
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		infoPanel = new JPanel();
		infoPanel.setLayout(null);
		listModel = new DefaultListModel<Requirement>();
		requirementsList = new JList<Requirement>(listModel);
		lblSessionName = new JLabel("");
		lblEndDate = new JLabel("");
		btnOpen = new JButton("Open");
		btnVote = new JButton("Vote");
		btnEndVote = new JButton("End Voting"); 
		btnEdit = new JButton("Edit Session");
		JLabel lblSessionNameLabel = new JLabel("Session Name:");
		JLabel lblEndDateLabel = new JLabel("End Date:");
		JLabel lblRequirementsLabel = new JLabel("Requirements:");
		JScrollPane listContainer = new JScrollPane(requirementsList);
		
		lblSessionNameLabel.setBounds(10, 10, 258, 14);
		lblEndDateLabel.setBounds(10, 60, 258, 14);
		lblRequirementsLabel.setBounds(10, 110, 258, 14);
		listContainer.setBounds(10, 135, 258, 107);
		lblSessionName.setBounds(10, 35, 258, 14);
		lblEndDate.setBounds(10, 85, 258, 14);
		btnVote.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		btnVote.setAlignmentX(Component.RIGHT_ALIGNMENT);
		btnOpen.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		btnOpen.setAlignmentX(Component.RIGHT_ALIGNMENT);
		btnEndVote.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		btnEndVote.setAlignmentX(Component.RIGHT_ALIGNMENT);
		btnEdit.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		btnEdit.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		// Buttons are visible by default
		btnVote.setVisible(true);
		btnOpen.setVisible(true);
		btnEdit.setVisible(true);
		
		// create button action listeners
		btnVote.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			// TODO bro
			}
		});

		btnOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getCurrentSession().setSessionState(SessionState.OPEN);
				PlanningPokerSessionModel.getInstance().updatePlanningPokerSession(getCurrentSession());
				ViewEventController.getInstance().refreshTable();
			}
		});
		
<<<<<<< HEAD
		btnEndVote.addActionListener(new ActionListener() {
=======
		btnOpen.addActionListener(new ActionListener() {
>>>>>>> added end voting button
			public void actionPerformed(ActionEvent e) {
				getCurrentSession().setSessionState(SessionState.VOTINGENDED); 
				PlanningPokerSessionModel.getInstance().updatePlanningPokerSession(getCurrentSession());
				ViewEventController.getInstance().refreshTable();
			}
		}); 
		
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Have the event controller open a new edit session tab
				ViewEventController.getInstance().editSession(getCurrentSession());
			}
		});
		
		/* JLabel lblDeckName = new JLabel("Deck Name:");
		lblDeckName.setBounds(10, 110, 258, 14);
		infoPanel.add(lblDeckName);
		
		JLabel lblCreatorName = new JLabel("Session Creator Name:");
		lblCreatorName.setBounds(10, 160, 258, 14);
		infoPanel.add(lblCreatorName); */
		
		add(infoPanel);
		infoPanel.add(lblRequirementsLabel); 
		infoPanel.add(listContainer);
		infoPanel.add(lblSessionName);
		infoPanel.add(lblEndDate);
		infoPanel.add(lblSessionNameLabel);
		infoPanel.add(lblEndDateLabel);
		add(btnVote);
		add(btnOpen);
		add(btnEndVote); 
		add(btnEdit);
			
	}
	
	/**
	 * Displays the given session in the detail panel
	 * @param session The given session to display
	 * @author randyacheson
	 */
	public void updatePanel(final PlanningPokerSession session)
	{
		this.currentSession = session;
		String endDate = "No end date";
		Set<Integer> requirements = session.getRequirementIDs();
		
		// Change name
		this.lblSessionName.setText(session.getName());
		
		// Change requirements list
		this.listModel.clear();
		if (session.requirementsGetSize() > 0) 
		{
			for (Integer id : requirements) {
				Requirement requirement = RequirementModel.getInstance().getRequirement(id);
				if (requirement != null) {
					this.listModel.addElement(requirement);
				}
			}
		}
		
		requirementsList = new JList<Requirement>(listModel);

		// Change end date
		try {
			endDate = DateFormat.getDateInstance(DateFormat.MEDIUM).format(session.getEndDate().getTime());
		} catch (NullPointerException ex) {
			endDate = new String("No end date");
		}
		
		this.lblEndDate.setText(endDate);

		// change the visibility of the top buttons
		setButtonVisibility(session);
		

		btnVote.setVisible(false);
		btnOpen.setVisible(false);
		
		
		btnVote.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			// TODO bro
			}
		});

		btnOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				session.setSessionState(SessionState.OPEN);
				PlanningPokerSessionModel.getInstance().updatePlanningPokerSession(getCurrentSession());
				ViewEventController.getInstance().refreshTable();
			}
		});
		
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Have the event controller open a new edit session tab
				ViewEventController.getInstance().editSession(getCurrentSession());
			}
		});
		
		// Check if the buttons should appear
/*
		btnEdit.isVisible(false);
		if (session.isEditable()) {
			btnEdit.isVisible(true);
		}
*/

		// redraw panel
		infoPanel.revalidate();
		infoPanel.repaint();
	}	
	
	private void setButtonVisibility(PlanningPokerSession session) {
		// Check if the buttons should appear
		/*
			btnEdit.isVisible(false);
			if (session.isEditable()) {
				btnEdit.isVisible(true);
			}
		*/
	}


	
	
	public PlanningPokerSession getCurrentSession() {
		
		return this.currentSession;
	}
}
