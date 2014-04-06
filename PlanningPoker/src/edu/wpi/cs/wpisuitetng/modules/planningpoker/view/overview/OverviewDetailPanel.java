package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.overview;

import javax.swing.DefaultListModel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.BoxLayout;
import javax.swing.JLabel;

import java.awt.Component;

import javax.swing.JButton;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.text.DateFormat;
import java.util.Collection;
import java.util.List;

import javax.swing.JTextArea;
import javax.swing.JList;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.SpringLayout;

public class OverviewDetailPanel extends JPanel {
	JPanel infoPanel;
	JLabel lblSessionName;
	JLabel lblEndDate;
	DefaultListModel<Requirement> listModel;
	JList<Requirement> requirementsList;
	
	
	public OverviewDetailPanel(PlanningPokerSession session) {
		
		
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS)); // oops?
		
		infoPanel = new JPanel();
		add(infoPanel);
		infoPanel.setLayout(null);
		
		JLabel lblSessionNameLabel = new JLabel("Session Name:");
		lblSessionNameLabel.setBounds(10, 10, 258, 14);
		infoPanel.add(lblSessionNameLabel);
		
		JLabel lblEndDateLabel = new JLabel("End Date:");
		lblEndDateLabel.setBounds(10, 60, 258, 14);
		infoPanel.add(lblEndDateLabel);
		
		JLabel lblRequirementsLabel = new JLabel("Requirements:");
		lblRequirementsLabel.setBounds(10, 110, 258, 14);
		infoPanel.add(lblRequirementsLabel);
		
		listModel = new DefaultListModel<Requirement>();
		requirementsList = new JList<Requirement>(listModel);
		requirementsList.setBounds(10, 135, 258, 107);
		infoPanel.add(requirementsList);

		this.lblSessionName = new JLabel("");
		this.lblSessionName.setBounds(10, 35, 258, 14);
		infoPanel.add(this.lblSessionName);
		
		lblEndDate = new JLabel("");
		lblEndDate.setBounds(10, 85, 258, 14);
		infoPanel.add(lblEndDate);
		
		JButton btnVote = new JButton("Vote");
		btnVote.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnVote.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		btnVote.setAlignmentX(Component.RIGHT_ALIGNMENT);
		add(btnVote);
		
		
	}
	
	public void updatePanel(PlanningPokerSession session)
	{
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

		// redraw panel
		infoPanel.revalidate();
		infoPanel.repaint();
		
	}

}
