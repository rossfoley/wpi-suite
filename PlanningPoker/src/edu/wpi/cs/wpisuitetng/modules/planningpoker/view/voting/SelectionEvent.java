/*******************************************************************************
 * Copyright (c) 2014 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Team8s
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.voting;

import java.util.EventObject;

import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;

public class SelectionEvent extends EventObject{

	private Requirement requirement;
	
	public SelectionEvent(Object source, Requirement req) {
		super(source);
		requirement = req;
	}
	
	public Requirement getRequirement(){
		return requirement;
	}
}
