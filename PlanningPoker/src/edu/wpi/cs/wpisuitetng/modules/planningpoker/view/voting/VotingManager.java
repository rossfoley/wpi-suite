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
import java.util.Set;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.SpringLayout;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.Estimate;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.overview.icons.IterationIcon;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.overview.icons.RequirementIcon;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.RequirementModel;

/**
 * Creates and manages the requirement tree in the voting page
 *
 */
public class VotingManager extends JPanel {
	
	private final List<Requirement> requirements;
	private final List<Estimate> estimates;
	private final PlanningPokerSession session;
	private final String ownerName;
	private transient Vector<SelectionListener> selectionListeners;
	
	private JTree tree = new JTree();
	
	private List<Requirement> notVotedList;
	private List<Requirement> votedList;
	private Requirement lastSelected;
	
	public VotingManager(Requirement reqToSelect, PlanningPokerSession pokerSession, String ownerName) {
		lastSelected = reqToSelect;
		session = pokerSession;
		setName("Voting Manager");
		requirements = getSessionReqs();
		estimates = pokerSession.getEstimates();
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
				final String name = (String) node.getUserObject().toString();

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
					lastSelected = selected;
					fireSelectionEvent(selected);
				}
			}
		});
		
		if (lastSelected != null) {
			tree.setSelectionPath(findSessionTreePath(lastSelected, (DefaultMutableTreeNode) rootNode));
		}
	}

	/**
	 * @return	The currently selected requirement or null if none selected	
	 */
	private Requirement getSelected() {
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
	
	/**
	 * Finds the path to the input session in the tree
	 * @param prevReq	The previous requirement that was selected
	 * @return	The path to the session in the tree
	 */
	private TreePath findSessionTreePath(Requirement prevReq, DefaultMutableTreeNode root) {
		// If the root is a leaf
		if (root.isLeaf()) {
			Object obj = root.getUserObject();
			// If the object is a planning poker session
			if (obj instanceof Requirement) {
				if (((Requirement)obj).getId() == prevReq.getId()) {
					return new TreePath(root.getPath());
				}
			}
			return null;
		}
		TreePath tPath;

		// Search tree for session
		for (int i = 0; i < root.getChildCount(); i++) {
			tPath = findSessionTreePath(prevReq, (DefaultMutableTreeNode) root.getChildAt(i));
			// If the path is non-null, then this is the path we want!
			if (tPath != null) {
				return tPath;
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
		
		for (Requirement rqt : votedList) {
			DefaultMutableTreeNode node1 = new DefaultMutableTreeNode(rqt);
			voted.add(node1);
		}
		
		for (Requirement rqt : notVotedList) {
			DefaultMutableTreeNode node1 = new DefaultMutableTreeNode(rqt);
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
	
	/**
	 * Gets the requirements that have been selected for the given session
	 * @return a list of requirements that have been selected for the given session
	 */
	public List<Requirement> getSessionReqs(){
		final Set<Integer> sessionReqIds = session.getRequirementIDs();
		final List<Requirement> sessionReqs = new LinkedList<Requirement>();
		for (Integer id : sessionReqIds) {
			Requirement current = RequirementModel.getInstance().getRequirement(id);
			sessionReqs.add(current);			
		}
		return sessionReqs;
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
	
}
