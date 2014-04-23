/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.requirementselection;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SpringLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;

import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;

/**
 * This class is used to display the name and description of the requirement
 * that was clicked.
 * 
 * @author theteam8s
 * @version 1.0
 */
public class InfoPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JTextField name;
	private final JTextField description;
	public InfoPanel() {
		final SpringLayout springLayout = new SpringLayout();
		setLayout(springLayout);
		
		final JLabel lblRequirementname = new JLabel("RequirementName");
		springLayout.putConstraint(SpringLayout.NORTH, lblRequirementname,
									10, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, lblRequirementname,
									10, SpringLayout.WEST, this);
		add(lblRequirementname);
		
<<<<<<< HEAD
		name = new JTextArea();
		springLayout.putConstraint(SpringLayout.NORTH, name, 6, SpringLayout.SOUTH, lblRequirementname);
		springLayout.putConstraint(SpringLayout.WEST, name, 10, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.EAST, name, -10, SpringLayout.EAST, this);
		name.setEditable(false);
		name.setBorder((new JTextField()).getBorder());
=======
		name = new JTextField();
		springLayout.putConstraint(SpringLayout.NORTH, name,
									6, SpringLayout.SOUTH, lblRequirementname);
		springLayout.putConstraint(SpringLayout.WEST, name,
									10, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.EAST, name,
									-10, SpringLayout.EAST, this);
>>>>>>> More codePro on InfoPanel and RequirementSelectionView
		add(name);
		name.setColumns(10);
		
		final JLabel lblRequirementDescription = new JLabel("Requirement Description");
		springLayout.putConstraint(SpringLayout.NORTH, lblRequirementDescription,
									6, SpringLayout.SOUTH, name);
		springLayout.putConstraint(SpringLayout.WEST, lblRequirementDescription,
									0, SpringLayout.WEST, lblRequirementname);
		add(lblRequirementDescription);
		
<<<<<<< HEAD
		final JScrollPane descrScroll = new JScrollPane();
		add(descrScroll);
		springLayout.putConstraint(SpringLayout.NORTH, descrScroll, 6, SpringLayout.SOUTH, lblRequirementDescription);
		springLayout.putConstraint(SpringLayout.WEST, descrScroll, 0, SpringLayout.WEST, lblRequirementname);
		springLayout.putConstraint(SpringLayout.SOUTH, descrScroll, 56, SpringLayout.SOUTH, lblRequirementDescription);
		springLayout.putConstraint(SpringLayout.EAST, descrScroll, 0, SpringLayout.EAST, name);
		description = new JTextArea();
		descrScroll.setViewportView(description);
		description.setEditable(false);
		description.setLineWrap(true);
		description.setBorder((new JTextField()).getBorder());
=======
		description = new JTextField();
		springLayout.putConstraint(SpringLayout.NORTH, description,
									6, SpringLayout.SOUTH, lblRequirementDescription);
		springLayout.putConstraint(SpringLayout.WEST, description,
									10, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.SOUTH, description,
									66, SpringLayout.SOUTH, lblRequirementDescription);
		springLayout.putConstraint(SpringLayout.EAST, description,
									-10, SpringLayout.EAST, this);
		add(description);
>>>>>>> More codePro on InfoPanel and RequirementSelectionView
	}
	
	
	/**
	 * This method is used to change the requirement that is displayed
	 * @param requirement
	 */
	public void setRequirement(Requirement requirement){
		name.setText(requirement.getName());
		description.setText(requirement.getDescription());
	}
}
