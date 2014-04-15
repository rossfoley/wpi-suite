package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.overview;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSessionModel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession.SessionState;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.ViewEventController;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;

public class OverviewEndVotePanel extends JPanel {
	private JPanel infoPanel;
	private JButton btnSubmit;
	private JButton btnCancel;
	
	public OverviewEndVotePanel() {
		
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		infoPanel = new JPanel();
		infoPanel.setLayout(null);
		btnSubmit = new JButton("Submit");
		btnCancel = new JButton("Cancel");

		JLabel lblEstimates = new JLabel("Enter The Estimates:");
		
		lblEstimates.setBounds(10, 10, 258, 14);

		btnSubmit.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		btnSubmit.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnCancel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		btnCancel.setAlignmentX(Component.RIGHT_ALIGNMENT);
		// Buttons are visible by default
		btnSubmit.setVisible(true);
		btnCancel.setVisible(true);

		// create button action listeners
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//here it should be implemented
			ViewEventController.getInstance().getMainView().setRightComponentToDetailPanel();
			}
		});
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			ViewEventController.getInstance().getMainView().setRightComponentToDetailPanel();
			}
		});
		add(infoPanel);
		infoPanel.add(lblEstimates); 

		add(btnSubmit);
		add(btnCancel);
			
	}
}
