package edu.wpi.cs.wpisuitetng.modules.planningpoker.gui;

import java.awt.EventQueue;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SpringLayout;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import javax.swing.Action;

public class TestGui_A extends JInternalFrame {
	
	private final Action action = new SwingAction();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TestGui_A frame = new TestGui_A();
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
	public TestGui_A() {
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
				SpringLayout sl_toolbar = new SpringLayout();
				toolbar.setLayout(sl_toolbar);
				
				JButton btnNewGame = new JButton("New Game");
				btnNewGame.setAction(action);
				sl_toolbar.putConstraint(SpringLayout.NORTH, btnNewGame, 25, SpringLayout.NORTH, toolbar);
				sl_toolbar.putConstraint(SpringLayout.WEST, btnNewGame, 10, SpringLayout.WEST, toolbar);
				toolbar.add(btnNewGame);
				tabPanel.add(mainPanel);
				SpringLayout sl_mainPanel = new SpringLayout();
				mainPanel.setLayout(sl_mainPanel);
				
				JScrollPane scrollPane = new JScrollPane();
				sl_mainPanel.putConstraint(SpringLayout.NORTH, scrollPane, 10, SpringLayout.NORTH, mainPanel);
				sl_mainPanel.putConstraint(SpringLayout.WEST, scrollPane, 10, SpringLayout.WEST, mainPanel);
				sl_mainPanel.putConstraint(SpringLayout.SOUTH, scrollPane, 258, SpringLayout.NORTH, mainPanel);
				sl_mainPanel.putConstraint(SpringLayout.EAST, scrollPane, 135, SpringLayout.WEST, mainPanel);
				mainPanel.add(scrollPane);
				
				JTabbedPane tabbedPane_1 = new JTabbedPane(JTabbedPane.TOP);
				sl_mainPanel.putConstraint(SpringLayout.NORTH, tabbedPane_1, 0, SpringLayout.NORTH, scrollPane);
				sl_mainPanel.putConstraint(SpringLayout.WEST, tabbedPane_1, 6, SpringLayout.EAST, scrollPane);
				sl_mainPanel.putConstraint(SpringLayout.SOUTH, tabbedPane_1, 0, SpringLayout.SOUTH, scrollPane);
				sl_mainPanel.putConstraint(SpringLayout.EAST, tabbedPane_1, 434, SpringLayout.EAST, scrollPane);
				mainPanel.add(tabbedPane_1);
				
				JPanel panel = NewGameTab.createJPanel();
				tabbedPane_1.addTab("New tab", null, panel, null);
				SpringLayout sl_panel = new SpringLayout();
				panel.setLayout(sl_panel);
				
				JScrollPane scrollPane_1 = new JScrollPane();
				sl_panel.putConstraint(SpringLayout.NORTH, scrollPane_1, 10, SpringLayout.NORTH, panel);
				sl_panel.putConstraint(SpringLayout.WEST, scrollPane_1, 10, SpringLayout.WEST, panel);
				sl_panel.putConstraint(SpringLayout.SOUTH, scrollPane_1, 210, SpringLayout.NORTH, panel);
				sl_panel.putConstraint(SpringLayout.EAST, scrollPane_1, 135, SpringLayout.WEST, panel);
				panel.add(scrollPane_1);

			}
	
	private class SwingAction extends AbstractAction {
		public SwingAction() {
			putValue(NAME, "New Game");
			putValue(SHORT_DESCRIPTION, "Creates a new game tab");
		}
		public void actionPerformed(ActionEvent e) {
		}
	}
}
