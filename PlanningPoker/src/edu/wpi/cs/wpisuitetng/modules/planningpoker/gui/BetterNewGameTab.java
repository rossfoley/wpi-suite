/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.planningpoker.gui;

/**
 * @author mandi1267
 *
 */

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SpringLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JButton;
import javax.swing.border.LineBorder;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.controller.AddSessionController;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSessionModel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.ViewEventController;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.SwingConstants;

public class BetterNewGameTab {
	private static JTextField textFieldSessionField;
	private static JTextArea textFieldDescription;
	JComboBox<Months> comboMonth = new JComboBox<Months>();
	JComboBox<String> comboDay = new JComboBox<String>();
	JComboBox<String> comboYear = new JComboBox<String>();
	JComboBox<String> comboTime = new JComboBox<String>();
	JComboBox<String> comboAMPM = new JComboBox<String>();
	int month = 13;
	int day = 0;
	int year = 1;
	int endHour;
	int endMinutes;
	int displayingDays;
	JPanel panel_1 = new JPanel();
	SpringLayout sl_panel_1 = new SpringLayout();
	JLabel lblEndDate = new JLabel("Session End Date:");
	boolean alreadyVisited = false;
	
	/**
	 * @wbp.parser.entryPoint
	 */
	
	public JPanel createJPanel(){
		final JPanel panel = new JPanel();
		final SpringLayout sl_panel = new SpringLayout();
		panel.setLayout(sl_panel);
		
		final String defaultName = this.makeDefaultName();
		
		
		//JPanel panel_1 = new JPanel();
		sl_panel.putConstraint(SpringLayout.NORTH, panel_1, 10, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.WEST, panel_1, 10, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.SOUTH, panel_1, -10, SpringLayout.SOUTH, panel);
		sl_panel.putConstraint(SpringLayout.EAST, panel_1, -10, SpringLayout.EAST, panel);
		
		JButton btnNext = new JButton("Next >");
		sl_panel.putConstraint(SpringLayout.SOUTH, btnNext, -10, SpringLayout.SOUTH, panel);
		sl_panel.putConstraint(SpringLayout.EAST, btnNext, -10, SpringLayout.EAST, panel);
		panel.add(btnNext);
		panel_1.setForeground(Color.BLACK);
		panel.add(panel_1);

		sl_panel_1.putConstraint(SpringLayout.EAST, comboDay, 70, SpringLayout.EAST, comboMonth);
		panel_1.setLayout(sl_panel_1);
		

		
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean canSaveSession = false;
				System.out.print(year);
				System.out.print(month);
				System.out.println(day);
				final PlanningPokerSession pokerSession = new PlanningPokerSession();
				pokerSession.setName(textFieldSessionField.getText());
				pokerSession.setDescription(textFieldDescription.getText());

				boolean dateCorrect = true;
				boolean dateInRange = true;
				JLabel lblDateError = new JLabel("Please select a value for all date fields");
				JLabel lblDateOutOfRange = new JLabel("Please select a date after the current date");
				try {
					dateCorrect = true;
					canSaveSession = pokerSession.validateFields(year, month, day, endHour, endMinutes, defaultName);
					

					//after that you need to call this to revalidate and repaint the panel

				}
				catch(InvalidDateException ex) {
					dateCorrect = false;
					canSaveSession = false;
					lblDateError.setText("Please select a value for all date fields");
					lblDateError.setForeground(Color.RED);
					sl_panel_1.putConstraint(SpringLayout.NORTH, lblDateError, 0, SpringLayout.NORTH, lblEndDate);
					sl_panel_1.putConstraint(SpringLayout.WEST, lblDateError, 20, SpringLayout.EAST, lblEndDate);
					panel_1.add(lblDateError);
					panel_1.revalidate();
					panel_1.repaint();
					System.out.println("Exception thrown");
				} 
				catch(DateOutOfRangeException ex){
					dateInRange = false;
					canSaveSession = false;
					lblDateOutOfRange.setText("Please select a date after the current date");
					lblDateOutOfRange.setForeground(Color.RED);
					sl_panel_1.putConstraint(SpringLayout.NORTH, lblDateOutOfRange, 0, SpringLayout.NORTH, lblEndDate);
					sl_panel_1.putConstraint(SpringLayout.WEST, lblDateOutOfRange, 20, SpringLayout.EAST, lblEndDate);
					panel_1.add(lblDateOutOfRange);
					panel_1.revalidate();
					panel_1.repaint();
					System.out.println("Exception thrown");
				}
				finally {}
				System.out.println(textFieldDescription.getText());
				System.out.println(textFieldSessionField.getText());
					//if(pokerSession.validateFields(year, month, day, endHour, endMinutes)){
				if (canSaveSession){
						alreadyVisited = true;
						// go to next screen
						panel.removeAll();
						
						// Add the requirements panel
						final SelectFromListPanel requirementPanel = new SelectFromListPanel();
						sl_panel.putConstraint(SpringLayout.NORTH, requirementPanel, 10, SpringLayout.NORTH, panel);
						sl_panel.putConstraint(SpringLayout.WEST, requirementPanel, 10, SpringLayout.WEST, panel);
						sl_panel.putConstraint(SpringLayout.SOUTH, requirementPanel, -50, SpringLayout.SOUTH, panel);
						sl_panel.putConstraint(SpringLayout.EAST, requirementPanel, -10, SpringLayout.EAST, panel);
						
						JButton btnSubmit = new JButton("Submit");
						sl_panel.putConstraint(SpringLayout.SOUTH, btnSubmit, -10, SpringLayout.SOUTH, panel);
						sl_panel.putConstraint(SpringLayout.EAST, btnSubmit, -10, SpringLayout.EAST, panel);
						panel.add(btnSubmit);
						panel.add(requirementPanel);
		
						panel.revalidate();
						panel.repaint();
						
						btnSubmit.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								pokerSession.setRequirements(requirementPanel.getSelected());
								PlanningPokerSessionModel.getInstance().addPlanningPokerSession(pokerSession);
								ViewEventController.getInstance().removeTab((JComponent)panel.getComponentAt(0,0));// this thing closes the tabs
							}
						});
				}
			}
		});
		
		
		JLabel lblSessionName = new JLabel("Session Name:");
		sl_panel_1.putConstraint(SpringLayout.NORTH, lblSessionName, 10, SpringLayout.NORTH, panel_1);
		sl_panel_1.putConstraint(SpringLayout.WEST, lblSessionName, 10, SpringLayout.WEST, panel_1);
		sl_panel.putConstraint(SpringLayout.NORTH, lblSessionName, 0, SpringLayout.NORTH, panel_1);
		sl_panel.putConstraint(SpringLayout.WEST, lblSessionName, 40, SpringLayout.EAST, panel_1);
		panel_1.add(lblSessionName);
		
		textFieldSessionField = new JTextField();
		textFieldSessionField.setText(defaultName);
		sl_panel_1.putConstraint(SpringLayout.NORTH, textFieldSessionField, 6, SpringLayout.SOUTH, lblSessionName);
		sl_panel_1.putConstraint(SpringLayout.WEST, textFieldSessionField, 10, SpringLayout.WEST, panel_1);
		sl_panel_1.putConstraint(SpringLayout.EAST, textFieldSessionField, -10, SpringLayout.EAST, panel_1);
		panel_1.add(textFieldSessionField);
		textFieldSessionField.setColumns(10);
		
		JLabel lblSessionDescription = new JLabel("Session Description:");
		sl_panel_1.putConstraint(SpringLayout.NORTH, lblSessionDescription, 6, SpringLayout.SOUTH, textFieldSessionField);
		sl_panel_1.putConstraint(SpringLayout.WEST, lblSessionDescription, 0, SpringLayout.WEST, lblSessionName);
		panel_1.add(lblSessionDescription);
		
		textFieldDescription = new JTextArea();
		textFieldDescription.setToolTipText("");
		sl_panel_1.putConstraint(SpringLayout.NORTH, textFieldDescription, 6, SpringLayout.SOUTH, lblSessionDescription);
		sl_panel_1.putConstraint(SpringLayout.WEST, textFieldDescription, 0, SpringLayout.WEST, lblSessionName);
		sl_panel_1.putConstraint(SpringLayout.SOUTH, textFieldDescription, -175, SpringLayout.SOUTH, panel_1);
		sl_panel_1.putConstraint(SpringLayout.EAST, textFieldDescription, -10, SpringLayout.EAST, panel_1);
		textFieldDescription.setColumns(10);
		panel_1.add(textFieldDescription);
		

		sl_panel_1.putConstraint(SpringLayout.EAST, comboMonth, 10, SpringLayout.EAST, lblEndDate);
		sl_panel_1.putConstraint(SpringLayout.NORTH, lblEndDate, 6, SpringLayout.SOUTH, textFieldDescription);
		sl_panel_1.putConstraint(SpringLayout.WEST, lblEndDate, 0, SpringLayout.WEST, lblSessionName);
		panel_1.add(lblEndDate);
		

		sl_panel_1.putConstraint(SpringLayout.NORTH, comboMonth, 6, SpringLayout.SOUTH, lblEndDate);
		sl_panel_1.putConstraint(SpringLayout.WEST, comboMonth, 0, SpringLayout.WEST, lblEndDate);
		comboMonth.setModel(new DefaultComboBoxModel<Months>(Months.values()));
		panel_1.add(comboMonth);
		
		
		comboMonth.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
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
		});
		
		comboDay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				String dayString = (String) comboDay.getSelectedItem();
				if (dayString.equals("Day")){
					day = 0;
				}
				else {
					day = Integer.parseInt(dayString);
				}
				System.out.print("Day:");
				System.out.println(day);
						

			}	
		});
		
		
		sl_panel_1.putConstraint(SpringLayout.NORTH, comboDay, 6, SpringLayout.SOUTH, lblEndDate);
		sl_panel_1.putConstraint(SpringLayout.WEST, comboDay, 6, SpringLayout.EAST, comboMonth);
		setDays31();
		panel_1.add(comboDay);
		
		sl_panel_1.putConstraint(SpringLayout.WEST, comboYear, 6, SpringLayout.EAST, comboDay);
		sl_panel_1.putConstraint(SpringLayout.EAST, comboYear, 90, SpringLayout.EAST, comboDay);
		sl_panel_1.putConstraint(SpringLayout.NORTH, comboYear, 0, SpringLayout.NORTH, comboMonth);
		setYearDropdown();
		panel_1.add(comboYear);
		
		comboYear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				String yearString = (String) comboYear.getSelectedItem();
				System.out.println(yearString);
				if (yearString.equals("Year")){
					year = 1;
				}
				else {
					year = Integer.parseInt(yearString);
				}
				if (month==1){
					setFebDays();
				}
				System.out.print("year:");
				System.out.println(year);
						

			}	
		});
		
		
		JLabel lblSessionEndTime = new JLabel("Session End Time:");
		lblSessionEndTime.setForeground(Color.BLACK);
		sl_panel_1.putConstraint(SpringLayout.NORTH, lblSessionEndTime, 6, SpringLayout.SOUTH, comboMonth);
		sl_panel_1.putConstraint(SpringLayout.WEST, lblSessionEndTime, 0, SpringLayout.WEST, lblSessionName);
		panel_1.add(lblSessionEndTime);
		
		sl_panel_1.putConstraint(SpringLayout.NORTH, comboTime, 6, SpringLayout.SOUTH, lblSessionEndTime);
		sl_panel_1.putConstraint(SpringLayout.WEST, comboTime, 0, SpringLayout.WEST, lblSessionName);
		sl_panel_1.putConstraint(SpringLayout.EAST, comboTime, 60, SpringLayout.WEST, lblSessionName);
		setTimeDropdown();
		panel_1.add(comboTime);
	
		sl_panel_1.putConstraint(SpringLayout.WEST, comboAMPM, 6, SpringLayout.EAST, comboTime);
		sl_panel_1.putConstraint(SpringLayout.SOUTH, comboAMPM, 0, SpringLayout.SOUTH, comboTime);
		sl_panel_1.putConstraint(SpringLayout.EAST, comboAMPM, 80, SpringLayout.EAST, comboTime);
		comboAMPM.setModel(new DefaultComboBoxModel<String>(new String[] {"AM","PM"}));
		parseTimeDropdowns();
		panel_1.add(comboAMPM);
		
		comboTime.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				parseTimeDropdowns();
			}
		});
		
		comboAMPM.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				parseTimeDropdowns();
			}
		});
		
		JLabel lblDeck = new JLabel("Deck:");
		sl_panel_1.putConstraint(SpringLayout.NORTH, lblDeck, 6, SpringLayout.SOUTH, comboTime);
		sl_panel_1.putConstraint(SpringLayout.WEST, lblDeck, 0, SpringLayout.WEST, lblSessionName);
		panel_1.add(lblDeck);
		
		JComboBox comboDeck = new JComboBox();
		sl_panel_1.putConstraint(SpringLayout.NORTH, comboDeck, 6, SpringLayout.SOUTH, lblDeck);
		sl_panel_1.putConstraint(SpringLayout.WEST, comboDeck, 0, SpringLayout.WEST, lblSessionName);
		sl_panel_1.putConstraint(SpringLayout.EAST, comboDeck, 0, SpringLayout.EAST, lblEndDate);
		panel_1.add(comboDeck);
		
		//System.out.println(panelWidth);
		
		return panel;
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
	
}


