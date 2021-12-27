package com.view.server;

public enum Command {
	PLAYER_NAME_REQUEST,
	NEW_GAME_REQUEST,
	PLAYER_CHOICE_REQUEST,
	CARD_CHOICE_REQUEST,
	BLANK_CARD_CHOICE_REQUEST,
	REPARTITION_REQUEST,
	PLAYER_IDENTITY_REQUEST,
	ACTION_REQUEST,
	PLAYER_SWITCH_REQUEST, // 8

	PLAYER_NAME_WAIT,
	NEW_GAME_WAIT,
	PLAYER_CHOICE_WAIT,
	CARD_CHOICE_WAIT,
	BLANK_CARD_CHOICE_WAIT,
	REPARTITION_WAIT,
	PLAYER_IDENTITY_WAIT,
	ACTION_WAIT,
	PLAYER_SWITCH_WAIT, // 17

	SHOW_GAME_WINNER,
	SHOW_ROUND_WINNER,
	SHOW_START_OF_ROUND,
	SHOW_PLAYER_IDENTITY,
	SHOW_REVEAL_ACTION,
	SHOW_ACCUSE_ACTION,
	SHOW_CARD_USE_ACTION,
	SHOW_CARD_LIST;

	public Command switchPA() {
		String name = toString();
		if (this.ordinal() <= 8) {
			name = name.replaceAll("REQUEST", "WAIT");
		} else if (this.ordinal() <= 17) {
			name = name.replaceAll("WAIT", "REQUEST");
		}
		return Command.valueOf(name);
	}

	public boolean isActive() {
		return toString().contains("REQUEST");
	}
}
