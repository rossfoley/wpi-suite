/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.session;

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
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import net.sourceforge.jdatepicker.JDateComponentFactory;
import net.sourceforge.jdatepicker.JDatePicker;
import net.sourceforge.jdatepicker.impl.UtilCalendarModel;
import edu.wpi.cs.wpisuitetng.janeway.config.ConfigManager;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.controller.GetEmailController;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.Deck;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.DeckListModel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.EmailAddress;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.EmailAddressModel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession.SessionState;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSessionModel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.notifications.Mailer;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.ISessionTab;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.ViewEventController;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.deck.CreateDeck;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.deck.DeckEvent;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.deck.DeckListener;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.requirementselection.RequirementSelectionView;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.requirementselection.RequirementsSelectedEvent;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.requirementselection.RequirementsSelectedListener;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.RequirementModel;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.requirements.ViewMode;

/**
 * The GUI for creating and updating Planning Poker Sessions
 * 
 */
public class PlanningPokerSessionTab extends JPanel implements ISessionTab {
	private final PlanningPokerSession pokerSession;
	private final PlanningPokerSession unmodifiedSession = new PlanningPokerSession();
	private final SpringLayout layout = new SpringLayout();
	private final SpringLayout firstPanelLayout = new SpringLayout();
	private final SpringLayout secondPanelLayout = new SpringLayout();
	private final JSplitPane firstPanel = new JSplitPane();
	private final JPanel sessionDetailPanel = new JPanel();
	private final JPanel secondPanel = new JPanel();
	private CreateDeck createDeckPanel;

	private final ViewMode viewMode;
	private final JComboBox<String> comboTime = new JComboBox<String>();
	private final JComboBox<String> comboAMPM = new JComboBox<String>();
	private final JComboBox<String> comboDeck = new JComboBox<String>();
	private final JTextField textFieldSessionField = new JTextField();
	private final JTextArea textFieldDescription = new JTextArea();
	private final JLabel dateErrorMessage = new JLabel("");
	private final JLabel nameErrorMessage = new JLabel("");
	private final JLabel descriptionErrorMessage = new JLabel("");
	private final JLabel numbers = new JLabel("Users input non-negative intergers");
	private final RequirementSelectionView requirementPanel;
	private JDatePicker datePicker;
	private final JCheckBox endDateCheckBox = new JCheckBox("End Date and Time?");
	JLabel norequirements = new JLabel("Please select requirements before creating the session.");
	private JPanel disabledDatePicker;
	JButton btnCreateDeck;

	private boolean dateHasBeenSet;
	private boolean haveEndDate;
	private boolean submitSession = false;
	private int endHour;
	private int endMinutes;
	private boolean isUsingDeck;
	private int sessionDeckID;
	private Deck sessionDeck;
	private JLabel lblSessionEndTime;
	private JLabel lblEndDate;
	private final String[] availableTimes = new String[] { "12:00", "12:30", "1:00", "1:30", "2:00", "2:30", "3:00", "3:30", 
			"4:00", "4:30", "5:00", "5:30", "6:00", "6:30", "7:00", "7:30", 
			"8:00", "8:30", "9:00", "9:30", "10:00", "10:30", "11:00", "11:30" };
	private boolean editedDescription;
	private Set<Integer> originalReqs;


	/**
	 * Constructor for creating a planning poker session
	 */
	public PlanningPokerSessionTab() {
		pokerSession = new PlanningPokerSession();
		originalReqs = new HashSet<Integer>();
		requirementPanel = new RequirementSelectionView(null);
		viewMode = (ViewMode.CREATING);
		dateHasBeenSet = false;
		this.buildLayouts();
		this.displayPanel(firstPanel);
		editedDescription = false;
	}

