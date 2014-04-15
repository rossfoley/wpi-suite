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
		DefaultMutableTreeNode closedSessions = new DefaultMutableTreeNode("Closed Sessions");
		System.out.println(sessions.size());

		for(int i = 0; i < sessions.size(); i++) {
			DefaultMutableTreeNode newSessionNode = new DefaultMutableTreeNode(sessions.get(i)); //make a new session node to add
			if (sessions.get(i).isOpen()) {
				openSessions.add(newSessionNode);
			}
			else if (sessions.get(i).isPending()) {
				pendingSessions.add(newSessionNode);
			}
			else {
				closedSessions.add(newSessionNode);
			}
		}
		
		top.add(pendingSessions);
		top.add(openSessions);
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
					// Shawn added lines here
					if(((PlanningPokerSession) node.getUserObject()).getSessionCreatorName().equals(ConfigManager.getConfig().getUserName())){
					// now here implement the button visibility for creator
					}
					PlanningPokerSession session = (PlanningPokerSession)node.getUserObject();
					displaySession(session);
				}
			}
		}
	
			/* Right button!
		    if (SwingUtilities.isRightMouseButton(e)) {
		    }
		    */
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
		if(!initialized)
		{
			try 
			{
				GetSessionController.getInstance().retrieveSessions();
				initialized = true;
			}
			catch (Exception e)
			{

			}
		}

		super.paintComponent(g);
	}

	/**
	 * Method valueChanged.
	 * @param e TreeSelectionEvent
	 * @see javax.swing.event.TreeSelectionListener#valueChanged(TreeSelectionEvent)
	 */
	/*	@Override
	public void valueChanged(TreeSelectionEvent e) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
		if(node != null)
		{
			if(node.getUserObject() instanceof Iteration)
			{	
				ViewEventController.getInstance().getIterationOverview().highlight((Iteration)node.getUserObject());
			}
			else
			{
				ViewEventController.getInstance().getIterationOverview().highlight(IterationModel.getInstance().getBacklog());
			}
		}
	}
*/
	/**
	 * Method selectIteration.
	 * @param iteration Iteration
	 */
/*	public void selectIteration(Iteration iteration) {
		DefaultMutableTreeNode root = (DefaultMutableTreeNode)tree.getModel().getRoot();
		Enumeration<DefaultMutableTreeNode> e = root.breadthFirstEnumeration();
		
		DefaultMutableTreeNode foundNode = null;
		while(e.hasMoreElements())
		{
			DefaultMutableTreeNode node = e.nextElement();
			if(node.getUserObject() == iteration)
			{
				foundNode = node;
				break;
			}
		}
		
		if(foundNode != null) 
		{
			TreePath path = new TreePath(foundNode.getPath());
			tree.setSelectionPath(path);
			tree.scrollPathToVisible(path);
		}
	}
*/
	/**
	 * @param list the list of iterations to be sorted
	 * @return the same list sorted by start date
	 */
	/*public List<Iteration> sortIterations(List<Iteration> list) {
		
		Collections.sort(list, new IterationComparator());

		return list;
	}
	*/
	/**
	 * @param list requirements to be sorted
	 * @return the same list sorted by name
	 * 
	 */
/*	public List<Requirement> sortRequirements(List<Requirement> list) {
		
		Collections.sort(list, new RequirementComparator());

		return list;
	}*/
}


/**
 * @author Kevin
 * sorts the Iterations by date
 *
 */
	/*
class IterationComparator implements Comparator<Iteration> {
    public int compare(Iteration I1, Iteration I2) {
       if(I1.getStart() == null) 
    	   return -1;
       if(I2.getStart() == null)
    	   return 1;
       return I1.getStart().getDate().compareTo(I2.getStart().getDate());
    }
}*/

/**
 * @author Kevin
 * sorts Requirements by name
 *
 */
	/*
class RequirementComparator implements Comparator<Requirement>{
	public int compare(Requirement R1, Requirement R2){
		return R1.getName().compareTo(R2.getName());
	}
}*/
