package org.osbot.jailbreak;

import org.osbot.jailbreak.data.Constants;
import org.osbot.jailbreak.utils.NetUtils;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Loader {

	private String OsSeparator, javaHome, extendedHome, javaExecute;

	public Loader() {
		configurePaths();
		if (javaHome.contains("jdk")) {
			String[] toolsJar = new File(javaHome + File.separator + "lib").list(new FilenameFilter() {
				@Override
				public boolean accept(final File dir, final String name) {
					return dir.isDirectory() && name.equalsIgnoreCase("tools.jar");
				}
			});
			if (toolsJar != null && toolsJar.length > 0) {
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
				JOptionPane.showConfirmDialog(null, "Your JDK is corrupted. Please re-install it.", "Corrupted JDK!", JOptionPane.DEFAULT_OPTION);
				System.exit(0);
			}
		} else {
			JOptionPane.showConfirmDialog(null, "No java JDK found. Please see forums for how to fix.", "Java JDK Required!", JOptionPane.DEFAULT_OPTION);
			System.exit(0);
		}
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			if (!Constants.LOAD_LOCAL_JAILBREAK) {
				final String VERSION = "1.2.1";
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

	public void configurePaths() {
		OsSeparator = System.getProperty("os.name").startsWith("Windows") ? ";" : ":";
		List<String> jdkPaths = new ArrayList<>();
		if (OsSeparator.equals(":")) {
			File baseJavaDirectory = new File("/Library/Java/JavaVirtualMachines/");
			for (File javaType : baseJavaDirectory.listFiles()) {
				if (javaType.isDirectory() && javaType.getName().contains("jdk")) {
					jdkPaths.add(javaType.getAbsolutePath());
				}
			}
			javaHome = getNewestJDK(jdkPaths) + "/Contents/Home/";
			extendedHome = javaHome + "jre";
			javaExecute = javaHome + "bin" + File.separator + "java";
		} else {
			File[] files86 = new File("C:/Program Files (x86)/java").listFiles();
			File[] non86Files = new File("C:/Program Files/java").listFiles();
			File[] combinedFiles = new File[files86.length + non86Files.length];
			System.arraycopy(files86, 0, combinedFiles, 0, files86.length);
			System.arraycopy(non86Files, 0, combinedFiles, files86.length, non86Files.length);
			for (File file : combinedFiles) {
				if (file.isDirectory() && file.getName().contains("jdk")) {
					jdkPaths.add(file.getAbsolutePath());
				}
			}
			javaHome = getNewestJDK(jdkPaths);
			extendedHome = javaHome + File.separator + "jre";
			javaExecute = javaHome + File.separator + "bin" + File.separator + "java";
		}
	}

	private String getNewestJDK(List<String> jdkPaths) {
		return jdkPaths.stream().sorted(new Comparator<String>() {
			@Override
			public int compare(final String o1, final String o2) {
				String o1Version = o1.split("jdk")[1].replace("_", ".");
				String o2Version = o2.split("jdk")[1].replace("_", ".");
				for (int i = 0; i < o1Version.split("\\.").length; i++) {
					int o1Number = Integer.parseInt(o1Version.split("\\.")[i]);
					int o2Number = Integer.parseInt(o2Version.split("\\.")[i]);
					if (o1Number != o2Number) {
						return o2Number - o1Number;
					}
				}
				return 0;
			}
		}).findFirst().orElse("");
	}


	public String getCommandLineArgument() {
		StringBuilder argument = new StringBuilder();
		argument.append(extendedHome + File.separator + "lib" + File.separator + "ext" + File.separator + "*" + OsSeparator);
		argument.append(extendedHome + File.separator + "lib" + File.separator + "*" + OsSeparator);
		argument.append(javaHome + File.separator + "lib" + File.separator + "*" + OsSeparator);
		argument.append(getExecutionPath() + File.separator + (Constants.RUN_THROUGH_IDE ? "" : getJarName()));
		return argument.toString();
	}

	private String getExecutionPath() {
		String absolutePath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		absolutePath = absolutePath.substring(0, absolutePath.lastIndexOf("/"));
		absolutePath = absolutePath.replaceAll("%20", " ");
		return absolutePath;
	}

	private String getJarName() {
		return new java.io.File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath()).getName().replace("%20", " ");
	}
}
