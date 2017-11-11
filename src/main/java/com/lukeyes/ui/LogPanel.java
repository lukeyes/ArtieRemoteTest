package com.lukeyes.ui;

import javax.swing.*;
import java.awt.*;

public class LogPanel extends JPanel {
    public LogPanel() {
        this.setLayout(new GridLayout(2,1));
        this.add(new JLabel("Log"));

        JScrollPane scrollPane = new JScrollPane();
        this.add(scrollPane);

    }
}
