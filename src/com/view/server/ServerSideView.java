package com.view.server;

import com.controller.PlayerAction;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The type Server side view.
 *
 * The server will try to listen on the first given port. If the port isn't available, it will try all the port of the range, shutting down if it reaches the end.
 * Any client can connect until the treshold is reashed.
 * If there is more than 1 not AI player in the game, the host will be able to hand over some players to the client upon connection but never all player.
 * The client that has one or more player assigned to it will be prompted if the corresponding player need to give information.
 */
@SuppressWarnings("ConstantConditions")
public class ServerSideView extends Frame implements ActiveView, Runnable {

	/**
	 * The maximum number of client supported.
	 */
	private static final int MAX_CAPACITY = 5;

	/**
	 * The Clients.
	 */
	private final List<Terminal> clients = new ArrayList<>();

	/**
	 * The players associated to each terminal.
	 */
	private final HashMap<Terminal, List<Player>> localPlayers = new HashMap<>();

	/**
	 * The Active view.
	 */
	private final ActiveView activeView;

	/**
	 * Instantiates a new Server side view.
	 *
	 * @param activeView the active view
	 */
	public ServerSideView(ActiveView activeView) {
		this.activeView = activeView;

		this.setModalExclusionType(Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
		Thread thread = new Thread(this);
		thread.start();
	}

	///////////////////////////////////////////////////////////////////////////
	// Connection methods
	///////////////////////////////////////////////////////////////////////////

	/**
	 * Search for available port between 49152 and 49160 to create server connection.
	 *
	 * @return opened server connection or null if none available
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

	/**
	 * Initiate client connection.
	 *
	 * @param clientSocket the client socket
	 */
	private void initiateClientConnection(Socket clientSocket) {
		try {
			Terminal terminal = new Terminal(
					clientSocket,
					new ObjectOutputStream(clientSocket.getOutputStream()),
					new ObjectInputStream(clientSocket.getInputStream())
			);
			clients.add(terminal);

			//Player assignation : choose if the client is a spectator (default if only 1 player) or represents 1 or more player
			if (Round.getInstance() != null) {
				List<Player> availablePlayers = Round.getInstance().getSelectablePlayers(null).stream()
						.filter(player -> !(player instanceof AI))
						.filter(player -> localPlayers.keySet().stream().noneMatch(socket -> localPlayers.get(socket).contains(player)))
						.toList();
				if (availablePlayers.size() > 1) {
					List<Player> players = select(this, "Choose player(s) to hand over to new client", availablePlayers);
					if (players.size() > 0 && players.size() < availablePlayers.size()) {
						localPlayers.put(terminal, new ArrayList<>(players.size()));
						for (Player player : players) localPlayers.get(terminal).add(player);
					}
				}
			}
		} catch (IOException e) {
			System.err.println("A connection was abandonned by the client");
		}
	}

	///////////////////////////////////////////////////////////////////////////
	// Communication methods
	///////////////////////////////////////////////////////////////////////////

	/**
	 * Update client.
	 *
	 * @param terminal the terminal
	 * @param object   the object to send
	 */
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

	/**
	 * Update clients.
	 *
	 * @param object the object to send
	 */
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

	/**
	 * Select 1 or more option in a list via a Pane.
	 *
	 * @param <T>     type of the options
	 * @param parent  parent of this pane
	 * @param message message to display
	 * @param options proposed options
	 * @return chosen options
	 *
	 * @see <a href="https://stackoverflow.com/questions/8899605/multiple-choices-from-a-joptionpane">source</a>
	 */
	public static <T> List<T> select(Component parent, String message, List<T> options) {
		Map<JCheckBox, T> boxes = options.stream().collect(Collectors.toMap(boxe -> new JCheckBox(String.valueOf(boxe)), boxe -> boxe));

		JPanel panel = new JPanel(new GridBagLayout());
		boxes.forEach((box, value) -> panel.add(box,
				new GridBagConstraints(
						0, boxes.keySet().stream().toList().indexOf(box), 1, 1, 1.0, 1.0,
						GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
						new Insets(0, 0, 0, 0), 0, 0
				)
		));

		JOptionPane.showMessageDialog(parent, panel, message, JOptionPane.PLAIN_MESSAGE);
		return boxes.keySet().stream()
				.filter(AbstractButton::isSelected)
				.map(boxes::get)
				.toList();
	}

	///////////////////////////////////////////////////////////////////////////
	// Active methods
	///////////////////////////////////////////////////////////////////////////

	/**
	 * Send round state.
	 */
	private void sendRoundState() {
		Round round = Round.getInstance();
		if (round != null) updateClients(round);
	}

	/**
	 * Gets input from the client associated to a player.
	 *
	 * @param player the player associated to the client
	 * @param c      the exchange container to send
	 * @return the input from client
	 */
	private Object getInputFromClient(Player player, ExchangeContainer c) {
		clients.forEach(terminal -> updateClient(
				terminal,
				localPlayers.containsKey(terminal) && localPlayers.get(terminal).contains(player) ? c.setActive() : c
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
		updateClients(new ExchangeContainer(Command.PLAYER_NAME_WAIT, null, null, null, playerIndex, null));
		return activeView.promptForPlayerName(playerIndex);
	}

	@Override
	public String promptForNewGame() {
		sendRoundState();
		updateClients(new ExchangeContainer(Command.NEW_GAME_WAIT, null, null, null, null, null));
		return activeView.promptForNewGame();
	}

	@Override
	public int[] promptForRepartition() {
		sendRoundState();
		updateClients(new ExchangeContainer(Command.REPARTITION_WAIT, null, null, null, null, null));
		return activeView.promptForRepartition();
	}

	// Player specific

	@Override
	public int promptForPlayerChoice(String playerName, List<String> playerNames) {
		sendRoundState();

		Player player = Round.getInstance().getPlayerByName(playerName);
		ExchangeContainer exchangeContainer = new ExchangeContainer(Command.PLAYER_CHOICE_WAIT, playerName, null, playerNames, null, null);
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
		ExchangeContainer exchangeContainer = new ExchangeContainer(Command.CARD_CHOICE_WAIT, playerName, null, rumourCards, null, null);
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
		ExchangeContainer exchangeContainer = new ExchangeContainer(Command.CARD_CHOICE_WAIT, playerName, null, null, listSize, null);
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
		ExchangeContainer exchangeContainer = new ExchangeContainer(Command.PLAYER_IDENTITY_WAIT, name, null, null, null, null);
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
		ExchangeContainer exchangeContainer = new ExchangeContainer(Command.ACTION_WAIT, playerName, null, possibleActions, null, null);
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
		ExchangeContainer exchangeContainer = new ExchangeContainer(Command.PLAYER_SWITCH_WAIT, name, null, null, null, null);
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
		updateClients(new ExchangeContainer(Command.SHOW_GAME_WINNER, name, null, null, numberOfRound, null));
		activeView.showGameWinner(name, numberOfRound);
	}

	@Override
	public void showRoundWinner(String name) {
		sendRoundState();
		updateClients(new ExchangeContainer(Command.SHOW_ROUND_WINNER, name, null, null, null, null));
		activeView.showRoundWinner(name);
	}

	@Override
	public void showStartOfRound(int numberOfRound) {
		sendRoundState();
		updateClients(new ExchangeContainer(Command.SHOW_START_OF_ROUND, null, null, null, numberOfRound, null));
		activeView.showStartOfRound(numberOfRound);
	}

	@Override
	public void showPlayerIdentity(String name, boolean witch) {
		sendRoundState();
		updateClients(new ExchangeContainer(Command.SHOW_PLAYER_IDENTITY, name, null, null, null, null));
		activeView.showPlayerIdentity(name, witch);
	}

	@Override
	public void showRevealAction(String name) {
		sendRoundState();
		updateClients(new ExchangeContainer(Command.SHOW_REVEAL_ACTION, name, null, null, null, null));
		activeView.showRevealAction(name);
	}

	@Override
	public void showAccuseAction(String name, String targetedPlayerName) {
		sendRoundState();
		updateClients(new ExchangeContainer(Command.SHOW_ACCUSE_ACTION, name, targetedPlayerName, null, null, null));
		activeView.showAccuseAction(name, targetedPlayerName);
	}

	@Override
	public void showUseCardAction(String name, String chosenCardName) {
		sendRoundState();
		updateClients(new ExchangeContainer(Command.SHOW_CARD_USE_ACTION, name, chosenCardName, null, null, null));
		activeView.showUseCardAction(name, chosenCardName);
	}

	@Override
	public void showCardList(String name, List<String> cards) {
		sendRoundState();
		updateClients(new ExchangeContainer(Command.SHOW_CARD_LIST, name, null, cards, null, null));
		activeView.showCardList(name, cards);
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
}
