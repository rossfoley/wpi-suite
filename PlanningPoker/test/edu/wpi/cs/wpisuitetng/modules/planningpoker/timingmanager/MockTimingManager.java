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

import java.util.List;

/**
 * Mock version of the TimingManager used for testing to allow access to otherwise protected fields
 * @author Perry
 * @version 1
 *
 */
public class MockTimingManager extends TimingManager {
	
	protected static MockTimingManager instance;
	
	/**
	 * Constructor for MockTimingManager
	 */
	MockTimingManager(){
		super();
		T = 1000; //For testing, the interval can be smaller
	}
	
	/**
	 * Getter for the list of objects to poll, not visible for the release version
	 * @return the list of objects to poll
	 */
	public List<IPollable> getPollList(){
		return PollList;
	}
	
	/**
	 * Gets the instance of the MockTimingManager
	 * @return the instance of the MockTimingManager
	 */
	public static MockTimingManager getInstance(){
		if (instance == null){
			instance = new MockTimingManager();
		}
		return instance;
	}
	
	/**
	 * Gets whether the timer has started or not
	 * @return timer has started?
	 */
	public boolean getStarted(){
		return started;
	}
	
}
