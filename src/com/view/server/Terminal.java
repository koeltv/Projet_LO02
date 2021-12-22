package com.view.server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public record Terminal(Socket socket, ObjectOutputStream output, ObjectInputStream input) {
}