	/**
	 * Constructor for editing an existing planning poker session
	 * @param existingSession the session that you want to edit
	 */
	public PlanningPokerSessionTab(PlanningPokerSession existingSession) {
		pokerSession = existingSession;
		originalReqs = pokerSession.getRequirementIDs();
		requirementPanel = new RequirementSelectionView(pokerSession);
		viewMode = (ViewMode.EDITING);
		// Set the end date checkbox and update fields.
		dateHasBeenSet = (existingSession.getEndDate() != null);
		endDateCheckBox.setSelected(dateHasBeenSet);
		// Update the fields current deck being used
		isUsingDeck = existingSession.isUsingDeck();
		sessionDeckID = existingSession.getSessionDeckID();
		sessionDeck = DeckListModel.getInstance().getDeck(sessionDeckID);

		// Create 
		unmodifiedSession.copyFrom(existingSession);

		editedDescription = true;

		buildLayouts();
		displayPanel(firstPanel);
	}

	private void buildLayouts() {
		// Apply the layout and build the panels
		setLayout(layout);
		buildFirstPanel();
		buildSecondPanel();
	}

	/**
	 * Builds the first screen for creating/editing a session
	 */
	private void buildFirstPanel() {
		buildSessionDetailPanel();

		createDeckPanel = new CreateDeck();
		createDeckPanel.addDeckListener(new DeckListener() {
			@Override
			public void deckSubmitted(DeckEvent e) {
				if (e.getDeck() == null) {
					closeCreateDeckPanel();
				}
				else {
					newDeckCreated(e.getDeck());
				}
			}
		});

		firstPanel.setLeftComponent(sessionDetailPanel);
		firstPanel.setRightComponent(null);
		final int dividerLocation = firstPanel.getSize().width - 
				firstPanel.getInsets().right - firstPanel.getDividerSize() - createDeckPanel.getPreferredSize().width;
		firstPanel.setDividerLocation(dividerLocation);
		firstPanel.setResizeWeight(1.0);
	}

	/**
	 * Opens the Create Deck panel in the right divider of the first panel 
	 */
	private void openCreateDeckPanel() {
		firstPanel.setRightComponent(createDeckPanel);
		final int dividerLocation = firstPanel.getSize().width - 
				firstPanel.getInsets().right - firstPanel.getDividerSize() - createDeckPanel.getPreferredSize().width;
		firstPanel.setDividerLocation(dividerLocation);

		btnCreateDeck.setEnabled(false);
	}

	/**
	 * Close the right divider of the first panel after a new deck has been created
	 */
	private void newDeckCreated(Deck newDeck) {
		createDeckPanel = new CreateDeck();
		createDeckPanel.addDeckListener(new DeckListener() {
			@Override
			public void deckSubmitted(DeckEvent e) {
				if (e.getDeck() == null) {
					closeCreateDeckPanel();
				}
				else {
					newDeckCreated(e.getDeck());
				}
			}
		});
		// Update deck combo box
		setDeckDropdown();
		// Set the default deck name for the session
		try {
			comboDeck.setSelectedItem(newDeck.getDeckName());
			parseDeckDropdowns();
		} catch (NullPointerException ex) {} // if the session is being created

		closeCreateDeckPanel();
	}

	/**
	 * Closes the create deck panel
	 */
	public void closeCreateDeckPanel() {
		firstPanel.setRightComponent(null);
		btnCreateDeck.setEnabled(true);
	}

