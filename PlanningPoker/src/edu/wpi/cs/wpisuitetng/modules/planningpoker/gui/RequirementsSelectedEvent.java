package edu.wpi.cs.wpisuitetng.modules.planningpoker.gui;

import java.util.EventObject;

public class RequirementsSelectedEvent extends EventObject {
	private boolean areRequirementsSelected;
	
	public RequirementsSelectedEvent(Object source, boolean areRequirementsSelected) {
		super(source);
		
		this.areRequirementsSelected = areRequirementsSelected;
	}
	
	public boolean areRequirementsSelected() {
		return this.areRequirementsSelected;
	}

}
