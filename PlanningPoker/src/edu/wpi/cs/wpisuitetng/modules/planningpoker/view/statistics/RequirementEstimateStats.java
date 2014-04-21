package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.statistics;

import java.util.Collections;
import java.util.LinkedList;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.Estimate;

public class RequirementEstimateStats {
	private LinkedList<Estimate> estimates;
	private double mean;
	private double median;
	
	RequirementEstimateStats() {
		
	}
	
	/**
	 * calculates the mean value of the estimates 
	 * @return the mean value of the estimates 
	 */
	double calculateMean() {
		int sum = 0;
		for (Estimate e : estimates) {
			sum += e.getVote();
		}
		double theMean = sum/estimates.size();
		return theMean;
	}
	
	/**
	 * calculates the median of the estimates 
	 * assumes that the list of estimates is already sorted by vote value
	 * @return the median value of the estimates 
	 */
	double calculateMedian() {
		int size = estimates.size();
		if (size % 2 == 0) {
			int mid1 = size/2;
			int mid2 = size/2 - 1;
			int val1 = estimates.get(mid1).getVote();
			int val2 = estimates.get(mid2).getVote();
			return (val1+val2)/2;
		}
		else {
			int mid = size/2;
			return estimates.get(mid).getVote();
		}
	}
	
	/**
	 * sorts the list of estimates by vote value
	 */
	void sortEstimatesByVote() {
		  Collections.sort(estimates);
	}
	
	/**
	 * set this.mean to the mean value of the votes in the list of estimates 
	 */
	void setMean() {
		mean = calculateMean();
	}
	
	/**
	 * returns the mean
	 * @return mean
	 */
	double getMean() {
		return mean;
	}
	
	/**
	 * set this.median to the median value of the votes in the list of estimates
	 * @param theMedian
	 */
	void setMedian() {
		median = calculateMedian();
	}
	
	/**
	 * returns the median 
	 * @return median 
	 */
	double getMedian() {
		return median;
	}
	
	/**
	 * sets the list of estimates equal to the given list 
	 * @param givenEstimates
	 */
	void setEstimates(LinkedList<Estimate> givenEstimates) {
		estimates = givenEstimates;
	}
	
	/**
	 * returns the list of estimates 
	 * @return estimates
	 */
	LinkedList<Estimate> getEstimates() {
		return estimates;
	}
}
