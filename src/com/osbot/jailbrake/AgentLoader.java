package com.osbot.jailbrake;

import com.osbot.jailbrake.data.Constants;
import com.osbot.jailbrake.ui.LoaderFrame;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Objects;

public class AgentLoader {

	public static void main(String[] args) {
		if (validateDirectory()) {
			new LoaderFrame().setVisible(true);
		}
	}


	private static boolean validateDirectory() {
		File directory = new File(Constants.DIRECTORY_PATH);
		if (!directory.exists()) {
			if (!directory.mkdirs()) {
				throw new RuntimeException("Directory loader error");
			}
			validateDirectory();
		}
		boolean hasJailbreak = Objects.requireNonNull(directory.listFiles(new FileFilter() {
			@Override
			public boolean accept(final File pathname) {
				return pathname.getName().equalsIgnoreCase(Constants.JAILBREAK_JAR);
			}
		})).length > 0;
		if (!hasJailbreak) {
			System.out.println("Downloading jailbreak...");
			downloadJailbreak();
			System.out.println("Jailbreak downloaded!");
			validateDirectory();
		}
		return true;
	}

	private static void downloadJailbreak() {
		try {
			URL download = new URL(Constants.JAILBREAK_JAR_LINK);
			ReadableByteChannel rbc = Channels.newChannel(download.openStream());
			FileOutputStream fileOut = new FileOutputStream(Constants.DIRECTORY_PATH + File.separator + Constants.JAILBREAK_JAR);
			fileOut.getChannel().transferFrom(rbc, 0, 1 << 24);
			fileOut.close();
			rbc.close();
		} catch (Exception e) { e.printStackTrace(); }
	}
}

