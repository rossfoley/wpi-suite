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
public class Estimate extends AbstractModel{
	private int requirementID;
	private int vote;
	private String ownerName;
	private int ownerID;
	private UUID uuid = UUID.randomUUID();
	
	public Estimate() {
		
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
	 * @return the ID of the vote creator
	 */
	public int getOwnerID(){
		return this.ownerID;
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
	 * sets the ID of the owner
	 * @param ownerID
	 */
	public void setOwnerID(int ownerID){
		this.ownerID = ownerID;
	}
	/**
	 * sets the name of the owner
	 * @param ownerName
	 */
	public void setOwnerName(String ownerName){
		this.ownerName = ownerName;
	}


	@Override
	public void save() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void delete() {
		// TODO Auto-generated method stub
		
	}



	@Override
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

	@Override
	public Boolean identify(Object o) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void copyFrom(Estimate toCopyFrom){
		this.requirementID = toCopyFrom.getRequirementID();
		this.vote = toCopyFrom.getVote();
		this.ownerName = toCopyFrom.getOwnerName();
		this.ownerID = toCopyFrom.getOwnerID();
		this.uuid = toCopyFrom.getID();
	}
	
	
}
