/*******************************************************************************
 * Copyright (c) 2014 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Team8s
 ******************************************************************************/

package view.updateestimates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.table.DefaultTableModel;

import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.updateestimates.SelectRequirementToUpdateTable;
import static org.junit.Assert.*;

/**
 * @author amandaadkins
 *
 */
public class SelectRequirementToUpdateTableTest {

	@Test
	public void testisCellEditable(){
		Object[][] data = {};
		String[] columnNames = {"Send Estimate?", "Requirement Name", "Final Estimate"};
		SelectRequirementToUpdateTable testTable = new SelectRequirementToUpdateTable(data, columnNames ,new LinkedList<Integer>(), new HashMap<Integer, Integer>());
		assertTrue(testTable.isCellEditable(0, 0));
		assertFalse(testTable.isCellEditable(0, 2));
		assertTrue(testTable.isCellEditable(15, 0));
		assertFalse(testTable.isCellEditable(20, 50));
	}
	
	@Test
	public void testGetColumnClass(){
		Object[][] data = {};
		String[] columnNames = {"Send Estimate?", "Requirement Name", "Final Estimate"};
		SelectRequirementToUpdateTable testTable = new SelectRequirementToUpdateTable(data, columnNames ,new LinkedList<Integer>(), new HashMap<Integer, Integer>());
		DefaultTableModel testDefaultTableModel = new DefaultTableModel();
		assertEquals(Boolean.class, testTable.getColumnClass(0));
		assertEquals(testDefaultTableModel.getColumnClass(2), testTable.getColumnClass(2));
	}
	
	@Test
	public void testWasChangedByRefresh(){
		Object[][] data = {};
		String[] columnNames = {"Send Estimate?", "Requirement Name", "Final Estimate"};
		SelectRequirementToUpdateTable testTable = new SelectRequirementToUpdateTable(data, columnNames ,new LinkedList<Integer>(), new HashMap<Integer, Integer>());
		testTable.refresh();
		assertFalse(testTable.wasChangedByRefresh());
	}
	
	@Test
	public void testNoSelectedReqs(){
		Object[][] data = {};
		String[] columnNames = {"Send Estimate?", "Requirement Name", "Final Estimate"};
		SelectRequirementToUpdateTable testTable = new SelectRequirementToUpdateTable(data, columnNames ,new LinkedList<Integer>(), new HashMap<Integer, Integer>());
		assertEquals(new ArrayList<Integer>(), testTable.getSelectedReqs());
	}
}
