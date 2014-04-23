/*******************************************************************************
 * Copyright (c) 2014 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Team8s
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.planningpoker.controller;

import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.UserModel;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;

public class GetUserRequestObserver implements RequestObserver {
	private GetUserController controller;
	
	/**
	 * Constructs the observer given a GetDckController
	 * @param controller the controller used to retrieve decks
	 */
	public GetUserRequestObserver(GetUserController controller){
		this.controller = controller;
	}
	
	/**
	 * Parse the decks out of the response body and pass them to the controller
	 * 
	 * @see edu.wpi.cs.wpisuitetng.network.RequestObserver#responseSuccess(edu.wpi.cs.wpisuitetng.network.models.IRequest)
	 */
	@Override
	public void responseSuccess(IRequest iReq) {
		// Convert the JSON array of decks to a Deck object array
		UserModel.getInstance().emptyModel();
		User[] user= User.fromJsonArray(iReq.getResponse().getBody());
		for (int i = 0; i < user.length; i++) {
			controller.receivedUser(user[i]);
		}
		// Pass these Decks to the controller
//		controller.receivedUser(user);
	}
	
	/**
	 * @see edu.wpi.cs.wpisuitetng.network.RequestObserver#responseError(edu.wpi.cs.wpisuitetng.network.models.IRequest)
	 */
	@Override
	public void responseError(IRequest iReq) {
		fail(iReq, null);
	}
	
	/**
	 * handle request fails
	 * @param ireq network request
	 * @param exception 
	 *
	 * @see edu.wpi.cs.wpisuitetng.network.RequestObserver#fail(edu.wpi.cs.wpisuitetng.network.models.IRequest, java.lang.Exception)
	 */
	@Override
	public void fail(IRequest iReq, Exception exception) {
		System.out.println("Failed requst for getting User");
	}
	

}