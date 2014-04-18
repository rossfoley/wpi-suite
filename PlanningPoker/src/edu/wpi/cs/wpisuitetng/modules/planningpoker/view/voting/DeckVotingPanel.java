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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
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
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import edu.wpi.cs.wpisuitetng.janeway.config.ConfigManager;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.Deck;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.Estimate;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.voting.EstimateListener;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.ViewEventController;

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
	private Estimate prevEstimate;
	private JFormattedTextField estimateField;
	private double userEstimate;
	private JLayeredPane layeredDeckPane;
	private JButton submitButton;
	private int cardOffset = 40; //This is the offset for computing the origin for the next label.
	private List<JButton> listOfCardButtons;
	private Integer lastCard = -1;
	private JLabel estimateFieldErrorMessage = new JLabel("");
	private transient Vector<EstimateListener> listeners;
	private JLabel estimateSubmittedMessage = new JLabel("Your estimate has been submitted.");

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
	 * Constructor for DeckVotingPanel when using a deck and already voted on
	 * @param votingDeck the deck to use when voting 
	 */
	public DeckVotingPanel(Deck votingDeck, Estimate estimate) {
		this.votingDeck = votingDeck;
		// Check if a previous estimate was input
		this.prevEstimate = estimate;
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
	 * 	Builds a voting panel where the user inputs a number for their vote 
	 */
	private void buildDefaultVotingPanel() {
		estimateFieldErrorMessage.setForeground(Color.RED); 
		estimateSubmittedMessage.setForeground(Color.BLUE);
		estimateSubmittedMessage.setVisible(false);

		// Create the text field for the estimation number
		NumberFormat estimateFormat = NumberFormat.getNumberInstance();
		estimateField = new JFormattedTextField(estimateFormat);
		estimateField.setHorizontalAlignment(SwingConstants.CENTER);
		estimateField.setFont(new Font("Tahoma", Font.PLAIN, 50));
		estimateField.setToolTipText("Enter Estimation Here");
		estimateField.addPropertyChangeListener("value", this);

		estimateField.setPreferredSize(new Dimension(200, 100));
		
		estimateField.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				estimateSubmittedMessage.setVisible(false);
			}
		});
		
		// Set default values if this is the first vote
		if (prevEstimate.getVote() < 0) {
			estimateField.setValue(new Double(0));
			submitButton = new JButton("Submit Estimation");
		}
		else {	// set default values if this is a re-vote
			submitButton = new JButton("Resubmit Estimation");
			estimateField.setValue(new Double(prevEstimate.getVote()));
		}
		submitButton.setPreferredSize(new Dimension(50, 26));
		submitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (validateEstimate()){
					fireEstimateEvent();

				}
			}
		});

		// Setup error message display
		estimateFieldErrorMessage.setForeground(Color.RED);

		// Add Label for estimation number
		JLabel estimateLabel = new JLabel("Estimation for Requirement: ");
		estimateLabel.setHorizontalAlignment(SwingConstants.CENTER);
		estimateLabel.setLabelFor(estimateField);
		setLayout(new BorderLayout(10, 10));

		//JPanel subPanel = new JPanel();
		//subPanel.setPreferredSize(new Dimension(400, 100));
		SpringLayout thisLayout = new SpringLayout();
		this.setLayout(thisLayout);
		
		thisLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, estimateLabel, 0, SpringLayout.HORIZONTAL_CENTER, this);
		thisLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, estimateField, 0, SpringLayout.HORIZONTAL_CENTER, this);
		thisLayout.putConstraint(SpringLayout.NORTH, estimateLabel, 10, SpringLayout.NORTH, this);
		thisLayout.putConstraint(SpringLayout.NORTH, estimateField, 10, SpringLayout.SOUTH, estimateLabel);
		thisLayout.putConstraint(SpringLayout.SOUTH, estimateField, -25, SpringLayout.NORTH, submitButton);
		thisLayout.putConstraint(SpringLayout.WEST, submitButton, -90, SpringLayout.HORIZONTAL_CENTER, this);
		thisLayout.putConstraint(SpringLayout.EAST, submitButton, 90, SpringLayout.HORIZONTAL_CENTER, this);
		thisLayout.putConstraint(SpringLayout.SOUTH, submitButton, -10, SpringLayout.SOUTH, this);
		
		thisLayout.putConstraint(SpringLayout.NORTH, estimateFieldErrorMessage, 7, SpringLayout.SOUTH, estimateField);
		thisLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, estimateFieldErrorMessage, 0, SpringLayout.HORIZONTAL_CENTER, this);
		
		thisLayout.putConstraint(SpringLayout.NORTH, estimateSubmittedMessage, 7, SpringLayout.SOUTH, estimateField);
		thisLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, estimateSubmittedMessage, 0, SpringLayout.HORIZONTAL_CENTER, this);
		
		add(estimateSubmittedMessage);
		add(estimateFieldErrorMessage);
		add(submitButton);
		add(estimateLabel);
		add(estimateField);
	}


	/**
	 * Builds a deck based voting panel 
	 */
	private void buildDeckVotingPanel() {
		List<Integer> numbersInDeck = votingDeck.getNumbersInDeck();
		List<Integer> prevEstimateCards;
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		estimateSubmittedMessage.setForeground(Color.BLUE);
		estimateSubmittedMessage.setVisible(false);
		
		// Set default values if this is the first vote
		if (prevEstimate.getVote() < 0) {
			submitButton = new JButton("Submit Estimation");
			prevEstimateCards = new ArrayList<Integer>();
		}
		else {	// Set the default values if this is a re-vote
			prevEstimateCards = cardsFromLastEstimate();
			submitButton = new JButton("Resubmit Estimation");
		}

		// Create submission button
		submitButton.setAlignmentX(CENTER_ALIGNMENT);
		submitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (validateEstimate()){
					fireEstimateEvent();
					clearSelectedCards();
				}
			}
		});

		// Create clear button
		JButton clearButton = new JButton("Clear selected cards");
		clearButton.setAlignmentX(CENTER_ALIGNMENT);
		clearButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				clearSelectedCards();
			}
		});

		//Create and set up the layered pane.
		layeredDeckPane = new JLayeredPane();
		layeredDeckPane.addMouseMotionListener(this);
		layeredDeckPane.setBorder(BorderFactory.createTitledBorder(
				"Select Cards For Your Estimate: "));

		//This is the origin of the first label added.
		Point origin = new Point(10, 20);
		cardOffset = 600/numbersInDeck.size();

		//Add several overlapping, card buttons to the layered pane
		//using absolute positioning/sizing.
		listOfCardButtons = new ArrayList<JButton>();
		for (int i = 0; i < numbersInDeck.size(); i++) {
			JButton cardButton;
			if (prevEstimateCards.contains(numbersInDeck.get(i))) {
				cardButton = createCardButtons(numbersInDeck.get(i), origin, true);
			}
			else {
				cardButton = createCardButtons(numbersInDeck.get(i), origin, false);
			}
			layeredDeckPane.add(cardButton, new Integer(i));
			listOfCardButtons.add(cardButton);
			origin.x += cardOffset;
		}
		layeredDeckPane.setPreferredSize(new Dimension(400, 250));

		// Create control panel for submit, clear, etc. buttons
		JPanel controlPanel = new JPanel();
		SpringLayout controlPanelLayout = new SpringLayout();
		controlPanel.setLayout(controlPanelLayout);
		controlPanel.setPreferredSize(new Dimension(400, 30));
		controlPanelLayout.putConstraint(SpringLayout.VERTICAL_CENTER, submitButton, 0, SpringLayout.VERTICAL_CENTER, controlPanel);
		controlPanelLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, submitButton, 0, SpringLayout.HORIZONTAL_CENTER, controlPanel);
		
		controlPanelLayout.putConstraint(SpringLayout.EAST, clearButton, -10, SpringLayout.WEST, submitButton);
		controlPanelLayout.putConstraint(SpringLayout.VERTICAL_CENTER, clearButton, 0, SpringLayout.VERTICAL_CENTER, submitButton);
		controlPanelLayout.putConstraint(SpringLayout.WEST, estimateSubmittedMessage, 10, SpringLayout.EAST, submitButton);
		controlPanelLayout.putConstraint(SpringLayout.VERTICAL_CENTER, estimateSubmittedMessage, 0, SpringLayout.VERTICAL_CENTER, submitButton);
		
		controlPanelLayout.putConstraint(SpringLayout.WEST, estimateSubmittedMessage, 10, SpringLayout.EAST, submitButton);
		controlPanelLayout.putConstraint(SpringLayout.VERTICAL_CENTER, estimateSubmittedMessage, 0, SpringLayout.VERTICAL_CENTER, submitButton);
		
		controlPanel.add(estimateSubmittedMessage);
		controlPanel.add(submitButton);
		controlPanel.add(clearButton);

		// Create sum of cards label and field
		JPanel sumPane = new JPanel();
		JLabel estimateLabel = new JLabel("Sum of Cards: ");
		estimateLabel.setHorizontalAlignment(SwingConstants.CENTER);
		estimateLabel.setLabelFor(estimateField);
		estimateField = new JFormattedTextField();
		estimateField.setHorizontalAlignment(SwingConstants.CENTER);
		estimateField.setEditable(false);
		estimateField.setBackground(Color.WHITE);
		estimateField.setValue(new Integer(0));
		estimateField.setFont(new Font("Tahoma", Font.PLAIN, 50));
		estimateField.setHorizontalAlignment(JTextField.CENTER);
		estimateField.setPreferredSize(new Dimension(112, 112));
		sumPane.add(estimateLabel);
		sumPane.add(estimateField);
		sumPane.setBounds(700, origin.y, 112, 140);
		layeredDeckPane.add(sumPane, layeredDeckPane.getComponentCount());

		// Add the control and deck sub-panels to the overall panel
		add(layeredDeckPane);
		add(controlPanel);
		
		updateCardEstimateSum();
	}

	
	/**
	 *  Create and set up card button
	 * @param cardValue	The value of the card
	 * @param origin	The position to place the card
	 * @param selected	If it should be selected on startup
	 * @return	The card button created
	 */
	private JButton createCardButtons(int cardValue, Point origin, boolean selected) {
		final JButton card = new JButton();
		// Try to load the corresponding playing card
		try {
			String fileName = new String("cards/" + Integer.toString(cardValue) + "-of-Diamonds.png");
			Image img = ImageIO.read(getClass().getResource(fileName));
			//getClass().getResource("new_req.png"));	// this should work... but doesn't...
			card.setIcon(new ImageIcon(img.getScaledInstance(112, 140, 0)));
		} catch (IOException | NullPointerException | IllegalArgumentException ex) {
			card.setText("\t  " + Integer.toString(cardValue));
		};
		card.setName(String.valueOf(cardValue));
		card.setVerticalAlignment(JLabel.CENTER);
		card.setHorizontalAlignment(JLabel.LEFT);
		card.setOpaque(true);
		// If the card should be selected, set the border to green
		if (selected) {
			card.setBorder(BorderFactory.createLineBorder(Color.GREEN, 4));
			card.setBackground(Color.GREEN);	
		}
		else {
			card.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			card.setBackground(Color.WHITE);
		}
		card.setBounds(origin.x, origin.y, 112, 140);
		card.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				estimateSubmittedMessage.setVisible(false);
				setCardSelected(card, !isCardSelected(card));
				updateEstimate(card);
			}
		});
		card.addMouseMotionListener(this);	// To track mouse movements and dragging
		
		return card;
	}

	/**
	 * Updates the users estimate on a card button click
	 * @param card	the card clicked
	 */
	private void updateEstimate(JButton card) {
		// If multiple cards can be selected
		if (votingDeck.getAllowMultipleSelections()) {
			// If card was selected from estimate
			if (isCardSelected(card)) {
				userEstimate += Integer.parseInt(card.getName());
			}
			// If card was removed from estimate
			else {
				userEstimate -= Integer.parseInt(card.getName());
			}
			// Make sure sum is non-negative
			if (userEstimate < 0) {
				userEstimate = 0;
			}
		}
		else {	// If only one card can be selected
			// Set the card if it wasn't set before
			if (isCardSelected(card)) {
				clearSelectedCards();
				setCardSelected(card, true);
				userEstimate = Integer.parseInt(card.getName());
			}
			else {	// If it was set, don't set
				clearSelectedCards();
				userEstimate = 0;
			}
		}
		estimateField.setValue(new Integer((int) userEstimate));
	}
	
	/**
	 * Updates the sum of selected cards
	 */
	private void updateCardEstimateSum() {
		userEstimate = 0;
		for (JButton card : listOfCardButtons) {
			if (isCardSelected(card)) {
				userEstimate += Integer.parseInt(card.getName());
			}
		}
		estimateField.setValue(new Integer((int) userEstimate));
	}

	/**
	 * Sets all the cards as not selected and the current estimate to zero.
	 */
	private void clearSelectedCards() {
		// De-select all cards
		for (JButton card : listOfCardButtons) {
			setCardSelected(card, false);
		}
		userEstimate = 0;
		estimateField.setValue(new Integer(0));
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
			int tempIndex = -1;
			for (int i = 0; i < listOfCardButtons.size(); i++) {
				if (currentCard == listOfCardButtons.get(i)) {
					// Check the location of the mouse wrt the cards origin
					if ((e.getX() < cardOffset) || (i == listOfCardButtons.size() - 1)) {
						tempIndex = i;
					}
					// If the mouse is on the right side of the card, highlight the next card
					else {
						tempIndex = i + 1;
					}
					break;
				}
			}
			// Only handle the event if the mouse moved to a new card
			if (tempIndex == lastCard) {
				return;
			}
			lastCard = tempIndex;
		}
		highlightCard();
	}

	/**
	 * Moves the specified card slightly lower (y-direction) with respect to the other cards
	 */
	private void highlightCard() {
		int layerLevel = layeredDeckPane.getComponentCount() - 1;
		// Un-highlight all cards not selected for highlighting
		for (int i = 0; i < listOfCardButtons.size(); i++) {
			if (i != lastCard) {
				Point origin = new Point(10 + cardOffset*i, 20);
				JButton card = listOfCardButtons.get(i); 	
				card.setBounds(origin.x, origin.y, 112, 140);
				layeredDeckPane.setComponentZOrder(card, (layerLevel - i));
				layeredDeckPane.moveToBack(card);
				card.revalidate();
				card.repaint();
			}
		}
		// Highlight this card if in the index
		if ((lastCard >= 0) && (lastCard < listOfCardButtons.size())) {
			Point origin = new Point(10 + cardOffset*lastCard, 50);
			JButton card = listOfCardButtons.get(lastCard);
			card.setBounds(origin.x, origin.y, 112, 140);
			layeredDeckPane.setComponentZOrder(card, 0);
			layeredDeckPane.moveToFront(card);
			card.revalidate();
			card.repaint();
		}
		layeredDeckPane.revalidate();
		layeredDeckPane.repaint();
	}
	

	/**
	 * @param card	The card to set properties for
	 * @param cardSelected	If the card is selected or not
	 */
	private void setCardSelected(JButton card, boolean cardSelected) {
		// Set the card to selected
		if (cardSelected) {
			card.setBackground(Color.GREEN);	// card is part of estimate
			card.setBorder(BorderFactory.createLineBorder(Color.GREEN, 4));	
		}
		else {	// Set the card to not selected
			card.setBackground(Color.WHITE); // card is not part of estimate
			card.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		}	
	}
	
	/** 
	 * @param card	The card to check if selected
	 */
	private boolean isCardSelected(JButton card) {
		// Card is selected
		if (card.getBackground() == Color.GREEN) {
			return true;
		}
		// Card is not selected (background == Color.WHITE)
		return false;
	}
	
	/**
	 * Assumes that the cards fit exactly into the estimate
	 * @return	The numbers from the deck that made up the previous estimate
	 */
	private List<Integer> cardsFromLastEstimate() {
		// Get the list of numbers in the deck and sort in ascending order
		List<Integer> numbersInDeck = votingDeck.getNumbersInDeck();
		Collections.sort(numbersInDeck);
		
		int temp = prevEstimate.getVote();
		List<Integer> numbersInEstimate = new ArrayList<Integer>();
		
		// Find which numbers make up the estimate
		for (int i = (numbersInDeck.size() - 1); i >= 0; i--) {
			// If this card fits into the estimate
			if (temp >= numbersInDeck.get(i)) {
				temp -= numbersInDeck.get(i);
				numbersInEstimate.add(numbersInDeck.get(i));
			}
		}
		// If estimate was not 0, remove the 0 card
		if (numbersInEstimate.size() > 1) {
			numbersInEstimate.remove(new Integer(0));
		}
		// Some decks are ony allowed to select 1 card
		if ((!votingDeck.getAllowMultipleSelections()) && (numbersInEstimate.size() > 1)) {
			int highCard = numbersInEstimate.get(numbersInEstimate.size() - 1);
			numbersInEstimate = new ArrayList<Integer>();
			numbersInEstimate.add(highCard);
		}
		
		return numbersInEstimate;
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
			estimateFieldErrorMessage.setText("Please enter a positive number");
			estimateFieldErrorMessage.setVisible(true);
			estimateFieldErrorMessage.revalidate();
			estimateFieldErrorMessage.repaint();
			revalidate();
			repaint();
			return false;
		}
		estimateFieldErrorMessage.setText("");
		estimateFieldErrorMessage.setVisible(false);
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
			estimateSubmittedMessage.setVisible(false);
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
			estimateSubmittedMessage.setVisible(true);
		}
	}

}
