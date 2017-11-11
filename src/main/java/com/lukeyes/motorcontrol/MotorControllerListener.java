package com.lukeyes.motorcontrol;

import java.util.List;

public interface MotorControllerListener {
    void onConnected();
    void onConnectFailed();

    void onDisconnected();

    void onPortsFound(List<String> portNames);
}
