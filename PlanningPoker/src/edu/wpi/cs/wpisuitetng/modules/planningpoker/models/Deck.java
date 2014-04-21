/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.planningpoker.models;

/**
 * @author amandaadkins
 *
 */

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.modules.AbstractModel;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;

public class Deck extends AbstractModel {
	private String deckName;
	private ArrayList<Integer> numbersInDeck = new ArrayList<Integer>();
	private boolean allowMultipleSelections;
	private int id;
	
	/** constructor for Decks that takes in a list to use as the deck
	 * 
	 * @param listOfCards numbers to use in the new deck
	 */
	public Deck(ArrayList<Integer> listOfCards, boolean allowMultipleSelections) {
		this.numbersInDeck = listOfCards;
		this.allowMultipleSelections = allowMultipleSelections;
		this.deckName = autoName();
	}
	/**
	 * constructor for decks that does not have a list given at initialization
	 * 
	 * deckName is set to "" as a default
	 */
	public Deck(){
		this.deckName = autoName();
		this.allowMultipleSelections = false;
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
		int listLength = numbersInDeck.size();
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
	public ArrayList<Integer> getNumbersInDeck() {
		return numbersInDeck;
	}

	/**
	 * @param numbersInDeck the numbersInDeck to set
	 */
	public void setNumbersInDeck(ArrayList<Integer> numbersInDeck) {
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
		return this.allowMultipleSelections;
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
		this.id = toCopyFrom.getId();
		this.numbersInDeck = toCopyFrom.getNumbersInDeck();
		this.allowMultipleSelections = toCopyFrom.getAllowMultipleSelections();
		this.deckName = toCopyFrom.getDeckName();
	}
	
	/**
	 * returns the default name for a deck (which is Deck followed by a space and then it's id number
	 * 
	 * @return string containing the default name for a deck
	 */
	public String autoName(){
		String newDeckName = "Deck " + Integer.toString(this.id);
		return newDeckName;
	}	

}
