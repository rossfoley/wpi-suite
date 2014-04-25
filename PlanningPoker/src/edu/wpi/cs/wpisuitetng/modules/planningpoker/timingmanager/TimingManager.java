/*******************************************************************************
 * Copyright (c) 2012 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Perry Franklin
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.planningpoker.timingmanager;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Singleton class that handles objects that require regular polling
 * In essence, abstracts Timers so that only one timer needs to be created
 * @author Perry
 * @version 1
 *
 */
public class TimingManager {

	private static final TimingManager instance = new TimingManager();
	private final List<IPollable> PollList;
	private final long T = 5000;
	
	/**
	 * Constructor for the singleton class TimingManager
	 */
	private TimingManager(){
		PollList = new ArrayList<IPollable>();
		
		final Thread t = new Thread(new Runnable() {
			public void run() {
				final Timer timer = new Timer();
				timer.scheduleAtFixedRate(new TimerTask() {
					@Override
					public void run() {
						timedFunc();
					}
				}, 0, T);
			}
		});
		t.setDaemon(true);
		t.start();
	}
	
	/**
	 * Returns the instance of the TimingManager
	 * @return the instance of the TimingManager
	 */
	public static TimingManager getInstance() {
		return instance;
	}
	
	/**
	 * Add an object to the list of objects that need polling
	 * @param p object that needs polling
	 */
	public void addPollable(IPollable p){
		if (!PollList.contains(p)){
			PollList.add(p);
		}
	}
	
	/**
	 * Removes an object from the list of objects that need polling
	 * @param p object that will be removed from the list
	 */
	public void removePollable(IPollable p){
		PollList.remove(p);
	}
	
	/**
	 * Function that is called every T seconds
	 * Cycles through the list of objects that need polling and calls pollFunction()
	 */
	private void timedFunc(){
		for(IPollable p: PollList){
			
			if(p != null){
				p.pollFunction();
			} else {
				removePollable(p);
			}
		}
	}
	
	/**
	 * gets the current timer interval (which is final)
	 * @return the timer interval
	 */
	public long getTimerInterval(){
		return T;
	}
	
}
