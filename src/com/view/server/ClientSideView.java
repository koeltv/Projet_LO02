package com.view.server;

import com.controller.PlayerAction;
import com.controller.RoundController;
import com.model.card.RumourCard;
import com.model.game.LocalRound;
import com.model.game.Round;
import com.view.ActiveView;
import com.view.PassiveView;
import com.view.View;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.IntStream;

/**
 * The type Client side view.
 */
public class ClientSideView implements ActiveView, PassiveView {
	/**
	 * The minimum byte value.
	 */
	private static final int ADDRESS_BYTE_MIN = 0;

	/**
	 * The maximum byte value (excluded).
	 */
	private static final int ADDRESS_BYTE_MAX = 256;

	/**
	 * The first port to check.
	 */
	private static final int PORT_MIN = 49152;

	/**
	 * The last port to check.
	 */
	private static final int PORT_MAX = 49160;

	/**
	 * The linked server.
	 */
	private Terminal server;

	/**
	 * The View.
	 */
	private final View view;

	/**
	 * Instantiates a new Client side view.
	 *
	 * @param view the view
	 */
	public <T extends View> ClientSideView(T view) {
		this.view = view;
		new RoundController((ActiveView) view);
		try {
			connectToAvailableHost();
		} catch (ExecutionException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	///////////////////////////////////////////////////////////////////////////
	// Connection method
	///////////////////////////////////////////////////////////////////////////

	/**
	 * Try to connect to an available host.
	 *
	 * @throws ExecutionException   if no connection succedded
	 * @throws InterruptedException if interrupted
	 */
	public void connectToAvailableHost() throws ExecutionException, InterruptedException {
		ExecutorService executor = ForkJoinPool.commonPool();
		try { //Check for similar to local IPs first
			var ips = Arrays.stream(InetAddress.getAllByName(InetAddress.getLocalHost().getCanonicalHostName()))
					.filter(address -> address instanceof Inet4Address)
					.map(InetAddress::getAddress)
					.toList();

			List<CallableTerminal> terminals = new ArrayList<>();
			ips.forEach(ip -> terminals.addAll(IntStream.range(ADDRESS_BYTE_MIN, ADDRESS_BYTE_MAX).unordered().parallel()
					.mapToObj(i -> new byte[]{ip[0], ip[1], ip[2], (byte) i})
					.<CallableTerminal>mapMulti(((bytes, consumer) -> {
						try {
							InetAddress inetAddress = InetAddress.getByAddress(bytes);
							consumer.accept(new CallableTerminal(inetAddress, PORT_MIN));
						} catch (UnknownHostException e) {
							e.printStackTrace();
						}
					}))
					.toList())
			);
			server = executor.invokeAny(terminals);
			System.out.println("Found host: " + server.socket());
			executor.shutdown();
		} catch (ExecutionException e) { //Check other if no result
			try {
				byte[] ip = InetAddress.getLocalHost().getAddress();
				executor.shutdown();

				executor.invokeAny(IntStream.range(ADDRESS_BYTE_MIN, ADDRESS_BYTE_MAX).unordered().parallel()
						.mapToObj(i -> new byte[]{ip[0], ip[1], (byte) i, 0})
						.flatMap(bytes -> IntStream.range(ADDRESS_BYTE_MIN, ADDRESS_BYTE_MAX).mapToObj(j -> new byte[]{bytes[0], bytes[1], bytes[2], (byte) j}))
						.<CallableTerminal>mapMulti((bytes, consumer) -> {
							try {
								InetAddress inetAddress = InetAddress.getByAddress(bytes);
								IntStream.range(PORT_MIN, PORT_MAX).forEach(i -> consumer.accept(new CallableTerminal(inetAddress, i)));
							} catch (UnknownHostException ex) {
								e.printStackTrace();
							}
						})
						.toList());
			} catch (UnknownHostException ex) {
				ex.printStackTrace();
			}
		} catch (InterruptedException | UnknownHostException e) {
			e.printStackTrace();
		}
	}

	///////////////////////////////////////////////////////////////////////////
	// Communication methods
	///////////////////////////////////////////////////////////////////////////

	/**
	 * Choose an appropriate passive action.
	 *
	 * @param c the container containing parameters to use
	 */
	@SuppressWarnings("unchecked")
	private void chooseAppropriatePassiveAction(ExchangeContainer c) {
		switch (c.command()) {
			case SHOW_CARD_LIST -> showCardList(c.name1(), (List<String>) c.list());
			case SHOW_GAME_WINNER -> showGameWinner(c.name1(), c.number());
			case SHOW_ROUND_WINNER -> showRoundWinner(c.name1());
			case SHOW_ACCUSE_ACTION -> showAccuseAction(c.name1(), c.name2());
			case SHOW_REVEAL_ACTION -> showRevealAction(c.name1());
			case SHOW_START_OF_ROUND -> showStartOfRound(c.number());
			case SHOW_CARD_USE_ACTION -> showUseCardAction(c.name1(), c.name2());
			case SHOW_PLAYER_IDENTITY -> showPlayerIdentity(c.name1(), c.isWitch());

			case ACTION_WAIT -> waitForAction(c.name1(), (List<PlayerAction>) c.list());
			case NEW_GAME_WAIT -> waitForNewGame();
			case CARD_CHOICE_WAIT -> waitForCardChoice((List<RumourCard>) c.list());
			case REPARTITION_WAIT -> waitForRepartition();
			case PLAYER_NAME_WAIT -> waitForPlayerName(c.number());
			case PLAYER_CHOICE_WAIT -> waitForPlayerChoice((List<String>) c.list());
			case PLAYER_SWITCH_WAIT -> waitForPlayerSwitch(c.name1());
			case PLAYER_IDENTITY_WAIT -> waitForPlayerIdentity(c.name1());
			case BLANK_CARD_CHOICE_WAIT -> waitForCardChoice(null);

			default -> System.err.println("Wrong passive action : " + c.command());
		}
	}

	/**
	 * Choose an appropriate active action and return the result.
	 *
	 * @param c the container containing parameters to use
	 * @return the result of prompting the user
	 */
	@SuppressWarnings("unchecked")
	private Object chooseAppropriateActiveAction(ExchangeContainer c) {
		return switch (c.command()) {
			case ACTION_REQUEST -> promptForAction(c.name1(), (List<PlayerAction>) c.list());
			case NEW_GAME_REQUEST -> promptForNewGame();
			case CARD_CHOICE_REQUEST -> promptForCardChoice(c.name1(), (List<RumourCard>) c.list());
			case REPARTITION_REQUEST -> promptForRepartition();
			case PLAYER_NAME_REQUEST -> promptForPlayerName(c.number());
			case PLAYER_CHOICE_REQUEST -> promptForPlayerChoice(c.name1(), (List<String>) c.list());
			case PLAYER_SWITCH_REQUEST -> {
				promptForPlayerSwitch(c.name1());
				yield null;
			}
			case PLAYER_IDENTITY_REQUEST -> promptForPlayerIdentity(c.name1());
			case BLANK_CARD_CHOICE_REQUEST -> promptForCardChoice(c.name1(), c.number());
			default -> throw new IllegalStateException("Unexpected value: " + c.command());
		};
	}

	/**
	 * Run the client.
	 */
	public void run() {
		try {
			while (server != null) {
				try {
					Object object = server.input().readObject();

					if (object instanceof Round round) {
						LocalRound.setInstance(round);
					} else if (Round.getInstance() != null && object instanceof ExchangeContainer container) {
						if (container.command().isActive()) {
							server.output().writeObject(chooseAppropriateActiveAction(container));
						} else {
							chooseAppropriatePassiveAction(container);
						}
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
			System.exit(0);
		}
	}

	///////////////////////////////////////////////////////////////////////////
	// View methods
	///////////////////////////////////////////////////////////////////////////

	@Override
	public void showGameWinner(String name, int numberOfRound) {
		view.showGameWinner(name, numberOfRound);
	}

	@Override
	public void showRoundWinner(String name) {
		view.showRoundWinner(name);
	}

	@Override
	public void showStartOfRound(int numberOfRound) {
		view.showStartOfRound(numberOfRound);
	}

	@Override
	public void showPlayerIdentity(String name, boolean witch) {
		view.showPlayerIdentity(name, witch);
	}

	@Override
	public void showRevealAction(String name) {
		view.showRevealAction(name);
	}

	@Override
	public void showAccuseAction(String name, String targetedPlayerName) {
		view.showAccuseAction(name, targetedPlayerName);
	}

	@Override
	public void showUseCardAction(String name, String chosenCardName) {
		view.showUseCardAction(name, chosenCardName);
	}

	@Override
	public void showCardList(String name, List<String> cards) {
		view.showCardList(name, cards);
	}

	///////////////////////////////////////////////////////////////////////////
	// Active methods
	///////////////////////////////////////////////////////////////////////////

	@Override
	public String promptForPlayerName(int playerIndex) {
		return ((ActiveView) view).promptForPlayerName(playerIndex);
	}

	@Override
	public String promptForNewGame() {
		return ((ActiveView) view).promptForNewGame();
	}

	@Override
	public int promptForPlayerChoice(String playerName, List<String> playerNames) {
		return ((ActiveView) view).promptForPlayerChoice(playerName, playerNames);
	}

	@Override
	public int promptForCardChoice(String playerName, List<RumourCard> rumourCards) {
		return ((ActiveView) view).promptForCardChoice(playerName, rumourCards);
	}

	@Override
	public int promptForCardChoice(String playerName, int listSize) {
		return ((ActiveView) view).promptForCardChoice(playerName, listSize);
	}

	@Override
	public int[] promptForRepartition() {
		return ((ActiveView) view).promptForRepartition();
	}

	@Override
	public int promptForPlayerIdentity(String name) {
		return ((ActiveView) view).promptForPlayerIdentity(name);
	}

	@Override
	public PlayerAction promptForAction(String playerName, List<PlayerAction> possibleActions) {
		return ((ActiveView) view).promptForAction(playerName, possibleActions);
	}

	@Override
	public void promptForPlayerSwitch(String name) {
		((ActiveView) view).promptForPlayerSwitch(name);
	}

	///////////////////////////////////////////////////////////////////////////
	// Passive Methods
	///////////////////////////////////////////////////////////////////////////

	@Override
	public void waitForPlayerName(int playerIndex) {
		((PassiveView) view).waitForPlayerName(playerIndex);
	}

	@Override
	public void waitForNewGame() {
		((PassiveView) view).waitForNewGame();
	}

	@Override
	public void waitForPlayerChoice(List<String> playerNames) {
		((PassiveView) view).waitForPlayerChoice(playerNames);
	}

	@Override
	public void waitForCardChoice(List<RumourCard> rumourCards) {
		((PassiveView) view).waitForCardChoice(rumourCards);
	}

	@Override
	public void waitForRepartition() {
		((PassiveView) view).waitForRepartition();
	}

	@Override
	public void waitForPlayerIdentity(String name) {
		((PassiveView) view).waitForPlayerIdentity(name);
	}

	@Override
	public void waitForAction(String playerName, List<PlayerAction> possibleActions) {
		((PassiveView) view).waitForAction(playerName, possibleActions);
	}

	@Override
	public void waitForPlayerSwitch(String name) {
		((PassiveView) view).waitForPlayerSwitch(name);
	}
}
