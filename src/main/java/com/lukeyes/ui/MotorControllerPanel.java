package com.lukeyes.ui;

import com.lukeyes.motorcontrol.MotorController;
import com.lukeyes.motorcontrol.MotorControllerListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MotorControllerPanel extends JPanel implements ActionListener, MotorControllerListener {


    private JButton connectButton;
    private JLabel statusLabel;

    private ButtonModel connectModel;
    private ButtonModel disconnectModel;
    private ComboBoxModel<String> comboBoxModel;
    private JComboBox<String> comboBox;


    MotorControllerPanel() {
        this.setLayout(new GridLayout(2,1));

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(1,2));
        JLabel label = new JLabel("Motor Controller");

        statusLabel = new JLabel("Disconnected");

        statusLabel.setForeground(Color.RED);

        infoPanel.add(label);
        infoPanel.add(statusLabel);

        this.add(infoPanel);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(1,3));
        comboBox = new JComboBox<>();
        comboBoxModel = new DefaultComboBoxModel<>();
        comboBox.setModel(comboBoxModel);

        controlPanel.add(comboBox);

        this.connectButton = new JButton("Connect");
        connectButton.addActionListener(this);
        connectButton.setEnabled(false);

        controlPanel.add(connectButton);
        this.add(controlPanel);

        MotorController.getInstance().setListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
       System.out.println(e.getActionCommand());
       if(e.getActionCommand().equals("Connect")) {
           System.out.println("Connect to Arduino here");
           connectButton.setEnabled(false);
           comboBox.setEnabled(false);
           statusLabel.setForeground(Color.BLACK);
           statusLabel.setText("Connecting");
           statusLabel.updateUI();
           MotorController.getInstance().connect((String) comboBoxModel.getSelectedItem());
       } else if(e.getActionCommand().equals("Disconnect")) {
           System.out.println("Disconnect from Arduino here");
           connectButton.setEnabled(false);
           statusLabel.setText("Disconnecting");
           comboBox.setEnabled(true);
          MotorController.getInstance().disconnect();
       }
    }

    public void onConnected() {
        statusLabel.setText("Connected");
        connectButton.setText("Disconnect");
        connectButton.setEnabled(true);
        connectButton.setActionCommand("Disconnect");
    }

    @Override
    public void onConnectFailed() {
        statusLabel.setText("Connection failed");
        statusLabel.setForeground(Color.red);
        connectButton.setEnabled(true);
        connectButton.setActionCommand("Connect");
        comboBox.setEnabled(true);
    }

    public void onDisconnected() {
        statusLabel.setText("Disconnected");
        statusLabel.setForeground(Color.red);
        connectButton.setText("Connect");
        connectButton.setEnabled(true);
        connectButton.setActionCommand("Connect");
    }

    @Override
    public void onPortsFound(List<String> portNames) {
        for(String portName: portNames) {
            comboBox.addItem(portName);
        }
        connectButton.setEnabled(comboBox.getSelectedItem() != null);
    }
}
