package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.voting.test;

import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;

import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;

import javax.swing.SpringLayout;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import java.awt.Container;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class VotingManager extends JPanel{
	
	private List<Requirement> requirements;
	private List<Vote> votes;
	private String user;
	
	private JTree tree = new JTree();;
	private DefaultMutableTreeNode rootNode;
	
	private LinkedList<Requirement> notVotedList;
	private LinkedList<Requirement> votedList;
	
	VotingManager(List<Requirement> requirements, List<Vote> votes, String user){
		setName("Voting Manager");
		this.votes = votes;
		this.requirements = requirements;
		this.user = user;
		SpringLayout springLayout = new SpringLayout();
		setLayout(springLayout);
		
		
		
		tree.setEditable(true);
		/*
		tree.setModel(new DefaultTreeModel(
			new DefaultMutableTreeNode("JTree") {
				{
					DefaultMutableTreeNode node_1;
					node_1 = new DefaultMutableTreeNode("colors");
						node_1.add(new DefaultMutableTreeNode("blue"));
						node_1.add(new DefaultMutableTreeNode("violet"));
						node_1.add(new DefaultMutableTreeNode("red"));
						node_1.add(new DefaultMutableTreeNode("yellow"));
					add(node_1);
					node_1 = new DefaultMutableTreeNode("sports");
						node_1.add(new DefaultMutableTreeNode("basketball"));
						node_1.add(new DefaultMutableTreeNode("soccer"));
						node_1.add(new DefaultMutableTreeNode("football"));
						node_1.add(new DefaultMutableTreeNode("hockey"));
					add(node_1);
					node_1 = new DefaultMutableTreeNode("food");
						node_1.add(new DefaultMutableTreeNode("hot dogs"));
						node_1.add(new DefaultMutableTreeNode("pizza"));
						node_1.add(new DefaultMutableTreeNode("ravioli"));
						node_1.add(new DefaultMutableTreeNode("bananas"));
					add(node_1);
				}
			}
		));*/
		update();
		springLayout.putConstraint(SpringLayout.NORTH, tree, 10, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, tree, 10, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.SOUTH, tree, -10, SpringLayout.SOUTH, this);
		springLayout.putConstraint(SpringLayout.EAST, tree, -10, SpringLayout.EAST, this);
		add(tree);
		
		
	}
	
	private void update(){
		
		TreeNode rootNode = createNodes();
		
		tree = new JTree(rootNode);
		tree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				pushSelection();
			}
		});
	}

	protected void pushSelection() {
		Container parent = this.getParent();
		LinkedList<Container> explore = new LinkedList<Container>();
		explore.add(parent);
		int timeout = 0;
		VotingPanel votingPanel = new VotingPanel(new LinkedList<Requirement>(), new LinkedList<Vote>(), "");
		while ((explore.size() > 0) && (timeout < 5)){
			Container current = explore.pop();
			if (current.getName() == "VotingPanel"){
				votingPanel = (VotingPanel)current;
			}
			else{
				explore.add(current.getParent());
			}
			timeout++;
		}
		Requirement rqt = getSelected();
		votingPanel.setSelected(rqt);
	}
	
	private Requirement getSelected(){
		Requirement rqt = new Requirement();
		TreePath paths[] = tree.getSelectionPaths();
		LinkedList<TreePath> path = new LinkedList<TreePath>();
		for (TreePath tPath : paths){
			path.addLast(tPath);
		}
		
		TreeNode node = (TreeNode)path.getLast().getLastPathComponent();
		String selected = node.toString();
		for (Requirement requirement : this.requirements){
			if (requirement.getName() == selected){
				return requirement;
			}
		}
		
		return null;
	}

	private TreeNode createNodes() {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("Requirements");
		DefaultMutableTreeNode notVoted = new DefaultMutableTreeNode("Need Estimation");
		DefaultMutableTreeNode voted = new DefaultMutableTreeNode("Estimated");
		notVotedList = new LinkedList<Requirement>();
		votedList = new LinkedList<Requirement>();
		
		for (Requirement rqt : this.requirements)
			if (hasVote(rqt)){
				votedList.add(rqt);
			}
			else{
				notVotedList.add(rqt);
			}
		
		for (Requirement rqt : votedList){
			DefaultMutableTreeNode node1 = new DefaultMutableTreeNode(rqt.getName());
			voted.add(node1);
		}
		
		for (Requirement rqt : notVotedList){
			DefaultMutableTreeNode node1 = new DefaultMutableTreeNode(rqt.getName());
			notVoted.add(node1);
		}
		
		root.add(notVoted);
		root.add(voted);
		return root;
	}

	private boolean hasVote(Requirement rqt) {
		int id = rqt.getId();
		boolean has = false;
		for (Vote vt : this.votes){
			if (vt.getID() == id){
				has = true;
			}
		}
		
		return has;
	}
}
