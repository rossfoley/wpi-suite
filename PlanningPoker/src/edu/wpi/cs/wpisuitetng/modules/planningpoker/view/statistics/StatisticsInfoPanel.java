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

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import java.text.DateFormat;
import java.util.GregorianCalendar;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SpringLayout;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.ViewEventController;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;

import java.awt.Dimension;

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
	SpringLayout springLayout;
	int currentReqID;
	
	public StatisticsInfoPanel(PlanningPokerSession session) {

		ViewEventController.getInstance().setStatisticsInfoPanel(this);
		
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
		reqDescriptionDisplay = new JTextArea();
		springLayout = new SpringLayout();


		setLayout(springLayout);
		scrollPane.setViewportView(reqDescriptionDisplay);
		reqDescriptionDisplay.setWrapStyleWord(true);
		reqDescriptionDisplay.setLineWrap(true);
		reqDescriptionDisplay.setEditable(false);
		
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
	}
	
	public void refresh(PlanningPokerSession session) {
		
		// Change session description
		reqDescriptionDisplay.setText(session.getDescription());
		
		
		//RequirementEstimateStats reqStats = new RequirementEstimateStats();

	}

	
	//format estimate mean to a string
	public String formatMean(RequirementEstimateStats stats){
		String mean = "";
		mean = Double.toString(stats.getMean());
		return mean;
	}
	//format estimate median to a string
	public String formatMedian(RequirementEstimateStats stats){
		String median = "";
		median = Double.toString(stats.getMedian());
		return median;
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
		springLayout.putConstraint(SpringLayout.EAST, scrollPane, -67, SpringLayout.EAST, this);
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
		
		
	}


}
