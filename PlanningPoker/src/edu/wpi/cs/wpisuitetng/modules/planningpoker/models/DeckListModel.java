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
	private static DeckListModel instance;
	
	private DeckListModel(){
		existingDecks = new ArrayList<Deck>();
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
	
	public void addDeck(Deck deck){
		existingDecks.add(deck);
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
	
	

}
