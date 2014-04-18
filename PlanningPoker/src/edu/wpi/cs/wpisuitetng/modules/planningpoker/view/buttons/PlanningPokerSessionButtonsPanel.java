package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.buttons;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.ToolbarGroupView;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSessionModel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.ViewEventController;


/**
 * This class contains the toolbar buttons for the planning poker module. 
 *
 */
public class PlanningPokerSessionButtonsPanel extends ToolbarGroupView {
	private JButton createButton = new JButton("<html>Create<br />Planning Poker Session</html>");
	private JButton editButton = new JButton("<html>Edit<br />Planning Poker Session</html>");
	private JButton voteButton = new JButton("<html>Vote on<br />Planning Poker Session</html>");
	private JButton endVoteButton = new JButton("<html>End Voting on<br />Planning Poker Session</html>");
	private final JPanel contentPanel = new JPanel();

	public PlanningPokerSessionButtonsPanel(){
		super("");

		ViewEventController.getInstance().setPlanningPokerSessionButtonsPanel(this);

		disableEditButton();
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.X_AXIS));
		setPreferredWidth(1000);
		createButton.setHorizontalAlignment(SwingConstants.CENTER);
		// Add image icon for the create planning poker session button
		try {
			Image img = ImageIO.read(
					new File("../PlanningPoker/src/edu/wpi/cs/wpisuitetng/modules/planningpoker/view/buttons/new_req.png"));
			//getClass().getResource("new_req.png"));	// this should work... but doesn't...
			createButton.setIcon(new ImageIcon(img));

			img = ImageIO.read(
					new File("../PlanningPoker/src/edu/wpi/cs/wpisuitetng/modules/planningpoker/view/buttons/edit.png"));
			//getClass().getResource("edit.png"));	// this should work... but doesn't...
			editButton.setIcon(new ImageIcon(img));
			
			img = ImageIO.read(
					new File("../PlanningPoker/src/edu/wpi/cs/wpisuitetng/modules/planningpoker/view/buttons/voting-icon.png"));
			//getClass().getResource("voting-icon.png"));	// this should work... but doesn't...
			voteButton.setIcon(new ImageIcon(img));
			
			img = ImageIO.read(
					new File("../PlanningPoker/src/edu/wpi/cs/wpisuitetng/modules/planningpoker/view/buttons/end-icon.png"));
			//getClass().getResource("end-icon.png"));	// this should work... but doesn't...
			endVoteButton.setIcon(new ImageIcon(img));

		} catch (IOException | NullPointerException | IllegalArgumentException ex) {}; 

		// the action listener for the Create Planning Poker Session Button
		createButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ViewEventController.getInstance().createPlanningPokerSession();
			}
		});	

		//action listener for the Edit Session Button
		editButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				PlanningPokerSession session = ViewEventController.getInstance().getOverviewDetailPanel().getCurrentSession();
				ViewEventController.getInstance().editSession(session);
			}
		});		

		// the action listener for the Vote Planning Poker Session Button
		voteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				PlanningPokerSession session = ViewEventController.getInstance().getOverviewDetailPanel().getCurrentSession();
				ViewEventController.getInstance().voteOnSession(session);
			}
		});	

		// the action listener for the End Vote Planning Poker Session Button
		endVoteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				PlanningPokerSession session = ViewEventController.getInstance().getOverviewDetailPanel().getCurrentSession();
				session.setGameState(PlanningPokerSession.SessionState.VOTINGENDED);
				PlanningPokerSessionModel.getInstance().updatePlanningPokerSession(session);
				ViewEventController.getInstance().getOverviewTreePanel().refresh();
			}
		});	


		contentPanel.add(createButton);
		contentPanel.add(editButton);
		contentPanel.add(voteButton);
		contentPanel.add(endVoteButton);
		contentPanel.setOpaque(false);

		add(contentPanel);
	}

	/**
	 * Method getCreateButton.
	 * @return JButton */
	public JButton getCreateButton() {
		return this.createButton;
	}

	/**
	 * Method geteditButton
	 * @return JButton */
	public JButton getCreateIterationButton() {
		return this.editButton;
	}

	public void disableEditButton() {
		editButton.setEnabled(false);
	}

	public void enableEditButton() {
		editButton.setEnabled(true);
	}

	public void disableVoteButton() {
		voteButton.setEnabled(false);
	}

	public void enableVoteButton() {
		voteButton.setEnabled(true);
	}

	public void disableEndVoteButton() {
		endVoteButton.setEnabled(false);
	}

	public void enableEndVoteButton() {
		endVoteButton.setEnabled(true);
	}
}
