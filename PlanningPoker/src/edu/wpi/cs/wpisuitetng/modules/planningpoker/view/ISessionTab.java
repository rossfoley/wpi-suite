/*******************************************************************************
 * Copyright (c) 2014 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Team8s
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.planningpoker.view;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;

/**
 * Provides an interface for all JPanels/tabs related to a planning poker session.
 * Examples: Editing, voting, or viewing statistics on a session
 */
public interface ISessionTab {
	
	/**
	 * Get the session that the tab was opened for 
	 * @return the session being used in this panel
	 */
	public PlanningPokerSession getDisplaySession();

}
