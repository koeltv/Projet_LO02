package com.controller;

import com.model.card.CardName;
import com.model.card.RumourCard;
import com.model.game.CardState;
import com.model.game.IdentityCard;
import com.model.player.AI;
import com.model.player.Player;
import com.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * The type Round.
 * Round is the class that contains all methods to supervise a round. It is a singleton.
 */
public class RoundController {

    enum RoundState {
        INITIALIZING_ROUND, PLAYING_ROUND, ENDING_ROUND, ROUND_ENDED
    }

    public static RoundController roundController;

    private static int numberOfRound;

    private static Player currentPlayer;

    public int numberOfNotRevealedPlayers;

    private Player nextPlayer;

    private final View view;

    public RoundState roundState;

    /**
     * The Discard pile.
     */
    public final List<RumourCard> discardPile;

    /**
     * The Active players.
     */
    public final List<IdentityCard> identityCards;

    RoundController(View view) {
        this.discardPile = new ArrayList<>();
        this.identityCards = new ArrayList<>();
        this.roundState = RoundState.INITIALIZING_ROUND;
        this.view = view;

        RoundController.roundController = this; //TODO Get rid of
    }

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
     * Sets next player.
     *
     * @param nextPlayer the next player
     */
    public void setNextPlayer(Player nextPlayer) {
        this.nextPlayer = nextPlayer;
    }

    /**
     * Get the list of not revealed card from the player's hand
     *
     * @return selectable cards
     */
    public List<CardState> getSelectableCards(Player player) {
        List<CardState> selectableCards = new ArrayList<>();
        for (CardState cardState : player.hand) {
            if (!cardState.isRevealed()) selectableCards.add(cardState);
        }
        return selectableCards;
    }

    /**
     * Prompt the player to choose a card
     *
     * @return chosen card
     */
    public RumourCard chooseCard(Player player) {
        List<CardName> selectableCards = new ArrayList<>();
        for (CardState cardState : getSelectableCards(player)) {
            selectableCards.add(cardState.rumourCard.getCardName());
        }
        //Printing selectable cards
        view.promptForCardChoice(selectableCards);
        Scanner scanner = new Scanner(System.in);
        return player.hand.get(scanner.nextInt()).rumourCard;
    }

    /**
     * Get the list of not revealed player in the current round
     *
     * @return selectable players
     */
    public List<IdentityCard> getSelectablePlayers(Player player) {
        List<IdentityCard> selectablePlayers = new ArrayList<>();
        for (IdentityCard identityCard : identityCards) {
            if (identityCard.player != player && !identityCard.isIdentityRevealed()) {
                selectablePlayers.add(identityCard);
            }
        }
        return selectablePlayers;
    }

    /**
     * Prompt the player to choose another player
     *
     * @return chosen player
     */
    public Player choosePlayer(Player player) {
        List<String> selectablePlayers = new ArrayList<>();
        for (IdentityCard identityCard : getSelectablePlayers(player)) {
            selectablePlayers.add(identityCard.player.getName());
        }
        //Printing selectable players
        int index = view.promptForPlayerChoice(selectablePlayers);
        return getSelectablePlayers(player).get(index).player;
    }

    /**
     * Ask the current player for his next action
     * This method will call the play method of the current player
     */
    public void askPlayerForAction(Player player) {
        if (!(player instanceof AI)) {
            List<String> possibleActions = new ArrayList<>();
            if (player == RoundController.getCurrentPlayer()) possibleActions.add("accuse");
            else possibleActions.add("reveal identity");
            if (player.hand.size() > 0) possibleActions.add("use card");

            String action = view.promptForAction(player.getName(), possibleActions);
            switch (action) {
                case "reveal identity" -> {
                    player.revealIdentity();
                    numberOfNotRevealedPlayers--;
                    view.showPlayerIdentity(player.getName(), player.identityCard.isWitch());
                }
                case "accuse" -> {
                    Player targetedPlayer = choosePlayer(player);
                    askPlayerForAction(targetedPlayer);
                    player.accuse(targetedPlayer);
                }
                case "use card" -> {
                    boolean cardUsedSuccessfully;
                    do {
                        cardUsedSuccessfully = player.revealRumourCard(chooseCard(player));
                    } while (!cardUsedSuccessfully);
                }
            }
        } else {
            ((AI) (player)).play();
        }
    }

