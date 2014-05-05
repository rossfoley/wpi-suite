/*******************************************************************************
 * Copyright (c) 2014 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Team8s
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.preferences;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.UIManager;

import java.awt.Font;
import java.awt.GridLayout;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JTextField;

import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

public class HelpTextPanel extends JPanel{
	private JTextArea txtWhatIsPlanning;
	private JEditorPane content;
	private String baseURL;
	
	public HelpTextPanel() {
		setLayout(new GridLayout(0,1));
		URL startingURL;
		baseURL = Network.getInstance().makeRequest("docs/PlanningPoker/", HttpMethod.GET).getUrl().toString().replace("API/", "");
		try {
			startingURL = new URL(baseURL + "start.html");
			content = new JEditorPane(startingURL);
			content.setEditable(false);
		} catch (Exception e) {}
		
		JScrollPane container = new JScrollPane(content);
		add(container);
	}

	public void setPage(String newPage) {
		try {
			content.setPage(new URL(baseURL + newPage));
		} catch (Exception e) {}
	}
}
