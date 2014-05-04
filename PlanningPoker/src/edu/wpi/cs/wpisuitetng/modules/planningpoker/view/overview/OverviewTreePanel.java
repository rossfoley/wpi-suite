/*******************************************************************************
 * Copyright (c) 2014 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Team8s
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.overview;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.DropMode;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import edu.wpi.cs.wpisuitetng.janeway.config.ConfigManager;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.controller.AddDeckController;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.controller.GetDeckController;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.controller.GetSessionController;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSessionModel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.ViewEventController;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.ViewMode;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.controller.GetRequirementsController;


/**
 * Displays all sessions in a tree for the left side of the overview panel
 * @see requirementmanager.view.overview.overviewtree
 * @version 4/18/14
 */
public class OverviewTreePanel extends JScrollPane implements MouseListener, TreeSelectionListener {

	private JTree tree;
	private boolean initialized;
	
	/**
	 * Sets up the left hand panel of the overview
	 */
	public OverviewTreePanel()
	{
        this.setViewportView(tree);
        ViewEventController.getInstance().setOverviewTree(this);
		this.refresh();
		initialized = false;
	}
	
	/**
	 * Method valueChanged.
	 * @see javax.swing.event.TreeSelectionListener#valueChanged(TreeSelectionEvent) */
	@Override
	public void valueChanged(TreeSelectionEvent e) {
		
	}
	
	/**
	 * Clears out the current tree and rebuilds it
	 */
	public void refresh() {
		final TreeNode rootNode = createNodes();
		tree = new JTree(rootNode);
        
		for (int i = 0; i < tree.getRowCount(); i++) {
			tree.expandRow(i);
		}
		
		OverviewDetailPanel overviewPanel = ViewEventController.getInstance().getOverviewDetailPanel();
		if (overviewPanel != null) {
			overviewPanel.clearPanel();
			ViewEventController.getInstance().setOverviewDetailPanel(overviewPanel);
		}
		// Tell it that it can only select one thing at a time
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setToggleClickCount(0);
 
        // Set to custom cell renderer so that icons make sense
        tree.setCellRenderer(new CustomTreeCellRenderer());
        // Add a listener to check for clicking
        tree.addMouseListener(this);
        tree.addTreeSelectionListener(this);
        
        tree.setDropMode(DropMode.ON);
        
        setViewportView(tree); //make panel display the tree
        // Update the ViewEventControler so it contains the right tree
        ViewEventController.getInstance().setOverviewTree(this);
        // Disable all buttons if nothing is selected
        if (tree.getLastSelectedPathComponent() != null) {
        	try {
        		ViewEventController.getInstance().getPlanningPokerSessionButtonsPanel().disableAllButtons();
        	} catch (NullPointerException ex) {
        		System.out.println("OverviewTree: Buttons panel not created yet. Cannot disable");
        	}
        }
	}
	
	/**
	 * Method mousePressed.
	 * @param e MouseEvent
	 * @see java.awt.event.MouseListener#mousePressed(MouseEvent) */
	@Override
	public void mousePressed(MouseEvent e) {
		final int x = e.getX();
		final int y = e.getY();

		final TreePath treePath = tree.getPathForLocation(x, y);
		// If the treePath exists
		if(treePath != null) {
			// If the node exists in the tree
			final DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
			if(node != null) {
				// If the node is a PlanningPokerSession
				if (node.getUserObject() instanceof PlanningPokerSession) {
					final PlanningPokerSession session = (PlanningPokerSession)node.getUserObject();

					if(e.getClickCount() == 2) {
						doubleClickOpenSession(session);
					} else {

						displaySession(session);
					}
				}
				else {
					OverviewDetailPanel overviewDetails = ViewEventController.getInstance().getOverviewDetailPanel();
					overviewDetails.clearPanel();
					ViewEventController.getInstance().setOverviewDetailPanel(overviewDetails);
				}
			}
		}
	}
	
