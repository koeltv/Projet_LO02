package com.view.server;

import com.controller.PlayerAction;
import com.model.card.CardName;
import com.model.card.RumourCard;
import com.model.game.Round;
import com.model.player.AI;
import com.model.player.Player;
import com.view.ActiveView;
import com.view.PassiveView;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("ConstantConditions")
public class ServerSideView extends Frame implements ActiveView, Runnable {
	private static final int MAX_CAPACITY = 5;

	private final List<Terminal> clients = new ArrayList<>();

	private final HashMap<Terminal, List<Player>> localPlayers = new HashMap<>();

	private final ActiveView activeView;

	public ServerSideView(ActiveView activeView) {
		this.activeView = activeView;

		this.setModalExclusionType(Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
		Thread thread = new Thread(this);
		thread.start();
	}

	/**
	 * Search for available port between 49152 and 65535 to create server connection.
	 *
	 * @return opened server connection
	 */
	private ServerSocket createServerConnection() {
		for (int i = 49152; i <= 49160; i++) {
			try {
				return new ServerSocket(i);
			} catch (IOException e) {
				System.err.println("Port n°" + i + " isn't available");
			}
		}
		return null;
	}

	public void updateClient(Terminal terminal, Object object) {
		try {

			var stream = clients.stream()
					.filter(terminal1 -> terminal1 == terminal)
					.findFirst().orElseThrow()
					.output();
			stream.writeUnshared(object);
			stream.flush();
			stream.reset();
		} catch (SocketException e) {
			clients.remove(terminal);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void updateClients(Object object) {
		List<Terminal> toRemove = new ArrayList<>();

		clients.forEach(terminal -> {
			try {
				terminal.output().writeUnshared(object);
				terminal.output().flush();
				terminal.output().reset();
			} catch (SocketException e) {
				toRemove.add(terminal);
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
				System.out.println("Server is opened: " + serverSocket);
				do {
					Socket clientSocket = serverSocket.accept();
					System.out.println("New client : " + clientSocket);
					initiateClientConnection(clientSocket);
				} while (clients.size() <= MAX_CAPACITY);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initiateClientConnection(Socket clientSocket) throws IOException {
		Terminal terminal = null;
		try {
			terminal = new Terminal(
					clientSocket,
					new ObjectOutputStream(clientSocket.getOutputStream()),
					new ObjectInputStream(clientSocket.getInputStream())
			);
			clients.add(terminal);
		} catch (IOException e) {
			System.err.println("A connection was abandonned by the client");
		}

		//Player assignation : choose if the client is a spectator (default if only 1 player) or represents 1 or more player
		if (Round.getInstance() != null) {
			List<Player> availablePlayers = Round.getInstance().getSelectablePlayers(null).stream()
					.filter(player -> !(player instanceof AI))
					.filter(player -> localPlayers.keySet().stream().noneMatch(socket -> localPlayers.get(socket).contains(player)))
					.toList();
			if (availablePlayers.size() > 1) {
				List<Player> players = select(this, "Choose player(s) to hand over to new client", availablePlayers);
				if (players.size() > 0) {
					localPlayers.put(terminal, new ArrayList<>(players.size()));
					for (Player player : players) localPlayers.get(terminal).add(player);
				}
			}
		}
	}

	/**
	 * @param parent  parent of this pane
	 * @param message message to display
	 * @param options proposed options
	 * @param <T>     type of the options
	 * @return chosen options
	 * @see <a href="https://stackoverflow.com/questions/8899605/multiple-choices-from-a-joptionpane">source</a>
	 */
	public static <T> List<T> select(Component parent, String message, List<T> options) {
		List<AbstractMap.SimpleEntry<JCheckBox, T>> boxes = options.stream()
				.map(variant -> new AbstractMap.SimpleEntry<>(new JCheckBox(String.valueOf(variant)), variant))
				.toList();

		JPanel panel = new JPanel(new GridBagLayout());

		boxes.forEach(p -> panel.add(p.getKey(),
				new GridBagConstraints(0, boxes.indexOf(p), 1, 1, 1.0, 1.0,
						GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
						new Insets(0, 0, 0, 0), 0, 0
				)
		));

		JOptionPane.showMessageDialog(parent, panel, message, JOptionPane.PLAIN_MESSAGE);

		return boxes.stream()
				.filter(p -> p.getKey().isSelected())
				.map(AbstractMap.SimpleEntry::getValue)
				.toList();
	}

	///////////////////////////////////////////////////////////////////////////
	// Active methods
	///////////////////////////////////////////////////////////////////////////

	private void sendRoundState() {
		Round round = Round.getInstance();
		if (round != null) updateClients(round);
	}

	private Object getInputFromClient(Player player, ExchangeContainer exchangeContainer) {
		clients.forEach(terminal -> updateClient(terminal,
				localPlayers.containsKey(terminal) && localPlayers.get(terminal).contains(player) ?
						exchangeContainer.setActive() :
						exchangeContainer
		));
		try {
			return clients.stream()
					.filter(terminal -> localPlayers.get(terminal).contains(player))
					.findFirst().orElseThrow()
					.input().readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String promptForPlayerName(int playerIndex) {
		sendRoundState();
		updateClients(new ExchangeContainer(ChangedState.PLAYER_NAME_WAIT, playerIndex));
		return activeView.promptForPlayerName(playerIndex);
	}

	@Override
	public String promptForNewGame() {
		sendRoundState();
		updateClients(new ExchangeContainer(ChangedState.NEW_GAME_WAIT));
		return activeView.promptForNewGame();
	}

	@Override
	public int[] promptForRepartition() {
		sendRoundState();
		updateClients(new ExchangeContainer(ChangedState.REPARTITION_WAIT));
		return activeView.promptForRepartition();
	}

	// Player specific

	@Override
	public int promptForPlayerChoice(String playerName, List<String> playerNames) {
		sendRoundState();

		Player player = Round.getInstance().getPlayerByName(playerName);
		ExchangeContainer exchangeContainer = new ExchangeContainer(ChangedState.PLAYER_CHOICE_WAIT, playerName, playerNames);
		if (localPlayers.keySet().stream().anyMatch(socket -> localPlayers.get(socket).contains(player))) {
			((PassiveView) activeView).waitForPlayerChoice(playerNames);
			return (int) getInputFromClient(player, exchangeContainer);
		} else {
			updateClients(exchangeContainer);
			return activeView.promptForPlayerChoice(playerName, playerNames);
		}
	}

	@Override
	public int promptForCardChoice(String playerName, List<RumourCard> rumourCards) {
		sendRoundState();

		Player player = Round.getInstance().getPlayerByName(playerName);
		ExchangeContainer exchangeContainer = new ExchangeContainer(ChangedState.CARD_CHOICE_WAIT, playerName, rumourCards);
		if (localPlayers.keySet().stream().anyMatch(socket -> localPlayers.get(socket).contains(player))) {
			((PassiveView) activeView).waitForCardChoice(rumourCards);
			return (int) getInputFromClient(player, exchangeContainer);
		} else {
			updateClients(exchangeContainer);
			return activeView.promptForCardChoice(playerName, rumourCards);
		}
	}

	@Override
	public int promptForCardChoice(String playerName, int listSize) {
		sendRoundState();

		Player player = Round.getInstance().getPlayerByName(playerName);
		ExchangeContainer exchangeContainer = new ExchangeContainer(ChangedState.CARD_CHOICE_WAIT, playerName, listSize);
		if (localPlayers.keySet().stream().anyMatch(socket -> localPlayers.get(socket).contains(player))) {
			((PassiveView) activeView).waitForCardChoice(null);
			return (int) getInputFromClient(player, exchangeContainer);
		} else {
			updateClients(exchangeContainer);
			return activeView.promptForCardChoice(playerName, listSize);
		}
	}

	@Override
	public int promptForPlayerIdentity(String name) {
		sendRoundState();

		Player player = Round.getInstance().getPlayerByName(name);
		ExchangeContainer exchangeContainer = new ExchangeContainer(ChangedState.PLAYER_IDENTITY_WAIT, name);
		if (localPlayers.keySet().stream().anyMatch(socket -> localPlayers.get(socket).contains(player))) {
			((PassiveView) activeView).waitForPlayerIdentity(name);
			return (int) getInputFromClient(player, exchangeContainer);
		} else {
			updateClients(exchangeContainer);
			return activeView.promptForPlayerIdentity(name);
		}
	}

	@Override
	public PlayerAction promptForAction(String playerName, List<PlayerAction> possibleActions) {
		sendRoundState();

		Player player = Round.getInstance().getPlayerByName(playerName);
		ExchangeContainer exchangeContainer = new ExchangeContainer(ChangedState.ACTION_WAIT, playerName, possibleActions);
		if (localPlayers.keySet().stream().anyMatch(socket -> localPlayers.get(socket).contains(player))) {
			((PassiveView) activeView).waitForAction(playerName, possibleActions);
			return (PlayerAction) getInputFromClient(player, exchangeContainer);
		} else {
			updateClients(exchangeContainer);
			return activeView.promptForAction(playerName, possibleActions);
		}
	}

	@Override
	public void promptForPlayerSwitch(String name) {
		sendRoundState();

		Player player = Round.getInstance().getPlayerByName(name);
		ExchangeContainer exchangeContainer = new ExchangeContainer(ChangedState.PLAYER_SWITCH_WAIT, name);
		if (localPlayers.keySet().stream().anyMatch(socket -> localPlayers.get(socket).contains(player))) {
			((PassiveView) activeView).waitForPlayerSwitch(name);
			getInputFromClient(player, exchangeContainer);
		} else {
			updateClients(exchangeContainer);
			activeView.promptForPlayerSwitch(name);
		}
	}

	///////////////////////////////////////////////////////////////////////////
	// View methods
	///////////////////////////////////////////////////////////////////////////

	@Override
	public void showGameWinner(String name, int numberOfRound) {
		sendRoundState();
		updateClients(new ExchangeContainer(ChangedState.SHOW_GAME_WINNER, name, numberOfRound));
		activeView.showGameWinner(name, numberOfRound);
	}

	@Override
	public void showRoundWinner(String name) {
		sendRoundState();
		updateClients(new ExchangeContainer(ChangedState.SHOW_ROUND_WINNER, name));
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
