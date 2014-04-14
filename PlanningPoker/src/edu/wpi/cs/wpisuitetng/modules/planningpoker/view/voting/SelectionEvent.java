package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.voting;

import java.util.EventObject;

import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;

public class SelectionEvent extends EventObject{

	private Requirement requirement;
	
	public SelectionEvent(Object source, Requirement requirement) {
		super(source);
		
		this.requirement = requirement;
	}
	
	public Requirement getRequirement(){
		return this.requirement;
	}

}
