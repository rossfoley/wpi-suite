package edu.wpi.cs.wpisuitetng.modules.planningpoker.view;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.overview.OverviewPanel;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.view.overview.OverviewTable;

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
	private OverviewPanel openSessions = new OverviewPanel(true);
	private OverviewPanel closedSessions = new OverviewPanel(false);
	private Component lastTab = null;
	private final JPopupMenu popup = new JPopupMenu();
	private JMenuItem closeAll = new JMenuItem("Close All Tabs");
	private JMenuItem closeOthers = new JMenuItem("Close Others");



	/**
	 * Adds main subtab when user goes to RequirementManager
	 */
	public MainView() {
		this.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		this.addTab("Open Sessions", openSessions);
		this.addTab("Closed Sessions", closedSessions);

		
		closeAll.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				ViewEventController.getInstance().closeAllTabs();

			}	
		});
		
		closeOthers.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				ViewEventController.getInstance().closeOthers();

			}	
		});

		
		
		popup.add(closeAll);
		popup.add(closeOthers);


		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {

				if(!dragging) {
					// Gets the tab index based on the mouse position
					int tabNumber = getUI().tabForCoordinate(MainView.this, e.getX(), e.getY());

					if(tabNumber >= 0) {
						draggedTabIndex = tabNumber;
						Rectangle bounds = getUI().getTabBounds(MainView.this, tabNumber);


						// Paint the tabbed pane to a buffer
						Image totalImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
						Graphics totalGraphics = totalImage.getGraphics();
						totalGraphics.setClip(bounds);
						// Don't be double buffered when painting to a static image.
						setDoubleBuffered(false);
						paintComponent(totalGraphics);

						// Paint just the dragged tab to the buffer
						tabImage = new BufferedImage(bounds.width, bounds.height, BufferedImage.TYPE_INT_ARGB);
						Graphics graphics = tabImage.getGraphics();
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
			@Override
			public void mousePressed(MouseEvent e)
			{
				if(e.isPopupTrigger()) popup.show(e.getComponent(), e.getX(), e.getY());
			}

			public void mouseReleased(MouseEvent e) {
				if(dragging) {
					int tabNumber = getUI().tabForCoordinate(MainView.this, e.getX(), e.getY());
					if(tabNumber >= 0) {
						Component comp = getComponentAt(draggedTabIndex);
						String title = getTitleAt(draggedTabIndex);
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
		final MainView panel = this;
		this.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JComponent selected = (JComponent)MainView.this.getSelectedComponent();
				
				if(selected == openSessions || selected == closedSessions)
				{
					ViewEventController.getInstance().getOverviewTable().refresh();
					closedSessions.revalidate();
					openSessions.revalidate();
					closedSessions.repaint();
					openSessions.repaint();
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
		if (!(component instanceof OverviewPanel)) {
			setTabComponentAt(index, new ClosableTabComponent(this));
		}
	}
	
	/**
	 * Method getOpenOverview.
	 * @return OpenOverviewPanel */
	public OverviewPanel getOpenOverview() {
		return openSessions;
	}
	
	/**
	 * Method getClosedOverview.
	 * @return ClosedOverviewPanel */
	public OverviewPanel getClosedOverview() {
		return closedSessions;
	}
	
	/**
	 * Method setSelectedComponent.
	 * @param c Component
	 */
	@Override
	public void setSelectedComponent(Component c){
		this.lastTab = this.getSelectedComponent();
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
			if (this.lastTab != null){
				setSelectedComponent(this.lastTab);}
		} catch (IllegalArgumentException e){}
	}
	
	/**
	 * Method getPopup.
	 * @return JPopupMenu
	 */
	public JPopupMenu getPopup() {
		return popup;
	}
	/**
	 * Method getCloseAll.
	 * @return JMenuItem
	 */
	public JMenuItem getCloseAll() {
		return closeAll;
	}


	/**
	 * Method getCloseOthers.
	 * @return JMenuItem
	 */
	public JMenuItem getCloseOthers() {
		return closeOthers;
	}

}
