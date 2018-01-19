package com.osbot.jailbrake.ui;

import com.osbot.jailbrake.data.Constants;
import com.osbot.jailbrake.data.RuntimeVariables;
import com.osbot.jailbrake.utils.NetUtils;
import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

public class LoaderFrame extends JFrame implements ActionListener {

	private final JButton jailbreak;
	private final JLabel authors;
	private final JMenuBar menuBar;
	private final JMenu helpMenu;
	private final JMenuItem requestAccess, forums;
	private final JLabel status;

	public LoaderFrame() {
		super("OSBot Jailbreaker - BotUpgrade.us");
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		this.menuBar = new JMenuBar();
		this.helpMenu = new JMenu("Help");
		this.requestAccess = new JMenuItem("Request Access");
		this.requestAccess.setActionCommand("request access");
		this.requestAccess.addActionListener(this::actionPerformed);
		this.helpMenu.add(requestAccess);
		this.forums = new JMenuItem("Open Forums");
		this.forums.setActionCommand("forums");
		this.forums.addActionListener(this::actionPerformed);
		this.helpMenu.add(forums);
		this.menuBar.add(helpMenu);
		setJMenuBar(menuBar);

		this.authors = new JLabel("Developed by: Mate & Dredd");
		add(authors, BorderLayout.NORTH);

		this.status = new JLabel("Status: Launcher Ready");
		add(status, BorderLayout.CENTER);

		this.jailbreak = new JButton("Jailbreak");
		this.jailbreak.addActionListener(this::actionPerformed);
		this.jailbreak.setActionCommand("jailbreak");
		add(jailbreak, BorderLayout.SOUTH);

		setPreferredSize(new Dimension(300, 150));
		pack();
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		switch (e.getActionCommand()) {
			case "jailbreak":
				String jvmPid = null;
				List<VirtualMachineDescriptor> jvms = VirtualMachine.list();
				for (VirtualMachineDescriptor jvm : jvms) {
					if (jvm.displayName().contains(Constants.APPLICATION_NAME)) {
						status.setText("Status: OSBot client found!");
						jvmPid = jvm.id();
						break;
					}

				}
				if (jvmPid != null) {
					status.setText("Status: Preparing jailbreak...");
					File agentFile = new File(Constants.DIRECTORY_PATH + File.separator + Constants.JAILBREAK_JAR);
					if (!agentFile.exists()) {
						NetUtils.downloadJailbreak(RuntimeVariables.jarUrl);
					}
					if (agentFile.isFile()) {
						String agentFileName = agentFile.getName();
						String agentFileExtension = agentFileName.substring(agentFileName.lastIndexOf(".") + 1);
						if (agentFileExtension.equalsIgnoreCase("jar")) {
							try {
								status.setText("Status: Starting jailbreak...");
								VirtualMachine jvm = VirtualMachine.attach(jvmPid);
								jvm.loadAgent(agentFile.getAbsolutePath());
								jvm.detach();
								status.setText("Status: Jailbreak started!");
							} catch (Exception exception) {
								throw new RuntimeException(exception);
							}
							System.exit(0);
						}
					}
				} else {
					status.setText("Status: OSBot client not found!");
				}
				break;
			case "prepare jailbreak":
				break;
			case "forums":
				try {
					NetUtils.openWebpage(new URL("http://botupgrade.us/forums/").toURI());
				} catch (URISyntaxException e1) {
					e1.printStackTrace();
				} catch (MalformedURLException e1) {
					e1.printStackTrace();
				}
				break;
			case "request access":
				try {
					NetUtils.openWebpage(new URL("http://botupgrade.us/forums/").toURI());
				} catch (URISyntaxException e1) {
					e1.printStackTrace();
				} catch (MalformedURLException e1) {
					e1.printStackTrace();
				}
				break;
		}
	}
}
