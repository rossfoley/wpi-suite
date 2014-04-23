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
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.janeway.config.ConfigManager;
import edu.wpi.cs.wpisuitetng.modules.AbstractModel;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.gui.CreatePokerSessionErrors;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.RequirementModel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.gui.NoDescriptionException;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.statistics.RequirementEstimateStats;

/**
 * @author rossfoley
 *
 */
public class PlanningPokerSession extends AbstractModel {

	private String name;
	private GregorianCalendar endDate;
	private Set<Integer> requirementIDs;
	private UUID uuid = UUID.randomUUID();
	public enum SessionState {
		OPEN, PENDING, VOTINGENDED, CLOSED, 
	}
	private SessionState gameState;
	private List<Estimate> estimates;
	private boolean isUsingDeck;

	private String description;
	private String sessionCreatorName;
	private Deck sessionDeck;
	private final String defaultSessionName;
	private Set<Integer> reqsWithCompleteEstimates;
	private Map<Requirement, Integer> finalEstimatesMap;
	private List<Integer> requirementsWithExportedEstimates;

	private List<Integer> requirementsWithExportedEstimatesIDs;
	private final HashMap<Integer, RequirementEstimateStats> reqEstimateStats;
	private List<String> VoterNameList;
	private List<EstimateVoters> estimateVoterList;
	
	/**
	 * Constructor for PlanningPokerSession
	 */
	public PlanningPokerSession () {
		name = "Planning Poker " + this.makeDefaultName();
		gameState = PlanningPokerSession.SessionState.PENDING;
		requirementIDs = new HashSet<Integer>();
		estimates = new ArrayList<Estimate>();
		reqsWithCompleteEstimates = new HashSet<Integer>();
		reqEstimateStats = new HashMap<Integer, RequirementEstimateStats>();
		requirementsWithExportedEstimates = new ArrayList<Integer>();
		requirementsWithExportedEstimatesIDs = new ArrayList<Integer>();
		finalEstimatesMap = new HashMap<Requirement, Integer>(); 
		defaultSessionName = new String(name.toString());
		finalEstimatesMap = new HashMap<Requirement, Integer>();
		this.setVoterNameList(new ArrayList<String>());
		estimateVoterList = new ArrayList<EstimateVoters>();
	}
	//private int[] finalEstimates;

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
	 * @return the requirementsWithExportedEstimates
	 */
	public List<Integer> getRequirementsWithExportedEstimates() {
		return requirementsWithExportedEstimatesIDs;
	}
	
	/**
	 * @param requirementsWithExportedEstimates the requirementsWithExportedEstimates to set
	 */
	public void setRequirementsWithExportedEstimates(ArrayList<Integer> requirementsWithExportedEstimates) {
		this.requirementsWithExportedEstimatesIDs = requirementsWithExportedEstimates;
	}
	
