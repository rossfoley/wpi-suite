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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.lang.Math; 

import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.Estimate;

/**
 * This class holds a list of estimates sorted by vote value
 * and both calculates and holds the mean and median values
 * for that list of elements 
 * 
 * @author Rick Wight (fmwight)
 */
public class RequirementEstimateStats {
	private int ID; 
	private List<Estimate> estimates;
	private double mean;
	private double median;
	private double stdDev; 
	
	public RequirementEstimateStats(int reqID, List<Estimate> givenEstimates) {
		setEstimates(givenEstimates);
		refreshAll(); 
	}
	
	/**
	 * calculates the mean value of the estimates 
	 * @return the mean value of the estimates 
	 */
	public double calculateMean() {
		double sum = 0;
		for (Estimate e : estimates) {
			sum += e.getVote();
		}
		double theMean = 0; 
		if (estimates.size() != 0) {
			theMean = sum/estimates.size();
		}
		return theMean;
	}
	
	/**
	 * calculates the median of the estimates 
	 * assumes that the list of estimates is already sorted by vote value
	 * @return the median value of the estimates 
	 */
	public double calculateMedian() {
		final int size = estimates.size();
		if (size == 0) {
			return 0; 
		}
		else if (size % 2 == 0) {
			final int mid1 = size/2;
			final int mid2 = size/2 - 1;
			final int val1 = estimates.get(mid1).getVote();
			final int val2 = estimates.get(mid2).getVote();
			return (val1+val2)/2;
		}
		else {
			final int mid = size/2;
			return estimates.get(mid).getVote();
		}
	}
	
	/**
	 * calculate the population standard deviation of the votes in the list of estimates
	 * assumes that the mean has already been calculated for this list of estimates 
	 * @return the standard deviation 
	 */
	public double calculateStdDev() {
		double var = 0;
		for (Estimate e : estimates) {
			var += Math.pow(e.getVote() - mean, 2);
			System.out.println("var = " + var + ", mean = " + mean + ", e.getVote() = " + e.getVote());
		}
		if (estimates.size() == 0) {
			return 0;
		}
		else {
			return Math.sqrt(var/estimates.size());
		}
	}
	
	/**
	 * sorts the list of estimates by vote value
	 */
	public void sortEstimatesByVote() {
		  Collections.sort(estimates);
	}
	
	/**
	 * calls the methods to sort the list of estimates, then calculates and sets 
	 * the mean, median, and population standard deviation in the appropriate order 
	 */
	public void refreshAll() {
		sortEstimatesByVote();
		setMean();
		setMedian();
		setStdDev();
	}
	
	/**
	 * set this.mean to the mean value of the votes in the list of estimates 
	 */
	public void setMean() {
		mean = calculateMean();
	}
	
	/**
	 * returns the mean
	 * @return mean
	 */
	public double getMean() {
		return mean;
	}
	
	/**
	 * set this.median to the median value of the votes in the list of estimates
	 * @param theMedian
	 */
	public void setMedian() {
		median = calculateMedian();
	}
	
	/**
	 * returns the median 
	 * @return median 
	 */
	public double getMedian() {
		return median;
	}
	
	/**
	 * sets the list of estimates equal to the given list 
	 * @param givenEstimates
	 */
	public void setEstimates(List<Estimate> givenEstimates) {
		estimates = givenEstimates;
	}
	
	/**
	 * returns the list of estimates 
	 * @return estimates
	 */
	public List<Estimate> getEstimates() {
		return estimates;
	}
	
	/**
	 * set this.stdDev to the population standard deviation of the votes
	 * in the list of estimates 
	 */
	public void setStdDev() {
		stdDev = calculateStdDev();
	}
	
	/**
	 * get the population standard deviation of the estimates 
	 * @return stdDev 
	 */
	public double getStdDev() {
		return stdDev;
	}

	/**
	 * get the ID number of the requirement
	 * @return ID
	 */
	public int getID() {
		return ID;
	}

	/**
	 * set the ID number to the ID of the requirement
	 * @param ID
	 */
	public void setID(int iD) {
		ID = iD;
	}
	
	/**
	 * adds estimate anEstimate to the list of estimates
	 * @param anEstimate
	 */
	public void add(Estimate anEstimate) {
		estimates.add(anEstimate); 
	}
}
