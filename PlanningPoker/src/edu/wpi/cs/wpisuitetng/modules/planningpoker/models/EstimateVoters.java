package edu.wpi.cs.wpisuitetng.modules.planningpoker.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.modules.AbstractModel;

/**
 * Combines the Estimate with list of voters who voted on them.
 * @author Shawn-User-Ultrabook
 *
 */
public class EstimateVoters extends AbstractModel {
	public int requirementID;
	public int vote;
	public String voterUsername;
	public String ownerName;
	public UUID uuid = UUID.randomUUID();
	public UUID sessionID;
	public List<String> VoterNameList = new ArrayList<String>();
	
	public EstimateVoters() {
		voterUsername = new String();
	}
	
	public List<String> getVoterNameList() {
		return VoterNameList;
	}
	
	//---------------Gets
	/**
	 * 
	 * @return the requirement ID to be estimated
	 */
	public int getRequirementID(){
		return requirementID;
	}
	
	/**
	 * 
	 * @return returns the ID of the estimate
	 */
	public UUID getID(){
		return uuid;
	}
	
	/**
	 * 
	 * @return the value to vote for
	 */
	public int getVote(){
		return vote;
	}
	
	/**
	 * 
	 * @return the name of the creator
	 */
	public String getOwnerName(){
		return ownerName;
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
		uuid = ID;
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


	public UUID getSessionID() {
		return sessionID;
	}
	public void setSessionID(UUID id) {
		sessionID = id;
	}
	/**
	 * @param VoterNameList the VoterNameList to set
	 */
	public void setVoterNameList(List<String> VoterNameList) {
		this.VoterNameList = VoterNameList;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean identify(Object o) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @return the voterUsername
	 */
	public String getVoterUsername() {
		return voterUsername;
	}

	/**
	 * @param voterUsername the voterUsername to set
	 */
	public void setVoterUsername(String voterUsername) {
		this.voterUsername = voterUsername;
	}
}
