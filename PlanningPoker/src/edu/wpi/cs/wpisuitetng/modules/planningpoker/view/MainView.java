/*******************************************************************************
 * Copyright (c) 2014 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Team8s
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.planningpoker.view;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.timingmanager.TimingManager;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.overview.OverviewPanel;
//import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.preferences.PreferencesPanel;

/**
 * This class sets the main view when user goes to the PlanningPoker tab
 * It also allows opened tabs to be closed by the user
 * 
 *
 * @version $Revision: 1.0 $
 */
public class MainView extends JTabbedPane {

	private boolean dragging = false;
	private Image tabImage = null;
	private Point currentMouseLocation = null;
	private int draggedTabIndex = 0;
	private final OverviewPanel overviewPanel = new OverviewPanel();
	private Component lastTab = null;

	/**
	 * Adds main subtab when user goes to RequirementManager
	 */
	public MainView() {
		this.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		this.addTab("Current Sessions", overviewPanel);
		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {

				if(!dragging) {
					// Gets the tab index based on the mouse position
					final int tabNumber = getUI().tabForCoordinate(MainView.this, e.getX(), e.getY());

					if(tabNumber >= 0) {
						draggedTabIndex = tabNumber;
						final Rectangle bounds = getUI().getTabBounds(MainView.this, tabNumber);


						// Paint the tabbed pane to a buffer
						final Image totalImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
						final Graphics totalGraphics = totalImage.getGraphics();
						totalGraphics.setClip(bounds);
						// Don't be double buffered when painting to a static image.
						setDoubleBuffered(false);
						paintComponent(totalGraphics);

						// Paint just the dragged tab to the buffer
						tabImage = new BufferedImage(bounds.width, bounds.height, BufferedImage.TYPE_INT_ARGB);
						final Graphics graphics = tabImage.getGraphics();
						graphics.drawImage(totalImage, 0, 0, bounds.width, bounds.height, bounds.x, bounds.y, bounds.x + bounds.width, bounds.y+bounds.height, MainView.this);

						dragging = true;
						repaint();
					}
				} else {
					currentMouseLocation = e.getPoint();

					// Need to repaint
					repaint();
				}

				super.mouseDragged(e);
			}
		});


		this.addMouseListener(new MouseAdapter()
		{
			public void mouseReleased(MouseEvent e) {
				if(dragging) {
					final int tabNumber = getUI().tabForCoordinate(MainView.this, e.getX(), e.getY());
					if(tabNumber >= 0) {
						final Component comp = getComponentAt(draggedTabIndex);
						final String title = getTitleAt(draggedTabIndex);
						if (!title.equals("Current Sessions")) {
							removeTabAt(draggedTabIndex);
							insertTab(title, null, comp, null, tabNumber);
							setSelectedIndex(tabNumber);
						}
					}
				}
				dragging = false;
				tabImage = null;
			}
		});
		this.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				
				final JComponent selected = (JComponent) getSelectedComponent();
				// If the selected tab is the session overview panel, refresh the tree and
				// correctly enable the tool-bar buttons
				if (selected instanceof OverviewPanel) {
					ViewEventController.getInstance().getOverviewTreePanel().refresh();
					overviewPanel.revalidate();
					overviewPanel.repaint();
					PlanningPokerSession session = 
							ViewEventController.getInstance().getOverviewDetailPanel().getCurrentSession();
					ViewEventController.getInstance().getPlanningPokerSessionButtonsPanel().enableButtonsForSession(session);
				}
				// Otherwise, disable all buttons
				else {
					ViewEventController.getInstance().getPlanningPokerSessionButtonsPanel().disableAllButtons();
				}
			}
		});
	}


	/**
	 * Method paintComponent.
	 * @param g Graphics
	 */
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		TimingManager.getInstance().start();
		// Are we dragging?
		if(dragging && currentMouseLocation != null && tabImage != null) {
			// Draw the dragged tab
			g.drawImage(tabImage, currentMouseLocation.x, currentMouseLocation.y, this);
		}
	}


	/**
	 * Overridden insertTab function to add the closable tab element.
	 * 
	 * @param title	Title of the tab
	 * @param icon	Icon for the tab
	 * @param component	The tab
	 * @param tip	Showing mouse tip when hovering over tab
	 * @param index	Location of the tab
	 */
	@Override
	public void insertTab(String title, Icon icon, Component component,
			String tip, int index) {
		super.insertTab(title, icon, component, tip, index);
		if (!(component instanceof OverviewPanel)) {// || component instanceof PreferencesPanel)) {
			setTabComponentAt(index, new ClosableTabComponent(this));
		}
	}
	
	/**
	 * Method getOverviewPanel
	 * @return OverviewPanel */
	public OverviewPanel getOverviewPanel() {
		return overviewPanel;
	}
	
	/**
	 * Method setSelectedComponent.
	 * @param c Component
	 */
	@Override
	public void setSelectedComponent(Component c) {
		lastTab = this.getSelectedComponent();
		super.setSelectedComponent(c);
	}

	/**
	 * Method removeTabAt.
	 * @param i int
	 */
	@Override
	public void removeTabAt(int i){
		super.removeTabAt(i);
		try{
			if (lastTab != null){
				setSelectedComponent(lastTab);}
		} catch (IllegalArgumentException e){}
	}
	
}
