package edu.wpi.cs.wpisuitetng.modules.planningpoker.view;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * This panel contains buttons to pick which PlanningPoker game to play
 * 
 * @author jdorich
 *
 */
@SuppressWarnings("serial")
public class ToolbarPanel extends JPanel {
    /** The interactive/distributed buttons */
    private final JButton btnInteractive;
    private final JButton btnDistributed;

    /**
     * Construct the panel.
     */
    public ToolbarPanel() {

        // Make this panel transparent, we want to see the JToolbar gradient beneath it
        this.setOpaque(false);

        // Construct the interactive and distributed button and add it to this panel
        btnInteractive = new JButton("Interactive");
        btnDistributed = new JButton("Distributed");
        add(btnInteractive);
        add(btnDistributed);
    }
}
