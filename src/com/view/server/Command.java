package com.view.server;

/**
 * The enum Command.
 * <p>
 * Used to transmit an operation to do to a client.
 */
public enum Command {
	/**
	 * Player name request command.
	 */
	PLAYER_NAME_REQUEST,
	/**
	 * New game request command.
	 */
	NEW_GAME_REQUEST,
	/**
	 * Player choice request command.
	 */
	PLAYER_CHOICE_REQUEST,
	/**
	 * Card choice request command.
	 */
	CARD_CHOICE_REQUEST,
	/**
	 * Blank card choice request command.
	 */
	BLANK_CARD_CHOICE_REQUEST,
	/**
	 * Repartition request command.
	 */
	REPARTITION_REQUEST,
	/**
	 * Player identity request command.
	 */
	PLAYER_IDENTITY_REQUEST,
	/**
	 * Action request command.
	 */
	ACTION_REQUEST,
	/**
	 * Player switch request command.
	 */
	PLAYER_SWITCH_REQUEST, // 8

	/**
	 * Player name wait command.
	 */
	PLAYER_NAME_WAIT,
	/**
	 * New game wait command.
	 */
	NEW_GAME_WAIT,
	/**
	 * Player choice wait command.
	 */
	PLAYER_CHOICE_WAIT,
	/**
	 * Card choice wait command.
	 */
	CARD_CHOICE_WAIT,
	/**
	 * Blank card choice wait command.
	 */
	BLANK_CARD_CHOICE_WAIT,
	/**
	 * Repartition wait command.
	 */
	REPARTITION_WAIT,
	/**
	 * Player identity wait command.
	 */
	PLAYER_IDENTITY_WAIT,
	/**
	 * Action wait command.
	 */
	ACTION_WAIT,
	/**
	 * Player switch wait command.
	 */
	PLAYER_SWITCH_WAIT, // 17

	/**
	 * Show game winner command.
	 */
	SHOW_GAME_WINNER,
	/**
	 * Show round winner command.
	 */
	SHOW_ROUND_WINNER,
	/**
	 * Show start of round command.
	 */
	SHOW_START_OF_ROUND,
	/**
	 * Show player identity command.
	 */
	SHOW_PLAYER_IDENTITY,
	/**
	 * Show reveal action command.
	 */
	SHOW_REVEAL_ACTION,
	/**
	 * Show accuse action command.
	 */
	SHOW_ACCUSE_ACTION,
	/**
	 * Show card use action command.
	 */
	SHOW_CARD_USE_ACTION,
	/**
	 * Show card list command.
	 */
	SHOW_CARD_LIST;

	/**
	 * Switch passive/active command.
	 * If the command is currently active (ending in REQUEST) switch it to passive (ending in WAIT) and vice-versa.
	 *
	 * @return the active or passive counterpart of a command
	 */
	public Command switchPA() {
		String name = toString();
		if (this.ordinal() <= 8) {
			name = name.replaceAll("REQUEST", "WAIT");
		} else if (this.ordinal() <= 17) {
			name = name.replaceAll("WAIT", "REQUEST");
		}
		return Command.valueOf(name);
	}

	/**
	 * Check if the command is active.
	 *
	 * @return the boolean
	 */
	public boolean isActive() {
		return toString().contains("REQUEST");
	}
}
