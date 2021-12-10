package com.model.game;

import com.controller.PlayerAction;
import com.model.card.CardName;
import com.model.card.Deck;
import com.model.card.RumourCard;
import com.model.player.AI;
import com.model.player.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static com.util.GameUtil.randomInInterval;

public class Round {
    /**
     * The single instance of roundController.
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

    private final Deck deck;

    /**
     * Instantiates a new Round controller.
     */
    public Round(Deck deck, List<Player> players) {
        numberOfRound++;
        this.discardPile = new LinkedList<>();
        this.identityCards = new ArrayList<>();
        this.notSelectablePlayers = new HashMap<>();

        if (currentPlayer == null) currentPlayer = players.get(randomInInterval(players.size() - 1));
        for (Player player : players) identityCards.add(new IdentityCard(player));
        this.deck = deck;

        Round.instance = this;
    }

    /**
     * Gets round controller.
     * Return the single available instance of round controller (Singleton).
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
     * Gets current player.
     *
     * @return the current player
     */
    public static Player getCurrentPlayer() {
        return currentPlayer;
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

    /**
     * Gets standard actions.
     *
     * @param player the player
     * @return the standard actions
     */
    public List<PlayerAction> getStandardActions(Player player) {
        List<PlayerAction> possibleActions = new ArrayList<>();
        if (player == Round.getCurrentPlayer()) possibleActions.add(PlayerAction.ACCUSE);
        else possibleActions.add(PlayerAction.REVEAL_IDENTITY);
        //If the player has at least 1 usable card, we add the possibility to use it
        List<RumourCard> hand = player.getSelectableCardsFromHand();
        if (hand.size() > 0 && getUsableCards(player, hand).size() > 0) possibleActions.add(PlayerAction.USE_CARD);
        if (!(player instanceof AI)) {
            possibleActions.add(PlayerAction.VIEW_HAND);
            possibleActions.add(PlayerAction.VIEW_REVEALED);
            possibleActions.add(PlayerAction.VIEW_DISCARD_PILE);
        }
        return possibleActions;
    }

    /**
     * Reveal identity.
     *
     * @param player the player
     */
    public void revealIdentity(Player player) {
        IdentityCard playerIdentityCard = getPlayerIdentityCard(player);
        playerIdentityCard.setIdentityRevealed(true);
        if (playerIdentityCard.isWitch()) {
            //If a player is revealed as a witch, we exclude him from the round
            identityCards.removeIf(identityCard -> identityCard.player == player);
            setNextPlayer(Round.getCurrentPlayer());
        } else {
            setNextPlayer(player);
        }
    }

    /**
     * Distribute Rumour cards.
     * This method distribute the Rumour cards at the start of a round based on the number of players.
     */
    public void distributeRumourCards() {
        deck.shuffle();
        int nbOfExcessCards = CardName.values().length % identityCards.size();

        //Take care of excess cards
        for (int i = 0; i < nbOfExcessCards; i++) {
            discardPile.add(deck.removeTopCard());
        }

        //Distribute the rest equally
        int numberOfCardsPerPlayer = CardName.values().length / identityCards.size();
        for (IdentityCard identityCard : identityCards) {
            for (int i = 0; i < numberOfCardsPerPlayer; i++) {
                identityCard.player.addCardToHand(deck.removeTopCard());
            }
        }
    }

    public void actualiseCurrentPlayer() {
        currentPlayer = nextPlayer;
    }
}
