package com.controller;

import com.model.card.CardName;
import com.model.card.RumourCard;
import com.model.game.IdentityCard;
import com.model.player.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Round.
 * Round is the class that contains all methods to supervise a round. It is a singleton.
 */
public class RoundController {
    private static RoundController roundController = new RoundController();

    private static int numberOfRound;

    private static Player currentPlayer;

    private int numberOfNotRevealedPlayers;

    private Player nextPlayer;

    /**
     * The Discard pile.
     */
    public final List<RumourCard> discardPile = new ArrayList<>();

    /**
     * The Active players.
     */
    public final List<IdentityCard> identityCards = new ArrayList<>();

    private RoundController() {}

    /**
     * Gets round instance.
     *
     * @return the round instance
     */
    public static RoundController getRound() {
        return roundController;
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
     * Gets number of not revealed players.
     *
     * @return the number of not revealed players
     */
    public int getNumberOfNotRevealedPlayers() {
        return numberOfNotRevealedPlayers;
    }

    /**
     * Sets number of not revealed players.
     *
     * @param numberOfNotRevealedPlayers the number of not revealed players
     */
    public void setNumberOfNotRevealedPlayers(int numberOfNotRevealedPlayers) {
        this.numberOfNotRevealedPlayers = numberOfNotRevealedPlayers;
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
     * Sets next player.
     *
     * @param nextPlayer the next player
     */
    public void setNextPlayer(Player nextPlayer) {
        this.nextPlayer = nextPlayer;
    }

    /**
     * Ask the current player for his next action
     * This method will call the play method of the current player
     */
    private void askCurrentPlayerForAction() {
        RoundController.getCurrentPlayer().play();
    }

    /**
     * Distribute Rumour cards
     * This method distribute the Rumour cards at the start of a round based on the number of players
     */
    private void distributeRumourCards() {
        int nbOfExcessCards = CardName.values().length % this.identityCards.size();

        //Take care of excess cards
        if (nbOfExcessCards > 0) {
            for (int i = 0; i < nbOfExcessCards; i++) {
                int index = GameController.randomInInterval(0, GameController.getGame().deck.size() - 1);
                this.discardPile.add(GameController.getGame().deck.remove(index));
            }
        }

        //Distribute the rest equally
        int numberOfCardsPerPlayer = CardName.values().length / this.identityCards.size();
        for (Player player : GameController.getGame().players) {
            for (int i = 0; i < numberOfCardsPerPlayer; i++) {
                int index = GameController.randomInInterval(0, GameController.getGame().deck.size() - 1);
                player.addCardToHand(GameController.getGame().deck.remove(index));
            }
        }
    }

    /**
     * Ask players for their chosen identity
     * This method will call the selectIdentity() method to prompt players to choose a role for the round
     */
    private void askPlayersForIdentity() {
        for (IdentityCard identityCard : this.identityCards) {
            identityCard.player.selectIdentity();
        }
    }

    /**
     * Select the first player
     * This method will only be used on the first round to select a random player to start
     */
    private void selectFirstPlayer() {
        List<Player> playerList = GameController.getGame().players;
        RoundController.currentPlayer = playerList.get(GameController.randomInInterval(0, playerList.size() - 1));
    }

    /**
     * Set up the round
     * This method will do everything necessary to set up a round (select 1st player, create identity cards, distribute Rumour cards, ask players for identity)
     */
    public void startRound() {
        if (currentPlayer == null) selectFirstPlayer();
        //Fill up the list of active players at the start
        for (Player player: GameController.getGame().players) {
            IdentityCard playerIdentityCard = new IdentityCard(player);
            roundController.identityCards.add(playerIdentityCard);
            player.identityCard = playerIdentityCard;
        }
        this.numberOfNotRevealedPlayers = this.identityCards.size();

        this.distributeRumourCards();
        this.askPlayersForIdentity();
        numberOfRound++;
        System.out.println("================Round " + RoundController.getNumberOfRound() + "================");
        playRound();
    }

    /**
     * Round playing loop
     * This method will prompt the current player for action, then set the current player to the next and loop while there is more than 1 not revealed player
     */
    private void playRound() {
        do {
            askCurrentPlayerForAction();
            if (this.numberOfNotRevealedPlayers > 1) RoundController.currentPlayer = this.nextPlayer; //TODO Test if conditional is useful
            //TODO Playing loop, currently there is a risk of not setting next player
        } while (this.numberOfNotRevealedPlayers > 1);
        endRound();
    }

    /**
     * Wrap up the round
     * This method will do everything necessary to wrap up a round (reveal last player and give him points, gather all cards)
     */
    private void endRound() {
        //We search the last not revealed player, reveal is identity and give him points
        for (IdentityCard identityCard : this.identityCards) {
            if (!identityCard.isIdentityRevealed()) {
                //Reveal player identity and give points
                System.out.println(identityCard.player.getName() + " won this round !");
                identityCard.player.revealIdentity();
                identityCard.player.addToScore(identityCard.player.identityCard.isWitch() ? 2 : 1);
                //Set him to be first for the next round
                RoundController.currentPlayer = identityCard.player;
                break;
            }
        }
        //Gather all players cards
        for (Player player : GameController.getGame().players) {
            int startingNumberOfCard = player.hand.size();
            for (int i = 0; i < startingNumberOfCard; i++) {
                RumourCard removedCard = player.hand.get(0).rumourCard;
                player.removeCardFromHand(removedCard);
                GameController.getGame().deck.add(removedCard);
            }
        }
        //Gather the discarded cards
        int startingNumberOfCard = this.discardPile.size();
        for (int i = 0; i < startingNumberOfCard; i++) {
            RumourCard removedCard = this.discardPile.get(0);
            this.discardPile.remove(removedCard);
            GameController.getGame().deck.add(removedCard);
        }
        RoundController.roundController = new RoundController();
    }

}
