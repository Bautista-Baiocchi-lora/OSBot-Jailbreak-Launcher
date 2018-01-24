package org.osbot.jailbreak.ui;

import com.sun.tools.attach.VirtualMachine;
import org.osbot.jailbreak.data.Constants;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.List;

public class JailbreakView extends JPanel {

	private final JProgressBar progressBar;
	private final LauncherController controller;
	private final Jailbreak jailbreak;
	private final java.util.List<String> jvmPid;

	public JailbreakView(LauncherController controller, List<String> pid) {
		this.controller = controller;
		this.jvmPid = pid;
		this.progressBar = new JProgressBar(0, 100);
		this.progressBar.setStringPainted(true);
		this.progressBar.setString("Jailbreak...");
		this.progressBar.setPreferredSize(new Dimension(300, 50));

		this.jailbreak = new Jailbreak();
		this.jailbreak.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(final PropertyChangeEvent evt) {
				if ("progress".equals(evt.getPropertyName())) {
					progressBar.setValue((Integer) evt.getNewValue());
				}
			}
		});

		add(progressBar);
	}

	public void start() {
		this.jailbreak.execute();
	}

	private final class Jailbreak extends SwingWorker<Void, Void> {

		@Override
		protected Void doInBackground() throws Exception {
			if (controller.verifyHWID()) {
				setProgress(15);
				if (controller.isVIP()) {
					setProgress(30);
					controller.downloadJailbreak();
					setProgress(45);
				}
			}
			int interval = 55 / (jvmPid.size() * 3);
			for (String pid : jvmPid) {
				jailbreak(pid, interval);
			}
			return null;
		}

		private void jailbreak(String jvmPid, int sleep) {
			if (jvmPid != null) {
				jvmPid = jvmPid.replaceAll("[^\\d]", "");
				System.out.println("Status: Preparing jailbreak...");
				File agentFile = new File(Constants.DIRECTORY_PATH + File.separator + Constants.JAILBREAK_JAR);
				if (agentFile.isFile()) {
					String agentFileName = agentFile.getName();
					String agentFileExtension = agentFileName.substring(agentFileName.lastIndexOf(".") + 1);
					if (agentFileExtension.equalsIgnoreCase("jar")) {
						try {
							System.out.println("Status: Starting jailbreak...");
							VirtualMachine jvm = VirtualMachine.attach(jvmPid);
							setProgress(sleep);
							StringBuilder agentParameters = new StringBuilder().append(controller.getId()).append(":" + controller.getHWID()).append(":HtEk6jyT6kAgpHc6VbRbj");
							jvm.loadAgent(agentFile.getAbsolutePath(), agentParameters.toString());
							jvm.detach();
							setProgress(sleep);
							System.out.println("Status: Jailbreak started!");
						} catch (Exception exception) {
							exception.printStackTrace();
							controller.jailbreakFailed();
							throw new RuntimeException(exception);
						}
						System.exit(0);
					}
				}
			} else {
				controller.clientNotFound();
			}
		}

	}
}