	/**
	 * Builds the panel for setting the session name, description, endDate, and deck
	 */
	private void buildSessionDetailPanel() {
		// Set the layout and colors
		sessionDetailPanel.setLayout(firstPanelLayout);
		sessionDetailPanel.setForeground(Color.BLACK);

		// Initialize all of the fields on the panel
		final JLabel lblSessionName = new JLabel("Session Name: *");
		final JLabel lblSessionDescription = new JLabel("Session Description: *");
		lblEndDate = new JLabel("Session End Date:");
		lblSessionEndTime = new JLabel("Session End Time:");
		final JLabel lblDeck = new JLabel("Deck:");

		final JButton btnNext = new JButton("Next >");
		if (viewMode == ViewMode.CREATING) {
			btnNext.setEnabled(false);
		}
		final JButton btnCancel = new JButton("Cancel");
		datePicker = JDateComponentFactory.createJDatePicker(new UtilCalendarModel(pokerSession.getEndDate()));

		// Create disabled datePicker placeholder 
		disabledDatePicker = new JPanel(new BorderLayout());
		final JButton disabledButton = new JButton("...");
		final JTextField disabledDateText = new JTextField("No End Date Allowed");
		disabledDateText.setEnabled(false);
		disabledDateText.setPreferredSize(new Dimension(130, 26));
		disabledDateText.setBackground(Color.LIGHT_GRAY);
		disabledButton.setEnabled(false);
		disabledButton.setPreferredSize(new Dimension(30, 26));
		disabledDatePicker.add(disabledDateText, BorderLayout.LINE_START);
		disabledDatePicker.add(disabledButton, BorderLayout.LINE_END);

		// Setup colors and initial values for the panel elements
		final JScrollPane descrScroll = new JScrollPane();		
		textFieldDescription.setToolTipText("");
		textFieldDescription.setText(pokerSession.getDescription());
		textFieldSessionField.setText(pokerSession.getName());
		textFieldSessionField.setColumns(10);
		textFieldDescription.setColumns(10);
		textFieldDescription.setLineWrap(true);
		textFieldDescription.setWrapStyleWord(true);
		descrScroll.setViewportView(textFieldDescription);
		comboTime.setBackground(Color.WHITE);
		comboAMPM.setBackground(Color.WHITE);
		comboTime.setEnabled(false);
		comboAMPM.setEnabled(false);
		comboDeck.setBackground(Color.WHITE);
		lblSessionEndTime.setForeground(Color.BLACK);
		comboAMPM.setModel(new DefaultComboBoxModel<String>(new String[] {"AM","PM"}));
		nameErrorMessage.setForeground(Color.RED);
		dateErrorMessage.setForeground(Color.RED);
		requirementPanel.setSelectedRequirements(pokerSession.getRequirementIDs());

		// Button to create a deck
		btnCreateDeck = new JButton("Create New Deck");
		btnCreateDeck.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				openCreateDeckPanel();
			}
		});

		// Apply all of the constraints
		firstPanelLayout.putConstraint(SpringLayout.SOUTH, btnNext, -10, SpringLayout.SOUTH, sessionDetailPanel);
		firstPanelLayout.putConstraint(SpringLayout.EAST, btnNext, -10, SpringLayout.EAST, sessionDetailPanel);

		firstPanelLayout.putConstraint(SpringLayout.NORTH, lblSessionName, 10, SpringLayout.NORTH, sessionDetailPanel);
		firstPanelLayout.putConstraint(SpringLayout.WEST, lblSessionName, 10, SpringLayout.WEST, sessionDetailPanel);		

		firstPanelLayout.putConstraint(SpringLayout.NORTH, textFieldSessionField, 6, SpringLayout.SOUTH, lblSessionName);
		firstPanelLayout.putConstraint(SpringLayout.WEST, textFieldSessionField, 10, SpringLayout.WEST, sessionDetailPanel);
		firstPanelLayout.putConstraint(SpringLayout.EAST, textFieldSessionField, -10, SpringLayout.EAST, sessionDetailPanel);		

		firstPanelLayout.putConstraint(SpringLayout.NORTH, lblSessionDescription, 6, SpringLayout.SOUTH, textFieldSessionField);
		firstPanelLayout.putConstraint(SpringLayout.WEST, lblSessionDescription, 0, SpringLayout.WEST, lblSessionName);

		firstPanelLayout.putConstraint(SpringLayout.NORTH, descrScroll, 6, SpringLayout.SOUTH, lblSessionDescription);
		firstPanelLayout.putConstraint(SpringLayout.WEST, descrScroll, 0, SpringLayout.WEST, lblSessionName);
		firstPanelLayout.putConstraint(SpringLayout.SOUTH, descrScroll, -250, SpringLayout.SOUTH, sessionDetailPanel);
		firstPanelLayout.putConstraint(SpringLayout.EAST, descrScroll, -10, SpringLayout.EAST, sessionDetailPanel);					

		firstPanelLayout.putConstraint(SpringLayout.WEST, endDateCheckBox, 0, SpringLayout.WEST, lblSessionName);
		firstPanelLayout.putConstraint(SpringLayout.NORTH, endDateCheckBox, 6, SpringLayout.SOUTH, descrScroll);

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

		firstPanelLayout.putConstraint(SpringLayout.WEST, btnCreateDeck, 0, SpringLayout.WEST, comboDeck);
		firstPanelLayout.putConstraint(SpringLayout.NORTH, btnCreateDeck, 10, SpringLayout.SOUTH, comboDeck);
		firstPanelLayout.putConstraint(SpringLayout.EAST, btnCreateDeck, 0, SpringLayout.EAST, comboDeck);

		firstPanelLayout.putConstraint(SpringLayout.NORTH, numbers, 6, SpringLayout.NORTH, comboDeck);
		firstPanelLayout.putConstraint(SpringLayout.WEST, numbers, 6, SpringLayout.EAST, comboDeck);

		firstPanelLayout.putConstraint(SpringLayout.NORTH, nameErrorMessage, 0, SpringLayout.NORTH, lblSessionName);
		firstPanelLayout.putConstraint(SpringLayout.WEST, nameErrorMessage, 20, SpringLayout.EAST, lblSessionName);

		firstPanelLayout.putConstraint(SpringLayout.NORTH, descriptionErrorMessage, 0, SpringLayout.NORTH, lblSessionDescription);
		firstPanelLayout.putConstraint(SpringLayout.WEST, descriptionErrorMessage, 20, SpringLayout.EAST, lblSessionDescription);

		firstPanelLayout.putConstraint(SpringLayout.NORTH, dateErrorMessage, 0, SpringLayout.NORTH, lblEndDate);
		firstPanelLayout.putConstraint(SpringLayout.WEST, dateErrorMessage, 20, SpringLayout.EAST, lblEndDate);

		// Position the cancel button
		firstPanelLayout.putConstraint(SpringLayout.SOUTH, btnCancel, 0, SpringLayout.SOUTH, btnNext);
		firstPanelLayout.putConstraint(SpringLayout.EAST, btnCancel, -85, SpringLayout.EAST, btnNext);


		// Handle the time dropdowns
		populateTimeDropdown();
		parseTimeDropdowns();
		setDeckDropdown();
		// Set the default deck name for the session
		try {
			comboDeck.setSelectedItem(sessionDeck.getDeckName());
			parseDeckDropdowns();
		}
		// if the session is being created or the default deck is used
		catch (NullPointerException ex) {}

		haveEndDate = handleCheckBox();
		if ((viewMode == ViewMode.EDITING) && (pokerSession.hasEndDate())) {
			setTimeDropdown();
		}

		// Handle changes in session name field
		textFieldSessionField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				if (!validateFields()) {
					btnNext.setEnabled(false);
				}
				else {
					btnNext.setEnabled(true);
				}
			}
			@Override
			public void insertUpdate(DocumentEvent e) {
				if (!validateFields()) {
					btnNext.setEnabled(false);
				}
				else {
					btnNext.setEnabled(true);
				}
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
				if (!validateFields()) {
					btnNext.setEnabled(false);
				}
				else {
					btnNext.setEnabled(true);
				}
			}
		});
		// Handle changes in description field
		textFieldDescription.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
				editedDescription = true;
				if (!validateFields()) {
					btnNext.setEnabled(false);
					descriptionErrorMessage.setForeground(Color.RED);
				}
				else {
					btnNext.setEnabled(true);
				}
			}
			@Override
			public void insertUpdate(DocumentEvent e) {
				editedDescription = true;
				if(!validateFields()) {
					btnNext.setEnabled(false);
					descriptionErrorMessage.setForeground(Color.RED);
				}
				else {
					btnNext.setEnabled(true);
				}
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
				editedDescription = true;
				if (!validateFields()) {
					btnNext.setEnabled(false);
					descriptionErrorMessage.setForeground(Color.RED);
				}
				else {
					btnNext.setEnabled(true);
				}
			}
		});

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

		// Cancel button event handler
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closePanel();
			}
		});

		// Add all of the elements to the first panel
		sessionDetailPanel.add(btnNext);
		sessionDetailPanel.add(btnCancel);
		sessionDetailPanel.add(lblDeck);
		sessionDetailPanel.add(lblSessionEndTime);
		sessionDetailPanel.add(lblSessionName);
		sessionDetailPanel.add(textFieldSessionField);
		sessionDetailPanel.add(lblSessionDescription);
		sessionDetailPanel.add(descrScroll);
		sessionDetailPanel.add(lblEndDate);
		sessionDetailPanel.add(comboTime);
		sessionDetailPanel.add(comboAMPM);
		sessionDetailPanel.add(comboDeck);
		sessionDetailPanel.add(btnCreateDeck);
		sessionDetailPanel.add(numbers);
		sessionDetailPanel.add(descriptionErrorMessage);
		sessionDetailPanel.add(nameErrorMessage);
		sessionDetailPanel.add(dateErrorMessage);
		sessionDetailPanel.add(endDateCheckBox);
	}


	/**
	 * Builds the second screen for creating/editing a session.
	 * This layout displays the requirements to select for a planning poker session.
	 */
	private void buildSecondPanel() {
		secondPanel.setLayout(secondPanelLayout);
		
		final JButton btnSave = new JButton("Save as Pending");
		final JButton btnBack = new JButton("Back");
		final JButton btnStart = new JButton("Start");
		final JButton btnCancel = new JButton("Cancel");

		// Disable buttons if in creating mode (since no requirements are selected yet) 
		if (viewMode == ViewMode.CREATING) {
			btnStart.setEnabled(false);
			btnSave.setEnabled(false);
		}
		else {
			norequirements.setVisible(false);
		}

		//Position the error message for requirements
		secondPanelLayout.putConstraint(SpringLayout.SOUTH, norequirements, -5, SpringLayout.SOUTH, btnCancel);
		secondPanelLayout.putConstraint(SpringLayout.EAST, norequirements, -90, SpringLayout.EAST, btnCancel);

		// Position the requirements panel
		secondPanelLayout.putConstraint(SpringLayout.NORTH, requirementPanel, 10, SpringLayout.NORTH, secondPanel);
		secondPanelLayout.putConstraint(SpringLayout.WEST, requirementPanel, 10, SpringLayout.WEST, secondPanel);
		secondPanelLayout.putConstraint(SpringLayout.SOUTH, requirementPanel, -50, SpringLayout.SOUTH, secondPanel);
		secondPanelLayout.putConstraint(SpringLayout.EAST, requirementPanel, -10, SpringLayout.EAST, secondPanel);

		// Position the save button
		secondPanelLayout.putConstraint(SpringLayout.SOUTH, btnSave, -10, SpringLayout.SOUTH, secondPanel);
		secondPanelLayout.putConstraint(SpringLayout.EAST, btnSave, -10, SpringLayout.EAST, secondPanel);

		//Position the Start button
		secondPanelLayout.putConstraint(SpringLayout.SOUTH, btnStart, 0, SpringLayout.SOUTH, btnSave);
		secondPanelLayout.putConstraint(SpringLayout.EAST, btnStart, -10, SpringLayout.WEST, btnSave);

		// Position the back button
		secondPanelLayout.putConstraint(SpringLayout.SOUTH, btnBack, -10, SpringLayout.SOUTH, secondPanel);
		secondPanelLayout.putConstraint(SpringLayout.WEST, btnBack, 10, SpringLayout.WEST, secondPanel);

		// Position the cancel button
		secondPanelLayout.putConstraint(SpringLayout.SOUTH, btnCancel, 0, SpringLayout.SOUTH, btnStart);
		secondPanelLayout.putConstraint(SpringLayout.EAST, btnCancel, -10, SpringLayout.WEST, btnStart);



		// Save button event handler
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				List<Integer> oldRequirements = new LinkedList<Integer>();
				List<Integer> newRequirements = new LinkedList<Integer>();
				for (Integer i : originalReqs) {
					oldRequirements.add(i);
				}
				final List<Requirement> requirements =  requirementPanel.getSelected();
				for (Requirement req : requirements) {
					newRequirements.add(req.getId());
				}
				
				int changes = notifyUserOfReqChanges(oldRequirements, newRequirements);
				if (requirements.isEmpty()) {
					secondPanel.revalidate();
					secondPanel.repaint();
				} 
				
				else { 
					if (changes == 1) return;
					submitSession = true;
					saveFields();
					pokerSession.setGameState(SessionState.PENDING);
					submitSessionToDatabase();
				}
			}
		});

		// Start button event handler
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Button Start pressed!");
				final List<Requirement> requirements =  requirementPanel.getSelected();
				List<Integer> oldRequirements = new LinkedList<Integer>();
				List<Integer> newRequirements = new LinkedList<Integer>();
				for (Integer i : originalReqs) {
					oldRequirements.add(i);
				}
				for (Requirement req : requirements) {
					newRequirements.add(req.getId());
				}
				
				int changes = notifyUserOfReqChanges(oldRequirements, newRequirements);
				System.out.println("How many changes made in session: " + changes);
				if (requirements.isEmpty()) {
					secondPanel.revalidate();
					secondPanel.repaint();
				} 
				
				else {
					if (changes == 1) return;
					submitSession = true;
					saveFields();
					pokerSession.setGameState(SessionState.OPEN);
					submitSessionToDatabase();
					
					final List<String> recipients = new LinkedList<String>();
					List<EmailAddress> emailRecipients = null;

					final GetEmailController getEmailController = GetEmailController.getInstance();
					getEmailController.retrieveEmails();

					final EmailAddressModel emailAddressModel = EmailAddressModel.getInstance();
					try {
						emailRecipients = emailAddressModel.getEmailAddresses();
					}
					catch (Exception E) {

					}

					for (int i = 0; i < emailRecipients.size(); i++) {
						recipients.add(emailRecipients.get(i).getEmail());
					}
					
					final Thread t = new Thread(new Runnable() {
						@Override
						public void run() {
							final Mailer mailer = new Mailer();
							mailer.notifyOfPlanningPokerSessionStart(recipients, pokerSession);
						}
					});
					t.setDaemon(true);
					t.start();
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

		// Cancel button event handler
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closePanel();
			}
		});

		// setup action listener for the requirement selection
		requirementPanel.addRequirementsSelectedListener(new RequirementsSelectedListener() {
			@Override
			public void setRequirementsSelected(RequirementsSelectedEvent e) {
				// If requirements are selected, enable the start and save buttons
				System.out.println("Are requirements selected: " + e.areRequirementsSelected());
				if (e.areRequirementsSelected()) {
					norequirements.setVisible(false);
					btnStart.setEnabled(true);
					btnSave.setEnabled(true);
				}
				else {
					norequirements.setVisible(true);
					btnStart.setEnabled(false);
					btnSave.setEnabled(false);
				}
			}

		});
		requirementPanel.getReqPanel().fireSelectedEventCaller();
		//


		// Add all of the elements to the second panel
		secondPanel.add(btnSave);
		secondPanel.add(btnBack);
		secondPanel.add(btnStart);
		secondPanel.add(btnCancel);
		secondPanel.add(requirementPanel);
		secondPanel.add(norequirements);
	}

	/**
	 * Makes the input screen the visible panel
	 */
	private void displayPanel(JComponent newPanel) {
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
		final int minute = pokerSession.getEndDate().get(Calendar.MINUTE);
		String ampm = "AM";
		if (hour > 12) {
			hour -= 12;
			ampm = "PM";
		} else if (hour == 0) {
			hour = 12;
		}
		final String selectedHour = String.format("%d:%02d", hour, minute);
		comboTime.setSelectedItem(selectedHour);
		comboAMPM.setSelectedItem(ampm);
	}

	public void setDeckDropdown(){
		final List<Deck> projectDecks = DeckListModel.getInstance().getDecks();
		final String[] deckNames = new String[projectDecks.size() + 1];
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
		final String stringTime = (String) comboTime.getSelectedItem();
		final String stringAMPM = (String) comboAMPM.getSelectedItem();
		if (!(stringTime.equals("Time"))){
			final String[] partsOfTime = stringTime.split(":");
			endMinutes = Integer.parseInt(partsOfTime[1]);
			endHour = Integer.parseInt(partsOfTime[0]);
			if (endHour == 12){
				endHour -= 12;
			}
			if (stringAMPM.equals("PM")){
				endHour += 12;
			}
		}
		validateFields();
	}

	public void parseDeckDropdowns(){
		final String deckName = (String) comboDeck.getSelectedItem();
		final List<Deck> projectDecks = DeckListModel.getInstance().getDecks();
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
		final List<CreatePokerSessionErrors> errors;
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
			if (editedDescription){
				if (errors.contains(CreatePokerSessionErrors.NoDescription)){
					descriptionErrorMessage.setText("Please enter a description");
				} else {
					descriptionErrorMessage.setText("");
				}
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
		final int year = datePicker.getModel().getYear();
		final int month = datePicker.getModel().getMonth();
		final int day = datePicker.getModel().getDay();

		pokerSession.setName(textFieldSessionField.getText());
		pokerSession.setDescription(textFieldDescription.getText());
		if (sessionDeck == null){
			pokerSession.setSessionDeckID(-1);
		}
		else {
			pokerSession.setSessionDeckID(sessionDeck.getId());
		}
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
		final boolean boxChecked = endDateCheckBox.isSelected();

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

		sessionDetailPanel.revalidate();
		sessionDetailPanel.repaint();
		return boxChecked;
	}

	/**
	 * Disables the date picker button from opening the calendar
	 */
	private void disableDatePicker() {
		// Remove the datePicker from the layout
		firstPanelLayout.removeLayoutComponent((Component) datePicker);
		sessionDetailPanel.remove((JPanel) datePicker);

		// Add a disabled button in place of the datePicker
		firstPanelLayout.putConstraint(SpringLayout.NORTH, disabledDatePicker, 6, SpringLayout.SOUTH, lblEndDate);
		firstPanelLayout.putConstraint(SpringLayout.WEST, disabledDatePicker, 0, SpringLayout.WEST, lblEndDate);	
		firstPanelLayout.putConstraint(SpringLayout.EAST, disabledDatePicker, 0, SpringLayout.EAST, comboAMPM);
		firstPanelLayout.putConstraint(SpringLayout.NORTH, lblSessionEndTime, 6, SpringLayout.SOUTH, disabledDatePicker);
		sessionDetailPanel.add(disabledDatePicker);


	}

	/**
	 * Enables the date picker button to open the calendar
	 */
	private void enableDatePicker() {
		// Remve the datePicker placeholder
		firstPanelLayout.removeLayoutComponent(disabledDatePicker);
		sessionDetailPanel.remove(disabledDatePicker);

		// Re-add the datePicker
		firstPanelLayout.putConstraint(SpringLayout.NORTH, (JPanel) datePicker, 6, SpringLayout.SOUTH, lblEndDate);
		firstPanelLayout.putConstraint(SpringLayout.WEST, (JPanel) datePicker, 0, SpringLayout.WEST, lblEndDate);	
		firstPanelLayout.putConstraint(SpringLayout.EAST, (JPanel) datePicker, 0, SpringLayout.EAST, comboAMPM);
		firstPanelLayout.putConstraint(SpringLayout.NORTH, lblSessionEndTime, 6, SpringLayout.SOUTH, (JPanel) datePicker);
		sessionDetailPanel.add((JPanel) datePicker);
	}


	/**
	 * @return Returns if no fields in the panel have been changed
	 */
	public boolean readyToRemove()
	{
		boolean fieldsChanged = false;
		// Check if the submit button was clicked
		if (submitSession) {
			return true;
		}
		// Otherwise check if data was modified
		if (viewMode == ViewMode.CREATING) {
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
			final int result = JOptionPane.showConfirmDialog(this, "Discard unsaved changes and close tab?", "Discard Changes?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (result == 0) {
				restoreInformation();
				return true;
			}
			return false;
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
		if (!(textFieldDescription.getText().equals(""))) {
			return true;
		}
		// Check if an endDate has been set (default for creating is no endDate)
		// Check if a deck was selected (default for creating is noDeck)
		if (haveEndDate || isUsingDeck) {
			return true;
		}

		return false;
	}


	/**
	 * 
	 * @return whether any fields have been changed.
	 */
	private boolean anythingChangedEditing() {
		saveFields();

		// Check if the user has changed the session name
		if (!unmodifiedSession.getName().equals(pokerSession.getName())) {
			return true;
		}
		// Check if the user has changed the description
		if (!unmodifiedSession.getDescription().equals(pokerSession.getDescription())) {
			return true;
		}
		// Check if an endDate has been changed
		if (unmodifiedSession.hasEndDate() ^ pokerSession.hasEndDate()) {
			return true;
		}
		if (unmodifiedSession.hasEndDate() &&	// If one date is set, the other is guaranteed to be set
				(!unmodifiedSession.getEndDate().equals(pokerSession.getEndDate()))) {
			return true;
		}
		// Check if the poker deck was changed
		if (!(unmodifiedSession.isUsingDeck()) == (pokerSession.isUsingDeck())) {
			return true;
		}
		if (unmodifiedSession.isUsingDeck()){
			if (unmodifiedSession.getSessionDeckID() != pokerSession.getSessionDeckID()){
				return true;
			}
		}
		// Check if the requirements were changed
		if (!unmodifiedSession.getRequirementIDs().equals(pokerSession.getRequirementIDs())) {
			return true;
		}
		return false;
	}

	//closes this panel
	public void closePanel() {
		ViewEventController.getInstance().removeTab(this);
	}

	/**
	 * restores the original session 
	 * used for if edits have been made, but cancel has been hit
	 */
	public void restoreInformation() {
		pokerSession.copyFrom(unmodifiedSession);
	}
	
	/**
	 * Notifies user if reqs were removed due to being in other sessions.
	 * @param oldReqs The requirements before session was modified.
	 * @param newReqs The requirements after the session was modified.
	 * @return the user's response to the dialog box (cancel or continue)
	 */
	
	public int notifyUserOfReqChanges(List<Integer> oldReqs, List<Integer> newReqs) {
		System.out.println("Executing notifyUserofReqChanges function().");
		LinkedList<Integer> changes = checkIfReqsChangedByConflict(oldReqs, newReqs);
		if (changes == null) return -1;
		String message = "If you continue, the following requirements that were previously in this session will be removed.\n" +
		"If you did not discard them, they have been or are being estimated in other sessions.\n\n";
		for (Integer i : changes) {
			Requirement req = RequirementModel.getInstance().getRequirement(i);
			message = message + req.getName() + "\n";
		}
		final int result = JOptionPane.showConfirmDialog(this, message , "Continue Removing Requirements?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		return result;
	}

	/**
	 * Checks what if any requirements were removed due to conflict.
	 * @param oldReqs The requirements before the session was modified.
	 * @param newReqs The requirements after the session was modified.
	 * @return the list of requirement IDs of removed requirements, or null
	 */
	public LinkedList<Integer> checkIfReqsChangedByConflict(List<Integer> oldReqs, List<Integer> newReqs) {
		if (oldReqs == null) return null;
		if (oldReqs.size() == 0) return null;
		if (newReqs.size() == 0) {//fault point
			return (LinkedList<Integer>) oldReqs;
		}
		LinkedList<Integer> removedReqs = new LinkedList<Integer>();
		for (int i = 0; i < oldReqs.size(); i++) {
			if (!newReqs.contains(oldReqs.get(i)) && !removedReqs.contains(oldReqs.get(i))) {
				removedReqs.add(oldReqs.get(i));
			}
		}
		
		return (removedReqs.size() == 0) ? null : removedReqs;

	}

}
