package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.voting;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;

import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.Deck;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.Estimate;

public class DeckVotingPanelTest {
	private Deck testDeck;

	public DeckVotingPanelTest() {
		// Create deck for testing purposes
		List<Integer> cards = new ArrayList<Integer>();
		for (int i = 0; i < 14; i += 2) {
			cards.add(i);
		}
		testDeck = new Deck(cards, true);
	}

	@Test
	public void testCardSelection() {
		DeckVotingPanel deckPanel = new DeckVotingPanel(testDeck);
		
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
	
	@Test
	public void testClearSelectedCardsOnNoneSelected() {
		DeckVotingPanel deckPanel = new DeckVotingPanel(testDeck);
		
		// Check clearSelectedCards on an empty deck
		deckPanel.clearSelectedCards();
		for (JButton card : deckPanel.getCardButtons()) {
			assertFalse(deckPanel.isCardSelected(card));
		}
	}
	
	@Test
	public void testClearSelectedCardsOnAllSelected() {
		DeckVotingPanel deckPanel = new DeckVotingPanel(testDeck);
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
	
	@Test
	public void testClearSelectedCardsOnSomeSelected() {
		DeckVotingPanel deckPanel = new DeckVotingPanel(testDeck);
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
	
	@Test
	public void testGetEstimateNoneSelected() {
		int estimate = 0;
		
		DeckVotingPanel deckPanel = new DeckVotingPanel(testDeck);
		deckPanel.clearSelectedCards();
		for (JButton card : deckPanel.getCardButtons()) {
			deckPanel.updateEstimate(card);
		}
		assertEquals(estimate, deckPanel.getEstimate());
		
	}
	
	@Test
	public void testGetEstimateSomeSelected() {	
		int estimate = 0;
		int selectCount = 0;
		
		DeckVotingPanel deckPanel = new DeckVotingPanel(testDeck);
		// Select some cards
		for (JButton card : deckPanel.getCardButtons()) {
			if (selectCount % 4 == 0) {
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
	
	@Test
	public void testGetEstimateAllSelected() {	
		int estimate = 0;
		
		DeckVotingPanel deckPanel = new DeckVotingPanel(testDeck);
		// Select some cards
		for (JButton card : deckPanel.getCardButtons()) {
			deckPanel.setCardSelected(card, true);
			deckPanel.updateEstimate(card);
			estimate += Integer.parseInt(card.getName());
		}
		assertEquals(estimate, deckPanel.getEstimate());
	}

	@Test
	public void testCardsFromLastEstimateZero() {
		Estimate prevEst = new Estimate();
		prevEst.setVote(0);
		DeckVotingPanel deckPanel = new DeckVotingPanel(testDeck, prevEst);
		// Get previous selected cards
		List<Integer> prevSelected= deckPanel.cardsFromLastEstimate();
		for (int val : prevSelected) {
			assertEquals(0, val);	
		}
	}


}
