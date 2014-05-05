/*******************************************************************************
 * Copyright (c) 2014 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Team8s
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.voting;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.SpringLayout;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeNode;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.Estimate;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.overview.icons.IterationIcon;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.overview.icons.RequirementIcon;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;

public class VotingManager extends JPanel {
	
	private final List<Requirement> requirements;
	private final List<Estimate> estimates;
	private final String ownerName;
	private transient Vector<SelectionListener> selectionListeners;
	private transient Vector<EstimateListener> estimateListeners;
	
	private JTree tree = new JTree();
	
	private List<Requirement> notVotedList;
	private List<Requirement> votedList;
	
	public VotingManager(List<Requirement> requirements, PlanningPokerSession pokerSession, String ownerName) {		
		setName("Voting Manager");
		estimates = pokerSession.getEstimates();
		this.requirements = requirements;
		this.ownerName = ownerName;
		
		final SpringLayout springLayout = new SpringLayout();
		setLayout(springLayout);
		
		update();
		springLayout.putConstraint(SpringLayout.NORTH, tree, 0, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, tree, 0, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.SOUTH, tree, 0, SpringLayout.SOUTH, this);
		springLayout.putConstraint(SpringLayout.EAST, tree, 0, SpringLayout.EAST, this);
		
		tree.setCellRenderer(new DefaultTreeCellRenderer() {
			
			@Override
			public Component getTreeCellRendererComponent(JTree tree, Object value,
					boolean sel, boolean expanded, boolean leaf, int row,
					boolean hasFocus) {
				super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
						row, hasFocus);
				final DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
				final Icon requirementIcon = new RequirementIcon();
				final Icon iterationIcon = new IterationIcon();
				final String name = (String) node.getUserObject();

				setIcon(requirementIcon);
				if (name != null) {
					if (name.equals("Requirements") || name.equals("Need Estimation") || name.equals("Estimated")) {
						setIcon(iterationIcon);
					}
				}

				return this; 
			}
		});
		add(tree);
	}
	
	private void update() {
		final TreeNode rootNode = createNodes();
		
		tree = new JTree(rootNode);
		for (int i = 0; i < tree.getRowCount(); i++) {
			tree.expandRow(i);
		}
		
		tree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				final Requirement selected = getSelected();
				if (selected != null){
					System.out.println(selected.getId() + ":" + selected.getName());
					fireSelectionEvent(selected);
				}
			}
		});
	}

	/**
	 * @return	The currently selected requirement or null if none selected	
	 */
	private Requirement getSelected(){
		final TreeNode node = (TreeNode)tree.getLastSelectedPathComponent();
		if (node != null){
			final String selected = node.toString();
			for (Requirement requirement : requirements){
				if (requirement.getName() == selected){
					return requirement;
				}
			}
		}
		return null;
	}

	private TreeNode createNodes() {
		final DefaultMutableTreeNode root = new DefaultMutableTreeNode("Requirements");
		final DefaultMutableTreeNode notVoted = new DefaultMutableTreeNode("Need Estimation");
		final DefaultMutableTreeNode voted = new DefaultMutableTreeNode("Estimated");
		notVotedList = new LinkedList<Requirement>();
		votedList = new LinkedList<Requirement>();
		
		
		for (Requirement rqt : requirements){
			if (hasEstimate(rqt)){
				votedList.add(rqt);
			}
			else{
				notVotedList.add(rqt);
			}
		}
		System.out.println("Not Voted:" + notVotedList.size());
		System.out.println("Voted:" + votedList.size());
		for (Requirement rqt : votedList) {
			DefaultMutableTreeNode node1 = new DefaultMutableTreeNode(rqt.getName());
			voted.add(node1);
		}
		
		for (Requirement rqt : notVotedList) {
			DefaultMutableTreeNode node1 = new DefaultMutableTreeNode(rqt.getName());
			notVoted.add(node1);
		}
		
		root.add(notVoted);
		root.add(voted);
		return root;
	}

	private boolean hasEstimate(Requirement rqt) {
		final int id = rqt.getId();
		for (Estimate es : estimates) {

			if (es.getRequirementID() == id && es.getOwnerName().equals(ownerName)) {
				return true;
			}
		}
		
		return false;
	}
	

	synchronized public void addSelectionListener(SelectionListener l) {
		if (selectionListeners == null) {
			selectionListeners = new Vector<SelectionListener>();
		}
		selectionListeners.addElement(l);
	}  

	/** Remove a listener for EstimateEvents */
	synchronized public void removeSelectionListener(SelectionListener l) {
		if (selectionListeners == null) {
			selectionListeners = new Vector<SelectionListener>();
		}
		else {
			selectionListeners.removeElement(l);
		}
	}
	
	protected void fireSelectionEvent(Requirement rqt) {
		// if we have no listeners, do nothing...
		if (selectionListeners != null && !selectionListeners.isEmpty()) {
			// create the event object to send
			final SelectionEvent event = 
					new SelectionEvent(this, rqt);

			// make a copy of the listener list in case
			//   anyone adds/removes listeners
			final Vector<SelectionListener> targets;
			synchronized (this) {
				targets = (Vector<SelectionListener>) selectionListeners.clone();
			}

			// walk through the listener list and
			//   call the sunMoved method in each
			final Enumeration e = targets.elements();
			while (e.hasMoreElements()) {
				SelectionListener l = (SelectionListener) e.nextElement();
				l.selectionMade(event);
			}
		}
	}
	
	/** Register a listener for EstimateEvents */
	synchronized public void addEstimateListener(EstimateListener l) {
		if (estimateListeners == null) {
			estimateListeners = new Vector<EstimateListener>();
		}
		estimateListeners.addElement(l);
	}  

	/** Remove a listener for EstimateEvents */
	synchronized public void removeEstimateListener(EstimateListener l) {
		if (estimateListeners == null) {
			estimateListeners = new Vector<EstimateListener>();
		}
		else {
			estimateListeners.removeElement(l);
		}
	}

}
