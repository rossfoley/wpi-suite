/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.planningpoker.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractListModel;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.controller.AddSessionController;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.ViewEventController;


public class PlanningPokerSessionModel extends AbstractListModel {

	private List<PlanningPokerSession> planningPokerSessions;
	private int nextID;
	private static PlanningPokerSessionModel instance;

	private PlanningPokerSessionModel() {
		planningPokerSessions = new ArrayList<PlanningPokerSession>();
		nextID = 0;
	}

	public static PlanningPokerSessionModel getInstance() {
		if (instance == null) {
			instance = new PlanningPokerSessionModel();
		}
		return instance;

	}

	/**
	 * Adds a single PlanningPokerSession to the PlanningPokerSessions of the project
	 * 
	 * @param newReq The PlanningPokerSession to be added to the list of PlanningPokerSessions in the project
	 */
	public void addPlanningPokerSession(PlanningPokerSession newSession){
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
	 * Returns the PlanningPokerSession with the given ID
	 * 
	 * @param id The ID number of the PlanningPokerSession to be returned

	 * @return the PlanningPokerSession for the id or null if the PlanningPokerSession is not found */
	public PlanningPokerSession getPlanningPokerSession(int id)
	{
		PlanningPokerSession temp = null;
		// iterate through list of planningPokerSessions until id is found
		for (int i=0; i < this.planningPokerSessions.size(); i++){
			temp = planningPokerSessions.get(i);
			if (temp.getID() == id){
				break;
			}
		}
		return temp;
	}
	/**
	 * Removes the PlanningPokerSession with the given ID
	 * 
	 * @param removeId The ID number of the PlanningPokerSession to be removed from the list of PlanningPokerSessions in the project
	 */
	public void removePlanningPokerSession(int removeId){
		// iterate through list of PlanningPokerSessions until id of project is found
		for (int i=0; i < this.planningPokerSessions.size(); i++){
			if (planningPokerSessions.get(i).getID() == removeId){
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



	 * @return the number of PlanningPokerSessions in the project * @see javax.swing.ListModel#getSize() * @see javax.swing.ListModel#getSize() * @see javax.swing.ListModel#getSize()
	 */
	public int getSize() {
		return planningPokerSessions.size();
	}

	/**
	 * 
	 * Provides the next ID number that should be used for a new PlanningPokerSession that is created.
	 * 

	 * @return the next open id number */
	public int getNextID()
	{

		return this.nextID++;
	}

	/**
	 * This function takes an index and finds the PlanningPokerSession in the list of PlanningPokerSessions
	 * for the project. Used internally by the JList in NewPlanningPokerSessionModel.
	 * 
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

	/**
	 * Adds the given array of planningpokersession to the list
	 * 
	 * @param planningpokersessions the array of PlanningPokerSessions to add
	 */
	public void addPlanningPokerSessions(PlanningPokerSession[] sessions) {
		for (int i = 0; i < sessions.length; i++) {
			this.planningPokerSessions.add(sessions[i]);
			if(sessions[i].getID() >= nextID) nextID = sessions[i].getID() + 1;
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



}
