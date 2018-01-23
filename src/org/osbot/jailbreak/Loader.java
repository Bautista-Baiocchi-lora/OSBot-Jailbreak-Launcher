package org.osbot.jailbreak;

import org.osbot.jailbreak.data.Constants;
import org.osbot.jailbreak.utils.NetUtils;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class Loader {

	private String OsSeperator, javaHome, extendedHome, javaExecute;

	public Loader() {
		OsSeperator = System.getProperty("os.name").startsWith("Windows") ? ";" : ":";
		if (OsSeperator.equals(":")) {
			javaHome = "/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/";
			extendedHome = javaHome + File.separator + "jre";
		} else {
			javaHome = System.getProperty("java.home").replace(File.separator + "jre", "");
			extendedHome = System.getProperty("java.home");
		}
		javaExecute = javaHome + File.separator + "bin" + File.separator + "java";
		if (javaHome.contains("jdk")) {
			ProcessBuilder processBuilder = new ProcessBuilder(javaExecute, "-cp", getCommandLineArgument(), "org.osbot.jailbreak.Launcher");
			try {
				Process process = processBuilder.start();
				try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
					String line;
					while ((line = reader.readLine()) != null) {
						System.out.println(line);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			JOptionPane.showConfirmDialog(null, "No java JDK found. Please see forums for how to fix.", "Java JDK Required!", JOptionPane.DEFAULT_OPTION);
			System.exit(0);
		}
	}

	public String getCommandLineArgument() {
		StringBuilder argument = new StringBuilder();
		argument.append(extendedHome + File.separator + "lib" + File.separator + "ext" + File.separator + "*" + OsSeperator);
		argument.append(extendedHome + File.separator + "lib" + File.separator + "*" + OsSeperator);
		argument.append(javaHome + File.separator + "lib" + File.separator + "*" + OsSeperator);
		argument.append(getExecutionPath() + File.separator + "Jailbreak_Launcher.jar");
		return argument.toString();
	}

	private String getExecutionPath() {
		String absolutePath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		absolutePath = absolutePath.substring(0, absolutePath.lastIndexOf("/"));
		absolutePath = absolutePath.replaceAll("%20", " ");
		return absolutePath;
	}


	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			if (!Constants.LOAD_LOCAL) {
				final String VERSION = "1.0";
				if (!NetUtils.getResponse("http://botupgrade.us/private/launcher/launcher_version.txt").equals(VERSION)) {
					JOptionPane.showConfirmDialog(null, "Launcher out dated! Please download newest version from botupgrade.us/forums/!", "Update Required!", JOptionPane.DEFAULT_OPTION);
					System.exit(0);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		File file = new File(Constants.DIRECTORY_PATH);
		if (!file.exists()) {
			file.mkdirs();
		}
		new Loader();
	}
}
