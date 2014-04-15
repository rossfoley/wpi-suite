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
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

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
	private List<JButton> listOfCardButtons;
	private Integer lastCard = -1;
	private JLabel estimateFieldErrorMessage = new JLabel("");
	private transient Vector<EstimateListener> listeners;

	/**
	 * Constructor for DeckVotingPanel when using a deck
	 * @param votingDeck the deck to use when voting 
	 */
	public DeckVotingPanel(Deck votingDeck) {
		this.votingDeck = votingDeck;
		// Make sure a deck was input
		if (votingDeck == null) {
			buildDefaultVotingPanel();	
		}
		else {
			buildDeckVotingPanel();
		}
	}

	/**
	 * Constructor for DeckVotingPanel when not using a deck
	 */
	public DeckVotingPanel() {
		votingDeck = null;
		buildDefaultVotingPanel();
	}


/**
 * Builds a voting panel where the user inputs a number for their vote 
 */
private void buildDefaultVotingPanel() {
		estimateFieldErrorMessage.setForeground(Color.RED); 

		// Create the text field for the estimation number
		NumberFormat estimateFormat = NumberFormat.getNumberInstance();
		estimateField = new JFormattedTextField(estimateFormat);
		estimateField.setHorizontalAlignment(SwingConstants.CENTER);
		estimateField.setFont(new Font("Tahoma", Font.PLAIN, 50));
		estimateField.setToolTipText("Enter Estimation Here");
		estimateField.setValue(new Double(0));
		estimateField.addPropertyChangeListener("value", this);

		estimateField.setPreferredSize(new Dimension(200, 100));

		submitButton = new JButton("Submit Estimation");
		submitButton.setPreferredSize(new Dimension(50, 26));
		submitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
					fireEstimateEvent();
				}
		});

		// Setup error message display
		estimateFieldErrorMessage.setForeground(Color.RED);

		// Add Label for estimation number
		JLabel estimateLabel = new JLabel("Estimation for Requirement: ");
		estimateLabel.setHorizontalAlignment(SwingConstants.CENTER);
		estimateLabel.setLabelFor(estimateField);
		setLayout(new BorderLayout(10, 10));

		JPanel subPanel = new JPanel();
		subPanel.setPreferredSize(new Dimension(400, 100));
		SpringLayout subPanelLayout = new SpringLayout();
		subPanel.setLayout(subPanelLayout);
		
		subPanelLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, estimateLabel, 0, SpringLayout.HORIZONTAL_CENTER, subPanel);
		subPanelLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, estimateField, 0, SpringLayout.HORIZONTAL_CENTER, subPanel);
		subPanelLayout.putConstraint(SpringLayout.NORTH, estimateLabel, 10, SpringLayout.NORTH, subPanel);
		subPanelLayout.putConstraint(SpringLayout.NORTH, estimateField, 10, SpringLayout.SOUTH, estimateLabel);
		subPanelLayout.putConstraint(SpringLayout.SOUTH, estimateField, -10, SpringLayout.NORTH, submitButton);
		subPanelLayout.putConstraint(SpringLayout.WEST, submitButton, -70, SpringLayout.HORIZONTAL_CENTER, subPanel);
		subPanelLayout.putConstraint(SpringLayout.EAST, submitButton, 70, SpringLayout.HORIZONTAL_CENTER, subPanel);
		subPanelLayout.putConstraint(SpringLayout.SOUTH, submitButton, -10, SpringLayout.SOUTH, subPanel);
		
		subPanel.add(submitButton);
		subPanel.add(estimateLabel);
		subPanel.add(estimateField);

		add(estimateFieldErrorMessage, BorderLayout.EAST);
		add(subPanel, BorderLayout.CENTER);				
	}


	/**
	 * Builds a deck based voting panel 
	 */
	private void buildDeckVotingPanel() {
		List<Integer> numbersInDeck = votingDeck.getNumbersInDeck();

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		//Create and set up the layered pane.
		layeredDeckPane = new JLayeredPane();
		layeredDeckPane.addMouseMotionListener(this);
		layeredDeckPane.setBorder(BorderFactory.createTitledBorder(
				"Select Cards For Your Estimate: "));

		//This is the origin of the first label added.
		Point origin = new Point(10, 20);
		cardOffset = 700/numbersInDeck.size();

		//Add several overlapping, card buttons to the layered pane
		//using absolute positioning/sizing.
		listOfCardButtons = new ArrayList<JButton>();
		highlightCard(-1);
		for (int i = 0; i < numbersInDeck.size(); i++) {
			JButton cardButton = createCardButtons(numbersInDeck.get(i), origin);
			layeredDeckPane.add(cardButton, new Integer(i));
			listOfCardButtons.add(cardButton);
			origin.x += cardOffset;
			//origin.y += offset;
		}
		// Create submission button
		submitButton = new JButton("Submit Estimation");
		submitButton.setAlignmentX(CENTER_ALIGNMENT);
		submitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fireEstimateEvent();
			}
		});

		//Add control pane and layered pane to this JPanel.
		add(layeredDeckPane);
		add(submitButton);
	}

	// Create and set up card button
	private JButton createCardButtons(int cardValue, Point origin) {
		final JButton card = new JButton();
		// Try to load the corresponding playing card
		try {
			String fileName = new String("../PlanningPoker/src/edu/wpi/cs/wpisuitetng/modules/planningpoker/view/voting/cards/" + 
					Integer.toString(cardValue) + "-of-Diamonds.png");

			Image img = ImageIO.read(new File(fileName));
			//getClass().getResource("new_req.png"));	// this should work... but doesn't...
			card.setIcon(new ImageIcon(img.getScaledInstance(112, 140, 0)));
		} catch (IOException | NullPointerException | IllegalArgumentException ex) {
			card.setText("\t  " + Integer.toString(cardValue));
		};
		card.setName(String.valueOf(cardValue));
		card.setVerticalAlignment(JLabel.CENTER);
		card.setHorizontalAlignment(JLabel.LEFT);
		card.setOpaque(true);
		card.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		card.setBackground(Color.WHITE);
		card.setBounds(origin.x, origin.y, 112, 140);
		card.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Change color based on previous state
				if (card.getBackground() == Color.WHITE) {
					card.setBackground(Color.GREEN);	// card is part of estimate
					card.setBorder(BorderFactory.createLineBorder(Color.GREEN, 4));	
				}
				else {
					card.setBackground(Color.WHITE); // card is not part of estimate
					card.setBorder(BorderFactory.createLineBorder(Color.BLACK));
				}
				updateEstimate(card.getName(), card.getBackground());
			}
		});
		card.addMouseMotionListener(this);	// To track mouse movements and dragging
		
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
		// If the source was not a card, no card should be highlighted 
		if (!listOfCardButtons.contains(e.getSource())) {
			// Check if near the top edge of a highlighted card
			if ((e.getX() < (cardOffset*listOfCardButtons.size() + 50)) 
					&& (e.getX() >= 10) && (e.getY() >= 20) && (e.getY() <= 60)) {
				return;
			}
			else if (lastCard == -1) {
				return;
			}
			else {
				lastCard = -1;	// Make the last card highlighted invalid
			}
		}
		// If the source of the mouseMoved event was a card, highlight it
		else {
			JButton currentCard = (JButton) e.getSource();
			// Find the index of the card
			for (int i = 0; i < listOfCardButtons.size(); i++) {
				if (currentCard == listOfCardButtons.get(i)) {
					// Only handle the event if the mouse moved to a new card
					if (lastCard != i) {
						// Check the location of the mouse wrt the cards origin
						if ((e.getX() < cardOffset) || (i == listOfCardButtons.size() - 1)) {
							lastCard = i;						
						}
						// If the mouse is on the right side of the card, highlight the next card
						else {
							lastCard = i + 1;
						}
						break;
					}
					return;	// if this card is already highlighted
				}
			}
		}
		
		highlightCard(lastCard);
	}

	private void highlightCard(int card_index) {
		layeredDeckPane.removeAll();
		//This is the origin of the first label added.
		Point origin = new Point(10, 20);

		// Update card buttons in panel to highlight the moused-over card
		for (int i = 0; i < listOfCardButtons.size(); i++) {
			if (i == card_index) {
				origin.y += 30;
			}
			int cardValue = Integer.parseInt(listOfCardButtons.get(i).getName());
			JButton cardButton = createCardButtons(cardValue, origin);
			// Keep track of if the user pressed the button already
			if (listOfCardButtons.get(i).getBackground() != Color.WHITE) {
				cardButton.setBorder(BorderFactory.createLineBorder(Color.GREEN, 4));
				cardButton.setBackground(Color.GREEN);	
			}
			listOfCardButtons.set(i, cardButton);
			// Set this card as the top cad if moused-over
			if (i == card_index) {
				layeredDeckPane.add(cardButton, new Integer(this.listOfCardButtons.size() + 1));
				origin.y -= 30;
			}
			else {
				layeredDeckPane.add(cardButton, new Integer(i));
			}
			origin.x += cardOffset;
		}
		// Refresh panes
		layeredDeckPane.revalidate();
		layeredDeckPane.repaint();
		revalidate();
		repaint();
		
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
	 * @return returns true if the estimate is valid (i.e. non-negative) 
	 */
	public boolean validateEstimate() {
		if (userEstimate < 0) {
			userEstimate = 0;
			estimateFieldErrorMessage.setText("Please enter a positive number");
			estimateField.setValue(new Double(0));
			estimateFieldErrorMessage.revalidate();
			estimateFieldErrorMessage.repaint();
			revalidate();
			repaint();
			return false;
		}
		estimateFieldErrorMessage.setText("");
		estimateFieldErrorMessage.revalidate();
		estimateFieldErrorMessage.repaint();
		revalidate();
		repaint();
		return true;
	}

	/**
	 * Listens for changes in the button properties and handles these events
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		Object source = evt.getSource();
		if (source == estimateField) {
			 userEstimate = ((Number) estimateField.getValue()).doubleValue();
			 validateEstimate();
		}
	}


	/**
	 * Register a listener for EstimateEvents
	 */

	synchronized public void addEstimateListener(EstimateListener l) {
		if (listeners == null) {
			listeners = new Vector<EstimateListener>();
		}
		listeners.addElement(l);
	}  

	/**
	 * Remove a listener for EstimateEvents
	 */
	synchronized public void removeEstimateListener(EstimateListener l) {
		if (listeners == null) {
			listeners = new Vector<EstimateListener>();
		}
		else {
			listeners.removeElement(l);
		}
	}

	/**
	 * Fire an EstimateEvent to all registered listeners
	 */
	protected void fireEstimateEvent() {
		// Do nothing if we have no listeners
		if (listeners != null && !listeners.isEmpty()) {
			// Create the event object to send
			EstimateEvent event = 
					new EstimateEvent(this, this.getEstimate());

			// Make a copy of the listener list in case anyone adds/removes listeners
			Vector<EstimateListener> targets;
			synchronized (this) {
				targets = (Vector<EstimateListener>) listeners.clone();
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
