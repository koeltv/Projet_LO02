package com.view.server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.Callable;

/**
 * The record Callable terminal.
 * <p>
 * Represent a connection to a terminal by its IP and a port.
 * When the call method is used, it will try to reach the port of the terminal and return it, or stop if it fails or timeout.
 */
public record CallableTerminal(InetAddress address, int port) implements Callable<Terminal> {
	/**
	 * The constant TIMEOUT.
	 */
	private static final int TIMEOUT = 150;

	@Override
	public Terminal call() throws Exception {
		System.out.println("Trying to connect to " + address + " on port " + port + " !");
		try (Socket socket = new Socket()) {
			socket.connect(new InetSocketAddress(address, port), TIMEOUT);
			System.out.println("Could connect to " + address + " on port " + port + " !");
		}
		Socket socket = new Socket(address, port);
		return new Terminal(socket, new ObjectOutputStream(socket.getOutputStream()), new ObjectInputStream(socket.getInputStream()));
	}
}
