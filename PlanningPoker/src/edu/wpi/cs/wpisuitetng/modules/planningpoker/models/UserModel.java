/*******************************************************************************
 * Copyright (c) 2014 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Team8s
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.planningpoker.models;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;

import edu.wpi.cs.wpisuitetng.modules.core.models.User;

public class UserModel extends AbstractListModel {
	
	private final List<User> existingUser;	
	private static UserModel instance; // static object to allow the decklistmodel 
	

	/**
	 * Constructs an list of decks for the User that contains only the default deck
	 */
	private UserModel(){
		existingUser = new ArrayList<User>();
	}
	
	/** 
	 * if the decklistmodel has not yet been created, create it
	 * @return the instance of the decklistmodel 
	 */
	public static UserModel getInstance(){
		if (instance == null){
			instance = new UserModel();
		}
		return instance;
	}

	@Override
	public int getSize() {
		return existingUser.size();
	}

	/**
	 * Finds a deck with the deck numbers matching the given list
	 * 
	 * @param deckNums list of numbers to match to decks in decklistmodel
	 */
	public List<User> getUsers(){
		return existingUser;
	}

	@Override
	public Object getElementAt(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	public void emptyModel() {
		existingUser.clear();
		
	}

	public void addUser(User user) {
		existingUser.add(user);		
	}


}