    /**
     * Distribute Rumour cards
     * This method distribute the Rumour cards at the start of a round based on the number of players
     */
    private void distributeRumourCards() {
        int nbOfExcessCards = CardName.values().length % identityCards.size();

        //Take care of excess cards
        if (nbOfExcessCards > 0) {
            for (int i = 0; i < nbOfExcessCards; i++) {
                int index = GameController.randomInInterval(0, GameController.gameController.deck.size() - 1);
                discardPile.add(GameController.gameController.deck.remove(index));
            }
        }

        //Distribute the rest equally
        int numberOfCardsPerPlayer = CardName.values().length / identityCards.size();
        for (Player player : GameController.gameController.players) {
            for (int i = 0; i < numberOfCardsPerPlayer; i++) {
                int index = GameController.randomInInterval(0, GameController.gameController.deck.size() - 1);
                player.addCardToHand(GameController.gameController.deck.remove(index));
            }
        }
    }

    /**
     * Ask players for their chosen identity
     * This method will call the selectIdentity() method to prompt players to choose a role for the round
     */
    private void askPlayersForIdentity() {
        for (IdentityCard identityCard : identityCards) {
            if (!(identityCard.player instanceof AI)) {
                int identity = view.promptForPlayerIdentity(identityCard.player.getName());
                identityCard.setWitch(identity > 0);
                //                identityCard.player.selectIdentity();
            } else {
                ((AI) (identityCard.player)).selectIdentity();
            }

        }
    }

    /**
     * Select the first player
     * This method will only be used on the first round to select a random player to start
     */
    private void selectFirstPlayer() {
        List<Player> playerList = GameController.gameController.players;
        RoundController.currentPlayer = playerList.get(GameController.randomInInterval(0, playerList.size() - 1));
    }

    /**
     * Set up the round
     * This method will do everything necessary to set up a round (select 1st player, create identity cards, distribute Rumour cards, ask players for identity)
     */
    public void startRound() {
        numberOfRound++;
        view.showStartOfRound(numberOfRound);

        if (currentPlayer == null) selectFirstPlayer();
        //Fill up the list of active players at the start
        for (Player player : GameController.gameController.players) {
            IdentityCard playerIdentityCard = new IdentityCard(player);
            identityCards.add(playerIdentityCard);
            player.identityCard = playerIdentityCard;
        }
        numberOfNotRevealedPlayers = identityCards.size();

        distributeRumourCards();
        askPlayersForIdentity();
        roundState = RoundState.PLAYING_ROUND;
    }

    /**
     * Round playing loop
     * This method will prompt the current player for action, then set the current player to the next and loop while there is more than 1 not revealed player
     */
    private void playRound() {
        do {
            askPlayerForAction(RoundController.getCurrentPlayer());
            RoundController.currentPlayer = nextPlayer;
            //TODO Playing loop, currently there is a risk of not setting next player
        } while (numberOfNotRevealedPlayers > 1);
        roundState = RoundState.ENDING_ROUND;
    }

    /**
     * Wrap up the round
     * This method will do everything necessary to wrap up a round (reveal last player and give him points, gather all cards)
     */
    private void endRound() {
        //We search the last not revealed player, reveal is identity and give him points
        for (IdentityCard identityCard : identityCards) {
            if (!identityCard.isIdentityRevealed()) {
                Player winner = identityCard.player;
                //Reveal player identity and give points
                winner.revealIdentity();
                view.showRoundWinner(winner.getName());
                view.showPlayerIdentity(winner.getName(), identityCard.isWitch());
                winner.addToScore(identityCard.isWitch() ? 2 : 1);
                //Set him to be first for the next round
                currentPlayer = winner;
                break;
            }
        }
        //Gather all players cards
        for (Player player : GameController.gameController.players) {
            int startingNumberOfCard = player.hand.size();
            for (int i = 0; i < startingNumberOfCard; i++) {
                RumourCard removedCard = player.hand.get(0).rumourCard;
                player.removeCardFromHand(removedCard);
                GameController.gameController.deck.add(removedCard);
            }
        }
        //Gather the discarded cards
        int startingNumberOfCard = this.discardPile.size();
        for (int i = 0; i < startingNumberOfCard; i++) {
            RumourCard removedCard = this.discardPile.get(0);
            this.discardPile.remove(removedCard);
            GameController.gameController.deck.add(removedCard);
        }
        roundState = RoundState.ROUND_ENDED;
    }

    public void run() {
        while (roundState != RoundState.ROUND_ENDED) {
            switch (roundState) {
                case INITIALIZING_ROUND -> startRound();
                case PLAYING_ROUND -> playRound();
                case ENDING_ROUND -> endRound();
            }
        }
    }

}
