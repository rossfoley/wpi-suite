/*******************************************************************************
 * Copyright (c) 2014 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Team8s
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.preferences;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SpringLayout;

import java.awt.Color;
import java.awt.Font;

/**
 * @author amandaadkins
 *
 */
public class OptionsOverviewPanel extends JSplitPane {
	private JPanel comboPanel = new JPanel();
	private JLabel comboLabel = new JLabel("Select Option To View");
	private JComboBox<String> comboBoxOptions = new JComboBox<String>();
	private PreferencesPanel prefPanel = new PreferencesPanel();
	private JPanel deckOverviewPanel = new JPanel();
	private JPanel helpPanel = new JPanel();
	private SpringLayout comboLayout = new SpringLayout();
	private String[] availableOptions = {"Help", "View Decks", "Email Preferences"};
	private int previousLowerScreenIndex;
	
	/**
	 *  constructor for OptionsOverviewPanel
	 *  builds initial panel
	 */
	public OptionsOverviewPanel(){
		setOrientation(VERTICAL_SPLIT);
		setEnabled(false);
		setDividerLocation(50);

		setTopComponent(comboPanel);
		setBottomComponent(helpPanel);
		comboBoxOptions.setSelectedIndex(0);
		previousLowerScreenIndex = 0;
	}
	
	/**
	 * builds upper panel containing combo box for different options choices
	 */
	public void buildComboPanel(){
		comboPanel.setLayout(comboLayout);
		
		comboLayout.putConstraint(SpringLayout.EAST, comboBoxOptions, -10, SpringLayout.EAST, comboPanel);
		comboLayout.putConstraint(SpringLayout.NORTH, comboBoxOptions, 10, SpringLayout.NORTH, comboPanel);
		comboLayout.putConstraint(SpringLayout.SOUTH, comboBoxOptions, -10, SpringLayout.SOUTH, comboPanel);
		
		comboLayout.putConstraint(SpringLayout.WEST, comboLabel, 10, SpringLayout.WEST, comboPanel);
		comboLayout.putConstraint(SpringLayout.VERTICAL_CENTER, comboLabel, 0, SpringLayout.VERTICAL_CENTER, comboBoxOptions);
		
		comboBoxOptions.setModel(new DefaultComboBoxModel<String>(availableOptions));
		
		comboBoxOptions.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				parseComboBoxOptions();
			}
		});

		comboBoxOptions.setBackground(Color.WHITE);
		comboLabel.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 14));
		
		comboPanel.add(comboLabel);
		comboPanel.add(comboBoxOptions);
	}
	
//	public void buildOptionsPanel(){
//		optionsLayout.putConstraint(SpringLayout.NORTH, comboPanel, 10, SpringLayout.NORTH, this);
//		optionsLayout.putConstraint(SpringLayout.EAST, comboPanel, -10, SpringLayout.EAST, this);
//		optionsLayout.putConstraint(SpringLayout.WEST, comboPanel, 10, SpringLayout.WEST, this);
//		
//		optionsLayout.putConstraint(SpringLayout.NORTH, lowerPanel, 10, SpringLayout.SOUTH, comboPanel);
//		optionsLayout.putConstraint(SpringLayout.SOUTH, lowerPanel, -10, SpringLayout.SOUTH, this);
//		optionsLayout.putConstraint(SpringLayout.EAST, lowerPanel, -10, SpringLayout.EAST, this);
//		optionsLayout.putConstraint(SpringLayout.WEST, lowerPanel, 10, SpringLayout.WEST, this);
//		
//		add(comboPanel);
//		add(lowerPanel);
//	}
//
//	
//	public void setBottomPanel(JPanel newBottomPanel){
//		optionsLayout.putConstraint(SpringLayout.NORTH, newBottomPanel, 10, SpringLayout.NORTH, lowerPanel);
//		optionsLayout.putConstraint(SpringLayout.SOUTH, newBottomPanel, -10, SpringLayout.SOUTH, lowerPanel);
//		optionsLayout.putConstraint(SpringLayout.EAST, newBottomPanel, -10, SpringLayout.EAST, lowerPanel);
//		optionsLayout.putConstraint(SpringLayout.WEST, newBottomPanel, 10, SpringLayout.WEST, lowerPanel);
//		
//		lowerPanel.add(newBottomPanel);	
//	}

	/**
	 * gets the information from the combo box used for selecting what 
	 * options to display and displays the proper option
	 */
	public void parseComboBoxOptions(){
		int index = comboBoxOptions.getSelectedIndex();
		int dividerLocation = getDividerLocation();
		if (index!=previousLowerScreenIndex){
			previousLowerScreenIndex = index;
			switch (index){
			case 0:
				setBottomComponent(helpPanel);
				break;
			case 1: 
				setBottomComponent(deckOverviewPanel);
				break;
			case 2: 
				setBottomComponent(prefPanel);
				break;
			default:
				previousLowerScreenIndex = 0;
				comboBoxOptions.setSelectedIndex(0);
				setBottomComponent(helpPanel);
			}
			setDividerLocation(dividerLocation);
		}
	}
}
