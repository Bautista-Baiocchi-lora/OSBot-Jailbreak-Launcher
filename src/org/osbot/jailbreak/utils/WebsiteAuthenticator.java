package org.osbot.jailbreak.utils;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

public class WebsiteAuthenticator extends Authenticator {

	private final String username, password;

	public WebsiteAuthenticator(String username, String password) {
		this.username = username;
		this.password = password;
	}

	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(username, password.toCharArray());
	}
}
