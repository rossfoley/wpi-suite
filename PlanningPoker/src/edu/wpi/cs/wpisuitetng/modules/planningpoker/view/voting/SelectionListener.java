package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.voting;

import java.util.EventListener;

import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;

public class SelectionListener implements EventListener{
	private Requirement requirement;
	
	public void selectionMade(SelectionEvent e){
		this.requirement = e.getRequirement();
	}

	/*@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}*/

}
