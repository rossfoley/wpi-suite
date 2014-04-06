package edu.wpi.cs.wpisuitetng.modules.planningpoker.controller;


import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.controller.UpdateRequirementController;

public class UpdateRequirement {
	
	//GetRequirementsController getRequirementsController = GetRequirementsController.getInstance();
	UpdateRequirementController updateRequirementsController;
	
	public UpdateRequirement(){
		updateRequirementsController = UpdateRequirementController.getInstance();
	}
	
	/**
	 * 
	 * @param Req Requirement to update
	 * 
	 */
	public void Update(Requirement Req){
		updateRequirementsController.updateRequirement(Req);
	}
	
}
