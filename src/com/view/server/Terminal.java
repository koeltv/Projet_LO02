package com.view.server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * The type Terminal.
 */
record Terminal(Socket socket, ObjectOutputStream output, ObjectInputStream input) {
}