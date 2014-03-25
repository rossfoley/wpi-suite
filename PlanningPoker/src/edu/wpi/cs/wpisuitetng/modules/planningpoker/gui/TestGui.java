package edu.wpi.cs.wpisuitetng.modules.planningpoker.gui;

import java.awt.EventQueue;

import javax.swing.JInternalFrame;
import javax.swing.SpringLayout;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.UIManager;

public class TestGui extends JInternalFrame {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TestGui frame = new TestGui();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public TestGui() {
		
		//Set up gui
		setBounds(100, 100, 600, 400);
		SpringLayout springLayout = new SpringLayout();
		getContentPane().setLayout(springLayout);
		
		//create tabpane to simulate janeway tabs
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		springLayout.putConstraint(SpringLayout.NORTH, tabbedPane, 0, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, tabbedPane, 0, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, tabbedPane, 0, SpringLayout.SOUTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, tabbedPane, 0, SpringLayout.EAST, getContentPane());
		getContentPane().add(tabbedPane);
		
		//create the planning poker tab
		JPanel tabPanel = new JPanel();
		tabbedPane.addTab("Planning Poker", null, tabPanel, null);
		SpringLayout sl_tabPanel = new SpringLayout();
		tabPanel.setLayout(sl_tabPanel);
		
		//create the toolbar
		JPanel toolbar = new JPanel();
		sl_tabPanel.putConstraint(SpringLayout.NORTH, toolbar, 0, SpringLayout.NORTH, tabPanel);
		sl_tabPanel.putConstraint(SpringLayout.WEST, toolbar, 0, SpringLayout.WEST, tabPanel);
		sl_tabPanel.putConstraint(SpringLayout.SOUTH, toolbar, 75, SpringLayout.NORTH, tabPanel);
		sl_tabPanel.putConstraint(SpringLayout.EAST, toolbar, 579, SpringLayout.WEST, tabPanel);
		tabPanel.add(toolbar);
		
		//create the main panel
		JPanel mainPanel = new JPanel();
		sl_tabPanel.putConstraint(SpringLayout.NORTH, mainPanel, 0, SpringLayout.SOUTH, toolbar);
		sl_tabPanel.putConstraint(SpringLayout.WEST, mainPanel, 0, SpringLayout.WEST, toolbar);
		sl_tabPanel.putConstraint(SpringLayout.SOUTH, mainPanel, 268, SpringLayout.SOUTH, toolbar);
		sl_tabPanel.putConstraint(SpringLayout.EAST, mainPanel, 0, SpringLayout.EAST, toolbar);
		tabPanel.add(mainPanel);

	}
}
