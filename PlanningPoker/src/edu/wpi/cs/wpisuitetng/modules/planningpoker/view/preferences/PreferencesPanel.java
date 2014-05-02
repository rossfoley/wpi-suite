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
import java.awt.Font;

import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * 
 * @author Andrew Leonard
 *
 */

/**
 * This class implements an interface to manage user preferences.
 */
public class PreferencesPanel extends JPanel {
	private final JTextField txtEnterEmailHere;
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
		btnSubmit.setEnabled(false);

		lblEmailErrorText = new JLabel("Invalid Email");
		springLayout.putConstraint(SpringLayout.NORTH, lblEmailErrorText, 6, SpringLayout.SOUTH, txtEnterEmailHere);
		springLayout.putConstraint(SpringLayout.WEST, lblEmailErrorText, 31, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.SOUTH, lblEmailErrorText, -187, SpringLayout.SOUTH, this);
		springLayout.putConstraint(SpringLayout.EAST, lblEmailErrorText, -129, SpringLayout.EAST, this);
		lblEmailErrorText.setForeground(Color.BLACK);
		add(lblEmailErrorText);

		lblEmailSubmitted = new JLabel("Submitted Email!");
		springLayout.putConstraint(SpringLayout.NORTH, lblEmailSubmitted, 0, SpringLayout.NORTH, lblEmailErrorText);
		springLayout.putConstraint(SpringLayout.WEST, lblEmailSubmitted, 0, SpringLayout.WEST, btnSubmit);
		springLayout.putConstraint(SpringLayout.EAST, lblEmailSubmitted, -26, SpringLayout.EAST, this);
		lblEmailSubmitted.setForeground(new Color(50, 205, 50));
		add(lblEmailSubmitted);


		lblEmailErrorText.setVisible(false);
		lblEmailSubmitted.setVisible(false);

		// Add functionality to submit
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				submitEmail();
			}
		});

		// Add live listener to input text field for live validation
		txtEnterEmailHere.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
				// Get rid of submission message if necessary
				lblEmailSubmitted.setVisible(false);
				final String email = txtEnterEmailHere.getText();
				// Should be a special case
				if (email.equals("")) {
					lblEmailErrorText.setVisible(false);
					btnSubmit.setEnabled(false);
				}

				else if (validateEmail(email) == true) {
					lblEmailErrorText.setText("Valid Email");
					lblEmailErrorText.setForeground(Color.GREEN);
					lblEmailErrorText.setVisible(true);
					btnSubmit.setEnabled(true);
				}
				else {
					lblEmailErrorText.setText("Invalid Email");
					lblEmailErrorText.setForeground(Color.BLACK);
					lblEmailErrorText.setVisible(true);
					btnSubmit.setEnabled(false);
				}
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				// Get rid of submission message if necessary
				lblEmailSubmitted.setVisible(false);
				final String email = txtEnterEmailHere.getText();
				// Should be a special case
				if (email.equals("")) {
					lblEmailErrorText.setVisible(false);
					btnSubmit.setEnabled(false);
				}

				else if (validateEmail(email) == true) {
					lblEmailErrorText.setText("Valid Email");
					lblEmailErrorText.setForeground(Color.GREEN);
					lblEmailErrorText.setVisible(true);
					btnSubmit.setEnabled(true);
				}
				else {
					lblEmailErrorText.setText("Invalid Email");
					lblEmailErrorText.setForeground(Color.BLACK);
					lblEmailErrorText.setVisible(true);
					btnSubmit.setEnabled(false);
				}

			}
			@Override
			public void changedUpdate(DocumentEvent e) {
				// Get rid of submission message if necessary
				lblEmailSubmitted.setVisible(false);
				final String email = txtEnterEmailHere.getText();

				// Should be a special case
				if (email.equals("")) {
					lblEmailErrorText.setVisible(false);
					btnSubmit.setEnabled(false);
				}

				else if (validateEmail(email) == true) {
					lblEmailErrorText.setText("Valid Email");
					lblEmailErrorText.setForeground(Color.GREEN);
					lblEmailErrorText.setVisible(true);
					btnSubmit.setEnabled(true);
				}
				else {
					lblEmailErrorText.setText("Invalid Email");
					lblEmailErrorText.setForeground(Color.BLACK);
					lblEmailErrorText.setVisible(true);
					btnSubmit.setEnabled(false);
				}

			}
		});

		JLabel lblPreferences = new JLabel("Preferences");
		lblPreferences.setHorizontalAlignment(SwingConstants.CENTER);
		lblPreferences.setFont(new Font("Tahoma", Font.PLAIN, 30));
		springLayout.putConstraint(SpringLayout.NORTH, lblPreferences, 10, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.EAST, lblPreferences, 0, SpringLayout.EAST, lblSub);
		add(lblPreferences);
	}


	/**
	 * This method contains all the functionality of user hitting the submit button for the email box.
	 */
	void submitEmail() {
		// Give immediate user feedback
		lblEmailSubmitted.setVisible(true);
		
		// Grab input and make new email address
		final String email = txtEnterEmailHere.getText();
		final EmailAddress newEmail = new EmailAddress();
		newEmail.setEmail(email);
		ConfigManager.getInstance();
		newEmail.setOwnerName(ConfigManager.getConfig().getUserName());

		// Check if user already has email address in system
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
		
		final String userName = ConfigManager.getConfig().getUserName();
		for (int i = 0; i < emailRecipients.size(); i++) {
			if (userName.equals(emailRecipients.get(i).getOwnerName())) {
				userHasEmail = true;
			}
		}

		// Depending on whether the user already has an email, the call gets modified.
		if (userHasEmail) {
			EmailAddressModel.getInstance().updateEmailAddress(newEmail);
		}
		else {
			EmailAddressModel.getInstance().addEmail(newEmail);
		}
		// Save when this happened so the success message can go away in 10 mins, not sticking around indefinitely
		emailLastSubmitted = System.currentTimeMillis();
	}

	/**
	 * This function validates a given email address for form.
	 */
	boolean validateEmail(String email) {
		String localEmailCopy = new String (email);
		
		// Built in validation has bugs, re-implementing myself
		
		//Split into substrings
		int atPos = localEmailCopy.indexOf('@');
		
		if (atPos == -1) return false;
		
		
		// make sure first part has size
		String part1 = new String(localEmailCopy.substring(0, atPos));
		if (part1.length() < 1) return false;
		
		// test if second part is at last a.a
		String part2 = new String (localEmailCopy.substring(atPos+1));
		
		if (part2.length() < 3) return false;
		if (part2.indexOf('.') == -1) return false;
		if (part2.indexOf('.') ==  part2.length() - 1) return false;

		return true;
	}


	/**
	 * Calls the super classes repaint and them implements a timeout for email submission
	 * success message.
	 * @param g
	 */
	public void repaint(Graphics g) {
		super.repaint();
		if (System.currentTimeMillis() > emailLastSubmitted + 1000*60*10) {
			lblEmailSubmitted.setVisible(false);
		}
	}
}
