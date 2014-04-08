/*******************************************************************************
 * Copyright (c) 2014 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Team8s
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.planningpoker.notifications;


import java.awt.Component;

import javax.swing.JOptionPane;

/**
 * 
 * @author Kevin Barry
 * Used for requirement 7 where we need to notify the user that there has been a session created.
 * Might be added to later for more notifications
 */
public class MockNotification {
	
	/**
	 * This is a mock notification where we use it to call methods that call up pop-ups.
	 */
	
	
	//Component cake;
	
	
	public MockNotification(){
	}
	
	/**
	 * Creates a pop-up that says that a session has been created.
	 */
	public void sessionStartedNotification(){
		JOptionPane.showMessageDialog(null, "You have notified the team of a session!");
	}
	
	
}

