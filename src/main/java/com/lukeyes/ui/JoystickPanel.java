package com.lukeyes.ui;

import com.lukeyes.XBoxInput;
import com.lukeyes.motorcontrol.MotorController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JoystickPanel extends JPanel implements ActionListener {

    XBoxInput xBoxController;
    JButton connectButton;
    JLabel statusLabel;
    JoystickThread mJoystickThread;

    private static final int DRIVE_WALK_SPEED = 50;
    private static final int DRIVE_RUN_SPEED = 127;


    public JoystickPanel() {
        this.setLayout(new GridLayout(2,1));
        this.add(new JLabel("XBox Controller"));

        JPanel controlPanel = new JPanel();
        statusLabel = new JLabel("Disconnected");
        statusLabel.setForeground(Color.RED);
        controlPanel.add(statusLabel);

        connectButton = new JButton("Connect");
        connectButton.addActionListener(this);

        controlPanel.add(connectButton);

        this.add(controlPanel);

        xBoxController = new XBoxInput();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Connect")) {
            System.out.println("Connect to XBox here");
            boolean result = xBoxController.setup();
            //boolean result = true;
            if(result) {
                connectButton.setEnabled(false);
                statusLabel.setText("Connected");
                statusLabel.setForeground(Color.BLACK);
                mJoystickThread =
                        new JoystickThread();
                Thread t = new Thread(mJoystickThread);
                t.start();
            }
        }
    }

    public class JoystickThread implements Runnable
    {
        JoystickThread( )
        {

        }

        void updateRemote()
        {
            boolean hasInput = false;

            float yAxis = xBoxController.getYAxis();
            yAxis = (Math.abs(yAxis) > 0.5) ? yAxis : 0;

            float yRotation = xBoxController.getYRotation();
            yRotation = (Math.abs(yRotation) > 0.5) ? yRotation : 0;

            //TODO - change max speed based on key press on controller
            boolean isRunButtonDown = false;
            int maxSpeed = isRunButtonDown ? DRIVE_RUN_SPEED : DRIVE_WALK_SPEED;

            byte leftByte = (byte) (yAxis * maxSpeed);
            byte rightByte = (byte) (yRotation * maxSpeed);

            if(Math.abs(leftByte) > 0 || Math.abs(rightByte) > 0)
                System.out.println(
                        String.format("(L,R) = (%d,%d)",
                        leftByte,
                        rightByte));

            MotorController.getInstance().send(leftByte,rightByte);

            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run()
        {
            while( true )
            {
                if(xBoxController == null)
                {
                    continue;
                }

                if (!xBoxController.poll())
                {
                    continue;
                }

                updateRemote();

                Thread.yield();
            }
        }
    }
}
