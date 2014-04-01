package edu.wpi.cs.wpisuitetng.modules.planningpoker.gui;

import javax.swing.DefaultListModel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SpringLayout;
import javax.swing.JList;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JLabel;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Component;

import javax.swing.Box;
import javax.swing.border.LineBorder;

import edu.wpi.cs.wpisuitetng.modules.requirementmanager.controller.GetRequirementsController;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.RequirementModel;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;

import java.awt.Color;



public class SelectFromListPanel extends JPanel{
	
	// define variables
	private List<Requirement> requirements;
	private LinkedList<Requirement> unSelected;
	private LinkedList<Requirement> selected;
	private int defaultSize;
	private String[] unSelectedListData = {};
	private String[] selectedListData = {};
	private javax.swing.AbstractListModel unSelectedListModel;
	private javax.swing.JList<String> unSelectedGuiList;
	private javax.swing.AbstractListModel selectedListModel;
	private javax.swing.JList<String> Selected;
	private LinkedList<String> selectedNames;
	private LinkedList<String> unSelectedNames;
	private JScrollPane unSelectedScrollPane;
	private JScrollPane selectedScrollPane;
	
	SelectFromListPanel(){
		
		unSelectedScrollPane = new JScrollPane();
		selectedScrollPane = new JScrollPane();
		
		//initialize the main requirement lists
		populateRequirements();
		this.unSelected = new LinkedList<Requirement>();
		this.selected = new LinkedList<Requirement>();
		//convert the inital data from an array to a list
		for(Requirement rqt : unSelected){
			this.unSelected.addLast(rqt);
		}
		
		//get the data to populate the initial unselected requirements
		unSelectedListData = getNames(this.unSelected).toArray(new String[0]);
		
		
		//defaultSize = this.unSelected.size();
		
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
		//this.unSelectedListModel.addListDataListener(new ListDataListener);
		
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
		unSelectedScrollPane.setBounds(11, 45, 200, 150);
		unSelectedScrollPane.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		unSelectedGuiList.setModel(unSelectedListModel);
		unSelectedGuiList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		add(unSelectedScrollPane);
		unSelectedScrollPane.setViewportView(unSelectedGuiList);
		
		// initializes the selected list
		this.Selected = new JList<String>();
		selectedScrollPane.setBounds(288, 45, 200, 150);
		selectedScrollPane.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		Selected.setModel(selectedListModel);
		Selected.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		add(selectedScrollPane);
		selectedScrollPane.setViewportView(Selected);
		
		// creates the remove button and attaches functionality
		JButton btnRemove = new JButton("<<");
		btnRemove.setBounds(221, 131, 57, 23);
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				remove();
			}
		});
		
		// creates the add button and attaches functionality
		JButton btnAdd = new JButton(">>");
		btnAdd.setBounds(221, 89, 57, 23);
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				add();
			}
		});
		add(btnAdd);
		add(btnRemove);
		//JPanel panel = new JPanel();
		
		update();
	}
	
	// refreshes the data from the lists of requirements
	private void update(){
		System.out.println("In Update");
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
		//this.Selected.updateUI();
		this.updateUI();
		
	}
	
	/*
	 * moves the requirements that are highlighted in the unselected list to
	 * the selected list
	 */
	private void add(){
		System.out.println("In Add");
		int selected[] = unSelectedGuiList.getSelectedIndices();
		for(int n : selected){
			System.out.print(n);
			//this.selected.add(n, this.unSelected.get(n));
			String str = this.unSelectedListData[n];
			int pos = this.unSelectedNames.indexOf(str);
			if (this.unSelected.get(pos) != null){
				Requirement element = this.unSelected.get(pos);
				//this.unSelected.add(n, null);
				System.out.println(element);
				if (!this.selected.contains(element)){
					
					this.selected.remove(pos);
					this.selected.add(pos, element);
					this.unSelected.remove(pos);
					this.unSelected.add(pos, null);
				}
			}
		}
		update();
		
	}
	
	/*
	 * moves the requirements that are highlighted from the selected list to 
	 * the unselected list
	 */
	private void remove(){
		System.out.println("In Remove");
		int selected[] = Selected.getSelectedIndices();
		for(int n : selected){
			System.out.print(n);
			//this.selected.add(n, this.unSelected.get(n));
			String str = this.selectedListData[n];
			int pos = this.selected.indexOf(str);
			if (this.selected.get(pos) != null){
				Requirement element = this.selected.get(pos);
				//this.unSelected.add(n, null);
				System.out.println(element);
				if (!this.unSelected.contains(element)){
					this.unSelected.remove(pos);
					this.unSelected.add(pos, element);
					this.selected.remove(pos);
					this.selected.add(pos, null);
				}
			}
		}
		update();
		
	}
	
	// get the name to be displayed by the list from the list of requirements given
	private LinkedList<String> getNames(LinkedList<Requirement> list){
		System.out.println("In Get Names");
		LinkedList<String> rList = new LinkedList<String>();
		for(int i = 0; i < list.size(); i++){
			System.out.print(i + ":");
			//rList.add(list.element());
			Requirement element = list.pollFirst();
			String str = element.getName();
			System.out.println(str);
			rList.add(str);
			list.addLast(element);
			
		}
		return rList;
	}
	
	// update the data displayed in the unselected list
	void updateUnselectedList(){
		
		this.unSelectedListModel = new javax.swing.AbstractListModel(){
			String[] strings = unSelectedListData;
			public int getSize(){return strings.length;}
			public Object getElementAt(int i){return strings[i];}
		};
		
		
		this.unSelectedGuiList.setModel(unSelectedListModel);
		
		
	}
	
	// update the data displayed by the selected list
	void updateSelectedList(){
		
		this.selectedListModel = new javax.swing.AbstractListModel(){
			String[] strings = selectedListData;
			public int getSize(){return strings.length;}
			public Object getElementAt(int i){return strings[i];}
		};
		
		
		this.Selected.setModel(selectedListModel);
		
		
	}
	
	//convert the data given into a linked list
	LinkedList<String> getData(List<String> data){
		LinkedList<String> list = new LinkedList<String>();
		
		String[] tempList = data.toArray(new String[0]);
		for (String element : tempList){
			list.addLast(element);
		}
		
		return list;
	}
	
	//add a requirement to the list after the object has been created 
	void addRequirement(Requirement requirement){
		unSelected.addLast(null);
		selected.addLast(requirement);
		update();
	}
	
	// method to get the list of selected requirements
	List<Requirement> getSelected(){
		List<Requirement> selection = new LinkedList<Requirement>();
		for(Requirement str : selected){
			if (str != null){
				selection.add(str);
			}
		}
		
		return selection;
		
	}
	
	/**
	 * populate PlanningPokerSession list of requirements
	 */
	public void populateRequirements() {
		/*
		// Get singleton instance of Requirements Controller
		GetRequirementsController requirementsController = GetRequirementsController.getInstance();
		// Manually force a population of the list of requirements in the requirement model
		requirementsController.retrieveRequirements();
		// Get the singleton instance of the requirement model to steal it's list of requirements.
		RequirementModel requirementModel = RequirementModel.getInstance();
		// Steal list of requirements from requirement model muhahaha.
		//this.requirements = requirementModel.getRequirements();
		//*/
	}
}
