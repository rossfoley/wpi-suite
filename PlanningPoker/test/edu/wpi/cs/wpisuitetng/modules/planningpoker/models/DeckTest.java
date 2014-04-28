package edu.wpi.cs.wpisuitetng.modules.planningpoker.models;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.Deck;

public class DeckTest {
	List<Integer> testList = new ArrayList<Integer>();
	boolean mult = true;
	Deck testDeck = new Deck(testList, mult);
	
	@Test
	public void testGetDeckName() {
		String testDeckString = new String("testDeck");
		testDeck.setDeckName(testDeckString);
		
		assertEquals(testDeckString, testDeck.getDeckName());
	}
	
	@Test
	public void testSetDeckName() {
		testDeck.setDeckName("testDeck");
		
		testDeck.setDeckName("nottestdeck");
		assertEquals("nottestdeck", testDeck.getDeckName());
	}
	
	@Test
	public void testGetId() {
		testDeck.setId(2);
		
		assertEquals(2, testDeck.getId());
	}
	
	@Test
	public void testSetId() {
		testDeck.setId(2);
		
		testDeck.setId(5);
		assertEquals(5, testDeck.getId());
	}
	
	@Test
	public void testgetAllowMultipleSelections() {
		
		assertEquals(mult, testDeck.getAllowMultipleSelections());
	}
	
	@Test
	public void testsetAllowMultipleSelections() {
		boolean changed = false;
		
		testDeck.setAllowMultipleSelections(changed);
		assertEquals(changed, testDeck.getAllowMultipleSelections());
	}
	
	@Test
	public void testgetNumbersInDeck() {
		testList.add(1);
		testList.add(2);
		List<Integer> testList2 = testDeck.getNumbersInDeck();
		
		assertEquals(testList, testList2);
	}
	
	@Test 
	public void testsetNumbersInDeck() {
		List<Integer> testList2 = new ArrayList<Integer>();
		testList2.add(1);
		testList2.add(2);
		testDeck.setNumbersInDeck(testList2);
		
		assertEquals(testList2, testDeck.getNumbersInDeck());
	}
	@Test 
	public void testcopyFrom() {
		List<Integer> testList2 = new ArrayList<Integer>();
		testList2.add(1);
		testList2.add(2);
		boolean mult2 = true;
		Deck testDeck2 = new Deck(testList2, mult2);
		testDeck2.setId(1);
		testDeck2.setDeckName("Hurf");
		testDeck.setId(5);
		testDeck.setDeckName("Durf");
		
		testDeck.copyFrom(testDeck2);
		assertEquals(1, testDeck.getId());
		assertEquals("Hurf", testDeck.getDeckName());
		assertEquals(mult2, testDeck.getAllowMultipleSelections());
		assertEquals(testList2, testDeck.getNumbersInDeck());
		
		
	}
}
