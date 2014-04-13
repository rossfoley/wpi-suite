package edu.wpi.cs.wpisuitetng.modules.planningpoker.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.swing.AbstractListModel;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.controller.AddSessionController;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.controller.UpdatePlanningPokerSessionController;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.ViewEventController;

public class EstimateModel extends AbstractListModel {
	
	private List<Estimate> estimates;
	private static EstimateModel instance;

	private EstimateModel() {
		estimates = new ArrayList<Estimate>();
	}
	
	public static EstimateModel getInstance() {
		if(instance == null){
			instance = new EstimateModel();
		}
		return instance;
	}
	
	/**
	 * Adds a single Estimate to the Estimates of the project
	 * 
	 * @param newEstimate The Estimate to be added to the list of Estimates in the project
	 */
	public void addEstimate(Estimate newEstimate){
		// add the Estimate
		estimates.add(newEstimate);
		try 
		{
			//AddEstimateController.getInstance().addEstimate(newEstimate);
		}
		catch(Exception e)
		{

		}
	}
	
	/**
	 * Updates a single Estimate from the Estimates of the project
	 * 
	 * @param currentEstimate The Estimate to be added to the list of Estimates in the project
	 */
	public void updateEstimate(Estimate currentSession){
		// add the Estimate
		removeEstimate(currentSession.getID());
		estimates.add(currentSession);
		try 
		{
			//UpdateEstimateController.getInstance().updateEstimate(currentSession);
		}
		catch(Exception e)
		{

		}
	}
	
	/**
	 * Returns the Estimate with the given ID
	 * 
	 * @param id The ID number of the Estimate to be returned

	 * @return the Estimate for the id or null if the Estimate is not found */
	public Estimate getEstimate(UUID id)
	{
		Estimate temp = null;
		// iterate through list of Estimates until id is found
		for (int i=0; i < this.estimates.size(); i++){
			temp = estimates.get(i);
			if (temp.getID().equals(id)){
				break;
			}
		}
		return temp;
	}
	public Estimate getEstimate(String ownerName) {
		Estimate temp = null;
		// iterate through list of Estimates until id is found
		for (int i=0; i < this.estimates.size(); i++){
			temp = estimates.get(i);
			if (temp.getOwnerName().equals(ownerName)) {
				return temp;
			}
		}
		return null;
	}
	/**
	 * Removes the Estimate with the given ID
	 * 
	 * @param removeId The ID number of the Estimate to be removed from the list of Estimates in the project
	 */
	public void removeEstimate(UUID removeId){
		// iterate through list of Estimates until id of project is found
		for (int i=0; i < this.estimates.size(); i++){
			if (estimates.get(i).getID().equals(removeId)) {
				// remove the id
				estimates.remove(i);
				break;
			}
		}
	}

	/**
	 * Provides the number of elements in the list of Estimates for the project. This
	 * function is called internally by the JList in NewEstimatePanel. Returns elements
	 * in reverse order, so the newest Estimate is returned first.
	 * 
	 * @return the number of estimates in the project 
	 * @see javax.swing.ListModel#getSize() 
	 * @see javax.swing.ListModel#getSize() 
	 * @see javax.swing.ListModel#getSize()
	 */
	public int getSize() {
		return estimates.size();
	}

	/**
	 * This function takes an index and finds the Estimate in the list of Estimates
	 * for the project. Used internally by the JList in NewEstimateModel.
	 * @param index The index of the Estimate to be returned
	 * @return the Estimate associated with the provided index * @see javax.swing.ListModel#getElementAt(int) * @see javax.swing.ListModel#getElementAt(int) * @see javax.swing.ListModel#getElementAt(int)
	 */
	public Estimate getElementAt(int index) {
		return estimates.get(estimates.size() - 1 - index);
	}

	/**
	 * Removes all Estimates from this model
	 * 
	 * NOTE: One cannot simply construct a new instance of
	 * the model, because other classes in this module have
	 * references to it. Hence, we manually remove each Estimate
	 * from the model.
	 */
	public void emptyModel() {
		int oldSize = getSize();
		Iterator<Estimate> iterator = estimates.iterator();
		while (iterator.hasNext()) {
			iterator.next();
			iterator.remove();
		}
		this.fireIntervalRemoved(this, 0, Math.max(oldSize - 1, 0));
	}

	/**
	 * Adds the given array of estimates to the list
	 * 
	 * @param estimatesToAdd the array of Estimates to add
	 */
	public void addEstimates(Estimate[] estimatesToAdd) {
		for (int i = 0; i < estimatesToAdd.length; i++) {
			this.estimates.add(estimatesToAdd[i]);
		}
		this.fireIntervalAdded(this, 0, Math.max(getSize() - 1, 0));
		ViewEventController.getInstance().refreshTable();
	}

	/**
	 * Returns the list of the estimates

	 * @return the estimates held within the EstimateModel. */
	public List<Estimate> getEstimates() {
		return estimates;
	}
}
