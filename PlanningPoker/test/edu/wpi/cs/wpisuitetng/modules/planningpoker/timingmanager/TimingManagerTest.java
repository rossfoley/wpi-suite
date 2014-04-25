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

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Test Class for TimingManager
 * @author Perry
 * @version 1
 */
public class TimingManagerTest {
	
	/**
	 * MockObject for IPollable
	 * @author Perry
	 *
	 */
	class mockPollable implements IPollable{

		int numTimesPolled;
		
		private mockPollable() {
			numTimesPolled = 0;
		}
		
		@Override
		public void pollFunction() {
			numTimesPolled +=1;
		}

		public int getNumTimesPolled() {
			return numTimesPolled;
		}

		public void setNumTimesPolled(int numTimesPolled) {
			this.numTimesPolled = numTimesPolled;
		}
		
	}
	
	/**
	 * Tests basic polling functionality
	 */
	@Test
	public void TimerCallTest(){
		
		final mockPollable mock1 = new mockPollable();
		
		TimingManager.getInstance().addPollable(mock1);
		assertTrue(mock1.getNumTimesPolled() == 0); //mock1.pollFunction has not been called

		try {
			Thread.sleep(TimingManager.getInstance().getTimerInterval());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//After the interval of the timer, has mock1.pollFunction been called
		assertTrue(mock1.getNumTimesPolled() > 0);

		try {
			Thread.sleep(TimingManager.getInstance().getTimerInterval());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	
		//After the interval of the timer, has mock1.pollFunction been called
		assertTrue(mock1.getNumTimesPolled() > 1);
		
	}
	
	/**
	 * Tests removing of polling objects from the timing manager
	 */
	@Test
	public void TimerRemoveTest(){
		
		final mockPollable mock1 = new mockPollable();
		final mockPollable mock2 = new mockPollable();
		
		TimingManager.getInstance().addPollable(mock1);
		assertTrue(mock1.getNumTimesPolled() == 0);
		TimingManager.getInstance().addPollable(mock2);
		assertTrue(mock2.getNumTimesPolled() == 0);

		try {
			Thread.sleep(TimingManager.getInstance().getTimerInterval());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		//After the interval of the timer, has mock1.pollFunction been called
		assertTrue(mock1.getNumTimesPolled() > 0);
		assertTrue(mock2.getNumTimesPolled() > 0);
		
		TimingManager.getInstance().removePollable(mock2);

		try {
			Thread.sleep(TimingManager.getInstance().getTimerInterval());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//After the interval of the timer, has mock2.pollFunction() not been called
		assertTrue(mock1.getNumTimesPolled() > mock2.getNumTimesPolled());
		
	}
	
}
