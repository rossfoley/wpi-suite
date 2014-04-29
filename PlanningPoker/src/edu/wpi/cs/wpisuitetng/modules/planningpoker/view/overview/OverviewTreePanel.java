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
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import edu.wpi.cs.wpisuitetng.janeway.config.ConfigManager;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.controller.AddDeckController;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.controller.GetDeckController;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.controller.GetSessionController;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSessionModel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.ViewEventController;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.controller.GetRequirementsController;


/**
 * Displays all sessions in a tree for the left side of the overview panel
 * @see requirementmanager.view.overview.overviewtree
 * @author Randy Acheson
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
		// Disable all toolbar buttons on initialization
		try {
			ViewEventController.getInstance().getPlanningPokerSessionButtonsPanel().disableEditButton();
			ViewEventController.getInstance().getPlanningPokerSessionButtonsPanel().disableVoteButton();
			ViewEventController.getInstance().getPlanningPokerSessionButtonsPanel().disableEndVoteButton();
			ViewEventController.getInstance().getPlanningPokerSessionButtonsPanel().disableStatisticsButton();
		} catch (NullPointerException ex) {} // Do nothing if the toolbar has not been instantiated yet
	}
	
	/**
	 * Method valueChanged.
	 * @see javax.swing.event.TreeSelectionListener#valueChanged(TreeSelectionEvent) */
	@Override
	public void valueChanged(TreeSelectionEvent e) {
		
	}
	
	/**
	 * This will wipe out the current tree and rebuild it
	 */
	public void refresh() {
		final List<PlanningPokerSession> sessions = PlanningPokerSessionModel.getInstance().getPlanningPokerSessions(); //retrieve the list of sessions
		
		final DefaultMutableTreeNode top = new DefaultMutableTreeNode("All Sessions"); //makes a starting node
		final DefaultMutableTreeNode pendingSessions = new DefaultMutableTreeNode("My Pending Sessions");
		final DefaultMutableTreeNode openSessions = new DefaultMutableTreeNode("Open Sessions");
		final DefaultMutableTreeNode endedSessions = new DefaultMutableTreeNode("Ended Sessions");
		final DefaultMutableTreeNode closedSessions = new DefaultMutableTreeNode("Closed Sessions");
		
		for(PlanningPokerSession session : sessions) {
			DefaultMutableTreeNode newSessionNode = new DefaultMutableTreeNode(session); //make a new session node to add
			boolean isOwner = session.getSessionCreatorName().equals(ConfigManager.getConfig().getUserName());

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
		
        tree = new JTree(top); //create the tree with the top node as the top
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION); //tell it that it can only select one thing at a time
        tree.setToggleClickCount(0);
 
        tree.setCellRenderer(new CustomTreeCellRenderer()); //set to custom cell renderer so that icons make sense
        tree.addMouseListener(this); //add a listener to check for clicking
        tree.addTreeSelectionListener(this);
        
        tree.setDragEnabled(true);
        tree.setDropMode(DropMode.ON);
        
        this.setViewportView(tree); //make panel display the tree
        
        ViewEventController.getInstance().setOverviewTree(this); //update the ViewEventControler so it contains the right tree

        //System.out.println("finished refreshing the tree");
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
			
		if(treePath != null) {
			final DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
			
			if(node != null) {
				
				if (node.getUserObject() instanceof PlanningPokerSession) {
					final PlanningPokerSession session = (PlanningPokerSession)node.getUserObject();
					final String sessionOwner = session.getSessionCreatorName();

					// Disable everything by default
					ViewEventController.getInstance().getPlanningPokerSessionButtonsPanel().disableEditButton();
					ViewEventController.getInstance().getPlanningPokerSessionButtonsPanel().disableVoteButton();
					ViewEventController.getInstance().getPlanningPokerSessionButtonsPanel().disableEndVoteButton();
					
					// If the current user is the owner of the session
					if (sessionOwner.equals(ConfigManager.getConfig().getUserName())) {
						// Enable editing if pending					
						if (session.isPending()) {
							ViewEventController.getInstance().getPlanningPokerSessionButtonsPanel().enableEditButton();
						}
						// Allow end of voting if open and editing if no estimates yet
						else if (session.isOpen()) {
							ViewEventController.getInstance().getPlanningPokerSessionButtonsPanel().enableEndVoteButton();
							// If no estimates yet, allow editing
							if (session.isEditable()) {
								ViewEventController.getInstance().getPlanningPokerSessionButtonsPanel().enableEditButton();
							}
						}
					}
					
					// If session is open, allow voting
					if (session.isOpen()) {
						ViewEventController.getInstance().getPlanningPokerSessionButtonsPanel().enableVoteButton();
					}
					
					// If the session is ended or closed, allow the user to view statistics
					if (session.isEnded() || session.isClosed()) {
						ViewEventController.getInstance().getPlanningPokerSessionButtonsPanel().enableStatisticsButton();
					}
					else if (session.isOpen() || session.isPending()) {
						ViewEventController.getInstance().getPlanningPokerSessionButtonsPanel().disableStatisticsButton();
					}
					
					displaySession(session);
				}
			}
		}
	}
	
	/**
	 * Updates the OverviewDetailPanel with the given session
	 * @param session The session that has been selected
	 */
	protected void displaySession(PlanningPokerSession session) {
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
	 * @return the tree */
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
}
