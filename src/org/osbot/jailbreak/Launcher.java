package org.osbot.jailbreak;

import org.osbot.jailbreak.ui.LauncherController;

import javax.swing.*;

public class Launcher {

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		new LauncherController().setVisible(true);
	}


}

