/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.planningpoker.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import javax.swing.AbstractListModel;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.controller.AddSessionController;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.controller.CheckForUpdatesController;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.controller.UpdateEstimateController;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.controller.UpdatePlanningPokerSessionController;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.ViewEventController;

public class PlanningPokerSessionModel extends AbstractListModel {

	private List<PlanningPokerSession> planningPokerSessions;
	private static PlanningPokerSessionModel instance;
	private boolean updateStarted = false;

	private PlanningPokerSessionModel() {
		planningPokerSessions = new ArrayList<PlanningPokerSession>();
	}

	public static PlanningPokerSessionModel getInstance() {
		if (instance == null) {
			instance = new PlanningPokerSessionModel();
		}
		return instance;

	}
	
	public void startLiveUpdating() {
		if (!updateStarted) {
			updateStarted = true;
			Thread t = new Thread(new Runnable() {
				public void run() {
					Timer timer = new Timer();
					timer.scheduleAtFixedRate(new TimerTask() {
						@Override
						public void run() {
							CheckForUpdatesController.getInstance().checkForUpdates();							
						}
					}, 0, 10000);
				}
			});
			t.setDaemon(true);
			t.run();
		}
	}

	/**
	 * Adds a single PlanningPokerSession to the PlanningPokerSessions of the project
	 * 
	 * @param newReq The PlanningPokerSession to be added to the list of PlanningPokerSessions in the project
	 */
	public void addPlanningPokerSession(PlanningPokerSession newSession) {
		// add the PlanningPokerSession
		planningPokerSessions.add(newSession);
		try 
		{
			AddSessionController.getInstance().addPlanningPokerSession(newSession);
		}
		catch(Exception e)
		{

		}
	}
	
	/**
	 * Adds a single PlanningPokerSession to the PlanningPokerSessions of the project
	 * 
	 * @param newReq The PlanningPokerSession to be added to the list of PlanningPokerSessions in the project
	 */
	public void addCachedPlanningPokerSession(PlanningPokerSession newSession) {
		planningPokerSessions.add(newSession);
	}
	
	/**
	 * Adds a single PlanningPokerSession to the PlanningPokerSessions of the project
	 * 
	 * @param newReq The PlanningPokerSession to be added to the list of PlanningPokerSessions in the project
	 */
	public void updatePlanningPokerSession(PlanningPokerSession currentSession){
		// add the PlanningPokerSession
		removePlanningPokerSession(currentSession.getID());
		planningPokerSessions.add(currentSession);
		try 
		{
			UpdatePlanningPokerSessionController.getInstance().updatePlanningPokerSession(currentSession);
		}
		catch(Exception e)
		{

		}
	}
	
	/**
	 * Returns the PlanningPokerSession with the given ID
	 * 
	 * @param id The ID number of the PlanningPokerSession to be returned

	 * @return the PlanningPokerSession for the id or null if the PlanningPokerSession is not found */
	public PlanningPokerSession getPlanningPokerSession(UUID id)
	{
		PlanningPokerSession temp = null;
		// iterate through list of planningPokerSessions until id is found
		for (int i=0; i < this.planningPokerSessions.size(); i++){
			temp = planningPokerSessions.get(i);
			if (temp.getID().equals(id)){
				break;
			}
		}
		return temp;
	}
	
	public PlanningPokerSession getPlanningPokerSession(String sessionName) {
		PlanningPokerSession temp = null;
		// iterate through list of planningPokerSessions until id is found
		for (int i=0; i < this.planningPokerSessions.size(); i++){
			temp = planningPokerSessions.get(i);
			if (temp.getName().equals(sessionName)) {
				return temp;
			}
		}
		return null;
	}
	/**
	 * Removes the PlanningPokerSession with the given ID
	 * 
	 * @param removeId The ID number of the PlanningPokerSession to be removed from the list of PlanningPokerSessions in the project
	 */
	public void removePlanningPokerSession(UUID removeId){
		// iterate through list of PlanningPokerSessions until id of project is found
		for (int i=0; i < this.planningPokerSessions.size(); i++){
			if (planningPokerSessions.get(i).getID().equals(removeId)) {
				// remove the id
				planningPokerSessions.remove(i);
				break;
			}
		}
	}

