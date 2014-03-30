package edu.wpi.cs.wpisuitetng.modules.planningpoker.gui;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.GregorianCalendar;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.border.LineBorder;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSessionModel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.ViewEventController;

public final class NewGameTab {
	private static JTextField sessionNameField;
	private static JTextField textField_1;
	private static JTextField textField_2;
	private static JTextField textField_3;
	private static JTextField textField_4;
	private static JTextField description;
	/**
	 * //@wbp.parser.entryPoint
	 * //@wbp.factory
	 * @wbp.parser.entryPoint
	 */
	public static JPanel createJPanel() {
		
		//create panel to add to planning poker tabs
		final JPanel panel = new JPanel();
		SpringLayout sl_panel = new SpringLayout();
		panel.setLayout(sl_panel);
		
		// create label
		JLabel lblModerator = new JLabel("Session Name:");
		panel.add(lblModerator);
		
		//create text field
		sessionNameField = new JTextField();
		sl_panel.putConstraint(SpringLayout.NORTH, lblModerator, 3, SpringLayout.NORTH, sessionNameField);
		sl_panel.putConstraint(SpringLayout.EAST, lblModerator, -6, SpringLayout.WEST, sessionNameField);
		sl_panel.putConstraint(SpringLayout.NORTH, sessionNameField, 10, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.WEST, sessionNameField, 103, SpringLayout.WEST, panel);
		panel.add(sessionNameField);
		sessionNameField.setColumns(10);
		
		// create dropdown
		JComboBox<String> comboBox = new JComboBox<String>();
		sl_panel.putConstraint(SpringLayout.NORTH, comboBox, 6, SpringLayout.SOUTH, sessionNameField);
		sl_panel.putConstraint(SpringLayout.WEST, comboBox, 103, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.EAST, comboBox, 0, SpringLayout.EAST, sessionNameField);
		panel.add(comboBox);
		
		// create label for dropdown menu
		JLabel lblDeck = new JLabel("Deck:");
		sl_panel.putConstraint(SpringLayout.NORTH, lblDeck, 12, SpringLayout.SOUTH, lblModerator);
		sl_panel.putConstraint(SpringLayout.EAST, lblDeck, -9, SpringLayout.WEST, comboBox);
		panel.add(lblDeck);
		
		// creates a second dropdown 
		JComboBox<String> comboBox_1 = new JComboBox<String>();
		sl_panel.putConstraint(SpringLayout.NORTH, comboBox_1, 6, SpringLayout.SOUTH, comboBox);
		sl_panel.putConstraint(SpringLayout.EAST, comboBox_1, 0, SpringLayout.EAST, sessionNameField);
		panel.add(comboBox_1);
		
		// creates a label for the second dropdown
		JLabel lblTimer = new JLabel("Timer:");
		sl_panel.putConstraint(SpringLayout.NORTH, lblTimer, 12, SpringLayout.SOUTH, lblDeck);
		sl_panel.putConstraint(SpringLayout.WEST, comboBox_1, 6, SpringLayout.EAST, lblTimer);
		sl_panel.putConstraint(SpringLayout.WEST, lblTimer, 0, SpringLayout.WEST, lblDeck);
		panel.add(lblTimer);
		
		JList list = new JList();
		sl_panel.putConstraint(SpringLayout.WEST, list, 13, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.SOUTH, list, -10, SpringLayout.SOUTH, panel);
		list.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.add(list);
		
		JList list_1 = new JList();
		sl_panel.putConstraint(SpringLayout.SOUTH, list_1, -10, SpringLayout.SOUTH, panel);
		sl_panel.putConstraint(SpringLayout.EAST, list_1, -40, SpringLayout.EAST, panel);
		list_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.add(list_1);
		
		JButton btnAdd = new JButton("Add");
		sl_panel.putConstraint(SpringLayout.EAST, btnAdd, -23, SpringLayout.WEST, list_1);
		panel.add(btnAdd);
		
		JButton btnRemove = new JButton("Remove");
		sl_panel.putConstraint(SpringLayout.EAST, list, -15, SpringLayout.WEST, btnRemove);
		sl_panel.putConstraint(SpringLayout.WEST, list_1, 15, SpringLayout.EAST, btnRemove);
		sl_panel.putConstraint(SpringLayout.NORTH, btnRemove, 6, SpringLayout.SOUTH, btnAdd);
		panel.add(btnRemove);
		
		JLabel lblMonth = new JLabel("Month:");
		sl_panel.putConstraint(SpringLayout.NORTH, lblMonth, 0, SpringLayout.NORTH, lblModerator);
		sl_panel.putConstraint(SpringLayout.EAST, lblMonth, -386, SpringLayout.EAST, panel);
		panel.add(lblMonth);
		
		JLabel lblDay = new JLabel("Day:");
		sl_panel.putConstraint(SpringLayout.NORTH, lblDay, 3, SpringLayout.NORTH, comboBox);
		sl_panel.putConstraint(SpringLayout.EAST, lblDay, 0, SpringLayout.EAST, lblMonth);
		panel.add(lblDay);
		
		JLabel lblYear = new JLabel("Year:");
		sl_panel.putConstraint(SpringLayout.NORTH, lblYear, 3, SpringLayout.NORTH, comboBox_1);
		sl_panel.putConstraint(SpringLayout.WEST, lblYear, 0, SpringLayout.WEST, lblMonth);
		panel.add(lblYear);
		
		textField_1 = new JTextField();
		sl_panel.putConstraint(SpringLayout.WEST, btnRemove, 0, SpringLayout.WEST, textField_1);
		sl_panel.putConstraint(SpringLayout.NORTH, textField_1, -3, SpringLayout.NORTH, lblModerator);
		sl_panel.putConstraint(SpringLayout.WEST, textField_1, 6, SpringLayout.EAST, lblMonth);
		textField_1.setToolTipText("month");
		panel.add(textField_1);
		textField_1.setColumns(10);
		
		textField_2 = new JTextField();
		sl_panel.putConstraint(SpringLayout.NORTH, textField_2, 0, SpringLayout.NORTH, comboBox);
		sl_panel.putConstraint(SpringLayout.WEST, textField_2, 6, SpringLayout.EAST, lblDay);
		textField_2.setToolTipText("day");
		panel.add(textField_2);
		textField_2.setColumns(10);
		
		textField_3 = new JTextField();
		sl_panel.putConstraint(SpringLayout.NORTH, textField_3, 0, SpringLayout.NORTH, comboBox_1);
		sl_panel.putConstraint(SpringLayout.WEST, textField_3, 8, SpringLayout.EAST, lblYear);
		textField_3.setToolTipText("year");
		panel.add(textField_3);
		textField_3.setColumns(10);
		
		JButton btnSubmit = new JButton("Submit");
		sl_panel.putConstraint(SpringLayout.NORTH, btnSubmit, -1, SpringLayout.NORTH, comboBox);
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Create a new session, have it public by default, and include attributes
				PlanningPokerSession pokerSession = new PlanningPokerSession();
				pokerSession.setOpen(true);
				pokerSession.setName(sessionName.getText());
				int month = Integer.parseInt(textField_1.getText()) - 1; // Months start at 0
				int day = Integer.parseInt(textField_2.getText());
				int year = Integer.parseInt(textField_3.getText());
				GregorianCalendar date = new GregorianCalendar(year, month, day);
				pokerSession.setEndDate(date);
				PlanningPokerSessionModel.getInstance().addPlanningPokerSession(pokerSession);
				ViewEventController.getInstance().removeTab((JComponent)panel.getComponentAt(0,0));// this thing closes the tabs
				}
				else {
					// reprompt for empty fields

			}
		});
		panel.add(btnSubmit);
		
		JPanel panel_1 = new JPanel();
		sl_panel.putConstraint(SpringLayout.SOUTH, panel_1, -177, SpringLayout.SOUTH, panel);
		sl_panel.putConstraint(SpringLayout.NORTH, list_1, 6, SpringLayout.SOUTH, panel_1);
		sl_panel.putConstraint(SpringLayout.NORTH, btnAdd, 58, SpringLayout.SOUTH, panel_1);
		sl_panel.putConstraint(SpringLayout.EAST, btnSubmit, 0, SpringLayout.EAST, panel_1);
		sl_panel.putConstraint(SpringLayout.NORTH, list, 6, SpringLayout.SOUTH, panel_1);
		sl_panel.putConstraint(SpringLayout.WEST, panel_1, 13, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.EAST, panel_1, -40, SpringLayout.EAST, panel);
		panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.add(panel_1);
		SpringLayout sl_panel_1 = new SpringLayout();
		panel_1.setLayout(sl_panel_1);
		
		textField_4 = new JTextField();
		sl_panel_1.putConstraint(SpringLayout.NORTH, textField_4, 10, SpringLayout.NORTH, panel_1);
		sl_panel_1.putConstraint(SpringLayout.WEST, textField_4, 10, SpringLayout.WEST, panel_1);
		sl_panel_1.putConstraint(SpringLayout.EAST, textField_4, -11, SpringLayout.EAST, panel_1);
		panel_1.add(textField_4);
		textField_4.setColumns(10);
		
		JButton btnAddRequirement = new JButton("Add Requirement");
		sl_panel_1.putConstraint(SpringLayout.NORTH, btnAddRequirement, 5, SpringLayout.SOUTH, textField_4);
		sl_panel_1.putConstraint(SpringLayout.WEST, btnAddRequirement, 238, SpringLayout.WEST, panel_1);
		panel_1.add(btnAddRequirement);
		
		description = new JTextField();
		sl_panel.putConstraint(SpringLayout.NORTH, panel_1, 40, SpringLayout.SOUTH, description);
		sl_panel.putConstraint(SpringLayout.EAST, description, 0, SpringLayout.EAST, textField_1);
		sl_panel.putConstraint(SpringLayout.NORTH, description, 18, SpringLayout.SOUTH, comboBox_1);
		sl_panel.putConstraint(SpringLayout.SOUTH, description, -287, SpringLayout.SOUTH, panel);
		panel.add(description);
		description.setColumns(10);
		
		JLabel lblDescriptionAndNotes = new JLabel("<html>Description<br />or Notes:</html>");
		sl_panel.putConstraint(SpringLayout.NORTH, lblDescriptionAndNotes, 21, SpringLayout.SOUTH, lblTimer);
		sl_panel.putConstraint(SpringLayout.EAST, lblDescriptionAndNotes, -555, SpringLayout.EAST, panel);
		sl_panel.putConstraint(SpringLayout.WEST, description, 6, SpringLayout.EAST, lblDescriptionAndNotes);
		lblDescriptionAndNotes.setHorizontalAlignment(SwingConstants.TRAILING);
		panel.add(lblDescriptionAndNotes);
		
		
		
		
		return panel;
	}
}
