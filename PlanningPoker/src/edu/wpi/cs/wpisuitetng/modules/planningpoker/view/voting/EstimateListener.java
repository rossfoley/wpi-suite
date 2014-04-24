/*******************************************************************************
 * Copyright (c) 2014 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Team8s
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.voting;

import java.util.EventListener;

/** 
 * A contract between a EstimateEvent source and listener classes
 */
public class EstimateListener implements EventListener {
	private double estimate;
	
	/**
	 *  Called whenever an estimate has been submitted by an
	 *  EstimateEvent source object 
	 */
	public void estimateSubmitted(EstimateEvent e) {
		estimate = e.getEstimate();
	}
	

	public double getEstimate() {
		return estimate;
	}
	
}
