package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.voting;

import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import javax.swing.JPanel;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.Estimate;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.EstimateModel;
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

public class VotingManager extends JPanel {
	
	private List<Requirement> requirements;
	private List<Estimate> estimate;
	private String ownerName;
	private EstimateModel estimateModel = EstimateModel.getInstance();
	private transient Vector<SelectionListener> selectionListeners;
	private transient Vector<EstimateListener> estimateListeners;
	
	private JTree tree = new JTree();;
	private DefaultMutableTreeNode rootNode;
	
	private LinkedList<Requirement> notVotedList;
	private LinkedList<Requirement> votedList;
	
	public VotingManager(List<Requirement> requirements, List<Estimate> estimate, String ownerName){
		setName("Voting Manager");
		this.estimate = estimate;
		this.requirements = requirements;
		this.ownerName = ownerName;
		
		SpringLayout springLayout = new SpringLayout();
		setLayout(springLayout);
		
		update();
		springLayout.putConstraint(SpringLayout.NORTH, tree, 0, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, tree, 0, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.SOUTH, tree, 0, SpringLayout.SOUTH, this);
		springLayout.putConstraint(SpringLayout.EAST, tree, 0, SpringLayout.EAST, this);
		add(tree);
		
		
	}
	
	private void update(){
		
		TreeNode rootNode = createNodes();
		
		tree = new JTree(rootNode);
		tree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//pushSelection();
				Requirement selected = getSelected();
				if (selected != null){
					System.out.println(selected.getId() + ":" + selected.getName());
					fireSelectionEvent(selected);
				}
			}
		});
	}
	
	
	

	private Requirement getSelected(){
		this.estimate = getEstimates();
		Requirement rqt = new Requirement();
		
		TreeNode node = (TreeNode)tree.getLastSelectedPathComponent();
		if (node != null){
			String selected = node.toString();
			for (Requirement requirement : this.requirements){
				if (requirement.getName() == selected){
					return requirement;
				}
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
			if (hasEstimate(rqt)){
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

	private boolean hasEstimate(Requirement rqt) {
		int id = rqt.getId();
		boolean has = false;
		for (Estimate es : this.estimate){
			if (es.getRequirementID() == id && es.getOwnerName() == this.ownerName){
				has = true;
			}
		}
		
		return has;
	}
	
	private LinkedList<Estimate> getEstimates(){
		LinkedList<Estimate> estimates = new LinkedList<Estimate>();
		for (Estimate estimate : this.estimateModel.getEstimates()){
			estimates.add(estimate);
		}
		return estimates;
	}
	
	synchronized public void addSelectionListener(SelectionListener l) {
		if (this.selectionListeners == null) {
			this.selectionListeners = new Vector<SelectionListener>();
		}
		this.selectionListeners.addElement(l);
	}  

	/** Remove a listener for EstimateEvents */
	synchronized public void removeSelectionListener(SelectionListener l) {
		if (this.selectionListeners == null) {
			this.selectionListeners = new Vector<SelectionListener>();
		}
		else {
			this.selectionListeners.removeElement(l);
		}
	}
	
	protected void fireSelectionEvent(Requirement rqt) {
		// if we have no listeners, do nothing...
		if (this.selectionListeners != null && !this.selectionListeners.isEmpty()) {
			// create the event object to send
			SelectionEvent event = 
					new SelectionEvent(this, rqt);

			// make a copy of the listener list in case
			//   anyone adds/removes listeners
			Vector<SelectionListener> targets;
			synchronized (this) {
				targets = (Vector<SelectionListener>) this.selectionListeners.clone();
			}

			// walk through the listener list and
			//   call the sunMoved method in each
			Enumeration e = targets.elements();
			while (e.hasMoreElements()) {
				SelectionListener l = (SelectionListener) e.nextElement();
				l.selectionMade(event);
			}
		}
	}
	
	/** Register a listener for EstimateEvents */
	synchronized public void addEstimateListener(EstimateListener l) {
		if (this.estimateListeners == null) {
			this.estimateListeners = new Vector<EstimateListener>();
		}
		this.estimateListeners.addElement(l);
	}  

	/** Remove a listener for EstimateEvents */
	synchronized public void removeEstimateListener(EstimateListener l) {
		if (this.estimateListeners == null) {
			this.estimateListeners = new Vector<EstimateListener>();
		}
		else {
			this.estimateListeners.removeElement(l);
		}
	}

}
