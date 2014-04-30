/*******************************************************************************
 * Copyright (c) 2014 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Team8s
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.overview;

import javax.swing.JSplitPane;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.ViewEventController;

/**
 * Sets up directory tree of all planning poker sessions
 * @author rossfoley
 * @version $Revision: 1.0 $
 */
public class OverviewPanel extends JSplitPane {

	private final OverviewTreePanel treePanel;
	private final OverviewDetailPanel detailPanel;
	
	public OverviewPanel() {

		// Create the tree panel and detail panel
		treePanel = new OverviewTreePanel();
		detailPanel = new OverviewDetailPanel();
		
		// Put the overview tree and detail panel into this tab
		this.setLeftComponent(treePanel);
		this.setRightComponent(detailPanel);
		this.setResizeWeight(0.2);  // set the right screen to not show by default

		ViewEventController.getInstance().setOverviewTree(treePanel);
		ViewEventController.getInstance().setOverviewDetailPanel(detailPanel);	
	}
}
