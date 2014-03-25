package edu.wpi.cs.wpisuitetng.modules.planningpoker.gui;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SpringLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JButton;
import javax.swing.border.LineBorder;
import java.awt.Color;

public final class NewGameTab {
	private static JTextField textField;
	private static JTextField textField_1;
	private static JTextField textField_2;
	private static JTextField textField_3;
	private static JTextField textField_4;
	/**
	 * //@wbp.parser.entryPoint
	 * //@wbp.factory
	 * @wbp.parser.entryPoint
	 */
	public static JPanel createJPanel() {
		
		//create panel to add to planning poker tabs
		JPanel panel = new JPanel();
		SpringLayout sl_panel = new SpringLayout();
		panel.setLayout(sl_panel);
		
		// create label
		JLabel lblModerator = new JLabel("Moderator:");
		panel.add(lblModerator);
		
		//create text field
		textField = new JTextField();
		sl_panel.putConstraint(SpringLayout.NORTH, lblModerator, 3, SpringLayout.NORTH, textField);
		sl_panel.putConstraint(SpringLayout.EAST, lblModerator, -6, SpringLayout.WEST, textField);
		sl_panel.putConstraint(SpringLayout.NORTH, textField, 10, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.WEST, textField, 73, SpringLayout.WEST, panel);
		panel.add(textField);
		textField.setColumns(10);
		
		// create dropdown
		JComboBox<String> comboBox = new JComboBox<String>();
		sl_panel.putConstraint(SpringLayout.NORTH, comboBox, 6, SpringLayout.SOUTH, textField);
		sl_panel.putConstraint(SpringLayout.WEST, comboBox, 73, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.EAST, comboBox, 0, SpringLayout.EAST, textField);
		panel.add(comboBox);
		
		// create label for dropdown menu
		JLabel lblDeck = new JLabel("Deck:");
		sl_panel.putConstraint(SpringLayout.NORTH, lblDeck, 3, SpringLayout.NORTH, comboBox);
		sl_panel.putConstraint(SpringLayout.EAST, lblDeck, 0, SpringLayout.EAST, lblModerator);
		panel.add(lblDeck);
		
		// creates a second dropdown 
		JComboBox<String> comboBox_1 = new JComboBox<String>();
		sl_panel.putConstraint(SpringLayout.NORTH, comboBox_1, 6, SpringLayout.SOUTH, comboBox);
		sl_panel.putConstraint(SpringLayout.WEST, comboBox_1, 73, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.EAST, comboBox_1, 0, SpringLayout.EAST, textField);
		panel.add(comboBox_1);
		
		// creates a label for the second dropdown
		JLabel lblTimer = new JLabel("Timer:");
		sl_panel.putConstraint(SpringLayout.NORTH, lblTimer, 3, SpringLayout.NORTH, comboBox_1);
		sl_panel.putConstraint(SpringLayout.EAST, lblTimer, 0, SpringLayout.EAST, lblModerator);
		panel.add(lblTimer);
		
		JList list = new JList();
		list.setBorder(new LineBorder(new Color(0, 0, 0)));
		sl_panel.putConstraint(SpringLayout.NORTH, list, -171, SpringLayout.SOUTH, panel);
		sl_panel.putConstraint(SpringLayout.WEST, list, 10, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.SOUTH, list, -10, SpringLayout.SOUTH, panel);
		sl_panel.putConstraint(SpringLayout.EAST, list, 110, SpringLayout.WEST, panel);
		panel.add(list);
		
		JList list_1 = new JList();
		list_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		sl_panel.putConstraint(SpringLayout.NORTH, list_1, 0, SpringLayout.NORTH, list);
		sl_panel.putConstraint(SpringLayout.WEST, list_1, 96, SpringLayout.EAST, list);
		sl_panel.putConstraint(SpringLayout.SOUTH, list_1, -10, SpringLayout.SOUTH, panel);
		sl_panel.putConstraint(SpringLayout.EAST, list_1, -294, SpringLayout.EAST, panel);
		panel.add(list_1);
		
		JButton btnAdd = new JButton("Add");
		sl_panel.putConstraint(SpringLayout.EAST, btnAdd, -17, SpringLayout.WEST, list_1);
		panel.add(btnAdd);
		
		JButton btnRemove = new JButton("Remove");
		sl_panel.putConstraint(SpringLayout.SOUTH, btnAdd, -6, SpringLayout.NORTH, btnRemove);
		sl_panel.putConstraint(SpringLayout.SOUTH, btnRemove, -70, SpringLayout.SOUTH, panel);
		sl_panel.putConstraint(SpringLayout.EAST, btnRemove, -6, SpringLayout.WEST, list_1);
		panel.add(btnRemove);
		
		JLabel lblMonth = new JLabel("Month:");
		sl_panel.putConstraint(SpringLayout.NORTH, lblMonth, 0, SpringLayout.NORTH, lblModerator);
		panel.add(lblMonth);
		
		JLabel lblDay = new JLabel("Day:");
		sl_panel.putConstraint(SpringLayout.EAST, lblMonth, 0, SpringLayout.EAST, lblDay);
		sl_panel.putConstraint(SpringLayout.NORTH, lblDay, 3, SpringLayout.NORTH, comboBox);
		panel.add(lblDay);
		
		JLabel lblYear = new JLabel("  Year:");
		sl_panel.putConstraint(SpringLayout.EAST, lblDay, 0, SpringLayout.EAST, lblYear);
		sl_panel.putConstraint(SpringLayout.NORTH, lblYear, 3, SpringLayout.NORTH, comboBox_1);
		sl_panel.putConstraint(SpringLayout.WEST, lblYear, 208, SpringLayout.WEST, panel);
		panel.add(lblYear);
		
		textField_1 = new JTextField();
		sl_panel.putConstraint(SpringLayout.NORTH, textField_1, -3, SpringLayout.NORTH, lblModerator);
		textField_1.setToolTipText("month");
		panel.add(textField_1);
		textField_1.setColumns(10);
		
		textField_2 = new JTextField();
		sl_panel.putConstraint(SpringLayout.EAST, textField_1, 0, SpringLayout.EAST, textField_2);
		sl_panel.putConstraint(SpringLayout.NORTH, textField_2, 0, SpringLayout.NORTH, comboBox);
		textField_2.setToolTipText("day");
		panel.add(textField_2);
		textField_2.setColumns(10);
		
		textField_3 = new JTextField();
		sl_panel.putConstraint(SpringLayout.EAST, lblYear, -6, SpringLayout.WEST, textField_3);
		sl_panel.putConstraint(SpringLayout.EAST, textField_2, 0, SpringLayout.EAST, textField_3);
		sl_panel.putConstraint(SpringLayout.NORTH, textField_3, 0, SpringLayout.NORTH, comboBox_1);
		sl_panel.putConstraint(SpringLayout.WEST, textField_3, 89, SpringLayout.EAST, comboBox_1);
		textField_3.setToolTipText("year");
		panel.add(textField_3);
		textField_3.setColumns(10);
		
		JButton btnSubmit = new JButton("Submit");
		sl_panel.putConstraint(SpringLayout.NORTH, btnSubmit, 0, SpringLayout.NORTH, lblModerator);
		sl_panel.putConstraint(SpringLayout.EAST, btnSubmit, -10, SpringLayout.EAST, panel);
		panel.add(btnSubmit);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		sl_panel.putConstraint(SpringLayout.NORTH, panel_1, -76, SpringLayout.NORTH, list);
		sl_panel.putConstraint(SpringLayout.WEST, panel_1, 13, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.SOUTH, panel_1, -6, SpringLayout.NORTH, list);
		sl_panel.putConstraint(SpringLayout.EAST, panel_1, 0, SpringLayout.EAST, list_1);
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
		sl_panel_1.putConstraint(SpringLayout.NORTH, btnAddRequirement, 4, SpringLayout.SOUTH, textField_4);
		sl_panel_1.putConstraint(SpringLayout.WEST, btnAddRequirement, 93, SpringLayout.WEST, panel_1);
		panel_1.add(btnAddRequirement);
		
		
		
		
		return panel;
	}
}