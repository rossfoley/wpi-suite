package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.voting;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;

import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.Deck;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.Estimate;

public class DeckVotingPanelTest {
	private Deck testDeckMultiSelect;
	private Deck testDeckSingleSelect;

	public DeckVotingPanelTest() {
		// Create deck for testing purposes
		List<Integer> cards = new ArrayList<Integer>();
		for (int i = 0; i < 14; i += 2) {
			cards.add(i);
		}
		testDeckMultiSelect = new Deck(cards, true);
		testDeckSingleSelect = new Deck(cards, false);
	}

	/**
	 * Tests to make sure the panel is built without any errors
	 */
	@Test
	public void testBuildGUIWithNoDeck() {	
		try {
			DeckVotingPanel deckPanel = new DeckVotingPanel(testDeckSingleSelect);
			assertEquals(0, deckPanel.getEstimate());
			DeckVotingPanel deckPanel2 = new DeckVotingPanel();
			assertEquals(0, deckPanel2.getEstimate());
		} catch (Error e) {
			fail("Error caught: " + e);
		}
	}
	
	/**
	 * Tests the setCardSelected and isCardSelected setters/getters
	 */
	@Test
	public void testCardSelection() {
		DeckVotingPanel deckPanel = new DeckVotingPanel(testDeckMultiSelect);
		
		// Check set/is CardSelected without clearing selection
		deckPanel.clearSelectedCards();
		for (JButton card : deckPanel.getCardButtons()) {
			deckPanel.setCardSelected(card, true);
			assertTrue(deckPanel.isCardSelected(card));
		}
		// Check set/is CardSelected with clearing selection
		for (JButton card : deckPanel.getCardButtons()) {
			deckPanel.clearSelectedCards();
			deckPanel.setCardSelected(card, true);
			assertTrue(deckPanel.isCardSelected(card));
		}
	}
	
	/**
	 * Tests the clearSelectedCards setter with no cards set
	 */
	@Test
	public void testClearSelectedCardsOnNoneSelected() {
		DeckVotingPanel deckPanel = new DeckVotingPanel(testDeckMultiSelect);
		
		// Check clearSelectedCards on an empty deck
		deckPanel.clearSelectedCards();
		for (JButton card : deckPanel.getCardButtons()) {
			assertFalse(deckPanel.isCardSelected(card));
		}
	}
	
	/**
	 * Tests the clearSelectedCards setter with all cards selected
	 */
	@Test
	public void testClearSelectedCardsOnAllSelected() {
		DeckVotingPanel deckPanel = new DeckVotingPanel(testDeckMultiSelect);
		// Select all cards
		for (JButton card : deckPanel.getCardButtons()) {
			deckPanel.setCardSelected(card, true);
		}
		// Check clearSelectedCards on a fully selected deck
		deckPanel.clearSelectedCards();
		for (JButton card : deckPanel.getCardButtons()) {
			assertFalse(deckPanel.isCardSelected(card));
		}
	}
	
	/**
	 * Tests the clearSelectedCards setter with some cards set
	 */
	@Test
	public void testClearSelectedCardsOnSomeSelected() {
		DeckVotingPanel deckPanel = new DeckVotingPanel(testDeckMultiSelect);
		int selectCount = 0;
		// Select some cards
		for (JButton card : deckPanel.getCardButtons()) {
			if (selectCount % 3 == 0) {
				deckPanel.setCardSelected(card, true);
			}
			else {
				deckPanel.setCardSelected(card, false);
			}
			selectCount++;
		}
		// Check clearSelectedCards on a fully selected deck
		deckPanel.clearSelectedCards();
		for (JButton card : deckPanel.getCardButtons()) {
			assertFalse(deckPanel.isCardSelected(card));
		}
	}
	
	/**
	 * Tests the getEstimate getter with no cards selected
	 */
	@Test
	public void testGetEstimateNoneSelected() {
		int estimate = 0;
		
		DeckVotingPanel deckPanel = new DeckVotingPanel(testDeckMultiSelect);
		deckPanel.clearSelectedCards();
		for (JButton card : deckPanel.getCardButtons()) {
			deckPanel.updateEstimate(card);
		}
		assertEquals(estimate, deckPanel.getEstimate());
		
	}
	
