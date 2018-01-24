package org.osbot.jailbreak;

import org.osbot.jailbreak.ui.LauncherController;

import javax.swing.*;

public class Launcher {

	/*
	Put the script selector into launcher
	each client type (osbot, tribot, dreambot) have their own script selector tab. You pair clients with scripts
	Agent will take script name + link
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

