/*******************************************************************************
 * Copyright (c) 2014 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Team8s
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.planningpoker.notifications;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import edu.wpi.cs.wpisuitetng.janeway.config.ConfigManager;
import edu.wpi.cs.wpisuitetng.modules.planningpoker.models.PlanningPokerSession;

/**
 * @author Kevin Barry & Andrew Leonard
 * Handles the mailing to users about events
 */
public class Mailer {
	private final Properties properties;
	private final Session session;

	private final String username = "wpisuiteplanningpoker@gmail.com";
	private final String password = "Q1W2E3ASDF";

	//Validate

	/**
	 * Default Constructor for the mailing system. Assigns the mailing system to mail from Gmail using the team8s as the sender
	 * Mailer is a class that doesn't take any parameters. It has one function: MailTo
	 */
	public Mailer()
	{
		// Get system properties
		properties = System.getProperties();

		// Setup mail server

		//Information for the sending email
		properties.put("mail.smtp.auth", "true");
		//Port
		properties.put("mail.smtp.port", "587");
		//Host server
		properties.put("mail.smtp.host", "smtp.gmail.com");
		//trust gmail
		properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
		//switches to a ttls protected connection
		properties.put("mail.smtp.starttls.enable", "true");

		// Get the default Session object.
		//session = Session.getInstance(properties);

		session = Session.getInstance(properties,
				new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
	}

	/**
	 * Sends an email to the given recipient with the given subject as it's subject and the given body as its content.
	 * @param recipient The email of who you want to send the mail to. It needs to be a proper email address
	 * @param subject String containing the title of the message
	 * @param body String containing the message to be sent
	 * @return True if email was sent successfully, false otherwise.
	 */
	public boolean mailTo(String recipient, String subject, String body){

		if (recipient == null || subject == null || body == null) {
			return false;
		}

		boolean isValidSender = true;
		boolean isValidReciever = true;
		try {
			final InternetAddress emailSender = new InternetAddress(username);
			emailSender.validate();
		} catch (AddressException e) {
			isValidSender = false;
		}
		try {
			final InternetAddress emailRecip = new InternetAddress(recipient);
			emailRecip.validate();
		} catch (AddressException e) {
			isValidReciever = false;
		}



		if(isValidSender && isValidReciever){
			try{
				// Create a default MimeMessage object.
				final MimeMessage message = new MimeMessage(session);

				// Set From: header field of the header.
				message.setFrom(new InternetAddress(username));

				// Set To: header field of the header.
				message.addRecipient(Message.RecipientType.TO,
						new InternetAddress(recipient));

				// Set Subject: header field
				message.setSubject(subject);

				// Now set the actual messages
				message.setText(body);

				// Send message
				Transport.send(message);
			}catch (MessagingException mex) {
				System.out.println(mex.getMessage());
			}
		}

		return isValidSender && isValidReciever;
	}
	
	/**
	 * Mails the people in the List of recipients with the subject and body of the message as specified
	 * @param recipients List of email addresses formated as a string to send the given message to
	 * @param subject Subject of the message to send to the recipients
	 * @param body Body of the message to send to the recipients.
	 * @return A list of people who could not be messaged because they did not have a properly formatted e-mail address.
	 */
	public List<String> mailToGroup(List<String> recipients, String subject, String body) {		
		final List<String> didNotSendTo = new ArrayList<String>();

		if (recipients == null || subject == null || body == null) {
			return null;
		}

		boolean thisValid;
		for (int i = 0; i < recipients.size(); i++) {
			thisValid = mailTo(recipients.get(i), subject, body);
			if (!thisValid) {
				didNotSendTo.add(recipients.get(i));
			}
		}
		return didNotSendTo;
	}

	/**
	 * Sends an email to the given list of recipients notifying them that a given Planning Poker Session has started
	 * Sends an email to the list of recipients that there that the given session has started
	 * @param recipients List of email addresses to notify of the start of a Planning Poker Session
	 * @param planningPokerSession The Planning Poker Session to alert the team about
	 * @return A list of people who could not be messaged because they did not have a properly formatted e-mail address.
	 */
	public List<String> notifyOfPlanningPokerSessionStart(List<String> recipients, PlanningPokerSession planningPokerSession) {
		final List<String> didNotSendTo = new ArrayList<String>();

		if (recipients == null || planningPokerSession == null) {
			return null;
		}

		boolean thisValid;
		
		String url = ConfigManager.getConfig().getCoreUrl().toString();
		int offset = 3;
		if (url.endsWith("/API/")) {
			offset++;
		}
		String finalUrl = url.substring(0, url.length() - offset) + "login.html?session=" + planningPokerSession.getUuid().toString();
		
		if (planningPokerSession.getEndDate() != null) { 
			final int day = planningPokerSession.getEndDate().get(planningPokerSession.getEndDate().DAY_OF_MONTH);
			final int month = (1 + planningPokerSession.getEndDate().get(planningPokerSession.getEndDate().MONTH));
			final int year = planningPokerSession.getEndDate().get(planningPokerSession.getEndDate().YEAR);
			final String hour = formatHour(planningPokerSession.getEndDate());
			final String minute = formatMinute(planningPokerSession.getEndDate());
			final String am_pm = formatAM_PM(planningPokerSession.getEndDate());
			final String endTime = month + "/" + day + "/" + year + " at " + hour + ":" + minute + am_pm;
			
			for (String recipient : recipients) {
				thisValid = mailTo(recipient, "Planning Poker Session: " + planningPokerSession.getName() + 
						" Has been started", "The Session: " + planningPokerSession.getName() + 
						" has been started. Its end date is: " + endTime + ".  To vote on this session, you can login to Janeway or go to "
						+ finalUrl +
						"\n\nGood Luck! \n\n --Your Development Team");
				if (!thisValid) {
					didNotSendTo.add(recipient);
				}
			}
		}
		else {
			for (String recipient : recipients) {
				thisValid = mailTo(recipient, "Planning Poker Session: " + planningPokerSession.getName() + 
						" Has been started", "The Session: " + planningPokerSession.getName() + 
						" has been started. The session doesn't currently have an end date. To vote on this session, you can login to Janeway or go to " 
						+ finalUrl +	
						"\n\nGood Luck! \n\n --Your Development Team");
				if (!thisValid) {
					didNotSendTo.add(recipient);
				}
			}
		}
		return didNotSendTo;
	}
	
	/**
	 * Sends an email to the given list of recipients notifying them that a given Planning Poker Session has ended
	 * @param recipients List of email addresses to notify of the start of a Planning Poker Session
	 * @param planningPokerSession The Planning Poker Session to alert the team about
	 * @return A list of people who could not be messaged because they did not have a properly formatted e-mail address.
	 */
	public List<String> notifyOfPlanningPokerSessionClose(List<String> recipients, PlanningPokerSession planningPokerSession) {
		final List<String> didNotSendTo = new ArrayList<String>();

		if (recipients == null || planningPokerSession == null) {
			return null;
		}

		boolean thisValid;
		for (int i = 0; i < recipients.size(); i++) {
			thisValid = mailTo(recipients.get(i), "Planning Poker Session: " + planningPokerSession.getName() + " has closed", "The Session: " + planningPokerSession.getName() + " has been closed. \n\nThank you for participating! \n\n --Your Development Team");
			if (!thisValid) {
				didNotSendTo.add(recipients.get(i));
			}
		}
		return didNotSendTo;
	}


	/**
	 * Gets the minute from the given calendar
	 * @param date the calendar to get the minute from
	 * @return returns the formated minute
	 */
	private String formatMinute(GregorianCalendar date){
		String minute = "";
		if(date.get(Calendar.MINUTE) == 0){
			minute = Integer.toString(date.get(Calendar.MINUTE)) + "0";
		}
		else{
			minute = Integer.toString(date.get(Calendar.MINUTE));
		}
		return minute;
	}
	/**
	 * Gets the hour from the given calendar
	 * @param date the calendar to get the hour from
	 * @return returns the formated hour
	 */
	private String formatHour(GregorianCalendar date){
		String hour = "";
		if(date.get(Calendar.HOUR) == 0){
			hour = "12";
		}
		else{
			hour = Integer.toString(date.get(Calendar.HOUR));
		}
		return hour;	
	}
	/**
	 * Gets AM/PM from the given calendar
	 * @param date the calendar to determine AM/PM from
	 * @return returns AM or PM
	 */
	private String formatAM_PM(GregorianCalendar date){
		String AM_PM = "";
		if(date.get(Calendar.AM_PM) == 0){
			AM_PM = "AM";
		}
		else{
			AM_PM = "PM";
		}
		return AM_PM;

	}


}

