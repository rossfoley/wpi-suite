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
	
	private LinkedList<String> unSelected;
	private LinkedList<String> selected;
	private String[] unSelectedListData = {};
	private String[] selectedListData = {};
	private javax.swing.AbstractListModel unSelectedListModel;
	private javax.swing.JList<String> Unselected;
	private javax.swing.AbstractListModel selectedListModel;
	private javax.swing.JList<String> Selected;
	
	SelectFromListPanel(LinkedList<String> unSelected){
		SpringLayout springLayout = new SpringLayout();
		setLayout(springLayout);
		this.unSelected = unSelected;
		this.selected = new LinkedList<String>();
		unSelectedListData = getNames(this.unSelected).toArray(new String[0]);
		
		this.unSelectedListModel = new javax.swing.AbstractListModel(){
			String[] strings = unSelectedListData;
			public int getSize(){return strings.length;}
			public Object getElementAt(int i){return strings[i];}
		};
		//this.unSelectedListModel.addListDataListener(new ListDataListener);
		
		this.selectedListModel = new javax.swing.AbstractListModel(){
			String[] strings = selectedListData;
			public int getSize(){return strings.length;}
			public Object getElementAt(int i){return strings[i];}
		};
		
		
		this.Unselected = new JList<String>();
		Unselected.setModel(unSelectedListModel);
		springLayout.putConstraint(SpringLayout.NORTH, Unselected, 10, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, Unselected, 10, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.SOUTH, Unselected, 355, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.EAST, Unselected, 203, SpringLayout.WEST, this);
		Unselected.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		add(Unselected);
		
		this.Selected = new JList<String>();
		Selected.setModel(selectedListModel);
		springLayout.putConstraint(SpringLayout.NORTH, Selected, 10, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, Selected, -225, SpringLayout.EAST, this);
		springLayout.putConstraint(SpringLayout.SOUTH, Selected, 355, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.EAST, Selected, -10, SpringLayout.EAST, this);
		Selected.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		add(Selected);
		
		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				add();
			}
		});
		springLayout.putConstraint(SpringLayout.NORTH, btnAdd, 152, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, btnAdd, 24, SpringLayout.EAST, Unselected);
		add(btnAdd);
		
		JButton btnRemove = new JButton("Remove");
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		springLayout.putConstraint(SpringLayout.NORTH, btnRemove, 13, SpringLayout.SOUTH, btnAdd);
		springLayout.putConstraint(SpringLayout.WEST, btnRemove, 0, SpringLayout.WEST, btnAdd);
		add(btnRemove);
		//JPanel panel = new JPanel();
		update();
	}
	
	private void update(){
		System.out.println("In Update");
		//section updates the unselected list object
		LinkedList<String> unSelectedNames = getNames(unSelected);
		//unSelectedListModel.
		//for(int i = 0; i < unSelectedNames.size(); i++){	
		//}
		unSelectedListData = unSelectedNames.toArray(new String[0]);
		LinkedList<String> selectedNames = getNames(selected);
		selectedListData = selectedNames.toArray(new String[0]);
		
		this.updateUI();
		
	}
	
	private void add(){
		System.out.println("In Add");
		int selected[] = Unselected.getSelectedIndices();
		for(int n : selected){
			System.out.print(n);
			//this.selected.add(n, this.unSelected.get(n));
			if (this.unSelected.get(n) != null){
				String element = this.unSelected.get(n);
				this.unSelected.add(n, null);
				System.out.println(element);
				this.selected.addLast(element);
			}
		}
		update();
		
	}
	
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
	
	
	LinkedList<String> getSelected(){
		return selected;
		
	}

}
