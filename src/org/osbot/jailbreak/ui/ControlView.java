package org.osbot.jailbreak.ui;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControlView extends JPanel implements ActionListener {

    private final LauncherController controller;
    private final JButton jailbreak;
    private final JLabel status;

    public ControlView(LauncherController controller) {
        this.controller = controller;
        this.setLayout(new BorderLayout());
        this.setBorder(new TitledBorder(new LineBorder(Color.BLACK, 2), "Launcher"));

        this.status = new JLabel("<html>Status: <font color='green'>Ready</font></html>");
        add(status, BorderLayout.CENTER);

        this.jailbreak = new JButton("Jailbreak");
        this.jailbreak.addActionListener(this);
        this.jailbreak.setActionCommand("jailbreak");
        add(jailbreak, BorderLayout.SOUTH);

        setPreferredSize(new Dimension(300, 90));
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        controller.jailbreak();
    }
}
