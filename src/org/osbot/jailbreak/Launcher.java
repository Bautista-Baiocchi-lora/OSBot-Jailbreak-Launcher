package org.osbot.jailbreak;

import org.osbot.jailbreak.data.Constants;
import org.osbot.jailbreak.ui.LauncherController;
import org.osbot.jailbreak.utils.NetUtils;

import javax.swing.*;
import java.io.File;

public class Launcher {

	public static void main(String[] args) {
		System.out.println(System.getProperty("java.class.path"));
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			final String VERSION = "1.0";
			if (!NetUtils.getResponse("http://botupgrade.us/private/launcher/launcher_version.txt").equals(VERSION)) {
				JOptionPane.showConfirmDialog(null, "Launcher out dated! Please download newest version from botupgrade.us/forums/!", "Update Required!", JOptionPane.DEFAULT_OPTION);
				System.exit(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		File file = new File(Constants.DIRECTORY_PATH);
		if (!file.exists()) {
			file.mkdirs();
		}
		new LauncherController().setVisible(true);
	}


}

