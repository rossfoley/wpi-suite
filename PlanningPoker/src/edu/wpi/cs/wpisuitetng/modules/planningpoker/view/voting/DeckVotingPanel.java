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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.Deck;

import javax.swing.BoxLayout;
import javax.swing.JButton;

import javax.swing.SwingConstants;

import java.awt.Font;
import java.awt.BorderLayout;


/**
 * This class is a sub-panel for the voting panel which displays the cards
 * for voting on a planning poker session or allows the user to enter a number 
 * depending on what was specified by the creator of the session. 
 * 
 * @version $Revision: 1.0 $
 */
public class DeckVotingPanel extends JPanel
							 implements PropertyChangeListener,
							 			MouseMotionListener {
	private Deck votingDeck;
	private JFormattedTextField estimateField;
	double userEstimate;
	private JLayeredPane layeredDeckPane;
	private JButton estimateButton;

	/**
	 * Constructor for DeckVotingPanel when using a deck
	 * @param votingDeck the deck to use when voting 
	 */
	public DeckVotingPanel(Deck votingDeck) {
		this.votingDeck = votingDeck;
		// Make sure a deck was input
		if (votingDeck == null) {
			this.buildDefaultVotingPanel();	
		}
		else {
			this.buildDeckVotingPanel();
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
		estimateField.setFont(new Font("Tahoma", Font.PLAIN, 17));
		estimateField.setToolTipText("Enter Estimation Here");
		estimateField.setValue(new Double(0));
		estimateField.setColumns(1);
		estimateField.setSize(new Dimension(26, 26));
		estimateField.addPropertyChangeListener("value", this);
		// Create submission button
		estimateButton = new JButton("Submit Estimation");
		estimateButton.setPreferredSize(new Dimension(50, 26));
		estimateButton.setVerticalAlignment(SwingConstants.BOTTOM);
		this.setLayout(new BorderLayout(0, 0));
		// Add Label for estimation number
		JLabel estimateLabel = new JLabel("Estimation for Requirement: ");
		estimateLabel.setLabelFor(estimateField);
		this.add(estimateLabel, BorderLayout.WEST);
		this.add(estimateField, BorderLayout.CENTER);
		this.add(estimateButton, BorderLayout.SOUTH);
	}

	/**
	 * Builds a deck based voting panel 
	 */
	private void buildDeckVotingPanel() {
		List<Integer> numbersInDeck = this.votingDeck.getNumbersInDeck();

		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		//Create and set up the layered pane.
		layeredDeckPane = new JLayeredPane();
		//layeredDeckPane.setPreferredSize(new Dimension(300, 310));
		layeredDeckPane.setBorder(BorderFactory.createTitledBorder(
				"Select Cards For Your Estimate: "));
		layeredDeckPane.addMouseMotionListener(this);

		//This is the origin of the first label added.
		Point origin = new Point(10, 20);

		//This is the offset for computing the origin for the next label.
		int offset = 40;

		//Add several overlapping, colored labels to the layered pane
		//using absolute positioning/sizing.
		for (int i = 0; i < numbersInDeck.size(); i++) {
			JButton label = createCardLabel(numbersInDeck.get(i), origin);
			layeredDeckPane.add(label, new Integer(i));
			origin.x += offset;
			//origin.y += offset;
		}
		// Create submission button
		estimateButton = new JButton("Submit Estimation");
		
		//Add control pane and layered pane to this JPanel.
		this.add(layeredDeckPane);
		this.add(estimateButton);
	}

	//Create and set up a colored label.
	private JButton createCardLabel(int cardValue, Point origin) {
		JButton card = new JButton("\t" + String.valueOf(cardValue));
		card.setVerticalAlignment(JLabel.CENTER);
		card.setHorizontalAlignment(JLabel.LEFT);
		card.setOpaque(true);
		card.setBorder(BorderFactory.createLineBorder(Color.black));
		card.setBounds(origin.x, origin.y, 140, 140);
		card.setBackground(Color.WHITE);
		// TODO use actual cards instead of a white background
		/*
		try {
		    Image img = ImageIO.read(
		    		new File("../PlanningPoker/src/edu/wpi/cs/wpisuitetng/modules/planningpoker/view/voting/card.png"));
		    		//getClass().getResource("new_req.png"));	// this should work... but doesn't...
		    card.setIcon(new ImageIcon(img));
		} catch (IOException | NullPointerException | IllegalArgumentException ex) {};
		*/
		return card;
	}

	//Make Duke follow the cursor.
	public void mouseMoved(MouseEvent e) {
		System.out.println("Mouse Position - X: " + e.getX() + " Y: " + e.getY());
	}

	public void mouseDragged(MouseEvent e) {
		// TODO Could use this to drag and select multiple cards
	} //do nothing right now


	/**
	 * 
	 * @return the user's selected/entered estimate
	 */
	public double getEstimate() {
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
