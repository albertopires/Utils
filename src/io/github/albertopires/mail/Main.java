package io.github.albertopires.mail;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Main {
	static Properties mailServerProperties;
	static Session session;
	static MimeMessage mimeMessage;

	public static void main(String[] args) throws AddressException, MessagingException, UnsupportedEncodingException {
		if (args.length == 0) {
			System.err.println("Error");
			System.exit(0);
		}
		generateAndSendEmail(args[0], args[1]);
	}

	public static void generateAndSendEmail(String userId, String password) throws AddressException, MessagingException, UnsupportedEncodingException {
		// Step1
		System.out.println("\n 1st ===> setup Mail Server Properties..");
		mailServerProperties = System.getProperties();
		mailServerProperties.put("mail.smtp.port", "587");
		mailServerProperties.put("mail.smtp.auth", "true");
		mailServerProperties.put("mail.smtp.starttls.enable", "true");
		System.out.println("Mail Server Properties have been setup successfully..");

		// Step2
		System.out.println("\n\n 2nd ===> get Mail Session..");
		session = Session.getDefaultInstance(mailServerProperties, null);
		mimeMessage = new MimeMessage(session);
		mimeMessage.setFrom(new InternetAddress("mrpenguin2005@gmail.com", "Alberto Pires"));
		mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress("alberto.pires.on@gmail.com"));
		mimeMessage.addRecipient(Message.RecipientType.CC, new InternetAddress("apon1@terra.com.br"));
		mimeMessage.setSubject("Greetings from Crunchify..");
		String emailBody = "Test email by Crunchify.com JavaMail API example. " + "<br><br> Regards, <br>Crunchify Admin";
		mimeMessage.setContent(emailBody, "text/html");
		System.out.println("Mail Session has been created successfully..");

		// Step3
		System.out.println("\n\n 3rd ===> Get Session and Send mail");
		Transport transport = session.getTransport("smtp");

		// Enter your correct gmail UserID and Password
		// if you have 2FA enabled then provide App Specific Password
		transport.connect("smtp.sendgrid.net", userId, password);
		transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
		transport.close();
	}

}
