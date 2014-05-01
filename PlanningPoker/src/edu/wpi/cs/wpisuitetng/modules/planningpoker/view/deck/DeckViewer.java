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

import java.util.List;
import javax.swing.*;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.Deck;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.DeckListModel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.voting.DeckVotingPanel;

import java.awt.Color;
import java.awt.Dimension;

/**
 * Deck viewer tab that allows users to select a deck and view how it would look like
 * during voting and it's other properties
 *
 */
public class DeckViewer extends JSplitPane {
	private final SpringLayout layout = new SpringLayout();
	private DeckListPanel deckListPanel;
	private final JPanel viewDeckPanel = new JPanel();
	private JPanel deckDetailsPanel;
	private DeckVotingPanel cardDisplayPanel;

	/**
	 * 	Constructor for creating the deck viewer panel
	 */
	public DeckViewer() {
		buildDeckPanel(null);
		
		final List<Deck> decks = DeckListModel.getInstance().getDecks();
		final String[] deckNames = new String[decks.size()];
		int i = 0;
		for (Deck d : decks) {
			deckNames[i] = d.getDeckName();
			i++;
		}

		deckListPanel = new DeckListPanel(decks);
		deckListPanel.addDeckListener(new DeckListener() {
			@Override
			public void deckSubmitted(DeckEvent e){
				buildDeckPanel(e.getDeck());
				final JScrollPane tablePanel = new JScrollPane();
				tablePanel.setViewportView(deckListPanel);

				tablePanel.setMinimumSize(new Dimension(200, 300));
				viewDeckPanel.setMinimumSize(new Dimension(300, 300));

				setLeftComponent(tablePanel);
				setRightComponent(viewDeckPanel);
				setDividerLocation(225);
			}

		});
		final JScrollPane tablePanel = new JScrollPane();
		tablePanel.setViewportView(deckListPanel);

		tablePanel.setMinimumSize(new Dimension(200, 300));
		viewDeckPanel.setMinimumSize(new Dimension(300, 300));

		setLeftComponent(tablePanel);
		setRightComponent(viewDeckPanel);
		setDividerLocation(225);
	}


	/**
	 * Creates the deck detail panel to display the deck name and selection mode
	 * @param deckToView	The deck to view
	 * @return	The created panel
	 */
	public JPanel makeDeckDetailPanel(Deck deckToView) {
		deckDetailsPanel = new JPanel();
		final SpringLayout sl_reqDetails = new SpringLayout();
		deckDetailsPanel.setLayout(sl_reqDetails);
		final JLabel nameLabel = new JLabel("Deck Name:");
		final JLabel selectionModeLabel = new JLabel("Deck Selection Mode:");

		final JTextField nameField = new JTextField();
		nameField.setBackground(Color.WHITE);
		nameField.setEditable(false);

		final JTextField selectionModeField = new JTextField("");
		selectionModeField.setBackground(Color.WHITE);
		selectionModeField.setEditable(false);

		sl_reqDetails.putConstraint(SpringLayout.NORTH, nameLabel, 10, SpringLayout.NORTH, deckDetailsPanel);
		sl_reqDetails.putConstraint(SpringLayout.WEST, nameLabel, 10, SpringLayout.WEST, deckDetailsPanel);	

		sl_reqDetails.putConstraint(SpringLayout.NORTH, nameField, 6, SpringLayout.SOUTH, nameLabel);
		sl_reqDetails.putConstraint(SpringLayout.WEST, nameField, 0, SpringLayout.WEST, nameLabel);
		sl_reqDetails.putConstraint(SpringLayout.EAST, nameField, -10, SpringLayout.HORIZONTAL_CENTER, deckDetailsPanel);

		sl_reqDetails.putConstraint(SpringLayout.NORTH, selectionModeLabel, 6, SpringLayout.SOUTH, nameField);
		sl_reqDetails.putConstraint(SpringLayout.WEST, selectionModeLabel, 0, SpringLayout.WEST, nameLabel);

		sl_reqDetails.putConstraint(SpringLayout.NORTH, selectionModeField, 6, SpringLayout.SOUTH, selectionModeLabel);
		sl_reqDetails.putConstraint(SpringLayout.WEST, selectionModeField, 0, SpringLayout.WEST, nameLabel);
		sl_reqDetails.putConstraint(SpringLayout.EAST, selectionModeField, 0, SpringLayout.EAST, nameField);
		sl_reqDetails.putConstraint(SpringLayout.SOUTH, selectionModeField, -10, SpringLayout.SOUTH, deckDetailsPanel);

		deckDetailsPanel.add(nameLabel);
		deckDetailsPanel.add(selectionModeLabel);
		deckDetailsPanel.add(nameField);
		deckDetailsPanel.add(selectionModeField);

		return deckDetailsPanel; 
	}

	/**
	 * Build the part of the panel that is specific to displaying the selected deck's details.
	 * Displays the deck name, selection mode, and displays the decks cards
	 * @param deckToView	The deck to have details about in the panel
	 */
	public void buildDeckPanel(Deck deckToView){
		// Remove panels before recreating to refresh them correctly
		if ((viewDeckPanel.getComponentCount() > 0) && (deckDetailsPanel != null) &&
				(cardDisplayPanel != null)) {
			viewDeckPanel.remove(deckDetailsPanel);
			viewDeckPanel.remove(cardDisplayPanel);
		}
		// Recreate the deck detail and card display panels
		deckDetailsPanel = makeDeckDetailPanel(deckToView);
		cardDisplayPanel = new DeckVotingPanel(deckToView);

		viewDeckPanel.setLayout(layout);

		layout.putConstraint(SpringLayout.NORTH, deckDetailsPanel, 5, SpringLayout.NORTH, viewDeckPanel);
		layout.putConstraint(SpringLayout.WEST, deckDetailsPanel, 5, SpringLayout.WEST, viewDeckPanel);
		layout.putConstraint(SpringLayout.EAST, deckDetailsPanel, -5, SpringLayout.EAST, viewDeckPanel);

		layout.putConstraint(SpringLayout.SOUTH, cardDisplayPanel, -5, SpringLayout.SOUTH, viewDeckPanel);
		layout.putConstraint(SpringLayout.WEST, cardDisplayPanel, 0, SpringLayout.WEST, deckDetailsPanel);
		layout.putConstraint(SpringLayout.EAST, cardDisplayPanel, 0, SpringLayout.EAST, deckDetailsPanel);

		layout.putConstraint(SpringLayout.SOUTH, deckDetailsPanel, -10, SpringLayout.NORTH, cardDisplayPanel);

		layout.putConstraint(SpringLayout.NORTH, cardDisplayPanel, -250, SpringLayout.SOUTH, viewDeckPanel);

		if (deckToView == null) {
			cardDisplayPanel.setVisible(false);
		}
		else {
			cardDisplayPanel.setVisible(true);
		}
		viewDeckPanel.add(deckDetailsPanel);
		viewDeckPanel.add(cardDisplayPanel);
	}

}
