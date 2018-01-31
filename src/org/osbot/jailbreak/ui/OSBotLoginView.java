package org.osbot.jailbreak.ui;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OSBotLoginView extends JPanel implements ActionListener {

	private final LauncherController controller;
	private final JTextField username;
	private final JPasswordField password;
	private final JButton login, cancel;

	public OSBotLoginView(final LauncherController controller) {
		this.controller = controller;

		this.setLayout(new BorderLayout());
		this.setBorder(new TitledBorder(new LineBorder(Color.BLACK, 2), "OSBot Login"));

		Box fields = Box.createVerticalBox();

		fields.add(new JLabel("PLEASE USE AN ALTERNATE OSBOT ACCOUNT!"));

		Box usernameLayout = Box.createHorizontalBox();
		usernameLayout.add(new JLabel("OSBot username: "));
		this.username = new JTextField(15);
		this.username.setToolTipText("OSBot username");
		usernameLayout.add(username);
		fields.add(usernameLayout);

		Box passwordLayout = Box.createHorizontalBox();
		passwordLayout.add(new JLabel("Password: "));
		this.password = new JPasswordField(15);
		this.password.setToolTipText("Password");
		passwordLayout.add(password);
		fields.add(passwordLayout);

		add(fields, BorderLayout.CENTER);

		Box buttonLayout = Box.createHorizontalBox();

		this.login = new JButton("Login");
		this.login.setActionCommand("login");
		this.login.addActionListener(this::actionPerformed);
		buttonLayout.add(login);

		this.cancel = new JButton("Cancel");
		this.cancel.setActionCommand("cancel");
		this.cancel.addActionListener(this::actionPerformed);
		buttonLayout.add(cancel);

		add(buttonLayout, BorderLayout.SOUTH);
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		switch (e.getActionCommand()) {
			case "login":
				controller.startOSBotClient(username.getText(), String.valueOf(password.getPassword()));
				break;
			case "cancel":
				controller.showSelectorView();
				break;
		}
	}
}
