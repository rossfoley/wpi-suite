/*******************************************************************************
 * Copyright (c) 2014 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Team8s
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.requirementcreation;

import java.awt.Color;

import java.util.LinkedList;
import java.util.List;

import javax.swing.JLabel;

/**
 * @author justinhess
 * @version $Revision: 1.0 $
 */
public class ErrorPanel extends JLabel {

	private final List<String> errorList;
	
	/**
	 * Constructor for the error panel which displays errors as needed.
	 */
	public ErrorPanel()
	{	
		errorList = new LinkedList<String>();
		setForeground(Color.RED);
		setAlignmentX(LEFT_ALIGNMENT);
	}
	
	/**
	 * Adds an error to the error list.
	 * @param msg the error to add
	 */
	public void displayError(String msg)
	{
		if(errorList.contains(msg)) return;
		errorList.add(msg);
		refreshErrors();
	}
	
	/**
	 * Removes all errors from the error list.
	 */
	public void removeAllErrors()
	{
		errorList.clear();
		refreshErrors();
	}
	
	/**
	 * Refreshes the error displayed at the bottom.
	 */
	public void refreshErrors()
	{
		setText("");
		for(String err : errorList)
		{
			setText(getText() + " " + err);
		}
	}
	
	/**
	
	 * @return whether there are outstanding errors */
	public boolean hasErrors()
	{
		return errorList.size() != 0;
	}

	/**
	 * Removes the given error from the error list.
	 * @param msg the error to remove
	 */
	public void removeError(String msg) {
		errorList.remove(msg);
		refreshErrors();
	}
}
