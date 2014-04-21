package edu.wpi.cs.wpisuitetng.modules.planningpoker.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.UserModel;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

public class GetUserController implements ActionListener {
	private GetUserRequestObserver observer;
	private static GetUserController instance;
	
	/**
	 * Constructs the controller given a Deck
	 */
	private GetUserController() {
		observer = new GetUserRequestObserver(this);
	}
	
	
	/**
	
	 * @return the instance of the GetDeckController or creates one if it does not
	 * exist. */
	public static GetUserController getInstance()
	{
		if(instance == null)
		{
			instance = new GetUserController();
		}
		
		return instance;
	}
	
	/**
	 * Sends an HTTP request to store a requirement when the
	 * update button is pressed
	 * @param e ActionEvent
	
	 * @see java.awt.event.ActionListener#actionPerformed(ActionEvent) */
	@Override
	public void actionPerformed(ActionEvent e) {
		// Send a request to the core to save this requirement
		final Request request = Network.getInstance().makeRequest("core/user", HttpMethod.GET); // GET == read
		request.addObserver(observer); // add an observer to process the response
		request.send(); // send the request
	}
	
	/**
	 * Sends an HTTP request to retrieve all decks
	 */
	public void retrieveUsers() {
		final Request request = Network.getInstance().makeRequest("core/user", HttpMethod.GET); // GET == read
		request.addObserver(observer); // add an observer to process the response
		request.send(); // send the request
	}
	
	/**
	 * Add the given requirements to the local model (they were received from the core).
	 * This method is called by the GetRequirementsRequestObserver
	 * 
	 * @param requirements array of requirements received from the server
	 */
	
	public void receivedUser(User User) {
		// Empty the local model to eliminate duplications
		// UserModel.getInstance().emptyModel();
		// Make sure the response was not null
		if (User != null) {
			
			// add the requirements to the local model
			UserModel.getInstance().addUser(User);
		}
	}

}