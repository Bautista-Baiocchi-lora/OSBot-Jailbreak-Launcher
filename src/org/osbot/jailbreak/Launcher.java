package org.osbot.jailbreak;

import org.osbot.jailbreak.ui.LauncherController;

import javax.swing.*;

public class Launcher {

	/*
	Put the script selector into launcher (different tabs for different client support all at once.)
	Agent will take script name
	downloads script
	auto runs it?
	 */

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		new LauncherController().setVisible(true);
	}


}

