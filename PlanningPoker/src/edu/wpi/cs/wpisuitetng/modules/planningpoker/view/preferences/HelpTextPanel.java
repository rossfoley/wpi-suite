package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.preferences;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import java.awt.Font;
import javax.swing.JTextField;

public class HelpTextPanel extends JPanel{
	private JTextArea txtWhatIsPlanning;
	public HelpTextPanel() {
		
		txtWhatIsPlanning = new JTextArea();
		txtWhatIsPlanning.setBackground(UIManager.getColor("CheckBox.background"));
		txtWhatIsPlanning.setEditable(false);
		txtWhatIsPlanning.setFont(new Font("Tahoma", Font.BOLD, 16));
		txtWhatIsPlanning.setText("What is Planning Poker?");
		add(txtWhatIsPlanning);
		txtWhatIsPlanning.setColumns(20);
		
		JTextArea WhatIsPlanningPoker = new JTextArea();
		WhatIsPlanningPoker.setFont(new Font("Tahoma", Font.PLAIN, 14));
		WhatIsPlanningPoker.setBackground(UIManager.getColor("CheckBox.background"));
		WhatIsPlanningPoker.setColumns(70);
		WhatIsPlanningPoker.setEditable(false);
		WhatIsPlanningPoker.setText("Planning poker is a consensus-based estimation tool used by groups of individuals to determine the relative size of a task, or requirement, in an unbiased environment. Members of the group will estimate how many work units they think a particular task will take, but will not reveal their estimation until everyone has voted. In this way, members of the group are unbiased by others\u2019 estimations, and the project leader will be able to generalize how much work a task might take. \r\nBy remaining anonymous during the voting session, members of a group are not influenced by other members' votes, so they are required to make an honest vote which can be discussed after voting has completed.\r\n\r\nThe planning poker procedure is as follows;\r\nA member, usually the leader, of a group will make a planning poker session. Every member of the group will be given a notification that the session has started, and when its end date is, if it has one. At this time, each member will go to the session and vote on an estimation for each of the requirements. The session will end when either the end date has been met, or if the session creator manually ends the session. The session creator will then make a final estimate for each requirement voted on within the session based on data provided such as who voted and what their vote was on the session statistics page. This final estimate will then be sent to the requirement manager, where the project leader can assign that requirement to an iteration when necessary.\r\n");
		WhatIsPlanningPoker.setWrapStyleWord(true);
		WhatIsPlanningPoker.setLineWrap(true);
		add(WhatIsPlanningPoker);
	}

}
