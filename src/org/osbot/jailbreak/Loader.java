package org.osbot.jailbreak;

import org.osbot.jailbreak.data.Constants;
import org.osbot.jailbreak.utils.NetUtils;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class Loader {


	/*
	/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/bin/java
	-Dfile.encoding=UTF-8
	-classpath
	"/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/jre/lib/charsets.jar:
	/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/jre/lib/deploy.jar:
	/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/jre/lib/ext/cldrdata.jar:
	/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/jre/lib/ext/dnsns.jar:
	/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/jre/lib/ext/jaccess.jar:
	/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/jre/lib/ext/jfxrt.jar:
	/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/jre/lib/ext/localedata.jar:
	/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/jre/lib/ext/nashorn.jar:
	/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/jre/lib/ext/sunec.jar:
	/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/jre/lib/ext/sunjce_provider.jar:
	/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/jre/lib/ext/sunpkcs11.jar:
	/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/jre/lib/ext/zipfs.jar:
	/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/jre/lib/javaws.jar:
	/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/jre/lib/jce.jar:
	/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/jre/lib/jfr.jar:
	/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/jre/lib/jfxswt.jar:
	/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/jre/lib/jsse.jar:
	/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/jre/lib/management-agent.jar:
	/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/jre/lib/plugin.jar:
	/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/jre/lib/resources.jar:
	/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/jre/lib/rt.jar:
	/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/lib/ant-javafx.jar:
	/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/lib/dt.jar:
	/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/lib/javafx-mx.jar:
	/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/lib/jconsole.jar:
	/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/lib/packager.jar:
	/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/lib/sa-jdi.jar:
	/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/lib/tools.jar:
	/Users/bautistabaiocchi-lora/Documents/IntelliJ Projects/OSBot-Jailbreak-Launcher/out/production/OSBot Jailbreaker" org.osbot.jailbreak.Loader
	 */

	private String OsSeperator, javaHome, executionPath;


	public Loader() {
		javaHome = System.getProperty("java.home");
		executionPath = getExecutionPath() + File.separator + Constants.JAILBREAK_LAUNCHER;
		System.out.println(javaHome);
		System.out.println(executionPath);
		if (javaHome.contains("jdk")) {
			OsSeperator = System.getProperty("os.name").startsWith("Windows") ? ";" : ":";
			System.out.println(OsSeperator);
			ProcessBuilder processBuilder = new ProcessBuilder();
			processBuilder.command("/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/bin/java");
			processBuilder.command("-classpath");
			processBuilder.command(getCommandLineArgument());
			processBuilder.command("org.osbot.jailbreak.Launcher");
			try {
				Process process = processBuilder.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			JOptionPane.showConfirmDialog(null, "No java JDK found. Please see forums for how to fix.", "No JDK found!", JOptionPane.DEFAULT_OPTION);
			System.exit(0);
		}
	}

	public String getCommandLineArgument() {
		StringBuilder argument = new StringBuilder();
		argument.append("\"" + javaHome + File.separator + "lib" + File.separator + "*" + OsSeperator);
		argument.append(javaHome + File.separator + "lib" + File.separator + "ext" + File.separator + "*" + OsSeperator);
		argument.append(executionPath + "\"");
		return argument.toString();
	}

	private String getExecutionPath() {
		String absolutePath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		absolutePath = absolutePath.substring(0, absolutePath.lastIndexOf("/"));
		absolutePath = absolutePath.replaceAll("%20", " "); // Surely need to do this here
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
