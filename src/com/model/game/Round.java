package com.model.game;

import com.model.card.RumourCard;
import com.model.player.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Round.
 * Round is the class that contains all methods to supervise a round. It is a singleton.
 */
public class Round {
    /**
     * The only available instance of Round.
     */
    private static Round instance;

    /**
     * The number of round.
     */
    private static int numberOfRound;

    /**
     * The current player.
     */
    private static Player currentPlayer;

    /**
     * The Next player.
     */
    private Player nextPlayer;

    /**
     * The Discard pile.
     */
    private final LinkedList<RumourCard> discardPile;

    /**
     * The Active players.
     */
    private final List<IdentityCard> identityCards;

    /**
     * The players that aren't selectable per player.
     */
    private final HashMap<Player, List<Player>> notSelectablePlayers;

    /**
     * Instantiates a new Round controller.
     */
    public Round() {
        this.discardPile = new LinkedList<>();
        this.identityCards = new ArrayList<>();
        this.notSelectablePlayers = new HashMap<>();

        Round.instance = this;
    }

    /**
     * Gets round instance.
     * Return the single available instance of round (Singleton).
     *
     * @return the round controller
     */
    public static Round getInstance() {
        return instance;
    }

    /**
     * Reset static attributes.
     */
    public static void reset() {
        numberOfRound = 0;
        currentPlayer = null;
    }

    /**
     * Gets number of round.
     *
     * @return the number of round
     */
    public static int getNumberOfRound() {
        return numberOfRound;
    }

    /**
     * Augment the counter of rounds.
     */
    public static void addNumberOfRound() {
        numberOfRound++;
    }

    /**
     * Gets current player.
     *
     * @return the current player
     */
    public static Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Sets current player.
     *
     * @param player the player
     */
    public static void setCurrentPlayer(Player player) {
        currentPlayer = player;
    }

    /**
     * Gets number of not revealed players.
     *
     * @return the number of not revealed players
     */
    public int getNumberOfNotRevealedPlayers() {
        return (int) identityCards.stream().filter(identityCard -> !identityCard.isIdentityRevealed()).count();
    }

    /**
     * Sets next player.
     *
     * @param nextPlayer the next player
     */
    public void setNextPlayer(Player nextPlayer) {
        this.nextPlayer = nextPlayer;
    }

    /**
     * Gets next player.
     *
     * @return the next player
     */
    public Player getNextPlayer() {
        return this.nextPlayer;
    }

    /**
     * Add not selectable player to a player.
     *
     * @param player              the player
     * @param notSelectablePlayer the player not selectable by player
     */
    public void addNotSelectablePlayer(Player player, Player notSelectablePlayer) {
        notSelectablePlayers.computeIfAbsent(player, k -> new ArrayList<>());
        notSelectablePlayers.get(player).add(notSelectablePlayer);
    }

    /**
     * Gets discard pile.
     *
     * @return the discard pile
     */
    public LinkedList<RumourCard> getDiscardPile() {
        return discardPile;
    }

    /**
     * Gets identity cards
     *
     * @return the identity cards
     */
    public List<IdentityCard> getIdentityCards() {
        return identityCards;
    }

    /**
     * Gets not selectable players of a specific player.
     *
     * @param player the player to check for
     * @return the list of not selectable players for the given player
     */
    public List<Player> getNotSelectablePlayers(Player player) {
        return notSelectablePlayers.get(player);
    }

    /**
     * Gets player identity card.
     *
     * @param targetedPlayer the targeted player
     * @return the player identity card
     */
    public IdentityCard getPlayerIdentityCard(Player targetedPlayer) {
        return identityCards.stream()
                .filter(identityCard -> identityCard.player == targetedPlayer)
                .findFirst().orElse(null);
    }

    /**
     * Gets usable cards.
     * Return the cards from the input list that can be used by the player.
     *
     * @param player the player
     * @param cards  the cards
     * @return the usable cards
     */
    public List<RumourCard> getUsableCards(Player player, List<RumourCard> cards) {
        return cards.stream()
                .filter(rumourCard -> rumourCard.isUsable(player))
                .collect(Collectors.toList());
    }

    /**
     * Get the list of all players in the current round.
     *
     * @param player the player
     * @return selectable players
     */
    public List<Player> getSelectablePlayers(Player player) {
        return identityCards.stream()
                .filter(identityCard -> identityCard.player != player)
                .map(identityCard -> identityCard.player)
                .collect(Collectors.toList());
    }

    /**
     * Get the list of not revealed players in the current round.
     *
     * @param player the player
     * @return selectable players
     */
    public List<Player> getNotRevealedPlayers(Player player) {
        return identityCards.stream()
                .filter(identityCard -> identityCard.player != player && !identityCard.isIdentityRevealed())
                .map(identityCard -> identityCard.player)
                .collect(Collectors.toList());
    }
}
