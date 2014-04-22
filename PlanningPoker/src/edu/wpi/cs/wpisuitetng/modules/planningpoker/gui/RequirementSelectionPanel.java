/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.planningpoker.gui;
/**
 * This Class makes up the panel that handles requirement selection
 * The user can manipulate the selected requirements by selecting
 * from one of the lists displayed and using the buttons to move
 * the requirements.
 */
import javax.swing.AbstractListModel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.Box;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JButton;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.requirementselection.RequirementSelectionView;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.controller.GetRequirementsController;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.RequirementModel;

import java.awt.Component;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RequirementSelectionPanel extends JPanel{
	
	private RequirementSelectionView parent;
	private LinkedList<Requirement> requirements;
	private LinkedList<Boolean> selection;
	private JList selectedListGui;
	private JList unselectedListGui;
	private AbstractListModel unSelectedListModel;
	private AbstractListModel selectedListModel;
	protected String[] unSelectedListData;
	protected String[] selectedListData;
	JButton btnAddAll;
	JButton btnAdd;
	JButton btnRemove;
	JButton btnRemoveAll;
	JButton btnNewReq;
	private transient Vector<RequirementsSelectedListener> listeners;
	private int numRequirementsAdded = 0;

	
	/**
	 * Constructor to create the requirement selection panel
	 */
	public RequirementSelectionPanel(RequirementSelectionView parent){
		
		this.parent = parent;
		populateRequirements();
		populateBooleans();
		
		
		SpringLayout springLayout = new SpringLayout();
		setLayout(springLayout);
		
		Box horizontalBox = Box.createHorizontalBox();
		springLayout.putConstraint(SpringLayout.NORTH, horizontalBox, 10, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, horizontalBox, 10, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.SOUTH, horizontalBox, -10, SpringLayout.SOUTH, this);
		springLayout.putConstraint(SpringLayout.EAST, horizontalBox, -10, SpringLayout.EAST, this);
		add(horizontalBox);
		
		JPanel unselectedPanel = new JPanel();
		horizontalBox.add(unselectedPanel);
		SpringLayout sl_unselectedPanel = new SpringLayout();
		unselectedPanel.setLayout(sl_unselectedPanel);
		
		JScrollPane unselectedScrollPane = new JScrollPane();
		sl_unselectedPanel.putConstraint(SpringLayout.NORTH, unselectedScrollPane, 10, SpringLayout.NORTH, unselectedPanel);
		sl_unselectedPanel.putConstraint(SpringLayout.WEST, unselectedScrollPane, 10, SpringLayout.WEST, unselectedPanel);
		sl_unselectedPanel.putConstraint(SpringLayout.SOUTH, unselectedScrollPane, -10, SpringLayout.SOUTH, unselectedPanel);
		sl_unselectedPanel.putConstraint(SpringLayout.EAST, unselectedScrollPane, -10, SpringLayout.EAST, unselectedPanel);
		unselectedPanel.add(unselectedScrollPane);
		
		unselectedListGui = new JList();
		unselectedListGui.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				validButtons();
			}
		});
		unselectedScrollPane.setViewportView(unselectedListGui);
		
		JPanel btnPanel = new JPanel();
		horizontalBox.add(btnPanel);
		SpringLayout sl_btnPanel = new SpringLayout();
		btnPanel.setLayout(sl_btnPanel);
		
		btnAddAll = new JButton(">>");
		btnAddAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectAll();
			}
		});
		sl_btnPanel.putConstraint(SpringLayout.NORTH, btnAddAll, 84, SpringLayout.NORTH, btnPanel);
		sl_btnPanel.putConstraint(SpringLayout.WEST, btnAddAll, 10, SpringLayout.WEST, btnPanel);
		sl_btnPanel.putConstraint(SpringLayout.EAST, btnAddAll, -10, SpringLayout.EAST, btnPanel);
		btnPanel.add(btnAddAll);
		
		btnAdd = new JButton(">");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				add();
			}
		});
		sl_btnPanel.putConstraint(SpringLayout.NORTH, btnAdd, 6, SpringLayout.SOUTH, btnAddAll);
		sl_btnPanel.putConstraint(SpringLayout.WEST, btnAdd, 10, SpringLayout.WEST, btnPanel);
		sl_btnPanel.putConstraint(SpringLayout.EAST, btnAdd, -10, SpringLayout.EAST, btnPanel);
		btnPanel.add(btnAdd);
		
		btnRemove = new JButton("<");
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				remove();
			}
		});
		sl_btnPanel.putConstraint(SpringLayout.NORTH, btnRemove, 12, SpringLayout.SOUTH, btnAdd);
		sl_btnPanel.putConstraint(SpringLayout.WEST, btnRemove, 10, SpringLayout.WEST, btnPanel);
		sl_btnPanel.putConstraint(SpringLayout.EAST, btnRemove, -10, SpringLayout.EAST, btnPanel);
		btnPanel.add(btnRemove);
		
		btnRemoveAll = new JButton("<<");
		btnRemoveAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				unselectAll();
			}
		});
		sl_btnPanel.putConstraint(SpringLayout.NORTH, btnRemoveAll, 6, SpringLayout.SOUTH, btnRemove);
		sl_btnPanel.putConstraint(SpringLayout.WEST, btnRemoveAll, 10, SpringLayout.WEST, btnPanel);
		sl_btnPanel.putConstraint(SpringLayout.EAST, btnRemoveAll, -10, SpringLayout.EAST, btnPanel);
		btnPanel.add(btnRemoveAll);
		
		btnNewReq = new JButton("New Req");
		btnNewReq.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openCreationPanel();
			}
		});
		sl_btnPanel.putConstraint(SpringLayout.NORTH, btnNewReq, 6, SpringLayout.SOUTH, btnRemoveAll);
		sl_btnPanel.putConstraint(SpringLayout.WEST, btnNewReq, 10, SpringLayout.WEST, btnPanel);
		sl_btnPanel.putConstraint(SpringLayout.EAST, btnNewReq, -10, SpringLayout.EAST, btnPanel);
		btnPanel.add(btnNewReq);
		
		Component horizontalStrut = Box.createHorizontalStrut(20);
		sl_btnPanel.putConstraint(SpringLayout.NORTH, horizontalStrut, 24, SpringLayout.SOUTH, btnRemoveAll);
		sl_btnPanel.putConstraint(SpringLayout.WEST, horizontalStrut, 0, SpringLayout.WEST, btnPanel);
		sl_btnPanel.putConstraint(SpringLayout.EAST, horizontalStrut, 0, SpringLayout.EAST, btnPanel);
		btnPanel.add(horizontalStrut);
		
		JPanel selectedPanel = new JPanel();
		horizontalBox.add(selectedPanel);
		SpringLayout sl_selectedPanel = new SpringLayout();
		selectedPanel.setLayout(sl_selectedPanel);
		
		JScrollPane selectedScrollPane = new JScrollPane();
		sl_selectedPanel.putConstraint(SpringLayout.NORTH, selectedScrollPane, 10, SpringLayout.NORTH, selectedPanel);
		sl_selectedPanel.putConstraint(SpringLayout.WEST, selectedScrollPane, 10, SpringLayout.WEST, selectedPanel);
		sl_selectedPanel.putConstraint(SpringLayout.SOUTH, selectedScrollPane, -10, SpringLayout.SOUTH, selectedPanel);
		sl_selectedPanel.putConstraint(SpringLayout.EAST, selectedScrollPane, -10, SpringLayout.EAST, selectedPanel);
		selectedPanel.add(selectedScrollPane);
		
		selectedListGui = new JList();
		selectedListGui.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				validButtons();
			}
		});
		selectedScrollPane.setViewportView(selectedListGui);
		
		
		update();
	}
	
	/**
	 * This function creates a list of booleans that is the same size
	 * as the list of requirements and used to indicate the selection
	 * status of the associated requirement 
	 */
	private void populateBooleans() {
		if (selection == null){
			this.selection = new LinkedList<Boolean>();
		}
		
		while (requirements.size() > selection.size()){
			selection.addLast(false);
		}
	}
	
	/**
	 * This function is used to grab the list requirements in the 
	 * backlog
	 */
	private void populateRequirements() {
		//System.out.println("In Populate Requirements");
		
				// Get singleton instance of Requirements Controller
				GetRequirementsController requirementsController = GetRequirementsController.getInstance();
				// Manually force a population of the list of requirements in the requirement model
				requirementsController.retrieveRequirements();
				// Get the singleton instance of the requirement model to steal it's list of requirements.
				RequirementModel requirementModel = RequirementModel.getInstance();
				try {
					// Steal list of requirements from requirement model muhahaha.
					List<Requirement> reqsList = requirementModel.getRequirements();
					List<Requirement> reqsInBacklog = new LinkedList<Requirement>();
					for (Requirement r:reqsList){
						if (r.getIteration().equals("Backlog")){
							reqsInBacklog.add(r);
						}
					} 
					this.requirements = (LinkedList<Requirement>)reqsInBacklog;
				
				}
				catch (Exception e) {}
	}
	
	/**
	 * This function is used to update the lists and the state of 
	 * the buttons
	 */
	private void update(){
		LinkedList<String> unselectedRequirements = new LinkedList<String>();
		LinkedList<String> selectedRequirements = new LinkedList<String>();
		for (Requirement rqt : this.requirements){
			int pos = this.requirements.indexOf(rqt);
			if (this.selection.get(pos) == true){
				selectedRequirements.add(rqt.getName());
			}
			else{
				unselectedRequirements.add(rqt.getName());
			}
		}
		
		unSelectedListData = unselectedRequirements.toArray(new String[0]);
		selectedListData = selectedRequirements.toArray(new String[0]);
		
		updateUnselectedList();
		updateSelectedList();
		
		validButtons();
		fireRequirementsSelectedEvent();

	}
	
	/**
	 * Update the list model for the unselected list
	 */
	// update the data displayed in the unselected list
	private void updateUnselectedList(){
		this.unSelectedListModel = new AbstractListModel(){
			String[] strings = unSelectedListData;
			public int getSize(){return strings.length;}
			public Object getElementAt(int i){return strings[i];}
		};
		this.unselectedListGui.setModel(unSelectedListModel);
	}
	
	/**
	 * Update the list model for the selected list
	 */
	// update the data displayed by the selected list
	private void updateSelectedList(){
		this.selectedListModel = new AbstractListModel(){
			String[] strings = selectedListData;
			public int getSize(){return strings.length;}
			public Object getElementAt(int i){return strings[i];}
		};
		this.selectedListGui.setModel(selectedListModel);
	}
	
	/**
	 * This function gets the position of each unselected requirement
	 * 
	 * @return list of positions
	 */
	private LinkedList<Integer> getUnselectedPos(){
		LinkedList<Integer> positions = new LinkedList<Integer>();
		for (Requirement rqt : this.requirements){
			int pos = this.requirements.indexOf(rqt);
			if (!this.selection.get(pos)){
				positions.add(pos);
			}
		}
		
		return positions;
	}
	
	/**
	 * This function get the position of each selected requirement
	 * @return
	 */
	private LinkedList<Integer> getSelectedPos(){
		LinkedList<Integer> positions = new LinkedList<Integer>();
		for (Requirement rqt : this.requirements){
			int pos = this.requirements.indexOf(rqt);
			if (this.selection.get(pos)){
				positions.add(pos);
			}
		}
		
		return positions;
	}
	/**
	 * This function takes the requirements that are indicated to 
	 * become selected
	 */
	private void add(){
		LinkedList<Integer> pos = getUnselectedPos();
		int selected[] = unselectedListGui.getSelectedIndices();
		for(int n : selected){
			int position = pos.get(n);
			this.selection.remove(position);
			this.selection.add(position, true);
		}
		numRequirementsAdded += 1;
		update();
	}
	
	/**
	 * This function takes the selected requirements that are
	 * indicated to become unselected
	 */
	private void remove(){
		LinkedList<Integer> pos = getSelectedPos();
		int selected[] = selectedListGui.getSelectedIndices();
		for(int n : selected){
			int position = pos.get(n);
			this.selection.remove(position);
			this.selection.add(position, false);
		}
		numRequirementsAdded -= 1;
		update();

	}
	
	/**
	 * This function makes all of the requirements selected
	 */
	private void selectAll(){
		LinkedList<Integer> pos = getUnselectedPos();
		//int selected[] = unselectedListGui.getSelectedIndices();
		for(int n : pos){
			this.selection.remove(n);
			this.selection.add(n, true);
		}
		numRequirementsAdded = selection.size();
		update();

	}
	
	/**
	 * This function makes all of the requirements unselected
	 */
	private void unselectAll(){
		LinkedList<Integer> pos = getSelectedPos();
		//int selected[] = unselectedListGui.getSelectedIndices();
		for(int n : pos){
			this.selection.remove(n);
			this.selection.add(n, false);
		}
		numRequirementsAdded = 0;
		update();
	}
	
	/**
	 * This function checks to see which buttons can be used and 
	 * deactivates the ones that can't
	 */
	// checks for whether any of the buttons can be used and disables the ones that can't
	private void validButtons(){
		boolean debug = false; // quick disable for console messages
		
		//checks for full lists and disables trying to move from empty lists
		boolean allUnselected = fullList(false);
		boolean allSelected = fullList(true);
		if(allUnselected){
			if(debug){System.out.println("Disableing removeAll");}
			this.btnRemoveAll.setEnabled(false);
		}
		else{
			if(debug){System.out.println("Enableing removeAll");}
			this.btnRemoveAll.setEnabled(true);
		}
		if(allSelected){
			if(debug){System.out.println("Disableing addAll");}
			this.btnAddAll.setEnabled(false);
		}
		else{
			if(debug){System.out.println("Enableing addAll");}
			this.btnAddAll.setEnabled(true);
		}
		
		// checks to see any requirements are selected for moving
		boolean pickedUnselected = anySelected(this.unselectedListGui.getSelectedIndices());
		boolean pickedSelected = anySelected(this.selectedListGui.getSelectedIndices());
		if(pickedUnselected){
			this.btnAdd.setEnabled(true);
		}
		else{
			this.btnAdd.setEnabled(false);
		}
		if(pickedSelected){
			this.btnRemove.setEnabled(true);
		}
		else{
			this.btnRemove.setEnabled(false);
		}
		
		
	}
	
	// checks for full lists
	private boolean fullList(boolean aBool){
		boolean full = true;
		
		for(boolean bool : this.selection){
			if(bool != aBool){
				full = false;
			}
		}
		
		return full;
	}
	
	// checks for any selections from the given array
	private boolean anySelected(int selected[]){
		boolean any = false;
		
		for(int n : selected){
			any = true;
		}
		
		return any;
	}
	
	/**
	 * This function add a new requirement directly to the selected 
	 * list
	 * @param requirement the requirement to be added
	 */
	public void addRequirement(Requirement requirement){
		this.selection.addLast(true);
		this.requirements.addLast(requirement);
		update();
	}
	
	/**
	 * This function returns the requirements that are currently 
	 * selected
	 * @return
	 */
	public List<Requirement> getSelected(){
		List<Requirement> selection = new LinkedList<Requirement>();
		for(Requirement rqt : this.requirements){
			if (this.selection.get(this.requirements.indexOf(rqt))){
				selection.add(rqt);
			}
		}
		
		return selection;
		
	}
	
	/**
	 * This function is used to set the requirements that are 
	 * already selected when editing a session
	 * @param selectedRequirements a set of requirement Id's 
	 */
	public void setSelectedRequirements(Set<Integer> selectedRequirements) {
		numRequirementsAdded = 0;
		for (Integer id : selectedRequirements) {
			Requirement current = RequirementModel.getInstance().getRequirement(id);
			int pos = this.requirements.indexOf(current);
			this.selection.remove(pos);
			this.selection.add(pos, true);
			numRequirementsAdded += 1;
		}
		update();
	}
	
	public void openCreationPanel(){
		parent.openCreationPanel();
	}
	
	public void newRequirementAdded(Requirement newReq){
		populateRequirements();
		populateBooleans();
		int pos = this.requirements.indexOf(newReq);
		this.selection.set(pos, true);
		numRequirementsAdded += 1; //Should probably be integrated better
		update();
	}
	
	synchronized public void addRequirementsSelectedListener(RequirementsSelectedListener l) {
		if (listeners == null) {
			listeners = new Vector<RequirementsSelectedListener>();
		}
		listeners.addElement(l);
	}  

	/**
	 * Remove a listener for RequirementsSelectedEvents
	 */
	synchronized public void removeRequirementsSelectedListener(RequirementsSelectedListener l) {
		if (listeners == null) {
			listeners = new Vector<RequirementsSelectedListener>();
		}
		else {
			listeners.removeElement(l);
		}
	}

	/**
	 * Fire an EstimateEvent to all registered listeners
	 */
	protected void fireRequirementsSelectedEvent() {
		// Do nothing if we have no listeners
		if (listeners != null && !listeners.isEmpty()) {
			// Create the event object to send
			RequirementsSelectedEvent event = 
					new RequirementsSelectedEvent(this, (numRequirementsAdded != 0));

			// Make a copy of the listener list in case anyone adds/removes listeners
			Vector<RequirementsSelectedListener> targets;
			synchronized (this) {
				targets = (Vector<RequirementsSelectedListener>) listeners.clone();
			}

			// Walk through the listener list and call the estimateSubmitted method in each
			Enumeration<RequirementsSelectedListener> e = targets.elements();
			while (e.hasMoreElements()) {
				RequirementsSelectedListener l = (RequirementsSelectedListener) e.nextElement();
				l.setRequirementsSelected(event);
			}
		}
	}
}
