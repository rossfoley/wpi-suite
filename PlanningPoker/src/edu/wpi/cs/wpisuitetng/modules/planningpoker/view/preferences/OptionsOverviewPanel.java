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

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

/**
 * @author amandaadkins
 *
 */
public class OptionsOverviewPanel extends JPanel {
	private JPanel comboPanel = new JPanel();
	private JLabel comboLabel = new JLabel("Select Option To View");
	private JComboBox<String> comboBoxOptions = new JComboBox<String>();
	private JPanel lowerPanel = new JPanel();
	private PreferencesPanel prefPanel = new PreferencesPanel();
	private JPanel deckOverviewPanel = new JPanel();
	private JPanel helpPanel = new JPanel();
	private SpringLayout optionsLayout = new SpringLayout();
	private SpringLayout comboLayout = new SpringLayout();
	
	public OptionsOverviewPanel(){
		setLayout(optionsLayout);
		
		buildComboPanel();
		
		buildOptionsPanel();
		
	}
	
	public void buildComboPanel(){
		comboPanel.setLayout(comboLayout);
		
		comboLayout.putConstraint(SpringLayout.EAST, comboBoxOptions, -10, SpringLayout.EAST, comboPanel);
		comboLayout.putConstraint(SpringLayout.NORTH, comboBoxOptions, 10, SpringLayout.NORTH, comboPanel);
		comboLayout.putConstraint(SpringLayout.SOUTH, comboBoxOptions, -10, SpringLayout.SOUTH, comboPanel);
		
		comboLayout.putConstraint(SpringLayout.WEST, comboLabel, 10, SpringLayout.WEST, comboPanel);
		comboLayout.putConstraint(SpringLayout.NORTH, comboLabel, 10, SpringLayout.NORTH, comboPanel);
		comboLayout.putConstraint(SpringLayout.SOUTH, comboLabel, 10, SpringLayout.SOUTH, comboPanel);
		
		comboBoxOptions.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				parseComboBoxOptions();
			}
		});
		
		comboPanel.add(comboLabel);
		comboPanel.add(comboBoxOptions);
	}
	
	public void buildOptionsPanel(){
	
		
	}
	
	public void parseComboBoxOptions(){
		
	}
}
