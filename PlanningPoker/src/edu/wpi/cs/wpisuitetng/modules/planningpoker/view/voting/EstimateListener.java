package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.voting;

import java.util.EventListener;

/** A contract between a EstimateEvent source and
 *   listener classes
 */
public class EstimateListener implements EventListener {
	private double estimate;
	/**
	 *  Called whenever an estimate has been submitted by an
	 *  EstimateEvent source object 
	 */
	public void estimateSubmitted(EstimateEvent e) {
		this.estimate = e.getEstimate();
	}
}
