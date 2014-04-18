package edu.wpi.cs.wpisuitetng.modules.planningpoker.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.voting.EstimateEvent;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.voting.EstimateListener;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.controller.GetRequirementsController;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.RequirementModel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * This class is a panel that contains the functionality for 
 * selection elements. This panel allows the user to move 
 * requirements between an unselected and a selected list
 *
 */

public class SelectFromListPanel extends JPanel {
	
	// define variables
	private List<Requirement> requirements;
	private LinkedList<Requirement> unSelected;
	private LinkedList<Requirement> selected;
	private String[] unSelectedListData = {};
	private String[] selectedListData = {};
	private int numRequirementsAdded = 0;
	private AbstractListModel unSelectedListModel;
	private JList<String> unSelectedGuiList;
	private AbstractListModel selectedListModel;
	private javax.swing.JList<String> Selected;
	private LinkedList<String> selectedNames;
	private LinkedList<String> unSelectedNames;
	private JScrollPane unSelectedScrollPane;
	private JScrollPane selectedScrollPane;
	private JButton btnAdd;
	private JButton btnAddAll;
	private JButton btnRemove;
	private JButton btnRemoveAll;
	private transient Vector<RequirementsSelectedListener> listeners;
	
	/**
	 * The constructor for the requirement selection panel
	 */
	SelectFromListPanel(){
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
		this.unSelected = new LinkedList<Requirement>();
		this.selected = new LinkedList<Requirement>();
		//convert the inital data from an array to a list
		for(Requirement rqt : requirements){
			this.unSelected.add(rqt);
		}
		
		//get the data to populate the initial unselected requirements
		unSelectedListData = getNames(this.unSelected).toArray(new String[0]);
		
		
		//populated the initial list of selected requirements
		for (Requirement rqt : this.unSelected){
			this.selected.add(null);
		}
		
		//initializes the default unselected list data
		this.unSelectedListModel = new javax.swing.AbstractListModel(){
			String[] strings = unSelectedListData;
			public int getSize(){return strings.length;}
			public Object getElementAt(int i){return strings[i];}
		};
		
		// initializes the default selected list data
		this.selectedListModel = new javax.swing.AbstractListModel(){
			String[] strings = selectedListData;
			public int getSize(){return strings.length;}
			public Object getElementAt(int i){return strings[i];}
		};
		setLayout(null);
		
		JLabel lblUnselectedRequirements = new JLabel("Unselected Requirements");
		lblUnselectedRequirements.setBounds(11, 20, 200, 14);
		add(lblUnselectedRequirements);
		
		JLabel lblSelectedRequirements = new JLabel("Selected Requirements");
		lblSelectedRequirements.setBounds(288, 20, 200, 14);
		add(lblSelectedRequirements);
		
		// initializes the unselected list
		this.unSelectedGuiList = new JList<String>();
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
		this.Selected = new JList<String>();
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
		LinkedList<String> unSelectedNonNull = new LinkedList<String>();
		for(String str : unSelectedNames){
			if (str != null){
				unSelectedNonNull.addLast(str);
			}
		}
		unSelectedListData = unSelectedNonNull.toArray(new String[0]);
		selectedNames = getNames(selected);
		LinkedList<String> selectedNonNull = new LinkedList<String>();
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
		int selected[] = unSelectedGuiList.getSelectedIndices();
		LinkedList<String> name = new LinkedList<String>();
		for(int n : selected){
			String str = this.unSelectedListData[n];
			name.add(str);
		}
		for(String str : name){
			//this.selected.add(n, this.unSelected.get(n));
			int pos = this.unSelectedNames.indexOf(str);
			if (this.unSelected.get(pos) != null){
				Requirement element = this.unSelected.get(pos);
				//this.unSelected.add(n, null);
				//System.out.println(element);
				if (!this.selected.contains(element)){
					this.selected.remove(pos);
					this.selected.add(pos, element);
					this.unSelected.remove(pos);
					this.unSelected.add(pos, null);
					update();
					numRequirementsAdded += 1;
				}
			}
		}
		update();
		
	}
	