	/**
	 * Provides the number of elements in the list of PlanningPokerSessions for the project. This
	 * function is called internally by the JList in NewPlanningPokerSessionPanel. Returns elements
	 * in reverse order, so the newest PlanningPokerSession is returned first.
	 * 
	 * @return the number of requirements in the project 
	 * @see javax.swing.ListModel#getSize() 
	 * @see javax.swing.ListModel#getSize() 
	 * @see javax.swing.ListModel#getSize()
	 */
	public int getSize() {
		return planningPokerSessions.size();
	}

	/**
	 * This function takes an index and finds the PlanningPokerSession in the list of PlanningPokerSessions
	 * for the project. Used internally by the JList in NewPlanningPokerSessionModel.
	 * @param index The index of the PlanningPokerSession to be returned
	 * @return the PlanningPokerSession associated with the provided index * @see javax.swing.ListModel#getElementAt(int) * @see javax.swing.ListModel#getElementAt(int) * @see javax.swing.ListModel#getElementAt(int)
	 */
	public PlanningPokerSession getElementAt(int index) {
		return planningPokerSessions.get(planningPokerSessions.size() - 1 - index);
	}

	/**
	 * Removes all PlanningPokerSessions from this model
	 * 
	 * NOTE: One cannot simply construct a new instance of
	 * the model, because other classes in this module have
	 * references to it. Hence, we manually remove each PlanningPokerSession
	 * from the model.
	 */
	public void emptyModel() {
		int oldSize = getSize();
		Iterator<PlanningPokerSession> iterator = planningPokerSessions.iterator();
		while (iterator.hasNext()) {
			iterator.next();
			iterator.remove();
		}
		this.fireIntervalRemoved(this, 0, Math.max(oldSize - 1, 0));
	}

	public void addEstimateToPlanningPokerSession(Estimate estimate) {
		PlanningPokerSession session = getPlanningPokerSession(estimate.getSessionID());
		session.addEstimate(estimate);
		removePlanningPokerSession(session.getID());
		planningPokerSessions.add(session);
		try {
			UpdateEstimateController.getInstance().updateEstimate(estimate);
		} catch (Exception e) {}
	}
	
	/**
	 * Adds the given array of planningpokersession to the list
	 * 
	 * @param planningpokersessions the array of PlanningPokerSessions to add
	 */
	public void addPlanningPokerSessions(PlanningPokerSession[] sessions) {
		for (int i = 0; i < sessions.length; i++) {
			this.planningPokerSessions.add(sessions[i]);
		}
		this.fireIntervalAdded(this, 0, Math.max(getSize() - 1, 0));
		ViewEventController.getInstance().refreshTable();
		//ViewEventController.getInstance().refreshTree();
		// Update the UI to reflect the list of sessions
	}

	/**
	 * Returns the list of the planning poker sessions

	 * @return the planning poker sessions held within the PlanningPokerSessionModel. */
	public List<PlanningPokerSession> getPlanningPokerSessions() {
		return planningPokerSessions;
	}

	
//	/**
//	 * Returns the Requirement with the given ID
//	 * 
//	 * @param id The ID number of the requirement to be returned
//	
//	 * @return the requirement for the id or null if the requirement is not found */
//	public Requirement getRequirement(int id)
//	{
//		
//	}
//	
//	/**
//	 * Provides the number of elements in the list of requirements for the project. This
//	 * function is called internally by the JList in NewRequirementPanel. Returns elements
//	 * in reverse order, so the newest requirement is returned first.
//	 * 	
//	 * @return the number of requirements in the project * @see javax.swing.ListModel#getSize() * @see javax.swing.ListModel#getSize() * @see javax.swing.ListModel#getSize()
//	 */
//	// need to rename part of this function to work in this package
//	public int requirementsGetSize() {
//		return ;
//	}


}
