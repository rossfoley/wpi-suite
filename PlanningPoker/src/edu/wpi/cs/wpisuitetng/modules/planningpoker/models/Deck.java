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
	
	public Deck(){
		
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
	
	
	

}
