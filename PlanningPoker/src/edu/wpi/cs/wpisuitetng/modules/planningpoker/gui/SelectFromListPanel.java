/*******************************************************************************
 * Copyright (c) 2014 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Team8s
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.planningpoker.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.controller.GetUserController;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.controller.GetRequirementsController;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.RequirementModel;

/**
 * This class is a panel that contains the functionality for 
 * selection elements. This panel allows the user to move 
 * requirements between an unselected and a selected list
 *
 */

public class SelectFromListPanel extends JPanel {

	private List<Requirement> requirements;
	private final LinkedList<Requirement> unSelected;
	private final LinkedList<Requirement> selected;
	private String[] unSelectedListData = {};
	private String[] selectedListData = {};
	private int numRequirementsAdded = 0;
	private AbstractListModel unSelectedListModel;
	private final JList<String> unSelectedGuiList;
	private AbstractListModel selectedListModel;
	private final JList<String> Selected;
	private List<String> selectedNames;
	private List<String> unSelectedNames;
	private final JScrollPane unSelectedScrollPane;
	private final JScrollPane selectedScrollPane;
	private final JButton btnAdd;
	private final JButton btnAddAll;
	private final JButton btnRemove;
	private final JButton btnRemoveAll;
	private transient Vector<RequirementsSelectedListener> listeners;
	
