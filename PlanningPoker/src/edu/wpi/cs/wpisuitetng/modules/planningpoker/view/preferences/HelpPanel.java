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

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SpringLayout;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.JTextPane;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.ViewEventController;

/**
 * Displays the help panel from within the options overview panel when "help panel" is
 * selected from the drop down selection box.
 * 
 * @author Brian Flynn
 * 
 */
public class HelpPanel extends JSplitPane{
	
	private final HelpTreePanel treePanel;
	
	/**
	 * @return the treePanel
	 */
	public HelpTreePanel getTreePanel() {
		return treePanel;
	}

	/**
	 * @return the helpGuidePanel
	 */
	public HelpTextPanel getHelpTextPanel() {
		return helpTextPanel;
	}

	private final HelpTextPanel helpTextPanel;
	
	public HelpPanel() {
		
		// Create the tree panel and text panel
		treePanel = new HelpTreePanel();
		helpTextPanel = new HelpTextPanel();
		
		// Put the help tree and text panel into this tab
		this.setLeftComponent(treePanel);
		this.setRightComponent(helpTextPanel);
		this.setResizeWeight(0.2);  // set the right screen to not show by default
		
		ViewEventController.getInstance().setHelpTree(treePanel);
		ViewEventController.getInstance().setHelpTextPanel(helpTextPanel);	

	}
}
