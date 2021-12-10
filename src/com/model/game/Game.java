package com.model.game;

import com.model.card.Deck;
import com.model.player.Player;

import java.util.ArrayList;
import java.util.List;

import static com.util.GameUtil.randomInInterval;

/**
 * The type Game.
 */
public class Game {
    /**
     * The Players.
     */
    final List<Player> players;

    /**
     * The Deck.
     */
    final Deck deck;

    /**
     * Instantiates a new Game.
     */
    public Game() {
        this.players = new ArrayList<>();
        this.deck = new Deck();
    }

    /**
     * Gets deck.
     *
     * @return the deck
     */
    public Deck getDeck() {
        return deck;
    }

    /**
     * Gets players.
     *
     * @return the players
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Add player.
     *
     * @param player the player
     */
    public void addPlayer(Player player) {
        players.add(player);
    }

    /**
     * Clear players.
     */
    public void clearPlayers() {
        players.clear();
    }

    /**
     * Reset scores.
     */
    public void resetScores() {
        players.forEach(Player::resetScore);
    }

    /**
     * Check if the given repartition is allowed.
     *
     * @param nbOfPlayers the number of players
     * @param nbOfAIs     the number of AIs
     * @return true if the repartition is valid, false otherwise
     */
    public boolean repartitionAllowed(int nbOfPlayers, int nbOfAIs) {
        if (nbOfPlayers + nbOfAIs >= 3 && nbOfPlayers + nbOfAIs <= 6) {
            return nbOfPlayers >= 0 && nbOfPlayers <= 6 && nbOfAIs >= 0 && nbOfAIs <= 6;
        }
        return false;
    }

    /**
     * Verify scores.
     *
     * @return true if at least 1 player has 5 points or more, and false otherwise
     */
    public boolean verifyScores() {
        return players.stream().anyMatch(player -> player.getScore() >= 5);
    }

    /**
     * Settle tie.
     *
     * @param winners the winners
     * @return the winning player
     */
    public Player settleTie(List<Player> winners) {
        return winners.get(randomInInterval(winners.size() - 1));
    }
}
