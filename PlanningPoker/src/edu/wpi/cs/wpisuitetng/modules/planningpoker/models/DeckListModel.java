/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.planningpoker.models;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.controller.AddDeckController;

/**
 * This class manages all decks and keeps track of a list of all of them
 *
 */
public class DeckListModel extends AbstractListModel {
	
	/** 
	 * The list in which all the decks for a project are contained
	 */
	private final List<Deck> existingDecks;
	private int nextID; // the next available id for a deck
	
	private static DeckListModel instance; // static object to allow the decklistmodel 
	
	/**
	 * Constructs an list of decks for the project that contains only the default deck
	 */
	private DeckListModel(){
		existingDecks = new ArrayList<Deck>();
		nextID = 0;
	}

	/** 
	 * if the decklistmodel has not yet been created, create it
	 * @return the instance of the decklistmodel 
	 */
	public static DeckListModel getInstance(){
		if (instance == null){
			instance = new DeckListModel();
		}
		return instance;
	}

	@Override
	public int getSize() {
		return existingDecks.size();
	}

	// revisit this
	@Override
	public Object getElementAt(int index) {
		return existingDecks.get(existingDecks.size() - 1 - index);
	}
	
	public void addDeck(Deck newDeck){
		
		newDeck.setId(getNextID());
		if (newDeck.getDeckName().equals("")){
			newDeck.setDeckName(newDeck.autoName());
		}
		existingDecks.add(newDeck);
		try {
			AddDeckController.getInstance().addDeck(newDeck);	
		}
		catch (Exception e){
			
		}
	}
	
	/**
	 * Finds a deck with the deck numbers matching the given list
	 * 
	 * @param deckNums list of numbers to match to decks in decklistmodel
	 * @return the deck whose numbers match deckNums
	 */
	public Deck getDeck(List<Integer> deckNums){
		for (Deck d:existingDecks){
			if (d.getNumbersInDeck().equals(deckNums)){
				return d;
			}
		}
		return null;
	}
	
	/**
	 * @param deckID the ID of the deck to search for
	 * @return the deck with the specified ID
	 */
	public Deck getDeck(int deckID) {
		for (Deck deck : existingDecks){
			if (deck.getId() == deckID) {
				return deck;
			}
		}
		return null;
	}
	
	/**
	 * @return	The next Deck ID to use
	 */
	public int getNextID(){
		int id = 0;
		for (Deck d: existingDecks) {
			if (id <= d.getId())
				id = d.getId();
		}
		return ++id;
	}
	
	/**
	 * Adds the given array of decks to the list
	 * 
	 * @param decks the array of decks to add
	 */
	public void addDecks(Deck[] decks) {
		for (int i = 0; i < decks.length; i++) {
			existingDecks.add(decks[i]);
		}
	}
	
	/** 
	 * creates the default deck
	 * @return defaultDeck deck containing the default numbers
	 */
	public Deck makeDefaultDeck(){
		final ArrayList<Integer> defaultDeckNums = new ArrayList<Integer>();
		defaultDeckNums.add(0);
		defaultDeckNums.add(1);
		defaultDeckNums.add(1);
		defaultDeckNums.add(2);
		defaultDeckNums.add(3);
		defaultDeckNums.add(5);
		defaultDeckNums.add(8);
		defaultDeckNums.add(13);
		final Deck defaultDeck = new Deck(defaultDeckNums, true);
		defaultDeck.setDeckName("Default"); 
		return defaultDeck;
	}
	
	
	/**
	 * empties the decklistnum
	 */
	public void emptyModel(){
		existingDecks.clear();
	}
	
	/**
	 * 
	 * @return list of decks stored in the deckListModel
	 */
	public List<Deck> getDecks() {
		return existingDecks;
	}
	
	
}
