package tests;

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
		testDeck.setDeckName("testDeck");
		
		assertTrue(testDeck.getDeckName() == "testDeck");
		assertFalse(testDeck.getDeckName() == "Blarg");
	}
	
	@Test
	public void testSetDeckName() {
		testDeck.setDeckName("testDeck");
		
		assertTrue(testDeck.getDeckName() == "testDeck");
		testDeck.setDeckName("nottestdeck");
		assertTrue(testDeck.getDeckName() == "nottestdeck");
	}
	
	@Test
	public void testGetId() {
		testDeck.setId(2);
		
		assertTrue(testDeck.getId() == 2);
	}
	
	@Test
	public void testSetId() {
		testDeck.setId(2);
		
		assertTrue(testDeck.getId() == 2);
		testDeck.setId(5);
		assertTrue(testDeck.getId() == 5);
	}
	
	@Test
	public void testgetAllowMultipleSelections() {
		
		assertTrue(testDeck.getAllowMultipleSelections() == mult);
	}
	
	@Test
	public void testsetAllowMultipleSelections() {
		boolean changed = false;
		
		assertTrue(testDeck.getAllowMultipleSelections() == mult);
		testDeck.setAllowMultipleSelections(changed);
		assertTrue(testDeck.getAllowMultipleSelections() == changed);
	}
	
	@Test
	public void testgetNumbersInDeck() {
		testList.add(1);
		testList.add(2);
		List<Integer> testList2 = testDeck.getNumbersInDeck();
		
		assertTrue(testList == testList2);
	}
	
	@Test 
	public void testsetNumbersInDeck() {
		List<Integer> testList2 = new ArrayList<Integer>();
		testList2.add(1);
		testList2.add(2);
		testDeck.setNumbersInDeck(testList2);
		
		assertTrue(testDeck.getNumbersInDeck() == testList2);
	}
	@Test 
	public void testcopyFrom() {
		List<Integer> testList2 = new ArrayList<Integer>();
		boolean mult2 = true;
		Deck testDeck2 = new Deck(testList2, mult2);
		testDeck.setId(1);
		testDeck.set
	}
}
