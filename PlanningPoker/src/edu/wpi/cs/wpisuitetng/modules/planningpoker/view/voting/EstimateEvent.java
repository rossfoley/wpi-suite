package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.voting;

import java.util.EventObject;

public class EstimateEvent extends EventObject {
	private double estimate;

	public EstimateEvent(Object source, double estimate) {
		super(source);
		
		this.estimate = estimate;
	}
	
	public double getEstimate() {
		return this.estimate;
	}

}
