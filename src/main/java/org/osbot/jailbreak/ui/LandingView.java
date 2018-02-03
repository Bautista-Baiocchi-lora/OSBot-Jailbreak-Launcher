package org.osbot.jailbreak.ui;

import org.osbot.jailbreak.utils.Account;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LandingView extends JPanel implements ActionListener {

	private final LauncherController controller;
	private final JTextField email;
	private final JPasswordField password;
	private final JButton login, register;
	private final JLabel status;
	private final JCheckBox rememberMe;

	public LandingView(LauncherController controller, Account account) {
		this.controller = controller;
		this.setLayout(new BorderLayout());
		this.setBorder(new TitledBorder(new LineBorder(Color.BLACK, 2), "Login"));

		this.status = new JLabel("");
		this.status.setVisible(false);
		add(status, BorderLayout.PAGE_START);

		Box fieldsLayout = Box.createVerticalBox();

		Box emailLayout = Box.createHorizontalBox();
		emailLayout.add(new JLabel("Email: "));
		this.email = new JTextField(20);
		this.email.setToolTipText("Email");
		emailLayout.add(email);
		fieldsLayout.add(emailLayout);

		Box passwordLayout = Box.createHorizontalBox();
		passwordLayout.add(new JLabel("Password: "));
		this.password = new JPasswordField(20);
		this.password.setToolTipText("Password");
		passwordLayout.add(password);
		fieldsLayout.add(passwordLayout);

		add(fieldsLayout, BorderLayout.CENTER);

		Box buttonLayout = Box.createHorizontalBox();
		this.login = new JButton("Log In");
		this.login.setActionCommand("log in");
		this.login.addActionListener(this::actionPerformed);
		buttonLayout.add(login);

		this.register = new JButton("Register");
		this.register.setActionCommand("register");
		this.register.addActionListener(this::actionPerformed);
		buttonLayout.add(register);

		this.rememberMe = new JCheckBox("Remember me");
		buttonLayout.add(rememberMe);
		add(buttonLayout, BorderLayout.SOUTH);

		if (account != null) {
			configureAccount(account);
		}
	}

	private void configureAccount(Account account) {
		rememberMe.setSelected(true);
		email.setText(account.getEmail());
		password.setText(account.getPassword());
	}

	public void setStatus(String status) {
		this.status.setText(status);
		this.status.setForeground(Color.RED);
		this.status.setVisible(true);
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		switch (e.getActionCommand()) {
			case "log in":
				if (rememberMe.isSelected()) {
					controller.rememberAccount(new Account(email.getText(), String.valueOf(password.getPassword())));
				}
				controller.login(email.getText(), new String(password.getPassword()));
				break;
			case "register":
				controller.register();
				break;
		}
	}
}
