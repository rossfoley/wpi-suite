package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.preferences;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.ViewEventController;

public class HelpTreePanel extends JScrollPane implements MouseListener, TreeSelectionListener {
	private final JTree tree = new JTree();
	
	public HelpTreePanel() {
		tree.setForeground(Color.WHITE);
		tree.setFont(new Font("Tahoma", Font.PLAIN, 11));
		tree.setBackground(Color.WHITE);
		tree.setModel(new DefaultTreeModel(
			new DefaultMutableTreeNode("Planning Poker Help Guide") {
				{
					HelpPanelItem whatIs = new HelpPanelItem("What is Planning Poker?", "whatis"),
							whereDo = new HelpPanelItem("Where do I start?", "wheredo"),
							createNewSession = new HelpPanelItem("Create or Edit a Planning Poker Session", "createnewsession"),
							createNewDeck = new HelpPanelItem("Creating a new deck", "createnewdeck"),
							createNewReq = new HelpPanelItem("Creating a new requirement", "createnewreq"),
							votePanel = new HelpPanelItem("The voting panel", "votepanel"),
							deckStyles = new HelpPanelItem("Deck Styles", "deckstyles"),
							viewSessionDetails = new HelpPanelItem("Viewing session details", "sessiondetails"),
							Archive = new HelpPanelItem("Archiving a session", "archive");
							
					
					DefaultMutableTreeNode rootNode;
					rootNode = new DefaultMutableTreeNode("Getting Started");
						rootNode.add(new DefaultMutableTreeNode(whatIs));
						rootNode.add(new DefaultMutableTreeNode(whereDo));
					add(rootNode);
					rootNode = new DefaultMutableTreeNode("Creating or editing a session");
						rootNode.add(new DefaultMutableTreeNode(createNewSession));
						rootNode.add(new DefaultMutableTreeNode(createNewDeck));
						rootNode.add(new DefaultMutableTreeNode(createNewReq));
					add(rootNode);
					rootNode = new DefaultMutableTreeNode("Voting on a session");
						rootNode.add(new DefaultMutableTreeNode(votePanel));
						rootNode.add(new DefaultMutableTreeNode(deckStyles));
					add(rootNode);
					rootNode = new DefaultMutableTreeNode("Viewing session statistics");
						rootNode.add(new DefaultMutableTreeNode(viewSessionDetails));
					add(rootNode);
					add(new DefaultMutableTreeNode(Archive));
				}
			}
		));
		tree.addMouseListener(this);
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
	public void mousePressed(MouseEvent e) {
		final int x = e.getX();
		final int y = e.getY();

		final TreePath treePath = tree.getPathForLocation(x, y);
		// If the treePath exists
		if (treePath != null) {
			// If the node exists in the tree
			final DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
			if (node != null) {
				// If the node is a PlanningPokerSession
				if (node.getUserObject() instanceof HelpPanelItem) {
					final HelpPanelItem newItem = (HelpPanelItem) node.getUserObject();
					final HelpTextPanel contentPanel = ViewEventController.getInstance().getHelpTextPanel();
					contentPanel.setPage(newItem.getFileName());
				}
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
