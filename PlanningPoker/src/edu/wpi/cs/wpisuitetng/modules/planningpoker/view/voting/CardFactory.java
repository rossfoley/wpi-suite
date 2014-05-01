package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.voting;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;


public class CardFactory {
	
	private final int cardNum;
	private final String background = "cards/default.png";
	private BufferedImage curImage;
	
	public CardFactory(int num){
		cardNum = num;
		curImage = null;
	}
	
	public void generateCard(){
		
		try{
			 curImage = ImageIO.read(getClass().getResource(background));
		} catch(IOException e){
			System.out.println("Background not found");
			return;
		}

		    Graphics2D g = curImage.createGraphics();
		    g.setFont(g.getFont().deriveFont(25f));
		    g.setColor(Color.RED);
		    int xloc = 12;
		    if (cardNum>9){
		    	xloc = 6;
		    }
		    g.drawString(Integer.toString(cardNum), xloc, 35);

		    g.setFont(g.getFont().deriveFont(50f));
		    
		    drawCenteredString(Integer.toString(cardNum), curImage.getWidth(), curImage.getHeight(), g);
		    
		    g.rotate(Math.PI, curImage.getWidth()/2, curImage.getHeight()/2);
		    
		    g.setFont(g.getFont().deriveFont(25f));
		    
		    g.drawString(Integer.toString(cardNum), xloc, 35);
		    
		    g.dispose();
	}
	
	public BufferedImage getCard(){
		if (curImage == null){
			generateCard();
		}
		return curImage;
	}
	
	public void drawCenteredString(String s, int w, int h, Graphics g){
		FontMetrics fm = g.getFontMetrics();
		int x = (w-fm.stringWidth(s))/2;
		int y = (fm.getAscent() + (h - (fm.getAscent() + fm.getDescent()))/2);
		g.drawString(s, x, y);
	}  
	
}
