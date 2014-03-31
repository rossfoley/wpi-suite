/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.buttons;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;

import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.ToolbarGroupView;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.ViewEventController;


/**
 * @author Amanda
 *
 */
public class PlanningPokerSessionButtonsPanel extends ToolbarGroupView{
	private JButton createButton = new JButton("<html>Create<br />Planning Poker Session</html>");
	private final JPanel contentPanel = new JPanel();
	
	public PlanningPokerSessionButtonsPanel(){
		super("");
		
		this.contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.X_AXIS));
		this.setPreferredWidth(350);
		
		//this.createIterationButton.setSize(200, 200);
		//this.createButton.setPreferredSize(new Dimension(200, 200));
		this.createButton.setHorizontalAlignment(SwingConstants.CENTER);
		try {
		    Image img = ImageIO.read(new File("../src/new_req.png"));
		    this.createButton.setIcon(new ImageIcon(img));
		    
		    //img = ImageIO.read(getClass().getResource("new_itt.png"));
		    //this.createIterationButton.setIcon(new ImageIcon(img));
		    
		} catch (IOException ex) {}
		  catch (IllegalArgumentException ex) {}
		
		// the action listener for the Create Planning Poker Session Button
		createButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// bring up a create requirement pane if not in Multiple Requirement Editing Mode
				//if (!ViewEventController.getInstance().getOverviewTable().getEditFlag()) {
					/*
					 * 
					 * CREATE PLANNING POKER SESSION BUTTON
					 * 
					 * 
					 */
				
				
				
				
				
				ViewEventController.getInstance().createPlanningPokerSession();
			//	}
			}
		});		
		
		//action listener for the Create Iteration Button
		/*createIterationButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//if (!ViewEventController.getInstance().getOverviewTable().getEditFlag()) {
					ViewEventController.getInstance().createIteration();
				}
		//	}
		}); */
			
		contentPanel.add(createButton);
		//contentPanel.add(createIterationButton);
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
