/*******************************************************************************
 * Copyright (c) 2014 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Team8s
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.buttons;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import edu.wpi.cs.wpisuitetng.janeway.config.ConfigManager;
import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.ToolbarGroupView;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.controller.GetEmailController;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.EmailAddress;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.EmailAddressModel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSessionModel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.notifications.Mailer;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.ViewEventController;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.ViewMode;


/**
 * This class contains the tool-bar buttons for the planning poker module. 
 *
 */
public class PlanningPokerSessionButtonsPanel extends ToolbarGroupView{
	private final JButton createButton = new JButton("<html>Create <br /> Session</html>");
	private final JButton editButton = new JButton("<html>Edit <br /> Session</html>");
	private final JButton voteButton = new JButton("<html>Vote on<br/> Session</html>");
	private final JButton endVoteButton = new JButton("<html>End Session<br />Voting</html>");
	private final JButton statisticsButton = new JButton("<html>View<br /> Statistics</html>");
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
			Image img = ImageIO.read(getClass().getResource("new_req.png"));
			createButton.setIcon(new ImageIcon(img));

			img = ImageIO.read(getClass().getResource("edit.png"));
			editButton.setIcon(new ImageIcon(img));

			img = ImageIO.read(getClass().getResource("voting-icon.png"));
			voteButton.setIcon(new ImageIcon(img));

			img = ImageIO.read(getClass().getResource("end-icon.png"));
			endVoteButton.setIcon(new ImageIcon(img));
			//getClass().getResource("new_req.png"));	// this should work... but doesn't...
			endVoteButton.setIcon(new ImageIcon(img));

			img = ImageIO.read(
					new File("../PlanningPoker/src/edu/wpi/cs/wpisuitetng/modules/planningpoker/view/buttons/bar_chart.png"));
			//getClass().getResource("new_req.png"));	// this should work... but doesn't...
			statisticsButton.setIcon(new ImageIcon(img));


		} catch (IOException | NullPointerException | IllegalArgumentException ex) {} 

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
				final PlanningPokerSession session = ViewEventController.getInstance().getOverviewDetailPanel().getCurrentSession();
				ViewEventController.getInstance().openSessionTab(session, ViewMode.EDITING);
			}
		});		

		// the action listener for the Vote Planning Poker Session Button
		voteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				final PlanningPokerSession session = ViewEventController.getInstance().getOverviewDetailPanel().getCurrentSession();
				ViewEventController.getInstance().openSessionTab(session, ViewMode.VOTING);
			}
		});	

		// the action listener for the End Vote Planning Poker Session Button
		endVoteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				final PlanningPokerSession session = ViewEventController.getInstance().getOverviewDetailPanel().getCurrentSession();
				session.setGameState(PlanningPokerSession.SessionState.VOTINGENDED);
				PlanningPokerSessionModel.getInstance().updatePlanningPokerSession(session);
				ViewEventController.getInstance().getOverviewTreePanel().refresh();

				final List<String> recipients = new LinkedList<String>();
				List<EmailAddress> emailRecipients = null;

				final GetEmailController getEmailController = GetEmailController.getInstance();
				getEmailController.retrieveEmails();

				final EmailAddressModel emailAddressModel = EmailAddressModel.getInstance();
				try {
					emailRecipients = emailAddressModel.getEmailAddresses();
				}
				catch (Exception E) {

				}

				for (int i = 0; i < emailRecipients.size(); i++) {
					recipients.add(emailRecipients.get(i).getEmail());
				}

				final Thread t = new Thread(new Runnable() {

					@Override
					public void run() {
						final Mailer mailer = new Mailer();
						mailer.notifyOfPlanningPokerSessionClose(recipients, session);
					}
				});
				t.setDaemon(true);
				t.start();


			}
		});	

		// the action listener for the View Statistics Button
		statisticsButton.addActionListener(new ActionListener() {
			@Override

			public void actionPerformed(ActionEvent e) {
				final PlanningPokerSession session = ViewEventController.getInstance().getOverviewDetailPanel().getCurrentSession();
				ViewEventController.getInstance().openSessionTab(session, ViewMode.STATISTICS);
			}
		});


		contentPanel.add(createButton);
		contentPanel.add(editButton);
		contentPanel.add(voteButton);
		contentPanel.add(endVoteButton);
		contentPanel.add(statisticsButton);
		contentPanel.setOpaque(false);

		add(contentPanel);
	}

	/**
	 * Method getCreateButton.
	 * @return JButton */
	public JButton getCreateButton() {
		return createButton;
	}

	/**
	 * Getter for the edit button
	 * @return The edit JButton
	 */
	public JButton getEditButton() {
		return this.editButton;
	}
	
	/**
	 * Getter for the vote button
	 * @return The vote JButton
	 */
	public JButton getVoteButton() {
		return this.voteButton;
	}
	
	/**
	 * Getter for the end vote button
	 * @return The end vote JButton
	 */
	public JButton getEndVoteButton() {
		return this.endVoteButton;
	}
	
	/**
	 * Getter for the statistics button
	 * @return The end vote JButton
	 */
	public JButton getStatisticsButton() {
		return this.statisticsButton;
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

	public void disableStatisticsButton() {
		statisticsButton.setEnabled(false);
	}

	public void enableStatisticsButton() {
		statisticsButton.setEnabled(true);
	}

	/**
	 * Enables the tool-bar buttons based on the input PlanningPoker and Client session 
	 * @param session	The planningPoker session to use
	 */
	public void enableButtonsForSession(PlanningPokerSession session) {
		final String sessionOwner = session.getSessionCreatorName();

		// Disable everything by default
		disableAllButtons();

		// If the current user is the owner of the session
		if (sessionOwner.equals(ConfigManager.getConfig().getUserName())) {
			// Enable editing if pending and not open or opened in editing mode
			if (session.isPending()) {
				enableEditButton();
			}
			// Allow end of voting if open and editing if no estimates yet
			else if (session.isOpen()) {
				enableEndVoteButton();
				// If no estimates yet, allow editing
				if (session.isEditable()) {
					enableEditButton();
				}
			}
		}

		// If session is open, allow voting
		if (session.isOpen()) {
			enableVoteButton();
		}

		// If the session is ended or closed, allow the user to view statistics
		if (session.isEnded() || session.isClosed()) {
			enableStatisticsButton();
		}
	}
	
	/**
	 * Disables all the buttons
	 */
	public void disableAllButtons() {
		disableEditButton();
		disableVoteButton();
		disableEndVoteButton();
		disableStatisticsButton();		
	}
}

