package org.osbot.jailbreak.ui;

import com.sun.tools.attach.VirtualMachineDescriptor;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientSelectorView extends JPanel implements ActionListener {

	private final LauncherController controller;
	private final JButton jailbreak, refresh;
	private final JList<VirtualMachineDescriptor> jvms;
	private final DefaultListModel<VirtualMachineDescriptor> jvmsModel;
	private final JLabel status;

	public ClientSelectorView(LauncherController controller) {
		this.controller = controller;
		this.setLayout(new BorderLayout());
		this.setBorder(new TitledBorder(new LineBorder(Color.BLACK, 2), "Launcher"));

		this.status = new JLabel("<html>Status: <font color='green'>Ready</font></html>");
		add(status, BorderLayout.NORTH);

		this.jvmsModel = new DefaultListModel<>();
		refreshList();
		this.jvms = new JList<>(jvmsModel);
		this.jvms.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.jvms.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(final ListSelectionEvent e) {
				jailbreak.setEnabled(true);
			}
		});
		add(jvms, BorderLayout.CENTER);

		Box buttonBox = Box.createHorizontalBox();
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

		setPreferredSize(new Dimension(300, 200));
	}

	private void refreshList() {
		this.jvmsModel.clear();
		for (VirtualMachineDescriptor jvm : controller.getJVMs()) {
			this.jvmsModel.addElement(jvm);
		}
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		switch (e.getActionCommand()) {
			case "refresh":
				refreshList();
				break;
			case "jailbreak":
				controller.jailbreak(jvms.getSelectedValue().id());
				break;

		}

	}
}
