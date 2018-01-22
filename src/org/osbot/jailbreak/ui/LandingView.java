package org.osbot.jailbreak.ui;

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

	public LandingView(LauncherController controller) {
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
		this.email.setText("ethan.ducky@gmail.com");
		emailLayout.add(email);
		fieldsLayout.add(emailLayout);

		Box passwordLayout = Box.createHorizontalBox();
		passwordLayout.add(new JLabel("Password: "));
		this.password = new JPasswordField(20);
		this.password.setToolTipText("Password");
		this.password.setText("iloveAyla123!");
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
		add(buttonLayout, BorderLayout.SOUTH);
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
				controller.login(email.getText(), new String(password.getPassword()));
				break;
			case "register":
				controller.register();
				break;
		}
	}
}
