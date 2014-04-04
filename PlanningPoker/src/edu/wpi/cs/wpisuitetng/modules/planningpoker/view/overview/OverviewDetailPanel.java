package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.overview;

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
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class OverviewDetailPanel extends JPanel {
	public OverviewDetailPanel(PlanningPokerSession session) {
		
		String endDate = "";
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		JPanel infoPanel = new JPanel();
		add(infoPanel);
		infoPanel.setLayout(null);
		
		JLabel lblSessionName = new JLabel("Session Name:");
		lblSessionName.setBounds(10, 10, 258, 14);
		infoPanel.add(lblSessionName);
		
		JLabel lblEndDate = new JLabel("End Date:");
		lblEndDate.setBounds(10, 60, 258, 14);
		infoPanel.add(lblEndDate);
		
		JLabel lblRequirements = new JLabel("Requirements:");
		lblRequirements.setBounds(10, 110, 258, 14);
		infoPanel.add(lblRequirements);
		
		JList list = new JList();
		list.setBounds(10, 135, 258, 107);
		infoPanel.add(list);

		JLabel lblSessionNameLabel = new JLabel(session.getName());
		lblSessionNameLabel.setBounds(10, 35, 258, 14);
		infoPanel.add(lblSessionNameLabel);
		
		/*try {
			endDate = DateFormat.getDateInstance(DateFormat.MEDIUM).format(session.getEndDate().getTime());
		} catch (NullPointerException ex) {
			endDate = new String("No end date");
		}*/
		
		JLabel lblEndDateLabel = new JLabel(endDate);
		lblEndDateLabel.setBounds(10, 85, 258, 14);
		infoPanel.add(lblEndDateLabel);
		
		JButton btnVote = new JButton("Vote");
		btnVote.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnVote.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		btnVote.setAlignmentX(Component.RIGHT_ALIGNMENT);
		add(btnVote);
		
		
	}
}
