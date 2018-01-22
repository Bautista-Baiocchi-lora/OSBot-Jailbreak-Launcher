package org.osbot.jailbreak;

import org.osbot.jailbreak.data.AttachAPI;
import org.osbot.jailbreak.data.Constants;
import org.osbot.jailbreak.ui.LauncherController;
import org.osbot.jailbreak.utils.NetUtils;

import javax.swing.*;
import java.io.File;

public class Loader {
	static {
		AttachAPI.ensureToolsJar();
	}

	public static void main(String[] args) {
		try {
			final String VERSION = "1.0";
			if (!NetUtils.getResponse("http://botupgrade.us/private/launcher/launcher_version.txt").equals(VERSION)) {
				JOptionPane.showConfirmDialog(null, "Launcher out dated! Please download newest version from botupgrade.us/forums/!", "Update Required!", JOptionPane.DEFAULT_OPTION);
				System.exit(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (IllegalAccessException | ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		File file = new File(Constants.DIRECTORY_PATH);
		if (!file.exists()) {
			file.mkdirs();
		}
		new LauncherController().setVisible(true);
	}

	public static void ensureToolsJar() {
		// do nothing, just ensure call to static initializer
	}
}

