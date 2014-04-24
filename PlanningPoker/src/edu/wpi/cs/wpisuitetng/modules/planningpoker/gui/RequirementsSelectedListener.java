/*******************************************************************************
 * Copyright (c) 2014 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Team8s
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.planningpoker.gui;

import java.util.EventListener;

/**
 * A contract between a RequirementsSelected source and listener classes
 */
public class RequirementsSelectedListener implements EventListener {
	private boolean areRequirementsSelected;
	
	/**
	 *  Called whenever a requirement has been selected by a
	 *  RequirementSelected source object 
	 */
	public void setRequirementsSelected(RequirementsSelectedEvent e) {
		areRequirementsSelected = e.areRequirementsSelected();
	}
	
	public boolean areRequirementsSelected() {
		return areRequirementsSelected;
	}

}
