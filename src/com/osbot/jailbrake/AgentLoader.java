package com.osbot.jailbrake;

import com.osbot.jailbrake.data.Constants;
import com.osbot.jailbrake.ui.LauncherController;

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

