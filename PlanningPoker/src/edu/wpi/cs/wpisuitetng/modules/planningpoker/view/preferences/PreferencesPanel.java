/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.planningpoker.view.preferences;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SpringLayout;
import javax.swing.JTextField;
import javax.swing.JButton;

import edu.wpi.cs.wpisuitetng.janeway.config.ConfigManager;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.controller.AddEmailController;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.controller.GetEmailController;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.controller.UpdateEmailController;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.EmailAddress;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.EmailAddressModel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.List;

/**
 * 
 * @author Andrew Leonard
 *
 */

/**
 * This class implements an interface to manage user preferences.
 */
public class PreferencesPanel extends JPanel implements FocusListener {
	private final JTextField txtEnterEmailHere;
	private boolean emailError = false;
	private boolean emailSent = false;
	JLabel lblEmailErrorText;
	JLabel lblEmailSubmitted;
	long emailLastSubmitted;
	/**
	 * Constructs the Preferences Panel
	 */
	public PreferencesPanel() {		
		final SpringLayout springLayout = new SpringLayout();
		setLayout(springLayout);

		txtEnterEmailHere = new JTextField();
		springLayout.putConstraint(SpringLayout.WEST, txtEnterEmailHere, 31, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.EAST, txtEnterEmailHere, -200, SpringLayout.EAST, this);
		txtEnterEmailHere.setText("Enter Email Here...");
		add(txtEnterEmailHere);
		txtEnterEmailHere.setColumns(10);
		txtEnterEmailHere.addFocusListener(this);

		final JLabel lblSub = new JLabel("Subscribe To Email Notifications");
		springLayout.putConstraint(SpringLayout.NORTH, txtEnterEmailHere, 13, SpringLayout.SOUTH, lblSub);
		springLayout.putConstraint(SpringLayout.SOUTH, lblSub, -240, SpringLayout.SOUTH, this);
		springLayout.putConstraint(SpringLayout.WEST, lblSub, 31, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.EAST, lblSub, -240, SpringLayout.EAST, this);
		add(lblSub);

		final JButton btnSubmit = new JButton("Submit");
		springLayout.putConstraint(SpringLayout.NORTH, btnSubmit, -1, SpringLayout.NORTH, txtEnterEmailHere);
		springLayout.putConstraint(SpringLayout.WEST, btnSubmit, 6, SpringLayout.EAST, txtEnterEmailHere);
		springLayout.putConstraint(SpringLayout.EAST, btnSubmit, 100, SpringLayout.EAST, txtEnterEmailHere);
		add(btnSubmit);

		lblEmailErrorText = new JLabel("Invalid Email");
		springLayout.putConstraint(SpringLayout.NORTH, lblEmailErrorText, 6, SpringLayout.SOUTH, txtEnterEmailHere);
		springLayout.putConstraint(SpringLayout.WEST, lblEmailErrorText, 31, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.SOUTH, lblEmailErrorText, -187, SpringLayout.SOUTH, this);
		springLayout.putConstraint(SpringLayout.EAST, lblEmailErrorText, -129, SpringLayout.EAST, this);
		lblEmailErrorText.setForeground(Color.RED);
		add(lblEmailErrorText);

		lblEmailSubmitted = new JLabel("Submitted Email!");
		springLayout.putConstraint(SpringLayout.NORTH, lblEmailSubmitted, 0, SpringLayout.NORTH, lblEmailErrorText);
		springLayout.putConstraint(SpringLayout.WEST, lblEmailSubmitted, 0, SpringLayout.WEST, btnSubmit);
		springLayout.putConstraint(SpringLayout.EAST, lblEmailSubmitted, -26, SpringLayout.EAST, this);
		lblEmailSubmitted.setForeground(new Color(50, 205, 50));
		add(lblEmailSubmitted);


		lblEmailErrorText.setVisible(emailError);
		lblEmailSubmitted.setVisible(emailSent);

		// Add functionality to submit
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				submitEmail();
			}
		});

		addFocusListener(this);
	}


	/**
	 * This method contains all the functionality of user hitting the submit button for the email box.
	 */
	void submitEmail() {
		final String email = txtEnterEmailHere.getText();

		// Validate
		if (validateEmail(email) == true) {
			emailError = false;
			emailSent = true;
			lblEmailErrorText.setVisible(emailError);
			lblEmailSubmitted.setVisible(emailSent);
			final EmailAddress newEmail = new EmailAddress();
			newEmail.setEmail(email);
			newEmail.setOwnerName(ConfigManager.getInstance().getConfig().getUserName());


			final GetEmailController getEmailController = GetEmailController.getInstance();
			getEmailController.retrieveEmails();
			List<EmailAddress> emailRecipients = null;
			boolean userHasEmail = false;

			final EmailAddressModel emailAddressModel = EmailAddressModel.getInstance();
			try {
				emailRecipients = emailAddressModel.getEmailAddresses();
			}
			catch (Exception E) {

			}
			final String userName = ConfigManager.getInstance().getConfig().getUserName();
			for (int i = 0; i < emailRecipients.size(); i++) {
				if (userName.equals(emailRecipients.get(i).getOwnerName())) {
					userHasEmail = true;
				}
			}

			if (userHasEmail) {
				EmailAddressModel.getInstance().updateEmailAddress(newEmail);
			}
			else {
				EmailAddressModel.getInstance().addEmail(newEmail);
			}
			emailLastSubmitted = System.currentTimeMillis();
		}
		else {
			emailError = true;
			emailSent = false;
			lblEmailErrorText.setVisible(emailError);
			lblEmailSubmitted.setVisible(emailSent);
		}
		


	}

	/**
	 * This function validates a given email address for form.
	 */
	boolean validateEmail(String email) {

		try {
			final InternetAddress valid = new InternetAddress(email);
			valid.validate();
		}
		catch (AddressException e) {
			return false;
		}

		return true;
	}

	/**
	 * Focus listen function for describing what happens when this panel comes into focus.
	 */
	@Override
	public void focusGained(FocusEvent e) {
		emailSent = false;
		lblEmailSubmitted.setVisible(emailSent);
	}

	/**
	 * Focus listen function for describing what happens when this panel comes out of focus.
	 */
	@Override
	public void focusLost(FocusEvent e) {
		emailSent = false;
		lblEmailSubmitted.setVisible(emailSent);

	}

	/**
	 * Calls the super classes repaint and them implements a timeout for email submission
	 * success message.
	 * @param g
	 */
	public void repaint(Graphics g) {
		super.repaint();
		if (System.currentTimeMillis() > emailLastSubmitted + 1000*60*10) {
			emailSent = false;
			lblEmailSubmitted.setVisible(emailSent);
		}
	}
}
