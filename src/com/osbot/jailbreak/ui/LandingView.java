package com.osbot.jailbreak.ui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LandingView extends JPanel implements ActionListener {

	private final LauncherController controller;
	private final JTextField email;
	private final JPasswordField password;
	private final JButton login, register;
	private final JLabel status;

	public LandingView(LauncherController controller) {
		this.controller = controller;

		Box landingLayout = Box.createVerticalBox();

		this.status= new JLabel("Please Log in");
		landingLayout.add(status);

		this.email = new JTextField();
		this.email.setToolTipText("Email");
		new JLabel("Email").setLabelFor(email);
		landingLayout.add(email);

		this.password = new JPasswordField();
		this.password.setToolTipText("Password");
		new JLabel("Password").setLabelFor(password);
		landingLayout.add(password);

		this.login = new JButton("Login");
		this.login.setActionCommand("login");
		this.login.addActionListener(this::actionPerformed);
		landingLayout.add(login);

		this.register = new JButton("Register");
		this.register.setActionCommand("register");
		this.register.addActionListener(this::actionPerformed);
		landingLayout.add(register);

		add(landingLayout);
	}

	public void setStatus(String status){
		this.status.setText(status);
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		switch (e.getActionCommand()){
			case "login":
				controller.login(email.getText(), new String(password.getPassword()));
				break;
			case "register":
				controller.register();
				break;
		}
	}
}
