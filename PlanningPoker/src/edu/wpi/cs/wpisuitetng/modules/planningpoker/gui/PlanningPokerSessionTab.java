/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.planningpoker.gui;

/**
 * @author mandi1267
 *
 */

import javax.swing.DefaultComboBoxModel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SpringLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JButton;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSessionModel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.ViewEventController;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.gui.ViewMode;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PlanningPokerSessionTab extends JPanel {
	private final PlanningPokerSession pokerSession;
	
	private final SpringLayout layout = new SpringLayout();
	SpringLayout firstPanelLayout = new SpringLayout();
	JPanel firstPanel = new JPanel();
	SpringLayout secondPanelLayout = new SpringLayout();
	JPanel secondPanel = new JPanel();
	
	private ViewMode viewMode;
	private JTextField textFieldSessionField;
	private JTextArea textFieldDescription;
	JComboBox<Months> comboMonth = new JComboBox<Months>();
	JComboBox<String> comboDay = new JComboBox<String>();
	JComboBox<String> comboYear = new JComboBox<String>();
	JComboBox<String> comboTime = new JComboBox<String>();
	JComboBox<String> comboAMPM = new JComboBox<String>();
	final SelectFromListPanel requirementPanel = new SelectFromListPanel();
	JLabel lblEndDate = new JLabel("Session End Date:");
	
	int month = 13;
	int day = 0;
	int year = 1;
	int endHour;
	int endMinutes;
	int displayingDays;
	
	
	/**
	 * Constructor for creating a planning poker session
	 */
	public PlanningPokerSessionTab() {
		this.pokerSession = new PlanningPokerSession();
		viewMode = (ViewMode.CREATING);
		this.buildLayouts();
		this.displayPanel(firstPanel);
	}
	
	
	/**
	 * Constructor for editing an existing planning poker session
	 */
	public PlanningPokerSessionTab(PlanningPokerSession existingSession) {
		this.pokerSession = existingSession;
		viewMode = (ViewMode.EDITING);
		this.buildLayouts();
		this.displayPanel(firstPanel);
	}
	
	public void buildLayouts() {
		layout.putConstraint(SpringLayout.NORTH, firstPanel, 10, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, firstPanel, 10, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.SOUTH, firstPanel, -10, SpringLayout.SOUTH, this);
		layout.putConstraint(SpringLayout.EAST, firstPanel, -10, SpringLayout.EAST, this);
		
		this.setLayout(layout);
		this.buildFirstLayout();
		this.buildSecondLayout();
	}
	
	/**
	 * Builds the first screen for creating/editing a session
	 */
	private void buildFirstLayout() {	
		// Set the layout and colors
		firstPanel.setLayout(firstPanelLayout);
		firstPanel.setForeground(Color.BLACK);
		
		// Initialize all of the fields on the panel
		final String defaultName = this.makeDefaultName();
		textFieldSessionField = new JTextField();
		JLabel lblSessionName = new JLabel("Session Name:");
		JLabel lblSessionDescription = new JLabel("Session Description:");
		textFieldDescription = new JTextArea();
		JLabel lblSessionEndTime = new JLabel("Session End Time:");
		textFieldDescription.setToolTipText("");
		JButton btnNext = new JButton("Next >");
		JLabel lblDeck = new JLabel("Deck:");
		JComboBox comboDeck = new JComboBox();
		
		textFieldSessionField.setText(defaultName);
		textFieldSessionField.setColumns(10);
		textFieldDescription.setColumns(10);
		comboMonth.setBackground(Color.WHITE);
		comboDay.setBackground(Color.WHITE);	
		comboYear.setBackground(Color.WHITE);
		comboTime.setBackground(Color.WHITE);
		comboAMPM.setBackground(Color.WHITE);
		lblSessionEndTime.setForeground(Color.BLACK);
		comboMonth.setModel(new DefaultComboBoxModel<Months>(Months.values()));
		comboAMPM.setModel(new DefaultComboBoxModel<String>(new String[] {"AM","PM"}));
		
		// Apply all of the constraints
		//layout.putConstraint(SpringLayout.NORTH, lblSessionName, 0, SpringLayout.NORTH, firstPanel);
		//layout.putConstraint(SpringLayout.WEST, lblSessionName, 40, SpringLayout.EAST, firstPanel);
		firstPanelLayout.putConstraint(SpringLayout.SOUTH, btnNext, -10, SpringLayout.SOUTH, this);
		firstPanelLayout.putConstraint(SpringLayout.EAST, btnNext, -10, SpringLayout.EAST, this);

		firstPanelLayout.putConstraint(SpringLayout.EAST, comboDay, 70, SpringLayout.EAST, comboMonth);							
		
		firstPanelLayout.putConstraint(SpringLayout.NORTH, lblSessionName, 10, SpringLayout.NORTH, firstPanel);
		firstPanelLayout.putConstraint(SpringLayout.WEST, lblSessionName, 10, SpringLayout.WEST, firstPanel);		
			
		firstPanelLayout.putConstraint(SpringLayout.NORTH, textFieldSessionField, 6, SpringLayout.SOUTH, lblSessionName);
		firstPanelLayout.putConstraint(SpringLayout.WEST, textFieldSessionField, 10, SpringLayout.WEST, firstPanel);
		firstPanelLayout.putConstraint(SpringLayout.EAST, textFieldSessionField, -10, SpringLayout.EAST, firstPanel);		
			
		firstPanelLayout.putConstraint(SpringLayout.NORTH, lblSessionDescription, 6, SpringLayout.SOUTH, textFieldSessionField);
		firstPanelLayout.putConstraint(SpringLayout.WEST, lblSessionDescription, 0, SpringLayout.WEST, lblSessionName);		
		
		firstPanelLayout.putConstraint(SpringLayout.NORTH, textFieldDescription, 6, SpringLayout.SOUTH, lblSessionDescription);
		firstPanelLayout.putConstraint(SpringLayout.WEST, textFieldDescription, 0, SpringLayout.WEST, lblSessionName);
		firstPanelLayout.putConstraint(SpringLayout.SOUTH, textFieldDescription, -175, SpringLayout.SOUTH, firstPanel);
		firstPanelLayout.putConstraint(SpringLayout.EAST, textFieldDescription, -10, SpringLayout.EAST, firstPanel);					

		firstPanelLayout.putConstraint(SpringLayout.EAST, comboMonth, 10, SpringLayout.EAST, lblEndDate);
		firstPanelLayout.putConstraint(SpringLayout.NORTH, lblEndDate, 6, SpringLayout.SOUTH, textFieldDescription);
		firstPanelLayout.putConstraint(SpringLayout.WEST, lblEndDate, 0, SpringLayout.WEST, lblSessionName);
		
		firstPanelLayout.putConstraint(SpringLayout.NORTH, comboMonth, 6, SpringLayout.SOUTH, lblEndDate);
		firstPanelLayout.putConstraint(SpringLayout.WEST, comboMonth, 0, SpringLayout.WEST, lblEndDate);
								
		firstPanelLayout.putConstraint(SpringLayout.NORTH, comboDay, 6, SpringLayout.SOUTH, lblEndDate);
		firstPanelLayout.putConstraint(SpringLayout.WEST, comboDay, 6, SpringLayout.EAST, comboMonth);			
		
		firstPanelLayout.putConstraint(SpringLayout.WEST, comboYear, 6, SpringLayout.EAST, comboDay);
		firstPanelLayout.putConstraint(SpringLayout.EAST, comboYear, 90, SpringLayout.EAST, comboDay);
		firstPanelLayout.putConstraint(SpringLayout.NORTH, comboYear, 0, SpringLayout.NORTH, comboMonth);		
		
		firstPanelLayout.putConstraint(SpringLayout.NORTH, lblSessionEndTime, 6, SpringLayout.SOUTH, comboMonth);
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
		firstPanelLayout.putConstraint(SpringLayout.WEST, comboDeck, 0, SpringLayout.WEST, lblSessionName);
		firstPanelLayout.putConstraint(SpringLayout.EAST, comboDeck, 0, SpringLayout.EAST, lblEndDate);
		
		// Handle the time dropdowns
		setDays31();
		setTimeDropdown();
		setYearDropdown();
		parseTimeDropdowns();
		
		// Month dropdown event handler
		comboMonth.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				handleMonthChange();
			}	
		});
		
		// Day dropdown event handler
		comboDay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				String dayString = (String) comboDay.getSelectedItem();
				if (dayString.equals("Day")){
					day = 0;
				}
				else {
					day = Integer.parseInt(dayString);
				}
			}	
		});
		
		// Year dropdown event handler
		comboYear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				String yearString = (String) comboYear.getSelectedItem();
				System.out.println(yearString);
				if (yearString.equals("Year")) {
					year = 1;
				}
				else {
					year = Integer.parseInt(yearString);
				}
				if (month == 1) {
					setFebDays();
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
		
		// Next button event handler
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleNextButton(defaultName);
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
		firstPanel.add(comboMonth);
		firstPanel.add(comboDay);
		firstPanel.add(comboYear);
		firstPanel.add(comboTime);
		firstPanel.add(comboAMPM);
		firstPanel.add(comboDeck);
	}
	
	
	/**
	 * Builds the second screen for creating/editing a session.
	 * This layout displays the requirements to select for a planning poker session.
	 */
	private void buildSecondLayout() {
		secondPanel.setLayout(secondPanelLayout);
		
		JButton btnSubmit = new JButton("Submit");
		JButton btnBack = new JButton("Back");
		
		// Position the requirements panel
		secondPanelLayout.putConstraint(SpringLayout.NORTH, requirementPanel, 10, SpringLayout.NORTH, this);
		secondPanelLayout.putConstraint(SpringLayout.WEST, requirementPanel, 10, SpringLayout.WEST, this);
		secondPanelLayout.putConstraint(SpringLayout.SOUTH, requirementPanel, -50, SpringLayout.SOUTH, this);
		secondPanelLayout.putConstraint(SpringLayout.EAST, requirementPanel, -10, SpringLayout.EAST, this);
		
		// Position the submit button
		secondPanelLayout.putConstraint(SpringLayout.SOUTH, btnSubmit, -10, SpringLayout.SOUTH, this);
		secondPanelLayout.putConstraint(SpringLayout.EAST, btnSubmit, -10, SpringLayout.EAST, this);
		
		// Position the back button
		secondPanelLayout.putConstraint(SpringLayout.SOUTH, btnBack, -10, SpringLayout.SOUTH, this);
		secondPanelLayout.putConstraint(SpringLayout.WEST, btnBack, -10, SpringLayout.WEST, this);
		
		// Submit button event handler
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pokerSession.setRequirements(requirementPanel.getSelected());
				submitSessionToDatabase();
			}
		});
		
		// Back button event handler
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pokerSession.setRequirements(requirementPanel.getSelected());
				displayPanel(firstPanel);
			}
		});
		
		// Add all of the elements to the second panel
		secondPanel.add(btnSubmit);
		secondPanel.add(btnBack);
		secondPanel.add(requirementPanel);
	}
	
	/**
	 * Makes the second screen the visible panel
	 */
	private void displayPanel(JPanel newPanel) {
		this.removeAll();
		this.add(newPanel);
		newPanel.revalidate();
		newPanel.repaint();
		this.revalidate();
		this.repaint();
	}
	
	private void handleMonthChange() {
		Months monthEnum = (Months) comboMonth.getSelectedItem();
		switch(monthEnum){
		case Month:
			month = 13;
			setDays31();
			break;
		case JANUARY:
			month = 0;
			setDays31();
			break;
		case FEBRUARY:
			month = 1;
			setFebDays();
			break;
		case MARCH:
			month = 2;
			setDays31();
			break;
		case APRIL:
			month = 3;
			setDays30();
			break;
		case MAY:
			month = 4;
			setDays31();
			break;
		case JUNE:
			month = 5;
			setDays30();
			break;
		case JULY:
			month = 6;
			setDays31();
			break;
		case AUGUST:
			month = 7;
			setDays31();
			break;
		case SEPTEMBER:
			month = 8;
			setDays30();
			break;
		case OCTOBER:
			month = 9;
			setDays31();
			break;
		case NOVEMBER:
			month = 10;
			setDays30();
			break;
		case DECEMBER:
			month = 11;
			setDays31();
			break;
		default:
			month = 0;
			setDays31();
			break;
				
		}
	}
	
	
	public void setDays31(){
		if (displayingDays!=31){
			comboDay.setModel(new DefaultComboBoxModel<String>(new String[] {"Day", "1", "2", "3", "4","5", "6", "7", "8",
					"9", "10", "11", "12","13", "14", "15", "16", "17", "18", "19", "20",
					"21", "22","23", "24", "25", "26", "27", "28", "29", "30", "31" }));
			System.out.println("Displaying 31 days");
			displayingDays = 31;
		}
	}
	
	public void setDays30(){
		if (displayingDays!=30){
			comboDay.setModel(new DefaultComboBoxModel<String>(new String[] {"Day", "1", "2", "3", "4","5", "6", "7", "8",
					"9", "10", "11", "12","13", "14", "15", "16", "17", "18", "19", "20",
					"21", "22","23", "24", "25", "26", "27", "28", "29", "30" }));
			System.out.println("Displaying 30 days");
			displayingDays = 30;
		}
	}

	public void setFebDays(){
		if ((year%4)==0){
			if (displayingDays!=29){
				comboDay.setModel(new DefaultComboBoxModel<String>(new String[] {"Day", "1", "2", "3", "4","5", "6", "7", "8",
						"9", "10", "11", "12","13", "14", "15", "16", "17", "18", "19", "20",
						"21", "22","23", "24", "25", "26", "27", "28", "29"}));
				displayingDays = 29;
			}
		}
		else {
			if (displayingDays!=28){
				comboDay.setModel(new DefaultComboBoxModel<String>(new String[] {"Day", "1", "2", "3", "4","5", "6", "7", "8",
						"9", "10", "11", "12","13", "14", "15", "16", "17", "18", "19", "20",
						"21", "22","23", "24", "25", "26", "27", "28"}));
				displayingDays = 28;
			}
		}
		System.out.println("Displaying feb days");
	}
	public void setYearDropdown(){
		comboYear.setModel(new DefaultComboBoxModel<String>(new String[] {
				"Year", "2014", "2015", "2016", "2017", "2018", "2019" , "2020", 
				"2021", "2022", "2023", "2024", "2025", "2026", "2027", "2028", "2029", "2030", 
				"2031", "2032", "2033", "2034"}));
	}
	public void setTimeDropdown(){
		comboTime.setModel(new DefaultComboBoxModel<String>
		(new String[] { "12:00", "12:30", "1:00", "1:30", "2:00", "2:30", "3:00", "3:30", 
				"4:00", "4:30", "5:00", "5:30", "6:00", "6:30", "7:00", "7:30", 
				"8:00", "8:30", "9:00", "9:30", "10:00", "10:30", "11:00", "11:30" } ));
		
		
	}
	
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
			System.out.print("Hour:");
			System.out.println(endHour);
			System.out.print("Minute:");
			System.out.println(endMinutes);
		}
	}
	
	/* private void printError(){
		JLabel lblDateError = new JLabel("Please select a value for all date fields");
		lblDateError.setForeground(Color.RED);
		
		sl_panel_1.putConstraint(SpringLayout.NORTH, lblDateError, 0, SpringLayout.NORTH, lblEndDate);
		sl_panel_1.putConstraint(SpringLayout.WEST, lblDateError, 20, SpringLayout.WEST, lblEndDate);
		panel_1.add(lblDateError);
		System.out.println("Error function reached");
	} */
	
	/**
	 * @return the initial ID
	 */
	public String makeDefaultName() {
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		return "Planning Poker " + dateFormat.format(date);
	}

	
	/**
	 * Submits the current session to the database as a new planning poker session
	 * and removes this tab.
	 */
	private void submitSessionToDatabase() {
		
		PlanningPokerSessionModel.getInstance().addPlanningPokerSession(pokerSession);		
		ViewEventController.getInstance().removeTab(this);// this thing closes the tabs
		
	}


	/**
	 * @return 
	 */
	public PlanningPokerSession getDisplaySession() {
		return pokerSession;
	}


	private void handleNextButton(final String defaultName) {
		pokerSession.setName(textFieldSessionField.getText());
		pokerSession.setDescription(textFieldDescription.getText());

		boolean dataValid = false;
		JLabel lblDateError = new JLabel("Please select a value for all date fields");
		JLabel lblDateOutOfRange = new JLabel("Please select a date after the current date");
		try {
			dataValid = pokerSession.validateFields(year, month, day, endHour, endMinutes, defaultName);
		}
		catch(InvalidDateException ex) {
			lblDateError.setText("Please select a value for all date fields");
			lblDateError.setForeground(Color.RED);
			firstPanelLayout.putConstraint(SpringLayout.NORTH, lblDateError, 0, SpringLayout.NORTH, lblEndDate);
			firstPanelLayout.putConstraint(SpringLayout.WEST, lblDateError, 20, SpringLayout.EAST, lblEndDate);
			firstPanel.add(lblDateError);
			firstPanel.revalidate();
			firstPanel.repaint();
			System.out.println("Exception thrown");
		} 
		catch(DateOutOfRangeException ex){
			lblDateOutOfRange.setText("Please select a date after the current date");
			lblDateOutOfRange.setForeground(Color.RED);
			firstPanelLayout.putConstraint(SpringLayout.NORTH, lblDateOutOfRange, 0, SpringLayout.NORTH, lblEndDate);
			firstPanelLayout.putConstraint(SpringLayout.WEST, lblDateOutOfRange, 20, SpringLayout.EAST, lblEndDate);
			firstPanel.add(lblDateOutOfRange);
			firstPanel.revalidate();
			firstPanel.repaint();
		}
		
		// If session data is valid, go to the next screen
		if (dataValid)
			displayPanel(secondPanel);
	}
	
}
