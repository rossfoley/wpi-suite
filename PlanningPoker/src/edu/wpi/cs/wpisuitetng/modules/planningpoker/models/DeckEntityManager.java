/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.planningpoker.models;

import java.util.List;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.exceptions.BadRequestException;
import edu.wpi.cs.wpisuitetng.exceptions.NotFoundException;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.EntityManager;
import edu.wpi.cs.wpisuitetng.modules.Model;
import edu.wpi.cs.wpisuitetng.modules.core.models.Role;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;

/**
 * @author Amanda Adkins
 *
 */
public class DeckEntityManager implements EntityManager<Deck> {

	Data db;
	
	public DeckEntityManager(Data db){
		this.db = db;
	}
	
	//@Override
	
	@Override
	public String advancedGet(Session s, String[] args)
			throws WPISuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	
	
	@Override
	public String advancedPost(Session s, String string, String content)
			throws WPISuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String advancedPut(Session s, String[] args, String content)
			throws WPISuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int Count() throws WPISuiteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void deleteAll(Session s) throws WPISuiteException {
		// don't know if we should restrict deleting decks to admins
		// 		ensureRole(s, Role.ADMIN);
		db.deleteAll(new Deck(), s.getProject());
		
	}

	@Override
	public boolean deleteEntity(Session s, String id) throws WPISuiteException {
		// don't know if we should restrict deck deletion to admins
		//ensureRole(s, Role.ADMIN);
		return (db.delete(getEntity(s, id)[0]) != null) ? true : false;
	}

	@Override
	public Deck[] getAll(Session s) throws WPISuiteException {
		return db.retrieveAll(new Deck(), s.getProject()).toArray(new Deck[0]);
	}

	@Override
	public Deck[] getEntity(Session s, String id) throws NotFoundException,
			WPISuiteException {
			final int intId = Integer.parseInt(id);
			if(intId < 1) {
				throw new NotFoundException();
			}
			Deck[] decks = null;
			try {
				decks = db.retrieve(Deck.class, "id", intId, s.getProject()).toArray(new Deck[0]);
			} catch (WPISuiteException e) {
				e.printStackTrace();
			}
			if(decks.length < 1 || decks[0] == null) {
				throw new NotFoundException();
			}
			return decks;
	}

	public Deck makeEntity(Session s, String content) throws WPISuiteException {
		final Deck newDeck = Deck.fromJson(content);
		if(!db.save(newDeck, s.getProject())) {
			throw new WPISuiteException();
		}
		return newDeck;
	}

	public void save(Session s, Deck model) throws WPISuiteException {
		db.save(model, s.getProject());
	}

	@Override
	public Deck update(Session s, String content) throws WPISuiteException {
		
		Deck updatedDeck = Deck.fromJson(content);
		/*
		 * Because of the disconnected objects problem in db4o, we can't just save Requirements.
		 * We have to get the original defect from db4o, copy properties from updatedRequirement,
		 * then save the original Requirement again.
		 */
		List<Model> oldDecks = db.retrieve(Deck.class, "id", updatedDeck.getId(), s.getProject());
		if(oldDecks.size() < 1 || oldDecks.get(0) == null) {
			throw new BadRequestException("Deck with ID does not exist.");
		}
				
		Deck existingDeck = (Deck) oldDecks.get(0);		

		// copy values to old requirement and fill in our changeset appropriately
		existingDeck.copyFrom(updatedDeck);
		
		if(!db.save(existingDeck, s.getProject())) {
			throw new WPISuiteException();
		}
		
		return existingDeck;
	}
}
