package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.voting;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventListener;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.Estimate;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;

public class SelectionListener implements EventListener{
	private Requirement requirement;
	
	public void selectionMade(SelectionEvent e){
		requirement = e.getRequirement();
	}

	/*@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}*/

}
