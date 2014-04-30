/*******************************************************************************
 * Copyright (c) 2014 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Team8s
 ******************************************************************************/
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
		newDeck = e.getDeck();
	}

	public Deck getDeck() {
		return newDeck;
	}
}
