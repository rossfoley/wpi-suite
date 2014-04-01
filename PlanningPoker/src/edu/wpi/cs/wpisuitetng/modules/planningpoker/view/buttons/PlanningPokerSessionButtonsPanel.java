package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.buttons;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

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
		this.createButton.setHorizontalAlignment(SwingConstants.CENTER);
		
		// the action listener for the Create Planning Poker Session Button
		createButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
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
