package edu.wpi.cs.wpisuitetng.modules.planningpoker.gui;

import javax.swing.DefaultListModel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.JList;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.LinkedList;

public class SelectFromListPanel extends JPanel{
	
	private LinkedList<String> unSelected;
	private LinkedList<String> selected;
	private javax.swing.AbstractListModel unSelectedListModel;
	private javax.swing.JList<String> Unselected;
	
	SelectFromListPanel(LinkedList<String> unSelected){
		SpringLayout springLayout = new SpringLayout();
		setLayout(springLayout);
		this.unSelected = unSelected;
		
		this.unSelectedListModel = new javax.swing.AbstractListModel(){
			String[] strings = {  };
			public int getSize(){return strings.length;}
			public Object getElementAt(int i){return strings[i];}
		};
		
		this.Unselected = new JList<String>();
		Unselected.setModel(unSelectedListModel);
		springLayout.putConstraint(SpringLayout.NORTH, Unselected, 10, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, Unselected, 10, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.SOUTH, Unselected, 355, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.EAST, Unselected, 203, SpringLayout.WEST, this);
		add(Unselected);
		
		JList<String> Selected = new JList<String>();
		springLayout.putConstraint(SpringLayout.NORTH, Selected, 10, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, Selected, -225, SpringLayout.EAST, this);
		springLayout.putConstraint(SpringLayout.SOUTH, Selected, 355, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.EAST, Selected, -10, SpringLayout.EAST, this);
		add(Selected);
		
		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
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
	}
	
	private void update(){
		LinkedList<String> unSelectedNames = getNames();
		
	}
	
	private LinkedList<String> getNames(){
		return unSelected;
	}
	
	
	LinkedList<String> getSelected(){
		return selected;
		
	}

}
