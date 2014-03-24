/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.planningpoker;

import java.util.List;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import edu.wpi.cs.wpisuitetng.janeway.modules.IJanewayModule;
import edu.wpi.cs.wpisuitetng.janeway.modules.JanewayTabModel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.gui.TestGui_A;

/**
 * Description
 *
 * @author rossfoley
 * @version Mar 21, 2014
 */
public class PlanningPoker implements IJanewayModule {
	
	private ArrayList<JanewayTabModel> tabs;
	
	public PlanningPoker() {
		
	    tabs = new ArrayList<JanewayTabModel>();
	    
	    // Create a JPanel to hold the toolbar for the tab
	    JPanel toolbarPanel = new JPanel();
	    SpringLayout sl_toolbarPanel = new SpringLayout();
	    toolbarPanel.setLayout(sl_toolbarPanel);
	    JLabel label = new JLabel("Planning Poker toolbar");
	    sl_toolbarPanel.putConstraint(SpringLayout.NORTH, label, 7, SpringLayout.NORTH, toolbarPanel);
	    sl_toolbarPanel.putConstraint(SpringLayout.WEST, label, 171, SpringLayout.WEST, toolbarPanel);
	    toolbarPanel.add(label); // add a label with some placeholder text
	    toolbarPanel.setBorder(BorderFactory.createLineBorder(Color.blue, 2)); // add a border so you can see the panel
		//*/
		
	    // Create a JPanel to hold the main contents of the tab
	    JPanel mainPanel = new JPanel();
	    SpringLayout sl_mainPanel = new SpringLayout();
	    mainPanel.setLayout(sl_mainPanel);
	    JLabel label2 = new JLabel("Planning Poker");
	    sl_mainPanel.putConstraint(SpringLayout.NORTH, label2, 7, SpringLayout.NORTH, mainPanel);
	    sl_mainPanel.putConstraint(SpringLayout.WEST, label2, 190, SpringLayout.WEST, mainPanel);
	    mainPanel.add(label2);
	    mainPanel.setBorder(BorderFactory.createLineBorder(Color.green, 2));

	    // Create a tab model that contains the toolbar panel and the main content panel
	    JanewayTabModel tab1 = new JanewayTabModel(getName(), new ImageIcon(), toolbarPanel, mainPanel);
	    
	    JScrollPane scrollPane = new JScrollPane();
	    sl_mainPanel.putConstraint(SpringLayout.NORTH, scrollPane, 27, SpringLayout.NORTH, mainPanel);
	    sl_mainPanel.putConstraint(SpringLayout.WEST, scrollPane, 10, SpringLayout.WEST, mainPanel);
	    sl_mainPanel.putConstraint(SpringLayout.SOUTH, scrollPane, -12, SpringLayout.SOUTH, mainPanel);
	    sl_mainPanel.putConstraint(SpringLayout.EAST, scrollPane, 110, SpringLayout.WEST, mainPanel);
	    mainPanel.add(scrollPane);
	    
	    JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
	    sl_mainPanel.putConstraint(SpringLayout.NORTH, tabbedPane, 6, SpringLayout.SOUTH, label2);
	    sl_mainPanel.putConstraint(SpringLayout.WEST, tabbedPane, 6, SpringLayout.EAST, scrollPane);
	    sl_mainPanel.putConstraint(SpringLayout.SOUTH, tabbedPane, -12, SpringLayout.SOUTH, mainPanel);
	    sl_mainPanel.putConstraint(SpringLayout.EAST, tabbedPane, 326, SpringLayout.EAST, scrollPane);
	    mainPanel.add(tabbedPane);
	    
	    JButton btnNewGame = new JButton("New Game");
	    sl_toolbarPanel.putConstraint(SpringLayout.WEST, btnNewGame, 10, SpringLayout.WEST, toolbarPanel);
	    sl_toolbarPanel.putConstraint(SpringLayout.SOUTH, btnNewGame, -33, SpringLayout.SOUTH, toolbarPanel);
	    toolbarPanel.add(btnNewGame);

	    // Add the tab to the list of tabs owned by this module
	    tabs.add(tab1);
	    //*/
		
	}
	
	/*
	 * @see edu.wpi.cs.wpisuitetng.janeway.modules.IJanewayModule#getName()
	 */
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Planning Poker";
	}

	/*
	 * @see edu.wpi.cs.wpisuitetng.janeway.modules.IJanewayModule#getTabs()
	 */
	@Override
	public List<JanewayTabModel> getTabs() {
		// TODO Auto-generated method stub
		return tabs;
	}

}
