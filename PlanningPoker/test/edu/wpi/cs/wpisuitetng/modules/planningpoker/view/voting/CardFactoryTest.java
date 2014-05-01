package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.voting;
import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Test;


public class CardFactoryTest {
	
	@Test
	public void GenerateCardTest1Digit(){
		final int number = 4;
		
		CardFactory cF = new CardFactory(number);
		
		cF.generateCard();
		
		final BufferedImage testCard = cF.getCard();
		
		assertNotNull(testCard);
		
		File testfile = new File("tests/TestCard1Digit.png");
		
		testfile.delete();
		
		testfile.mkdirs();
		
	    try {
			ImageIO.write(testCard, "png", testfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void GenerateCardTest2Digit(){
		final int number = 44;
		
		CardFactory cF = new CardFactory(number);
		
		cF.generateCard();
		
		final BufferedImage testCard = cF.getCard();
		
		assertNotNull(testCard);
		
		File testfile = new File("tests/TestCard2Digit.png");
		
		testfile.delete();
		
		testfile.mkdirs();
		
	    try {
			ImageIO.write(testCard, "png", testfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void GenerateCardTest3Digit(){
		final int number = 400;
		
		CardFactory cF = new CardFactory(number);
		
		cF.generateCard();
		
		final BufferedImage testCard = cF.getCard();
		
		assertNotNull(testCard);
		
		File testfile = new File("tests/TestCard3Digit.png");
		
		testfile.delete();
		
		testfile.mkdirs();
		
	    try {
			ImageIO.write(testCard, "png", testfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
