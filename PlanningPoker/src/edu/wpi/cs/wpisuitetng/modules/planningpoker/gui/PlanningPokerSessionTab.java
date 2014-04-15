/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.planningpoker.gui;

/**
 * This class makes up the panel that is used to create a game
 * It takes in fields from the user, displays appropriate messages, and stores information 
 * to a session and then passes it to the PlanningPokerSessionModel to be saved in the database
 *
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import net.sourceforge.jdatepicker.JDateComponentFactory;
import net.sourceforge.jdatepicker.JDatePicker;
import net.sourceforge.jdatepicker.impl.UtilCalendarModel;
import edu.wpi.cs.wpisuitetng.janeway.config.ConfigManager;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.Deck;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.DeckListModel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSessionModel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.notifications.MockNotification;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.ViewEventController;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.gui.SelectFromListPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.controller.GetRequirementsController;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.RequirementModel;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.characteristics.RequirementPriority;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.characteristics.RequirementStatus;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.characteristics.RequirementType;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.iterations.IterationModel;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.requirements.ViewMode;

import javax.swing.JCheckBox;

public class PlanningPokerSessionTab extends JPanel {
	private List<Requirement> requirements;
	private final PlanningPokerSession pokerSession;

	private final SpringLayout layout = new SpringLayout();
	private SpringLayout firstPanelLayout = new SpringLayout();
	private SpringLayout secondPanelLayout = new SpringLayout();
	private JPanel firstPanel = new JPanel();
	private JPanel secondPanel = new JPanel();

	private ViewMode viewMode;
	private JComboBox<String> comboTime = new JComboBox<String>();
	private JComboBox<String> comboAMPM = new JComboBox<String>();
	private JComboBox<String> comboDeck = new JComboBox<String>();
	private JTextField textFieldSessionField = new JTextField();
	private JTextArea textFieldDescription = new JTextArea();
	private JLabel dateErrorMessage = new JLabel("");
	private JLabel nameErrorMessage = new JLabel("");
	private JLabel descriptionErrorMessage = new JLabel("");
	private JLabel numbers = new JLabel("Users input non-negative integers");
	private final SelectFromListPanel requirementPanel = new SelectFromListPanel();
	private JDatePicker datePicker;
	private JCheckBox endDateCheckBox = new JCheckBox("End Date and Time?");
	JLabel norequirements = new JLabel("Please select requirements before creating the session.");
	private JPanel disabledDatePicker;

	private boolean dateHasBeenSet;
	private boolean haveEndDate;
	private boolean submitSession = false;
	private int endHour;
	private int endMinutes;
	private boolean isUsingDeck;
	private Deck sessionDeck;
	private JLabel lblSessionEndTime;
	private JLabel lblEndDate;
	private String[] availableTimes = new String[] { "12:00", "12:30", "1:00", "1:30", "2:00", "2:30", "3:00", "3:30", 
			"4:00", "4:30", "5:00", "5:30", "6:00", "6:30", "7:00", "7:30", 
			"8:00", "8:30", "9:00", "9:30", "10:00", "10:30", "11:00", "11:30" };


	/**
	 * Constructor for creating a planning poker session
	 */
	public PlanningPokerSessionTab() {
		this.pokerSession = new PlanningPokerSession();
		viewMode = (ViewMode.CREATING);
		dateHasBeenSet = false;
		this.buildLayouts();
		this.displayPanel(firstPanel);
	}
	public List<Requirement> PrePouplateRequirements() {
		System.out.println("In Populate Requirements");
		
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
			while(reqsList.isEmpty()){
				requirementsController.retrieveRequirements();
				reqsList = requirementModel.getRequirements();
				if(!reqsList.isEmpty()){
					break;
				}
			}
			for (Requirement r:reqsList){
				if (r.getIteration().equals("Backlog")){
					reqsInBacklog.add(r);
					System.out.println(r.getName());
				}
				System.out.println(r.getName());
			} 
			System.out.println("End of the 4 loop");
			this.requirements = reqsInBacklog;
		
		}
		catch (Exception e) {
			System.out.println("Populaton requirement exception");
		}
		return requirements;
	}
	/**
	 * Constructor for editing an existing planning poker session
	 */
	public PlanningPokerSessionTab(PlanningPokerSession existingSession) {
		this.pokerSession = existingSession;
		this.viewMode = (ViewMode.EDITING);
		// Set the end date checkbox and update fields.
		this.dateHasBeenSet = (existingSession.getEndDate() != null);
		this.endDateCheckBox.setSelected(dateHasBeenSet);
		// Update the fields current deck being used
		this.isUsingDeck = existingSession.isUsingDeck();
		this.sessionDeck = existingSession.getSessionDeck();

		this.buildLayouts();
		this.displayPanel(firstPanel);
	}

	private void buildLayouts() {
		// Apply the layout and build the panels
		this.setLayout(layout);
		this.buildFirstPanel();
		this.buildSecondPanel();
	}

	/**
	 * Builds the first screen for creating/editing a session
	 */
	private void buildFirstPanel() {
		// Set the layout and colors
		firstPanel.setLayout(firstPanelLayout);
		firstPanel.setForeground(Color.BLACK);

		// Initialize all of the fields on the panel
		final JLabel lblSessionName = new JLabel("Session Name: *");
		final JLabel lblSessionDescription = new JLabel("Session Description: *");
		lblEndDate = new JLabel("Session End Date:");
		lblSessionEndTime = new JLabel("Session End Time:");
		final JLabel lblDeck = new JLabel("Deck:");

		final JButton btnNext = new JButton("Next >");
		datePicker = JDateComponentFactory.createJDatePicker(new UtilCalendarModel(pokerSession.getEndDate()));

		// Create disabled datePicker placeholder 
		disabledDatePicker = new JPanel(new BorderLayout());
		JButton disabledButton = new JButton("...");
		JTextField disabledDateText = new JTextField("No End Date Allowed");
		disabledDateText.setEnabled(false);
		disabledDateText.setPreferredSize(new Dimension(130, 26));
		disabledDateText.setBackground(Color.LIGHT_GRAY);
		disabledButton.setEnabled(false);
		disabledButton.setPreferredSize(new Dimension(30, 26));
		disabledDatePicker.add(disabledDateText, BorderLayout.LINE_START);
		disabledDatePicker.add(disabledButton, BorderLayout.LINE_END);

		// Setup colors and initial values for the panel elements
		textFieldDescription.setToolTipText("");
		textFieldDescription.setText(this.pokerSession.getDescription());
		textFieldSessionField.setText(this.pokerSession.getName());
		textFieldSessionField.setColumns(10);
		textFieldDescription.setColumns(10);
		comboTime.setBackground(Color.WHITE);
		comboAMPM.setBackground(Color.WHITE);
		comboTime.setEnabled(false);
		comboAMPM.setEnabled(false);
		comboDeck.setBackground(Color.WHITE);
		lblSessionEndTime.setForeground(Color.BLACK);
		comboAMPM.setModel(new DefaultComboBoxModel<String>(new String[] {"AM","PM"}));
		descriptionErrorMessage.setForeground(Color.RED);
		nameErrorMessage.setForeground(Color.RED);
		dateErrorMessage.setForeground(Color.RED);
		requirementPanel.setSelectedRequirements(this.pokerSession.getRequirementIDs());

		// Apply all of the constraints
		firstPanelLayout.putConstraint(SpringLayout.SOUTH, btnNext, -10, SpringLayout.SOUTH, firstPanel);
		firstPanelLayout.putConstraint(SpringLayout.EAST, btnNext, -10, SpringLayout.EAST, firstPanel);

		firstPanelLayout.putConstraint(SpringLayout.NORTH, lblSessionName, 10, SpringLayout.NORTH, firstPanel);
		firstPanelLayout.putConstraint(SpringLayout.WEST, lblSessionName, 10, SpringLayout.WEST, firstPanel);		

		firstPanelLayout.putConstraint(SpringLayout.NORTH, textFieldSessionField, 6, SpringLayout.SOUTH, lblSessionName);
		firstPanelLayout.putConstraint(SpringLayout.WEST, textFieldSessionField, 10, SpringLayout.WEST, firstPanel);
		firstPanelLayout.putConstraint(SpringLayout.EAST, textFieldSessionField, -10, SpringLayout.EAST, firstPanel);		

		firstPanelLayout.putConstraint(SpringLayout.NORTH, lblSessionDescription, 6, SpringLayout.SOUTH, textFieldSessionField);
		firstPanelLayout.putConstraint(SpringLayout.WEST, lblSessionDescription, 0, SpringLayout.WEST, lblSessionName);

		firstPanelLayout.putConstraint(SpringLayout.NORTH, textFieldDescription, 6, SpringLayout.SOUTH, lblSessionDescription);
		firstPanelLayout.putConstraint(SpringLayout.WEST, textFieldDescription, 0, SpringLayout.WEST, lblSessionName);
		firstPanelLayout.putConstraint(SpringLayout.SOUTH, textFieldDescription, -225, SpringLayout.SOUTH, firstPanel);
		firstPanelLayout.putConstraint(SpringLayout.EAST, textFieldDescription, -10, SpringLayout.EAST, firstPanel);					

		firstPanelLayout.putConstraint(SpringLayout.WEST, endDateCheckBox, 0, SpringLayout.WEST, lblSessionName);
		firstPanelLayout.putConstraint(SpringLayout.NORTH, endDateCheckBox, 6, SpringLayout.SOUTH, textFieldDescription);

		firstPanelLayout.putConstraint(SpringLayout.NORTH, lblEndDate, 6, SpringLayout.SOUTH, endDateCheckBox);
		firstPanelLayout.putConstraint(SpringLayout.WEST, lblEndDate, 0, SpringLayout.WEST, lblSessionName);

		firstPanelLayout.putConstraint(SpringLayout.WEST, lblSessionEndTime, 0, SpringLayout.WEST, lblSessionName);

		firstPanelLayout.putConstraint(SpringLayout.NORTH, comboTime, 6, SpringLayout.SOUTH, lblSessionEndTime);
		firstPanelLayout.putConstraint(SpringLayout.WEST, comboTime, 0, SpringLayout.WEST, lblSessionName);
		firstPanelLayout.putConstraint(SpringLayout.EAST, comboTime, 80, SpringLayout.WEST, lblSessionName);

		firstPanelLayout.putConstraint(SpringLayout.WEST, comboAMPM, 6, SpringLayout.EAST, comboTime);
		firstPanelLayout.putConstraint(SpringLayout.SOUTH, comboAMPM, 0, SpringLayout.SOUTH, comboTime);
		firstPanelLayout.putConstraint(SpringLayout.EAST, comboAMPM, 80, SpringLayout.EAST, comboTime);						

		firstPanelLayout.putConstraint(SpringLayout.NORTH, lblDeck, 6, SpringLayout.SOUTH, comboTime);
		firstPanelLayout.putConstraint(SpringLayout.WEST, lblDeck, 0, SpringLayout.WEST, lblSessionName);

		firstPanelLayout.putConstraint(SpringLayout.NORTH, comboDeck, 6, SpringLayout.SOUTH, lblDeck);
		firstPanelLayout.putConstraint(SpringLayout.WEST, comboDeck, 0, SpringLayout.WEST, lblDeck);
		firstPanelLayout.putConstraint(SpringLayout.EAST, comboDeck, 0, SpringLayout.EAST, comboAMPM);

		firstPanelLayout.putConstraint(SpringLayout.NORTH, numbers, 6, SpringLayout.NORTH, comboDeck);
		firstPanelLayout.putConstraint(SpringLayout.WEST, numbers, 6, SpringLayout.EAST, comboDeck);

		firstPanelLayout.putConstraint(SpringLayout.NORTH, nameErrorMessage, 0, SpringLayout.NORTH, lblSessionName);
		firstPanelLayout.putConstraint(SpringLayout.WEST, nameErrorMessage, 20, SpringLayout.EAST, lblSessionName);

		firstPanelLayout.putConstraint(SpringLayout.NORTH, descriptionErrorMessage, 0, SpringLayout.NORTH, lblSessionDescription);
		firstPanelLayout.putConstraint(SpringLayout.WEST, descriptionErrorMessage, 20, SpringLayout.EAST, lblSessionDescription);

		firstPanelLayout.putConstraint(SpringLayout.NORTH, dateErrorMessage, 0, SpringLayout.NORTH, lblEndDate);
		firstPanelLayout.putConstraint(SpringLayout.WEST, dateErrorMessage, 20, SpringLayout.EAST, lblEndDate);

		// Handle the time dropdowns
		populateTimeDropdown();
		parseTimeDropdowns();
		setDeckDropdown();
		// Set the default deck name for the session
		try {
			comboDeck.setSelectedItem(this.sessionDeck.getDeckName());
			parseDeckDropdowns();
		}
		// if the session is being created or the default deck is used
		catch (NullPointerException ex) {};

		haveEndDate = handleCheckBox();
		if ((viewMode == ViewMode.EDITING) && (pokerSession.hasEndDate())) {
			setTimeDropdown();
		}

		// Time dropdown event handler
		comboTime.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				parseTimeDropdowns();
			}
		});

		// AM/PM dropdown event handler
		comboAMPM.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				parseTimeDropdowns();
			}
		});

		comboDeck.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				parseDeckDropdowns();
			}
		});

		endDateCheckBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				haveEndDate = handleCheckBox();
			}
		});

		datePicker.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dateHasBeenSet = true;
				validateFields();
			}
		});

		// Next button event handler
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Save the fields and validate them
				if (validateFields()) {
					displayPanel(secondPanel);
				}
			}
		});
		
		// Add all of the elements to the first panel
		firstPanel.add(btnNext);
		firstPanel.add(lblDeck);
		firstPanel.add(lblSessionEndTime);
		firstPanel.add(lblSessionName);
		firstPanel.add(textFieldSessionField);
		firstPanel.add(lblSessionDescription);
		firstPanel.add(textFieldDescription);
		firstPanel.add(lblEndDate);
		firstPanel.add(comboTime);
		firstPanel.add(comboAMPM);
		firstPanel.add(comboDeck);
		firstPanel.add(numbers);
		firstPanel.add(descriptionErrorMessage);
		firstPanel.add(nameErrorMessage);
		firstPanel.add(dateErrorMessage);
		firstPanel.add(endDateCheckBox);
	}


	/**
	 * Builds the second screen for creating/editing a session.
	 * This layout displays the requirements to select for a planning poker session.
	 */
	private void buildSecondPanel() {
		secondPanel.setLayout(secondPanelLayout);

		JButton btnSubmit = new JButton("Submit");
		JButton btnBack = new JButton("Back");

		//Position the error message for requirements
		secondPanelLayout.putConstraint(SpringLayout.SOUTH, norequirements, -15, SpringLayout.SOUTH, secondPanel);
		secondPanelLayout.putConstraint(SpringLayout.EAST, norequirements, -110, SpringLayout.EAST, secondPanel);

		// Position the requirements panel
		secondPanelLayout.putConstraint(SpringLayout.NORTH, requirementPanel, 10, SpringLayout.NORTH, secondPanel);
		secondPanelLayout.putConstraint(SpringLayout.WEST, requirementPanel, 10, SpringLayout.WEST, secondPanel);
		secondPanelLayout.putConstraint(SpringLayout.SOUTH, requirementPanel, -50, SpringLayout.SOUTH, secondPanel);
		secondPanelLayout.putConstraint(SpringLayout.EAST, requirementPanel, -10, SpringLayout.EAST, secondPanel);

		// Position the submit button
		secondPanelLayout.putConstraint(SpringLayout.SOUTH, btnSubmit, -10, SpringLayout.SOUTH, secondPanel);
		secondPanelLayout.putConstraint(SpringLayout.EAST, btnSubmit, -10, SpringLayout.EAST, secondPanel);

		// Position the back button
		secondPanelLayout.putConstraint(SpringLayout.SOUTH, btnBack, -10, SpringLayout.SOUTH, secondPanel);
		secondPanelLayout.putConstraint(SpringLayout.WEST, btnBack, 10, SpringLayout.WEST, secondPanel);


		// Submit button event handler
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				List<Requirement> requirements =  requirementPanel.getSelected();
				if (requirements.isEmpty()) {
					norequirements.setText("Requirements must be selected before creating the session.");
					norequirements.setForeground(Color.RED);
					secondPanel.revalidate();
					secondPanel.repaint();
				} else { 
					submitSession = true;
					saveFields();
					pokerSession.setOpen(true);
					submitSessionToDatabase();
					norequirements.setText("");
					if (viewMode == ViewMode.CREATING){
						MockNotification mock = new MockNotification();
						mock.sessionStartedNotification();
					}
				}
			}
		});

		// Back button event handler
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveFields();
				displayPanel(firstPanel);
			}
		});

		// Add all of the elements to the second panel
		secondPanel.add(btnSubmit);
		secondPanel.add(btnBack);
		secondPanel.add(requirementPanel);
		secondPanel.add(norequirements);
	}

	/**
	 * Makes the second screen the visible panel
	 */
	private void displayPanel(JPanel newPanel) {
		// Remove the old panel
		this.remove(firstPanel);
		this.remove(secondPanel);

		// Position the new panel
		layout.putConstraint(SpringLayout.NORTH, newPanel, 10, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, newPanel, 10, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.SOUTH, newPanel, -10, SpringLayout.SOUTH, this);
		layout.putConstraint(SpringLayout.EAST, newPanel, -10, SpringLayout.EAST, this);

		// Add the new panel
		this.add(newPanel);

		// Revalidate and repaint the panels
		this.revalidate();
		this.repaint();
		newPanel.revalidate();
		newPanel.repaint();
	}

	/**
	 * populates the drop down menu for the time
	 */
	public void populateTimeDropdown(){
		comboTime.setModel(new DefaultComboBoxModel<String>(availableTimes));
	}

	public void setTimeDropdown() {
		int hour = pokerSession.getEndDate().get(Calendar.HOUR_OF_DAY);
		int minute = pokerSession.getEndDate().get(Calendar.MINUTE);
		String ampm = "AM";
		if (hour > 12) {
			hour -= 12;
			ampm = "PM";
		} else if (hour == 0) {
			hour = 12;
		}
		String selectedHour = String.format("%d:%02d", hour, minute);
		comboTime.setSelectedItem(selectedHour);
		comboAMPM.setSelectedItem(ampm);
	}

	public void setDeckDropdown(){
		List<Deck> projectDecks = DeckListModel.getInstance().getDecks();
		String[] deckNames = new String[projectDecks.size() + 1];
		deckNames[0] = "None";
		int i = 1;
		for(Deck d:projectDecks){
			deckNames[i] = d.getDeckName();
			i++;
		}
		comboDeck.setModel(new DefaultComboBoxModel<String>(deckNames));
	}

	/**
	 * takes the strings from the time dropdown menus, parses the strings, and saves the hour and minute to the appopriate fields
	 */
	public void parseTimeDropdowns(){
		String stringTime = (String) comboTime.getSelectedItem();
		String stringAMPM = (String) comboAMPM.getSelectedItem();
		if (!(stringTime.equals("Time"))){
			String[] partsOfTime = stringTime.split(":");
			endMinutes = Integer.parseInt(partsOfTime[1]);
			endHour = Integer.parseInt(partsOfTime[0]);
			if (endHour == 12){
				endHour -= 12;
			}
			if (stringAMPM.equals("PM")){
				endHour += 12;
			}
		}
	}

	public void parseDeckDropdowns(){
		String deckName = (String) comboDeck.getSelectedItem();
		List<Deck> projectDecks = DeckListModel.getInstance().getDecks();
		String deckNumbers = "";
		if (deckName.equals("None")){
			isUsingDeck = false;
			sessionDeck = null;
			deckNumbers = "Users input non-negative integers";
		}
		else {
			for (Deck d:projectDecks){
				if (deckName.equals(d.getDeckName())){
					sessionDeck = d;
					isUsingDeck = true;
					deckNumbers = d.changeNumbersToString();	
				}
			}
		}
		numbers.setText(deckNumbers);
		numbers.revalidate();
		numbers.repaint();
	}

	/**
	 * Submits the current session to the database as a new planning poker session
	 * and removes this tab.
	 */
	private void submitSessionToDatabase() {
		if (viewMode == ViewMode.CREATING) {
			PlanningPokerSessionModel.getInstance().addPlanningPokerSession(pokerSession);
		} else if (viewMode == ViewMode.EDITING) {
			PlanningPokerSessionModel.getInstance().updatePlanningPokerSession(pokerSession);
		}
		this.removeAll(); // This should prevent the date picker from remaining on the screen
		ViewEventController.getInstance().removeTab(this);// this thing closes the tabs
	}

	/**
	 * @return the session that is currently being modified
	 */
	public PlanningPokerSession getDisplaySession() {
		return pokerSession;
	}

	private boolean validateFields() {
		// Save the fields
		saveFields();

		// Validate the fields to find all the errors
		ArrayList<CreatePokerSessionErrors> errors;
		errors = pokerSession.validateFields(haveEndDate, dateHasBeenSet);

		// If there are no errors
		if (errors.size() == 0) {
			descriptionErrorMessage.setText("");
			nameErrorMessage.setText("");
			dateErrorMessage.setText("");
			return true;
		} else { 
			// Display all error messages
			// Handle description error
			if (errors.contains(CreatePokerSessionErrors.NoDescription)){
				descriptionErrorMessage.setText("Please enter a description");
			} else {
				descriptionErrorMessage.setText("");
			}

			// Handle name error
			if (errors.contains(CreatePokerSessionErrors.NoName)){
				nameErrorMessage.setText("Please enter a name");
			} else {
				nameErrorMessage.setText("");
			}

			// Handle date errors
			if (errors.contains(CreatePokerSessionErrors.EndDateTooEarly)){
				dateErrorMessage.setText("Please enter a date after the current date");
			} else if (errors.contains(CreatePokerSessionErrors.NoDateSelected)){
				dateErrorMessage.setText("Please select a date or disable end date");
			} else {
				dateErrorMessage.setText("");
			}

			return false;
		}
	}

	/**
	 * Saves the current values in the panel to the PlanningPokerSession object
	 */
	public void saveFields() {
		int year = datePicker.getModel().getYear();
		int month = datePicker.getModel().getMonth();
		int day = datePicker.getModel().getDay();

		pokerSession.setName(textFieldSessionField.getText());
		pokerSession.setDescription(textFieldDescription.getText());
		pokerSession.setSessionDeck(sessionDeck);
		pokerSession.setUsingDeck(isUsingDeck);
		pokerSession.setSessionCreatorName(ConfigManager.getConfig().getUserName());
		pokerSession.setRequirements(requirementPanel.getSelected());

		if (haveEndDate) {
			pokerSession.setEndDate(new GregorianCalendar(year, month, day, endHour, endMinutes));
		} else {
			pokerSession.setEndDate(null);
		}
	}
	/**
	 * this method gets the contents of the checkbox and if it is unchecked, disables the time dropdown menus, 
	 * and removes the end date related error messages
	 * if it is checked, it enables the time dropdowns and shows the pertinent date error message again
	 * @return boolean indicating if the user wants to specify an end date
	 */
	public boolean handleCheckBox() {
		boolean boxChecked = endDateCheckBox.isSelected();

		comboAMPM.setEnabled(boxChecked);
		comboTime.setEnabled(boxChecked);
		dateErrorMessage.setVisible(boxChecked);

		if (boxChecked){
			parseTimeDropdowns();
			enableDatePicker();
		}
		else {
			disableDatePicker();
		}

		firstPanel.revalidate();
		firstPanel.repaint();
		return boxChecked;
	}

	/**
	 * Disables the date picker button from opening the calendar
	 */
	private void disableDatePicker() {
		// Remove the datePicker from the layout
		firstPanelLayout.removeLayoutComponent((Component) datePicker);
		firstPanel.remove((JPanel) datePicker);
		
		// Add a disabled button in place of the datePicker
		firstPanelLayout.putConstraint(SpringLayout.NORTH, disabledDatePicker, 6, SpringLayout.SOUTH, lblEndDate);
		firstPanelLayout.putConstraint(SpringLayout.WEST, disabledDatePicker, 0, SpringLayout.WEST, lblEndDate);	
		firstPanelLayout.putConstraint(SpringLayout.EAST, disabledDatePicker, 0, SpringLayout.EAST, comboAMPM);
		firstPanelLayout.putConstraint(SpringLayout.NORTH, lblSessionEndTime, 6, SpringLayout.SOUTH, disabledDatePicker);
		firstPanel.add(disabledDatePicker);
		

	}

	/**
	 * Enables the date picker button to open the calendar
	 */
	private void enableDatePicker() {
		// Remve the datePicker placeholder
		firstPanelLayout.removeLayoutComponent(disabledDatePicker);
		firstPanel.remove(disabledDatePicker);
		
		// Re-add the datePicker
		firstPanelLayout.putConstraint(SpringLayout.NORTH, (JPanel) datePicker, 6, SpringLayout.SOUTH, lblEndDate);
		firstPanelLayout.putConstraint(SpringLayout.WEST, (JPanel) datePicker, 0, SpringLayout.WEST, lblEndDate);	
		firstPanelLayout.putConstraint(SpringLayout.EAST, (JPanel) datePicker, 0, SpringLayout.EAST, comboAMPM);
		firstPanelLayout.putConstraint(SpringLayout.NORTH, lblSessionEndTime, 6, SpringLayout.SOUTH, (JPanel) datePicker);
		firstPanel.add((JPanel) datePicker);
	}
	

	/**
	 * @return Returns if no fields in the panel have been changed
	 */
	public boolean readyToRemove()
	{
		boolean fieldsChanged = false;
		// Check if the submit button was clicked
		if (this.submitSession) {
			return true;
		}
		// Otherwise check if data was modified
		if (this.viewMode == ViewMode.CREATING) {
			fieldsChanged = anythingChangedCreating();
		}
		else {
			fieldsChanged = anythingChangedEditing();
		}
		// If no fields were changed, it can be removed
		if (!fieldsChanged) {
			return true;
		}
		// If fields were changed, confirm with user that they want the tab removed.
		else {
			int result = JOptionPane.showConfirmDialog(this, "Discard unsaved changes and close tab?", "Discard Changes?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			
			return (result == 0);
		}
		
	}

	
	/**
	 * 
	 * @return Returns whether any fields in the panel have been changed when creating a session
	 */
	private boolean anythingChangedCreating() {		
		// Check if the user has changed the session name
		if (!textFieldSessionField.getText().equals(pokerSession.getDefaultName())) {
			System.out.println("Name is not the default: " + pokerSession.getName());
			if (!textFieldSessionField.getText().equals("")) {
				System.out.println("Name is not empty" + textFieldDescription.getText());
				return true;
			}
		}
		// Check if the user has changed the description
		if (!(textFieldDescription.getText().equals("")))
			return true;

		return false;
	}

	
	/**
	 * 
	 * @return whether any fields have been changed.
	 */
	private boolean anythingChangedEditing() {
		// Check if the user has changed the session name
		if (!(textFieldSessionField.getText().equals(pokerSession.getName())))
			return true;
		// Check if the user has changed the description
		if (!(textFieldDescription.getText().equals(pokerSession.getDescription())))
			return true;

		return false;
	}
	
}
