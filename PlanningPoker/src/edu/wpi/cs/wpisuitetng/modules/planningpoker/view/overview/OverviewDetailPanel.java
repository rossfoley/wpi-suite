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
		
		String endDate = "";
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS)); // oops?
		
		JPanel infoPanel = new JPanel();
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
		
		DefaultListModel<Requirement> listModel = new DefaultListModel<Requirement>();
		JList<Requirement> requirementsList = new JList<Requirement>(listModel);
		requirementsList.setBounds(10, 135, 258, 107);
		infoPanel.add(requirementsList);

		this.lblSessionName = new JLabel(session.getName());
		this.lblSessionName.setBounds(10, 35, 258, 14);
		infoPanel.add(this.lblSessionName);
		
		JLabel lblEndDate = new JLabel(endDate);
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
		
		// Change name
		this.lblSessionName.setText(session.getName());
		
		// Change end date
		try {
			endDate = DateFormat.getDateInstance(DateFormat.MEDIUM).format(session.getEndDate().getTime());
		} catch (NullPointerException ex) {
			endDate = new String("No end date");
		}
		this.lblEndDate.setText(endDate);
		
		// Change requirements list
		for (int i = 0; i < session.requirementsGetSize(); i++) {
			this.listModel.addElement(session.getRequirements().get(i));
		}
		
		requirementsList = new JList<Requirement>(listModel);
		

		// redraw panel
		infoPanel.revalidate();
		infoPanel.repaint();
		
	}

}
