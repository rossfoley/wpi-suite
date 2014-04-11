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

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.ViewEventController;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;

public class OverviewDetailPanel extends JPanel {
	private JPanel infoPanel;
	private JLabel lblSessionName;
	private JLabel lblEndDate;
	private DefaultListModel<Requirement> listModel;
	private JList<Requirement> requirementsList;
	private boolean isOpen;
	private JButton btnOpen;
	private JButton btnVote;
	private JButton editButton;
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
		JLabel lblSessionNameLabel = new JLabel("Session Name:");
		JLabel lblEndDateLabel = new JLabel("End Date:");
		JLabel lblRequirementsLabel = new JLabel("Requirements:");
		JScrollPane listContainer = new JScrollPane(requirementsList);
		
		lblSessionNameLabel.setBounds(10, 10, 258, 14);
		lblEndDateLabel.setBounds(10, 60, 258, 14);
		lblRequirementsLabel.setBounds(10, 110, 258, 14);
		listContainer.setBounds(10, 135, 258, 107);
		this.lblSessionName.setBounds(10, 35, 258, 14);
		lblEndDate.setBounds(10, 85, 258, 14);
		
		
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

		if (this.isOpen) {
		btnVote.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		btnVote.setAlignmentX(Component.RIGHT_ALIGNMENT);
		add(btnVote);
		}
		else {
			btnOpen.setAlignmentY(Component.BOTTOM_ALIGNMENT);
			btnOpen.setAlignmentX(Component.RIGHT_ALIGNMENT);
			add(btnOpen);
		}
		
		editButton = new JButton("Edit Session");
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
		List<Requirement> requirements = session.getRequirements();
		
		// Change name
		this.lblSessionName.setText(session.getName());
		
		// Change requirements list
		this.listModel.clear();
		if (session.requirementsGetSize() > 0) 
		{
			for (Requirement requirement : requirements) {
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
		
		//System.out.println(endDate);
		this.lblEndDate.setText(endDate);

		btnVote.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				session.setOpen(true);
			}
		});
		
		editButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Have the event controller open a new edit session tab
				ViewEventController.getInstance().editSession(getCurrentSession());
			}
		});
		editButton.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		editButton.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		// Check if the edit session button should appear
		remove(editButton);
		if (session.isEditable()) {
			add(editButton);
		}

		// redraw panel
		infoPanel.revalidate();
		infoPanel.repaint();
		
	}
	
	
	public PlanningPokerSession getCurrentSession() {
		
		return this.currentSession;
	}
}
