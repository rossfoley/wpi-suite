/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.planningpoker.gui;

/**
 * @author Amanda
 *
 */

import javax.swing.DefaultComboBoxModel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SpringLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JButton;
import javax.swing.border.LineBorder;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.controller.AddSessionController;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.GregorianCalendar;

import javax.swing.SwingConstants;

public class BetterNewGameTab { //extends JPanel {
	private static JTextField textFieldSessionField;
	private static JTextField textFieldDescription;
	JComboBox<Months> comboMonth = new JComboBox<Months>();
	JComboBox<String> comboDay = new JComboBox<String>();
	JComboBox<String> comboYear = new JComboBox<String>();
	int month;
	int day;
	int year;
	//JPanel panel = new JPanel();
	
	
	//private static JComboBox<String> comboMonth;
	
	/**
	 * @wbp.parser.entryPoint
	 */
	
	public JPanel createJPanel(){
		//public BetterNewGameTab(){
		
		
	//public JPanel createJPanel(){
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(Color.MAGENTA));
		SpringLayout sl_panel = new SpringLayout();
		panel.setLayout(sl_panel);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(Color.RED));
		sl_panel.putConstraint(SpringLayout.NORTH, panel_1, 10, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.WEST, panel_1, 10, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.SOUTH, panel_1, -10, SpringLayout.SOUTH, panel);
		sl_panel.putConstraint(SpringLayout.EAST, panel_1, -10, SpringLayout.EAST, panel);
		
		JButton btnNext = new JButton("Next >");
		sl_panel.putConstraint(SpringLayout.SOUTH, btnNext, -10, SpringLayout.SOUTH, panel);
		sl_panel.putConstraint(SpringLayout.EAST, btnNext, -10, SpringLayout.EAST, panel);
		panel.add(btnNext);
		panel.add(panel_1);
		SpringLayout sl_panel_1 = new SpringLayout();
		sl_panel_1.putConstraint(SpringLayout.EAST, comboDay, 70, SpringLayout.EAST, comboMonth);
		panel_1.setLayout(sl_panel_1);
		
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PlanningPokerSession pokerSession = new PlanningPokerSession();
				pokerSession.setOpen(true);
				pokerSession.setName(textFieldSessionField.getText());
				pokerSession.setDescription(textFieldDescription.getText());
				

				GregorianCalendar endDate = null;
				if ((month!=0)&&(day!=0)&&(year!=1)){
					endDate = new GregorianCalendar(month, day, year);
				}
				else {
					endDate = null;
				} 
				if (pokerSession.validateFields()){
					
				}
				else{
					
				}
				pokerSession.setEndDate(endDate);
				if(pokerSession.validateFields()){
					AddSessionController.getInstance().addPlanningPokerSession(pokerSession);
					// move to add reqs screen
				}
				else {
					// reprompt for empty fields
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
		sl_panel_1.putConstraint(SpringLayout.NORTH, textFieldSessionField, 6, SpringLayout.SOUTH, lblSessionName);
		sl_panel_1.putConstraint(SpringLayout.WEST, textFieldSessionField, 10, SpringLayout.WEST, panel_1);
		sl_panel_1.putConstraint(SpringLayout.EAST, textFieldSessionField, -10, SpringLayout.EAST, panel_1);
		panel_1.add(textFieldSessionField);
		textFieldSessionField.setColumns(10);
		
		JLabel lblSessionDescription = new JLabel("Session Description:");
		sl_panel_1.putConstraint(SpringLayout.NORTH, lblSessionDescription, 6, SpringLayout.SOUTH, textFieldSessionField);
		sl_panel_1.putConstraint(SpringLayout.WEST, lblSessionDescription, 0, SpringLayout.WEST, lblSessionName);
		panel_1.add(lblSessionDescription);
		
		textFieldDescription = new JTextField();
		sl_panel_1.putConstraint(SpringLayout.NORTH, textFieldDescription, 6, SpringLayout.SOUTH, lblSessionDescription);
		sl_panel_1.putConstraint(SpringLayout.WEST, textFieldDescription, 0, SpringLayout.WEST, lblSessionName);
		sl_panel_1.putConstraint(SpringLayout.SOUTH, textFieldDescription, -175, SpringLayout.SOUTH, panel_1);
		sl_panel_1.putConstraint(SpringLayout.EAST, textFieldDescription, -10, SpringLayout.EAST, panel_1);
		textFieldDescription.setColumns(10);
		panel_1.add(textFieldDescription);
		
		JLabel lblEndDate = new JLabel("Session End Date:");
		sl_panel_1.putConstraint(SpringLayout.EAST, comboMonth, 10, SpringLayout.EAST, lblEndDate);
		sl_panel_1.putConstraint(SpringLayout.NORTH, lblEndDate, 6, SpringLayout.SOUTH, textFieldDescription);
		sl_panel_1.putConstraint(SpringLayout.WEST, lblEndDate, 0, SpringLayout.WEST, lblSessionName);
		panel_1.add(lblEndDate);
		

		sl_panel_1.putConstraint(SpringLayout.NORTH, comboMonth, 6, SpringLayout.SOUTH, lblEndDate);
		sl_panel_1.putConstraint(SpringLayout.WEST, comboMonth, 0, SpringLayout.WEST, lblEndDate);
		comboMonth.setModel(new DefaultComboBoxModel<Months>(Months.values()));
		//comboMonth.setModel(new DefaultComboBoxModel<Months> (new String [] {"<Month>", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"}));
		//int month;
		panel_1.add(comboMonth);
		
		
		comboMonth.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				Months monthEnum = (Months) comboMonth.getSelectedItem();
				switch(monthEnum){
				case Month:
					month = 0;
					setDays31();
					break;
				case JANUARY:
					month = 1;
					setDays31();
					break;
				case FEBRUARY:
					month = 2;
					setFebDays();
					break;
				case MARCH:
					month = 3;
					setDays31();
					break;
				case APRIL:
					month = 4;
					setDays30();
					break;
				case MAY:
					month = 5;
					setDays31();
					break;
				case JUNE:
					month = 6;
					setDays30();
					break;
				case JULY:
					month = 7;
					setDays31();
					break;
				case AUGUST:
					month = 8;
					setDays31();
					break;
				case SEPTEMBER:
					month = 9;
					setDays30();
					break;
				case OCTOBER:
					month = 10;
					setDays31();
					break;
				case NOVEMBER:
					month = 11;
					setDays30();
					break;
				case DECEMBER:
					month = 12;
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
				if (yearString.equals("Year")){
					year = 1;
				}
				else {
					year = Integer.parseInt(yearString);
				}
				setFebDays();
				
				System.out.print("year:");
				System.out.println(year);
						

			}	
		});
		
		JLabel lblSessionEndTime = new JLabel("Session End Time:");
		sl_panel_1.putConstraint(SpringLayout.NORTH, lblSessionEndTime, 6, SpringLayout.SOUTH, comboMonth);
		sl_panel_1.putConstraint(SpringLayout.WEST, lblSessionEndTime, 0, SpringLayout.WEST, lblSessionName);
		panel_1.add(lblSessionEndTime);
		
		JComboBox comboTime = new JComboBox();
		sl_panel_1.putConstraint(SpringLayout.NORTH, comboTime, 6, SpringLayout.SOUTH, lblSessionEndTime);
		sl_panel_1.putConstraint(SpringLayout.WEST, comboTime, 0, SpringLayout.WEST, lblSessionName);
		sl_panel_1.putConstraint(SpringLayout.EAST, comboTime, 60, SpringLayout.WEST, lblSessionName);
		panel_1.add(comboTime);
		
		JComboBox comboAMPM = new JComboBox();
		sl_panel_1.putConstraint(SpringLayout.WEST, comboAMPM, 6, SpringLayout.EAST, comboTime);
		sl_panel_1.putConstraint(SpringLayout.SOUTH, comboAMPM, 0, SpringLayout.SOUTH, comboTime);
		sl_panel_1.putConstraint(SpringLayout.EAST, comboAMPM, 50, SpringLayout.EAST, comboTime);
		panel_1.add(comboAMPM);
		
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
		comboDay.setModel(new DefaultComboBoxModel<String>(new String[] {"Day", "1", "2", "3", "4","5", "6", "7", "8",
				"9", "10", "11", "12","13", "14", "15", "16", "17", "18", "19", "20",
				"21", "22","23", "24", "25", "26", "27", "28", "29", "30", "31" }));
		System.out.println("Displaying 31 days");
	}
	
	public void setDays30(){
		comboDay.setModel(new DefaultComboBoxModel<String>(new String[] {"Day", "1", "2", "3", "4","5", "6", "7", "8",
				"9", "10", "11", "12","13", "14", "15", "16", "17", "18", "19", "20",
				"21", "22","23", "24", "25", "26", "27", "28", "29", "30" }));
		System.out.println("Displaying 30 days");
	}

	public void setFebDays(){
		if ((year%4)==0){
			comboDay.setModel(new DefaultComboBoxModel<String>(new String[] {"Day", "1", "2", "3", "4","5", "6", "7", "8",
					"9", "10", "11", "12","13", "14", "15", "16", "17", "18", "19", "20",
					"21", "22","23", "24", "25", "26", "27", "28", "29"}));
		}
		else {
			comboDay.setModel(new DefaultComboBoxModel<String>(new String[] {"Day", "1", "2", "3", "4","5", "6", "7", "8",
					"9", "10", "11", "12","13", "14", "15", "16", "17", "18", "19", "20",
					"21", "22","23", "24", "25", "26", "27", "28"}));
		}
		System.out.println("Displaying feb days");
		
	}
	public void setYearDropdown(){
		comboYear.setModel(new DefaultComboBoxModel<String>(new String[] {
				"Year", "2014", "2015", "2016", "2017", "2018", "2019" , "2020", 
				"2021", "2022", "2023", "2024", "2025", "2026", "2027", "2028", "2029", "2030", 
				"2031", "2032", "2033", "2034"}));
	}
}


