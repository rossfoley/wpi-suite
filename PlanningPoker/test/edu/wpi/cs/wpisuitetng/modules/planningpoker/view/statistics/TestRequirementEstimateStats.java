/*******************************************************************************
 * Copyright (c) 2014 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Team8s
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.statistics;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.UUID;

import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.Estimate;

public class TestRequirementEstimateStats {
	ArrayList<Estimate> testEstimates = new ArrayList<Estimate>();
	boolean pointlessVariable1 = testEstimates.add(new Estimate(1, 1, UUID.randomUUID()));
	boolean pointlessVariable2 = testEstimates.add(new Estimate(2, 2, UUID.randomUUID()));
	boolean pointlessVariable3 = testEstimates.add(new Estimate(3, 3, UUID.randomUUID()));
	boolean pointlessVariable7 = testEstimates.add(new Estimate(7, 7, UUID.randomUUID()));
	boolean pointlessVariable8 = testEstimates.add(new Estimate(8, 8, UUID.randomUUID()));
	
	RequirementEstimateStats testStats = new RequirementEstimateStats(0, testEstimates);
	
	@Test
	public void testCalculateMean() {
		assertEquals(4.2, testStats.calculateMean(), 0.01);
		boolean pointlessVariable13 = testEstimates.add(new Estimate(13, 13, UUID.randomUUID()));
		assertEquals(5.66, testStats.calculateMean(), 0.01);
		testStats.setEstimates(new ArrayList<Estimate>());
		assertEquals(0, testStats.calculateMean(), 0.1); 
	}
	
	@Test
	public void testCalculateMedian() {
		testStats.sortEstimatesByVote();
		assertEquals(3, testStats.calculateMedian(), 0.1);
		boolean pointlessVariable13 = testEstimates.add(new Estimate(13, 13, UUID.randomUUID()));
		testStats.sortEstimatesByVote(); 
		assertEquals(5, testStats.calculateMedian(), 0.1); 
		testStats.setEstimates(new ArrayList<Estimate>());
		assertEquals(0, testStats.calculateMedian(), 0.1); 
	}
	

}
