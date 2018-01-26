package org.osbot.jailbreak.utils;

import org.json.simple.JSONObject;

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


	public JSONObject toJson() {
		JSONObject account = new JSONObject();
		account.put("email", email);
		account.put("password", password);
		return account;
	}

	public static Account wrap(JSONObject jsonObject) {
		String email = (String) jsonObject.getOrDefault("email", "");
		String password = (String) jsonObject.getOrDefault("password", "");
		return new Account(email, password);
	}

}
