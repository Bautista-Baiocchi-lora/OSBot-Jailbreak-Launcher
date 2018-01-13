package com.osbot.jailbrake.ui;

import com.osbot.jailbrake.data.Constants;
import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class LoaderFrame extends JFrame implements ActionListener {

	private final JButton jailbreak;
	private final JLabel authors, image;
	private Image fuckAlekImage;

	public LoaderFrame() {
		super("OSBot Jailbreaker");
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		try {
			fuckAlekImage = ImageIO.read(getClass().getResource("/doge.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		Box authorLayout = Box.createHorizontalBox();

		this.authors = new JLabel("Created by: ");
		authorLayout.add(authors, BorderLayout.NORTH);
		this.image = new JLabel();
		this.image.setIcon(new ImageIcon(fuckAlekImage));
		authorLayout.add(image);

		add(authorLayout, BorderLayout.CENTER);

		this.jailbreak = new JButton("Jailbreak");
		this.jailbreak.addActionListener(this::actionPerformed);
		add(jailbreak, BorderLayout.SOUTH);

		setPreferredSize(new Dimension(300, 150));
		pack();
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		String jvmPid = null;
		List<VirtualMachineDescriptor> jvms = VirtualMachine.list();
		for (VirtualMachineDescriptor jvm : jvms) {
			if (jvm.displayName().contains(Constants.APPLICATION_NAME)) {
				System.out.println("OSBot client found!");
				jvmPid = jvm.id();
				break;
			}

		}
		if (jvmPid != null) {
			File agentFile = new File(Constants.DIRECTORY_PATH + File.separator + Constants.JAILBREAK_JAR);
			if (agentFile.isFile()) {
				String agentFileName = agentFile.getName();
				String agentFileExtension = agentFileName.substring(agentFileName.lastIndexOf(".") + 1);
				if (agentFileExtension.equalsIgnoreCase("jar")) {
					try {
						System.out.println("Jailbreaking...");
						VirtualMachine jvm = VirtualMachine.attach(jvmPid);
						jvm.loadAgent(agentFile.getAbsolutePath());
						jvm.detach();
						System.out.println("Jailbreak successful!");
					} catch (Exception exception) {
						throw new RuntimeException(exception);
					}
					System.exit(0);
				}
			}
		} else {
			System.out.println("OSBot client not found!");
		}
	}
}
