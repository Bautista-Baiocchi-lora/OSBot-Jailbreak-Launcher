package org.osbot.jailbreak.ui;

import com.sun.tools.attach.VirtualMachineDescriptor;
import org.osbot.jailbreak.data.Constants;
import org.osbot.jailbreak.utils.Account;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class LauncherController extends JFrame implements ActionListener {

	private final LauncherModel model;
	private final JMenuBar menuBar;
	private final JLabel authors;
	private final JMenu helpMenu;
	private final JMenuItem requestAccess, forums, uniqueID;
	private LandingView landingView;
	private DownloadView downloadView;
	private ClientSelectorView clientSelectorView;
	private JailbreakView jailbreakView;

	public LauncherController() {
		super("Jailbreaker Launcher - BotUpgrade.us");
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
		this.uniqueID = new JMenuItem("Generate Unique ID");
		this.uniqueID.setActionCommand("unique id");
		this.uniqueID.addActionListener(this::actionPerformed);
		this.helpMenu.add(uniqueID);
		this.menuBar.add(helpMenu);
		setJMenuBar(menuBar);

		this.authors = new JLabel("Developed by: Mate & Ethan");
		add(authors, BorderLayout.NORTH);

		this.landingView = new LandingView(this, model.getSavedAccount());
		add(landingView, BorderLayout.CENTER);

		this.setResizable(false);
		pack();
	}

	public int getId() {
		return model.getID();
	}

	public String getHWID() {
		try {
			return model.getHWID();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	public HashMap<String, VirtualMachineDescriptor> getJVMs() {
		return model.getJVMs();
	}

	public boolean verifyHWID() {
		return model.verifyHWID();
	}

	public boolean isVIP() {
		return model.isVIP();
	}


	public void downloadJailbreak() {
		try {
			model.downloadJailbreak();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void showDialog(String title, String message) {
		JOptionPane.showConfirmDialog(this, message, title, JOptionPane.DEFAULT_OPTION);
	}

	public void clientNotFound() {
		showDialog("Error!", "OSBot client not found. Please open one before attempting to jailbreak.");
		System.exit(0);
	}

	public void jailbreakFailed() {
		showDialog("Error!", "Jailbreak failed to load.");
		System.exit(0);
	}

	public void showHWID() {
		model.copyHWID();
		showDialog("Unique ID", "Your Unique ID has been copied to your clipboard.");
	}

	public void showSelectorView() {
		if (downloadView != null) {
			remove(downloadView);
		}
		this.clientSelectorView = new ClientSelectorView(this);
		add(clientSelectorView);
		updateInterface();
	}

	private void showDownloadView() {
		final File environmentJar = new File(Constants.DIRECTORY_PATH + File.separator + "environment.jar");
		if (!environmentJar.exists()) {
			this.downloadView = new DownloadView(this, "http://botupgrade.us/private/tools.jar", "environment");
			add(downloadView, BorderLayout.CENTER);
			updateInterface();
			this.downloadView.start();
		} else {
			showSelectorView();
		}
	}

	public void rememberAccount(Account account) {
		model.saveAccount(account);
	}

	public void jailbreak(List<String> jvms) {
		if (clientSelectorView != null) {
			remove(clientSelectorView);
		}
		jvms.stream().forEach(new Consumer<String>() {
			@Override
			public void accept(final String s) {
				s.replace("Client ", "");
			}
		});
		jailbreakView = new JailbreakView(this, jvms);
		add(jailbreakView);
		jailbreakView.start();
		updateInterface();
	}

	public void login(String email, String password) {
		if (model.login(email, password)) {
			if (model.verifyHWID()) {
				if (model.isVIP()) {
					remove(landingView);
					showDownloadView();
				} else {
					landingView.setStatus("You are not a VIP.");
				}
			} else if (landingView != null) {
				landingView.setStatus("This device is not approved.");
			}
		} else if (landingView != null) {
			landingView.setStatus("Invalid email or password");
		}
		updateInterface();
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
			case "unique id":
				showHWID();
				break;
		}
	}
}
