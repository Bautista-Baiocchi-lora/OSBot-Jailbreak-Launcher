package org.osbot.jailbreak.utils;

import java.io.Serializable;

public class Account implements Serializable {

	private static final long serialVersionUID = 42L;
	private String email, password;

	public Account(final String email, final String password) {
		this.email = email;
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public void setPassword(final String password) {
		this.password = password;
	}
}
