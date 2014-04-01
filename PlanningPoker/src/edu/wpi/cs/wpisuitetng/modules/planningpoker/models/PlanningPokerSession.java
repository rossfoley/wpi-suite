/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.planningpoker.models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.modules.AbstractModel;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;

/**
 * @author rossfoley
 *
 */
public class PlanningPokerSession extends AbstractModel {
	
	private String name;
	private GregorianCalendar endDate;
	private Set<Integer> requirementIDs;
	private UUID uuid = UUID.randomUUID();
	private boolean isOpen;
	private List<Requirement> requirements;

	/**
	 * Constructor for PlanningPokerSession
	 */
	public PlanningPokerSession () {
		this.name = "Planning Poker " + this.makeDefaultName();
		this.isOpen = false;
	}
	public UUID getID() {
		return uuid;
	}
	/**
	 * Add a requirement to existing planning poker session.
	 * @param requirementID
	 */
	public void addRequirement(int requirementID) {
		requirementIDs.add((Integer) requirementID);
	}
	public void setID(UUID iD) {
		uuid = iD;
	}

	/* (non-Javadoc)
	 * @see edu.wpi.cs.wpisuitetng.modules.Model#delete()
	 */
	@Override
	public void delete() {
		// TODO Auto-generated method stub

	}

	/**
	 * @return the endDate
	 */
	public GregorianCalendar getEndDate() {
		return endDate;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the requirementIDs
	 */
	public Set<Integer> getRequirementIDs() {
		return requirementIDs;
	}

	/* (non-Javadoc)
	 * @see edu.wpi.cs.wpisuitetng.modules.Model#identify(java.lang.Object)
	 */
	//@Override
	public Boolean identify(Object o) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isOpen() {
		return isOpen;
	}

	/**
	 * @return the initial ID
	 */
	public String makeDefaultName() {
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		return dateFormat.format(date);
	}

	/* (non-Javadoc)
	 * @see edu.wpi.cs.wpisuitetng.modules.Model#save()
	 */
	@Override
	public void save() {
		// TODO Auto-generated method stub
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(GregorianCalendar endDate) {
		this.endDate = endDate;
	}

	/**
	 * @param iD the iD to set
	 */


	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * If isOpen is true, the session is open;
	 * if it is false, it is closed.
	 * @param isOpen open or closed boolean to set
	 */
	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}

	/**
	 * @param requirementIDs the requirementIDs to set
	 */
	public void setRequirementIDs(Set<Integer> requirementIDs) {
		this.requirementIDs = requirementIDs;
	}

	/* (non-Javadoc)
	 * @see edu.wpi.cs.wpisuitetng.modules.Model#toJSON()
	 */
	@Override
	public String toJSON() {
		return new Gson().toJson(this, PlanningPokerSession.class);
	}
	
	public static PlanningPokerSession fromJson(String json) {
		final Gson parser = new Gson();
		return parser.fromJson(json, PlanningPokerSession.class);
	}
	
	/**
	 * Returns the Requirement with the given ID
	 * 
	 * @param id The ID number of the requirement to be returned
	
	 * @return the requirement for the id or null if the requirement is not found */
	public Requirement getRequirement(int id)
	{
		Requirement temp = null;
		// iterate through list of requirements until id is found
		for (int i=0; i < this.requirements.size(); i++){
			temp = requirements.get(i);
			if (temp.getId() == id){
				break;
			}
		}
		return temp;
	}
	
	/**
	 * Provides the number of elements in the list of requirements for the project. This
	 * function is called internally by the JList in NewRequirementPanel. Returns elements
	 * in reverse order, so the newest requirement is returned first.
	 * 	
	 * @return the number of requirements in the project * @see javax.swing.ListModel#getSize() * @see javax.swing.ListModel#getSize() * @see javax.swing.ListModel#getSize()
	 */
	public int requirementsGetSize() {
		return requirements.size();
	}
	
	/**
	 * Returns an array of PlanningPokerSession parsed from the given JSON-encoded
	 * string.
	 * 
	 * @param json
	 *            string containing a JSON-encoded array of PlanningPokerSession
	
	 * @return an array of PlanningPokerSession deserialized from the given JSON string */
	public static PlanningPokerSession[] fromJsonArray(String json) {
		final Gson parser = new Gson();
		return parser.fromJson(json, PlanningPokerSession[].class);
	}
}
