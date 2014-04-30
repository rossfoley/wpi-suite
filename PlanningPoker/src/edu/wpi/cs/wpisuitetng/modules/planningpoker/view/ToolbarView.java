/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Team8s
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.planningpoker.view;


import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.DefaultToolbarView;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.buttons.PlanningPokerSessionButtonsPanel;

/**
 * Sets up upper toolbar of RequirementManager tab
 * 
 *
 * @version $Revision: 1.0 $
 * @author rossfoley
 */
public class ToolbarView  extends DefaultToolbarView {

	private final PlanningPokerSessionButtonsPanel sessionButton = new PlanningPokerSessionButtonsPanel();
	
	/**
	 * Creates and positions option buttons in upper toolbar
	 * @param visible boolean
	 */
	public ToolbarView(boolean visible) {
		this.addGroup(sessionButton);

	}
		
	/** 
	 * Method getSessionButton
	 * 
	 * @return PlanningPokerSessionButtons Panel
	 */
	public PlanningPokerSessionButtonsPanel getSessionButton(){
		return sessionButton;
	}
}
