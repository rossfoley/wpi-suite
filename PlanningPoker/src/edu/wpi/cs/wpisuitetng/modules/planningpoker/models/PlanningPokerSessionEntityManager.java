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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.exceptions.BadRequestException;
import edu.wpi.cs.wpisuitetng.exceptions.NotFoundException;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.EntityManager;
import edu.wpi.cs.wpisuitetng.modules.Model;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.controller.CheckForUpdatesController;

/**
 * @author rossfoley
 */
public class PlanningPokerSessionEntityManager implements EntityManager<PlanningPokerSession> {
	
	Data db;
	HashMap<String, ArrayList<PlanningPokerSession>> clientsUpdated = new HashMap<String, ArrayList<PlanningPokerSession>>();
	
	public PlanningPokerSessionEntityManager(final Data db) {
		this.db = db;
	}

	@Override
	public PlanningPokerSession makeEntity(Session s, String content) throws WPISuiteException {
		final PlanningPokerSession newSession = PlanningPokerSession.fromJson(content);
		if(!db.save(newSession, s.getProject())) {
			throw new WPISuiteException();
		}
		addClientUpdate(newSession, s);
		return newSession;
	}

	@Override
	public PlanningPokerSession[] getEntity(Session s, String id) throws NotFoundException, WPISuiteException {
		try {
			return db.retrieve(PlanningPokerSession.class, "uuid", UUID.fromString(id), s.getProject()).toArray(new PlanningPokerSession[0]);
		} catch (WPISuiteException e) {
			throw new NotFoundException(id);
		}
	}

	/**
	 * Retrieves all Planning Poker sessions from the database
	 * @param s the current session
	 * @return array of all of the current user's Planning Poker sessions
	 */
	@Override
	public PlanningPokerSession[] getAll(Session s) throws WPISuiteException {
		PlanningPokerSession [] allSessions = db.retrieveAll(new PlanningPokerSession(), s.getProject()).toArray(new PlanningPokerSession[0]);
		return allSessions;
	}

	/**
	 * Method update.
	 * @param session Session
	 * @param content String
	 * @return PlanningPokerSession 
	 * @throws WPISuiteException
	 * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#update(Session, String)
	 */
	@Override
	public PlanningPokerSession update(Session session, String content) throws WPISuiteException {
		PlanningPokerSession updatedSession = PlanningPokerSession.fromJson(content);
		/*
		 * Because of the disconnected objects problem in db4o, we can't just save PlanningPokerSession.
		 * We have to get the original defect from db4o, copy properties from updatedSession,
		 * then save the original PlanningPokerSession again.
		 */
		List<Model> oldSessions = db.retrieve(PlanningPokerSession.class, "uuid", updatedSession.getID(), session.getProject());
		if(oldSessions.size() < 1 || oldSessions.get(0) == null) {
			System.out.println("Problem with finding by the UUID");
			throw new BadRequestException("PlanningPokerSession with UUID does not exist.");
		}
				
		PlanningPokerSession existingSession = (PlanningPokerSession)oldSessions.get(0);		
		existingSession.copyFrom(updatedSession);
		
		if(!db.save(existingSession, session.getProject())) {
			System.out.println("Couldn't save the updated session");
			throw new WPISuiteException();
		}
		
		addClientUpdate(existingSession, session);
		return existingSession;
	}

	/* (non-Javadoc)
	 * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#save(edu.wpi.cs.wpisuitetng.Session, edu.wpi.cs.wpisuitetng.modules.Model)
	 */
	@Override
	public void save(Session s, PlanningPokerSession model) throws WPISuiteException {
		addClientUpdate(model, s);
		db.save(model);
	}

	@Override
	public boolean deleteEntity(Session s, String id) throws WPISuiteException {
		return db.delete(getEntity(s, id)[0]) != null;
	}
	
	@Override
	public void deleteAll(Session s) throws WPISuiteException {
		db.deleteAll(new PlanningPokerSession(), s.getProject());
	}

	@Override
	public int Count() throws WPISuiteException {
		return db.retrieveAll(new PlanningPokerSession()).size();
	}
	
	@Override
	public String advancedGet(Session s, String[] args) throws WPISuiteException {
		// Remove the Advanced/PlanningPokerSession part of the args
		args = Arrays.copyOfRange(args, 2, args.length);
		switch (args[0]) {
			case "check-for-updates":
				ArrayList<PlanningPokerSession> list = checkForUpdate(s.getSessionId());
				PlanningPokerSession[] updates = new PlanningPokerSession[list.size()];
				updates = list.toArray(updates);
				return new Gson().toJson(updates, PlanningPokerSession[].class);
			default:
				System.out.println(args[0]);
		}
		return null;
	}

	/**
	 * @param sessionId the current session ID
	 * @return an ArrayList of planning poker updates for the client to use
	 */
	private ArrayList<PlanningPokerSession> checkForUpdate(String sessionId) {
		if (!clientsUpdated.containsKey(sessionId)) {
			clientsUpdated.put(sessionId, new ArrayList<PlanningPokerSession>());
		}
		ArrayList<PlanningPokerSession> updates = clientsUpdated.get(sessionId);
		clientsUpdated.put(sessionId, new ArrayList<PlanningPokerSession>());
		return updates;
	}
	
	/**
	 * Add the specified session to the cached list of updates for live updating
	 * @param value the session that was created or updated
	 * @param s the current session
	 */
	private void addClientUpdate(PlanningPokerSession value, Session s) {
		for (String key : clientsUpdated.keySet()) {
			if (!s.getSessionId().equals(key)) {
				ArrayList<PlanningPokerSession> sessions = clientsUpdated.get(key);
				sessions.add(value);
				clientsUpdated.put(key, sessions);
			}
		}
	}

	@Override
	public String advancedPut(Session s, String[] args, String content)
			throws WPISuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String advancedPost(Session s, String string, String content)
			throws WPISuiteException {
		switch (string) {
			case "update-estimate":
				Estimate estimate = Estimate.fromJson(content);
				PlanningPokerSession pokerSession = getEntity(s, estimate.getSessionID().toString())[0];
				if (pokerSession.isOpen()) { 
					pokerSession.addEstimate(estimate);
					update(s, pokerSession.toJSON());
					addClientUpdate(pokerSession, s);
				} else {
					return "Voting for this session has ended!";
				}
			default:
				System.out.println(string);
		}

		return null;
	}
}
