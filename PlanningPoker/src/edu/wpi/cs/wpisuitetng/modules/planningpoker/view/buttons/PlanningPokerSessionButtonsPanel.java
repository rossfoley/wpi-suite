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
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.ViewEventController;


/**
 * @author amandaadkins
 *
 */
public class PlanningPokerSessionButtonsPanel extends ToolbarGroupView{
	private JButton createButton = new JButton("<html>Create<br />Planning Poker Session</html>");
	private JButton editButton = new JButton("<html>Edit<br />Planning Poker Session</html>");
	private final JPanel contentPanel = new JPanel();
	
	private boolean sessionSelected;
	
	public void disableEditButton() {
		editButton.setEnabled(false);
	}
	
	public void enableEditButton() {
		editButton.setEnabled(true);
	}
	
	public void updatePanel(PlanningPokerSession session){
		
	}
	
	public PlanningPokerSessionButtonsPanel(){
		super("");
		this.disableEditButton();
		this.contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.X_AXIS));
		this.setPreferredWidth(350);
		this.createButton.setHorizontalAlignment(SwingConstants.CENTER);
		// Add image icon for the create planning poker session button
		try {
		    Image img = ImageIO.read(
		    		new File("../PlanningPoker/src/edu/wpi/cs/wpisuitetng/modules/planningpoker/view/buttons/new_req.png"));
		    		//getClass().getResource("new_req.png"));	// this should work... but doesn't...
		    this.createButton.setIcon(new ImageIcon(img));
		    
		    img = ImageIO.read(
		    		new File("../PlanningPoker/src/edu/wpi/cs/wpisuitetng/modules/planningpoker/view/buttons/edit.png"));
		    		//getClass().getResource("new_req.png"));	// this should work... but doesn't...
		    this.editButton.setIcon(new ImageIcon(img));
		    
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
			
		contentPanel.add(createButton);
		contentPanel.add(editButton);
		contentPanel.setOpaque(false);

		this.add(contentPanel);
	}

	/**
	 * Method getCreateButton.
	
	 * @return JButton */
	public JButton getCreateButton() {
		return createButton;
	}


	/**
	 * Method geteditButton
	
	 * @return JButton */
	public JButton getCreateIterationButton() {
		return editButton;
	}
}
