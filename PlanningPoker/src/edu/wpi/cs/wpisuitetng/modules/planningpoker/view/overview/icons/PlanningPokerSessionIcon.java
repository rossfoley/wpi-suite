/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.overview.icons;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;

/**
 * @author Amanda Adkins
 *
 */
public class PlanningPokerSessionIcon implements Icon {

	private int height;
	private int width;
	
	/**
	 * @wbp.parser.entryPoint
	 */
	public PlanningPokerSessionIcon(){
		height = 5;
		width = 5;
	}
	
	/**
	 * Method getIconHeight.
	
	
	 * @return int * @see javax.swing.Icon#getIconHeight() 
	 * @wbp.parser.entryPoint*/
	@Override
	public int getIconHeight() {
		return height;
	}

	/**
	 * Method getIconWidth.
	
	
	 * @return int * @see javax.swing.Icon#getIconWidth() */
	@Override
	public int getIconWidth() {
		return width;
	}

	/**
	 * Method paintIcon.
	 * @param c Component
	 * @param g Graphics
	 * @param x int
	 * @param y int
	
	 * @see javax.swing.Icon#paintIcon(Component, Graphics, int, int) */
	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		// this color might need to be changed (I just copied the rest form Iteration Icon 
		g.setColor(new Color(255, 127, 0));
		g.fillOval(x, y, width, height);
	}

}


