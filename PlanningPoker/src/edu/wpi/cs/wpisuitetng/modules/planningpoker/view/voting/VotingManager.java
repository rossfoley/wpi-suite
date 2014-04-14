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

public class VotingManager extends JPanel{
	
	/**
	 * 
	 */
	//private static final long serialVersionUID = 1L;
	private List<Requirement> requirements;
	private List<Estimate> estimate;
	private int ownerID;
	private EstimateModel estimateModel = EstimateModel.getInstance();
	private transient Vector<SelectionListener> selectionListeners;
	private transient Vector<EstimateListener> estimateListeners;
	
	private JTree tree = new JTree();;
	private DefaultMutableTreeNode rootNode;
	
	private LinkedList<Requirement> notVotedList;
	private LinkedList<Requirement> votedList;
	
	public VotingManager(List<Requirement> requirements, List<Estimate> estimate, int ownerID){
		setName("Voting Manager");
		this.estimate = estimate;
		this.requirements = requirements;
		this.ownerID = ownerID;
		
		/*this.addActionListener(new EstimateListener() {
			//@Override
			public void actionPerformed(EstimateEvent e){
				update();
			};
		});
		*/
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
		/*
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
		}*/
		/*
		int selected[] = tree.getSelectionRows();
		for (int n : selected){
			int row = n - 2;
			if (row <=)
		}*/
		
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
			if (es.getRequirementID() == id && es.getOwnerID() == this.ownerID){
				has = true;
			}
		}
		
		return has;
	}
	
	private LinkedList<Estimate> getEstimates(){
		LinkedList<Estimate> estimates = new LinkedList<Estimate>();
		//estimates = (LinkedList<Estimate>)this.estimateModel.getEstimates();
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

	/** Fire an EstimateEvent to all registered listeners */
	/*protected void fireEstimateEvent() {
		// if we have no listeners, do nothing...
		if (this.estimateListeners != null && !this.estimateListeners.isEmpty()) {
			// create the event object to send
			EstimateEvent event = 
					new EstimateEvent(this, this.getEstimate());

			// make a copy of the listener list in case
			//   anyone adds/removes listeners
			Vector<EstimateListener> targets;
			synchronized (this) {
				targets = (Vector<EstimateListener>) this.estimateListeners.clone();
			}

			// walk through the listener list and
			//   call the sunMoved method in each
			Enumeration e = targets.elements();
			while (e.hasMoreElements()) {
				EstimateListener l = (EstimateListener) e.nextElement();
				l.estimateSubmitted(event);
			}
		}
	}
	*/
}
