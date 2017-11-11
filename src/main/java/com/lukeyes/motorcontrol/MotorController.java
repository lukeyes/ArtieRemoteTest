package com.lukeyes.motorcontrol;

import arduino.*;
import com.fazecast.jSerialComm.SerialPort;
import com.lukeyes.ui.MotorControllerPanel;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MotorController {

    private static MotorController instance = null;

    private MotorControllerListener listener;
    private List<SerialPort> ports;
   // private Arduino arduino;
    private SerialPort port;

    public static MotorController getInstance() {
        if(instance == null) {
            instance = new MotorController();
        }
        return instance;
    }



    public MotorController() {

    }

    public void setListener(MotorControllerPanel controllerPanel) {
        this.listener = controllerPanel;

        SerialPort[] portArray = SerialPort.getCommPorts();
        ports = Arrays.stream(portArray).collect(Collectors.toList());

        List<String> portDescriptions = ports.stream().map(SerialPort::getDescriptivePortName).collect(Collectors.toList());

        portDescriptions.add("Test");

        this.listener.onPortsFound(portDescriptions);
    }


    public void connect(String portDescription) {

        Thread t = new Thread(() -> {
            for(SerialPort port : ports) {
                if(port.getDescriptivePortName().equals(portDescription)) {
                    // this is my port
                   // arduino = new Arduino(port.getSystemPortName(), port.getBaudRate());
//
                    port.openPort();
                    this.port = port;
                    // need a delay between opening the connection and starting to send data
                    try {
                        Thread.sleep(1400);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    listener.onConnected();
                    return;
                }
            }

            listener.onConnectFailed();
        });
        t.start();

    }

    public void disconnect() {
        Thread t = new Thread( () -> {
            port.closePort();
            listener.onDisconnected();
        });
        t.start();

    }

    public void send(byte left, byte right) {

        /*
        if(arduino != null) {
            arduino.serialWrite(Byte.toString(left));
            arduino.serialWrite(Byte.toString(right));
        }*/
        if(port != null) {
            byte buffer[] = new byte[2];
            buffer[0] = left;
            buffer[1] = right;
            port.writeBytes(buffer,2);

        }
            }
}
