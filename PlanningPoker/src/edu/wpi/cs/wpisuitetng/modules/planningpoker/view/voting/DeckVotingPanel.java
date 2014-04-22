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
import java.awt.Component;
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
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SpringLayout;

import edu.wpi.cs.wpisuitetng.janeway.config.ConfigManager;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.Deck;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.Estimate;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSessionModel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.overview.OverviewDetailInfoPanel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.overview.OverviewReqTable;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.overview.OverviewVoterTable;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.voting.EstimateListener;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.RequirementModel;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.ViewEventController;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

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
	private DefaultTableModel tableModel = null;
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
	private OverviewVoterTable thetable;
	private JScrollPane thetablePanel;
	private Component newTable;
	private JScrollPane tablePanel;
	
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

		// Create the text field for the estimation number
		NumberFormat estimateFormat = NumberFormat.getNumberInstance();
		estimateField = new JFormattedTextField(estimateFormat);
		estimateField.setHorizontalAlignment(SwingConstants.CENTER);
		estimateField.setFont(new Font("Tahoma", Font.PLAIN, 50));
		estimateField.setToolTipText("Enter Estimation Here");
		estimateField.addPropertyChangeListener("value", this);

		estimateField.setPreferredSize(new Dimension(200, 100));
		// Set default values if this is the first vote
		if (prevEstimate == null) {
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

		JPanel subPanel = new JPanel();
		subPanel.setPreferredSize(new Dimension(400, 100));
		SpringLayout subPanelLayout = new SpringLayout();
		subPanel.setLayout(subPanelLayout);
		
		subPanelLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, estimateLabel, 0, SpringLayout.HORIZONTAL_CENTER, subPanel);
		subPanelLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, estimateField, 0, SpringLayout.HORIZONTAL_CENTER, subPanel);
		subPanelLayout.putConstraint(SpringLayout.NORTH, estimateLabel, 10, SpringLayout.NORTH, subPanel);
		subPanelLayout.putConstraint(SpringLayout.NORTH, estimateField, 10, SpringLayout.SOUTH, estimateLabel);
		subPanelLayout.putConstraint(SpringLayout.SOUTH, estimateField, -25, SpringLayout.NORTH, submitButton);
		subPanelLayout.putConstraint(SpringLayout.WEST, submitButton, -70, SpringLayout.HORIZONTAL_CENTER, subPanel);
		subPanelLayout.putConstraint(SpringLayout.EAST, submitButton, 70, SpringLayout.HORIZONTAL_CENTER, subPanel);
		subPanelLayout.putConstraint(SpringLayout.SOUTH, submitButton, -10, SpringLayout.SOUTH, subPanel);
		
		subPanelLayout.putConstraint(SpringLayout.NORTH, estimateFieldErrorMessage, 7, SpringLayout.SOUTH, estimateField);
		subPanelLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, estimateFieldErrorMessage, 0, SpringLayout.HORIZONTAL_CENTER, subPanel);
		
		
		subPanel.add(estimateFieldErrorMessage);		
		subPanel.add(submitButton);
		subPanel.add(estimateLabel);
		subPanel.add(estimateField);
		add(subPanel, BorderLayout.CENTER);				
	}
	

	/**
	 * Builds a deck based voting panel 
	 */
	private void buildDeckVotingPanel() {
		List<Integer> numbersInDeck = votingDeck.getNumbersInDeck();
		List<Integer> prevEstimateCards;
		// Set default values if this is the first vote
		if (prevEstimate == null) {
			submitButton = new JButton("Submit Estimation");
			prevEstimateCards = new ArrayList<Integer>();
		}
		else {	// Set the default values if this is a re-vote
			prevEstimateCards = cardsFromLastEstimate();
			submitButton = new JButton("Resubmit Estimation");
		}
		for (int temp : prevEstimateCards) {
			System.out.println("PrevCard: " + temp);
		}
		// Create submission button
		submitButton.setAlignmentX(CENTER_ALIGNMENT);
		submitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (validateEstimate()){
					fireEstimateEvent();
				}
			}
		});
		
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
		for (int i = 0; i < numbersInDeck.size(); i++) {
			JButton cardButton;
			cardButton = createCardButtons(numbersInDeck.get(i), origin, false);
			layeredDeckPane.add(cardButton, new Integer(i));
			listOfCardButtons.add(cardButton);
			origin.x += cardOffset;
		}

		//Add control pane and layered pane to this JPanel.
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(layeredDeckPane);
		add(submitButton);
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
		// If card was selected from estimate
		if (isCardSelected(card)) {
			userEstimate += Integer.parseInt(card.getName());
		}
		// If card was removed from estimate
		else if (userEstimate >= 0) {
			userEstimate -= Integer.parseInt(card.getName());
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
			System.out.println("invalid number entered");
			estimateFieldErrorMessage.setText("Please enter a positive number");
			estimateFieldErrorMessage.setVisible(true);
			estimateFieldErrorMessage.revalidate();
			estimateFieldErrorMessage.repaint();
			revalidate();
			repaint();
			return false;
		}
		System.out.println("valid number entered");
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
