package org.osbot.jailbreak.ui;

import com.sun.tools.attach.VirtualMachineDescriptor;
import org.osbot.jailbreak.classloader.ClassArchive;
import org.osbot.jailbreak.data.Constants;
import org.osbot.jailbreak.utils.reflection.ReflectedClass;
import org.osbot.jailbreak.utils.reflection.ReflectionEngine;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ClientSelectorView extends JPanel implements ActionListener {

	private final LauncherController controller;
	private final JButton jailbreak, refresh, startOsbot;
	private final JList<String> jvms;
	private final DefaultListModel<String> jvmsModel;
	private final JLabel status;
	private ClassArchive classArchive = null;
	private ReflectionEngine reflectionEngine = null;
	private JButton button = null;
	private JLabel botstatus, version = null;

	public ClientSelectorView(LauncherController controller) {
		this.controller = controller;
		this.setLayout(new BorderLayout());
		this.setBorder(new TitledBorder(new LineBorder(Color.BLACK, 2), "Launcher"));

		this.status = new JLabel("<html>Status: <font color='green'>Ready</font></html>");
		add(status, BorderLayout.NORTH);

		this.jvmsModel = new DefaultListModel<>();
		this.jvms = new JList<>(jvmsModel);
		this.jvms.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.jvms.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(final ListSelectionEvent e) {
				if (!jvms.isSelectionEmpty()) {
					jailbreak.setEnabled(true);
				} else {
					jailbreak.setEnabled(false);
				}
			}
		});
		refreshList();
		add(jvms, BorderLayout.CENTER);

		Box buttonBox = Box.createHorizontalBox();
		this.startOsbot = new JButton("Start OSBot");
		this.startOsbot.setActionCommand("start osbot");
		this.startOsbot.addActionListener(this::actionPerformed);
		buttonBox.add(startOsbot);
		this.jailbreak = new JButton("Jailbreak");
		this.jailbreak.addActionListener(this);
		this.jailbreak.setEnabled(false);
		this.jailbreak.setActionCommand("jailbreak");
		buttonBox.add(jailbreak);
		this.refresh = new JButton("Refresh");
		this.refresh.setActionCommand("refresh");
		this.refresh.addActionListener(this::actionPerformed);
		buttonBox.add(refresh);
		add(buttonBox, BorderLayout.SOUTH);

		setPreferredSize(new Dimension(350, 200));
	}

	private void refreshList() {
		this.jvmsModel.clear();
		HashMap<String, VirtualMachineDescriptor> jvms = controller.getJVMs();
		if (jvms.isEmpty()) {
			this.jvmsModel.addElement("No OSBot Clients found.");
			this.jvms.setEnabled(false);
			return;
		}
		for (Map.Entry<String, VirtualMachineDescriptor> entry : jvms.entrySet()) {
			this.jvmsModel.addElement(entry.getKey());
		}
		this.jvms.setEnabled(true);
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		switch (e.getActionCommand()) {
			case "refresh":
				refreshList();
				break;
			case "jailbreak":
				controller.jailbreak(jvms.getSelectedValuesList());
				break;
			case "start osbot":
				if (classArchive == null) {
					classArchive = new ClassArchive();
					classArchive.addJar(new File(Constants.DIRECTORY_PATH + File.separator + "environment.jar"));
				}
				if (reflectionEngine == null) {
					try {
						reflectionEngine = new ReflectionEngine(classArchive);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				ReflectedClass clazz = reflectionEngine.getClass("org.osbot.Boot");
				if (clazz != null) {
					String[] params = null;
					reflectionEngine.getMethodHookValue("org.osbot.Boot", "main", 1, (Object) params);
					button = (JButton) reflectionEngine.getFieldValue("org.osbot.sB", "IiIIIiiiiiII", null);
					button.setText("Launch");
					reflectionEngine.setFieldValue("org.osbot.sB", "IiIIIiiiiiII", button);
					version = (JLabel) reflectionEngine.getFieldValue("org.osbot.sB", "iiiiiiiiIiiI", null);
					version.setText("Current Version: 0.0.0.0");
					reflectionEngine.setFieldValue("org.osbot.sB", "iiiiiiiiIiiI", version);
					botstatus = (JLabel) reflectionEngine.getFieldValue("org.osbot.sB", "iiiiiiiIIiII", null);
					botstatus.setForeground(Color.red);
					botstatus.setText("Fuck Alek & Rest of OSBot Staff. (But Mostly Alek)");
					reflectionEngine.setFieldValue("org.osbot.sB", "iiiiiiiIIiII", status);
				} else {
					System.out.println("Your enviroment.jar seems to be missing.");
				}

				break;
		}

	}
}
