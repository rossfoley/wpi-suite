package edu.wpi.cs.wpisuitetng.modules.planningpoker.timingmanager;

import static org.junit.Assert.*;

import org.junit.Test;


public class TimingManagerTest {
	
	
	class mockPollable implements IPollable{

		int numTimesPolled;
		
		public mockPollable() {
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
	
	@Test
	public void TimerCallTest(){
		
		mockPollable mock1 = new mockPollable();
		assertTrue(mock1.getNumTimesPolled() == 0);
		
		TimingManager.getInstance().addPollable(mock1);

		try {
			Thread.sleep(TimingManager.getInstance().getTimerInterval());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		assertTrue(mock1.getNumTimesPolled() > 0);

		try {
			Thread.sleep(TimingManager.getInstance().getTimerInterval());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		assertTrue(mock1.getNumTimesPolled() > 1);
		
	}
	
	@Test
	public void TimerRemoveTest(){
		
		mockPollable mock1 = new mockPollable();
		assertTrue(mock1.getNumTimesPolled() == 0);
		mockPollable mock2 = new mockPollable();
		assertTrue(mock2.getNumTimesPolled() == 0);
		
		TimingManager.getInstance().addPollable(mock1);
		TimingManager.getInstance().addPollable(mock2);

		try {
			Thread.sleep(TimingManager.getInstance().getTimerInterval());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		assertTrue(mock1.getNumTimesPolled() > 0);
		assertTrue(mock2.getNumTimesPolled() > 0);
		
		TimingManager.getInstance().removePollable(mock2);

		try {
			Thread.sleep(TimingManager.getInstance().getTimerInterval());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		assertTrue(mock1.getNumTimesPolled() > mock2.getNumTimesPolled());
		
	}
	
}
