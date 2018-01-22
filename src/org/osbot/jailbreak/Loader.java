package org.osbot.jailbreak;

import org.osbot.jailbreak.data.Constants;
import org.osbot.jailbreak.ui.LauncherModel;
import org.osbot.jailbreak.utils.NetUtils;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class Loader {

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
		File file = new File(Constants.DIRECTORY_PATH);
		if (!file.exists()) {
			file.mkdirs();
		}
		try {
			exec(LauncherModel.class);
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static int exec(Class klass) throws IOException,
			InterruptedException {
		String javaHome = System.getProperty("java.home");
		String javaBin = javaHome +
				File.separator + "bin" +
				File.separator + "java";
		String classpath = System.getProperty("java.class.path");
		String className = klass.getCanonicalName();

		ProcessBuilder builder = new ProcessBuilder(
				javaBin, "-cp", classpath, className);

		Process process = builder.start();
		process.waitFor();
		return process.exitValue();
	}

}

