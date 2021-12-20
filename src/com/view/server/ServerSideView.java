package com.view.server;

import com.controller.PlayerAction;
import com.model.card.CardName;
import com.model.card.RumourCard;
import com.model.game.Round;
import com.view.ActiveView;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ServerSideView implements ActiveView, Runnable {
	private static final int MAX_CAPACITY = 5;

	private final HashMap<Socket, ObjectOutputStream> clients = new HashMap<>();

	private final ActiveView activeView;

	public ServerSideView(ActiveView activeView) {
		this.activeView = activeView;

		Thread thread = new Thread(this);
		thread.start();
	}

	/**
	 * Search for available port between 49152 and 65535 to create server connection.
	 *
	 * @return opened server connection
	 */
	private ServerSocket createServerConnection() {
		for (int i = 49152; i < 65535; i++) {
			try {
				return new ServerSocket(i);
			} catch (IOException e) {
				System.err.println("Port n°" + i + " isn't available");
			}
		}
		return null;
	}

	public void updateClients(Object object) {
		List<Socket> toRemove = new ArrayList<>();

		clients.forEach((socket, stream) -> {
			try {
				stream.writeUnshared(object);
				stream.flush();
				stream.reset();
			} catch (SocketException e) {
				toRemove.add(socket);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		toRemove.forEach(clients::remove);
	}

	///////////////////////////////////////////////////////////////////////////
	// Runnable method
	///////////////////////////////////////////////////////////////////////////

	@Override
	public void run() {
		try {
			ServerSocket serverSocket = createServerConnection();
			if (serverSocket != null) {
				System.out.println("Server is opened on port : " + serverSocket.getLocalPort());
				do {
					Socket clientSocket = serverSocket.accept();
					System.out.println("New client : " + clientSocket);

					clients.put(clientSocket, new ObjectOutputStream(clientSocket.getOutputStream()));
				} while (clients.size() <= MAX_CAPACITY);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	///////////////////////////////////////////////////////////////////////////
	// Active methods
	///////////////////////////////////////////////////////////////////////////

	private void sendRoundState() {
		Round round = Round.getInstance();
		if (round != null) updateClients(round);
	}

	@Override
	public String promptForPlayerName(int playerIndex) {
		sendRoundState();
		updateClients(new ExchangeContainer(ChangedState.PLAYER_NAME_REQUEST, playerIndex));
		//		updateClients(ChangedState.PLAYER_NAME_REQUEST);
		return activeView.promptForPlayerName(playerIndex);
	}

	@Override
	public String promptForNewGame() {
		sendRoundState();
		updateClients(new ExchangeContainer(ChangedState.NEW_GAME_REQUEST));
		//		updateClients(ChangedState.NEW_GAME_REQUEST);
		return activeView.promptForNewGame();
	}

	@Override
	public int promptForPlayerChoice(List<String> playerNames) {
		sendRoundState();
		updateClients(new ExchangeContainer(ChangedState.PLAYER_CHOICE_REQUEST, playerNames));
		//		updateClients(ChangedState.PLAYER_CHOICE_REQUEST);
		return activeView.promptForPlayerChoice(playerNames);
	}

	@Override
	public int promptForCardChoice(List<RumourCard> rumourCards) {
		sendRoundState();
		updateClients(new ExchangeContainer(ChangedState.CARD_CHOICE_REQUEST, rumourCards));
		//		updateClients(ChangedState.CARD_CHOICE_REQUEST);
		return activeView.promptForCardChoice(rumourCards);
	}

	@Override
	public int promptForCardChoice(int listSize) {
		sendRoundState();
		updateClients(new ExchangeContainer(ChangedState.BLANK_CARD_CHOICE_REQUEST, listSize));
		//		updateClients(ChangedState.BLANK_CARD_CHOICE_REQUEST);
		return activeView.promptForCardChoice(listSize);
	}

	@Override
	public int[] promptForRepartition() {
		sendRoundState();
		updateClients(new ExchangeContainer(ChangedState.REPARTITION_REQUEST));
		//		updateClients(ChangedState.REPARTITION_REQUEST);
		return activeView.promptForRepartition();
	}

	@Override
	public int promptForPlayerIdentity(String name) {
		sendRoundState();
		updateClients(new ExchangeContainer(ChangedState.PLAYER_IDENTITY_REQUEST, name));
		//		updateClients(ChangedState.PLAYER_IDENTITY_REQUEST);
		return activeView.promptForPlayerIdentity(name);
	}

	@Override
	public PlayerAction promptForAction(String playerName, List<PlayerAction> possibleActions) {
		sendRoundState();
		updateClients(new ExchangeContainer(ChangedState.ACTION_REQUEST, playerName, possibleActions));
		//		updateClients(ChangedState.ACTION_REQUEST);
		return activeView.promptForAction(playerName, possibleActions);
	}

	@Override
	public void promptForPlayerSwitch(String name) {
		sendRoundState();
		updateClients(new ExchangeContainer(ChangedState.PLAYER_SWITCH_REQUEST, name));
		//		updateClients(ChangedState.PLAYER_SWITCH_REQUEST);
		activeView.promptForPlayerSwitch(name);
	}

	///////////////////////////////////////////////////////////////////////////
	// View methods
	///////////////////////////////////////////////////////////////////////////

	@Override
	public void showGameWinner(String name, int numberOfRound) {
		sendRoundState();
		updateClients(new ExchangeContainer(ChangedState.SHOW_GAME_WINNER, name, numberOfRound));
		//		updateClients(ChangedState.SHOW_GAME_WINNER);
		activeView.showGameWinner(name, numberOfRound);
	}

	@Override
	public void showRoundWinner(String name) {
		sendRoundState();
		updateClients(new ExchangeContainer(ChangedState.SHOW_ROUND_WINNER, name));
		//		updateClients(ChangedState.SHOW_ROUND_WINNER);
		activeView.showRoundWinner(name);
	}

	@Override
	public void showStartOfRound(int numberOfRound) {
		sendRoundState();
		updateClients(new ExchangeContainer(ChangedState.SHOW_START_OF_ROUND, numberOfRound));
		activeView.showStartOfRound(numberOfRound);
	}

	@Override
	public void showPlayerIdentity(String name, boolean witch) {
		sendRoundState();
		updateClients(new ExchangeContainer(ChangedState.SHOW_PLAYER_IDENTITY, name, witch));
		activeView.showPlayerIdentity(name, witch);
	}

	@Override
	public void showPlayerAction(String name) {
		sendRoundState();
		updateClients(new ExchangeContainer(ChangedState.SHOW_REVEAL_ACTION, name));
		activeView.showPlayerAction(name);
	}

	@Override
	public void showPlayerAction(String name, String targetedPlayerName) {
		sendRoundState();
		updateClients(new ExchangeContainer(ChangedState.SHOW_ACCUSE_ACTION, name, targetedPlayerName));
		activeView.showPlayerAction(name, targetedPlayerName);
	}

	@Override
	public void showPlayerAction(String name, CardName chosenCardName) {
		sendRoundState();
		updateClients(new ExchangeContainer(ChangedState.SHOW_CARD_USE_ACTION, name, chosenCardName));
		activeView.showPlayerAction(name, chosenCardName);
	}

	@Override
	public void showCardList(String name, List<String> cards) {
		sendRoundState();
		updateClients(new ExchangeContainer(ChangedState.SHOW_CARD_LIST, name, cards));
		activeView.showCardList(name, cards);
	}
}
