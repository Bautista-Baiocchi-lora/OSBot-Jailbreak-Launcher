package com.osbot.jailbreak;

import com.osbot.jailbreak.data.Constants;
import com.osbot.jailbreak.ui.LauncherController;

import javax.swing.*;
import java.io.File;

public class AgentLoader {

	public static void main(String[] args) {
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

}

