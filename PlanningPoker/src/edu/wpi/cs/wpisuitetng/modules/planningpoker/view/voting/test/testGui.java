package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.voting.test;

import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SpringLayout;
import javax.swing.JPanel;

import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;

public class testGui {
	private static final JPanel panel = new JPanel();
	
	/**
	 * @wbp.parser.entryPoint
	 */
	public static void main(String[] args){
		
		JFrame main = new JFrame();
		SpringLayout springLayout = new SpringLayout();
		springLayout.putConstraint(SpringLayout.NORTH, panel, 0, SpringLayout.NORTH, main.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, panel, 0, SpringLayout.WEST, main.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, panel, 0, SpringLayout.SOUTH, main.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, panel, 0, SpringLayout.EAST, main.getContentPane());
		main.getContentPane().setLayout(springLayout);
		main.getContentPane().add(panel);
		SpringLayout sl_panel = new SpringLayout();
		panel.setLayout(sl_panel);
		
		List<Requirement> requirements = new LinkedList<Requirement>();
		
		Requirement requirement0 = new Requirement();
		requirement0.setId(0);;
		requirement0.setName("Requirement 0");
		requirement0.setDescription("Test");
		requirements.add(requirement0);
		
		Requirement requirement1 = new Requirement();
		requirement1.setId(1);;
		requirement1.setName("Requirement 1");
		requirement1.setDescription("Test 1");
		requirements.add(requirement1);
		
		Requirement requirement2 = new Requirement();
		requirement2.setId(2);;
		requirement2.setName("Requirement 2");
		requirement2.setDescription("Test 2");
		requirements.add(requirement2);
		
		LinkedList<Vote> votes = new LinkedList<Vote>();
		
		Vote vote0 = new Vote(1, "will", 3);
		votes.add(vote0);
		
		
		VotingPanel votingPanel = new VotingPanel(requirements, votes, "will");
		sl_panel.putConstraint(SpringLayout.NORTH, votingPanel, 0, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.WEST, votingPanel, 0, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.SOUTH, votingPanel, 262, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.EAST, votingPanel, 434, SpringLayout.WEST, panel);
		panel.add(votingPanel);
		
		main.setVisible(true);
		main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
