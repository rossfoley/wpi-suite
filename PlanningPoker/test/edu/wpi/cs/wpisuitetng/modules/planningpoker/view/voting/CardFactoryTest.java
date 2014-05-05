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
import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Test;

/**
 * Test Class for CardFactory
 * @author Perry
 * @version 1.0
 */
public class CardFactoryTest {
	
	/**
	 * Tests the generation of a card with 1 digit
	 * Output goes to tests/TestCard1Digit.png
	 */
	@Test
	public void GenerateCardTest1Digit(){
		final int number = 4;
		
		final CardFactory cF = new CardFactory(number);
		
		cF.generateCard();
		
		final BufferedImage testCard = cF.getCard();
		
		assertNotNull(testCard);
		
		final File testfile = new File("tests/TestCard1Digit.png");
		
		testfile.delete();
		
		testfile.mkdirs();
		
	    try {
			ImageIO.write(testCard, "png", testfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Tests the generation of a card with 2 digits
	 * Output goes to tests/TestCard2Digit.png
	 */
	@Test
	public void GenerateCardTest2Digit(){
		final int number = 44;
		
		final CardFactory cF = new CardFactory(number);
		
		cF.generateCard();
		
		final BufferedImage testCard = cF.getCard();
		
		assertNotNull(testCard);
		
		final File testfile = new File("tests/TestCard2Digit.png");
		
		testfile.delete();
		
		testfile.mkdirs();
		
	    try {
			ImageIO.write(testCard, "png", testfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Tests the generation of a card with 3 digits
	 * Output goes to tests/TestCard3Digit.png
	 */
	@Test
	public void GenerateCardTest3Digit(){
		final int number = 400;
		
		final CardFactory cF = new CardFactory(number);
		
		cF.generateCard();
		
		final BufferedImage testCard = cF.getCard();
		
		assertNotNull(testCard);
		
		final File testfile = new File("tests/TestCard3Digit.png");
		
		testfile.delete();
		
		testfile.mkdirs();
		
	    try {
			ImageIO.write(testCard, "png", testfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * A demonstration of the time it takes to generate 3 cards in a row.
	 * Testing on my machine show execution time of this test to be around .3 seconds
	 */
	@Test
	public void GenerationSpeedTest(){
		final CardFactory cF1 = new CardFactory(10);
		
		cF1.generateCard();
		
		final CardFactory cF2 = new CardFactory(10);
		
		cF2.generateCard();
		
		final CardFactory cF3 = new CardFactory(10);
		
		cF3.generateCard();
		
		assertNotNull(cF1.getCard());
		assertNotNull(cF2.getCard());
		assertNotNull(cF3.getCard());
	}
}
