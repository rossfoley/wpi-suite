/*******************************************************************************
 * Copyright (c) 2014 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Team8s
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.voting;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.Deck;


/**
 * This class is a sub-panel for the voting panel which displays the cards
 * for voting on a planning poker session or allows the user to enter a number 
 * depending on what was specified by the creator of the session. 
 * 
 * @version $Revision: 1.0 $
 */
public class DeckVotingPanel extends JPanel implements PropertyChangeListener {
	Deck votingDeck;
	JFormattedTextField estimateField;
	double userEstimate;
	
	/**
	 * Constructor for DeckVotingPanel when using a deck
	 * @param votingDeck the deck to use when voting 
	 */
	public DeckVotingPanel(Deck votingDeck) {
		this.votingDeck = votingDeck;
		// Make sure a deck was input
		if (votingDeck == null) {
			this.buildDeckVotingPanel();	
		}
		else {
			this.buildDefaultVotingPanel();
		}
	}

	/**
	 * Constructor for DeckVotingPanel when not using a deck
	 * 
	 */
	public DeckVotingPanel() {
		this.votingDeck = null;
		
		this.buildDefaultVotingPanel();
	}


	/**
	 * Builds a voting panel where the user inputs a number for their vote 
	 */
	private void buildDefaultVotingPanel() {
		// Create the text field for the estimation number
		NumberFormat estimateFormat = NumberFormat.getNumberInstance();
		estimateField = new JFormattedTextField(estimateFormat);
		estimateField.setValue(new Double(0));
		estimateField.setColumns(10);
		estimateField.setSize(new Dimension(500, 200));
		estimateField.addPropertyChangeListener("value", this);
		// Add Label for estimation number
		JLabel estimateLabel = new JLabel("Estimation for Requirement: ");
		estimateLabel.setLabelFor(estimateField);
		
		this.setLayout(new GridLayout(0,1));
		this.add(estimateLabel);
		this.add(estimateField);
	}
	
	/**
	 * Builds a deck based voting panel 
	 */
	private void buildDeckVotingPanel() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 
	 * @return the user's selected/entered estimate
	 */
	public double getEstimate() {
		// TODO Auto-generated method stub
		return userEstimate;
	}

	/**
	 * Listens for changes in the button properties and handles these events
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		Object source = evt.getSource();
		if (source == estimateField) {
			userEstimate = (double) estimateField.getValue();
		}
	}

}