	/**
	 * The constructor for the requirement selection panel
	 */
	public SelectFromListPanel(){
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				validButtons();
			}
		});
		
		unSelectedScrollPane = new JScrollPane();
		unSelectedScrollPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				validButtons();
			}
		});
		selectedScrollPane = new JScrollPane();
		selectedScrollPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				validButtons();
			}
		});
		
		//initialize the main requirement lists
		populateRequirements();
		//System.out.println(this.requirements.size());
		unSelected = new LinkedList<Requirement>();
		selected = new LinkedList<Requirement>();
		//convert the inital data from an array to a list
		for(Requirement rqt : requirements){
			unSelected.add(rqt);
		}
		
		//get the data to populate the initial unselected requirements
		unSelectedListData = getNames(unSelected).toArray(new String[0]);
		
		
		//populated the initial list of selected requirements
		for (Requirement rqt : unSelected){
			selected.add(null);
		}
		
		//initializes the default unselected list data
		unSelectedListModel = new javax.swing.AbstractListModel(){
			private final String[] strings = unSelectedListData;
			public int getSize(){return strings.length;}
			public Object getElementAt(int i){return strings[i];}
		};
		
		// initializes the default selected list data
		selectedListModel = new javax.swing.AbstractListModel(){
			private final String[] strings = selectedListData;
			public int getSize(){return strings.length;}
			public Object getElementAt(int i){return strings[i];}
		};
		setLayout(null);
		
		final JLabel lblUnselectedRequirements = new JLabel("Unselected Requirements");
		lblUnselectedRequirements.setBounds(11, 20, 200, 14);
		add(lblUnselectedRequirements);
		
		final JLabel lblSelectedRequirements = new JLabel("Selected Requirements");
		lblSelectedRequirements.setBounds(288, 20, 200, 14);
		add(lblSelectedRequirements);
		
		// initializes the unselected list
		unSelectedGuiList = new JList<String>();
		unSelectedGuiList.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				validButtons();
			}
		});
		unSelectedScrollPane.setBounds(11, 45, 200, 150);
		unSelectedScrollPane.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		unSelectedGuiList.setModel(unSelectedListModel);
		unSelectedGuiList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		add(unSelectedScrollPane);
		unSelectedScrollPane.setViewportView(unSelectedGuiList);
		
		// initializes the selected list
		Selected = new JList<String>();
		Selected.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				validButtons();
			}
		});
		selectedScrollPane.setBounds(288, 45, 200, 150);
		selectedScrollPane.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		Selected.setModel(selectedListModel);
		Selected.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		add(selectedScrollPane);
		selectedScrollPane.setViewportView(Selected);
		
		// creates the remove button and attaches functionality
		btnRemove = new JButton("<");
		btnRemove.setBounds(221, 131, 57, 23);
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				remove();
			}
		});
		
		// creates the add button and attaches functionality
		btnAdd = new JButton(">");
		btnAdd.setBounds(221, 89, 57, 23);
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				add();
			}
		});
		add(btnAdd);
		add(btnRemove);
		
		btnAddAll = new JButton(">>");
		btnAddAll.setBounds(221, 57, 57, 23);
		btnAddAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				addAll();
			}
		});
		add(btnAddAll);
		
		btnRemoveAll = new JButton("<<");
		btnRemoveAll.setBounds(221, 165, 57, 23);
		btnRemoveAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				unSelectAll();
			}
		});
		add(btnRemoveAll);
		
		update();
	}
	
	// refreshes the data from the lists of requirements
	private void update(){
		//System.out.println("In Update");
		//section updates the unselected list object
		unSelectedNames = getNames(unSelected);
		final LinkedList<String> unSelectedNonNull = new LinkedList<String>();
		for(String str : unSelectedNames){
			if (str != null){
				unSelectedNonNull.addLast(str);
			}
		}
		unSelectedListData = unSelectedNonNull.toArray(new String[0]);
		selectedNames = getNames(selected);
		final LinkedList<String> selectedNonNull = new LinkedList<String>();
		for(String str : selectedNames){
			if (str != null){
				selectedNonNull.addLast(str);
			}
		}
		selectedListData = selectedNonNull.toArray(new String[0]);
		
		updateUnselectedList();
		updateSelectedList();
		validButtons();
		this.updateUI();
		fireRequirementsSelectedEvent();
	}
	
	/*
	 * moves the requirements that are highlighted in the unselected list to
	 * the selected list
	 */
	private void add(){
		final int selected[] = unSelectedGuiList.getSelectedIndices();
		final List<String> name = new LinkedList<String>();
		for(int n : selected){
			String str = unSelectedListData[n];
			name.add(str);
		}
		for(String str : name){
			//this.selected.add(n, this.unSelected.get(n));
			int pos = unSelectedNames.indexOf(str);
			if (unSelected.get(pos) != null){
				Requirement element = unSelected.get(pos);
				//this.unSelected.add(n, null);
				//System.out.println(element);
				if (!this.selected.contains(element)){
					this.selected.remove(pos);
					this.selected.add(pos, element);
					unSelected.remove(pos);
					unSelected.add(pos, null);
					update();
					numRequirementsAdded += 1;
				}
			}
		}
		update();
		
	}
	
	private void addAll(){
		for(int i = 0; i < unSelected.size(); i++){
			int pos = i;
			Requirement element = unSelected.get(pos);
			if (element != null){
				selected.remove(pos);
				selected.add(pos, element);
				unSelected.remove(pos);
				unSelected.add(pos, null);
			}
		}
		numRequirementsAdded = selected.size();
		update();
	}
	
	/*
	 * moves the requirements that are highlighted from the selected list to 
	 * the unselected list
	 */
	private void remove(){
		final int selected[] = Selected.getSelectedIndices();
		final List<String> name = new LinkedList<String>();
		for(int n : selected){
			String str = selectedListData[n];
			name.add(str);
		}
		for(String str : name){
			//System.out.print(n);
			//this.selected.add(n, this.unSelected.get(n));
			//String str = this.selectedListData[n];
			int pos = selectedNames.indexOf(str);
			if (this.selected.get(pos) != null){
				Requirement element = this.selected.get(pos);
				//this.unSelected.add(n, null);
				//System.out.println(element);
				if (!unSelected.contains(element)){
					unSelected.remove(pos);
					unSelected.add(pos, element);
					this.selected.remove(pos);
					this.selected.add(pos, null);
					update();
					numRequirementsAdded -= 1;
				}
			}
		}
		update();
		
	}
	
	private void unSelectAll(){
		for(int i = 0; i < selected.size(); i++){
			int pos = i;
			Requirement element = selected.get(pos);
			if (element != null){
				unSelected.remove(pos);
				unSelected.add(pos, element);
				selected.remove(pos);
				selected.add(pos, null);
			}
		}
		numRequirementsAdded = 0;
		update();
		
	}
	
	// get the name to be displayed by the list from the list of requirements given
	private List<String> getNames(LinkedList<Requirement> list){
		//System.out.println("In Get Names");
		final List<String> rList = new LinkedList<String>();
		for(int i = 0; i < list.size(); i++){
			//System.out.print(i + ":");
			//rList.add(list.element());
			Requirement element = list.pollFirst();
			if (element == null){
				//System.out.println("null");
			}
			String str;
			try{
				str = element.getName();
			}
			catch(Exception e){
				str = null;
			}
			//System.out.println(str);
			rList.add(str);
			list.addLast(element);
			
		}
		return rList;
	}
	
	// update the data displayed in the unselected list
	private void updateUnselectedList(){
		unSelectedListModel = new AbstractListModel(){
			private final String[] strings = unSelectedListData;
			public int getSize(){return strings.length;}
			public Object getElementAt(int i){return strings[i];}
		};
		unSelectedGuiList.setModel(unSelectedListModel);	
	}
	
	// update the data displayed by the selected list
	private void updateSelectedList(){
		selectedListModel = new AbstractListModel(){
			private final String[] strings = selectedListData;
			public int getSize(){return strings.length;}
			public Object getElementAt(int i){return strings[i];}
		};
		Selected.setModel(selectedListModel);
	}
	
	/**
	 * Function used to add a new requirement directList<String> list
	 * @param requirement the requirement to add
	 */
	public void addRequirement(Requirement requirement){
		unSelected.addLast(null);
		selected.addLast(requirement);
		update();
	}
	
	/**
	 * Function to get the requirements that are currently selected
	 * @return list of requirements that are selected
	 */
	public List<Requirement> getSelected(){
		final List<Requirement> selection = new LinkedList<Requirement>();
		for(Requirement str : selected) {
			if (str != null) {
				selection.add(str);
			}
		}
		
		return selection;
		
	}
			
	/**
	 * @param selected the list of requirements that are selected
	 */
	public void setSelectedRequirements(Set<Integer> selectedRequirements) {
		numRequirementsAdded = 0;
		for (Integer id : selectedRequirements) {
			Requirement current = RequirementModel.getInstance().getRequirement(id);
			int pos = unSelectedNames.indexOf(current.getName());
			if (unSelected.get(pos) != null) {
				Requirement element = unSelected.get(pos);
				if (!selected.contains(element)) {
					selected.remove(pos);
					selected.add(pos, element);
					unSelected.remove(pos);
					unSelected.add(pos, null);
					numRequirementsAdded++;
				}
			}
		}
		update();
	}
	
	/**
	 * populate PlanningPokerSession list of requirements
	 */
	public void populateRequirements() {
		//System.out.println("In Populate Requirements");
		
		// Get singleton instance of Requirements Controller
		final GetRequirementsController requirementsController = GetRequirementsController.getInstance();
		// Manually force a population of the list of requirements in the requirement model
		requirementsController.retrieveRequirements();
		GetUserController.getInstance().retrieveUsers();
		// Get the singleton instance of the requirement model to steal it's list of requirements.
		final RequirementModel requirementModel = RequirementModel.getInstance();
		try {
			// Steal list of requirements from requirement model muhahaha.
			final List<Requirement> reqsList = requirementModel.getRequirements();
			final List<Requirement> reqsInBacklog = new LinkedList<Requirement>();
			for (Requirement r:reqsList){
				if (r.getIteration().equals("Backlog")){
					reqsInBacklog.add(r);
				}
			} 
			requirements = reqsInBacklog;
		
		}
		catch (Exception e) {}
	}
	
	// checks for whether any of the buttons can be used and disables the ones that can't
	private void validButtons(){
		final boolean debug = false; // quick disable for console messages
		
		//checks for full lists and disables trying to move from empty lists
		final boolean allUnselected = fullList(unSelected);
		final boolean allSelected = fullList(selected);
		if(allUnselected){
			if(debug){System.out.println("Disableing removeAll");}
			btnRemoveAll.setEnabled(false);
		}
		else{
			if(debug){System.out.println("Enableing removeAll");}
			btnRemoveAll.setEnabled(true);
		}
		if(allSelected){
			if(debug){System.out.println("Disableing addAll");}
			btnAddAll.setEnabled(false);
		}
		else{
			if(debug){System.out.println("Enableing addAll");}
			btnAddAll.setEnabled(true);
		}
		
		// checks to see any requirements are selected for moving
		final boolean pickedUnselected = anySelected(unSelectedGuiList.getSelectedIndices());
		final boolean pickedSelected = anySelected(Selected.getSelectedIndices());
		if(pickedUnselected){
			btnAdd.setEnabled(true);
		}
		else{
			btnAdd.setEnabled(false);
		}
		if(pickedSelected){
			btnRemove.setEnabled(true);
		}
		else{
			btnRemove.setEnabled(false);
		}
		
		
	}
	
	// checks for full lists
	private boolean fullList(List<Requirement> list){
		boolean full = true;
		
		for(Requirement rqt : list){
			if(rqt == null){
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
	 * Register a listener for RequirementsSelectedEvents
	 */

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
			final RequirementsSelectedEvent event = 
					new RequirementsSelectedEvent(this, (numRequirementsAdded != 0));

			// Make a copy of the listener list in case anyone adds/removes listeners
			final Vector<RequirementsSelectedListener> targets;
			synchronized (this) {
				targets = (Vector<RequirementsSelectedListener>) listeners.clone();
			}

			// Walk through the listener list and call the estimateSubmitted method in each
			final Enumeration<RequirementsSelectedListener> e = targets.elements();
			while (e.hasMoreElements()) {
				RequirementsSelectedListener l = (RequirementsSelectedListener) e.nextElement();
				l.setRequirementsSelected(event);
			}
		}
	}
}
