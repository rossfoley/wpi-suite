package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.deck;

import java.util.EventListener;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.Deck;

/** 
 * A contract between a DeckEvent source and listener classes
 */
public class DeckListener implements EventListener {
	private Deck newDeck;

	/**
	 *  Called whenever a deck has been submitted by an
	 *  DeckEvent source object 
	 */
	public void deckSubmitted(DeckEvent e) {
		this.newDeck= e.getDeck();
	}

	public Deck getDeck() {
		return this.newDeck;
	}

}
