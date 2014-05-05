package edu.wpi.cs.wpisuitetng.modules.planningpoker.models;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.junit.Test;

import com.google.gson.Gson;

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
		boolean t = true;
		
		assertEquals(t, testDeck.getAllowMultipleSelections());
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
	
	@Test
	public void testConstructor() {
		Deck construct = new Deck();
		boolean testFalse = false;
		
		assertEquals("Deck 6", construct.getDeckName());
		assertEquals(testFalse, construct.getAllowMultipleSelections());
	}
	
	@Test
	public void testchangeNumbersToString() {
		List<Integer> testList2 = new ArrayList<Integer>();
		testList2.add(1);
		testList2.add(2);
		testList2.add(3);
		boolean trueboolean = true;
		Deck testDeckChange = new Deck(testList2, trueboolean);
		String numString = new String("1, 2, 3");

		assertEquals(numString, testDeckChange.changeNumbersToString());
		
	}
	
	@Test public void testtoJSON() {
		Gson gsontest = new Gson();
		String jsontest = gsontest.toJson(testDeck);
		
		assertEquals(jsontest, testDeck.toJSON());
	}
	
	@Test public void testfromJson() {
		Gson gsontest = new Gson();
		String jsontest = gsontest.toJson(testDeck);
		
		assertEquals(testDeck, testDeck.fromJson(jsontest));
	}
	
	@Test public void testfromJsonArray() {
		Gson gsontest = new Gson(); 
		Deck[] testDeckArray = new Deck[]{testDeck};
		String jsontest = gsontest.toJson(testDeckArray);
		
		assertArrayEquals(testDeckArray, testDeck.fromJsonArray(jsontest));
		
	}
	
	@Test public void testequals() {
		int testreq = 1;
		int testvote = 2;
		UUID testUUID = null;
		testUUID.randomUUID();
		Estimate testest = new Estimate(testreq, testvote, testUUID);
		List<Integer> testList2 = new ArrayList<Integer>();
		boolean mult2 = true;
		Deck testDeck2 = new Deck(testList2, mult2);
		testDeck.setId(1);
		testDeck2.setId(2);
		testDeck.setDeckName("testDeck1");
		testDeck2.setDeckName("testDeck2");
		
		assertFalse(testDeck.equals(null));
		assertTrue(testDeck.equals(testDeck));
		assertFalse(testDeck.equals(testest));
		assertFalse(testDeck.equals(testDeck2));
	}
}
