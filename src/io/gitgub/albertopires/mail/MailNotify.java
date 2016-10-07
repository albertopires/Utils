/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package io.gitgub.albertopires.mail;

import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * 
 * @author Alberto Pires
 */
public class MailNotify {

	private String host;
	private String proto;
	private String sender;
	private boolean auth;
	private String subject;
	private String[] receipient;
	private String user;
	private String password;
	private String message;
	private Date timer;
	private int minTime;
	private Properties props;

	public static void main(String[] args) {
		// Without Authentication
		MailNotify mn = new MailNotify("stmp.example.com", "root@example.com");
		mn.setSubject("Teste New");
		String r[] = new String[2];
		r[0] = "user1@generic.com";
		r[1] = "user2@another.com";
		mn.setMessage("Test #0001 - 2011/07/12");
		mn.setReceipient(r);
		mn.setMinTime(120);
		mn.sendMail();

		while (true) {
			mn.sendMail();
		}

		// With Authentication
		/*
		 * MailNotify mna = new
		 * MailNotify("smtp.example.com","send@example.com");
		 * mna.setSubject("Teste Mail - Internet");
		 * mna.setUser("stmp_auth_user"); mna.setPassword("smtp_password");
		 * mna.setAuth(true); String ra[] = new String[2]; ra[0] =
		 * "user1@company.com"; ra[1] = "user2@anothercompany.com";
		 * mna.setMessage("Another test"); mna.setReceipient(ra);
		 * mna.sendMail();
		 */
	}

	public boolean isReady() {
		if (timer == null) {
			timer = new Date();
			return true;
		}
		Date aux = new Date();
		long elap = (aux.getTime() - timer.getTime()) / 1000;
		if (elap > minTime) {
			timer = new Date();
			return true;
		} else
			return false;
	}

	/**
	 * 
	 * @param smtp
	 *            SMTP Host
	 * @param sender
	 */
	public MailNotify(String smtp, String sender) {
		this.host = smtp;
		this.sender = sender;
		proto = "smtp";
		auth = false;
		subject = "Java Mail";
		props = System.getProperties();
		props.put("mail.smtp.host", smtp);
		timer = null;
		minTime = 900;
	}

	public void sendMail() {
		Session session = Session.getInstance(props);
		Message msg = new MimeMessage(session);
		Address h_snd[] = new Address[1];
		if (!isReady())
			return;
		try {
			h_snd[0] = new InternetAddress(sender);
			msg.addFrom(h_snd);
			msg.addHeader("Subject", subject);
			for (int i = 0; i < receipient.length; i++) {
				msg.addRecipient(Message.RecipientType.TO, new InternetAddress(
						receipient[i]));
			}
			msg.setText(message);
			// Transport.send(msg);
			SMTPTransport t = (SMTPTransport) session.getTransport(proto);
			if (isAuth())
				t.connect(host, user, password);
			else
				t.connect();
			t.sendMessage(msg, msg.getAllRecipients());
			System.out.println("Response: " + t.getLastServerResponse());
			t.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host
	 *            the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @return the proto
	 */
	public String getProto() {
		return proto;
	}

	/**
	 * @param proto
	 *            the proto to set
	 */
	public void setProto(String proto) {
		this.proto = proto;
	}

	/**
	 * @return the sender
	 */
	public String getSender() {
		return sender;
	}

	/**
	 * @param sender
	 *            the sender to set
	 */
	public void setSender(String sender) {
		this.sender = sender;
	}

	/**
	 * @return the auth
	 */
	public boolean isAuth() {
		return auth;
	}

	/**
	 * @param auth
	 *            the auth to set
	 */
	public void setAuth(boolean auth) {
		this.auth = auth;
	}

	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @param subject
	 *            the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @return the receipient
	 */
	public String[] getReceipient() {
		return receipient;
	}

	/**
	 * @param receipient
	 *            the receipient to set
	 */
	public void setReceipient(String[] receipient) {
		this.receipient = receipient;
	}

	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the props
	 */
	public Properties getProps() {
		return props;
	}

	/**
	 * @param props
	 *            the props to set
	 */
	public void setProps(Properties props) {
		this.props = props;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the minTime
	 */
	public int getMinTime() {
		return minTime;
	}

	/**
	 * @param minTime
	 *            in Seconds
	 */
	public void setMinTime(int minTime) {
		this.minTime = minTime;
	}
}
