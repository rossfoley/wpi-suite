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

import java.util.EventListener;

import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;

public class SelectionListener implements EventListener{
	private Requirement requirement;
	
	public void selectionMade(SelectionEvent e){
		requirement = e.getRequirement();
	}
}
