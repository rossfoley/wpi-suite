package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.deck;

import java.util.EventObject;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.Deck;

public class DeckEvent extends EventObject {
	private Deck newDeck;

	public DeckEvent(Object source, Deck newDeck) {
		super(source);
		
		this.newDeck = newDeck;
	}
	
	public Deck getDeck() {
		return this.newDeck;
	}

}
