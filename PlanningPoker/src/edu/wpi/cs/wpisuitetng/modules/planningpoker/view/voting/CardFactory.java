package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.voting;

import java.awt.Color;
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
		    
		    g.rotate(Math.PI, curImage.getWidth()/2, curImage.getHeight()/2);
		    
		    g.drawString(Integer.toString(cardNum), xloc, 35);

		    //g.drawString(Integer.toString(cardNum), xloc, 105);
		    
		   // g.rotate(180);
		    
		    g.dispose();
	}
	
	public BufferedImage getCard(){
		if (curImage == null){
			generateCard();
		}
		return curImage;
	}
	
}