	private void addAll(){
		for(int i = 0; i < this.unSelected.size(); i++){
			int pos = i;
			Requirement element = this.unSelected.get(pos);
			if (element != null){
				this.selected.remove(pos);
				this.selected.add(pos, element);
				this.unSelected.remove(pos);
				this.unSelected.add(pos, null);
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
		int selected[] = Selected.getSelectedIndices();
		LinkedList<String> name = new LinkedList<String>();
		for(int n : selected){
			String str = this.selectedListData[n];
			name.add(str);
		}
		for(String str : name){
			//System.out.print(n);
			//this.selected.add(n, this.unSelected.get(n));
			//String str = this.selectedListData[n];
			int pos = this.selectedNames.indexOf(str);
			if (this.selected.get(pos) != null){
				Requirement element = this.selected.get(pos);
				//this.unSelected.add(n, null);
				//System.out.println(element);
				if (!this.unSelected.contains(element)){
					this.unSelected.remove(pos);
					this.unSelected.add(pos, element);
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
		for(int i = 0; i < this.selected.size(); i++){
			int pos = i;
			Requirement element = this.selected.get(pos);
			if (element != null){
				this.unSelected.remove(pos);
				this.unSelected.add(pos, element);
				this.selected.remove(pos);
				this.selected.add(pos, null);
			}
		}
		numRequirementsAdded = 0;
		update();
		
	}
	
	// get the name to be displayed by the list from the list of requirements given
	private LinkedList<String> getNames(LinkedList<Requirement> list){
		//System.out.println("In Get Names");
		LinkedList<String> rList = new LinkedList<String>();
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
		this.unSelectedListModel = new AbstractListModel(){
			String[] strings = unSelectedListData;
			public int getSize(){return strings.length;}
			public Object getElementAt(int i){return strings[i];}
		};
		this.unSelectedGuiList.setModel(unSelectedListModel);	
	}
	
	// update the data displayed by the selected list
	private void updateSelectedList(){
		this.selectedListModel = new AbstractListModel(){
			String[] strings = selectedListData;
			public int getSize(){return strings.length;}
			public Object getElementAt(int i){return strings[i];}
		};
		this.Selected.setModel(selectedListModel);
	}
	
	//convert the data given into a linked list
	private LinkedList<String> getData(List<String> data){
		LinkedList<String> list = new LinkedList<String>();
		
		String[] tempList = data.toArray(new String[0]);
		for (String element : tempList){
			list.addLast(element);
		}
		
		return list;
	}
	
	/**
	 * Function used to add a new requirement directly to the selected list
	 * @param requirement the requirement to add
	 */
	//add a requirement to the list after the object has been created 
	public void addRequirement(Requirement requirement){
		unSelected.addLast(null);
		selected.addLast(requirement);
		update();
	}
	
	/**
	 * Function to get the requirements that are currently selected
	 * @return list of requirements that are selected
	 */
	// method to get the list of selected requirements
	public List<Requirement> getSelected(){
		List<Requirement> selection = new LinkedList<Requirement>();
		for(Requirement str : selected){
			if (str != null){
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
			int pos = this.unSelectedNames.indexOf(current.getName());
			if (this.unSelected.get(pos) != null) {
				Requirement element = this.unSelected.get(pos);
				if (!this.selected.contains(element)) {
					this.selected.remove(pos);
					this.selected.add(pos, element);
					this.unSelected.remove(pos);
					this.unSelected.add(pos, null);
					numRequirementsAdded += 1;
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
			this.requirements = reqsInBacklog;
		
		}
		catch (Exception e) {}
	}
	
	// checks for whether any of the buttons can be used and disables the ones that can't
	private void validButtons(){
		boolean debug = false; // quick disable for console messages
		
		//checks for full lists and disables trying to move from empty lists
		boolean allUnselected = fullList(this.unSelected);
		boolean allSelected = fullList(this.selected);
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
		boolean pickedUnselected = anySelected(this.unSelectedGuiList.getSelectedIndices());
		boolean pickedSelected = anySelected(this.Selected.getSelectedIndices());
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
	private boolean fullList(LinkedList<Requirement> list){
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
