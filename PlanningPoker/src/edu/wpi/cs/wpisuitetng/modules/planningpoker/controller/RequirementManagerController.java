package edu.wpi.cs.wpisuitetng.modules.planningpoker.controller;

import edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.ViewEventController;


/**
 * This class is to allow other classes in the statistics package to access 
 * the ViewEventControllers for both RequirementManager and PlanningPoker 
 * 
 * @author Rick Wight (fmwight)
 *
 */
public class RequirementManagerController {
	private static RequirementManagerController instance = null;
	
	private RequirementManagerController() {}
	
	public static RequirementManagerController getInstance() {
		if (instance == null) {
			instance = new RequirementManagerController();
		}
		return instance;
	}
	
	public void refreshReqManagerTable() {
		ViewEventController.getInstance().refreshTable();
	}
}
