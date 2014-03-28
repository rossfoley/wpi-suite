package edu.wpi.cs.wpisuitetng.modules.planningpoker.gui;

import java.util.LinkedList;

import javax.swing.JFrame;

public class GuiTester {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		LinkedList<String> testObject = new LinkedList<String>();
		testObject.add("requirement_1");
		testObject.add("requirement_2");
		testObject.add("requirement_3");
		testObject.add("requirement_4");
		
		JFrame frame = new JFrame();
		frame.add(new SelectFromListPanel(testObject));
		frame.setVisible(true);

	}

}
