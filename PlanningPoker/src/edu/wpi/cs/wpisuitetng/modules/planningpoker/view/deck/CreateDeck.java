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
	private JTextField txtDeckName = new JTextField();
	private JTextField txtCardValue;
	private JButton btnAddCard;
	private JLabel lblAddCardError = new JLabel("Card value must be a positive number");
	private JButton btnRemove;
	private JButton btnRemoveAll;
	private JButton btnCreate;
	private JTable cardTable;
	private DefaultTableModel cardTableModel;
	private SpringLayout springLayout;


	public CreateDeck() {
		buildPanel();
	}

	private void buildPanel() {
		springLayout = new SpringLayout();
		setLayout(springLayout);

		JLabel lblDeckName = new JLabel("Deck Name:* ");
		lblDeckName.setFont(new Font("Tahoma", Font.BOLD, 11));

		JLabel lblCard = new JLabel("New Card Value: ");
		lblCard.setFont(new Font("Tahoma", Font.BOLD, 11));

		// Radio button group for multiple vs. single selection mode
		JPanel modeSelectionPanel = createModeSelectionPanel();

		btnAddCard = new JButton("Add Card:");
		btnAddCard.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					int value = Integer.parseInt(txtCardValue.getText());
					addCard(value);
					txtCardValue.setText("");
					refresh();
				} catch (NumberFormatException ex) {}
			}
		});
		lblAddCardError.setVisible(false);

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

		buildCardTable();

		// Creates the remove button and attaches functionality
		btnRemove = new JButton("Remove card");
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int [] toRemove = cardTable.getSelectedRows();

				for (int index : toRemove) {
					removeCard((int) cardTable.getValueAt(index, 0));
				}
				refresh();
			}
		});

		btnRemoveAll = new JButton("Remove all cards");
		btnRemoveAll.setBounds(221, 165, 57, 23);
		btnRemoveAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				removeAllCards();
				refresh();
			}
		});

		// Put JTable into a scroll pane to allow for scrolling and column headers
		JScrollPane cardScrollPane = new JScrollPane(cardTable);

		// Create deck button
		btnCreate = new JButton("Create Deck");
		btnCreate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				createDeck();
				btnCreate.setEnabled(false);
			}
		});

		// Sprint layout constraints
		springLayout.putConstraint(SpringLayout.WEST, lblDeckName, 10, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.NORTH, lblDeckName, 20, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, txtDeckName, 10, SpringLayout.EAST, lblDeckName);
		springLayout.putConstraint(SpringLayout.EAST, txtDeckName, 0, SpringLayout.EAST, btnAddCard);
		springLayout.putConstraint(SpringLayout.VERTICAL_CENTER, txtDeckName, 0, SpringLayout.VERTICAL_CENTER, lblDeckName);

		springLayout.putConstraint(SpringLayout.WEST, modeSelectionPanel, 10, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.NORTH, modeSelectionPanel, 10, SpringLayout.SOUTH, txtDeckName);

		springLayout.putConstraint(SpringLayout.WEST, lblCard, 10, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.VERTICAL_CENTER, lblCard, 0, SpringLayout.VERTICAL_CENTER, txtCardValue);

		springLayout.putConstraint(SpringLayout.WEST, txtCardValue, 0, SpringLayout.EAST, lblCard);
		springLayout.putConstraint(SpringLayout.NORTH, txtCardValue, 10, SpringLayout.SOUTH, modeSelectionPanel);

		springLayout.putConstraint(SpringLayout.VERTICAL_CENTER, btnAddCard, 0, SpringLayout.VERTICAL_CENTER, txtCardValue);
		springLayout.putConstraint(SpringLayout.WEST, btnAddCard, 10, SpringLayout.EAST, txtCardValue);		
		springLayout.putConstraint(SpringLayout.VERTICAL_CENTER, lblAddCardError, 0, SpringLayout.VERTICAL_CENTER, btnAddCard);
		springLayout.putConstraint(SpringLayout.WEST, lblAddCardError, 10, SpringLayout.EAST, btnAddCard);
		
		springLayout.putConstraint(SpringLayout.EAST, cardScrollPane, 0, SpringLayout.EAST, txtCardValue);
		springLayout.putConstraint(SpringLayout.NORTH, cardScrollPane, 10, SpringLayout.SOUTH, txtCardValue);
		springLayout.putConstraint(SpringLayout.SOUTH, cardScrollPane, 150, SpringLayout.NORTH, cardScrollPane);

		springLayout.putConstraint(SpringLayout.VERTICAL_CENTER, btnRemoveAll, -25, SpringLayout.VERTICAL_CENTER, cardScrollPane);
		springLayout.putConstraint(SpringLayout.WEST, btnRemoveAll, 10, SpringLayout.EAST, cardScrollPane);

		springLayout.putConstraint(SpringLayout.WEST, btnRemove, 0, SpringLayout.WEST, btnRemoveAll);
		springLayout.putConstraint(SpringLayout.NORTH, btnRemove, 5, SpringLayout.SOUTH, btnRemoveAll);

		springLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, btnCreate, 0, SpringLayout.EAST, cardScrollPane);
		springLayout.putConstraint(SpringLayout.NORTH, btnCreate, 10, SpringLayout.SOUTH, cardScrollPane);


		// Add all components to the JPanel
		add(modeSelectionPanel);
		add(lblDeckName);
		add(txtDeckName);
		add(lblCard);
		add(btnAddCard);
		add(lblAddCardError);
		add(txtCardValue);
		add(cardScrollPane);
		add(btnRemove);
		add(btnRemoveAll);
		add(btnCreate);

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
		cardTable.setDragEnabled(true);
		cardTable.setDropMode(DropMode.ON);
		cardTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		cardTable.setPreferredScrollableViewportSize(new Dimension(75, 100));
		cardTable.setFillsViewportHeight(true);

		// Center align the data 
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
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
	private void addCard(int cardValue) {
		listOfCards.add(cardValue);
		Collections.sort(listOfCards);
	}

	/**
	 * Removes the input value from the list of cards
	 */
	private void removeCard(int cardValue) {
		listOfCards.remove(new Integer(cardValue));
	}

	/**
	 * Removes all cards from the list
	 */
	private void removeAllCards() {
		listOfCards = new ArrayList<Integer>();
	}

	/**
	 *  Enables the valid buttons and disables all other buttons
	 */
	private void validateButtons() {
		// Only display remove buttons if there are cards in the deck
		if (listOfCards.size() > 0) {
			btnRemoveAll.setEnabled(true);
		}
		else {
			btnRemoveAll.setEnabled(false);
		}
		// If an element in the table has been selected
		if (cardTable.getSelectedRow() >= 0) {
			btnRemove.setEnabled(true);
		}
		else {
			btnRemove.setEnabled(false);
		}
		// Disable add card button if no text entered
		if (txtCardValue.getText().equals("")) {
			btnAddCard.setEnabled(false);
		}
		else {
			btnAddCard.setEnabled(true);
		}
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

	public void createDeck() {
		Deck newDeck = new Deck(listOfCards, false);
		newDeck.setDeckName(txtDeckName.getText());
		newDeck.setAllowMultipleSelections(multiSelectionMode);

		DeckListModel.getInstance().addDeck(newDeck);
	}

	/**
	 * Creates a group of radio buttons to set the card selection mode
	 * @return	The created panel
	 */
	private JPanel createModeSelectionPanel() {
		JRadioButton rbtnSingle = new JRadioButton("Single Selection");
		rbtnSingle.setSelected(true);
		rbtnSingle.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				multiSelectionMode = false;
			}
		});

		JRadioButton rbtnMulti = new JRadioButton("Multiple Selection");
		rbtnMulti.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				multiSelectionMode = true;
			}
		});

		ButtonGroup rGroup = new ButtonGroup();
		rGroup.add(rbtnSingle);
		rGroup.add(rbtnMulti);

		JPanel modeBtnPanel = new JPanel(new GridLayout(0, 1));
		modeBtnPanel.add(rbtnSingle);
		modeBtnPanel.add(rbtnMulti);

		JPanel modePanel = new JPanel(new BorderLayout());
		JLabel lblMode = new JLabel("Card Selection Mode: ");
		modePanel.add(lblMode, BorderLayout.LINE_START);
		modePanel.add(modeBtnPanel, BorderLayout.CENTER);

		return modePanel;
	}
	
	private void warnCardValue() {
		// Disable adding card if nothing is input
		if (txtCardValue.getText().equals("")) {
			btnAddCard.setEnabled(false);
			lblAddCardError.setText("Please enter a non-negative number");
			lblAddCardError.setForeground(Color.BLACK);
			lblAddCardError.setVisible(true);
		}
		else {
			try {
				Integer.parseInt(txtCardValue.getText());
				btnAddCard.setEnabled(true);
				lblAddCardError.setVisible(false);
			// Disable and warn if it is not a number
			} catch (NumberFormatException ex) {
				btnAddCard.setEnabled(false);
				lblAddCardError.setVisible(true);
				lblAddCardError.setText("Card value must be a positive number");
				lblAddCardError.setForeground(Color.RED);
			}
		}

	}

}
