/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.planningpoker.controller;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.Deck;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;

/**
 * This observer handles responses to request for all requirements
 * 
 * @author Amanda Adkins
 *
 */
public class GetDeckRequestObserver implements RequestObserver {
	
	private GetDeckController controller;
	
	
	/**
	 * Constructs the observer given a GetDckController
	 * @param controller the controller used to retrieve decks
	 */
	public GetDeckRequestObserver(GetDeckController controller){
		this.controller = controller;
	}
	
	/**
	 * Parse the requirements out of the response body and pass them to the controller
	 * 
	 * @see edu.wpi.cs.wpisuitetng.network.RequestObserver#responseSuccess(edu.wpi.cs.wpisuitetng.network.models.IRequest)
	 */
	@Override
	public void responseSuccess(IRequest iReq) {
		// Convert the JSON array of requirements to a Requirement object array
		Deck[] decks= Deck.fromJsonArray(iReq.getResponse().getBody());
		
		// Pass these Requirements to the controller
		controller.receivedDecks(decks);
	}
	
	/**
	 * @see edu.wpi.cs.wpisuitetng.network.RequestObserver#responseError(edu.wpi.cs.wpisuitetng.network.models.IRequest)
	 */
	@Override
	public void responseError(IRequest iReq) {
		fail(iReq, null);
	}
	
	/**
	 * Put an error requirement in the PostBoardPanel if the request fails.
	 * 
	 * @see edu.wpi.cs.wpisuitetng.network.RequestObserver#fail(edu.wpi.cs.wpisuitetng.network.models.IRequest, java.lang.Exception)
	 */
	@Override
	public void fail(IRequest iReq, Exception exception) {
	//	PlanningPokerSession[] errorSession = { new PlanningPokerSession(6, "Error", "error desc") };
	//	controller.receivedSessions(errorSession);
	}
	

}
