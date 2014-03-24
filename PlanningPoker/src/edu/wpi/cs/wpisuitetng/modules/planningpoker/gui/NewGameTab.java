package edu.wpi.cs.wpisuitetng.modules.planningpoker.gui;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SpringLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JList;

public final class NewGameTab {
	private static JTextField textField;
	/**
	 * //@wbp.parser.entryPoint
	 * //@wbp.factory
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
		JComboBox comboBox = new JComboBox();
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
		JComboBox comboBox_1 = new JComboBox();
		sl_panel.putConstraint(SpringLayout.NORTH, comboBox_1, 6, SpringLayout.SOUTH, comboBox);
		sl_panel.putConstraint(SpringLayout.WEST, comboBox_1, 73, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.EAST, comboBox_1, 0, SpringLayout.EAST, textField);
		panel.add(comboBox_1);
		
		// creates a label for the second dropdown
		JLabel lblTimer = new JLabel("Timer:");
		sl_panel.putConstraint(SpringLayout.NORTH, lblTimer, 3, SpringLayout.NORTH, comboBox_1);
		sl_panel.putConstraint(SpringLayout.EAST, lblTimer, 0, SpringLayout.EAST, lblModerator);
		panel.add(lblTimer);
		
		
		
		
		return panel;
	}
}