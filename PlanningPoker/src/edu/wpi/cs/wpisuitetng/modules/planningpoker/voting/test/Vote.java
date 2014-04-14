package edu.wpi.cs.wpisuitetng.modules.planningpoker.voting.test;

public class Vote {
	
	private int id;
	private String user;
	private int estimate;
	
	Vote(int id, String user, int estimate){
		
		this.id = id;
		this.user = user;
		this.estimate = estimate;
		
	}
	
	public void setID(int id){
		this.id = id;
	}
	
	public int getID(){
		return this.id;
	}
	
	public void setUser(String user){
		this.user = user;
	}
	
	public String getUser(){
		return this.user;
	}
	
	public void setEstimate(int estimate){
		this.estimate = estimate;
	}
	
	public int getEstimate(){
		return this.estimate;
	}

}
