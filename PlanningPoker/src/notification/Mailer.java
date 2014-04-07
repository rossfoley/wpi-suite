/*******************************************************************************
 * Copyright (c) 2014 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Team8s
 ******************************************************************************/
package notification;


import java.util.Properties;

import javax.mail.PasswordAuthentication;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
//import javax.mail.internet.*;


/**
 * 
 * @author Kevin Barry & Andrew Leonard
 * Handles the mailing to users about events
 */
public class Mailer {
	
	private String to;
	private String from;
	private String host;
	private Properties properties;
	private Session session;
	private String port;
	
	private final String username = "theteam8s@gmail.com";
	private final String password = "TheTeam8";
	
	//Validate
    
	/**
	 * Mailer is a class that doesn't take any parameters. It has one function: MailTo
	 */
    public Mailer()
    {

    	//System.out.println("starting");

        // Get system properties
        properties = System.getProperties();

        // Setup mail server
        
        //Information for the sending email
        

        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        properties.put("mail.smtp.starttls.enable", "true");
       // properties.put("mail.smtp.starttls.enable", "true"); 

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
	 * 
	 * @param recipient The email of who you want to send the mail to. It needs to be a proper email address
	 * @param subject String containing the title of the message
	 * @param body String containing the message to be sent
	 * @return True if email was sent successfully, false otherwise.
	 */
	public boolean MailTo(String recipient, String subject, String body){
		//System.out.println("starting to mail");
		
		boolean isValidSender = true;
		boolean isValidReciever = true;
		try {
			InternetAddress emailSender = new InternetAddress(username);
			emailSender.validate();
		} catch (AddressException e) {
			isValidSender = false;
			//e.printStackTrace();
		}
		try {
			InternetAddress emailRecip = new InternetAddress(recipient);
			emailRecip.validate();
		} catch (AddressException e) {
			isValidReciever = false;
			//e.printStackTrace();
		}
		
		

		if(isValidSender == true && isValidReciever == true){
	      try{
	         // Create a default MimeMessage object.
	         MimeMessage message = new MimeMessage(session);

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
	         //System.out.println("Sent message successfully");
	      }catch (MessagingException mex) {
	         mex.printStackTrace();
	      }
		}
		if (isValidSender == false){
			System.out.println(username + " is not a valid email address");	
		}
		if (isValidReciever == false){
			System.out.println(recipient + " is not a valid email address");
		}
		
		return isValidSender && isValidReciever;
	}
	
	
}
