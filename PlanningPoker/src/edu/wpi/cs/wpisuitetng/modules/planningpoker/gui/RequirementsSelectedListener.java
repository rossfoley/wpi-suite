package edu.wpi.cs.wpisuitetng.modules.planningpoker.gui;

import java.util.EventListener;

/**
 * A contract between a RequirementsSelected source and listener classes
 */
public class RequirementsSelectedListener implements EventListener {
	private boolean areRequirementsSelected;
	
	/**
	 *  Called whenever a requirement has been selected by a
	 *  RequirementSelected source object 
	 */
	public void setRequirementsSelected(RequirementsSelectedEvent e) {
		this.areRequirementsSelected = e.areRequirementsSelected();
	}
	
	public boolean areRequirementsSelected() {
		return this.areRequirementsSelected;
	}

}
