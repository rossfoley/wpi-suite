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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Deck viewer tab that allows users to select a deck and view how it would look like
 * during voting and it's other properties
 */
public class DeckViewer extends JSplitPane {
	private final SpringLayout layout = new SpringLayout();
	private DeckListPanel deckListPanel;
	private final JSplitPane viewDeckPanel = new JSplitPane();
	//private JSplitPane deckSpecificPane;
	private JPanel deckDetailsPanel;
	private DeckVotingPanel cardDisplayPanel;
	private List<Deck> decks;
	private JPanel deckOverviewPanel = new JPanel();
	private JPanel createDeckButtonPanel = new JPanel();
	private JButton createDeckButton = new JButton("Create New Deck");
	private SpringLayout deckOverviewLayout = new SpringLayout();
	private CreateDeck createDeckPanel;
	private JScrollPane tablePanel = new JScrollPane();
	
	/**
	 * 	Constructor for creating the deck viewer panel
	 */
	public DeckViewer() {
		decks = DeckListModel.getInstance().getDecks();
		final String[] deckNames = new String[decks.size()];
		int i = 0;
		for (Deck d : decks) {
			deckNames[i] = d.getDeckName();
			i++;
		}
		
		buildDeckPanel(decks.get(0));

		deckListPanel = new DeckListPanel(decks);
		deckListPanel.addDeckListener(new DeckListener() {
			@Override
			public void deckSubmitted(DeckEvent e){
				
				buildDeckPanel(e.getDeck());
				//deckListPanel = new DeckListPanel(decks);
				
				//final JScrollPane tablePanel = new JScrollPane();
				tablePanel.setViewportView(deckListPanel);
				tablePanel.setMinimumSize(new Dimension(0, 400));
				
				viewDeckPanel.setMinimumSize(new Dimension(300, 300));
				
				createDeckButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						openCreateDeckPanel();
					}
				});
				
				createDeckPanel = new CreateDeck();
				createDeckPanel.addDeckListener(new DeckListener() {
					@Override
					public void deckSubmitted(DeckEvent e) {
						if (e.getDeck() == null) {
							closeCreateDeckPanel();
						}
						else {
							newDeckCreated(e.getDeck());
						}
					}
				}); 

				//buildDeckOverviewPanel(tablePanel);
				
				setLeftComponent(deckOverviewPanel);
				setRightComponent(viewDeckPanel);
				setDividerLocation(225);
			}

		});
		//final JScrollPane tablePanel = new JScrollPane();
		tablePanel.setViewportView(deckListPanel);
		tablePanel.setMinimumSize(new Dimension(0, 400));
		
		viewDeckPanel.setMinimumSize(new Dimension(300, 300));
		
		createDeckButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				openCreateDeckPanel();
			}
		});
		
		createDeckPanel = new CreateDeck();
		createDeckPanel.addDeckListener(new DeckListener() {
			@Override
			public void deckSubmitted(DeckEvent e) {
				if (e.getDeck() == null) {
					closeCreateDeckPanel();
				}
				else {
					newDeckCreated(e.getDeck());
				}
			}
		});
		
		buildDeckOverviewPanel(tablePanel);

		setLeftComponent(deckOverviewPanel);
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
		deckDetailsPanel.setMinimumSize(new Dimension(100, 125));
		final SpringLayout sl_reqDetails = new SpringLayout();
		deckDetailsPanel.setLayout(sl_reqDetails);
		final JLabel nameLabel = new JLabel("Deck Name:");
		final JLabel selectionModeLabel = new JLabel("Deck Selection Mode:");

		final JTextField nameField = new JTextField();
		nameField.setBackground(Color.WHITE);
		if (deckToView != null) {
			nameField.setText(deckToView.getDeckName());
		}
		nameField.setEditable(false);

		final JTextField selectionModeField = new JTextField("");
		selectionModeField.setBackground(Color.WHITE);
		if (deckToView != null) {
			// If it allows multiple card selection
			if (deckToView.getAllowMultipleSelections()) {
				selectionModeField.setText("Multiple card selection mode");
			}
			else {
				selectionModeField.setText("Single card selection mode");
			}
		}
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
		cardDisplayPanel.setSubmitButtonVisible(false);
		
		if (deckToView == null) {
			cardDisplayPanel.setVisible(false);
		}
		else {
			cardDisplayPanel.setVisible(true);
		}
		viewDeckPanel.setOrientation(VERTICAL_SPLIT);
		viewDeckPanel.setDividerSize(0);
		viewDeckPanel.setDividerLocation(viewDeckPanel.getSize().height-200);
		cardDisplayPanel.setMinimumSize(new Dimension(100, 225));
		
		viewDeckPanel.setEnabled(false);

		viewDeckPanel.setTopComponent(deckDetailsPanel);
		viewDeckPanel.setBottomComponent(cardDisplayPanel);
	}
	
	/**
	 * if new decks have been added, update the decklistpanel to reflect this
	 */
	public void refresh(){
		final List<Deck> newDecks = DeckListModel.getInstance().getDecks();
		boolean noNewDecks = true;
		for (Deck newDeck:newDecks){
			boolean deckFound = false;
			for (Deck oldDeck:decks){
				if (oldDeck.getId() == newDeck.getId()) {
					deckFound = true;
				}
			}		
			if (!deckFound){
				noNewDecks = false;
			}
		}
		
		if (!noNewDecks){
			decks = newDecks;
			deckListPanel = new DeckListPanel(decks);
			tablePanel.setViewportView(deckListPanel);
			deckListPanel.addDeckListener(new DeckListener() {
				@Override
				public void deckSubmitted(DeckEvent e){
					
					buildDeckPanel(e.getDeck());
					tablePanel.setViewportView(deckListPanel);
					tablePanel.setMinimumSize(new Dimension(0, 400));
					
					viewDeckPanel.setMinimumSize(new Dimension(300, 300));
					
					createDeckButton.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							openCreateDeckPanel();
						}
					});
					
					createDeckPanel = new CreateDeck();
					createDeckPanel.addDeckListener(new DeckListener() {
						@Override
						public void deckSubmitted(DeckEvent e) {
							if (e.getDeck() == null) {
								closeCreateDeckPanel();
							}
							else {
								newDeckCreated(e.getDeck());
							}
						}
					}); 					
					setLeftComponent(deckOverviewPanel);
					setRightComponent(viewDeckPanel);
					setDividerLocation(225);
				}

			});
		}
		System.out.println("refreshing");
	}
	
	public void buildCreateDeckButtonPanel(){
		createDeckButtonPanel.add(createDeckButton);
	}
	
	/**
	 * Opens the Create Deck panel in the right divider of the first panel 
	 */
	private void openCreateDeckPanel() {
		int dividerLocation = getDividerLocation();
		setRightComponent(createDeckPanel);
		setDividerLocation(dividerLocation);
		createDeckButton.setEnabled(false);
	}
	
	/**
	 * Close the right divider of the first panel after a new deck has been created
	 */
	private void newDeckCreated(Deck newDeck) {
		createDeckPanel = new CreateDeck();
		createDeckPanel.addDeckListener(new DeckListener() {
			@Override
			public void deckSubmitted(DeckEvent e) {
				if (e.getDeck() == null) {
					closeCreateDeckPanel();
				}
				else {
					newDeckCreated(e.getDeck());
					refresh();
				}
			}
		});
		closeCreateDeckPanel();
	}
	
	public void closeCreateDeckPanel() {
		int dividerLocation = getDividerLocation();
		setRightComponent(viewDeckPanel);
		createDeckButton.setEnabled(true);
		setDividerLocation(dividerLocation);
	}
	

	private void buildDeckOverviewPanel(JScrollPane tablePanel){
		deckOverviewPanel.setLayout(deckOverviewLayout);
		
		deckOverviewLayout.putConstraint(SpringLayout.NORTH, tablePanel, 0, SpringLayout.NORTH, deckOverviewPanel);
		deckOverviewLayout.putConstraint(SpringLayout.WEST, tablePanel, 0, SpringLayout.WEST, deckOverviewPanel);
		deckOverviewLayout.putConstraint(SpringLayout.EAST, tablePanel, 0, SpringLayout.EAST, deckOverviewPanel);
		deckOverviewLayout.putConstraint(SpringLayout.SOUTH, tablePanel, -10, SpringLayout.NORTH, createDeckButton);
		deckOverviewLayout.putConstraint(SpringLayout.SOUTH, createDeckButton, -10, SpringLayout.SOUTH, deckOverviewPanel);
		deckOverviewLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, createDeckButton, 0, SpringLayout.HORIZONTAL_CENTER, deckOverviewPanel);
				
		deckOverviewPanel.add(tablePanel);
		deckOverviewPanel.add(createDeckButton);
	}
	

}