	private TreeNode createNodes() {
		final List<PlanningPokerSession> sessions = PlanningPokerSessionModel.getInstance().getPlanningPokerSessions(); //retrieve the list of sessions
		
		final DefaultMutableTreeNode top = new DefaultMutableTreeNode("All Sessions"); //makes a starting node
		final DefaultMutableTreeNode pendingSessions = new DefaultMutableTreeNode("My Pending Sessions");
		final DefaultMutableTreeNode openSessions = new DefaultMutableTreeNode("Open Sessions");
		final DefaultMutableTreeNode endedSessions = new DefaultMutableTreeNode("Ended Sessions");
		final DefaultMutableTreeNode closedSessions = new DefaultMutableTreeNode("Archived Sessions");
		
		OverviewDetailPanel overviewPanel = ViewEventController.getInstance().getOverviewDetailPanel();
		if (overviewPanel != null) {
			overviewPanel.clearPanel();
			ViewEventController.getInstance().setOverviewDetailPanel(overviewPanel);
		}
		
		for(PlanningPokerSession session : sessions) {
			DefaultMutableTreeNode newSessionNode = new DefaultMutableTreeNode(session); //make a new session node to add
			boolean isOwner = false;
			try {
				isOwner = session.getSessionCreatorName().equals(ConfigManager.getConfig().getUserName());
			}
			catch (NullPointerException e) {
				
			}

			if (session.isClosed()) {
				closedSessions.add(newSessionNode);
			} else if (session.isEnded()) {
				endedSessions.add(newSessionNode);
			} else if (session.isOpen()) {
				openSessions.add(newSessionNode);
			} else if (session.isPending() && isOwner) {
				pendingSessions.add(newSessionNode);
			}
		}
		
		top.add(pendingSessions);
		top.add(openSessions);
		top.add(endedSessions);
		top.add(closedSessions);
		
		return top;
	}
	
	/**
	 * Updates the OverviewDetailPanel with the given session
	 * @param session The session that has been selected
	 */
	protected void displaySession(PlanningPokerSession session) {
		ViewEventController.getInstance().getPlanningPokerSessionButtonsPanel().enableButtonsForSession(session);
		ViewEventController.getInstance().displayDetailedSession(session);
	}
	
	
	/**
	 * Method mouseClicked.
	 * @param e MouseEvent
	 * @see java.awt.event.MouseListener#mouseClicked(MouseEvent) */
	@Override
	public void mouseClicked(MouseEvent e) {
	}

	/**
	 * Method mouseReleased.
	 * @param e MouseEvent
	 * @see java.awt.event.MouseListener#mouseReleased(MouseEvent) */
	@Override
	public void mouseReleased(MouseEvent e) {
	}

	/**
	 * Method mouseEntered.
	 * @param e MouseEvent
	 * @see java.awt.event.MouseListener#mouseEntered(MouseEvent) */
	@Override
	public void mouseEntered(MouseEvent e) {
	}

	/**
	 * Method mouseExited.
	 * @param e MouseEvent
	 * @see java.awt.event.MouseListener#mouseExited(MouseEvent) */
	@Override
	public void mouseExited(MouseEvent e) {
	}
	
	/**
	 * @return the tree
	 */
	public JTree getTree() {
		return tree;
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		if (!initialized) {
			try {
				GetSessionController.getInstance().retrieveSessions();
				GetRequirementsController.getInstance().retrieveRequirements();
				AddDeckController.getInstance().addDefaultDeck();
				GetDeckController.getInstance().retrieveDecks();
				initialized = true;
			} catch (Exception e) {}
		}

		super.paintComponent(g);
	}
	
	/**
	 * in case of double click, opens up the appropriate panel
	 * @param session	The planningPoker session to use
	 */
	public void doubleClickOpenSession(PlanningPokerSession session) {
		final String sessionOwner = session.getSessionCreatorName();
		if (sessionOwner.equals(ConfigManager.getConfig().getUserName())) {
			if (session.isPending()) {
				//if doubleclick and session is pending, open up an editing panel
				ViewEventController.getInstance().openSessionTab(session, ViewMode.EDITING);
			}
		}
		if (session.isOpen()) {
			// If doubleclick and session is open, open up a voting panel
			ViewEventController.getInstance().openSessionTab(session, ViewMode.VOTING);
		}
			// If doubleclick and session is ended or closed , open up a statistics panel
		if (session.isEnded() || session.isClosed()) {
			ViewEventController.getInstance().openSessionTab(session, ViewMode.STATISTICS);
		}
	}
}
