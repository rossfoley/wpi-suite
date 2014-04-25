/*******************************************************************************
 * Copyright (c) 2014 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Team8s
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.session;
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

import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSessionModel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.requirementselection.InfoPanel;
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

import javax.swing.JLabel;

public class RequirementSelectionPanel extends JPanel{

	private final RequirementSelectionView parent;
	private LinkedList<Requirement> requirements;
	private LinkedList<Boolean> selection;
	private final JList selectedListGui;
	private final JList unselectedListGui;
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
	private final InfoPanel infoPanel;
	private int[] unselectedIndicesOld = {};
	private int[] selectedIndicesOld = {};
	private Requirement visibleRequirement;


	/**
	 * Constructor to create the requirement selection panel
	 */
	public RequirementSelectionPanel(RequirementSelectionView parent){

		this.parent = parent;
		requirements = new LinkedList<Requirement>();
		populateRequirements();
		populateBooleans();


		final SpringLayout springLayout = new SpringLayout();
		setLayout(springLayout);

		infoPanel = new InfoPanel();
		springLayout.putConstraint(SpringLayout.NORTH, infoPanel, -150, SpringLayout.SOUTH, this);
		springLayout.putConstraint(SpringLayout.WEST, infoPanel, 0, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.SOUTH, infoPanel,0, SpringLayout.SOUTH, this);
		springLayout.putConstraint(SpringLayout.EAST, infoPanel, 0, SpringLayout.EAST, this);
		add(infoPanel);

		final Box horizontalBox = Box.createHorizontalBox();
		springLayout.putConstraint(SpringLayout.NORTH, horizontalBox, 0, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, horizontalBox, 0, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.SOUTH, horizontalBox, 0, SpringLayout.NORTH, infoPanel);
		springLayout.putConstraint(SpringLayout.EAST, horizontalBox, 0, SpringLayout.EAST, this);
		add(horizontalBox);

		final JPanel unselectedPanel = new JPanel();
		horizontalBox.add(unselectedPanel);
		final SpringLayout sl_unselectedPanel = new SpringLayout();
		unselectedPanel.setLayout(sl_unselectedPanel);

		final JLabel lblUnselectedList = new JLabel("Unselected Requirements");
		sl_unselectedPanel.putConstraint(SpringLayout.NORTH, lblUnselectedList, 10, SpringLayout.NORTH, unselectedPanel);
		sl_unselectedPanel.putConstraint(SpringLayout.WEST, lblUnselectedList, 10, SpringLayout.WEST, unselectedPanel);
		unselectedPanel.add(lblUnselectedList);

		final JScrollPane unselectedScrollPane = new JScrollPane();
		sl_unselectedPanel.putConstraint(SpringLayout.NORTH, unselectedScrollPane, 0, SpringLayout.SOUTH, lblUnselectedList);
		sl_unselectedPanel.putConstraint(SpringLayout.WEST, unselectedScrollPane, 10, SpringLayout.WEST, unselectedPanel);
		sl_unselectedPanel.putConstraint(SpringLayout.SOUTH, unselectedScrollPane, -10, SpringLayout.SOUTH, unselectedPanel);
		sl_unselectedPanel.putConstraint(SpringLayout.EAST, unselectedScrollPane, -10, SpringLayout.EAST, unselectedPanel);
		unselectedPanel.add(unselectedScrollPane);

		unselectedListGui = new JList();
		unselectedListGui.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				validButtons();
				viewUnselectedRequirement();
			}
		});
		unselectedScrollPane.setViewportView(unselectedListGui);

		final JPanel btnPanel = new JPanel();
		horizontalBox.add(btnPanel);
		final SpringLayout sl_btnPanel = new SpringLayout();
		btnPanel.setLayout(sl_btnPanel);

		final Box verticalBox = Box.createVerticalBox();
		sl_btnPanel.putConstraint(SpringLayout.NORTH, verticalBox, 20, SpringLayout.NORTH, btnPanel);
		sl_btnPanel.putConstraint(SpringLayout.WEST, verticalBox, 5, SpringLayout.WEST, btnPanel);
		sl_btnPanel.putConstraint(SpringLayout.SOUTH, verticalBox, -5, SpringLayout.SOUTH, btnPanel);
		sl_btnPanel.putConstraint(SpringLayout.EAST, verticalBox, -5, SpringLayout.EAST, btnPanel);
		btnPanel.add(verticalBox);

		final JPanel panelSpace = new JPanel();
		//verticalBox.add(panelSpace);
		panelSpace.setLayout(new SpringLayout());

		final JPanel panelAddAll = new JPanel();
		verticalBox.add(panelAddAll);
		final SpringLayout sl_panelAddAll = new SpringLayout();
		panelAddAll.setLayout(sl_panelAddAll);

		btnAddAll = new JButton(">>");
		sl_panelAddAll.putConstraint(SpringLayout.NORTH, btnAddAll, 5, SpringLayout.NORTH, panelAddAll);
		sl_panelAddAll.putConstraint(SpringLayout.WEST, btnAddAll, 5, SpringLayout.WEST, panelAddAll);
		sl_panelAddAll.putConstraint(SpringLayout.SOUTH, btnAddAll, -5, SpringLayout.SOUTH, panelAddAll);
		sl_panelAddAll.putConstraint(SpringLayout.EAST, btnAddAll, -5, SpringLayout.EAST, panelAddAll);
		sl_btnPanel.putConstraint(SpringLayout.WEST, btnAddAll, 10, SpringLayout.WEST, panelAddAll);
		sl_btnPanel.putConstraint(SpringLayout.SOUTH, btnAddAll, -1, SpringLayout.SOUTH, panelAddAll);
		sl_btnPanel.putConstraint(SpringLayout.EAST, btnAddAll, 0, SpringLayout.EAST, btnPanel);
		btnAddAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectAll();
			}
		});
		panelAddAll.add(btnAddAll);
		//sl_btnPanel.putConstraint(SpringLayout.NORTH, btnAdd, 25, SpringLayout.SOUTH, btnAddAll);

		final JPanel panelAdd = new JPanel();
		verticalBox.add(panelAdd);
		final SpringLayout sl_panelAdd = new SpringLayout();
		panelAdd.setLayout(sl_panelAdd);

		btnAdd = new JButton(">");
		sl_panelAdd.putConstraint(SpringLayout.NORTH, btnAdd, 5, SpringLayout.NORTH, panelAdd);
		sl_panelAdd.putConstraint(SpringLayout.WEST, btnAdd, 5, SpringLayout.WEST, panelAdd);
		sl_panelAdd.putConstraint(SpringLayout.SOUTH, btnAdd, -5, SpringLayout.SOUTH, panelAdd);
		sl_panelAdd.putConstraint(SpringLayout.EAST, btnAdd, -5, SpringLayout.EAST, panelAdd);
		sl_btnPanel.putConstraint(SpringLayout.NORTH, btnAdd, 10, SpringLayout.NORTH, panelAdd);
		sl_btnPanel.putConstraint(SpringLayout.WEST, btnAdd, 10, SpringLayout.WEST, panelAdd);
		panelAdd.add(btnAdd);
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				add();
			}
		});
		//sl_btnPanel.putConstraint(SpringLayout.NORTH, btnRemove, 12, SpringLayout.SOUTH, btnAdd);

		final JPanel panelRemove = new JPanel();
		verticalBox.add(panelRemove);
		final SpringLayout sl_panelRemove = new SpringLayout();
		panelRemove.setLayout(sl_panelRemove);

		btnRemove = new JButton("<");
		sl_panelRemove.putConstraint(SpringLayout.NORTH, btnRemove, 5, SpringLayout.NORTH, panelRemove);
		sl_panelRemove.putConstraint(SpringLayout.WEST, btnRemove, 5, SpringLayout.WEST, panelRemove);
		sl_panelRemove.putConstraint(SpringLayout.SOUTH, btnRemove, -5, SpringLayout.SOUTH, panelRemove);
		sl_panelRemove.putConstraint(SpringLayout.EAST, btnRemove, -5, SpringLayout.EAST, panelRemove);
		sl_btnPanel.putConstraint(SpringLayout.NORTH, btnRemove, 10, SpringLayout.NORTH, panelRemove);
		sl_btnPanel.putConstraint(SpringLayout.WEST, btnRemove, 25, SpringLayout.WEST, panelRemove);
		panelRemove.add(btnRemove);
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				remove();
			}
		});
		//sl_btnPanel.putConstraint(SpringLayout.NORTH, btnRemoveAll, 6, SpringLayout.SOUTH, btnRemove);

		final JPanel panelRemoveAll = new JPanel();
		verticalBox.add(panelRemoveAll);
		final SpringLayout sl_panelRemoveAll = new SpringLayout();
		panelRemoveAll.setLayout(sl_panelRemoveAll);

		btnRemoveAll = new JButton("<<");
		sl_panelRemoveAll.putConstraint(SpringLayout.NORTH, btnRemoveAll, 5, SpringLayout.NORTH, panelRemoveAll);
		sl_panelRemoveAll.putConstraint(SpringLayout.WEST, btnRemoveAll, 5, SpringLayout.WEST, panelRemoveAll);
		sl_panelRemoveAll.putConstraint(SpringLayout.SOUTH, btnRemoveAll, -5, SpringLayout.SOUTH, panelRemoveAll);
		sl_panelRemoveAll.putConstraint(SpringLayout.EAST, btnRemoveAll, -5, SpringLayout.EAST, panelRemoveAll);
		sl_btnPanel.putConstraint(SpringLayout.WEST, btnRemoveAll, 10, SpringLayout.WEST, btnPanel);
		sl_btnPanel.putConstraint(SpringLayout.EAST, btnRemoveAll, -10, SpringLayout.EAST, btnPanel);
		panelRemoveAll.add(btnRemoveAll);
		btnRemoveAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				unselectAll();
			}
		});
		//sl_btnPanel.putConstraint(SpringLayout.NORTH, btnNewReq, 6, SpringLayout.SOUTH, btnRemoveAll);
		//sl_btnPanel.putConstraint(SpringLayout.NORTH, horizontalStrut, 24, SpringLayout.SOUTH, btnRemoveAll);

		final JPanel panelNewReq = new JPanel();
		verticalBox.add(panelNewReq);
		final SpringLayout sl_panelNewReq = new SpringLayout();
		panelNewReq.setLayout(sl_panelNewReq);

		btnNewReq = new JButton("New Req");
		sl_panelNewReq.putConstraint(SpringLayout.NORTH, btnNewReq, 5, SpringLayout.NORTH, panelNewReq);
		sl_panelNewReq.putConstraint(SpringLayout.WEST, btnNewReq, 5, SpringLayout.WEST, panelNewReq);
		sl_panelNewReq.putConstraint(SpringLayout.SOUTH, btnNewReq, -5, SpringLayout.SOUTH, panelNewReq);
		sl_panelNewReq.putConstraint(SpringLayout.EAST, btnNewReq, -5, SpringLayout.EAST, panelNewReq);
		sl_btnPanel.putConstraint(SpringLayout.NORTH, btnNewReq, 10, SpringLayout.NORTH, panelNewReq);
		sl_btnPanel.putConstraint(SpringLayout.WEST, btnNewReq, 10, SpringLayout.WEST, panelNewReq);
		sl_btnPanel.putConstraint(SpringLayout.EAST, btnNewReq, 0, SpringLayout.EAST, btnPanel);
		panelNewReq.add(btnNewReq);

		final JPanel panelSpace2 = new JPanel();
		//verticalBox.add(panelSpace2);
		panelSpace2.setLayout(new SpringLayout());
		btnNewReq.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openCreationPanel();
			}
		});

		final JPanel selectedPanel = new JPanel();
		horizontalBox.add(selectedPanel);
		final SpringLayout sl_selectedPanel = new SpringLayout();
		selectedPanel.setLayout(sl_selectedPanel);

		final JLabel lblSelectedRequirements = new JLabel("Selected Requirements");
		sl_selectedPanel.putConstraint(SpringLayout.NORTH, lblSelectedRequirements, 10, SpringLayout.NORTH, selectedPanel);
		sl_selectedPanel.putConstraint(SpringLayout.WEST, lblSelectedRequirements, 10, SpringLayout.WEST, selectedPanel);
		selectedPanel.add(lblSelectedRequirements);

		final JScrollPane selectedScrollPane = new JScrollPane();
		sl_selectedPanel.putConstraint(SpringLayout.NORTH, selectedScrollPane, 0, SpringLayout.SOUTH, lblSelectedRequirements);
		sl_selectedPanel.putConstraint(SpringLayout.WEST, selectedScrollPane, 10, SpringLayout.WEST, selectedPanel);
		sl_selectedPanel.putConstraint(SpringLayout.SOUTH, selectedScrollPane, -10, SpringLayout.SOUTH, selectedPanel);
		sl_selectedPanel.putConstraint(SpringLayout.EAST, selectedScrollPane, -10, SpringLayout.EAST, selectedPanel);
		selectedPanel.add(selectedScrollPane);

		selectedListGui = new JList();
		selectedListGui.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				validButtons();
				viewSelectedRequirement();
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
			selection = new LinkedList<Boolean>();
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
		// Get the singleton instance of the requirement model to steal it's list of requirements.
		final RequirementModel requirementModel = RequirementModel.getInstance();
		try {
			// Steal list of requirements from requirement model muhahaha.
			final List<Requirement> reqsList = requirementModel.getRequirements();
			final List<Requirement> reqsInBacklog = new LinkedList<Requirement>();
			for (Requirement r:reqsList){
				if (r.getIteration().equals("Backlog") && r.getEstimate() == 0){
					reqsInBacklog.add(r);
				}
			}
			
			List<PlanningPokerSession> sessions = PlanningPokerSessionModel.getInstance().getPlanningPokerSessions();
			LinkedList<Integer> toRemove = new LinkedList<Integer>();
			for (PlanningPokerSession session : sessions) {
				if (!session.isPending()){
					Set<Integer> IDs = session.getRequirementIDs();
					for (Requirement req : reqsInBacklog) {
						if (IDs.contains(req.getId())) {
							int index = reqsInBacklog.indexOf(req);
							System.out.println("Removing index: " + Integer.toString(index));
							//reqsInBacklog.remove(index);
							toRemove.add(index);
							//System.out.println("Removing requirement: " + Integer.toString(req.getId()));
						}
					}
				}
			}
			
			System.out.println("toRemove: " + toRemove);
			for (int i = toRemove.size() -1; i >= 0 ; i--) {
				int index = toRemove.get(i);
				//System.out.println("Removing index: " + Integer.toString(index));
				reqsInBacklog.remove(index);
			}
			
			
			requirements = (LinkedList<Requirement>)reqsInBacklog;
			
			//requirements = (LinkedList<Requirement>)filterRequirements(reqsList);

		}
		catch (Exception e) {}
	}

	private List<Requirement> filterRequirements(List<Requirement> requirements){
		List<Requirement> requirementsList = new LinkedList<Requirement>();

		for (Requirement req : requirements) {
			boolean valid = true;
			if (!req.getIteration().equals("BackLog")) valid = false;
			if (req.getEstimate() > 0) valid = false;
			if (valid) requirementsList.add(req); 
		}

		List<PlanningPokerSession> sessions = PlanningPokerSessionModel.getInstance().getPlanningPokerSessions();
		for (PlanningPokerSession session : sessions){
			if (!session.isPending()){
				Set<Integer> IDs = session.getRequirementIDs();
				for (Requirement req : requirementsList){
					if (IDs.contains(req.getId())){
						requirementsList.remove(req);
					}
				}
			}
		}
		return requirementsList;
	}

	/**
	 * This function is used to update the lists and the state of 
	 * the buttons
	 */
	private void update() {
		final LinkedList<String> unselectedRequirements = new LinkedList<String>();
		final LinkedList<String> selectedRequirements = new LinkedList<String>();
		for (Requirement rqt : requirements) {
			int pos = requirements.indexOf(rqt);
			if (selection.get(pos)) {
				selectedRequirements.add(rqt.getName());
			}
			else {
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
		unSelectedListModel = new AbstractListModel(){
			private final String[] strings = unSelectedListData;
			public int getSize(){return strings.length;}
			public Object getElementAt(int i){return strings[i];}
		};
		unselectedListGui.setModel(unSelectedListModel);
	}

	/**
	 * Update the list model for the selected list
	 */
	// update the data displayed by the selected list
	private void updateSelectedList(){
		selectedListModel = new AbstractListModel(){
			private final String[] strings = selectedListData;
			public int getSize(){return strings.length;}
			public Object getElementAt(int i){return strings[i];}
		};
		selectedListGui.setModel(selectedListModel);
	}

	/**
	 * This function gets the position of each unselected requirement
	 * 
	 * @return list of positions
	 */
	private List<Integer> getUnselectedPos(){
		final LinkedList<Integer> positions = new LinkedList<Integer>();
		for (Requirement rqt : requirements){
			int pos = requirements.indexOf(rqt);
			if (!selection.get(pos)){
				positions.add(pos);
			}
		}

		return positions;
	}

	/**
	 * This function get the position of each selected requirement
	 * @return
	 */
	private List<Integer> getSelectedPos(){
		final LinkedList<Integer> positions = new LinkedList<Integer>();
		for (Requirement rqt : requirements){
			int pos = requirements.indexOf(rqt);
			if (selection.get(pos)){
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
		final List<Integer> pos = getUnselectedPos();
		final int selected[] = unselectedListGui.getSelectedIndices();
		for(int n : selected){
			int position = pos.get(n);
			selection.remove(position);
			selection.add(position, true);
		}
		numRequirementsAdded += 1;
		update();
	}

	/**
	 * This function takes the selected requirements that are
	 * indicated to become unselected
	 */
	private void remove(){
		final List<Integer> pos = getSelectedPos();
		final int selected[] = selectedListGui.getSelectedIndices();
		for(int n : selected){
			int position = pos.get(n);
			selection.remove(position);
			selection.add(position, false);
		}
		numRequirementsAdded -= 1;
		update();

	}

	/**
	 * This function makes all of the requirements selected
	 */
	private void selectAll(){
		final List<Integer> pos = getUnselectedPos();
		//int selected[] = unselectedListGui.getSelectedIndices();
		for(int n : pos){
			selection.remove(n);
			selection.add(n, true);
		}
		numRequirementsAdded = selection.size();
		update();

	}

	/**
	 * This function makes all of the requirements unselected
	 */
	private void unselectAll(){
		final List<Integer> pos = getSelectedPos();
		//int selected[] = unselectedListGui.getSelectedIndices();
		for(int n : pos){
			selection.remove(n);
			selection.add(n, false);
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
		//checks for full lists and disables trying to move from empty lists
		final boolean allUnselected = fullList(false);
		final boolean allSelected = fullList(true);
		if (allUnselected) {
			btnRemoveAll.setEnabled(false);
		}
		else {
			btnRemoveAll.setEnabled(true);
		}

		if (allSelected) {
			btnAddAll.setEnabled(false);
		}
		else {
			btnAddAll.setEnabled(true);
		}

		// checks to see any requirements are selected for moving
		final boolean pickedUnselected = anySelected(unselectedListGui.getSelectedIndices());
		final boolean pickedSelected = anySelected(selectedListGui.getSelectedIndices());
		if (pickedUnselected) {
			btnAdd.setEnabled(true);
		}
		else {
			btnAdd.setEnabled(false);
		}
		if (pickedSelected) {
			btnRemove.setEnabled(true);
		}
		else {
			btnRemove.setEnabled(false);
		}


	}

	// checks for full lists
	private boolean fullList(boolean aBool){
		boolean full = true;

		for(boolean bool : selection){
			if(bool != aBool){
				full = false;
			}
		}

		return full;
	}

	// checks for any selections from the given array
	private boolean anySelected(int selected[]){
		return selected != null && selected.length > 0;
	}

	private void viewUnselectedRequirement(){
		final int[] unselectedIndicesCurrent = unselectedListGui.getSelectedIndices();
		for (int n : unselectedIndicesCurrent){
			boolean contains = false;
			for (int i : unselectedIndicesOld){
				if (i == n){
					contains = true;
				}
			}
			if (!contains){
				int pos = getUnselectedPos().get(n);
				visibleRequirement = requirements.get(pos);

			}
		}

		infoPanel.setRequirement(visibleRequirement);
		unselectedIndicesOld = unselectedIndicesCurrent;
	}

	private void viewSelectedRequirement(){
		final int[] selectedIndicesCurrent = selectedListGui.getSelectedIndices();
		for (int n : selectedIndicesCurrent){
			boolean contains = false;
			for (int i : selectedIndicesOld){
				if (i == n){
					contains = true;
				}
			}
			if (!contains){
				int pos = getSelectedPos().get(n);
				visibleRequirement = requirements.get(pos);
			}
		}

		infoPanel.setRequirement(visibleRequirement);
		selectedIndicesOld = selectedIndicesCurrent;
		//remove(infoPanel);
		//add(infoPanel);
	}

	/**
	 * This function add a new requirement directly to the selected 
	 * list
	 * @param requirement the requirement to be added
	 */
	public void addRequirement(Requirement requirement){
		selection.addLast(true);
		requirements.addLast(requirement);
		update();
	}

	/**
	 * This function returns the requirements that are currently 
	 * selected
	 * @return
	 */
	public List<Requirement> getSelected(){
		final List<Requirement> selection = new LinkedList<Requirement>();
		for(Requirement rqt : requirements){
			if (this.selection.get(requirements.indexOf(rqt))){
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
			int pos = requirements.indexOf(current);
			if (pos > -1) {
				selection.set(pos, true);
				numRequirementsAdded += 1;
			}
		}
		update();
	}

	public void openCreationPanel(){
		parent.openCreationPanel();
	}

	public void newRequirementAdded(Requirement newReq){
		populateRequirements();
		populateBooleans();
		final int pos = requirements.indexOf(newReq);
		selection.set(pos, true);
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
				RequirementsSelectedListener l = e.nextElement();
				l.setRequirementsSelected(event);
			}
		}
	}
}
