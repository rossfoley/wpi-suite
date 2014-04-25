package edu.wpi.cs.wpisuitetng.modules.planningpoker.timingmanager;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.controller.CheckForUpdatesController;

public class TimingManager {

	private static TimingManager instance = new TimingManager();
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
	
	public long getTimerInterval(){
		return T;
	}
	
}
