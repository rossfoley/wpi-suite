/*******************************************************************************
 * Copyright (c) 2014 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Team8s
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.statistics;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.text.DecimalFormat;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SpringLayout;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSessionModel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.ViewEventController;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.controller.UpdateRequirementController;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.RequirementModel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * The general information (name, description etc) for a given session
 * that's being displayed in the overview detail panel
 * 
 * Top half of the overviewDetailPanel split pane
 */
public class StatisticsInfoPanel extends JPanel {
	JLabel lblReqName;
	JLabel lblReqDescription;
	JLabel lblMean;
	JLabel lblMedian;
	JLabel lblStdDev;
	JLabel reqNameDisplay;
	JTextArea reqDescriptionDisplay;
	JScrollPane scrollPane;
	JLabel meanDisplay;
	JLabel medianDisplay;
	JLabel stdDevDisplay;
	JLabel enterEstimateLabel;
	SpringLayout springLayout;
	private JTextField estimateField;
	private int userEstimate;
	private final JLabel estimateFieldErrorMessage = new JLabel("");
	private final JLabel estimateSubmittedMessage = new JLabel("Your final estimate has been submitted");
	private final JLabel estimateUnchangedMessage = new JLabel("This is the current estimate");
	private JButton submitFinalEstimateButton;
	private int currentReqID = -1;
	private Requirement aReq; 
	private final PlanningPokerSession session;
	
	public StatisticsInfoPanel(PlanningPokerSession aSession) {
		session = aSession; 
		lblReqName = new JLabel("Name:");
		reqNameDisplay = new JLabel("");
		lblReqDescription = new JLabel("Description:");
		scrollPane = new JScrollPane();
		lblMean = new JLabel("Mean:");
		lblMedian = new JLabel("Median:");
		lblStdDev = new JLabel("Std Dev:");
		meanDisplay = new JLabel();
		medianDisplay = new JLabel();
		stdDevDisplay = new JLabel();
		enterEstimateLabel = new JLabel("Enter a final estimate");
		reqDescriptionDisplay = new JTextArea();
		springLayout = new SpringLayout();


		setLayout(springLayout);
		scrollPane.setViewportView(reqDescriptionDisplay);
		reqDescriptionDisplay.setWrapStyleWord(true);
		reqDescriptionDisplay.setLineWrap(true);
		reqDescriptionDisplay.setEditable(false);
		
		buildSubmitEstimateField();
		
		setConstraints();
		
		add(lblReqName);
		add(reqNameDisplay);
		add(lblReqDescription);
		add(scrollPane);
		add(lblMean);
		add(lblMedian);
		add(lblStdDev);
		add(meanDisplay);
		add(medianDisplay);
		add(stdDevDisplay);
		add(estimateField);
		add(enterEstimateLabel);
		add(submitFinalEstimateButton);
		add(estimateFieldErrorMessage);
		add(estimateSubmittedMessage);
		add(estimateUnchangedMessage);
	}
	
	public void refresh(PlanningPokerSession session) {

		if ((aReq != null) && (currentReqID != -1)) {
			
			if (session.isEnded() || session.isClosed()) {
				session.addReqEstimateStats(currentReqID);
			}
			
			// Change session name
			reqNameDisplay.setText(aReq.getName());
			
			// Change session description
			reqDescriptionDisplay.setText(aReq.getDescription());
			
			// autofill final estimate submission field with current final estimate if one exists
			// or the mean value of the votes on this estimate if there is no existing final estimate 
			if (session.getReqsWithExportedEstimatesList().contains(aReq)) {
				Integer existingReqEstimate = RequirementModel.getInstance().getRequirement(currentReqID).getEstimate();
				estimateField.setText(existingReqEstimate.toString());
				submitFinalEstimateButton.setText("Resubmit Final Estimate");
			}
			else {
				estimateField.setText(formatMeanAsInt(session.getReqEstimateStats().get(currentReqID)));
				submitFinalEstimateButton.setText("Submit Final Estimate");
			}
			
			submitFinalEstimateButton.setEnabled(true);
			estimateField.setEnabled(true);
			
			/**
			 *  change mean display
			 */
			meanDisplay.setText(formatMean(session.getReqEstimateStats().get(currentReqID)));
			
			/**
			 * change median display
			 */
			medianDisplay.setText(formatMedian(session.getReqEstimateStats().get(currentReqID)));
			
			/**
			 * change standard deviation display 
			 */
			stdDevDisplay.setText(formatStdDev(session.getReqEstimateStats().get(currentReqID)));
		}
		else {
			estimateField.setText("");
		}
		
		estimateSubmittedMessage.setVisible(false);
		estimateUnchangedMessage.setVisible(false);
	}
	
