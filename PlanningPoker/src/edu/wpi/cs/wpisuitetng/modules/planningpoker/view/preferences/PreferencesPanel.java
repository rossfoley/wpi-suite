/**
 * 
 */
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * @author Andrew Leonard
 *
 */
public class PreferencesPanel extends JPanel {
	private final JTextField txtEnterEmailHere;
	private boolean emailError = false;
	JLabel lblEmailErrorText;


	public PreferencesPanel() {
		final SpringLayout springLayout = new SpringLayout();
		setLayout(springLayout);

		txtEnterEmailHere = new JTextField();
		springLayout.putConstraint(SpringLayout.WEST, txtEnterEmailHere, 31, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.EAST, txtEnterEmailHere, -129, SpringLayout.EAST, this);
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
		springLayout.putConstraint(SpringLayout.EAST, btnSubmit, -34, SpringLayout.EAST, this);
		add(btnSubmit);

		lblEmailErrorText = new JLabel("Invalid Email");
		lblEmailErrorText.setForeground(Color.RED);
		springLayout.putConstraint(SpringLayout.NORTH, lblEmailErrorText, 6, SpringLayout.SOUTH, txtEnterEmailHere);
		springLayout.putConstraint(SpringLayout.WEST, lblEmailErrorText, 31, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.SOUTH, lblEmailErrorText, 20, SpringLayout.SOUTH, txtEnterEmailHere);
		springLayout.putConstraint(SpringLayout.EAST, lblEmailErrorText, 0, SpringLayout.EAST, txtEnterEmailHere);
		add(lblEmailErrorText);
		lblEmailErrorText.setVisible(emailError);

		// Add functionality to submit
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				submitEmail();
			}
		});

	}

	void submitEmail() {
		final String email = txtEnterEmailHere.getText();

		// Validate
		if (validateEmail(email) == true) {
			emailError = false;
			lblEmailErrorText.setVisible(emailError);
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
				//UpdateEmailController updateEmailController = UpdateEmailController.getInstance();
				//updateEmailController.updateEmailAddress(newEmail);
				EmailAddressModel.getInstance().updateEmailAddress(newEmail);
			}
			else {
				//AddEmailController addEmailController = AddEmailController.getInstance();
				//addEmailController.addEmail(newEmail);
				EmailAddressModel.getInstance().addEmail(newEmail);
			}
		}
		else {
			emailError = true;
			lblEmailErrorText.setVisible(emailError);
		}


	}

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
}
