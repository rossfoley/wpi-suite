package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.deck;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.Deck;

public class CreateDeckTest {

	/**
	 * Tests adding cards and removing cards from the new deck
	 */
	@Test
	public void testAddRemoveCards() {
		CreateDeck deckPanel = new CreateDeck();

		// Add some cards
		for (int i = 0; i < 5; i += 2) {
			deckPanel.addCard(i);
		}
		deckPanel.removeAllCards();
		assertEquals(0, deckPanel.getListOfCards().size());
		// Add some cards again
		List<Integer> expectedCards = new ArrayList<Integer>();
		for (int i = 0; i < 14; i++) {
			deckPanel.addCard(i);
			expectedCards.add(i);
		}
		// Remove some cards
		for (int i = 3; i < 14; i += 3) {
			deckPanel.removeCard(i);
			expectedCards.remove(new Integer(i));
		}
		// Check if the expected and actual are the same
		assertTrue(expectedCards.equals(deckPanel.getListOfCards()));
	}
	
	/**
	 * Tests creating a deck with the default name, default selection mode, and a few cards added
	 */
	@Test
	public void testCreateDeck() {
		List<Integer> expectedCards = new ArrayList<Integer>();
		CreateDeck deckPanel = new CreateDeck();
		
		for (int i = 0; i < 5; i++) {
			deckPanel.addCard(i);
			expectedCards.add(i);
		}
		
		Deck expectedDeck = new Deck(expectedCards, false);
		expectedDeck.setDeckName("Deck 0");
		
		Deck createdDeck = deckPanel.createDeck();
		// Check Deck name
		assertEquals(expectedDeck.getDeckName(), createdDeck.getDeckName());
		// Check allow-multi-selection
		assertFalse(createdDeck.getAllowMultipleSelections());
		// Check if the cards are the same
		assertTrue(expectedDeck.getNumbersInDeck().equals(createdDeck.getNumbersInDeck()));
	}

	@Test
	public void testFireDeckEvent() {
		List<Integer> expectedCards = new ArrayList<Integer>();
		// Create the new deck
		CreateDeck deckPanel = new CreateDeck();
		
		for (int i = 0; i < 10; i += 2) {
			deckPanel.addCard(i);
			expectedCards.add(i);
		}
		
		final Deck expectedDeck = new Deck(expectedCards, false);
		expectedDeck.setDeckName("Deck 0");
		
		// Setup the DeckListener
		deckPanel.addDeckListener(new DeckListener() {
			@Override
			public void deckSubmitted(DeckEvent e) {
				final Deck newDeck = e.getDeck();
				// Check Deck name
				assertEquals(expectedDeck.getDeckName(), newDeck.getDeckName());
				// Check allow-multi-selection
				assertFalse(newDeck.getAllowMultipleSelections());
				// Check if the cards are the same
				assertTrue(expectedDeck.getNumbersInDeck().equals(newDeck.getNumbersInDeck()));
			}
		});
		
		// Fire the DeckEvent
		deckPanel.fireDeckEvent(deckPanel.createDeck());
	}

}
