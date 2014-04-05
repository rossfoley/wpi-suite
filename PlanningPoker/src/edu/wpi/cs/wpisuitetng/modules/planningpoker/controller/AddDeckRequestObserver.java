/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.planningpoker.controller;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.Deck;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;
import edu.wpi.cs.wpisuitetng.network.models.ResponseModel;

/**
 * @author mandi1267
 *
 */
public class AddDeckRequestObserver {
	private AddDeckController controller;
	
	public AddDeckRequestObserver(AddDeckController controller) {
		this.controller = controller;
	}
	
	/**
	 * Parse the session that was received from the server then pass them to
	 * the controller.
	 * 
	 * @see edu.wpi.cs.wpisuitetng.network.RequestObserver#responseSuccess(edu.wpi.cs.wpisuitetng.network.models.IRequest)
	 */
	//@Override
	public void responseSuccess(IRequest iReq) {
		// Get the response to the given request
		final ResponseModel response = iReq.getResponse();
		//System.out.println (response);
		
		// Parse the session out of the response body
		final Deck addedDeck = Deck.fromJson(response.getBody());		
	}
	
	
	
	
}
