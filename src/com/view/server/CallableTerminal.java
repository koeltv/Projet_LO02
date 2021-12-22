package com.view.server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.Callable;

public record CallableTerminal(InetAddress address, int port) implements Callable<Terminal> {
	private static final int TIMEOUT = 30;

	@Override
	public Terminal call() throws Exception {
		try (Socket socket = new Socket()) {
			socket.connect(new InetSocketAddress(address, port), TIMEOUT);
			System.out.println("Could connect to " + address + " on port " + port + " !");
		}
		Socket socket = new Socket(address, port);
		return new Terminal(socket, new ObjectOutputStream(socket.getOutputStream()), new ObjectInputStream(socket.getInputStream()));
	}
}
