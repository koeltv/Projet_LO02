package com.model.game;

import com.model.card.Deck;
import com.model.player.Player;

import java.util.List;

public class LocalRound extends Round {
	public LocalRound(Deck deck, List<Player> players) {
		super(deck, players);
	}

	public static void setInstance(Round round) {
		instance = round;
	}
}
