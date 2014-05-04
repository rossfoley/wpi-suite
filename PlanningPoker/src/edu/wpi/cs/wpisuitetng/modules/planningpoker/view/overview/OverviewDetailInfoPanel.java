/*******************************************************************************
 * Copyright (c) 2014 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Team8s
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.overview;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.util.GregorianCalendar;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;

import edu.wpi.cs.wpisuitetng.janeway.config.ConfigManager;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.Deck;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.DeckListModel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession.SessionState;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSessionModel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.ViewEventController;

/**
 * The general information (name, description etc) for a given session
 * that's being displayed in the overview detail panel
 * 
 * Top half of the overviewDetailPanel split pane
 * @version 4/18/14
 */
public class OverviewDetailInfoPanel extends JPanel {
	JLabel lblSessionName;
	JLabel lblSessionDescription;
	JLabel lblEndDate;
	JLabel lblEndTime;
	JLabel lblDeck;
	JLabel sessionNameDisplay;
	JTextArea sessionDescriptionDisplay;
	JScrollPane scrollPane;
	JLabel endDateDisplay;
	JLabel endTimeDisplay;
	JLabel deckDisplay;
	JLabel sessionCreatorDisplay;
	SpringLayout springLayout;
	JButton overviewDetailButton = new JButton("button");
	PlanningPokerSession currentSession;
	public OverviewDetailInfoPanel() {

		lblSessionName = new JLabel("Session Name:");
		sessionNameDisplay = new JLabel("");
		lblSessionDescription = new JLabel("Session Description:");
		scrollPane = new JScrollPane();
		lblEndDate = new JLabel("End Date:");
		lblEndTime = new JLabel("End Time:");
		lblDeck = new JLabel("Deck:");
		endDateDisplay = new JLabel();
		endTimeDisplay = new JLabel();
		deckDisplay = new JLabel();
		sessionCreatorDisplay = new JLabel("");
		sessionDescriptionDisplay = new JTextArea();
		springLayout = new SpringLayout();
		

		sessionCreatorDisplay.setHorizontalAlignment(SwingConstants.TRAILING);
		sessionCreatorDisplay.setMaximumSize(new Dimension(250, 14));
		setLayout(springLayout);
		scrollPane.setViewportView(sessionDescriptionDisplay);
		sessionDescriptionDisplay.setWrapStyleWord(true);
		sessionDescriptionDisplay.setLineWrap(true);
		sessionDescriptionDisplay.setEditable(false);
		overviewDetailButton.setVisible(false);
		
		createConstraints();
		
		add(lblSessionName);
		add(sessionNameDisplay);
		add(lblSessionDescription);
		add(scrollPane);
		add(lblEndDate);
		add(lblEndTime);
		add(lblDeck);
		add(endDateDisplay);
		add(endTimeDisplay);
		add(deckDisplay);
		add(sessionCreatorDisplay);
		add(overviewDetailButton);
		
		//open session button action listener
		overviewDetailButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (currentSession.getGameState() == SessionState.PENDING){
					currentSession.setGameState(SessionState.OPEN);
				}
				else if (currentSession.getGameState() == SessionState.OPEN){
					currentSession.setGameState(SessionState.VOTINGENDED);
				}
				else if (currentSession.getGameState() == SessionState.VOTINGENDED){
					currentSession.setGameState(SessionState.CLOSED);
				}
				PlanningPokerSessionModel.getInstance().updatePlanningPokerSession(currentSession);
				OverviewTreePanel treePanel = ViewEventController.getInstance().getOverviewTreePanel();
				treePanel.refresh();
				updateOverviewButton(currentSession);
				ViewEventController.getInstance().getPlanningPokerSessionButtonsPanel().enableButtonsForSession(currentSession);
			}
		});
	}
	
	/**
	 * Changes the information in this panel to be the given session
	 * @param session The session to display information about
	 */
	public void refresh(PlanningPokerSession session) {
		currentSession = session;
		// Change session name
		sessionNameDisplay.setText(session.getName());
		
		// Change session description
		sessionDescriptionDisplay.setText(session.getDescription());
		
		// Change session creator
		sessionCreatorDisplay.setText("Session Creator: " + session.getSessionCreatorName());
		
	
		updateOverviewButton(session);
		
	
		// Change end date
		String endDate, endTime;
		try {
			endDate = DateFormat.getDateInstance(DateFormat.FULL)
					.format(session.getEndDate().getTime());
		} catch (NullPointerException ex) {
			endDate = "No end date";
		}
		endDateDisplay.setText(endDate);
		
		// Change end time
		try {
			final GregorianCalendar sessionDate = session.getEndDate();
			final String hour = formatHour(sessionDate);
			final String minute = formatMinute(sessionDate);
			final String am_pm = formatAM_PM(sessionDate);
			endTime = hour + ":" + minute + am_pm;
			
		} catch (NullPointerException ex) {
			endTime = "No end time";
		}
		endTimeDisplay.setText(endTime);
		
		
		// Change deck name
		if (session.isUsingDeck()) {
			Deck sessionDeck = DeckListModel.getInstance().getDeck(session.getSessionDeckID());	
			deckDisplay.setText(sessionDeck.getDeckName());
		}
		else {
			deckDisplay.setText("None");
		}
	}
	
	public void updateOverviewButton(PlanningPokerSession session){
		//restrict ability to open/end voting on/close a session to session creator
		if ((ConfigManager.getConfig().getUserName().equals(session.getSessionCreatorName())) && ((session.getGameState() == SessionState.PENDING) || 
				(session.getGameState() == SessionState.OPEN) || (session.getGameState() == SessionState.VOTINGENDED)) || (session.getGameState() == SessionState.CLOSED)){
			overviewDetailButton.setVisible(true);

			// Set button to open session button for pending sessions
			if(session.getGameState() == SessionState.PENDING) {
				overviewDetailButton.setText("Open Session for Voting");
				overviewDetailButton.setEnabled(true);
			}
			
			// Set button to end session button for open sessions 
			else if(session.getGameState() == SessionState.OPEN) {
				overviewDetailButton.setText("End Session Voting");
				overviewDetailButton.setEnabled(true);
			}
			// Set button to close session for ended sessions
			else if (session.getGameState() == SessionState.VOTINGENDED) {
				overviewDetailButton.setText("Archive Session");
				//Check if there are final estimates 
				int finalEstimateSize = session.getFinalEstimates().size();
				System.out.println(finalEstimateSize);
				if (session.getFinalEstimates().size() != session.requirementsGetSize()) {
					overviewDetailButton.setEnabled(false);
				}
				else {
					overviewDetailButton.setEnabled(true);
				}
		}
		else {
				overviewDetailButton.setVisible(false);
			}
		}
	}
	
	/**
	 * Formats the minute of a given GregorianCalendar 
	 * @param date The GregorianCalendar date
	 * @return A formatted minute string
	 */
	public String formatMinute(GregorianCalendar date){
		String minute = "";
		if(date.get(GregorianCalendar.MINUTE) == 0){
			minute = Integer.toString(date.get(GregorianCalendar.MINUTE)) + "0";
		}
		else{
			minute = Integer.toString(date.get(GregorianCalendar.MINUTE));
		}
		return minute;
	}
	
	/**
	 * Formats the hour of a given GregorianCalendar 
	 * @param date The GregorianCalendar date
	 * @return A formatted hour string
	 */
	public String formatHour(GregorianCalendar date){
		String hour = "";
		if(date.get(GregorianCalendar.HOUR) == 0){
			hour = "12";
		}
		else{
			hour = Integer.toString(date.get(GregorianCalendar.HOUR));
		}
		return hour;
	}

	/**
	 * Formats the AM/PM of a given GregorianCalendar 
	 * @param date The GregorianCalendar date
	 * @return A formatted AM or PM string
	 */
	public String formatAM_PM(GregorianCalendar date){
		String AM_PM = "";
		if(date.get(GregorianCalendar.AM_PM) == 0){
			AM_PM = "AM";
		}
		else{
			AM_PM = "PM";
		}
		return AM_PM;
	}
	
	/**
	 * Creates the constraints for the panel
	 */
	private void createConstraints() {
		springLayout.putConstraint(SpringLayout.WEST, deckDisplay, 0, SpringLayout.WEST, endDateDisplay);
		springLayout.putConstraint(SpringLayout.EAST, deckDisplay, -122, SpringLayout.EAST, this);
		springLayout.putConstraint(SpringLayout.WEST, endDateDisplay, 75, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.WEST, endTimeDisplay, 6, SpringLayout.EAST, lblEndTime);
		springLayout.putConstraint(SpringLayout.EAST, lblSessionName, -6, SpringLayout.WEST, sessionNameDisplay);
		springLayout.putConstraint(SpringLayout.WEST, sessionNameDisplay, 105, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.NORTH, scrollPane, 6, SpringLayout.SOUTH, lblSessionDescription);
		springLayout.putConstraint(SpringLayout.WEST, scrollPane, 20, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.SOUTH, scrollPane, -16, SpringLayout.NORTH, lblEndDate);
		springLayout.putConstraint(SpringLayout.EAST, scrollPane, -67, SpringLayout.EAST, this);
		springLayout.putConstraint(SpringLayout.EAST, lblSessionDescription, 138, SpringLayout.WEST, this);
		
		springLayout.putConstraint(SpringLayout.NORTH, lblSessionName, 11, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, lblSessionName, 10, SpringLayout.WEST, this);
		
		springLayout.putConstraint(SpringLayout.NORTH, sessionNameDisplay, 11, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.SOUTH, sessionNameDisplay, 25, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.EAST, sessionNameDisplay, 383, SpringLayout.WEST, this);
		
		springLayout.putConstraint(SpringLayout.NORTH, lblSessionDescription, 36, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, lblSessionDescription, 10, SpringLayout.WEST, this);
		
		springLayout.putConstraint(SpringLayout.NORTH, lblEndDate, 137, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, lblEndDate, 10, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.EAST, lblEndDate, 69, SpringLayout.WEST, this);
		
		springLayout.putConstraint(SpringLayout.NORTH, lblEndTime, 155, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, lblEndTime, 10, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.EAST, lblEndTime, 69, SpringLayout.WEST, this);
		
		springLayout.putConstraint(SpringLayout.NORTH, lblDeck, 173, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, lblDeck, 10, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.EAST, lblDeck, 56, SpringLayout.WEST, this);
		
		springLayout.putConstraint(SpringLayout.NORTH, endDateDisplay, 137, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.SOUTH, endDateDisplay, 151, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.EAST, endDateDisplay, 328, SpringLayout.WEST, this);
		
		springLayout.putConstraint(SpringLayout.NORTH, endTimeDisplay, 155, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.SOUTH, endTimeDisplay, 169, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.EAST, endTimeDisplay, 328, SpringLayout.WEST, this);
		
		springLayout.putConstraint(SpringLayout.NORTH, deckDisplay, 173, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.SOUTH, deckDisplay, 187, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.EAST, deckDisplay, 328, SpringLayout.WEST, this);
		
		springLayout.putConstraint(SpringLayout.SOUTH, sessionCreatorDisplay, 0, SpringLayout.SOUTH, lblSessionName);
		springLayout.putConstraint(SpringLayout.EAST, sessionCreatorDisplay, -10, SpringLayout.EAST, this);
		
		springLayout.putConstraint(SpringLayout.SOUTH, overviewDetailButton, -10, SpringLayout.SOUTH, this);
		springLayout.putConstraint(SpringLayout.EAST, overviewDetailButton, -10, SpringLayout.EAST, this);

	}
	
	/**
	 * checks to see if all of the final estimates have been used to update requirement manager
	 * @param session - session to compare against
	 * @return true if all of the final estimates have been sent
	 */
	public boolean areAllEstimatesSent(PlanningPokerSession session){
		boolean allMatched = true;
		
		for (Integer finalEstimateID:session.getFinalEstimates().keySet()){
			boolean foundThisOne = false;
			for (Integer reqID:session.getRequirementsWithExportedEstimates()){
				if (finalEstimateID == reqID){
					foundThisOne = true;
				}
			}
			allMatched &= foundThisOne;
		}
		return allMatched;
	}
	
	/**
	 * clear all information from the table
	 */
	public void clearPanel(){
		overviewDetailButton.setVisible(false);
		sessionNameDisplay.setText("");
		sessionDescriptionDisplay.setText("");
		endDateDisplay.setText("");
		endTimeDisplay.setText("");
		deckDisplay.setText("");
		sessionCreatorDisplay.setText("");
	}
	
}
