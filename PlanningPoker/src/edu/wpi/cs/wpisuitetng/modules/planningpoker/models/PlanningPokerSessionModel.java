/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.planningpoker.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractListModel;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.controller.AddSessionController;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;


/**
 * @author Andrew Leonard
 *
 */
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
	 * Adds a single requirement to the requirements of the project
	 * 
	 * @param newReq The requirement to be added to the list of requirements in the project
	 */
	public void addPlanningPokerSession(PlanningPokerSession newSession){
		// add the requirement
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
	 * @param id The ID number of the requirement to be returned

	 * @return the requirement for the id or null if the requirement is not found */
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
	 * Removes the requirement with the given ID
	 * 
	 * @param removeId The ID number of the requirement to be removed from the list of requirements in the project
	 */
	public void removePlanningPokerSession(int removeId){
		// iterate through list of requirements until id of project is found
		for (int i=0; i < this.planningPokerSessions.size(); i++){
			if (planningPokerSessions.get(i).getID() == removeId){
				// remove the id
				planningPokerSessions.remove(i);
				break;
			}
		}
	}

	/**
	 * Provides the number of elements in the list of requirements for the project. This
	 * function is called internally by the JList in NewPlanningPokerSessionPanel. Returns elements
	 * in reverse order, so the newest requirement is returned first.
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
	 * 
	 * Provides the next ID number that should be used for a new requirement that is created.
	 * 

	 * @return the next open id number */
	public int getNextID()
	{

		return this.nextID++;
	}

	/**
	 * This function takes an index and finds the requirement in the list of requirements
	 * for the project. Used internally by the JList in NewPlanningPokerSessionModel.
	 * @param index The index of the requirement to be returned
	 * @return the requirement associated with the provided index * @see javax.swing.ListModel#getElementAt(int) * @see javax.swing.ListModel#getElementAt(int) * @see javax.swing.ListModel#getElementAt(int)
	 */
	public PlanningPokerSession getElementAt(int index) {
		return planningPokerSessions.get(planningPokerSessions.size() - 1 - index);
	}

	/**
	 * Removes all requirements from this model
	 * 
	 * NOTE: One cannot simply construct a new instance of
	 * the model, because other classes in this module have
	 * references to it. Hence, we manually remove each requirement
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

	//	/**
	//	 * Adds the given array of requirements to the list
	//	 * 
	//	 * @param requirements the array of requirements to add
	//	 */
	//	public void addPlanningPokerSessions(PlanningPokerSession[] requirements) {
	//		for (int i = 0; i < requirements.length; i++) {
	//			this.requirements.add(requirements[i]);
	//			if(requirements[i].getId() >= nextID) nextID = requirements[i].getId() + 1;
	//		}
	//		this.fireIntervalAdded(this, 0, Math.max(getSize() - 1, 0));
	//		ViewEventController.getInstance().refreshTable();
	//		ViewEventController.getInstance().refreshTree();
	//	}

	/**
	 * Returns the list of the requirements

	 * @return the requirements held within the requirementmodel. */
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
