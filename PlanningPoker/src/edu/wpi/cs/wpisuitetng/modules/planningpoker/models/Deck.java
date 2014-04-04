/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.planningpoker.models;

/**
 * @author mandi1267
 *
 */

import java.util.ArrayList;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.modules.AbstractModel;

public class Deck extends AbstractModel {
	ArrayList<Integer> numbersInDeck = new ArrayList<Integer>();
	int iD;
	
	public Deck(ArrayList<Integer> numbersInDeck){
		this.numbersInDeck = numbersInDeck;
	}
	
	public String toJSON() {
		return new Gson().toJson(this, Deck.class);
	}
	
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
			// if not at last element in the list
			if (loopProgress != (listLength-1)){
				stringOfDeckNums += ", ";
			}
				
			loopProgress ++;
		}
		return stringOfDeckNums;
		
	}
}