	/**
	 * Tests the getEstimate getter with some cards selected using a single selection deck
	 */
	@Test
	public void testSingleDeckGetEstimateSomeSelected() {	
		int estimate = 0;
		int selectCount = 0;
		
		DeckVotingPanel deckPanel = new DeckVotingPanel(testDeckSingleSelect);
		// Select some cards
		for (JButton card : deckPanel.getCardButtons()) {
			if (selectCount % 2 == 0) {
				deckPanel.setCardSelected(card, true);
				deckPanel.updateEstimate(card);
				if (Integer.parseInt(card.getName()) > estimate) {
					estimate = Integer.parseInt(card.getName());
				}
			}
			else {
				deckPanel.setCardSelected(card, false);
			}
			selectCount++;
		}
		assertEquals(estimate, deckPanel.getEstimate());
	}
	
	/**
	 * Tests the getEstimate getter with some cards selected using a multipe selection deck
	 */
	@Test
	public void testMultiDeckGetEstimateSomeSelected() {	
		int estimate = 0;
		int selectCount = 0;
		
		DeckVotingPanel deckPanel = new DeckVotingPanel(testDeckMultiSelect);
		// Select some cards
		for (JButton card : deckPanel.getCardButtons()) {
			if (selectCount % 2 == 0) {
				deckPanel.setCardSelected(card, true);
				deckPanel.updateEstimate(card);
				estimate += Integer.parseInt(card.getName());
			}
			else {
				deckPanel.setCardSelected(card, false);
			}
			selectCount++;
		}
		assertEquals(estimate, deckPanel.getEstimate());
	}
	
	/**
	 * Tests the getEstimate getter with all cards selected using a single selection deck
	 */
	@Test
	public void testSingleDeckGetEstimateAllSelected() {	
		int estimate = 0;
		
		DeckVotingPanel deckPanel = new DeckVotingPanel(testDeckSingleSelect);
		// Select some cards
		for (JButton card : deckPanel.getCardButtons()) {
			deckPanel.setCardSelected(card, true);
			deckPanel.updateEstimate(card);
			if (Integer.parseInt(card.getName()) > estimate) {
				estimate = Integer.parseInt(card.getName());
			}
		}
		assertEquals(estimate, deckPanel.getEstimate());
	}
	
	/**
	 * Tests the getEstimate getter with all cards selected using a multiple selection deck
	 */
	@Test
	public void testMultiDeckGetEstimateAllSelected() {	
		int estimate = 0;
		
		DeckVotingPanel deckPanel = new DeckVotingPanel(testDeckMultiSelect);
		// Select some cards
		for (JButton card : deckPanel.getCardButtons()) {
			deckPanel.setCardSelected(card, true);
			deckPanel.updateEstimate(card);
			estimate += Integer.parseInt(card.getName());
		}
		assertEquals(estimate, deckPanel.getEstimate());
	}
	
	@Test
	public void testGetEstimateWithNoDeckAndPrevEstimate() {
		Estimate prevEst = new Estimate();
		prevEst.setVote(5);
		DeckVotingPanel deckPanel = new DeckVotingPanel(null, prevEst);
		assertEquals(5, deckPanel.getEstimate());
	}	
	
	/**
	 * Tests the updateEstimate function after selected and de-selected a card with a
	 * single selection deck
	 */
	@Test
	public void testSingleDeckUpdateEstimateDeselectCard() {
		int estimate = 0;
		
		DeckVotingPanel deckPanel = new DeckVotingPanel(testDeckSingleSelect);
		// Select some cards, then de-select (estimate should be 0 after de-select)
		for (JButton card : deckPanel.getCardButtons()) {
			deckPanel.setCardSelected(card, true);
			deckPanel.updateEstimate(card);
			if (Integer.parseInt(card.getName()) > estimate) {
				estimate = Integer.parseInt(card.getName());
			}
			assertEquals(estimate, deckPanel.getEstimate());
			deckPanel.setCardSelected(card, false);
			deckPanel.updateEstimate(card);
			estimate = 0;
			assertEquals(estimate, deckPanel.getEstimate());
		}
	}

	/**
	 * Tests if the previous selected cards can be correctly determined with a multiple selection deck.
	 * Previous estimate = 0
	 */
	@Test
	public void testMultiDeckCardsFromLastEstimateZero() {
		Estimate prevEst = new Estimate();
		prevEst.setVote(0);
		DeckVotingPanel deckPanel = new DeckVotingPanel(testDeckMultiSelect, prevEst);
		// Get previous selected cards
		List<Integer> prevSelected= deckPanel.cardsFromLastEstimate();
		for (int val : prevSelected) {
			assertEquals(0, val);	
		}
	}
	
