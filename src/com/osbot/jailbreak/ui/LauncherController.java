package com.osbot.jailbreak.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LauncherController extends JFrame implements ActionListener {

	private final LauncherModel model;
	private final LandingView landingView;
	private ControlView controlView;
	private final JMenuBar menuBar;
	private final JLabel authors;
	private final JMenu helpMenu;
	private final JMenuItem requestAccess, forums;

	public LauncherController() {
		super("OSBot Jailbreaker - BotUpgrade.us");
		this.model = new LauncherModel(this);
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

		this.landingView = new LandingView(this);
		add(landingView, BorderLayout.CENTER);

		setPreferredSize(new Dimension(300, 150));
		pack();
	}


	public void jailbreak() {
		if (model.verifyHWID() && model.isVIP()) {
			model.downloadJailbreak();
			model.startJailbreak();
			System.exit(0);
		}
	}

	public void login(String email, String password) {
		if (model.login(email, password)) {
			if (model.verifyHWID()) {
				if (model.isVIP()) {
					this.controlView = new ControlView(this);
					remove(landingView);
					add(controlView);
					updateInterface();
				} else {
					landingView.setStatus("You are not a VIP.");
				}
			} else if (landingView != null) {
				landingView.setStatus("This device is not approved.");
			}
		} else if (landingView != null) {
			landingView.setStatus("Invalid email or password");
		}
	}

	public void register() {
		model.openRegisterPage();
	}

	private void updateInterface() {
		revalidate();
		pack();
		repaint();
	}


	@Override
	public void actionPerformed(final ActionEvent e) {
		switch (e.getActionCommand()) {
			case "forums":
				model.openForums();
				break;
			case "request access":
				model.openRequestAccessPage();
				break;
		}
	}
}
