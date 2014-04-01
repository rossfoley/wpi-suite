package edu.wpi.cs.wpisuitetng.modules.planningpoker.gui;

import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.BorderLayout;

public class GuiTester {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		List<String> testObject = new LinkedList<String>();
		testObject.add("Requirement_1");
		
		JFrame frame = new JFrame();
		JPanel mainPanel = new JPanel();
		frame.getContentPane().add(mainPanel);
		mainPanel.setLayout(null);
		//frame.getContentPane().setLayout(null);
		
		SelectFromListPanel panel = new SelectFromListPanel();
		panel.setBounds(10, 11, 430, 240);
		//panel.setBounds(0, 0, 1, 1);
		
		mainPanel.add(panel);
		//panel.addRequirement("add_test");
		//panel.addRequirement("add_test_2");
		frame.setVisible(true);
		frame.setSize(800, 600);
		/*
		List<String> out = panel.getSelected();
		for(String str : out){
			System.out.println("Out:" + str);
		}
		//*/
	}

}
