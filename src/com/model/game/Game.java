package com.model.game;

import com.model.card.Deck;
import com.model.player.Player;

import java.util.ArrayList;
import java.util.List;

import static com.util.GameUtil.randomInInterval;

/**
 * The type Game.
 * 
 * Gives all the methods related to the game.
 */
public class Game {
    
	/**
     * The Players.
     * 
     * @see com.model.player.Player
     */
    final List<Player> players;

    /**
     * The Deck.
     * 
     * @see com.model.card.Deck
     */
    final Deck deck;

    /**
     * Instantiates a new Game.
     * 
     * @see com.model.card.Deck
     */
    public Game() {
        this.players = new ArrayList<>();
        this.deck = new Deck();
    }

    /**
     * Gets deck.
     *
     * @return the deck
     * @see com.model.card.Deck
     */
    public Deck getDeck() {
        return deck;
    }

    /**
     * Gets players.
     *
     * @return the players
     * @see com.model.player.Player
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Add player.
     *
     * @param player the player
     * @see com.model.player.Player
     */
    public void addPlayer(Player player) {
        players.add(player);
    }

    /**
     * Clear players.
     * Remove all players in the list.
     */
    public void clearPlayers() {
        players.clear();
    }

    /**
     * Reset scores.
     * 
     * @see com.model.player.Player
     */
    public void resetScores() {
        players.forEach(Player::resetScore);
    }

    /**
     * Check if the given repartition is allowed.
     * The total number of players/AI should not be less than 3 nor more than 6.
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
     * The game ends when at least one of the players has 5 points. This method is only the verification of this condition.
     *
     * @return true if at least 1 player has 5 points or more, and false otherwise
     * @see com.model.player.Player 
     */
    public boolean verifyScores() {
        return players.stream().anyMatch(player -> player.getScore() >= 5);
    }
    
    /**
     * Settle tie.
     *
     * @param winners the winners
     * @return the winning player
     * @see com.model.player.Player
     */
    public Player settleTie(List<Player> winners) {
        return winners.get(randomInInterval(winners.size() - 1));
    }
}
