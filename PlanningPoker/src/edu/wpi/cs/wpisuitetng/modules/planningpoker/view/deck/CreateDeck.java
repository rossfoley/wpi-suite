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
import java.util.List;

import javax.swing.DropMode;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SpringLayout;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Font;


/**
 * This class is a sub-panel for creating a new set of cards to use for planning poker voting 
 * 
 * @version $Revision: 1.0 $
 */
public class CreateDeck extends JPanel {
	private String deckName;
	private JTextField textField;
	private List<Integer> listOfCards = new ArrayList<Integer>();
	private JButton btnRemove;
	private JButton btnRemoveAll;
	private JTable cardTable;
	private DefaultTableModel cardTableModel;
	private SpringLayout springLayout;


	public CreateDeck() {
		buildPanel();
	}

	private void buildPanel() {
		springLayout = new SpringLayout();
		setLayout(springLayout);

		JLabel cardLabel = new JLabel("New Card Value: ");
		cardLabel.setFont(new Font("Tahoma", Font.BOLD, 11));

		JButton btnAddCard = new JButton("Add Card:");
		btnAddCard.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					int value = Integer.parseInt(textField.getText());
					addCard(value);
					textField.setText("");
					refresh();
				} catch (NumberFormatException ex) {}
			}
		});

		textField = new JTextField();
		textField.setPreferredSize(new Dimension(26, 26));

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

		// Sprint layout constraints
		springLayout.putConstraint(SpringLayout.WEST, cardLabel, 10, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.VERTICAL_CENTER, cardLabel, 0, SpringLayout.VERTICAL_CENTER, textField);

		springLayout.putConstraint(SpringLayout.WEST, textField, 0, SpringLayout.EAST, cardLabel);
		springLayout.putConstraint(SpringLayout.NORTH, textField, 10, SpringLayout.NORTH, this);

		springLayout.putConstraint(SpringLayout.VERTICAL_CENTER, btnAddCard, 0, SpringLayout.VERTICAL_CENTER, textField);
		springLayout.putConstraint(SpringLayout.WEST, btnAddCard, 10, SpringLayout.EAST, textField);		

		springLayout.putConstraint(SpringLayout.EAST, cardScrollPane, 0, SpringLayout.EAST, textField);
		springLayout.putConstraint(SpringLayout.NORTH, cardScrollPane, 10, SpringLayout.SOUTH, textField);
		springLayout.putConstraint(SpringLayout.SOUTH, cardScrollPane, 0, SpringLayout.SOUTH, this);

		springLayout.putConstraint(SpringLayout.VERTICAL_CENTER, btnRemoveAll, 0, SpringLayout.VERTICAL_CENTER, cardScrollPane);
		springLayout.putConstraint(SpringLayout.WEST, btnRemoveAll, 10, SpringLayout.EAST, cardScrollPane);

		springLayout.putConstraint(SpringLayout.WEST, btnRemove, 0, SpringLayout.WEST, btnRemoveAll);
		springLayout.putConstraint(SpringLayout.NORTH, btnRemove, 5, SpringLayout.SOUTH, btnRemoveAll);

		// Add all components to the JPanel
		add(cardLabel);
		add(btnAddCard);
		add(textField);
		add(cardScrollPane);
		add(btnRemove);
		add(btnRemoveAll);

		refresh();
	}

	private void buildCardTable() {
		final String[] columnNames = {"Card(s)"};
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

}
