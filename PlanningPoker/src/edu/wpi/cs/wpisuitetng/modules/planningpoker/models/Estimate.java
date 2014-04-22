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

import java.util.Set;
import java.util.UUID;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.janeway.config.ConfigManager;
import edu.wpi.cs.wpisuitetng.modules.AbstractModel;

/**
 * A vote that contains the vote, requirementID, ownerID, ownername and it's own ID.
 * @author Kevin
 * 
 */
public class Estimate implements Comparable<Estimate> {
	private int requirementID;
	private int vote;
	private String ownerName;
	private UUID uuid = UUID.randomUUID();
	private UUID sessionID;
	
	public Estimate() {
		this.vote = -1;	// Initialize as an invalid vote
	}
	
	/**
	 * detailed constructor (for testing purposes)
	 * @param reqID
	 * @param newVote
	 * @param sessionUUID
	 */
	public Estimate(int reqID, int newVote, UUID sessionUUID) {
		requirementID = reqID; 
		vote = newVote; 
		sessionID = sessionUUID; 
	}
	
	/**
	 * compares this estimate to the given estimate by votes
	 * @return -1 if this estimate has a smaller vote than the given, 1 if larger, and 0 if same 
	 */
	public int compareTo(Estimate e) {
		if (vote < e.vote) {
			return -1; 
		}
		else if (vote > e.vote) {
			return 1; 
		}
		else {
			return 0; 
		}
	}
	
	//---------------Gets
	/**
	 * 
	 * @return the requirement ID to be estimated
	 */
	public int getRequirementID(){
		return this.requirementID;
	}
	
	/**
	 * 
	 * @return returns the ID of the estimate
	 */
	public UUID getID(){
		return this.uuid;
	}
	
	/**
	 * 
	 * @return the value to vote for
	 */
	public int getVote(){
		return this.vote;
	}
	
	/**
	 * 
	 * @return the name of the creator
	 */
	public String getOwnerName(){
		return this.ownerName;
	}
	//---------------Sets
	/**
	 * sets the requirementID
	 * @param requirementID
	 */
	public void setRequirementID(int requirementID){
		this.requirementID = requirementID;
	}
	/**
	 * sets the estimate's ID
	 * @param ID
	 */
	public void setID(UUID ID){
		this.uuid = ID;
	}
	/**
	 * sets the vote
	 * @param vote
	 */
	public void setVote(int vote){
		this.vote = vote;
	}
	/**
	 * sets the name of the owner
	 * @param ownerName
	 */
	public void setOwnerName(String ownerName){
		this.ownerName = ownerName;
	}


	
	public String toJSON() {
		return new Gson().toJson(this, Estimate.class);
	}
	
	/**
	 * 
	 * @param json json string to convert from
	 * @return estimate parsed from the json
	 */
	public static Estimate fromJson(String json) {
		final Gson parser = new Gson();
		return parser.fromJson(json, Estimate.class);
	}

	/**
	 * returns an array of estimates from a json string
	 * @param json string containing a JSON-encoded array of Estimate
	 * 
	 * @return an array of Estimates deserialized from the given JSON string
	 */
	public static Estimate[] fromJsonArray(String json) {
		final Gson parser = new Gson();
		return parser.fromJson(json, Estimate[].class);
	}

	public UUID getSessionID() {
		return this.sessionID;
	}
	public void setSessionID(UUID id) {
		this.sessionID = id;
	}
	
	
}
