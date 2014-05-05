/*******************************************************************************
 * Copyright (c) 2014 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Team8s
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.overview;

import java.util.HashMap;
import java.util.LinkedList;

import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;

/**
 * Tests for sendAllEstimates() and sendSingleEstimate() methods within OverviewDetailPanel
 * 
 * Not sure whether or not this can even be tested, since this is GUI stuff
 * In order to test, you would need to have a hashmap of requirements and their
 * final estimates, and then check whether or not the matching requirement had been updated
 * within the requirement manager, which would imply actually saving something to the
 * requirement manager.
 * 
 * @author Brian Flynn
 */
//public class SendFinalEstimatesTest {
//	private HashMap<Requirement, Integer> reqEstMap; // a dummy requirement hashmap for testing.
//	
//	public SendFinalEstimatesTest() {
//		// create a dummy hashmap of requirements and their final estimates.
//		reqEstMap = new HashMap<Requirement, Integer>();
//		reqEstMap.put(arg0, arg1);
//		reqEstMap.put(arg2, arg3);
//		reqEstMap.put(arg4, arg5);
//		reqEstMap.put(arg6, arg7);
//	}
//	
//	/**
//	 * Tests whether or not requirements' final estimates are sent to the requirement manager.
//   * Only sends one final estimate from the hashap that matches the value being checked.
//	 */
//	@Test
//	public void testSendSingleFinalEstimate() {
//		/* Can't really test this, actually, would need to call the function, then check whether
//		/ "arg1" is equal to the requirements estimate within the requirement manager.
//		*/
//		assertEquals(sendSingleEstimate(arg0),requirementManager.getRequirement().getEstimate());
//	}
//
//	/**
//	 * Tests whether or not all requirements within a hashmap have their final
//	 * estimates sent to the requirement manager.
//	 */
//	@Test
//	public void testSendAllFinalEstimates() {
//		assertEquals();
//	}
//	}
//
//}
