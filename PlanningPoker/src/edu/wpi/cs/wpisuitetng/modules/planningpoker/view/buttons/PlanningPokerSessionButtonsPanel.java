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
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.ViewEventController;


/**
 * @author amandaadkins
 *
 */
public class PlanningPokerSessionButtonsPanel extends ToolbarGroupView{
	private JButton createButton = new JButton("<html>Create<br />Planning Poker Session</html>");
	private final JPanel contentPanel = new JPanel();
	public boolean flag = true;
	
	public PlanningPokerSessionButtonsPanel(){
		super("");
		
		this.contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.X_AXIS));
		this.setPreferredWidth(350);
		this.createButton.setHorizontalAlignment(SwingConstants.CENTER);
		// Add image icon for the create planning poker session button
		try {
		    Image img = ImageIO.read(
		    		new File("../PlanningPoker/src/edu/wpi/cs/wpisuitetng/modules/planningpoker/view/buttons/new_req.png"));
		    		//getClass().getResource("new_req.png"));	// this should work... but doesn't...
		    this.createButton.setIcon(new ImageIcon(img));
		} catch (IOException | NullPointerException | IllegalArgumentException ex) {}; 
		
		// the action listener for the Create Planning Poker Session Button
		createButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(flag == true){
					ViewEventController.getInstance().createPlanningPokerSession();
					ViewEventController.getInstance().closeAllTabs();
					flag = false;
				}
				ViewEventController.getInstance().createPlanningPokerSession();
				
			}
		});		
			
		contentPanel.add(createButton);
		contentPanel.setOpaque(false);

		this.add(contentPanel);
	}

	/**
	 * Method getCreateButton.
	
	 * @return JButton */
	public JButton getCreateButton() {
		return createButton;
	}
}
