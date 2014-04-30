package edu.wpi.cs.wpisuitetng.modules.planningpoker.models;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class DeckListModelTest {
	private List<Integer> testCards;

	public DeckListModelTest() {
		// Create deck for testing purposes
		testCards = new ArrayList<Integer>();
		for (int i = 0; i < 14; i += 2) {
			testCards.add(i);
		}
	}
	
	/**
	 * Tests if the model is correctly instantiated on the first call of getInstance.
	 * This test must be the first test to run to pass.
	 */
	@Test
	public void testGetInstance() {
		assertNotNull(DeckListModel.getInstance());
	}

	@Test
	public void testGetSizeEmpty() {
		DeckListModel.getInstance().emptyModel();
		assertEquals(0, DeckListModel.getInstance().getSize());
	}
	
	@Test
	public void testGetSizeNonEmpty() {
		DeckListModel.getInstance().emptyModel();
		DeckListModel.getInstance().addDeck(new Deck());
		DeckListModel.getInstance().addDeck(new Deck(testCards, false));
		DeckListModel.getInstance().addDeck(new Deck(testCards, true));
		assertEquals(3, DeckListModel.getInstance().getSize());
	}

	@Test
	public void testGetElementAtEmptyList() {
		DeckListModel.getInstance().emptyModel();
		try {
			DeckListModel.getInstance().getElementAt(0);
			fail("IndexOutOfBounds not caught");
		} catch (IndexOutOfBoundsException ex) {
			assertTrue("IndexOutOfBounds caught", true);
		}
	}
	
	@Test
	public void testGetElementAtNonEmptyList() {
		Deck deckToTest = new Deck(testCards, false);
		DeckListModel.getInstance().emptyModel();
		DeckListModel.getInstance().addDeck(new Deck());
		DeckListModel.getInstance().addDeck(deckToTest);
		DeckListModel.getInstance().addDeck(new Deck(testCards, true));

		assertEquals(deckToTest, (Deck) DeckListModel.getInstance().getElementAt(1));
	}

	@Test
	public void testGetDeck() {
		List<Integer> listToTest = new ArrayList<Integer>();
		for (int val : testCards) {
			listToTest.add(val);
		}
		Deck deckToTest = new Deck(listToTest, false);
		
		DeckListModel.getInstance().emptyModel();
		DeckListModel.getInstance().addDeck(new Deck());
		DeckListModel.getInstance().addDeck(deckToTest);
		DeckListModel.getInstance().addDeck(new Deck(testCards, true));

		assertEquals(deckToTest, (Deck) DeckListModel.getInstance().getDeck(listToTest));		
	}

	@Test
	public void testGetNextID() {
		int currentID = DeckListModel.getInstance().getNextID();
		assertEquals(++currentID, DeckListModel.getInstance().getNextID());
	}

	@Test
	public void testMakeDefaultDeck() {
		// Make the default deck
		ArrayList<Integer> defaultDeckNums = new ArrayList<Integer>();
		defaultDeckNums.add(0);
		defaultDeckNums.add(1);
		defaultDeckNums.add(1);
		defaultDeckNums.add(2);
		defaultDeckNums.add(3);
		defaultDeckNums.add(5);
		defaultDeckNums.add(8);
		defaultDeckNums.add(13);
		Deck defaultDeck = new Deck(defaultDeckNums, true);
		defaultDeck.setDeckName("Default");
		assertEquals(defaultDeck, DeckListModel.getInstance().makeDefaultDeck());
	}

	@Test
	public void testEmptyModel() {
		DeckListModel.getInstance().addDeck(new Deck());
		DeckListModel.getInstance().addDeck(new Deck());
		DeckListModel.getInstance().emptyModel();
		List<Deck> emptyList = new ArrayList<Deck>();
		assertEquals(emptyList, DeckListModel.getInstance().getDecks());
	}

	@Test
	public void testGetDecks() {
		Deck[] listToTest = new Deck[3];
		listToTest[0] = new Deck();
		listToTest[1] = new Deck(testCards, false);
		listToTest[2] = new Deck(testCards, true);
		
		DeckListModel.getInstance().emptyModel();
		DeckListModel.getInstance().addDecks(listToTest);
		
		List<Deck> actualDecks = DeckListModel.getInstance().getDecks();
		// Check if both lists have the same decks
		for (Deck element : listToTest) {
			assertTrue(actualDecks.contains(element));
		}
		// Make sure they are the same size
		assertEquals(listToTest.length, actualDecks.size());
	}

}
