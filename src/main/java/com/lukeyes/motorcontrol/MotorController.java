package com.lukeyes.motorcontrol;

import com.fazecast.jSerialComm.SerialPort;
import com.lukeyes.ui.MotorControllerPanel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MotorController {

    private static MotorController instance = null;

    private MotorControllerListener listener;
    private List<SerialPort> ports;
   // private Arduino arduino;
    private SerialPort port;
    private SerialPort arduinoPort;
    private List<String> portDescriptions;

    public static MotorController getInstance() {
        if(instance == null) {
            instance = new MotorController();
        }
        return instance;
    }



    public MotorController() {
        arduinoPort = null;
        portDescriptions = new ArrayList<>();
    }

    public void setListener(MotorControllerPanel controllerPanel) {
        this.listener = controllerPanel;

        searchPorts();

        this.listener.onPortsFound(portDescriptions);
    }

    public void autoSearch() {
        searchPorts();
        if(this.listener != null) {
            this.listener.onPortsFound(portDescriptions);
        }
    }

    public boolean getHasArduino() {
        return (this.arduinoPort != null);
    }

    private void searchPorts() {

        SerialPort[] portArray = SerialPort.getCommPorts();
        ports = Arrays.stream(portArray).collect(Collectors.toList());

        portDescriptions = ports.stream()
                .map(SerialPort::getDescriptivePortName)
                .collect(Collectors.toList());

        // search for Arduino
        SerialPort foundArduino = null;
        for(SerialPort port : ports) {
            if(port.getDescriptivePortName().contains("Arduino")) {
                foundArduino = port;
                break;
            }
        }
        this.arduinoPort = foundArduino;

    }


    public void connect(String portDescription) {

        for(SerialPort port : ports) {
            if(port.getDescriptivePortName().equals(portDescription)) {//
                connectToPort(port);
                return;
            }
        }

        listener.onConnectFailed();

    }

    public void connect() {

        if(!getHasArduino()) {
            return;
        }

        connectToPort(arduinoPort);
    }

    private void connectToPort(SerialPort port) {

        Thread t = new Thread(() ->{
            port.openPort();
            this.port = port;
            // need a delay between opening the connection and starting to send data
            try {
                Thread.sleep(1400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            listener.onConnected();
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
