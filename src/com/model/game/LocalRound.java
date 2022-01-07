package com.model.game;

import com.model.card.Deck;
import com.model.player.Player;

import java.util.List;

/**
 * The type Local round.
 *
 * A subtype of round used by client view.
 *
 * @see com.view.server.ClientSideView
 */
public class LocalRound extends Round {

	/**
	 * Instantiates a new Round.
	 *
	 * @param deck    the deck
	 * @param players the players
	 * @see Deck
	 * @see Player
	 * @see IdentityCard
	 */
	LocalRound(Deck deck, List<Player> players) {
		super(deck, players);
	}

	/**
	 * Sets the round instance.
	 *
	 * @param round the round
	 */
	public static void setInstance(Round round) {
		instance = round;
	}

	/**
	 * Sets current player.
	 *
	 * @param player the player to set to current player
	 */
	public static void setCurrentPlayer(Player player) {
		currentPlayer = player;
	}

	/**
	 * Sets number of round.
	 *
	 * @param roundNumber the round number
	 */
	public static void setNumberOfRound(int roundNumber) {
		numberOfRound = roundNumber;
	}
}
