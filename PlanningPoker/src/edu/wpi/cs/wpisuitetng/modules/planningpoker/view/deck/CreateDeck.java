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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.DropMode;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.Deck;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.DeckListModel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Font;


/**
 * This class is a sub-panel for creating a new set of cards to use for planning poker voting 
 * 
 * @version $Revision: 1.0 $
 */
public class CreateDeck extends JPanel {
	private ArrayList<Integer> listOfCards = new ArrayList<Integer>();
	private boolean multiSelectionMode = false;
	private final JTextField txtDeckName = new JTextField();
	private final JLabel lblDeckNameError = new JLabel("A deck name is required");
	private JTextField txtCardValue;
	private JButton btnAddCard;
	private final JLabel lblAddCardError = new JLabel("Value must be positive");
	private JButton btnRemove;
	private JButton btnRemoveAll;
	private JButton btnCreate;
	private JButton btnCancel;
	private final JLabel lblNoCardsError = new JLabel("Please add cards to the deck");
	private JTable cardTable;
	private DefaultTableModel cardTableModel;
	private SpringLayout springLayout;
	private SpringLayout modePanelLayout = new SpringLayout();
	private transient Vector<DeckListener> listeners;


	public CreateDeck() {
		buildPanel();
	}

	private void buildPanel() {
		springLayout = new SpringLayout();
		setLayout(springLayout);
		setPreferredSize(new Dimension(370, 350));

		final JLabel lblDeckName = new JLabel("Deck Name:* ");
		//lblDeckName.setFont(new Font("Tahoma", Font.BOLD, 11));
		final Deck tempDeck = new Deck();
		txtDeckName.setText(tempDeck.getDeckName());
		txtDeckName.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				validateButtons();
			}
			@Override
			public void insertUpdate(DocumentEvent e) {
				validateButtons();
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
				validateButtons();
			}
		});
		lblDeckNameError.setVisible(false);
		lblDeckNameError.setForeground(Color.RED);

		final JLabel lblCard = new JLabel("New Card Value: ");
		final JLabel cardArea = new JLabel("Add/Remove Cards:");
		//lblCard.setFont(new Font("Tahoma", Font.BOLD, 11));

		// Radio button group for multiple vs. single selection mode
		JPanel modeSelectionPanel = createModeSelectionPanel();
		modeSelectionPanel.setPreferredSize(new Dimension(200, 70));

		btnAddCard = new JButton("Add Card");
		btnAddCard.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					final int value = Integer.parseInt(txtCardValue.getText());
					addCard(value);
					txtCardValue.setText("");
					refresh();
				} catch (NumberFormatException ex) {}
			}
		});
		lblAddCardError.setVisible(false);
		lblAddCardError.setForeground(Color.RED);

		txtCardValue = new JTextField();
		txtCardValue.setPreferredSize(new Dimension(26, 26));
		txtCardValue.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				warnCardValue();

			}
			@Override
			public void insertUpdate(DocumentEvent e) {
				warnCardValue();
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
				warnCardValue();
			}
		});
		txtCardValue.setText("0");
		txtCardValue.setHorizontalAlignment(SwingConstants.CENTER);

		buildCardTable();

		// Creates the remove button and attaches functionality
		btnRemove = new JButton("Remove card");
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final int [] toRemove = cardTable.getSelectedRows();

				for (int index : toRemove) {
					removeCard((int) cardTable.getValueAt(index, 0));
				}
				lblNoCardsError.setForeground(Color.RED);
				refresh();
			}
		});

		btnRemoveAll = new JButton("Remove all cards");
		btnRemoveAll.setBounds(221, 165, 57, 23);
		btnRemoveAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				lblNoCardsError.setForeground(Color.RED);
				removeAllCards();
				refresh();
			}
		});

		// Put JTable into a scroll pane to allow for scrolling and column headers
		final JScrollPane cardScrollPane = new JScrollPane(cardTable);

		// Create deck button
		btnCreate = new JButton("Create Deck");
		btnCreate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fireDeckEvent(createDeck());
			}
		});

		// Create cancel button
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fireDeckEvent(null);
			}
		});

		// Sprint layout constraints
		springLayout.putConstraint(SpringLayout.WEST, lblDeckName, 10, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.NORTH, lblDeckName, 20, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, txtDeckName, 0, SpringLayout.WEST, lblDeckName);
		springLayout.putConstraint(SpringLayout.EAST, txtDeckName, 150, SpringLayout.WEST, txtDeckName);
		springLayout.putConstraint(SpringLayout.NORTH, txtDeckName, 10, SpringLayout.SOUTH, lblDeckName);

		springLayout.putConstraint(SpringLayout.WEST, lblDeckNameError, 10, SpringLayout.EAST, lblDeckName);
		springLayout.putConstraint(SpringLayout.VERTICAL_CENTER, lblDeckNameError, 0, SpringLayout.VERTICAL_CENTER, lblDeckName);

		springLayout.putConstraint(SpringLayout.WEST, modeSelectionPanel, 10, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.NORTH, modeSelectionPanel, 10, SpringLayout.SOUTH, txtDeckName);
		
		springLayout.putConstraint(SpringLayout.NORTH, cardArea, 20, SpringLayout.SOUTH, modeSelectionPanel);
		springLayout.putConstraint(SpringLayout.WEST, cardArea, 10, SpringLayout.WEST, this);
		
		springLayout.putConstraint(SpringLayout.NORTH, cardScrollPane, 10, SpringLayout.SOUTH, cardArea);
		springLayout.putConstraint(SpringLayout.WEST, cardScrollPane, 25, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.SOUTH, cardScrollPane, -10, SpringLayout.NORTH, btnCancel);
		
		springLayout.putConstraint(SpringLayout.SOUTH, txtCardValue, -30, SpringLayout.VERTICAL_CENTER, cardScrollPane);
		springLayout.putConstraint(SpringLayout.WEST, txtCardValue, 10, SpringLayout.EAST, cardScrollPane);
		springLayout.putConstraint(SpringLayout.EAST, txtCardValue, 50, SpringLayout.WEST, txtCardValue);
		
		springLayout.putConstraint(SpringLayout.SOUTH, lblCard, -10, SpringLayout.NORTH, txtCardValue);
		springLayout.putConstraint(SpringLayout.WEST, lblCard, 10, SpringLayout.EAST, cardScrollPane);
		
		springLayout.putConstraint(SpringLayout.SOUTH, btnRemove, 30, SpringLayout.VERTICAL_CENTER, cardScrollPane);
		springLayout.putConstraint(SpringLayout.WEST, btnRemove, 10, SpringLayout.EAST, cardScrollPane);
		springLayout.putConstraint(SpringLayout.EAST, btnRemove, 0, SpringLayout.EAST, btnRemoveAll);
		
		springLayout.putConstraint(SpringLayout.NORTH, btnRemoveAll, 10, SpringLayout.SOUTH, btnRemove);
		springLayout.putConstraint(SpringLayout.WEST, btnRemoveAll, 10, SpringLayout.EAST, cardScrollPane);
		springLayout.putConstraint(SpringLayout.EAST, btnRemoveAll, 0, SpringLayout.EAST, btnAddCard);
		
		springLayout.putConstraint(SpringLayout.NORTH, btnAddCard, 0, SpringLayout.NORTH, txtCardValue);
		springLayout.putConstraint(SpringLayout.WEST, btnAddCard, 10, SpringLayout.EAST, txtCardValue);
		
		springLayout.putConstraint(SpringLayout.VERTICAL_CENTER, lblAddCardError, 0, SpringLayout.VERTICAL_CENTER, lblCard);
		springLayout.putConstraint(SpringLayout.WEST, lblAddCardError, 10, SpringLayout.EAST, lblCard);

		springLayout.putConstraint(SpringLayout.WEST, btnCancel, 10, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.SOUTH, btnCancel, -10, SpringLayout.SOUTH, this);
		
		springLayout.putConstraint(SpringLayout.WEST, btnCreate, 10, SpringLayout.EAST, btnCancel);
		springLayout.putConstraint(SpringLayout.VERTICAL_CENTER, btnCreate, 0, SpringLayout.VERTICAL_CENTER, btnCancel);
		
		springLayout.putConstraint(SpringLayout.WEST, lblNoCardsError, 10, SpringLayout.EAST, btnCreate);
		springLayout.putConstraint(SpringLayout.VERTICAL_CENTER, lblNoCardsError, 0, SpringLayout.VERTICAL_CENTER, btnCreate);
		

		// Add all components to the JPanel
		add(modeSelectionPanel);
		add(lblDeckName);
		add(txtDeckName);
		add(lblDeckNameError);
		add(lblCard);
		add(btnAddCard);
		add(lblAddCardError);
		add(txtCardValue);
		add(cardScrollPane);
		add(btnRemove);
		add(btnRemoveAll);
		add(btnCreate);
		add(btnCancel);
		add(lblNoCardsError);
		add(cardArea);
		
		refresh();
	}

	private void buildCardTable() {
		final String[] columnNames = {"Card(s)*"};
		final Object[][] data = {};
		cardTable = new JTable(data, columnNames);
		cardTableModel = new DefaultTableModel(data, columnNames);
		cardTable.setModel(cardTableModel);
		cardTable.setToolTipText("List of cards already added to the deck");
		cardTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		cardTable.setDropMode(DropMode.ON);
		cardTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		cardTable.setPreferredScrollableViewportSize(new Dimension(75, 100));
		cardTable.setFillsViewportHeight(true);

		// Center align the data 
		final DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment( JLabel.CENTER );
		cardTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);

		/* Create double-click event listener */
		cardTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				validateButtons();
			}
		});

	}

	/**
	 * Adds the input value to the list of cards
	 */
	public void addCard(int cardValue) {
		listOfCards.add(cardValue);
		Collections.sort(listOfCards);
	}

	/**
	 * Removes the input value from the list of cards
	 */
	public void removeCard(int cardValue) {
		listOfCards.remove(new Integer(cardValue));
	}

	/**
	 * Removes all cards from the list
	 */
	public void removeAllCards() {
		listOfCards = new ArrayList<Integer>();
	}
	
	/**
	 * Getter for the list of cards to be added to the deck
	 * @return	The list of cards that have been added
	 */
	public ArrayList<Integer> getListOfCards() {
		return listOfCards;
	}

	/**
	 *  Enables the valid buttons and disables all other buttons
	 */
	private void validateButtons() {
		// Only display remove all button and create buton if there are cards in the deck
		btnCreate.setEnabled(true);
		if (listOfCards.size() > 0) {
			btnRemoveAll.setEnabled(true);
			lblNoCardsError.setVisible(false);
		}
		else {
			btnRemoveAll.setEnabled(false);
			btnCreate.setEnabled(false);
			lblNoCardsError.setVisible(true);
		}
		// Disable the create button if no deck name
		if (txtDeckName.getText().equals("")) {
			btnCreate.setEnabled(false);
			lblDeckNameError.setVisible(true);
		}
		else {
			lblDeckNameError.setVisible(false);
		}

		// Display single card remove button if an element in the table has been selected
		if (cardTable.getSelectedRow() >= 0) {
			btnRemove.setEnabled(true);
		}
		else {
			btnRemove.setEnabled(false);
		}

		// Disable add card button if no text entered
		warnCardValue();
	}

	/**
	 * Refreshes the card table and buttons
	 */
	public void refresh() {
		// clear the table
		cardTableModel.setRowCount(0);
		// Add all cards
		for (Integer element : listOfCards) {
			cardTableModel.addRow(new Object[]{element});
		}
		validateButtons();
	}

	public Deck createDeck() {
		final Deck newDeck = new Deck(listOfCards, multiSelectionMode);
		newDeck.setDeckName(txtDeckName.getText());

		DeckListModel.getInstance().addDeck(newDeck);

		return newDeck;
	}

	/**
	 * Creates a group of radio buttons to set the card selection mode
	 * @return	The created panel
	 */
	private JPanel createModeSelectionPanel() {
		final JRadioButton rbtnSingle = new JRadioButton("Single Selection");
		rbtnSingle.setSelected(true);
		rbtnSingle.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				multiSelectionMode = !rbtnSingle.isSelected();
			}
		});

		final JRadioButton rbtnMulti = new JRadioButton("Multiple Selection");
		rbtnMulti.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				multiSelectionMode = rbtnMulti.isSelected();
			}
		});

		final ButtonGroup rGroup = new ButtonGroup();
		rGroup.add(rbtnSingle);
		rGroup.add(rbtnMulti);

		final JPanel modeBtnPanel = new JPanel(new GridLayout(0, 1));
		modeBtnPanel.add(rbtnSingle);
		modeBtnPanel.add(rbtnMulti);

		final JPanel modePanel = new JPanel();
		final JLabel lblMode = new JLabel("Card Selection Mode: ");
		modePanel.setLayout(modePanelLayout);
		
		modePanelLayout.putConstraint(SpringLayout.NORTH, lblMode, 0, SpringLayout.NORTH, modePanel);
		modePanelLayout.putConstraint(SpringLayout.WEST, lblMode, 0, SpringLayout.WEST, modePanel);
		
		modePanelLayout.putConstraint(SpringLayout.NORTH, modeBtnPanel, 6, SpringLayout.SOUTH, lblMode);
		modePanelLayout.putConstraint(SpringLayout.WEST, modeBtnPanel, 10, SpringLayout.WEST, modePanel);
		
		modePanel.add(lblMode);
		modePanel.add(modeBtnPanel);

		return modePanel;
	}

	private void warnCardValue() {
		// Disable adding card if nothing is input
		if (txtCardValue.getText().equals("")) {
			btnAddCard.setEnabled(false);
			lblAddCardError.setVisible(false);
		}
		else {
			try {
				if (Integer.parseInt(txtCardValue.getText()) >= 0) {
					btnAddCard.setEnabled(true);
					lblAddCardError.setVisible(false);
				}
				else {
					btnAddCard.setEnabled(false);
					lblAddCardError.setVisible(true);					
				}
				// Disable and warn if it is not a number
			} catch (NumberFormatException ex) {
				btnAddCard.setEnabled(false);
				lblAddCardError.setVisible(true);
			}
		}
	}


	/**
	 * Register a listener for DeckEvents
	 */
	synchronized public void addDeckListener(DeckListener l) {
		if (listeners == null) {
			listeners = new Vector<DeckListener>();
		}
		listeners.addElement(l);
	}  

	/**
	 * Remove a listener for DeckEvents
	 */
	synchronized public void removeDeckListener(DeckListener l) {
		if (listeners == null) {
			listeners = new Vector<DeckListener>();
		}
		else {
			listeners.removeElement(l);
		}
	}

	/**
	 * Fire a DeckeEvent to all registered listeners
	 */
	protected void fireDeckEvent(Deck newDeck) {
		// Do nothing if we have no listeners
		if (listeners != null && !listeners.isEmpty()) {
			// Create the event object to send
			final DeckEvent event = 
					new DeckEvent(this, newDeck);

			// Make a copy of the listener list in case anyone adds/removes listeners
			final Vector<DeckListener> targets;
			synchronized (this) {
				targets = (Vector<DeckListener>) listeners.clone();
			}

			// Walk through the listener list and call the estimateSubmitted method in each
			final Enumeration<DeckListener> e = targets.elements();
			while (e.hasMoreElements()) {
				DeckListener l = e.nextElement();
				l.deckSubmitted(event);
			}
		}
	}

}
