package edu.wpi.cs.wpisuitetng.modules.planningpoker.voting.test;

import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;

import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;

import javax.swing.SpringLayout;
import javax.swing.Box;

public class VotingPanel extends JPanel{
	
	private List<Requirement> requirements = new LinkedList<Requirement>();
	private List<Vote> votes = new LinkedList<Vote>();
	private String user;
	private Requirement selected;
	
	VotingPanel(List<Requirement> requirements, List<Vote> votes, String user){
		this.votes = votes;
		this.requirements = requirements;
		this.user = user;
		
		this.setName("Voting Panel");
		
		SpringLayout springLayout = new SpringLayout();
		setLayout(springLayout);
		
		JPanel requirementVoting = new JPanel();
		springLayout.putConstraint(SpringLayout.NORTH, requirementVoting, 10, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, requirementVoting, 194, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.SOUTH, requirementVoting, -10, SpringLayout.SOUTH, this);
		springLayout.putConstraint(SpringLayout.EAST, requirementVoting, -10, SpringLayout.EAST, this);
		add(requirementVoting);
		
		VotingManager votingManager = new VotingManager(this.requirements,this.votes, this.user);
		springLayout.putConstraint(SpringLayout.NORTH, votingManager, 10, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, votingManager, 10, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.SOUTH, votingManager, -10, SpringLayout.SOUTH, this);
		springLayout.putConstraint(SpringLayout.EAST, votingManager, -6, SpringLayout.WEST, requirementVoting);
		SpringLayout sl_requirementVoting = new SpringLayout();
		requirementVoting.setLayout(sl_requirementVoting);
		
		Box verticalBox = Box.createVerticalBox();
		sl_requirementVoting.putConstraint(SpringLayout.NORTH, verticalBox, 0, SpringLayout.NORTH, requirementVoting);
		sl_requirementVoting.putConstraint(SpringLayout.WEST, verticalBox, -282, SpringLayout.EAST, requirementVoting);
		sl_requirementVoting.putConstraint(SpringLayout.SOUTH, verticalBox, 0, SpringLayout.SOUTH, requirementVoting);
		sl_requirementVoting.putConstraint(SpringLayout.EAST, verticalBox, 0, SpringLayout.EAST, requirementVoting);
		requirementVoting.add(verticalBox);
		
		JPanel viewRequirement = new JPanel();
		verticalBox.add(viewRequirement);
		
		JPanel estimateRequirement = new JPanel();
		verticalBox.add(estimateRequirement);
		add(votingManager);
	}
	
	public void setSelected(Requirement rqt){
		this.selected = rqt;
	}
}
