/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.planningpoker.gui;

/**
 * @author Amanda
 *
 */

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

public class BetterNewGameTab {
	private static JTextField textFieldSessionField;
	private static JTextField textFieldDescription;
	private static JComboBox<String> comboMonth;
	
	/**
	 * @wbp.parser.entryPoint
	 */
	public static JPanel createJPanel(){
		JPanel panel = new JPanel();
		SpringLayout sl_panel = new SpringLayout();
		panel.setLayout(sl_panel);
		
		JPanel panel_1 = new JPanel();
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
		panel_1.setLayout(sl_panel_1);
		
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PlanningPokerSession pokerSession = new PlanningPokerSession();
				pokerSession.setOpen(true);
				pokerSession.setName(textFieldSessionField.getText());
				pokerSession.setDescription(textFieldDescription.getText());
				//int month = Integer.parseInt(comboMonth);
				//int day = Integer.parseInt(comboDay);
				//int year = Integer.parseInt(comboYear);
				GregorianCalendar endDate;
				if ((month!=null)&&(day!=null)&&(year!=null)){
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
		sl_panel_1.putConstraint(SpringLayout.NORTH, lblEndDate, 6, SpringLayout.SOUTH, textFieldDescription);
		sl_panel_1.putConstraint(SpringLayout.WEST, lblEndDate, 0, SpringLayout.WEST, lblSessionName);
		panel_1.add(lblEndDate);
		
		JComboBox<String> comboMonth = new JComboBox<String>();
		sl_panel_1.putConstraint(SpringLayout.NORTH, comboMonth, 6, SpringLayout.SOUTH, lblEndDate);
		sl_panel_1.putConstraint(SpringLayout.WEST, comboMonth, 0, SpringLayout.WEST, lblEndDate);
		sl_panel_1.putConstraint(SpringLayout.EAST, comboMonth, -10, SpringLayout.EAST, lblEndDate);
		panel_1.add(comboMonth);
		
		JComboBox<String> comboDay = new JComboBox<String>();
		sl_panel_1.putConstraint(SpringLayout.NORTH, comboDay, 6, SpringLayout.SOUTH, lblEndDate);
		sl_panel_1.putConstraint(SpringLayout.WEST, comboDay, 6, SpringLayout.EAST, comboMonth);
		sl_panel_1.putConstraint(SpringLayout.EAST, comboDay, 50, SpringLayout.EAST, comboMonth);
		panel_1.add(comboDay);
		
		JComboBox<String> comboYear = new JComboBox<String>();
		sl_panel_1.putConstraint(SpringLayout.WEST, comboYear, 6, SpringLayout.EAST, comboDay);
		sl_panel_1.putConstraint(SpringLayout.EAST, comboYear, 60, SpringLayout.EAST, comboDay);
		sl_panel_1.putConstraint(SpringLayout.NORTH, comboYear, 0, SpringLayout.NORTH, comboMonth);
		panel_1.add(comboYear);
		
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
}
