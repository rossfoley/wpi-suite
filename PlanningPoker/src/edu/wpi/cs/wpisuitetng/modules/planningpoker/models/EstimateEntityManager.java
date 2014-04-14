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

import java.util.List;
import java.util.UUID;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.exceptions.BadRequestException;
import edu.wpi.cs.wpisuitetng.exceptions.ConflictException;
import edu.wpi.cs.wpisuitetng.exceptions.NotFoundException;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.EntityManager;
import edu.wpi.cs.wpisuitetng.modules.Model;

public class EstimateEntityManager implements EntityManager<Estimate> {
	Data db;
	
	public EstimateEntityManager(Data db) {
		this.db = db;
	}

	@Override
	public Estimate makeEntity(Session s, String content) throws WPISuiteException {
		final Estimate newEstimate = Estimate.fromJson(content);
		if(!db.save(newEstimate, s.getProject())) {
			throw new WPISuiteException();
		}
		return newEstimate;
	}

	@Override
	public Estimate[] getEntity(Session s, String id) throws NotFoundException, WPISuiteException {
		try {
			return db.retrieve(Estimate.class, "uuid", UUID.fromString(id), s.getProject()).toArray(new Estimate[0]);
		} catch (WPISuiteException e) {
			throw new NotFoundException(id);
		}
	}

	@Override
	public Estimate[] getAll(Session s) throws WPISuiteException {
		Estimate [] allEstimates = db.retrieveAll(new Estimate(), s.getProject()).toArray(new Estimate[0]);
		return allEstimates;
	}

	@Override
	public Estimate update(Session s, String content) throws WPISuiteException {
		Estimate updatedEstimate = Estimate.fromJson(content);
		/*
		 * Because of the disconnected objects problem in db4o, we can't just save Estimate.
		 * We have to get the original defect from db4o, copy properties from updatedSession,
		 * then save the original Estimate again.
		 */
		List<Model> oldEstimates = db.retrieve(Estimate.class, "uuid", updatedEstimate.getID(), s.getProject());
		if(oldEstimates.size() < 1 || oldEstimates.get(0) == null) {
			System.out.println("Problem with finding by the UUID");
			throw new BadRequestException("Estimate with UUID does not exist.");
		}
				
		Estimate existingEstimate = (Estimate)oldEstimates.get(0);		
		existingEstimate.copyFrom(updatedEstimate);
		
		if(!db.save(existingEstimate, s.getProject())) {
			System.out.println("Couldn't save the updated estimate");
			throw new WPISuiteException();
		}
		
		return existingEstimate;
	}

	@Override
	public void save(Session s, Estimate model) throws WPISuiteException {
		db.save(model);
	}

	@Override
	public boolean deleteEntity(Session s, String id) throws WPISuiteException {
		return db.delete(getEntity(s,id)[0]) != null;
	}

	@Override
	public String advancedGet(Session s, String[] args)
			throws WPISuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteAll(Session s) throws WPISuiteException {
		db.deleteAll(new Estimate(), s.getProject());
		
	}

	@Override
	public int Count() throws WPISuiteException {
		return db.retrieveAll(new Estimate()).size();
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
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
