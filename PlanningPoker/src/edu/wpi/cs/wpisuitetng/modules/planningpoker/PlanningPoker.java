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
import java.util.List;

import javax.swing.*;

import edu.wpi.cs.wpisuitetng.janeway.modules.IJanewayModule;
import edu.wpi.cs.wpisuitetng.janeway.modules.JanewayTabModel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.controller.GetEmailController;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.timingmanager.PlanningPokerSessionUpdater;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.timingmanager.TimingManager;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.MainView;
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
	
	private final List<JanewayTabModel> tabs;
	
	public PlanningPoker() {
		tabs = new ArrayList<JanewayTabModel>();

		final ToolbarView toolBar = new ToolbarView(true);
		final MainView mainPanel = new MainView();

		ViewEventController.getInstance().setMainView(mainPanel);
		ViewEventController.getInstance().setToolBar(toolBar);
		
		// Create a tab model that contains the toolbar panel and the main content panel
		final JanewayTabModel tab1 = new JanewayTabModel(getName(), new ImageIcon(), toolBar, mainPanel);

		// Register all of the polling classes
		TimingManager.getInstance().addPollable(new PlanningPokerSessionUpdater());
		// Calls GetEmailController.getInstance() so that it can start polling.
		GetEmailController.getInstance();
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
