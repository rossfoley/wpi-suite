package edu.wpi.cs.wpisuitetng.modules.planningpoker.gui;
import java.util.*;

import edu.wpi.cs.wpisuitetng.modules.requirementmanager.controller.GetRequirementsController;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.RequirementModel;

public class SelectFromListPanelDatabase extends TimerTask {
	public static void main(String[] args) {
	      TimerTask task = new SelectFromListPanelDatabase();
	      Timer timer = new Timer();
    
	      timer.schedule(task,100, 100);      
	   }
	private List<Requirement> requirements;
	public void run() {
		GetRequirementsController requirementsController = GetRequirementsController.getInstance();
		requirementsController.retrieveRequirements();
		RequirementModel requirementModel = RequirementModel.getInstance();
		try {
			List<Requirement> reqsList = requirementModel.getRequirements();
			List<Requirement> reqsInBacklog = new LinkedList<Requirement>();
			if(reqsList.isEmpty()){
				requirementsController.retrieveRequirements();
				reqsList = requirementModel.getRequirements();
			}
			Thread.sleep(1000);
			for (Requirement r:reqsList){
				if (r.getIteration().equals("Backlog")){
					reqsInBacklog.add(r);
				}
			} 
			this.requirements = reqsInBacklog;
		
		}
		catch (Exception e) {
			System.out.println("Populaton requirement exception");
		}
	}
	   
}
