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

import java.util.Collections;
import java.util.List;
import java.lang.Math; 

import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.Estimate;

/**
 * This class holds a list of estimates sorted by vote value
 * and both calculates and holds the mean, median, and standard 
 * deviation values for that list of elements. 
 * 
 * @author Rick Wight (fmwight)
 */
public class RequirementEstimateStats {
	private int ID; 
	private List<Estimate> estimates;
	private double mean;
	private double median;
	private double stdDev;
	private boolean isUpToDate;
	
	public RequirementEstimateStats(int reqID, List<Estimate> givenEstimates) {
		setID(reqID); 
		setEstimates(givenEstimates);
		refreshAll(); 
	}
	
	/**
	 * Check if the current statistics are all up to date. If yes, return mean. 
	 * If no, calculate the mean value of the votes in the list of estimates, 
	 * update mean with that value, and return that value. 
	 * @return the mean value of the estimates 
	 */
	public double getMean() {
		if (isUpToDate) {
			return mean;
		}
		else {
			double sum = 0;
			for (Estimate e : estimates) {
				sum += e.getVote();
			}
			double theMean = 0;
			if (estimates.size() != 0) {
				theMean = sum/estimates.size();
			}
			mean = theMean;
			return theMean;
		}
	}
	
	/**
	 * Check if the current statistics are all up to date. If yes, return median. 
	 * If no, calculate the median of the votes in the list of estimates, update
	 * median with that value, and return that value. 
	 * Assumes that the list of estimates is already sorted by vote value. 
	 * @return the median value of the estimates 
	 */
	public double getMedian() {
		if (isUpToDate) {
			return median;
		}
		else {
			double theMedian;
			final int size = estimates.size();
			if (size == 0) {
				return 0;
			}
			else if (size % 2 == 0) {
				final int mid1 = size/2;
				final int mid2 = size/2 - 1;
				final int val1 = estimates.get(mid1).getVote();
				final int val2 = estimates.get(mid2).getVote();
				theMedian = (val1+val2)/2.0;
				median = theMedian;
				return theMedian;
			}
			else {
				final int mid = size/2;
				theMedian = estimates.get(mid).getVote();
				median = theMedian;
				return theMedian;
			}
		}
	}
	
	/**
	 * Check if the current statistics are all up to date. If yes, return stdDev. 
	 * If no, calculate the population standard deviation of the votes in the list 
	 * of estimates, update stdDev with that value, and return that value. 
	 * Assumes that the mean has already been calculated for this list of estimates 
	 * @return the standard deviation 
	 */
	public double getStdDev() {
		if (isUpToDate) {
			return stdDev;
		}
		else {
			double var = 0;
			double theStdDev;
			for (Estimate e : estimates) {
				var += Math.pow(e.getVote() - mean, 2);
			}
			if (estimates.size() == 0) {
				stdDev = 0;
				return 0;
			}
			else {
				theStdDev = Math.sqrt(var/estimates.size());
				stdDev = theStdDev;
				return theStdDev;
			}
		}
	}
	
	/**
	 * Sorts the list of estimates by vote value
	 */
	public void sortEstimatesByVote() {
		  Collections.sort(estimates);
	}
	
	/**
<<<<<<< HEAD
	 * Prints out the ID, mean, median, and stdDev
	 */
	public void printStats() {
		System.out.println("Printing stats. ID = " + ID + ", mean = " + mean 
				+ ", median = " + median + ", stdDev = " + stdDev );
	}
	
	/**
=======
>>>>>>> statistics page now has a standard deviation field. Refractored requirementEstimateStats and updated comments
	 * Calls the methods to sort the list of estimates, then updates the 
	 * mean, median, and stdDev fields and sets isUpToDate to true. 
	 */
	public void refreshAll() {
		sortEstimatesByVote();
		getMean();
		getMedian();
		getStdDev();
		isUpToDate = true;
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
	 * Get the ID number of the requirement
	 * @return ID
	 */
	public int getID() {
		return ID;
	}

	/**
	 * Set the ID number to the ID of the requirement
	 * @param ID
	 */
	public void setID(int iD) {
		ID = iD;
	}
	
	/**
	 * Adds estimate anEstimate to the list of estimates and sets isUpToDate to false. 
	 * @param anEstimate
	 */
	public void add(Estimate anEstimate) {
		estimates.add(anEstimate);
		isUpToDate = false; 
	}
	
	public void addAndRefresh(Estimate anEstimate) {
		add(anEstimate); 
		refreshAll(); 
	}
}
