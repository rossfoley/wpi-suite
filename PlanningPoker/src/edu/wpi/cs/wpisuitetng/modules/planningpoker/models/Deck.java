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

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.modules.AbstractModel;

public class Deck extends AbstractModel {
	private String deckName;
	private List<Integer> numbersInDeck = new ArrayList<Integer>();
	private boolean allowMultipleSelections;
	private int id;
	
	/** constructor for Decks that takes in a list to use as the deck
	 * 
	 * @param listOfCards numbers to use in the new deck
	 */
	public Deck(List<Integer> listOfCards, boolean allowMultipleSelections) {
		numbersInDeck = listOfCards;
		this.allowMultipleSelections = allowMultipleSelections;
		deckName = autoName();
	}
	/**
	 * constructor for decks that does not have a list given at initialization
	 * 
	 * deckName is set to "" as a default
	 */
	public Deck(){
		deckName = autoName();
		allowMultipleSelections = false;
	}
	
	
	/**
	 * @return the deckName
	 */
	public String getDeckName() {
		return deckName;
	}

	/**
	 * @param deckName the deckName to set
	 */
	public void setDeckName(String deckName) {
		this.deckName = deckName;
	}
	
	/** 
	 * converts deck object to a JSON
	 * @see edu.wpi.cs.wpisuitetng.modules.Model#toJSON()
	 */
	public String toJSON() {
		return new Gson().toJson(this, Deck.class);
	}
	
	/** parses a deck object from a Json string
	 * 
	 * @param json json-encoded deck 
	 * @return Deck object that was encoded in json string
	 */
	public static Deck fromJson(String json) {
		final Gson parser = new Gson();
		return parser.fromJson(json, Deck.class);
	}
	
	/**
	 * Returns an array of Deck parsed from the given JSON-encoded
	 * string.
	 * 
	 * @param json
	 *            string containing a JSON-encoded array of Deck
	
	 * @return an array of Deck deserialized from the given JSON string */
	public static Deck[] fromJsonArray(String json) {
		final Gson parser = new Gson();
		return parser.fromJson(json, Deck[].class);
	}

	@Override
	public void save() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Boolean identify(Object o) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	/** 
	 * 
	 * @return string representing the numbers in the deck separated by a comma and a space
	 */
	public String changeNumbersToString(){
		String stringOfDeckNums = "";
		int loopProgress = 0;
		final int listLength = numbersInDeck.size();
		for (Integer n:numbersInDeck){
			stringOfDeckNums += n.toString();
			// if not at last element in the list, add a comma and a space after the number
			if (loopProgress != (listLength-1)){
				stringOfDeckNums += ", ";
			}
				
			loopProgress ++;
		}
		return stringOfDeckNums;
		
	}
	
	/**
	 * @return the numbersInDeck
	 */
	public List<Integer> getNumbersInDeck() {
		return numbersInDeck;
	}

	/**
	 * @param numbersInDeck the numbersInDeck to set
	 */
	public void setNumbersInDeck(List<Integer> numbersInDeck) {
		this.numbersInDeck = numbersInDeck;
	}
	
	/**
	 * @param allowMultipleSelections	If multiple cards can be selected when voting
	 */
	public void setAllowMultipleSelections(boolean allowMultipleSelections) {
		this.allowMultipleSelections = allowMultipleSelections;
	}
	
	/**
	 * @return	If multiple cards can be selected when voting
	 */
	public boolean getAllowMultipleSelections() {
		return allowMultipleSelections;
	}
	

	/**
	 * @return the iD
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	
	/**
	 * Copies traits of another deck to this deck 
	 * used to update decks in the database
	 * 
	 * @param toCopyFrom deck to copy attributes from
	 */
	public void copyFrom(Deck toCopyFrom) {
		id = toCopyFrom.getId();
		numbersInDeck = toCopyFrom.getNumbersInDeck();
		allowMultipleSelections = toCopyFrom.getAllowMultipleSelections();
		deckName = toCopyFrom.getDeckName();
	}
	
	/**
	 * returns the default name for a deck (which is Deck followed by a space and then it's id number
	 * 
	 * @return string containing the default name for a deck
	 */
	public String autoName(){
		final String newDeckName = "Deck " + Integer.toString(id);
		return newDeckName;
	}	
	
	/**
	 * Equals operator for decks
	 * 
	 * @see java.lang.Object.equals
	 */
	@Override
	public boolean equals(Object obj){
		if (obj == null){
			return false;
		}
		if (obj == this){
			return true;
		}
		if (!(obj instanceof Deck)){
			return false;
		}
		final Deck rhs = (Deck) obj;
		return ((deckName.equals(rhs.deckName)) && (id == rhs.id));
		
	}

}
