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

import java.util.EventObject;

public class SelectedRequirementEvent extends EventObject {
	private final int selectedRequirment;
	
	/**
	 * Constructor for Event
	 * @param source
	 * @param areRequirementsSelected
	 */
	public SelectedRequirementEvent(Object source, int selectedRequirement) {
		super(source);
		
		this.selectedRequirment = selectedRequirement;
	}
	
	/**
	 * Getter for the selected requirement
	 * @return	The selected requirement
	 */
	public int getSelectedRequirement() {
		return this.selectedRequirment;
	}

}
