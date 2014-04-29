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

/**
 * Interface for objects that will need regular polling
 * @author Perry
 * @version 1
 *
 */
public interface IPollable {
	/**
	 * Function to be called repeatedly, i.e. checking for updates
	 */
	void pollFunction();
}
