package edu.wpi.cs.wpisuitetng.modules.planningpoker.controller;

import java.awt.event.ActionEvent;

import edu.wpi.cs.wpisuitetng.modules.core.models.Project;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.Deck;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.DeckListModel;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;

public class GetProjectRequestObserver implements RequestObserver {
	private GetProjectController controller;
	
	/**
	 * Constructs the observer given a GetDckController
	 * @param controller the controller used to retrieve decks
	 */
	public GetProjectRequestObserver(GetProjectController controller){
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
		Project project= Project.fromJSON(iReq.getResponse().getBody());
		
		// Pass these Decks to the controller
		controller.receivedProject(project);
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
		System.out.println("Failed requst for getting project");
		System.out.println("Failed requst for getting project");
		System.out.println("Failed requst for getting project");
		System.out.println("Failed requst for getting project");
	}
	

}