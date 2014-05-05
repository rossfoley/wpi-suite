/*******************************************************************************
 * Copyright (c) 2014 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Team8s
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.voting;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 *Class which generates an image of a card based on the desired number
 *@author theTeam8s
 *@version 1.0
 */
public class CardFactory {
	
	private final int cardNum;
	private final String background = "cards/small_default.png";
	private BufferedImage curImage;
	/**
	 * Constructor for the CardFactory
	 * @param num number of the desired card
	 */
	public CardFactory(int num){
		cardNum = num;
		curImage = null;
	}
	
	/**
	 * Creates the image of the card
	 */
	public void generateCard(){
		
		try{
			 curImage = ImageIO.read(getClass().getResource(background));
		} catch(IOException e){
			System.out.println("Background not found");
			return;
		}

		    final Graphics2D g = curImage.createGraphics();
		    g.setFont(new Font("Tahoma", Font.PLAIN, 16));
		    g.setColor(Color.BLACK);
		    int xloc = 7;
		    if (cardNum > 9){
		    	xloc = 4;
		    }
		    g.drawString(Integer.toString(cardNum), xloc, 21);

		    g.setFont(g.getFont().deriveFont(70f));
		    
		    drawCenteredString(Integer.toString(cardNum), 
		    		curImage.getWidth(), curImage.getHeight(), g);
		    
		    g.rotate(Math.PI, curImage.getWidth() / 2, curImage.getHeight() / 2);
		    
		    g.setFont(g.getFont().deriveFont(16f));
		    
		    g.drawString(Integer.toString(cardNum), xloc, 21);
		    
		    g.dispose();
	}
	
	/**
	 * Returns the image of the card, also generates the card if it has not been generated
	 * @return the image of the card
	 */
	public BufferedImage getCard(){
		if (curImage == null){
			generateCard();
		}
		return curImage;
	}
	
	/**
	 * Draws a string on a specified Graphics object with a specified area
	 * @param s String to be centered
	 * @param w Width of the area that the string will be centered in
	 * @param h Height of the area that the string will be centered in
	 * @param g Graphics that the string will be drawn on
	 */
	private void drawCenteredString(String s, int w, int h, Graphics g){

		FontMetrics fm = g.getFontMetrics();
		
		if (fm.stringWidth(s) > w){
			g.setFont(g.getFont().deriveFont(
					g.getFont().getSize2D() * (((float)w) / (fm.stringWidth(s) + 4))));
			fm = g.getFontMetrics();
		}

		
		final int x = (w - fm.stringWidth(s)) / 2;
		final int y = (fm.getAscent() + (h - (fm.getAscent() + fm.getDescent())) / 2);
		
		g.drawString(s, x, y);
	}  
	
}
