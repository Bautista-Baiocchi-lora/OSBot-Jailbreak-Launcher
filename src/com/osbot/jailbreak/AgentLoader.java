package com.osbot.jailbreak;

import com.osbot.jailbreak.data.Constants;
import com.osbot.jailbreak.ui.LauncherController;

import java.io.File;

public class AgentLoader {

	public static void main(String[] args) {
		File file = new File(Constants.DIRECTORY_PATH);
		if (!file.exists()) {
			file.mkdirs();
		}
		new LauncherController().setVisible(true);
	}

}

