package it.ciroliviero.model.notifications;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import it.ciroliviero.model.Page;

public class MailSender implements NotificationMethod{

	private Session session;
	private String emailAddress;

	public MailSender(String username, String password) {
		// Setup properties for smtp
		Properties properties = new Properties();
		properties.setProperty("mail.transport.protocol", "smtp");
		properties.setProperty("mail.host", "smtp.live.com");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.auth", "true");


		// Tries to get a session with specified username and password
		this.session = Session.getInstance(properties,
				new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
	}

	public boolean checkAuthenticationState(String username, String password){
		try {
			Transport fakeTransport = session.getTransport("smtp");
			fakeTransport.connect("smtp.live.com", username, password);
			fakeTransport.close();

			//Authentication success
			this.emailAddress = username;
			return true;
		} catch (MessagingException e) {
			//Authentication failed
			System.out.println("Authentication Exception");
			return false;
		}
	}

	@Override
	public void notifyPageChanged(Page oldVersionPage, Page newVersionPage) {
		try {
			System.out.println("Sending mail...");
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(emailAddress));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(emailAddress));
			message.setSubject("Something changed!");
			message.setText("Hey this is PageChangedDetector!\n"
					+ "At "+newVersionPage.getDateOfFinding()+","
					+ " something changed in your target site " +newVersionPage.getUrl()
					+ ".\nCheck it out! :)" );
			Transport.send(message);
			System.out.println("Done");
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

}


