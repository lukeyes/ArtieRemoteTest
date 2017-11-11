package com.lukeyes.ui;

import javax.swing.*;
import java.awt.*;

public class ArtieFrame extends JFrame {

    public static ArtieFrame createAndShowGUI() {
        ArtieFrame frame = new ArtieFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.setState(Frame.NORMAL);
        frame.setVisible(true);
        return frame;
    }


    public ArtieFrame() {
        this.setSize(400,400);
        this.setLayout(new GridLayout(3,1));
        MotorControllerPanel motorControllerPanel = new MotorControllerPanel();
        this.add(motorControllerPanel);

        JoystickPanel joystickPanel = new JoystickPanel();
        this.add(joystickPanel);

        this.add(new LogPanel());
    }
}
