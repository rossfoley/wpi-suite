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
	JPanel infoPanel;
	JLabel lblSessionName;
	JLabel lblEndDate;
	DefaultListModel<Requirement> listModel;
	JList<Requirement> requirementsList;
	JButton editButton;
	
	
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
		
		/* JLabel lblDeckName = new JLabel("Deck Name:");
		lblDeckName.setBounds(10, 110, 258, 14);
		infoPanel.add(lblDeckName);
		
		JLabel lblCreatorName = new JLabel("Session Creator Name:");
		lblCreatorName.setBounds(10, 160, 258, 14);
		infoPanel.add(lblCreatorName); */
		
		JLabel lblRequirementsLabel = new JLabel("Requirements:");
		//lblRequirementsLabel.setBounds(10, 210, 258, 14);
		lblRequirementsLabel.setBounds(10, 110, 258, 14);
		infoPanel.add(lblRequirementsLabel); 
		
		listModel = new DefaultListModel<Requirement>();
		requirementsList = new JList<Requirement>(listModel);
		JScrollPane listContainer = new JScrollPane(requirementsList);
		//listContainer.setBounds(10, 235, 258, 107);
		listContainer.setBounds(10, 135, 258, 107);
		infoPanel.add(listContainer);

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
		
		editButton = new JButton("Edit Session");
		editButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Have the event controller open a new edit session tab
				ViewEventController.getInstance().editSelectedSession();
			}
		});
		editButton.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		editButton.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(editButton);
		
		
	}
	
	/**
	 * Displays the given session in the detail panel
	 * @param session The given session to display
	 * @author randyacheson
	 */
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
		
		// Check if the edit session button should appear
		remove(editButton);
		if (session.isEditable()) {
			add(editButton);
		}

		// redraw panel
		infoPanel.revalidate();
		infoPanel.repaint();
		
	}

}
