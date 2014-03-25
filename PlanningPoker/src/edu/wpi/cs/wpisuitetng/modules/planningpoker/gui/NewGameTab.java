package edu.wpi.cs.wpisuitetng.modules.planningpoker.gui;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SpringLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JButton;

public final class NewGameTab {
	private static JTextField textField;
	private static JTextField textField_1;
	private static JTextField textField_2;
	private static JTextField textField_3;
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
		sl_panel.putConstraint(SpringLayout.NORTH, list, -171, SpringLayout.SOUTH, panel);
		sl_panel.putConstraint(SpringLayout.WEST, list, 10, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.SOUTH, list, -10, SpringLayout.SOUTH, panel);
		sl_panel.putConstraint(SpringLayout.EAST, list, 101, SpringLayout.WEST, panel);
		panel.add(list);
		
		JList list_1 = new JList();
		sl_panel.putConstraint(SpringLayout.NORTH, list_1, 0, SpringLayout.NORTH, list);
		sl_panel.putConstraint(SpringLayout.SOUTH, list_1, -10, SpringLayout.SOUTH, panel);
		sl_panel.putConstraint(SpringLayout.EAST, list_1, 197, SpringLayout.EAST, list);
		panel.add(list_1);
		
		JButton btnAdd = new JButton("Add");
		sl_panel.putConstraint(SpringLayout.EAST, btnAdd, -419, SpringLayout.EAST, panel);
		sl_panel.putConstraint(SpringLayout.WEST, list_1, 17, SpringLayout.EAST, btnAdd);
		sl_panel.putConstraint(SpringLayout.SOUTH, btnAdd, -99, SpringLayout.SOUTH, panel);
		panel.add(btnAdd);
		
		JButton btnRemove = new JButton("Remove");
		sl_panel.putConstraint(SpringLayout.NORTH, btnRemove, 6, SpringLayout.SOUTH, btnAdd);
		sl_panel.putConstraint(SpringLayout.EAST, btnRemove, 0, SpringLayout.EAST, btnAdd);
		panel.add(btnRemove);
		
		JLabel lblMonth = new JLabel("Month:");
		sl_panel.putConstraint(SpringLayout.WEST, lblMonth, 0, SpringLayout.WEST, list_1);
		sl_panel.putConstraint(SpringLayout.SOUTH, lblMonth, 0, SpringLayout.SOUTH, lblModerator);
		panel.add(lblMonth);
		
		JLabel lblDay = new JLabel("Day:");
		sl_panel.putConstraint(SpringLayout.NORTH, lblDay, 3, SpringLayout.NORTH, comboBox);
		sl_panel.putConstraint(SpringLayout.EAST, lblDay, 0, SpringLayout.EAST, lblMonth);
		panel.add(lblDay);
		
		JLabel lblYear = new JLabel("  Year:");
		sl_panel.putConstraint(SpringLayout.NORTH, lblYear, 3, SpringLayout.NORTH, comboBox_1);
		sl_panel.putConstraint(SpringLayout.WEST, lblYear, 0, SpringLayout.WEST, list_1);
		sl_panel.putConstraint(SpringLayout.EAST, lblYear, 0, SpringLayout.EAST, lblMonth);
		panel.add(lblYear);
		
		textField_1 = new JTextField();
		textField_1.setToolTipText("month");
		sl_panel.putConstraint(SpringLayout.WEST, textField_1, 6, SpringLayout.EAST, lblMonth);
		sl_panel.putConstraint(SpringLayout.SOUTH, textField_1, 0, SpringLayout.SOUTH, textField);
		panel.add(textField_1);
		textField_1.setColumns(10);
		
		textField_2 = new JTextField();
		textField_2.setToolTipText("day");
		sl_panel.putConstraint(SpringLayout.NORTH, textField_2, 0, SpringLayout.NORTH, comboBox);
		sl_panel.putConstraint(SpringLayout.EAST, textField_2, 0, SpringLayout.EAST, textField_1);
		panel.add(textField_2);
		textField_2.setColumns(10);
		
		textField_3 = new JTextField();
		textField_3.setToolTipText("year");
		sl_panel.putConstraint(SpringLayout.SOUTH, textField_3, 0, SpringLayout.SOUTH, comboBox_1);
		sl_panel.putConstraint(SpringLayout.EAST, textField_3, 0, SpringLayout.EAST, textField_1);
		panel.add(textField_3);
		textField_3.setColumns(10);
		
		JButton btnSubmit = new JButton("Submit");
		sl_panel.putConstraint(SpringLayout.NORTH, btnSubmit, 0, SpringLayout.NORTH, lblModerator);
		sl_panel.putConstraint(SpringLayout.EAST, btnSubmit, -10, SpringLayout.EAST, panel);
		panel.add(btnSubmit);
		
		
		
		
		return panel;
	}
}