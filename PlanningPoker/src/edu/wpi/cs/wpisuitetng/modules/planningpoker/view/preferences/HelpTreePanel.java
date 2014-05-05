package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.preferences;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JScrollPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.JTree;
import javax.swing.JPanel;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.AbstractListModel;
import javax.swing.UIManager;
import java.awt.Font;
import java.awt.Color;

public class HelpTreePanel extends JScrollPane implements MouseListener, TreeSelectionListener{
	public HelpTreePanel() {
		
		JTree tree = new JTree();
		tree.setForeground(Color.WHITE);
		tree.setFont(new Font("Tahoma", Font.PLAIN, 11));
		tree.setBackground(Color.WHITE);
		tree.setModel(new DefaultTreeModel(
			new DefaultMutableTreeNode("Planning Poker Help Guide") {
				{
					DefaultMutableTreeNode node_1;
					node_1 = new DefaultMutableTreeNode("Getting Started");
						node_1.add(new DefaultMutableTreeNode("What is Planning Poker?"));
						node_1.add(new DefaultMutableTreeNode("Where do I start?"));
					add(node_1);
					node_1 = new DefaultMutableTreeNode("Creating or editing a session");
						node_1.add(new DefaultMutableTreeNode("Create a new Planning Poker session"));
						node_1.add(new DefaultMutableTreeNode("Creating a new deck"));
						node_1.add(new DefaultMutableTreeNode("Creating a new requirement"));
					add(node_1);
					node_1 = new DefaultMutableTreeNode("Voting on a session");
						node_1.add(new DefaultMutableTreeNode("The voting panel"));
						node_1.add(new DefaultMutableTreeNode("Voting methods"));
					add(node_1);
					node_1 = new DefaultMutableTreeNode("Viewing session statistics");
						node_1.add(new DefaultMutableTreeNode("Viewing session details"));
						node_1.add(new DefaultMutableTreeNode("Making final estimates"));
						node_1.add(new DefaultMutableTreeNode("Sending final estimates"));
					add(node_1);
					add(new DefaultMutableTreeNode("Archiving a session"));
				}
			}
		));
		setViewportView(tree);
		for (int i = 0; i < tree.getRowCount(); i++) {
	         tree.expandRow(i);
	}
	}

	@Override
	public void valueChanged(TreeSelectionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
