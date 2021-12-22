package com.view.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.Callable;

public record CallableServer(InetAddress inetAddress) implements Callable<Terminal> {
	private static Terminal server;

	private void waitForConfirmation() {
		try {
			if (server.input().readObject().equals("WitchHunt")) {
				System.out.println("Confirmation received");
			}
		} catch (IOException | ClassNotFoundException e) {
			try {
				server.socket().close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			server = null;
		}
		synchronized (this) {
			notify();
		}
	}

	public void searchForOpenPort(InetAddress address) {
		for (int i = 49152; i <= 49160; i++) {
			try {
				Socket socket = new Socket(address, i);
				System.out.println("port " + i + " can be reached !");
				server = new Terminal(
						socket,
						new ObjectOutputStream(socket.getOutputStream()),
						new ObjectInputStream(socket.getInputStream())
				);

				//Try to see if the server answer
				Thread confirmation = new Thread(this::waitForConfirmation);
				confirmation.start();

				try {
					synchronized (this) {
						wait(300);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				if (server != null) break;
			} catch (IOException ignored) {
				System.err.println("port " + i + " cannot be reached...");
			}
		}
	}

	@Override
	public Terminal call() throws Exception {
		if (inetAddress.isReachable(30)) {
			System.out.println(inetAddress + " can be reached !");
			searchForOpenPort(inetAddress);
			if (server != null) return server;
		}
		throw new Exception("Unreachable server port");
	}
}
