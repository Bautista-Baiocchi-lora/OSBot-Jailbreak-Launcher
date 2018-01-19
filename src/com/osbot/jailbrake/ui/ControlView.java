package com.osbot.jailbrake.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControlView extends JPanel implements ActionListener{

	private final LauncherController controller;
	private final JButton jailbreak;
	private final JLabel status;

	public ControlView(LauncherController controller) {
		this.controller= controller;

		this.status = new JLabel("Status: Launcher Ready");
		add(status, BorderLayout.CENTER);

		this.jailbreak = new JButton("Jailbreak");
		this.jailbreak.addActionListener(this);
		this.jailbreak.setActionCommand("jailbreak");
		add(jailbreak, BorderLayout.SOUTH);
	}

	public void setStatus(String status) {
		this.status.setText("Status: " + status);
	}


	@Override
	public void actionPerformed(final ActionEvent e) {
		controller.jailbreak();
	}
}
