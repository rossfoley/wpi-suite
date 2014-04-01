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
import java.util.List;

import javax.swing.JLabel;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Component;

import javax.swing.Box;
import javax.swing.border.LineBorder;
import java.awt.Color;



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
	
	SelectFromListPanel(List<String> unSelected){
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
		setLayout(null);
		
		JLabel lblUnselectedRequirements = new JLabel("Unselected Requirements");
		lblUnselectedRequirements.setBounds(11, 20, 200, 14);
		add(lblUnselectedRequirements);
		
		JLabel lblSelectedRequirements = new JLabel("Selected Requirements");
		lblSelectedRequirements.setBounds(222, 20, 200, 14);
		add(lblSelectedRequirements);
		
		// initializes the unselected list
		this.unSelectedGuiList = new JList<String>();
		unSelectedGuiList.setBounds(11, 45, 200, 150);
		unSelectedGuiList.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		unSelectedGuiList.setModel(unSelectedListModel);
		unSelectedGuiList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		add(unSelectedGuiList);
		
		// initializes the selected list
		this.Selected = new JList<String>();
		Selected.setBounds(222, 45, 200, 150);
		Selected.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		Selected.setModel(selectedListModel);
		Selected.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		add(Selected);
		
		// creates the remove button and attaches functionality
		JButton btnRemove = new JButton("Remove");
		btnRemove.setBounds(222, 206, 200, 23);
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				remove();
			}
		});
		
		// creates the add button and attaches functionality
		JButton btnAdd = new JButton("Add");
		btnAdd.setBounds(11, 206, 200, 23);
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				add();
			}
		});
		add(btnAdd);
		add(btnRemove);
		
		Component verticalStrut = Box.createVerticalStrut(20);
		verticalStrut.setBounds(169, 0, 18, 46);
		add(verticalStrut);
		
		Component verticalStrut_1 = Box.createVerticalStrut(20);
		verticalStrut_1.setBounds(189, 194, 11, 46);
		add(verticalStrut_1);
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
	void addRequirement(String requirement){
		unSelected.addLast(null);
		selected.addLast(requirement);
		update();
	}
	
	// method to get the list of selected requirements
	List<String> getSelected(){
		List<String> selection = new LinkedList<String>();
		for(String str : selected){
			if (str != null){
				selection.add(str);
			}
		}
		
		return selection;
		
	}
}
