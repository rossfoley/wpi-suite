/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.planningpoker;

import java.util.ArrayList;

import javax.swing.ImageIcon;

import edu.wpi.cs.wpisuitetng.janeway.modules.IJanewayModule;
import edu.wpi.cs.wpisuitetng.janeway.modules.JanewayTabModel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.MainView;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.gui.TestGui_A;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.ToolbarView;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.ViewEventController;

/**
 * Description
 *
 * @author rossfoley
 * @version Mar 21, 2014
 * @see edu.wpi.cs.wpisuitetng.modules.requirementmanager.RequirementManager
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
	    //*/
	    
	    // Create a tab model that contains the toolbar panel and the main content panel
	    JanewayTabModel tab1 = new JanewayTabModel(getName(), new ImageIcon(), toolbarPanel, mainPanel);
	    
	    //scroll pane for viewing current planning poker games
	    //no functionality yet
	    JScrollPane scrollPane = new JScrollPane();
	    sl_mainPanel.putConstraint(SpringLayout.NORTH, scrollPane, 27, SpringLayout.NORTH, mainPanel);
	    sl_mainPanel.putConstraint(SpringLayout.WEST, scrollPane, 10, SpringLayout.WEST, mainPanel);
	    sl_mainPanel.putConstraint(SpringLayout.SOUTH, scrollPane, -12, SpringLayout.SOUTH, mainPanel);
	    sl_mainPanel.putConstraint(SpringLayout.EAST, scrollPane, 110, SpringLayout.WEST, mainPanel);
	    mainPanel.add(scrollPane);
	    
	    
	    //planning poker tabs(new game, edit, vote, etc...)
	    final JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
	    sl_mainPanel.putConstraint(SpringLayout.NORTH, tabbedPane, 27, SpringLayout.NORTH, mainPanel);
	    sl_mainPanel.putConstraint(SpringLayout.WEST, tabbedPane, 6, SpringLayout.EAST, scrollPane);
	    sl_mainPanel.putConstraint(SpringLayout.SOUTH, tabbedPane, -12, SpringLayout.SOUTH, mainPanel);
	    sl_mainPanel.putConstraint(SpringLayout.EAST, tabbedPane, -12, SpringLayout.EAST, mainPanel);
	    mainPanel.add(tabbedPane);
	    
	    
	    
	    //tabbedPane.insertTab("Test", null, new JPanel(), null, 0);
	    /*
	    JPanel panel = NewGameTab.createJPanel();
		tabbedPane.addTab("New tab", null, panel, null);
		SpringLayout sl_panel = new SpringLayout();
		panel.setLayout(sl_panel);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		sl_panel.putConstraint(SpringLayout.NORTH, scrollPane_1, 10, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.WEST, scrollPane_1, 10, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.SOUTH, scrollPane_1, 210, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.EAST, scrollPane_1, 135, SpringLayout.WEST, panel);
		panel.add(scrollPane_1);
	    */
	    
	    //the toolbar button to create a new game
	    JButton btnNewGame = new JButton("New Game");
	    //the function performed on clicking the button
	    btnNewGame.addMouseListener(new MouseAdapter() {
	    	@Override
	    	public void mouseClicked(MouseEvent e) {
	    		//tabbedPaneTemp = tabbedPane;
	    		//JPanel panel = NewGameTab.createJPanel();
				//tabbedPane.addTab("New tab", NewGameTab.createJPanel());
	    		//int count = tabbedPane.getTabCount();
				//tabbedPane.insertTab("New Game", null, new JPanel(), null, count+1);
	    		
	    		//code to create a new tab
	    	    JPanel panel = NewGameTab.createJPanel();
	    	    PlanningPokerSession newSession = new PlanningPokerSession();
	    		tabbedPane.addTab(newSession.getName(), null, panel, null);
	    		//int count = tabbedPane.getComponentCount();
	    		//tabbedPane.setTabComponentAt( count, new ClosableTabComponent(tabbedPane));
	    		//setTabComponentAt()
	    	}
	    });
	    sl_toolbarPanel.putConstraint(SpringLayout.WEST, btnNewGame, 10, SpringLayout.WEST, toolbarPanel);
	    sl_toolbarPanel.putConstraint(SpringLayout.SOUTH, btnNewGame, -33, SpringLayout.SOUTH, toolbarPanel);
	    toolbarPanel.add(btnNewGame);

		MainView mainPanel = new MainView();
		ToolbarView toolBar = new ToolbarView(true);

		ViewEventController.getInstance().setMainView(mainPanel);
		ViewEventController.getInstance().setToolBar(toolBar);
		
		// Create a tab model that contains the toolbar panel and the main content panel
		JanewayTabModel tab1 = new JanewayTabModel(getName(), new ImageIcon(), toolBar, mainPanel);

		// Add the tab to the list of tabs owned by this module
		tabs.add(tab1);		
	}
	
	/*
	 * @see edu.wpi.cs.wpisuitetng.janeway.modules.IJanewayModule#getName()
	 */
	@Override
	public String getName() {
		return "Planning Poker";
	}

	/*
	 * @see edu.wpi.cs.wpisuitetng.janeway.modules.IJanewayModule#getTabs()
	 */
	@Override
	public List<JanewayTabModel> getTabs() {
		return tabs;
	}

}
