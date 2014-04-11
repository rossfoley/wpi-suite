package edu.wpi.cs.wpisuitetng.modules.planningpoker.gui;

import javax.swing.AbstractListModel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.Box;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JButton;

import edu.wpi.cs.wpisuitetng.modules.requirementmanager.controller.GetRequirementsController;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.RequirementModel;

import java.awt.Component;
import java.util.LinkedList;
import java.util.List;

public class RequirementSelectionPanel extends JPanel{
	
	private LinkedList<Requirement> requirements;
	private LinkedList<Boolean> selection;
	private JList selectedListGui;
	private JList unselectedListGui;
	private AbstractListModel unSelectedListModel;
	private AbstractListModel selectedListModel;
	protected String[] unSelectedListData;
	protected String[] selectedListData;
	
	RequirementSelectionPanel(){
		
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
		unselectedScrollPane.setViewportView(unselectedListGui);
		
		JPanel btnPanel = new JPanel();
		horizontalBox.add(btnPanel);
		SpringLayout sl_btnPanel = new SpringLayout();
		btnPanel.setLayout(sl_btnPanel);
		
		JButton btnAddAll = new JButton(">>");
		sl_btnPanel.putConstraint(SpringLayout.NORTH, btnAddAll, 84, SpringLayout.NORTH, btnPanel);
		sl_btnPanel.putConstraint(SpringLayout.WEST, btnAddAll, 10, SpringLayout.WEST, btnPanel);
		sl_btnPanel.putConstraint(SpringLayout.EAST, btnAddAll, -10, SpringLayout.EAST, btnPanel);
		btnPanel.add(btnAddAll);
		
		JButton btnAdd = new JButton(">");
		sl_btnPanel.putConstraint(SpringLayout.NORTH, btnAdd, 6, SpringLayout.SOUTH, btnAddAll);
		sl_btnPanel.putConstraint(SpringLayout.WEST, btnAdd, 10, SpringLayout.WEST, btnPanel);
		sl_btnPanel.putConstraint(SpringLayout.EAST, btnAdd, -10, SpringLayout.EAST, btnPanel);
		btnPanel.add(btnAdd);
		
		JButton btnRemove = new JButton("<");
		sl_btnPanel.putConstraint(SpringLayout.NORTH, btnRemove, 12, SpringLayout.SOUTH, btnAdd);
		sl_btnPanel.putConstraint(SpringLayout.WEST, btnRemove, 10, SpringLayout.WEST, btnPanel);
		sl_btnPanel.putConstraint(SpringLayout.EAST, btnRemove, -10, SpringLayout.EAST, btnPanel);
		btnPanel.add(btnRemove);
		
		JButton btnRemoveAll = new JButton("<<");
		sl_btnPanel.putConstraint(SpringLayout.NORTH, btnRemoveAll, 6, SpringLayout.SOUTH, btnRemove);
		sl_btnPanel.putConstraint(SpringLayout.WEST, btnRemoveAll, 10, SpringLayout.WEST, btnPanel);
		sl_btnPanel.putConstraint(SpringLayout.EAST, btnRemoveAll, -10, SpringLayout.EAST, btnPanel);
		btnPanel.add(btnRemoveAll);
		
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
		selectedScrollPane.setViewportView(selectedListGui);
		
		
		update();
	}
	
	private void populateBooleans() {
		for (Requirement rqt : this.requirements){
			this.selection.addLast(false);
		}
		
	}

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

	private void update(){
		LinkedList<String> unselectedRequirements = new LinkedList<String>();
		LinkedList<String> selectedRequirements = new LinkedList<String>();
		for (Requirement rqt : this.requirements){
			int pos = this.requirements.indexOf(rqt);
			if (this.selection.get(pos)){
				selectedRequirements.add(rqt.getName());
			}
			else{
				unselectedRequirements.add(rqt.getName());
			}
		}
		
		unSelectedListData = unselectedRequirements.toArray(new String[0]);
		selectedListData = selectedRequirements.toArray(new String[0]);
		
		updateUnselectedList();
		updateUnselectedList();
	}
	
	// update the data displayed in the unselected list
	private void updateUnselectedList(){
		this.unSelectedListModel = new AbstractListModel(){
			String[] strings = unSelectedListData;
			public int getSize(){return strings.length;}
			public Object getElementAt(int i){return strings[i];}
		};
		this.unselectedListGui.setModel(unSelectedListModel);	
	}
	
	// update the data displayed by the selected list
	private void updateSelectedList(){
		this.selectedListModel = new AbstractListModel(){
			String[] strings = selectedListData;
			public int getSize(){return strings.length;}
			public Object getElementAt(int i){return strings[i];}
		};
		this.selectedListGui.setModel(selectedListModel);
	}
	
	private int getUnselectedPos(){
		int position[];
		
		for (boolean bool : this.selection){
			
		}
		
		return positions;
	}
	
	private void add(){
		int pos[] = getUnselectedPos();
		int selected[] = unselectedListGui.getSelectedIndices();
		for(int n : selected){
			int position = pos[n];
			this.selection.remove(position);
			this.selection.add(position, true);
		}
		
	}
	
	private void remove(){
		int pos[] = getSelectedPos();
		int selected[] = selectedListGui.getSelectedIndices();
		for(int n : selected){
			int position = pos[n];
			this.selection.remove(position);
			this.selection.add(position, false);
		}
		
	}
}
