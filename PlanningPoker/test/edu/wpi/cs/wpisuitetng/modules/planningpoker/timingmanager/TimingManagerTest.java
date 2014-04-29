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

import java.lang.reflect.Field;

import org.junit.Before;
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
	 * Resets the Mock Timing Manager before every test
	 */
	@Before
	public void resetTimingManager()throws SecurityException,
    NoSuchFieldException, IllegalArgumentException,
    IllegalAccessException {
			Field instance = MockTimingManager.class.getDeclaredField("instance");
			instance.setAccessible(true);
			instance.set(null, null);
		
	}
	
	/**
	 * Tests basic polling functionality
	 */
	@Test
	public void TimerCallTest(){
		
		assertFalse(MockTimingManager.getInstance().getStarted());
		
		final mockPollable mock1 = new mockPollable();
		
		MockTimingManager.getInstance().addPollable(mock1);
		
		assertEquals(0, mock1.getNumTimesPolled()); //mock1.pollFunction has not been called

		MockTimingManager.getInstance().start();

		try {
			Thread.sleep(MockTimingManager.getInstance().getTimerInterval()*2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//After at least the interval of the timer, has mock1.pollFunction been called
		assertTrue(mock1.getNumTimesPolled() > 0);
		
	}
	
	/**
	 * Tests removing of polling objects from the timing manager
	 */
	@Test
	public void TimerRemoveTest(){
		
		assertFalse(MockTimingManager.getInstance().getStarted());
		
		final mockPollable mock1 = new mockPollable();
		final mockPollable mock2 = new mockPollable();
		
		MockTimingManager.getInstance().addPollable(mock1);
		MockTimingManager.getInstance().addPollable(mock2);
		assertEquals(0, mock1.getNumTimesPolled());
		assertEquals(0, mock2.getNumTimesPolled());
		
		assertEquals(2, MockTimingManager.getInstance().getPollList().size());
		
		MockTimingManager.getInstance().start();

		try {
			Thread.sleep(MockTimingManager.getInstance().getTimerInterval()*2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		//After the interval of the timer, has mock1.pollFunction been called
		assertTrue(mock1.getNumTimesPolled() > 0);
		assertTrue(mock2.getNumTimesPolled() > 0);
		
		MockTimingManager.getInstance().removePollable(mock2);
		
		assertEquals(1, MockTimingManager.getInstance().getPollList().size());

		try {
			Thread.sleep(MockTimingManager.getInstance().getTimerInterval()*2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//After the interval of the timer, has mock2.pollFunction() not been called
		assertTrue(mock1.getNumTimesPolled() > mock2.getNumTimesPolled());
		
	}
	
	/**
	 * Tests trying to add a single object multiple times
	 */
	@Test
	public void TimerDuplicateTest(){
		
		assertFalse(MockTimingManager.getInstance().getStarted());
		
		final mockPollable mock1 = new mockPollable();

		MockTimingManager.getInstance().addPollable(mock1);
		MockTimingManager.getInstance().addPollable(mock1);
		MockTimingManager.getInstance().addPollable(mock1);
		MockTimingManager.getInstance().addPollable(mock1);

		assertEquals(1, MockTimingManager.getInstance().getPollList().size());
	}
	
	/**
	 * Tests trying to add a null object
	 */
	@Test
	public void TimerNullTest(){
		
		assertFalse(MockTimingManager.getInstance().getStarted());
		
		mockPollable mock1 = null;
		
		MockTimingManager.getInstance().addPollable(mock1);		

		MockTimingManager.getInstance().start();
		
		try {
			Thread.sleep(MockTimingManager.getInstance().getTimerInterval()*2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		//At this point, the null object should have been cleared from the list
		assertEquals(0, MockTimingManager.getInstance().getPollList().size());
	}
	
	/**
	 * Tests the actual Timing Manager's getInstance() function
	 * Is just a check for exceptions when creating the singleton object
	 */
	@Test //DO NOT USE THIS AS A TEMPLATE FOR FURTHER TESTS
	public void ActualTimerGetInstance(){
		TimingManager.getInstance();
	}
}
