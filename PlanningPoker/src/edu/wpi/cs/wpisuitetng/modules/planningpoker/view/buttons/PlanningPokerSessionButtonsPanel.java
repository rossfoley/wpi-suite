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

import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.ToolbarGroupView;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.controller.GetEmailController;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.EmailAddress;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.EmailAddressModel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSessionModel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.notifications.Mailer;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.ViewEventController;


/**
 * This class contains the toolbar buttons for the planning poker module. 
 *
 */
public class PlanningPokerSessionButtonsPanel extends ToolbarGroupView{
	private JButton createButton = new JButton("<html>Create <br /> Session</html>");
	private JButton editButton = new JButton("<html>Edit <br /> Session</html>");
	private JButton voteButton = new JButton("<html>Vote on<br/> Session</html>");
	private JButton endVoteButton = new JButton("<html>End Session<br />Voting</html>");
	private JButton statisticsButton = new JButton("<html>View<br /> Statistics</html>");
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
			this.endVoteButton.setIcon(new ImageIcon(img));
			
			img = ImageIO.read(
					new File("../PlanningPoker/src/edu/wpi/cs/wpisuitetng/modules/planningpoker/view/buttons/bar_chart.png"));
			//getClass().getResource("new_req.png"));	// this should work... but doesn't...
			this.statisticsButton.setIcon(new ImageIcon(img));
			

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
				final PlanningPokerSession session = ViewEventController.getInstance().getOverviewDetailPanel().getCurrentSession();
				session.setGameState(PlanningPokerSession.SessionState.VOTINGENDED);
				PlanningPokerSessionModel.getInstance().updatePlanningPokerSession(session);
				ViewEventController.getInstance().getOverviewTreePanel().refresh();
				
				final List<String> recipients = new LinkedList<String>();
				List<EmailAddress> emailRecipients = null;
				
				GetEmailController getEmailController = GetEmailController.getInstance();
				getEmailController.retrieveEmails();
				
				EmailAddressModel emailAddressModel = EmailAddressModel.getInstance();
				try {
					emailRecipients = emailAddressModel.getEmailAddresses();
				}
				catch (Exception E) {
					
				}
				
				for (int i = 0; i < emailRecipients.size(); i++) {
					recipients.add(emailRecipients.get(i).getEmail());
				}
				
				
				Thread t = new Thread(new Runnable() {
					
					@Override
					public void run() {
						Mailer mailer = new Mailer();
						mailer.notifyOfPlanningPokerSessionClose(recipients, session);
					}
				});
				t.setDaemon(true);
				t.run();
				
				
			}
		});	
		
		// the action listener for the View Statistics Button
		statisticsButton.addActionListener(new ActionListener() {
			@Override
			
			public void actionPerformed(ActionEvent e) {
				PlanningPokerSession session = ViewEventController.getInstance().getOverviewDetailPanel().getCurrentSession();
				ViewEventController.getInstance().openStatisticsTab(session);
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
