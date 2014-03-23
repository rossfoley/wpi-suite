package edu.wpi.cs.wpisuitetng.modules.planningpoker.view;

import javax.swing.JPanel;

/**
 * This panel fills the main content area of the tab for this module. It
 * contains one inner JPanel, the BoardPanel.
 *
 * @author jdorich
 * @version Mar 22, 2014
 */
@SuppressWarnings("serial")
public class MainView extends JPanel{
	/** The panel containing the planning poker */
    private final PokerPanel pokerPanel;

    /**
     * Construct the panel.
     */
    public MainView() {
        // Add the board panel to this view
        pokerPanel = new PokerPanel();
        add(pokerPanel);
    }
}
