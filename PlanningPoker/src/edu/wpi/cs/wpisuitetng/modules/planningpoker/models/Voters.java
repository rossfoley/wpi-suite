package edu.wpi.cs.wpisuitetng.modules.planningpoker.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import edu.wpi.cs.wpisuitetng.modules.core.models.Project;
import edu.wpi.cs.wpisuitetng.janeway.config.ConfigManager;
import edu.wpi.cs.wpisuitetng.modules.AbstractModel;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.controller.GetRequirementsController;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.gui.CreatePokerSessionErrors;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.gui.NoDescriptionException;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.RequirementModel;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.characteristics.RequirementPriority;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.characteristics.RequirementStatus;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.characteristics.RequirementType;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.iterations.IterationModel;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.requirements.ViewMode;
import edu.wpi.cs.wpisuitetng.modules.*;

public class Voters extends AbstractModel {
	private Project project;
	private List<Estimate> estimates;
	private Set<Integer> reqsWithCompleteEstimates;
	public Voters() {
		project = getProject();
	}
	/**
	 * first get the selected session, get list of requirements.
	 * for each of requirements selected compare whether user has a vote entered
	 * 
	 * @param reqID
	 */
	public void testvote() {
		project = getProject();
		if (project == null) {
			System.out.println("NULL");
		} else {
			System.out.println("NOT NULL");
		}
		try {
			getProject().getTeam();
			System.out.println("in try ");
		} catch (Exception e ) {
			System.out.println( "NULL ");
		}
		for (User teamMember : getProject().getTeam()) {
			if (teamMember != null) {
				System.out.println(teamMember.getUsername());
			}
		}
	}
	public void checkReqEstimationComplete(Integer reqID){
		// get all estimates for this reqID
		ArrayList<Estimate> estimatesForReq = new ArrayList<Estimate>();
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
		}
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
}
