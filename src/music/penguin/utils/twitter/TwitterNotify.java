/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package music.penguin.utils.twitter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

/**
 * 
 * @author Administrador
 */
public class TwitterNotify {

	private TwitterFactory factory;
	private AccessToken accessToken;
	private Twitter sender;
	private String consumerKey;
	private String consumerSecret;
	private String tokenKey;
	private String tokenSecret;
	private String userId;
	private Date timer;
	private int minTime;

	public static void main(String args[]) throws Exception {
		TwitterNotify twitterNotify;
		// getOAuthAccessToken();
		Properties conf = new Properties();
		// conf.put("consumer.key", "aaaaaaaaaaaaaaaaaaaaa");
		// conf.put("consumer.secret", "sssssssssssssssssssssssssssssssssssssssss");
		// conf.put("token.key","111111111-xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
		// conf.put("token.secret","SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS");
		// conf.put("userid","111111111");
		File f = new File("c:\\Alberto\\ooo\\myjconsole.conf");
		try {
			FileInputStream fi = new FileInputStream(f);
			conf.load(fi);
		} catch (IOException e) {
			e.printStackTrace();
		}
		twitterNotify = new TwitterNotify(conf);

		twitterNotify.update("Teste 20110714-05 - Failsafe Test");
		twitterNotify.setMinTime(60);
		try {
			Thread.sleep(61500);
		} catch (Exception e) {
		}
		twitterNotify.update("Teste 20110714-06 - Failsafe Test");
	}

	public TwitterNotify(Properties conf) {
		// The factory instance is re-useable and thread safe.
		timer = null;
		minTime = 900;
		factory = new TwitterFactory();
		consumerKey = conf.getProperty("consumer.key");
		consumerSecret = conf.getProperty("consumer.secret");
		tokenKey = conf.getProperty("token.key");
		tokenSecret = conf.getProperty("token.secret");
		userId = conf.getProperty("userid");

		accessToken = new AccessToken(tokenKey, tokenSecret);
		sender = factory.getInstance();
		sender.setOAuthConsumer(consumerKey, consumerSecret);
		sender.setOAuthAccessToken(accessToken);
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

	public void update(String text) {
		if (isReady()) {
			try {
				Status status = sender.updateStatus(text);
				System.err.println("Successfully updated the status to ["
						+ status.getText() + "].");
			} catch (TwitterException ex) {
				Logger.getLogger(TwitterNotify.class.getName()).log(
						Level.SEVERE, null, ex);
			}
		}
	}

	public static void getOAuthAccessToken() throws Exception {
		// The factory instance is re-useable and thread safe.
		Twitter twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer("key", "secret");
		RequestToken requestToken = twitter.getOAuthRequestToken();
		AccessToken accessToken = null;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while (null == accessToken) {
			System.out
					.println("Open the following URL and grant access to your account:");
			System.out.println(requestToken.getAuthorizationURL());
			System.out
					.print("Enter the PIN(if aviailable) or just hit enter.[PIN]:");
			String pin = br.readLine();
			try {
				if (pin.length() > 0) {
					accessToken = twitter
							.getOAuthAccessToken(requestToken, pin);
				} else {
					accessToken = twitter.getOAuthAccessToken();
				}
			} catch (TwitterException te) {
				if (401 == te.getStatusCode()) {
					System.out.println("Unable to get the access token.");
				} else {
					te.printStackTrace();
				}
			}
		}
		// persist to the accessToken for future reference.
		showAccessToken(twitter.verifyCredentials().getId(), accessToken);
	}

	private static void showAccessToken(long useId, AccessToken accessToken) {
		System.out.println("--> useId       : " + useId);
		System.out.println("--> accessToken : " + accessToken);
		System.out.println("--> accessToken : " + accessToken.getToken());
		System.out.println("--> accessToken : " + accessToken.getTokenSecret());
	}

	/**
	 * @return the minTime
	 */
	public int getMinTime() {
		return minTime;
	}

	/**
	 * @param minTime
	 *            the minTime to set
	 */
	public void setMinTime(int minTime) {
		this.minTime = minTime;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return userId;
	}
}
