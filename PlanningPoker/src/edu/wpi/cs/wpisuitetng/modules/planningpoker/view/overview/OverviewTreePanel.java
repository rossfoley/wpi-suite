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

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.LinkedList;
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
import edu.wpi.cs.wpisuitetng.modules.planningpoker.controller.GetSessionController;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSessionModel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession.SessionState;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.ViewEventController;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.overview.OverviewDetailPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.controller.GetRequirementsController;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.iterationcontroller.GetIterationController;


/**
 * Displays all sessions in a tree for the left side of the overview panel
 * @see requirementmanager.view.overview.overviewtree
 */
public class OverviewTreePanel extends JScrollPane implements MouseListener, TreeSelectionListener {

	private JTree tree;
	private PlanningPokerSession currentSession;
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
	 * This will wipe out the current tree and rebuild it
	 */
	public void refresh(){

		List<PlanningPokerSession> sessions = PlanningPokerSessionModel.getInstance().getPlanningPokerSessions(); //retrieve the list of sessions
		
		DefaultMutableTreeNode top = new DefaultMutableTreeNode("All Sessions"); //makes a starting node
		DefaultMutableTreeNode pendingSessions = new DefaultMutableTreeNode("My Pending Sessions");
		DefaultMutableTreeNode openSessions = new DefaultMutableTreeNode("Open Sessions");
		DefaultMutableTreeNode endedSessions = new DefaultMutableTreeNode("Ended Sessions");
		DefaultMutableTreeNode closedSessions = new DefaultMutableTreeNode("Closed Sessions");

		for(PlanningPokerSession session : sessions) {
			DefaultMutableTreeNode newSessionNode = new DefaultMutableTreeNode(session); //make a new session node to add
			
			if (session.getGameState() == SessionState.OPEN) {
				openSessions.add(newSessionNode);
			}
			else if (session.getGameState() == SessionState.PENDING) {
				pendingSessions.add(newSessionNode);
			}
			else if (session.getGameState() == SessionState.VOTINGENDED) {
				endedSessions.add(newSessionNode);
			}
			else {
				closedSessions.add(newSessionNode);
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
        
        //tree.setTransferHandler(new IterationTransferHandler());
        tree.setDragEnabled(true);
        tree.setDropMode(DropMode.ON);
        
        this.setViewportView(tree); //make panel display the tree
        
        ViewEventController.getInstance().setOverviewTree(this); //update the ViewEventControler so it contains the right tree

        System.out.println("finished refreshing the tree");
	}
	
	/**
	 * Method mouseClicked.
	 * @param e MouseEvent
	 * @see java.awt.event.MouseListener#mouseClicked(MouseEvent) */
	@Override
	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();

		TreePath treePath = tree.getPathForLocation(x, y);
			
		if(treePath != null) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
			
			if(node != null) {
				
				if (node.getUserObject() instanceof PlanningPokerSession) {
					ViewEventController.getInstance().getPlanningPokerSessionButtonsPanel().disableEditButton();
					ViewEventController.getInstance().getPlanningPokerSessionButtonsPanel().enableVoteButton();
					ViewEventController.getInstance().getPlanningPokerSessionButtonsPanel().disableEndVoteButton();
					
					if (((PlanningPokerSession) node.getUserObject()).getSessionCreatorName().equals(ConfigManager.getConfig().getUserName())) {
						ViewEventController.getInstance().getPlanningPokerSessionButtonsPanel().enableEditButton();
						if ((((PlanningPokerSession) node.getUserObject()).getGameState()) == SessionState.OPEN) {
							ViewEventController.getInstance().getPlanningPokerSessionButtonsPanel().enableEndVoteButton();
						}
					}
					if (!((PlanningPokerSession) node.getUserObject()).isOpen()) {
						ViewEventController.getInstance().getPlanningPokerSessionButtonsPanel().disableVoteButton();
					}

					
					PlanningPokerSession session = (PlanningPokerSession)node.getUserObject();
					displaySession(session);
				}
			}
		}
	}
	
	protected void displaySession(PlanningPokerSession session) {
		ViewEventController.getInstance().displayDetailedSession(session);
	}
	
	
	/**
	 * Method mousePressed.
	 * @param e MouseEvent
	 * @see java.awt.event.MouseListener#mousePressed(MouseEvent) */
	@Override
	public void mousePressed(MouseEvent e) {		
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
				PlanningPokerSessionModel.getInstance().startLiveUpdating();
				initialized = true;
			} catch (Exception e) {}
		}

		super.paintComponent(g);
	}
}