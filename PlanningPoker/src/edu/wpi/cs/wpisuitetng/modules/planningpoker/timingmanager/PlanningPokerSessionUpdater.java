/*******************************************************************************
 * Copyright (c) 2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: TheTeam8s
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.planningpoker.timingmanager;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.controller.CheckForUpdatesController;

/**
 * Check for updates to Planning Poker Sessions
 * @author rossfoley
 * @version 1
 */
public class PlanningPokerSessionUpdater implements IPollable {

	/* (non-Javadoc)
	 * @see edu.wpi.cs.wpisuitetng.modules.planningpoker.timingmanager.IPollable#pollFunction()
	 */
	@Override
	public void pollFunction() {
		CheckForUpdatesController.getInstance().checkForUpdates();
	}

}
