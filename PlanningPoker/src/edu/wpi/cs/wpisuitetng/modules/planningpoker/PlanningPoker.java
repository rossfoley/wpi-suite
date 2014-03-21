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
import javax.swing.JTextField;

import edu.wpi.cs.wpisuitetng.janeway.modules.IJanewayModule;
import edu.wpi.cs.wpisuitetng.janeway.modules.JanewayTabModel;

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
	    toolbarPanel.add(new JLabel("PostBoard toolbar placeholder")); // add a label with some placeholder text
	    toolbarPanel.setBorder(BorderFactory.createLineBorder(Color.blue, 2)); // add a border so you can see the panel

	    // Create a JPanel to hold the main contents of the tab
	    JPanel mainPanel = new JPanel();
	    mainPanel.add(new JLabel("PostBoard placeholder"));
	    mainPanel.setBorder(BorderFactory.createLineBorder(Color.green, 2));

	    // Create a tab model that contains the toolbar panel and the main content panel
	    JanewayTabModel tab1 = new JanewayTabModel(getName(), new ImageIcon(), toolbarPanel, mainPanel);

	    // Add the tab to the list of tabs owned by this module
	    tabs.add(tab1);
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
