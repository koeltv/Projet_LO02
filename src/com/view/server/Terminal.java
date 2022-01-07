package com.view.server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * The type Terminal.
 *
 * Used to represent a terminal by his connection (the socket) and its input and output
 *
 * @see ServerSideView
 * @see ClientSideView
 */
record Terminal(Socket socket, ObjectOutputStream output, ObjectInputStream input) {
}