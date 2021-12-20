package com.view.server;

import com.controller.PlayerAction;
import com.model.card.CardName;
import com.model.card.RumourCard;
import com.model.game.LocalRound;
import com.model.game.Round;
import com.view.ActiveView;
import com.view.PassiveView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

public class ClientSideView implements ActiveView {
	private Socket socket;

	private final PassiveView passiveView;

	public ClientSideView(PassiveView passiveView) {
		this.passiveView = passiveView;
		connectToAvailableHost();
	}

	public static List<InetAddress> searchForReachableLocalAddresses(int timeout) throws IOException {
		byte[] ip = InetAddress.getLocalHost().getAddress();

		return IntStream.range(1, 255)
				.parallel()
				.mapToObj(i -> {
					ip[3] = (byte) i;
					try {
						InetAddress address = InetAddress.getByAddress(ip);
						if (address.isReachable(timeout)) {
							System.out.println(address + " can be reached !");
							return address;
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					return null;
				})
				.filter(Objects::nonNull)
				.toList();
	}

	public Socket parallelSearchForOpenPort(InetAddress address) {
		System.out.println("Trying to connect to " + address);
		for (int i = 49152; i < 65535; i++) {
			try {
				Socket socket = new Socket(address, i);
				System.out.println("port " + i + " can be reached !");
				return socket;
			} catch (IOException ignored) {
				System.err.println("port " + i + " cannot be reached...");
			}
		}
		return null;
	}

	private void connectToAvailableHost() {
		if (socket == null) {
			//Search for reachable hosts
			try {
				var inetAddresses = searchForReachableLocalAddresses(30);

				//Connect to the first available host
				for (InetAddress address : inetAddresses) {
					socket = parallelSearchForOpenPort(address);
					if (socket != null) {
						System.out.println("Found " + socket);
						break;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	///////////////////////////////////////////////////////////////////////////
	// Active methods
	///////////////////////////////////////////////////////////////////////////

	@Override
	public String promptForPlayerName(int playerIndex) {
		passiveView.waitForPlayerName(playerIndex);
		return null;
	}

	@Override
	public String promptForNewGame() {
		passiveView.waitForNewGame();
		return null;
	}

	@Override
	public int promptForPlayerChoice(List<String> playerNames) {
		passiveView.waitForPlayerChoice(playerNames);
		return 0;
	}

	@Override
	public int promptForCardChoice(List<RumourCard> rumourCards) {
		passiveView.waitForCardChoice(rumourCards);
		return 0;
	}

	@Override
	public int promptForCardChoice(int listSize) {
		passiveView.waitForCardChoice(null);
		return 0;
	}

	@Override
	public int[] promptForRepartition() {
		passiveView.waitForRepartition();
		return null;
	}

	@Override
	public int promptForPlayerIdentity(String name) {
		passiveView.waitForPlayerIdentity(name);
		return 0;
	}

	@Override
	public PlayerAction promptForAction(String playerName, List<PlayerAction> possibleActions) {
		passiveView.waitForAction(playerName, possibleActions);
		return null;
	}

	@Override
	public void promptForPlayerSwitch(String name) {
		passiveView.waitForPlayerSwitch(name);
	}

	///////////////////////////////////////////////////////////////////////////
	// View methods
	///////////////////////////////////////////////////////////////////////////

	@Override
	public void showGameWinner(String name, int numberOfRound) {
		passiveView.showGameWinner(name, numberOfRound);
	}

	@Override
	public void showRoundWinner(String name) {
		passiveView.showRoundWinner(name);
	}

	@Override
	public void showStartOfRound(int numberOfRound) {
		passiveView.showStartOfRound(numberOfRound);
	}

	@Override
	public void showPlayerIdentity(String name, boolean witch) {
		passiveView.showPlayerIdentity(name, witch);
	}

	@Override
	public void showPlayerAction(String name) {
		passiveView.showPlayerAction(name);
	}

	@Override
	public void showPlayerAction(String name, String targetedPlayerName) {
		passiveView.showPlayerAction(name, targetedPlayerName);
	}

	@Override
	public void showPlayerAction(String name, CardName chosenCardName) {
		passiveView.showPlayerAction(name, chosenCardName);
	}

	@Override
	public void showCardList(String name, List<String> cards) {
		passiveView.showCardList(name, cards);
	}

	///////////////////////////////////////////////////////////////////////////
	// Runnable method
	///////////////////////////////////////////////////////////////////////////

	@SuppressWarnings("unchecked")
	private void chooseAppropriateAction(ExchangeContainer c) {
		switch (c.state) {
			case ACTION_REQUEST -> promptForAction(c.name, (List<PlayerAction>) c.list);
			case SHOW_CARD_LIST -> showCardList(c.name, (List<String>) c.list);
			case NEW_GAME_REQUEST -> promptForNewGame();
			case SHOW_GAME_WINNER -> showGameWinner(c.name, c.nb);
			case SHOW_ROUND_WINNER -> showRoundWinner(c.name);
			case SHOW_ACCUSE_ACTION -> showPlayerAction(c.name, c.name2);
			case SHOW_REVEAL_ACTION -> showPlayerAction(c.name);
			case CARD_CHOICE_REQUEST -> promptForCardChoice((List<RumourCard>) c.list);
			case REPARTITION_REQUEST -> promptForRepartition();
			case PLAYER_NAME_REQUEST -> promptForPlayerName(c.nb);
			case SHOW_START_OF_ROUND -> showStartOfRound(c.nb);
			case SHOW_CARD_USE_ACTION -> showPlayerAction(c.name, c.cardName);
			case SHOW_PLAYER_IDENTITY -> showPlayerIdentity(c.name, c.isWitch);
			case PLAYER_CHOICE_REQUEST -> promptForPlayerChoice((List<String>) c.list);
			case PLAYER_SWITCH_REQUEST -> promptForPlayerSwitch(c.name);
			case PLAYER_IDENTITY_REQUEST -> promptForPlayerIdentity(c.name);
			case BLANK_CARD_CHOICE_REQUEST -> promptForCardChoice(c.nb);
		}
	}

	public void run() {
		if (socket != null) {
			try {
				var serverOutput = new ObjectInputStream(socket.getInputStream());

				while (this.socket != null) {
					try {
						Object object = serverOutput.readObject();

						if (object instanceof Round round) {
							LocalRound.setInstance(round);
						} else if (object instanceof ExchangeContainer container) {
							chooseAppropriateAction(container);
						} else {
							System.err.println("Received non standard transmission : " + object);
						}
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
			} catch (SocketException e) {
				System.err.println("The server was closed, shutting down...");
				System.exit(0);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