	/**
	 * Create the text box to submit estimates
	 */
	public void buildSubmitEstimateField() {
		estimateFieldErrorMessage.setForeground(Color.RED);
		estimateSubmittedMessage.setForeground(Color.BLUE);
		estimateSubmittedMessage.setVisible(false);
		estimateUnchangedMessage.setVisible(false);
		
		estimateField = new JTextField();
		estimateField.setHorizontalAlignment(SwingConstants.CENTER);
		estimateField.setFont(new Font("Tahoma", Font.PLAIN, 50));
		estimateField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				estimateValueChange();
			}
			@Override
			public void insertUpdate(DocumentEvent e) {
				estimateValueChange();
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
				estimateValueChange();
			}
		});
		
		estimateField.setPreferredSize(new Dimension(200, 150));
		estimateField.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				estimateSubmittedMessage.setVisible(false);
				estimateUnchangedMessage.setVisible(false);
			}
		});
		
		if (!(session.getReqsWithExportedEstimatesList().contains(aReq))) {
			submitFinalEstimateButton = new JButton("Submit Final Estimate");
		}
		else {
			submitFinalEstimateButton = new JButton("Resubmit Final Estimate");
		}
		
		if (currentReqID < 0) {
			submitFinalEstimateButton.setEnabled(false);
			estimateField.setEnabled(false);
		}
		
		submitFinalEstimateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					userEstimate = Integer.parseInt(estimateField.getText());
					if (validateEstimate()) {
						session.addFinalEstimate(currentReqID, userEstimate);
						sendFinalEstimate(aReq);
						PlanningPokerSessionModel.getInstance().updatePlanningPokerSession(session);
					}
				}
				catch (NumberFormatException ne) {
					// give deliberately failing value to produce an error message
					userEstimate = -1;
					validateEstimate();
				}
			}
		});
	}
	
	/**
	 * set the current estimate value to whatever is in the estimateField
	 */
	private void estimateValueChange() {
		try {
			userEstimate = Integer.parseInt(estimateField.getText());
			validateEstimate();
		}
		catch (NumberFormatException e) {
			// give deliberately failing value 
			userEstimate = -1;
			validateEstimate();
		}
	}
	
	/**
	 * @return true if the field contains a positive integer
	 */
	private boolean validateEstimate() {
		if (userEstimate < 0) {
			estimateFieldErrorMessage.setText("Please enter a positive integer");
			estimateFieldErrorMessage.setVisible(true);
			estimateFieldErrorMessage.revalidate();
			estimateFieldErrorMessage.repaint();
			submitFinalEstimateButton.setEnabled(false);
			return false;
		}
		else {
			estimateFieldErrorMessage.setText("");
			estimateFieldErrorMessage.setVisible(false);
			estimateFieldErrorMessage.revalidate();
			estimateFieldErrorMessage.repaint();
			//TODO find out if this is a worthwhile feature
			if (RequirementModel.getInstance().getRequirement(currentReqID).getEstimate() == userEstimate) {
				estimateUnchangedMessage.setVisible(true);
				submitFinalEstimateButton.setEnabled(false);
				return false;
			}
			else {
				estimateUnchangedMessage.setVisible(false);
				submitFinalEstimateButton.setEnabled(true);
				return true;
			}
		}
	}

	
	//format estimate mean to a string
	public String formatMean(RequirementEstimateStats stats){
		String mean = "";
		DecimalFormat df = new DecimalFormat("##0.0");
		mean = df.format(stats.getMean());
		return mean;
	}
	//format estimate median to a string
	public String formatMedian(RequirementEstimateStats stats){
		String median = "";
		DecimalFormat df = new DecimalFormat("##0.0");
		median = df.format(stats.getMedian());
		return median;
	}
	//format estimate standard deviation to a string
	public String formatStdDev(RequirementEstimateStats stats) {
		String stdDev = "";
		DecimalFormat df = new DecimalFormat("##0.0");
		stdDev = df.format(stats.getStdDev());
		return stdDev;
	}
	//format estimate mean to a string with no decimals
	public String formatMeanAsInt(RequirementEstimateStats stats) {
		String mean = "";
		DecimalFormat df = new DecimalFormat("##0");
		mean = df.format(stats.getMean());
		return mean;
	}
	
	//set selected requirement ID
	public void setRequirementID(int ID) {
		reqNameDisplay.setText(Integer.toString(ID));
	}
	
	
	private void setConstraints() {
		springLayout.putConstraint(SpringLayout.WEST, stdDevDisplay, 0, SpringLayout.WEST, meanDisplay);
		springLayout.putConstraint(SpringLayout.EAST, stdDevDisplay, -122, SpringLayout.EAST, this);
		springLayout.putConstraint(SpringLayout.WEST, meanDisplay, 75, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.WEST, medianDisplay, 6, SpringLayout.EAST, lblMedian);
		springLayout.putConstraint(SpringLayout.EAST, lblReqName, -6, SpringLayout.WEST, reqNameDisplay);
		springLayout.putConstraint(SpringLayout.WEST, reqNameDisplay, 105, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.NORTH, scrollPane, 6, SpringLayout.SOUTH, lblReqDescription);
		springLayout.putConstraint(SpringLayout.WEST, scrollPane, 20, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.SOUTH, scrollPane, -16, SpringLayout.NORTH, lblMean);
		springLayout.putConstraint(SpringLayout.EAST, scrollPane, -67, SpringLayout.WEST, estimateField);
		springLayout.putConstraint(SpringLayout.EAST, lblReqDescription, 138, SpringLayout.WEST, this);
		
		springLayout.putConstraint(SpringLayout.NORTH, lblReqName, 11, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, lblReqName, 10, SpringLayout.WEST, this);
		
		springLayout.putConstraint(SpringLayout.NORTH, reqNameDisplay, 11, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.SOUTH, reqNameDisplay, 25, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.EAST, reqNameDisplay, 383, SpringLayout.WEST, this);
		
		springLayout.putConstraint(SpringLayout.NORTH, lblReqDescription, 36, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, lblReqDescription, 10, SpringLayout.WEST, this);
		
		springLayout.putConstraint(SpringLayout.NORTH, lblMean, 137, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, lblMean, 10, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.EAST, lblMean, 69, SpringLayout.WEST, this);
		
		springLayout.putConstraint(SpringLayout.NORTH, lblMedian, 155, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, lblMedian, 10, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.EAST, lblMedian, 69, SpringLayout.WEST, this);
		
		springLayout.putConstraint(SpringLayout.NORTH, lblStdDev, 173, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, lblStdDev, 10, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.EAST, lblStdDev, 56, SpringLayout.WEST, this);
		
		springLayout.putConstraint(SpringLayout.NORTH, meanDisplay, 137, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.SOUTH, meanDisplay, 151, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.EAST, meanDisplay, 328, SpringLayout.WEST, this);
		
		springLayout.putConstraint(SpringLayout.NORTH, medianDisplay, 155, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.SOUTH, medianDisplay, 169, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.EAST, medianDisplay, 328, SpringLayout.WEST, this);
		
		springLayout.putConstraint(SpringLayout.NORTH, stdDevDisplay, 173, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.SOUTH, stdDevDisplay, 187, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.EAST, stdDevDisplay, 328, SpringLayout.WEST, this);
		
		springLayout.putConstraint(SpringLayout.NORTH, enterEstimateLabel, 11, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, enterEstimateLabel, 0, SpringLayout.HORIZONTAL_CENTER, estimateField);
		
		springLayout.putConstraint(SpringLayout.NORTH, estimateField, 10, SpringLayout.SOUTH, enterEstimateLabel);
		springLayout.putConstraint(SpringLayout.EAST, estimateField, -40, SpringLayout.EAST, this);
		springLayout.putConstraint(SpringLayout.WEST, estimateField, -200, SpringLayout.EAST, this);
		
		springLayout.putConstraint(SpringLayout.NORTH, submitFinalEstimateButton, 25, SpringLayout.SOUTH, estimateField);
		springLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, submitFinalEstimateButton, 0, SpringLayout.HORIZONTAL_CENTER, estimateField);
		
		springLayout.putConstraint(SpringLayout.NORTH, estimateFieldErrorMessage, 7, SpringLayout.SOUTH, estimateField);
		springLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, estimateFieldErrorMessage, 0, SpringLayout.HORIZONTAL_CENTER, estimateField);
		
		springLayout.putConstraint(SpringLayout.NORTH, estimateSubmittedMessage, 7, SpringLayout.SOUTH, estimateField);
		springLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, estimateSubmittedMessage, 0, SpringLayout.HORIZONTAL_CENTER, estimateField);
		
		springLayout.putConstraint(SpringLayout.NORTH, estimateUnchangedMessage, 7, SpringLayout.SOUTH, estimateField);
		springLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, estimateUnchangedMessage, 0, SpringLayout.HORIZONTAL_CENTER, estimateField);
	}

	public void setCurrentReqID(int ID) {
		currentReqID = ID;
		aReq = RequirementModel.getInstance().getRequirement(ID);
		refresh(session);
	}
	
	public void sendFinalEstimate(Requirement reqToSendFinalEstimate) {
		boolean hadValidReason = true;
		if (session.getReqsWithExportedEstimatesList().contains(reqToSendFinalEstimate)) {
			//TODO get confirmation on popup as a valid way of asking for the explanation
			String explanation = JOptionPane.showInputDialog(this, "Please enter the reason for this change", JOptionPane.QUESTION_MESSAGE);
			if (explanation == null || explanation.isEmpty()) {
				//TODO check for valid input inside the popup window
				hadValidReason = false;
			}
			else {
				System.out.println("explanation was: " + explanation);
				session.addReqWithExplainedChange(reqToSendFinalEstimate.getId(), explanation);
			}
		}
		if (hadValidReason) {
			reqToSendFinalEstimate.setEstimate(session.getFinalEstimates().get(reqToSendFinalEstimate.getId()));
			session.addRequirementToExportedList(reqToSendFinalEstimate.getId());
			UpdateRequirementController.getInstance().updateRequirement(reqToSendFinalEstimate);
			RequirementManagerController.getInstance().refreshReqManagerTable();
			ViewEventController.getInstance().getStatisticsPanel().refresh();
			ViewEventController.getInstance().getOverviewDetailPanel().updatePanel(session);
			estimateSubmittedMessage.setVisible(true);
		}
	}


}