	/**
	 * @param idToAdd idOfTheRequirementToAddToExportedEstimates
	 */
	public void addIDToSetRequirementsWithExportedEstimates(int idToAdd){
		requirementsWithExportedEstimatesIDs.add(idToAdd);
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

	public SessionState getGameState() {
		return gameState;
	}

	public void setGameState(SessionState gameState) {
		this.gameState = gameState;
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
	 * @return if the session name is the default name
	 */
	public String getDefaultName() {
		return defaultSessionName;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString(){
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
		boolean open = true;
		if (endDate != null) {
			open = (new GregorianCalendar()).before(endDate);
		}
		return open && gameState == SessionState.OPEN;
	}

	/**
	 * @return a boolean indicating if the session is pending
	 */
	public boolean isPending() {
		return gameState == SessionState.PENDING;
	}

	/**
	 * @return a boolean indicating if the session has ended
	 */
	public boolean isEnded() {
		return (new GregorianCalendar()).after(endDate) || gameState == SessionState.VOTINGENDED;
	}

	/**
	 * @return a boolean indicating if the session has been closed with a final estimate
	 */
	public boolean isClosed() {
		return gameState == SessionState.CLOSED;
	}

	/**
	 * Creates the default name for the session.
	 * 
	 * @return the initial ID
	 */
	public String makeDefaultName() {
		final Date date = new Date();
		final DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
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
	public List<CreatePokerSessionErrors> validateFields(boolean haveEndDate, boolean dateHasBeenSet) {
		final List<CreatePokerSessionErrors> errors = new ArrayList<CreatePokerSessionErrors>();
		final GregorianCalendar currentDate = new GregorianCalendar();

		if (haveEndDate) {
			if (dateHasBeenSet) {
				if (endDate.before(currentDate)) {
					errors.add(CreatePokerSessionErrors.EndDateTooEarly);
				}
			}
			else {
				errors.add(CreatePokerSessionErrors.NoDateSelected);
			}
		}

		// Description validation
		if (description.equals("")){
			errors.add(CreatePokerSessionErrors.NoDescription);
		}

		// Name validation
		if (name.equals("")){
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
	 * Provides the number of elements in the list of requirements for the project. This
	 * function is called internally by the JList in NewRequirementPanel. Returns elements
	 * in reverse order, so the newest requirement is returned first.
	 * 	
	 * @return the number of requirements in the project * @see javax.swing.ListModel#getSize() * @see javax.swing.ListModel#getSize() * @see javax.swing.ListModel#getSize()
	 */
	public int requirementsGetSize() {
		return requirementIDs.size();
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


	/**
	 * @param selected the list of requirements to add to the session
	 */
	public void setRequirements(List<Requirement> selected) {
		requirementIDs = new HashSet<Integer>();
		for (Requirement requirement : selected) {
			requirementIDs.add(requirement.getId());
		}
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
		for (Estimate e : estimates) {
			if (e.getOwnerName().equals(estimate.getOwnerName()) && e.getRequirementID() == estimate.getRequirementID()) {
				estimates.remove(e);
				break;
			}
		}
		estimates.add(estimate);
		//checkReqEstimationComplete(estimate.getRequirementID());
	}

	/**
	 * checks to see if all users in the current project have estimated the requirement assosciated with the given id
	 * @param reqID requirement to check estimations of
	 */
	public void checkReqEstimationComplete(Integer reqID){
		// get all estimates for this reqID
		final List<Estimate> estimatesForReq = new ArrayList<Estimate>();
		for (Estimate e: estimates){
			if (e.getRequirementID() == reqID){
				estimatesForReq.add(e);
			}
		}

		boolean estimationComplete = true;
		for (User teamMember : getProject().getTeam()) {
			if (teamMember != null) {
				String currentUsername = teamMember.getUsername();
				boolean foundCurrentUserEstimate = false;
				for (Estimate e:estimatesForReq){
					if (currentUsername.equals(e.getOwnerName())){
						foundCurrentUserEstimate = true;
					}
				}
				if (!foundCurrentUserEstimate){
					estimationComplete = false;
				}
			}
		}

		if (estimationComplete) {
			reqsWithCompleteEstimates.add(reqID);
			addReqEstimateStats(reqID);
		}
	}


	/**
	 * @return the reqsWithCompleteEstimates		
	 */
	public Set<Integer> getReqsWithCompleteEstimates() {
		return reqsWithCompleteEstimates;
	}
	/**
	 * @param reqsWithCompleteEstimates the reqsWithCompleteEstimates to set
	 */
	public void setReqsWithCompleteEstimates(Set<Integer> reqsWithCompleteEstimates) {
		this.reqsWithCompleteEstimates = reqsWithCompleteEstimates;
	}
	
	/**
	 * @return the reqsWithSubmittedEstimates
	 */
	public Map<Requirement, Integer> getFinalEstimatesMap() {
		return finalEstimatesMap; 
	}
	
	/**
	 * @param sets reqsWithSubmittedEstimates to the input HashMap 
	 */
	public void setFinalEstimatesMap(Map<Requirement,Integer> reqsWithSubmissions) {
		finalEstimatesMap = reqsWithSubmissions; 
	}
	
	public void addFinalEstimate(int ID, int estimate){
		final RequirementModel reqs = RequirementModel.getInstance();
		final Requirement req = reqs.getRequirement(ID);
		finalEstimatesMap.put(req, estimate);
	}
	
	/**
	 * @return the reqEstimateStats
	 */
	public Map<Integer, RequirementEstimateStats> getReqEstimateStats() {
		return reqEstimateStats;
	}
	
	/**
	 * clears all existing RequirementEstimateStats from reqEstimateStats and replaces them 
	 * with the contents of the given HashMap
	 * @param newReqStats
	 */
	public void setReqEstimateStats(Map<Integer,RequirementEstimateStats> newReqStats) {
		reqEstimateStats.clear();
		reqEstimateStats.putAll(newReqStats);
	}
	
	/**
	 * creates a RequirementEstimateStats object for the requirement with the given ID 
	 * and adds it to the reqEstimateStats HashMap using the ID as they key value 
	 * @param reqID
	 */
	public void addReqEstimateStats(int reqID) {
		final List<Estimate> forThisReq = new ArrayList<Estimate>();
		for (Estimate e : estimates) {
			if (e.getRequirementID() == reqID) {
				forThisReq.add(e);
			}
		}
		reqEstimateStats.put(reqID, new RequirementEstimateStats(reqID, forThisReq));
	}
	
	public RequirementEstimateStats getStatsByID(int reqID) {
		if (reqEstimateStats.containsKey(reqID)) {
			return reqEstimateStats.get(reqID);
		}
		else {
			System.err.println("Could not find a value corresponding to key '" + reqID + "'"
					+ ", returning empty RequirementEstimateStats object");
			return new RequirementEstimateStats(reqID, new ArrayList<Estimate>());
		}
	}
	
	/**
	 * @return true if the session is allowed to be edited
	 */
	public boolean isEditable() {
		return (isPending() || ((estimates.size() == 0) && isOpen())) && sessionCreatorName.equals(ConfigManager.getConfig().getUserName());
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
		description = toCopyFrom.description;
		name = toCopyFrom.name;
		endDate = toCopyFrom.endDate;
		requirementIDs = toCopyFrom.requirementIDs;
		gameState = toCopyFrom.gameState;
		requirementIDs = toCopyFrom.requirementIDs;
		estimates = toCopyFrom.estimates;
		isUsingDeck = toCopyFrom.isUsingDeck;
		sessionCreatorName = toCopyFrom.sessionCreatorName;
		sessionDeck = toCopyFrom.sessionDeck;
		finalEstimatesMap = toCopyFrom.getFinalEstimates();
		VoterNameList = toCopyFrom.VoterNameList;
		estimateVoterList = toCopyFrom.estimateVoterList;
		reqsWithCompleteEstimates = toCopyFrom.reqsWithCompleteEstimates;
		requirementsWithExportedEstimates = toCopyFrom.requirementsWithExportedEstimates;
		requirementsWithExportedEstimatesIDs = toCopyFrom.requirementsWithExportedEstimatesIDs;
	}

	/** 
	 * Returns an array of all of the final estimation values for a finished planning poker session.
	 * 
	 * @return Hashmap relating requirement to final estimate for that requirement
	 */
	public Map<Requirement, Integer> getFinalEstimates() { 
		return finalEstimatesMap;
	}

	/**
	 * Returns a list or Requirement of those requirements which have had their final estimates sent to
	 * the requirement manager. 
	 * 
	 * @return LinkedList<Requirement> of all requirements that have had their final estimates sent to
	 * the requirement manager.
	 */
	public LinkedList<Requirement> getReqsWithExportedEstimatesList() {
		LinkedList<Requirement> requirementsWithExportedEstimatesList = new LinkedList<Requirement>();
		int i;
		for (i = 0; i < requirementsWithExportedEstimatesIDs.size(); i++) {
			requirementsWithExportedEstimatesList.add(RequirementModel.getInstance().getRequirement(requirementsWithExportedEstimatesIDs.get(i)));
		}
		return requirementsWithExportedEstimatesList;		
	}
	
	/**
	 * Adds a requirement ID to the list of requirement IDs representing the requirements that have
	 * had their final estimates sent to the requirement manager.
	 * 
	 * @param reqIDToAdd the requirement ID of the requirement which has had its final estimate sent to the
	 * requirement manager.
	 */
	public void addRequirementToExportedList(int reqIDToAdd) {
		requirementsWithExportedEstimatesIDs.add(reqIDToAdd);
}
	/**
	 * @return the VoterNameList
	 */
	public List<String> getVoterNameList() {
		return VoterNameList;
	}
	public void setVoterNameList(List<String> VoterNameList)  {
		this.VoterNameList = VoterNameList;
	}
	public List<EstimateVoters> getEstimateVoterList() {
		return estimateVoterList;
	}
	/**
	 * @param VoterNameList the VoterNameList to set
	 */
	public void setEstimateVoterList(List<EstimateVoters> EstimateVoterList) {
		estimateVoterList = EstimateVoterList;
	}
	
}
