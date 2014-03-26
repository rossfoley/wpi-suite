/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.planningpoker.models;

import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.modules.AbstractModel;

/**
 * @author rossfoley
 *
 */
public class PlanningPokerSession extends AbstractModel {
	private String name;
	private GregorianCalendar endDate;
	private Set<Integer> requirementIDs;
	private int ID;
	

	/**
	 * @return the ID
	 */
	public int getID() {
		return ID;
	}

	/**
	 * @param iD the iD to set
	 */
	public void setID(int iD) {
		ID = iD;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the endDate
	 */
	public GregorianCalendar getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(GregorianCalendar endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the requirementIDs
	 */
	public Set<Integer> getRequirementIDs() {
		return requirementIDs;
	}

	/**
	 * @param requirementIDs the requirementIDs to set
	 */
	public void setRequirementIDs(Set<Integer> requirementIDs) {
		this.requirementIDs = requirementIDs;
	}

	/**
	 * Add a requirement to existing planning poker session.
	 * @param requirementID
	 */
	public void addRequirement(int requirementID) {
		requirementIDs.add((Integer) requirementID);
	}

	/* (non-Javadoc)
	 * @see edu.wpi.cs.wpisuitetng.modules.Model#save()
	 */
	@Override
	public void save() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see edu.wpi.cs.wpisuitetng.modules.Model#delete()
	 */
	@Override
	public void delete() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see edu.wpi.cs.wpisuitetng.modules.Model#toJSON()
	 */
	@Override
	public String toJSON() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.wpi.cs.wpisuitetng.modules.Model#identify(java.lang.Object)
	 */
	@Override
	public Boolean identify(Object o) {
		// TODO Auto-generated method stub
		return null;
	}

	public static PlanningPokerSession fromJson(String json) {
		final Gson parser = new Gson();
		return parser.fromJson(json, PlanningPokerSession.class);
	}

}
