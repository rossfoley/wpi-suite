/*******************************************************************************
 * Copyright (c) 2014 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Team8s
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.statistics;

import java.util.EventListener;

/**
 * A contract between a SelectedRequirement source and listener classes
 */
public class SelectedRequirementListener implements EventListener {
	private int selectedRequirement = -1;
	
	/**
	 *  Called whenever a requirement has been selected by a
	 *  SelectedRequirement source object 
	 */
	public void setSelectedRequirement(SelectedRequirementEvent e) {
		selectedRequirement = e.getSelectedRequirement();
	}
	
	public int getSelectedRequirement() {
		return this.selectedRequirement;
	}

}
