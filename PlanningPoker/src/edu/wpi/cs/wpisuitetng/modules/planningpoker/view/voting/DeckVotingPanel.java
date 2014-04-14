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
import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.Deck;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.voting.EstimateListener;

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
							 			MouseMotionListener,
							 			Serializable {
	private Deck votingDeck;
	private JFormattedTextField estimateField;
	private double userEstimate;
	private JLayeredPane layeredDeckPane;
	private JButton submitButton;
	private int cardOffset = 40; //This is the offset for computing the origin for the next label.
	private transient Vector<EstimateListener> listeners;

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
		this.setPreferredSize(new Dimension(100, 100));
		this.setMaximumSize(new Dimension(100, 100));
		// Create the text field for the estimation number
		NumberFormat estimateFormat = NumberFormat.getNumberInstance();
		estimateField = new JFormattedTextField(estimateFormat);
		estimateField.setFont(new Font("Tahoma", Font.PLAIN, 17));
		estimateField.setToolTipText("Enter Estimation Here");
		estimateField.setValue(new Double(0));
		estimateField.setPreferredSize(new Dimension(26, 26));
		estimateField.addPropertyChangeListener("value", this);
		// Create submission button
		submitButton = new JButton("Submit Estimation");
		submitButton.setPreferredSize(new Dimension(26, 26));
		submitButton.setVerticalAlignment(SwingConstants.BOTTOM);
		submitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fireEstimateEvent();
			}
		});
		this.setLayout(new BorderLayout(0, 0));
		// Add Label for estimation number
		JLabel estimateLabel = new JLabel("Estimation for Requirement: ");
		estimateLabel.setLabelFor(estimateField);
		this.add(estimateLabel, BorderLayout.WEST);
		this.add(estimateField, BorderLayout.CENTER);
		this.add(submitButton, BorderLayout.SOUTH);
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
		Point origin = new Point(0, 0);

		//Add several overlapping, colored labels to the layered pane
		//using absolute positioning/sizing.
		for (int i = 0; i < numbersInDeck.size(); i++) {
			JButton cardButton = createCardButtons(numbersInDeck.get(i), origin);
			layeredDeckPane.add(cardButton, new Integer(i));
			origin.x += cardOffset;
			//origin.y += offset;
		}
		// Create submission button
		submitButton = new JButton("Submit Estimation");
		submitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fireEstimateEvent();
			}
		});

		//Add control pane and layered pane to this JPanel.
		this.add(layeredDeckPane);
		this.add(submitButton);
	}

	// Create and set up card button
	private JButton createCardButtons(int cardValue, Point origin) {
		final JButton card = new JButton("  " + String.valueOf(cardValue));
		card.setName(String.valueOf(cardValue));
		card.setVerticalAlignment(JLabel.CENTER);
		card.setHorizontalAlignment(JLabel.LEFT);
		card.setOpaque(true);
		card.setBorder(BorderFactory.createLineBorder(Color.black));
		card.setBounds(origin.x, origin.y, 140, 140);
		card.setBackground(Color.WHITE);
		card.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Change color based on previous state
				if (card.getBackground() == Color.WHITE) {
					card.setBackground(Color.GREEN);	// card is part of estimate
				}
				else {
					card.setBackground(Color.WHITE); // card is not part of estimate
				}
				updateEstimate(card.getName(), card.getBackground());
			}
		});
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

	/**
	 * Updates the users estimate on a card button click
	 * @param name	the name of the card button clicked
	 * @param background	the current color the card is set to
	 */
	private void updateEstimate(String name, Color background) {
		// If card was removed from estimate
		if (background == Color.WHITE) {
			userEstimate -= Integer.parseInt(name);
		}
		// If card was selected from estimate
		else {
			userEstimate += Integer.parseInt(name);
		}
	}

	public void mouseMoved(MouseEvent e) {
		// TODO use this to highlight the card that the mouse is hovering over 
		//System.out.println("Mouse Position - X: " + e.getX() + " Y: " + e.getY());
	}

	public void mouseDragged(MouseEvent e) {
		// TODO Could use this to drag and select multiple cards
	}

	/**
	 * @return the user's selected/entered estimate
	 */
	public double getEstimate() {
		return this.userEstimate;
	}

	/**
	 * Listens for changes in the button properties and handles these events
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		Object source = evt.getSource();
		if (source == estimateField) {
			this.userEstimate = ((Number) estimateField.getValue()).doubleValue();
		}
	}

	/**
	 * Register a listener for EstimateEvents
	 */
	synchronized public void addEstimateListener(EstimateListener l) {
		if (this.listeners == null) {
			this.listeners = new Vector<EstimateListener>();
		}
		this.listeners.addElement(l);
	}  

	/**
	 * Remove a listener for EstimateEvents
	 */
	synchronized public void removeEstimateListener(EstimateListener l) {
		if (this.listeners == null) {
			this.listeners = new Vector<EstimateListener>();
		}
		else {
			this.listeners.removeElement(l);
		}
	}

	/**
	 * Fire an EstimateEvent to all registered listeners
	 */
	protected void fireEstimateEvent() {
		// Do nothing if we have no listeners
		if (this.listeners != null && !this.listeners.isEmpty()) {
			// Create the event object to send
			EstimateEvent event = 
					new EstimateEvent(this, this.getEstimate());

			// Make a copy of the listener list in case anyone adds/removes listeners
			Vector<EstimateListener> targets;
			synchronized (this) {
				targets = (Vector<EstimateListener>) this.listeners.clone();
			}

			// Walk through the listener list and call the estimateSubmitted method in each
			Enumeration<EstimateListener> e = targets.elements();
			while (e.hasMoreElements()) {
				EstimateListener l = (EstimateListener) e.nextElement();
				l.estimateSubmitted(event);
			}
		}
	}
}
