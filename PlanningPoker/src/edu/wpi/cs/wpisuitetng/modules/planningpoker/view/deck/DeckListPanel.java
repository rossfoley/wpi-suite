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

import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.Deck;


/**
 * Side table for deck selection in the deck viewer tab
 *
 */
public class DeckListPanel extends JScrollPane
						   implements ListSelectionListener {
	
	private JList<String> deckList;
	private DefaultListModel<String> listModel;

	private final List<Deck> decks;
	private transient Vector<DeckListener> deckListeners;
	private DeckViewer parent;

	public DeckListPanel(List<Deck> decks, DeckViewer parent) {		
		setName("Deck List Scroll Panel");
		this.decks = decks;
		this.parent = parent;

		// Add deck names to the list model
		listModel = new DefaultListModel<>();
		for (Deck el : decks) {
			listModel.addElement(el.getDeckName());
		}

		//Create the list and put it in a scroll pane.
		deckList = new JList<>(listModel);
		deckList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		deckList.setSelectedIndex(0);
		deckList.addListSelectionListener(this);
		deckList.setVisibleRowCount(5);
		setViewportView(deckList);
	}

	synchronized public void addDeckListener(DeckListener l) {
		if (deckListeners == null) {
			deckListeners = new Vector<DeckListener>();
		}
		deckListeners.addElement(l);
	}  

	/** Remove a listener for EstimateEvents */
	synchronized public void removeDeckListener(DeckListener l) {
		if (deckListeners == null) {
			deckListeners = new Vector<DeckListener>();
		}
		else {
			deckListeners.removeElement(l);
		}
	}

	protected void fireDeckSelectionEvent(Deck deck) {
		// if we have no listeners, do nothing...
		if (deckListeners != null && !deckListeners.isEmpty()) {
			// create the event object to send
			final DeckEvent event = 
					new DeckEvent(this, deck);

			// make a copy of the listener list in case
			//   anyone adds/removes listeners
			final Vector<DeckListener> targets;
			synchronized (this) {
				targets = (Vector<DeckListener>) deckListeners.clone();
			}

			// walk through the listener list and
			//   call the sunMoved method in each
			final Enumeration e = targets.elements();
			while (e.hasMoreElements()) {
				DeckListener l = (DeckListener) e.nextElement();
				l.deckSubmitted(event);
			}
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
        if ((e.getValueIsAdjusting() == false) && (deckList.getSelectedIndex() >= 0)) {
        	parent.setLastDeck(decks.get(deckList.getSelectedIndex()));
        	fireDeckSelectionEvent(decks.get(deckList.getSelectedIndex()));
        }
	}
	
	public void setSelectedDeck(Deck deck) {
		int pos = decks.indexOf(deck);
		deckList.setSelectedIndex(pos);
	}
	
}
