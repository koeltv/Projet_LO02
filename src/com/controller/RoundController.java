package com.controller;

import com.model.card.CardName;
import com.model.card.RumourCard;
import com.model.game.CardState;
import com.model.game.IdentityCard;
import com.model.player.AI;
import com.model.player.Player;
import com.model.player.PlayerAction;
import com.view.ActiveView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Round.
 * Round is the class that contains all methods to supervise a round. It is a singleton.
 */
public class RoundController {
    /**
     * The single instance of roundController.
     */
    private static RoundController roundController;

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
     * The Game controller.
     */
    private final GameController gameController;

    /**
     * The View.
     */
    private final ActiveView view;

    /**
     * The Discard pile.
     */
    public final List<RumourCard> discardPile;

    /**
     * The Active players.
     */
    public final List<IdentityCard> identityCards;

    /**
     * Instantiates a new Round controller.
     *
     * @param gameController the game controller
     * @param view           the view
     */
    RoundController(GameController gameController, ActiveView view) {
        this.discardPile = new ArrayList<>();
        this.identityCards = new ArrayList<>();

        this.view = view;
        this.gameController = gameController;

        RoundController.roundController = this;
    }

    /**
     * Gets round controller.
     * Return the single available instance of round controller (Singleton).
     *
     * @return the round controller
     */
    public static RoundController getRoundController() {
        return roundController;
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
    private int getNumberOfNotRevealedPlayers() {
        return (int) identityCards.stream().filter(IdentityCard::isIdentityNotRevealed).count();
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
     * Gets player identity card.
     *
     * @param targetedPlayer the targeted player
     * @return the player identity card
     */
    public IdentityCard getPlayerIdentityCard(Player targetedPlayer) {
        return identityCards
                .stream()
                .filter(identityCard -> identityCard.player == targetedPlayer)
                .findFirst().orElse(null);
    }

    /**
     * Ask the player to choose a card.
     *
     * @param player         the player
     * @param rumourCardList the list of card to select from
     * @return chosen card
     */
    public RumourCard chooseCard(Player player, List<RumourCard> rumourCardList) {
        //Printing selectable cards
        if (player instanceof AI) {
            return ((AI) player).selectCard(rumourCardList);
        } else {
            int index = view.promptForCardChoice(rumourCardList);
            return rumourCardList.get(index);
        }
    }

    /**
     * Get the list of all players in the current round.
     *
     * @param player the player
     * @return selectable players
     */
    public List<Player> getSelectablePlayers(Player player) {
        return identityCards
                .stream()
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
        return identityCards
                .stream()
                .filter(identityCard -> identityCard.player != player && identityCard.isIdentityNotRevealed())
                .map(identityCard -> identityCard.player)
                .collect(Collectors.toList());
    }

    /**
     * Ask a player to choose another player.
     *
     * @param choosingPlayer the choosing player
     * @param playerList     the player list to choose from
     * @return chosen player
     */
    public Player choosePlayer(Player choosingPlayer, List<Player> playerList) {
        if (choosingPlayer instanceof AI) {
            return ((AI) choosingPlayer).selectPlayer(playerList);
        } else {
            int index = view.promptForPlayerChoice(playerList.stream().map(Player::getName).collect(Collectors.toList()));
            return playerList.get(index);
        }
    }

    /**
     * Ask the current player for his next action.
     * This method will call the play method of the current player.
     *
     * @param player the player
     */
    private void askPlayerForAction(Player player) {
        //Get possible actions
        List<PlayerAction> possibleActions = new ArrayList<>();
        if (player == RoundController.getCurrentPlayer()) possibleActions.add(PlayerAction.ACCUSE);
        else possibleActions.add(PlayerAction.REVEAL_IDENTITY);
        if (player.hand.size() > 0) possibleActions.add(PlayerAction.USE_CARD);

        //Ask the player to choose his next action
        PlayerAction action;
        if (player instanceof AI) action = ((AI) player).play(possibleActions);
        else action = view.promptForAction(player.getName(), possibleActions);

        applyPlayerAction(player, action);
    }

    /**
     * Apply player action.
     *
     * @param player the player
     * @param action the action
     */
    public void applyPlayerAction(Player player, PlayerAction action) {
        switch (action) {
            case REVEAL_IDENTITY -> {
                view.showPlayerAction(player.getName());
                view.showPlayerIdentity(player.getName(), getPlayerIdentityCard(player).isWitch());

                revealIdentity(player);
            }
            case ACCUSE -> {
                Player targetedPlayer = choosePlayer(player, getNotRevealedPlayers(player));
                view.showPlayerAction(player.getName(), targetedPlayer.getName());

                askPlayerForAction(targetedPlayer);

                //If the player is a witch, its identity card is deleted, so if null the player was revealed as a witch
                if (getPlayerIdentityCard(targetedPlayer) == null) {
                    player.addToScore(1);
                }
            }
            case USE_CARD -> {
                boolean cardUsedSuccessfully;
                do {
                    RumourCard chosenRumourCard = chooseCard(player, player.getSelectableCardsFromHand());
                    view.showPlayerAction(player.getName(), chosenRumourCard.getCardName());
                    cardUsedSuccessfully = player.revealRumourCard(chosenRumourCard);
                } while (!cardUsedSuccessfully);
            }
        }
    }

    /**
     * Reveal identity.
     *
     * @param player the player
     */
    private void revealIdentity(Player player) {
        IdentityCard playerIdentityCard = getPlayerIdentityCard(player);
        playerIdentityCard.setIdentityRevealed(true);
        if (playerIdentityCard.isWitch()) {
            //If a player is revealed as a witch, we exclude him from the round
            identityCards.removeIf(identityCard -> identityCard.player == player);
            setNextPlayer(RoundController.getCurrentPlayer());
        } else {
            setNextPlayer(player);
        }
    }

    /**
     * Distribute Rumour cards.
     * This method distribute the Rumour cards at the start of a round based on the number of players.
     */
    private void distributeRumourCards() {
        int nbOfExcessCards = CardName.values().length % identityCards.size();

        //Take care of excess cards
        for (int i = 0; i < nbOfExcessCards; i++) {
            discardPile.add(gameController.deck.removeTopCard());
        }

        //Distribute the rest equally
        int numberOfCardsPerPlayer = CardName.values().length / identityCards.size();
        gameController.players.forEach(player -> {
            for (int i = 0; i < numberOfCardsPerPlayer; i++) {
                player.addCardToHand(gameController.deck.removeTopCard());
            }
        });
    }

    /**
     * Ask players for their chosen identity.
     * This method will call the selectIdentity() method to prompt players to choose a role for the round.
     */
    private void askPlayersForIdentity() {
        identityCards.forEach(identityCard -> {
            if (identityCard.player instanceof AI) {
                ((AI) (identityCard.player)).selectIdentity();
            } else {
                int identity = view.promptForPlayerIdentity(identityCard.player.getName());
                identityCard.setWitch(identity > 0);
            }
        });
    }

    /**
     * Select the first player.
     * This method will only be used on the first round to select a random player to start.
     */
    private void selectFirstPlayer() {
        List<Player> playerList = gameController.players;
        RoundController.currentPlayer = playerList.get(GameController.randomInInterval(0, playerList.size() - 1));
    }

    /**
     * Set up the round.
     * This method will do everything necessary to set up a round (select 1st player, create identity cards, distribute Rumour cards, ask players for identity).
     */
    private void startRound() {
        numberOfRound++;
        view.showStartOfRound(numberOfRound);

        if (currentPlayer == null) selectFirstPlayer();
        //Fill up the list of active players at the start
        gameController.players.forEach(player -> identityCards.add(new IdentityCard(player)));

        distributeRumourCards();
        askPlayersForIdentity();
    }

    /**
     * Round playing loop.
     * This method will prompt the current player for action, then set the current player to the next and loop while there is more than 1 not revealed player.
     */
    private void playRound() {
        do {
            askPlayerForAction(getCurrentPlayer());
            currentPlayer = nextPlayer;
        } while (getNumberOfNotRevealedPlayers() > 1);
    }

    /**
     * Wrap up the round.
     * This method will do everything necessary to wrap up a round (reveal last player and give him points, gather all cards).
     */
    private void endRound() {
        //We search the last not revealed player, reveal is identity and give him points
        for (IdentityCard identityCard : identityCards) {
            if (identityCard.isIdentityNotRevealed()) {
                Player winner = identityCard.player;
                //Reveal player identity and give points
                view.showRoundWinner(winner.getName());
                view.showPlayerIdentity(winner.getName(), identityCard.isWitch());
                winner.addToScore(identityCard.isWitch() ? 2 : 1);
                //Set him to be first for the next round
                currentPlayer = winner;
                break;
            }
        }
        //Gather all players cards
        gameController.players.forEach(player -> {
            for (Iterator<CardState> iterator = player.hand.iterator(); iterator.hasNext(); ) {
                gameController.deck.returnCardToDeck(player.removeCardFromHand(player.hand.get(0).rumourCard));
            }
        });
        //Gather the discarded cards
        for (Iterator<RumourCard> iterator = discardPile.iterator(); iterator.hasNext(); ) {
            gameController.deck.returnCardToDeck(discardPile.remove(0));
        }
    }

    /**
     * Run the round.
     */
    public void run() {
        startRound();
        playRound();
        endRound();
    }

}
