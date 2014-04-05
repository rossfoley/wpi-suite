/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.planningpoker.models;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;

/**
 * @author mandi1267
 *
 */
public class DeckListModel extends AbstractListModel {
	private List<Deck> existingDecks;
	private int nextID;
	
	private static DeckListModel instance;
	
	private DeckListModel(){
		existingDecks = new ArrayList<Deck>();
		this.nextID = 0;
		// add Default Deck
		Deck defaultDeck = makeDefaultDeck();
		addDeck(defaultDeck);
	}
	
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
		catch {
			
		}
	}
	
	public Deck getDeck(ArrayList<Integer> deckNums){
		for (Deck d:existingDecks){
			if (d.getNumbersInDeck().equals(deckNums)){
				return d;
			}
		}
		System.out.println("deck not found");
		return null;
	}
	
	public int getNextID(){
		return this.nextID++;
	}
	
	public Deck makeDefaultDeck(){
		ArrayList<Integer> defaultDeckNums = new ArrayList<Integer>();
		defaultDeckNums.add(1);
		defaultDeckNums.add(1);
		defaultDeckNums.add(2);
		defaultDeckNums.add(3);
		defaultDeckNums.add(5);
		defaultDeckNums.add(8);
		defaultDeckNums.add(13);
		Deck defaultDeck = new Deck(defaultDeckNums);
		defaultDeck.setDeckName("Default"); 
		return defaultDeck;
	}
}
