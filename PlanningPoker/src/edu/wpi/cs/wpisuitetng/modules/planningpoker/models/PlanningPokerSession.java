
/*******************************************************************************
 * Copyright (c) 2014 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Team8s
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.planningpoker.models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.janeway.config.ConfigManager;
import edu.wpi.cs.wpisuitetng.modules.AbstractModel;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.controller.GetRequirementsController;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.gui.CreatePokerSessionErrors;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.gui.NoDescriptionException;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.RequirementModel;

/**
 * @author rossfoley
*  @author amandaadkins 
 *
 */
public class PlanningPokerSession extends AbstractModel {
	
	private String name;
	private GregorianCalendar endDate;
	private Set<Integer> requirementIDs;
	private UUID uuid = UUID.randomUUID();
	private boolean isOpen;
	private List<Requirement> requirements;
	private List<Estimate> estimates;
	private boolean isUsingDeck;

	private String description;
	private String sessionCreatorName;
	private Deck sessionDeck;
	
	/**
	 * @return the sessionCreatorID
	 */
	public String getSessionCreatorName() {
		return sessionCreatorName;
	}
	/**
	 * @param sessionCreatorID the sessionCreatorID to set
	 */
	public void setSessionCreatorName(String sessionCreatorName) {
		this.sessionCreatorName = sessionCreatorName;
	}
	/**
	 * @return the isUsingDeck
	 */
	public boolean isUsingDeck() {
		return isUsingDeck;
	}
	/**
	 * @param isUsingDeck the isUsingDeck to set
	 */
	public void setUsingDeck(boolean isUsingDeck) {
		this.isUsingDeck = isUsingDeck;
	}
	/**
	 * Constructor for PlanningPokerSession
	 */
	public PlanningPokerSession () {
		this.name = "Planning Poker " + this.makeDefaultName();
		this.isOpen = false;
		this.requirements = new ArrayList<Requirement>();
		this.estimates = new ArrayList<Estimate>();
	}
	/**
	 * @return uuid
	 */
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
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description.trim();
	}
	
	/**
	 * @return the endDate
	 */
	public GregorianCalendar getEndDate() {
		return endDate;
	}
	
	/**
	 * @return if the session has an endDate
	 */
	public boolean hasEndDate() {
		return (endDate != null);
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

	/**
	 * @return a boolean indicating if the session is open
	 */
	public boolean isOpen() {
		return isOpen;
	}

	/**
	 * Creates the default name for the session.
	 * 
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
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name.trim();
	}

	/**
	 * If isOpen is true, the session is open;
	 * if it is false, it is closed.
	 * 
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
	/**
	 * validates the fields of a planning poker session
	 * @param haveEndDate if true, the user is specifying an end date, if false, no end date (null)
	 * @param dateHasBeenSet if true, the user has set an end date, if false, they haven't touched the date picker and it's still the default date
	 * @return List<CreatePokerSessionErrors> which is a list of that type of enum 
	 * which correspond to the possible errors with the different fields
	 */
	public ArrayList<CreatePokerSessionErrors> validateFields(boolean haveEndDate, boolean dateHasBeenSet) {
		ArrayList<CreatePokerSessionErrors> errors = new ArrayList<CreatePokerSessionErrors>();
		GregorianCalendar currentDate = new GregorianCalendar();
		
		if (haveEndDate) {
			if (dateHasBeenSet) {
				if (this.endDate.before(currentDate)) {
					errors.add(CreatePokerSessionErrors.EndDateTooEarly);
				}
			}
			else {
				errors.add(CreatePokerSessionErrors.NoDateSelected);
			}
		}
		
		// Description validation
		if (this.description.equals("")){
			errors.add(CreatePokerSessionErrors.NoDescription);
		}
		
		// Name validation
		if (this.name.equals("")){
			errors.add(CreatePokerSessionErrors.NoName);
		}

		// Check if other fields are in appropriate range
		return errors;
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
	 * @return the requirement for the id or null if the requirement is not found 
	 */
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
	 * @return the sessionDeck
	 */
	public Deck getSessionDeck() {
		return sessionDeck;
	}
	/**
	 * @param sessionDeck the sessionDeck to set
	 */
	public void setSessionDeck(Deck sessionDeck) {
		this.sessionDeck = sessionDeck;
	}
	/**
	 * Returns an array of PlanningPokerSession parsed from the given JSON-encoded
	 * string.
	 * 
	 * @param json
	 *            string containing a JSON-encoded array of PlanningPokerSession
	 * @return an array of PlanningPokerSession deserialized from the given JSON string 
	 */
	public static PlanningPokerSession[] fromJsonArray(String json) {
		final Gson parser = new Gson();
		return parser.fromJson(json, PlanningPokerSession[].class);
	}
	
	public List<Requirement> getRequirements() {
		return this.requirements;
	}
	
	/**
	 * @param selected the list of requirements to add to the session
	 */
	public void setRequirements(List<Requirement> selected) {
		this.requirements = selected;
	}
	/**
	 * @return the estimates
	 */
	public List<Estimate> getEstimates() {
		return estimates;
	}
	/**
	 * @param estimates the estimates to set
	 */
	public void setEstimates(List<Estimate> estimates) {
		this.estimates = estimates;
	}
	
	/**
	 * @param estimate the estimate to add to the session
	 */
	public void addEstimate(Estimate estimate) {
		this.estimates.add(estimate);
	}
	
	
	/**
	 * @return true if the session is allowed to be edited
	 */
	public boolean isEditable() {
		return (!this.isOpen || (this.estimates.size() == 0)) && this.sessionCreatorName.equals(ConfigManager.getConfig().getUserName());
	}
	
	/**
	 * @return the uuid
	 */
	public UUID getUuid() {
		return uuid;
	}
	/**
	 * @param uuid the uuid to set
	 */
	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}
	/**
	 * Copies all of the values from the given planning poker session to this planning poker session.
	 * 
	 * @param toCopyFrom
	 *            the planning poker session to copy from.
	 */
	public void copyFrom(PlanningPokerSession toCopyFrom) {
		this.description = toCopyFrom.description;
		this.name = toCopyFrom.name;
		this.endDate = toCopyFrom.endDate;
		this.requirementIDs = toCopyFrom.requirementIDs;
		this.isOpen = toCopyFrom.isOpen;
		this.requirements = toCopyFrom.requirements;
		this.estimates = toCopyFrom.estimates;
		this.isUsingDeck = toCopyFrom.isUsingDeck;
		this.sessionCreatorName = toCopyFrom.sessionCreatorName;
		this.sessionDeck = toCopyFrom.sessionDeck;
	}
}
