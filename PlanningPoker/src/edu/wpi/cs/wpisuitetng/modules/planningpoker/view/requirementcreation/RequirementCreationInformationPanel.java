/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team Rolling Thunder
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.requirementcreation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.reflect.Array;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

import net.miginfocom.swing.MigLayout;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.controller.UpdateRequirementController;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.RequirementModel;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.characteristics.RequirementPriority;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.characteristics.RequirementStatus;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.characteristics.RequirementType;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.iterations.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.iterations.IterationModel;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.ViewEventController;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.requirements.RequirementPanelListener;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.requirements.RequirementSelector;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.requirements.RequirementSelectorListener;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.requirements.RequirementSelectorMode;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.requirements.ScrollablePanel;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.requirements.ViewMode;

/**
 * @author justinhess
 * @version $Revision: 1.0 $
 */
public class RequirementCreationInformationPanel extends JScrollPane implements KeyListener,
ItemListener, RequirementPanelListener, RequirementSelectorListener {
	private Requirement currentRequirement;
	private ViewMode viewMode;
	private RequirementCreationPanel parentPanel;

	private final Iteration storedIteration;
	private RequirementStatus storedStatus;
	
	private JTextField boxName;
	private JTextField boxReleaseNum;
	private JTextArea boxDescription;
	private JComboBox<Iteration> boxIteration;
	private JTextField boxChildEstimate;
	private JLabel labelChildEstimate;
	private JTextField boxTotalEstimate;
	private JLabel labelTotalEstimate;
	private final Border defaultBorder = (new JTextField()).getBorder();
	private final Border errorBorder = BorderFactory
			.createLineBorder(Color.RED);

	private JLabel currentParent;
	private JButton editParent;
	private JButton removeFromParent;
	private JButton chooseParent;
	private JPanel noParentInfoPanel;
	private RequirementSelector parentSelector;
	private JComboBox<RequirementType> dropdownType;
	private JComboBox<RequirementStatus> dropdownStatus;
	private JComboBox<RequirementPriority> dropdownPriority;
	private JTextField boxEstimate;

	private RequirementStatus lastValidStatus;
	private boolean fillingFieldsForRequirement = false;
	
	private JLabel errorName;
	private JLabel errorDescription;

	/**
	 * Constructs the requirement information panel
	 * @param parentPanel the panel this info panel reports to
	 * @param mode the current view mode.
	 * @param curr the requirement being edited/created.
	 */
	public RequirementCreationInformationPanel(RequirementCreationPanel parentPanel,
			ViewMode mode, Requirement currRequirement) {
		this.currentRequirement = currRequirement;
		this.parentPanel = parentPanel;
		this.viewMode = mode;
		storedIteration = IterationModel.getInstance().getIteration("Backlog");
		
		this.setMinimumSize(new Dimension(500,200));
		this.buildLayout();

		clearInfo();
	}

	/**
	 * Builds the layout panel.
	 * 
	
	 */
	@SuppressWarnings("rawtypes")
	private void buildLayout() {
		final ScrollablePanel contentPanel = new ScrollablePanel();
		contentPanel.setLayout(new MigLayout("", "", "shrink"));
		//instantialize everything.
		final JLabel labelName = new JLabel("Name *");
		final JLabel labelReleaseNum = new JLabel("Release Number");
		final JLabel labelDescription = new JLabel("Description *");
		final JLabel labelIteration = new JLabel("Iteration");
		labelChildEstimate = new JLabel("Children Estimate");
		labelTotalEstimate = new JLabel("Total Estimate");
		final JLabel labelType = new JLabel("Type");
		final JLabel labelStatus = new JLabel("Status");
		final JLabel labelPriority = new JLabel("Priority");
		final JLabel labelEstimate = new JLabel("Estimate");
		final JPanel parentInfoPanel = new JPanel();
		boxName = new JTextField();
		boxName.addKeyListener(this);

		boxReleaseNum = (new JTextField());
		boxReleaseNum.addKeyListener(this);

		final JScrollPane descrScroll = new JScrollPane();
		boxDescription = new JTextArea();
		boxDescription.setLineWrap(true);
		boxDescription.setBorder(defaultBorder);
		boxDescription.addKeyListener(this);
		descrScroll.setViewportView(boxDescription);

		final List<Iteration> iterations = IterationModel.getInstance().getIterations();
		final Iteration[] iterationArray = new Iteration[iterations.size()];
		for (int i = 0; i < iterations.size(); i++) {
			Iteration iter = iterations.get(i);
			iterationArray[i] = iter;
		}
		boxIteration = (new JComboBox<Iteration>(iterationArray));
		boxIteration.addItemListener(this);
		boxIteration.setBackground(Color.WHITE);
		boxIteration.setMaximumSize(new Dimension(150, 25));
		
		boxIteration.setSelectedItem(storedIteration);

		errorName = (new JLabel());
		errorDescription = (new JLabel());

		dropdownType = (new JComboBox<RequirementType>(RequirementType.values()));
		dropdownType.setEditable(false);
		dropdownType.setBackground(Color.WHITE);
		dropdownType.addItemListener(this);

		dropdownStatus = (new JComboBox<RequirementStatus>(
				RequirementStatus.values()));
		dropdownStatus.setEditable(false);
		dropdownStatus.setBackground(Color.WHITE);
		dropdownStatus.addItemListener(this);

		// Radio buttons

		dropdownPriority = new JComboBox<RequirementPriority>(RequirementPriority.values());
		dropdownPriority.setEditable(false);
		dropdownPriority.setBackground(Color.WHITE);
		dropdownPriority.addItemListener(this);

		boxEstimate = (new JTextField());
		boxEstimate.setPreferredSize(new Dimension(50, 20));
		boxEstimate.addKeyListener(this);
		boxEstimate.setEnabled(false);
		boxChildEstimate = (new JTextField());
		boxChildEstimate.setEnabled(false);
		boxTotalEstimate = new JTextField();
		boxTotalEstimate.setEnabled(false);
		
		final boolean hasChildren = false;
		labelChildEstimate.setVisible(hasChildren);
		boxChildEstimate.setVisible(hasChildren);

		labelTotalEstimate.setVisible(hasChildren);
		boxTotalEstimate.setVisible(hasChildren);

		currentParent = new JLabel();
		editParent = new JButton("Edit Parent");
		editParent.setAlignmentX(RIGHT_ALIGNMENT);
		editParent.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				if(currentRequirement.getParentID() != -1)
				{
					ViewEventController.getInstance().editRequirement(currentRequirement.getParent());
				}
			}
		});
		removeFromParent = new JButton("Remove From Parent");
		removeFromParent.setAlignmentX(RIGHT_ALIGNMENT);
		removeFromParent.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				final Requirement oldParent = currentRequirement.getParent();
				try {
					currentRequirement.setParentID(-1);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				UpdateRequirementController.getInstance().updateRequirement(currentRequirement);
				ViewEventController.getInstance().refreshEditRequirementPanel(currentRequirement);
				ViewEventController.getInstance().refreshEditRequirementPanel(oldParent);
				ViewEventController.getInstance().getOverviewTree().refresh();
			}
		});
		
		parentSelector = new RequirementSelector(this, currentRequirement, RequirementSelectorMode.POSSIBLE_PARENTS, false);

		chooseParent = new JButton("Choose Parent");
		chooseParent.setAlignmentX(RIGHT_ALIGNMENT);
		chooseParent.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e) {
				chooseParent.setVisible(false);
				parentSelector.setVisible(true);
				repaint();
			}
			
		});
		

		parentInfoPanel.setLayout(new BoxLayout(parentInfoPanel, BoxLayout.Y_AXIS));
		currentParent.setAlignmentX(LEFT_ALIGNMENT);
		noParentInfoPanel = new JPanel();
		parentInfoPanel.add(editParent);
		parentInfoPanel.add(removeFromParent);
		noParentInfoPanel.add(parentSelector);
		noParentInfoPanel.add(chooseParent);
		parentInfoPanel.add(noParentInfoPanel);
		chooseParent.setVisible(true);
		parentSelector.setVisible(false);
		//setup the top.

		contentPanel.add(labelName, "wrap");
		contentPanel.add(boxName, "growx, pushx, shrinkx, span, wrap");

		contentPanel.add(labelDescription, "wrap");
		contentPanel.add(descrScroll, "growx, pushx, shrinkx, span, height 200px, wmin 10, wrap");

		//setup columns.
		final JPanel leftColumn = new JPanel(new MigLayout());
		final JPanel rightColumn = new JPanel(new MigLayout());
		leftColumn.add(labelReleaseNum, "left,wrap");
		leftColumn.add(boxReleaseNum, "width 100px, left,wrap");
		leftColumn.add(labelIteration, "left,wrap");
		leftColumn.add(boxIteration, "width 100px, left,wrap");
		leftColumn.add(labelEstimate, "left,wrap");
		leftColumn.add(boxEstimate, "width 50px, left,wrap");
		leftColumn.add(labelChildEstimate, "left,wrap");
		leftColumn.add(boxChildEstimate, "width 50px, left, wrap");
		leftColumn.add(labelTotalEstimate, "left, wrap");
		leftColumn.add(boxTotalEstimate, "width 50px, left");

		rightColumn.add(labelType, "left, wrap");
		rightColumn.add(dropdownType, "left, wrap");
		rightColumn.add(labelStatus, "left, wrap");
		rightColumn.add(dropdownStatus, "left, wrap");
		rightColumn.add(labelPriority, "left, wrap");
		rightColumn.add(dropdownPriority, "left, wrap");
		rightColumn.add(currentParent, "left, wrap");
		rightColumn.add(parentInfoPanel, "left, span, wrap");
		
		contentPanel.add(leftColumn, "left, spany, growy, push");
		contentPanel.add(rightColumn, "right, spany, growy, push");
		
		fireRefresh();
		this.setViewportView(contentPanel);
	}

	/**
	 * Refreshes the information of the requirement.
	 * @see edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.requirements.RequirementPanelListener#fireRefresh()
	 */
	public void fireRefresh() {
		parentSelector.refreshList();
		refreshParentInformation();
		adjustFieldEnability();
	}

	
	/**
	 * Refresh information about the parent
	 */
	private void refreshParentInformation()
	{
		if (currentRequirement.getParentID() != -1) {
			currentParent.setText("Parent: " + currentRequirement.getParent().getName());
			currentParent.setVisible(true);
			noParentInfoPanel.setVisible(false);
		}
		else {
			currentParent.setText("Parent: ");
			editParent.setVisible(false);
			removeFromParent.setVisible(false);
			noParentInfoPanel.setVisible(true);
		}
	}

	/**
	 * Method fireDeleted.
	 * @param b boolean
	
	 * @see edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.requirements.RequirementPanelListener#fireDeleted(boolean) */
	@Override
	public void fireDeleted(boolean b) {}

	/**
	 * Method fireValid.
	 * @param b boolean
	
	 * @see edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.requirements.RequirementPanelListener#fireValid(boolean) */
	@Override
	public void fireValid(boolean b) {}

	/**
	 * Method fireChanges.
	 * @param b boolean
	
	 * @see edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.requirements.RequirementPanelListener#fireChanges(boolean) */
	@Override
	public void fireChanges(boolean b) {}

	/**
	 * Fills the fields of the edit requirement panel based on the current
	 * settings of the edited requirement.
	 */
	@SuppressWarnings("unchecked")
	private void fillFieldsForRequirement() {
		fillingFieldsForRequirement = true;
		boxName.setText(currentRequirement.getName());
		boxDescription.setText(currentRequirement.getDescription());
		boxEstimate.setText("0");
		boxReleaseNum.setText(currentRequirement.getRelease());
		boxIteration.setSelectedItem(storedIteration);

		dropdownStatus.addItem(RequirementStatus.NEW);
		storedStatus = currentRequirement.getStatus();
		dropdownStatus.setSelectedItem(storedStatus);

		
		dropdownType.setSelectedItem(currentRequirement.getType());

		dropdownPriority.setSelectedItem(currentRequirement.getPriority());

		// reset the error messages.
		boxEstimate.setBorder(defaultBorder);
		errorDescription.setText("");
		boxDescription.setBorder(defaultBorder);
		errorName.setText("");
		boxName.setBorder(defaultBorder);
		boxChildEstimate.setText(Integer.toString(currentRequirement.getChildEstimate()));
		boxTotalEstimate.setText(Integer.toString(currentRequirement.getTotalEstimate()));

		fireRefresh();
		fillingFieldsForRequirement = false;
		repaint();
	}

	/**
	 * Validates the values of the fields in the requirement panel to ensure
	 * they are valid
	 * @param warn whether to warn the user or not
	
	 * @return whether fields are valid. */
	public boolean validateFields(boolean warn) {
		boolean isNameValid;
		boolean isDescriptionValid;
		
		parentPanel.removeError("Name can be no more than 100 chars.");
		parentPanel.removeError("Name is required.");
		parentPanel.removeError("Description is required.");
		
		if (boxName.getText().length() >= 100) {
			isNameValid = false;
			errorName.setText("No more than 100 chars");
			boxName.setBorder(errorBorder);
			errorName.setForeground(Color.RED);
			parentPanel.displayError("Name can be no more than 100 chars.");
		} else if (boxName.getText().trim().length() <= 0) {
			isNameValid = false;
			if(warn)
			{
				errorName.setText("** Name is REQUIRED");
				boxName.setBorder(errorBorder);
				errorName.setForeground(Color.RED);
			}
			parentPanel.displayError("Name is required.");
		} else {
			if(warn)
			{
				errorName.setText("");
				boxName.setBorder(defaultBorder);
			}
			isNameValid = true;

		}
		if (boxDescription.getText().trim().length() <= 0) {
			isDescriptionValid = false;
			if(warn)
			{
				errorDescription.setText("** Description is REQUIRED");
				errorDescription.setForeground(Color.RED);
				boxDescription.setBorder(errorBorder);
			}
			parentPanel.displayError("Description is required.");
		} else {
			if(warn)
			{
				errorDescription.setText("");
				boxDescription.setBorder(defaultBorder);
			}
			isDescriptionValid = true;
		}
		return isNameValid && isDescriptionValid;
	}

	/**
	 * Resets the information back to default
	 */
	public void clearInfo()
	{
		this.fillFieldsForRequirement(); //if editing, revert back to old info

		//no changes have been made, so let the parent know.
		parentPanel.fireChanges(false); 
	}

	/**
	 * Updates the requirement/creates the requirement based on the view mode.
	 */
	public void update() {
		updateRequirement();
	}

	/**
	 * Updates the requirement based on whether it is being created or not
	 * @param wasCreated whether the requirement is being created or edited.
	 */
	private void updateRequirement() {
		currentRequirement.setId(RequirementModel.getInstance().getNextID());
		currentRequirement.setWasCreated(true);
		
		// Extract the name, release number, and description from the GUI fields
		final String stringName = boxName.getText();
		final String stringReleaseNum = boxReleaseNum.getText();
		final String stringDescription = boxDescription.getText();

		final RequirementType type = (RequirementType) dropdownType.getSelectedItem();

		currentRequirement.setName(stringName);
		currentRequirement.setRelease(stringReleaseNum);
		currentRequirement.setDescription(stringDescription);
		currentRequirement.setStatus(RequirementStatus.NEW);
		currentRequirement.setPriority((RequirementPriority)dropdownPriority.getSelectedItem());
		currentRequirement.setEstimate(0);
		currentRequirement.setIteration("Backlog");
		currentRequirement.setType(type);
		
		// Set the time stamp for the transaction for the creation of the requirement
		currentRequirement.getHistory().setTimestamp(System.currentTimeMillis());
		System.out.println("The Time Stamp is now :" + currentRequirement.getHistory().getTimestamp());
		currentRequirement.getHistory().add("Requirement created");

		RequirementModel.getInstance().addRequirement(currentRequirement);
		
		UpdateRequirementController.getInstance().updateRequirement(
				currentRequirement);

		ViewEventController.getInstance().refreshTable();
		ViewEventController.getInstance().refreshTree();

		if(currentRequirement.getParentID() != -1)
		{
			ViewEventController.getInstance().refreshEditRequirementPanel(currentRequirement.getParent());
		}
		
	}
	
	/**
	 * Enables or disables components as needed
	 */
	private void adjustFieldEnability()
	{
		boolean allDisabled = dropdownStatus.getSelectedItem() == RequirementStatus.DELETED;
		allDisabled |= dropdownStatus.getSelectedItem() == RequirementStatus.COMPLETE;
		//boolean inProgress = getDropdownStatus().getSelectedItem() == RequirementStatus.INPROGRESS;
		
		boolean allChildrenDeleted = true;
		for(Requirement child : currentRequirement.getChildren())
		{
			allChildrenDeleted &= child.getStatus() == RequirementStatus.DELETED;
		}
		
		boxName.setEnabled(!allDisabled);
		boxDescription.setEnabled(!allDisabled);
		boxEstimate.setEnabled(false);
		boxReleaseNum.setEnabled(!allDisabled);
		dropdownType.setEnabled(!allDisabled);
		dropdownStatus.setEnabled(false);
		boxIteration.setEnabled(false);
		dropdownPriority.setEnabled(!allDisabled);
		parentPanel.fireDeleted(allDisabled || !allChildrenDeleted);
	}

	/**
	
	 * @return Returns whether any fields in the panel have been changed  */
	private boolean anythingChangedCreating() {
		// Check if the user has changed the name
		if (!(boxName.getText().equals(""))){
			return true;}
		// Check if the user has changed the description
		if (!(boxDescription.getText().equals(""))){
			return true;}
		// Check if the user has changed the release number
		if (!(boxReleaseNum.getText().equals(""))){
			return true;}
		// Check if the user has changed the type
		if (!(((RequirementType)dropdownType.getSelectedItem()) == RequirementType.BLANK)){
			return true;}

		if (dropdownPriority.getSelectedItem() != RequirementPriority.BLANK)
		{
			return true;
		}

		return false;
	}


	/**
	 * Returns whether the panel is ready to be removed or not based on if there are changes that haven't been
	 * saved.
	 * 
	 * @return whether the panel can be removed. * @see edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.requirements.RequirementPanelListener#readyToRemove() * @see edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.requirements.RequirementPanelListener#readyToRemove() * @see edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.requirements.RequirementPanelListener#readyToRemove()
	 */
	public boolean readyToRemove()
	{
		return !anythingChangedCreating();
	}

	/**
	 * Method itemStateChanged.
	 * @param e ItemEvent
	
	 * @see java.awt.event.ItemListener#itemStateChanged(ItemEvent) */
	@Override
	public void itemStateChanged(ItemEvent e) {
		parentPanel.removeError("Cannot complete unless children are completed.");
		parentPanel.removeError("Cannot delete when non-deleted children exist.");
		if(!fillingFieldsForRequirement)
		{
			boolean allChildrenCompleted = true;
			boolean allChildrenDeleted = true;
			for (Requirement Child: currentRequirement.getChildren()){
				allChildrenCompleted &= Child.getStatus() == RequirementStatus.COMPLETE;
				allChildrenDeleted &= Child.getStatus() == RequirementStatus.DELETED;
			}

			if (!allChildrenCompleted && dropdownStatus.getSelectedItem() == RequirementStatus.COMPLETE)
			{
				dropdownStatus.setSelectedItem(lastValidStatus);
				parentPanel.displayError("Cannot complete unless children are completed.");
			}
			else if (!allChildrenDeleted && dropdownStatus.getSelectedItem() == RequirementStatus.DELETED)
			{
				dropdownStatus.setSelectedItem(lastValidStatus);
				parentPanel.displayError("Cannot delete when non-deleted children exist.");
			}
			else
			{

				lastValidStatus = (RequirementStatus)dropdownStatus.getSelectedItem();
			}
		}
		parentPanel.fireValid(validateFields(false));
		parentPanel.fireChanges(anythingChangedCreating());
		adjustFieldEnability();
		this.repaint();
	}

	/**
	 * Method keyReleased.
	 * @param e KeyEvent
	
	 * @see java.awt.event.KeyListener#keyReleased(KeyEvent) */
	@Override
	public void keyReleased(KeyEvent e) {
		parentPanel.fireValid(validateFields(false));
		parentPanel.fireChanges(anythingChangedCreating());
		adjustFieldEnability();


		this.repaint();
	}

	/**
	 * Method keyPressed.
	 * @param e KeyEvent
	
	 * @see java.awt.event.KeyListener#keyPressed(KeyEvent) */
	@Override
	public void keyPressed(KeyEvent e) {}

	/**
	 * Method keyTyped.
	 * @param e KeyEvent
	
	 * @see java.awt.event.KeyListener#keyTyped(KeyEvent) */
	@Override
	public void keyTyped(KeyEvent e) {}

	/**
	 * Checks if the input string is an integer
	 * 
	 * @param input
	 *            the string to test
	
	 * @return true if input is an integer, false otherwise */
	public boolean isInteger(String input) {
		try {
			Integer.parseInt(input);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Returns the error name label
	
	 * @return error name label */
	public JLabel getErrorName() {
		return errorName;
	}

	/**
	
	 * @return the error description label */
	public JLabel getErrorDescription() {
		return errorDescription;
	}

	/**
	 * 
	
	 * @return box name */
	public JTextField getBoxName() {
		return boxName;
	}

	/**
	 * 
	
	 * @return box release num */
	public JTextField getBoxReleaseNum() {
		return boxReleaseNum;
	}

	/**
	 * 
	
	 * @return box description */
	public JTextArea getBoxDescription() {
		return boxDescription;
	}

	/**
	 * 
	
	 * @return box iteration */
	public JComboBox<Iteration> getBoxIteration() {
		return boxIteration;
	}

	/**
	 * 
	
	 * @return box child estimate */
	public JTextField getBoxChildEstimate() {
		return boxChildEstimate;
	}

	/**
	 * 
	
	 * @return box total estimate */
	public JTextField getBoxTotalEstimate() {
		return boxTotalEstimate;
	}

	/**
	 * 
	
	 * @return type dropdown */
	public JComboBox<RequirementType> getDropdownType() {
		return dropdownType;
	}

	/**
	 * 
	
	 * @return status dropdown */
	public JComboBox<RequirementStatus> getDropdownStatus() {
		return dropdownStatus;
	}

	/**
	 * 
	
	 * @return estimate box */
	public JTextField getBoxEstimate() {
		return boxEstimate;
	}

	/**
	 * Method requirementSelected.
	
	 * @param requirements Object[]
	 * @see edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.requirements.RequirementSelectorListener#requirementSelected() */
	@Override
	public void requirementSelected(Object[] requirements) {
		parentSelector.setVisible(false);
		chooseParent.setVisible(true);
	}
	
	public void resetRequirement(Requirement currRequirement){
		currentRequirement = currRequirement;
		clearInfo();
	}
}
