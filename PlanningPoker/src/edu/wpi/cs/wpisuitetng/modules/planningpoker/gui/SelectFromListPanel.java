package edu.wpi.cs.wpisuitetng.modules.planningpoker.gui;

import javax.swing.DefaultListModel;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.SpringLayout;
import javax.swing.JList;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.LinkedList;

public class SelectFromListPanel extends JPanel{
	
	// define variables
	private LinkedList<String> unSelected;
	private LinkedList<String> selected;
	private int defaultSize;
	private String[] unSelectedListData = {};
	private String[] selectedListData = {};
	private javax.swing.AbstractListModel unSelectedListModel;
	private javax.swing.JList<String> unSelectedGuiList;
	private javax.swing.AbstractListModel selectedListModel;
	private javax.swing.JList<String> Selected;
	private LinkedList<String> selectedNames;
	private LinkedList<String> unSelectedNames;
	
	SelectFromListPanel(String[] unSelected){
		//set panel layout
		SpringLayout springLayout = new SpringLayout();
		setLayout(springLayout);
		//initialize the main requirement lists
		this.unSelected = new LinkedList<String>();
		this.selected = new LinkedList<String>();
		//convert the inital data from an array to a list
		for(String str : unSelected){
			this.unSelected.addLast(str);
		}
		
		//get the data to populate the initial unselected requirements
		unSelectedListData = getNames(this.unSelected).toArray(new String[0]);
		
		
		//defaultSize = this.unSelected.size();
		
		//populated the initial list of selected requirements
		for (String str : this.unSelected){
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
		
		// initializes the unselected list
		this.unSelectedGuiList = new JList<String>();
		unSelectedGuiList.setModel(unSelectedListModel);
		springLayout.putConstraint(SpringLayout.NORTH, unSelectedGuiList, 10, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, unSelectedGuiList, 10, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.SOUTH, unSelectedGuiList, 355, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.EAST, unSelectedGuiList, 203, SpringLayout.WEST, this);
		unSelectedGuiList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		add(unSelectedGuiList);
		
		// initializes the selected list
		this.Selected = new JList<String>();
		Selected.setModel(selectedListModel);
		springLayout.putConstraint(SpringLayout.NORTH, Selected, 10, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, Selected, -225, SpringLayout.EAST, this);
		springLayout.putConstraint(SpringLayout.SOUTH, Selected, 355, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.EAST, Selected, -10, SpringLayout.EAST, this);
		Selected.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		add(Selected);
		
		// creates the add button and attaches functionality
		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				add();
			}
		});
		springLayout.putConstraint(SpringLayout.NORTH, btnAdd, 152, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, btnAdd, 24, SpringLayout.EAST, unSelectedGuiList);
		add(btnAdd);
		
		// creates the remove button and attaches functionality
		JButton btnRemove = new JButton("Remove");
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				remove();
			}
		});
		springLayout.putConstraint(SpringLayout.NORTH, btnRemove, 13, SpringLayout.SOUTH, btnAdd);
		springLayout.putConstraint(SpringLayout.WEST, btnRemove, 0, SpringLayout.WEST, btnAdd);
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
				String element = this.unSelected.get(pos);
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
				String element = this.selected.get(pos);
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
	private LinkedList<String> getNames(LinkedList<String> list){
		System.out.println("In Get Names");
		LinkedList<String> rList = new LinkedList<String>();
		for(int i = 0; i < list.size(); i++){
			System.out.print(i + ":");
			rList.add(list.element());
			String element = list.pollFirst();
			System.out.println(element);
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
	
	//add a requirement to the list after the object has been created 
	void addRequirement(String requirement){
		unSelected.addLast(null);
		selected.addLast(requirement);
		update();
	}
	
	// method to get the list of selected requirements
	String[] getSelected(){
		LinkedList<String> selection = new LinkedList<String>();
		for(String str : selected){
			if (str != null){
				selection.addFirst(str);
			}
		}
		
		return selection.toArray(new String[0]);
		
	}
}
