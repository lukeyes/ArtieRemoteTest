# ArtieRemoteTest

This project is a test for running Artie's motor control through a Linux PC.  The PC serves as a passthrough for the XBox Wireless Joystick and an Arduino Mega ADK that itself communicates with the Sabretooth 2x32 motor controller.

Moving the marshalling logic off of the ADK and onto a computer will allow the buttons on the controller to handle other functions for Artie, such as switching between different move states (head pan/tilt vs drive), allowing emotion input (changing eye colors), and different speech outputs (mapping different outputs to the face buttons on the controller).

The project relies on two major pieces of open source software.

JInput - https://github.com/jinput/jinput
Java-Arduino communication library - https://github.com/HirdayGupta/Java-Arduino-Communication-Library
