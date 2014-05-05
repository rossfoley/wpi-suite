/*******************************************************************************
 * Copyright (c) 2014 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Team8s
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.requirementselection;

import java.util.EventObject;

public class RequirementsSelectedEvent extends EventObject {
	private final boolean areRequirementsSelected;
	
	public RequirementsSelectedEvent(Object source, boolean areRequirementsSelected) {
		super(source);
		
		this.areRequirementsSelected = areRequirementsSelected;
	}
	
	public boolean areRequirementsSelected() {
		return areRequirementsSelected;
	}

}
