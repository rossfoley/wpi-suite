package edu.wpi.cs.wpisuitetng.modules.planningpoker.gui;

import java.util.LinkedList;

import javax.swing.JFrame;

public class GuiTester {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String[] testObject = {"requirement_1", "requirement_2", "requirement_3",
				"requirement_4", "requirement_5", "requirement_6"};
		
		SelectFromListPanel panel = new SelectFromListPanel(testObject);
		
		panel.addRequirement("add_test");
		panel.addRequirement("add_test_2");
		
		JFrame frame = new JFrame();
		frame.add(panel);
		frame.setVisible(true);
		frame.setSize(800, 600);
		
	}

}
