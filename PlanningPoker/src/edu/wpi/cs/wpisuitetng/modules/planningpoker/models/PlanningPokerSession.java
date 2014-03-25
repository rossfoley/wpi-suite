/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.planningpoker.models;

import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;

import edu.wpi.cs.wpisuitetng.modules.AbstractModel;

/**
 * @author rossfoley
 *
 */
public class PlanningPokerSession extends AbstractModel {
	private String name;
	private GregorianCalendar endDate;
	private Set<Integer> requirementIDs;
	
	public Set<Integer> getRequirementIDs() {
		return requirementIDs;
	}

	public void setRequirementIDs(Set<Integer> requirementIDs) {
		this.requirementIDs = requirementIDs;
	}

	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public GregorianCalendar getEndDate() {
		return endDate;
	}

	public void setEndDate(GregorianCalendar endDate) {
		this.endDate = endDate;
	}

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

}
