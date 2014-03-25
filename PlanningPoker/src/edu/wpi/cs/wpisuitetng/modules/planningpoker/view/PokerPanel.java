package edu.wpi.cs.wpisuitetng.modules.planningpoker.view;

import java.awt.Component;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
//import javax.swing.JList;
import javax.swing.JPanel;
//import javax.swing.JScrollPane;
//import javax.swing.JTextField;

/**
 * This class is a JPanel. It contains the "cards" for the poker game, 
 * and a submit button for submitting the card.
 * 
 * @author jdorich
 *
 */
@SuppressWarnings({"serial"})
public class PokerPanel extends JPanel {

	/** A list box to display all the message on the board */
	private final JLabel userStoryMessage;
	/** Estimation cards */
	private final JPanel cardPanel;
	private final JButton[] btnCards;
	/** A button for submitting a card */
	private final JButton btnSubmit;

	/**
	 * Construct the poker game, including the deck of cards and
	 * button to submit a card. 
	 */
	public PokerPanel() {
		
		//Set the layout manager of this panel that controls the positions of the components
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS)); // components will  be arranged vertically

		// JPanel for the card buttons  
		cardPanel = new JPanel(new GridLayout(1, 6, 10, 30));
		userStoryMessage = new JLabel("As a ___ I would like to ___ so that ___");
		
		// Construct the components to be displayed
		// Submit Score button
		btnSubmit = new JButton("Submit");
		// Estimation Cards
		btnCards = new JButton[6];
		// Initialize and set properties
		for(int x = 0; x < btnCards.length; x++) {
			btnCards[x] = new JButton();
			btnCards[x].setMargin(new Insets(0,0,0,0));
			btnCards[x].setPreferredSize(new Dimension(160, 200));
		}

		// Add image for all card buttons
		//reference: package edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.buttons;
		try {
			Image img = ImageIO.read(new File("../PlanningPoker/src/card2.jpg"));
			btnCards[0].setIcon(new ImageIcon(img));
			img = ImageIO.read(new File("../PlanningPoker/src/card4.jpg"));
			btnCards[1].setIcon(new ImageIcon(img));
			img = ImageIO.read(new File("../PlanningPoker/src/card6.jpg"));
			btnCards[2].setIcon(new ImageIcon(img));
			img = ImageIO.read(new File("../PlanningPoker/src/card8.jpg"));
			btnCards[3].setIcon(new ImageIcon(img));
			img = ImageIO.read(new File("../PlanningPoker/src/card10.jpg"));
			btnCards[4].setIcon(new ImageIcon(img));
			img = ImageIO.read(new File("../PlanningPoker/src/cardKing.jpg"));
			btnCards[5].setIcon(new ImageIcon(img));
		} catch (IOException ex) {}
		
//		try {
//			Image img = ImageIO.read(getClass().getResource("card2.jpg"));
//			btnCards[0].setIcon(new ImageIcon(img));
//			img = ImageIO.read(getClass().getResource("card4.jpg"));
//			btnCards[1].setIcon(new ImageIcon(img));
//			img = ImageIO.read(getClass().getResource("card6.jpg"));
//			btnCards[2].setIcon(new ImageIcon(img));
//			img = ImageIO.read(getClass().getResource("card8.jpg"));
//			btnCards[3].setIcon(new ImageIcon(img));
//			img = ImageIO.read(getClass().getResource("card10.jpg"));
//			btnCards[4].setIcon(new ImageIcon(img));
//			img = ImageIO.read(getClass().getResource("cardKing.jpg"));
//			btnCards[5].setIcon(new ImageIcon(img));
//		} catch (IOException ex) {}
		
		// Adjust sizes and alignments
		btnSubmit.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnSubmit.setPreferredSize(new Dimension(50, 25));
		userStoryMessage.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		// Add the components to the panel
		cardPanel.add(btnCards[0]);
		cardPanel.add(btnCards[1]);
		cardPanel.add(btnCards[2]);
		cardPanel.add(btnCards[3]);
		cardPanel.add(btnCards[4]);
		cardPanel.add(btnCards[5]);
		add(userStoryMessage);
		add(Box.createVerticalStrut(60));
		add(cardPanel);
		add(Box.createVerticalStrut(60));
		add(btnSubmit);
		setOpaque(true);
	}

}
