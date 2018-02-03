package org.osbot.jailbreak;

import org.osbot.jailbreak.ui.LauncherController;
import org.osbot.jailbreak.utils.WebsiteAuthenticator;

import javax.swing.*;
import java.net.Authenticator;

public class Launcher {

	public static void main(String[] args) {
		Authenticator.setDefault(new WebsiteAuthenticator("C8VhFM9mRbBm8", "mEYshRY94KYT9"));
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		new LauncherController().setVisible(true);
	}


}