	/**
	 * Tests if the previous selected cards can be correctly determined with a single selection deck.
	 * Previous estimate = 0
	 */
	@Test
	public void testSingleDeckCardsFromLastEstimateZero() {
		Estimate prevEst = new Estimate();
		prevEst.setVote(0);
		DeckVotingPanel deckPanel = new DeckVotingPanel(testDeckSingleSelect, prevEst);
		// Get previous selected cards
		List<Integer> prevSelected= deckPanel.cardsFromLastEstimate();
		for (int val : prevSelected) {
			assertEquals(0, val);	
		}
	}

	/**
	 * Tests if the previous selected cards can be correctly determined with a multiple selection deck.
	 * Previous estimate is 2 of the cards
	 */
	@Test
	public void testMultiDeckCardsFromLastEstimateSomeSelected() {
		// Create Previous Estimate
		List<Integer> prevCards = new ArrayList<Integer>();
		prevCards.add(testDeckMultiSelect.getNumbersInDeck().get(3));
		prevCards.add(testDeckMultiSelect.getNumbersInDeck().get(6));
		int prevVote = 0;
		for (int val : prevCards) {
			prevVote += val;
		}
		Estimate prevEst = new Estimate();
		prevEst.setVote(prevVote);
		
		// Test if it can correctly detect the previous cards selected
		DeckVotingPanel deckPanel = new DeckVotingPanel(testDeckMultiSelect, prevEst);
		// Get previous selected cards
		List<Integer> prevSelected = deckPanel.cardsFromLastEstimate();
		for (int val : prevSelected) {
			assertTrue(prevCards.contains(val));
		}
	}
	
	/**
	 * Tests if the previous selected cards can be correctly determined with a single selection deck.
	 * Previous estimate is a single card
	 */
	@Test
	public void testSingleDeckCardsFromLastEstimateSomeSelected() {
		// Create Previous Estimate
		int prevVote= testDeckMultiSelect.getNumbersInDeck().get(6); 
		Estimate prevEst = new Estimate();
		prevEst.setVote(prevVote);
		
		// Test if it can correctly detect the previous cards selected
		DeckVotingPanel deckPanel = new DeckVotingPanel(testDeckSingleSelect, prevEst);
		// Get previous selected cards
		List<Integer> prevSelected = deckPanel.cardsFromLastEstimate();
		for (int val : prevSelected) {
			assertEquals(prevVote , val);
		}
	}
	
	/**
	 * Tests if the previous selected cards can be correctly determined with a multiple selection deck.
	 * Previous estimate = all cards
	 */
	@Test
	public void testMutliDeckCardsFromLastEstimateAllSelected() {
		// Create Previous Estimate
		int prevVote = 0;
		for (int val : testDeckMultiSelect.getNumbersInDeck()) {
				prevVote += val;
		}
		Estimate prevEst = new Estimate();
		prevEst.setVote(prevVote);
		
		// Test if it can correctly detect the previous cards selected
		DeckVotingPanel deckPanel = new DeckVotingPanel(testDeckSingleSelect, prevEst);
		// Get previous selected cards
		List<Integer> prevSelected = deckPanel.cardsFromLastEstimate();
		for (int val : prevSelected) {
			assertTrue(testDeckMultiSelect.getNumbersInDeck().contains(val));
		}
	}
	
	/**
	 * Tests if the panel correctly fires an event with no previous estimate input
	 */
	@Test
	public void testFireEstimateEventNoPrevEst() {
		DeckVotingPanel deckPanel = new DeckVotingPanel(testDeckSingleSelect);
		
		EstimateListener eList = new EstimateListener();
		deckPanel.addEstimateListener(eList);
		
		deckPanel.fireEstimateEvent();
		assertEquals(0, eList.getEstimate());
	}
	
	/**
	 * Tests if the panel correctly fires an event with a previous estimate input
	 */
	@Test
	public void testFireEstimateEventWithPrevEst() {
		Estimate prevEst = new Estimate();
		prevEst.setVote(5);
		
		final DeckVotingPanel deckPanel = new DeckVotingPanel(null, prevEst);
		
		EstimateListener eList = new EstimateListener();
		deckPanel.addEstimateListener(eList);
		
		deckPanel.fireEstimateEvent();
		assertEquals(5, eList.getEstimate());
		
		// Remove EstimateListener
		eList.estimateSubmitted(new EstimateEvent(this, 0));
		deckPanel.removeEstimateListener(eList);
		deckPanel.fireEstimateEvent();
		assertEquals(0, eList.getEstimate());
	}

}
