/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team Rolling Thunder, The Team8s
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.requirementcreation;

import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.requirements.RequirementPanelListener;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.requirements.ViewMode;

/**
 * @version $Revision: 1.0 $
 */
public class RequirementCreationButtonPanel extends JPanel implements RequirementPanelListener
{
	private final RequirementCreationPanel parentPanel;
	
	private final ErrorPanel errorDisplay;
	
	private final JButton buttonOK;
	private final JButton buttonCancel;
	private final JButton buttonClear;
	
	private boolean changes;
	private boolean valid;
	
	/**
	 * Constructor for the requirement button panel
	 * @param parentPanel the panel this reports to
	 * @param mode viewmode for the panel
	 * @param curr current requirement
	 */
	public RequirementCreationButtonPanel(RequirementCreationPanel parentPanel, Requirement curr)
	{
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		changes = false;
		valid = false;
		errorDisplay = new ErrorPanel();
		this.parentPanel = parentPanel;
		
		buttonOK = new JButton("Create");
		buttonCancel = new JButton("Cancel");
		buttonClear = new JButton("Clear");
		

		try {
		    Image img = ImageIO.read(getClass().getResource("save-icon.png"));
		    buttonOK.setIcon(new ImageIcon(img));
		    
		    img = ImageIO.read(getClass().getResource("undo-icon.png"));
		    buttonClear.setIcon(new ImageIcon(img));
		} catch (IOException ex) {
			System.out.print("Unable to load image: ");
			System.out.println(ex.getMessage());
		}
		
		
		this.add(buttonOK);
		this.add(buttonClear);
		this.add(buttonCancel);
		buttonOK.setEnabled(false);
		buttonClear.setEnabled(false);
		this.add(errorDisplay);
		setupListeners();
	}
	
	/**
	 * Sets up the listeners for the buttons in the requirement button panel.
	 */
	private void setupListeners()
	{
		buttonOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parentPanel.OKPressed();
			}
		});

		buttonClear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				parentPanel.clearPressed();
			}

		});
		
		buttonCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parentPanel.cancelPressed();
			}
		});
	}

	/**
	 * Method fireDeleted.
	 * @param b boolean
	
	 * @see edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.requirements.RequirementPanelListener#fireDeleted(boolean) */
	@Override
	public void fireDeleted(boolean b) 
	{
	}

	/**
	 * Method fireValid.
	 * @param b boolean
	
	 * @see edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.requirements.RequirementPanelListener#fireValid(boolean) */
	@Override
	public void fireValid(boolean b) {
		valid = b;
		buttonOK.setEnabled(b && changes);
	}

	/**
	 * Method fireChanges.
	 * @param b boolean
	
	 * @see edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.requirements.RequirementPanelListener#fireChanges(boolean) */
	@Override
	public void fireChanges(boolean b) {
		changes = b;
		buttonOK.setEnabled(b && valid);
		buttonClear.setEnabled(b);
	}
	
	/**
	 * Method readyToRemove.
	
	
	 * @return boolean * @see edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.requirements.RequirementPanelListener#readyToRemove() * @see edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.requirements.RequirementPanelListener#readyToRemove()
	 */
	@Override
	public boolean readyToRemove() {
		return true;
	}

	/**
	 * Method fireRefresh.
	 * @see edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.requirements.RequirementPanelListener#fireRefresh()
	 */
	@Override
	public void fireRefresh() {}
	

	/**
	
	 * @return the error panel */
	public ErrorPanel getErrorPanel()
	{
		return errorDisplay;
	}
	
	/**
	 * 
	
	 * * @return the clear button */
	public JButton getButtonClear() {
		return buttonClear;
	}
	
	/**
	 *
	 * * @return the ok button */
	public JButton getButtonOK() {
		return buttonOK;
	}
	
	/**
	 *
	
	 * * @return the cancel button  */
	public JButton getButtonCancel() {
		return buttonCancel;
	}
}
