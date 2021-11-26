package com.controller;

import com.model.card.CardName;
import com.model.card.RumourCard;
import com.model.game.CardState;
import com.model.game.IdentityCard;
import com.model.game.Round;
import com.model.player.AI;
import com.model.player.Player;
import com.view.ActiveView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static com.util.GameUtil.randomInInterval;

/**
 * The type Round.
 * Round is the class that contains all methods to supervise a round. It is a singleton.
 */
public class RoundController {
    /**
     * The Game controller.
     */
    private final GameController gameController;

    /**
     * The View.
     */
    private final ActiveView view;

    private final Round round;

    /**
     * Instantiates a new Round controller.
     *
     * @param gameController the game controller
     * @param view           the view
     */
    RoundController(GameController gameController, ActiveView view) {
        this.view = view;
        this.gameController = gameController;

        this.round = Round.getInstance();
    }

    /**
     * Ask the player to choose a card.
     *
     * @param player         the player
     * @param rumourCardList the list of card to select from
     * @return chosen card
     */
    public RumourCard chooseCard(Player player, List<RumourCard> rumourCardList) {
        return player instanceof AI ai ?
                ai.selectCard(rumourCardList) :
                rumourCardList.get(view.promptForCardChoice(rumourCardList));
    }

    /**
     * Choose card blindly rumour card.
     *
     * @param player         the player
     * @param rumourCardList the rumour card list
     * @return the rumour card
     */
    public RumourCard chooseCardBlindly(Player player, List<RumourCard> rumourCardList) {
        int index = player instanceof AI ai ?
                ai.selectCard(rumourCardList.size()) :
                view.promptForCardChoice(rumourCardList.size());
        return rumourCardList.get(index);
    }

    /**
     * Ask a player to choose another player.
     *
     * @param choosingPlayer the choosing player
     * @param playerList     the player list to choose from
     * @return chosen player
     */
    public Player choosePlayer(Player choosingPlayer, List<Player> playerList) {
        return choosingPlayer instanceof AI ai ?
                ai.selectPlayer(playerList) :
                playerList.get(view.promptForPlayerChoice(playerList.stream().map(Player::getName).collect(Collectors.toList())));
    }

    /**
     * Gets standard actions.
     *
     * @param player the player
     * @return the standard actions
     */
    private List<PlayerAction> getStandardActions(Player player) {
        List<PlayerAction> possibleActions = new ArrayList<>();
        if (player == Round.getCurrentPlayer()) possibleActions.add(PlayerAction.ACCUSE);
        else possibleActions.add(PlayerAction.REVEAL_IDENTITY);
        //If the player has at least 1 usable card, we add the possibility to use it
        List<RumourCard> hand = player.getSelectableCardsFromHand();
        if (hand.size() > 0 && round.getUsableCards(player, hand).size() > 0)
            possibleActions.add(PlayerAction.USE_CARD);
        return possibleActions;
    }

    /**
     * Ask the current player for his next action.
     * This method will call the play method of the current player.
     *
     * @param player          the player
     * @param possibleActions the possible actions
     */
    public void askPlayerForAction(Player player, List<PlayerAction> possibleActions) {
        //Ask the player to choose his next action
        applyPlayerAction(player, player instanceof AI ai ?
                ai.play(possibleActions) :
                view.promptForAction(player.getName(), possibleActions)
        );
    }

    /**
     * Apply player action.
     *
     * @param player the player
     * @param action the action
     */
    public void applyPlayerAction(Player player, PlayerAction action) {
        switch (action) {
            case DISCARD -> {
                RumourCard chosenCard = chooseCard(player, player.getSelectableCardsFromHand());
                round.getDiscardPile().add(player.removeCardFromHand(chosenCard));
            }
            case LOOK_AT_IDENTITY -> view.showPlayerIdentity(player.getName(), round.getPlayerIdentityCard(player).isWitch());
            case REVEAL_IDENTITY -> {
                view.showPlayerAction(player.getName());
                view.showPlayerIdentity(player.getName(), round.getPlayerIdentityCard(player).isWitch());

                revealIdentity(player);
            }
            case ACCUSE -> {
                List<Player> players = round.getNotRevealedPlayers(player);
                //In case an effect forbid a player from accusing a certain other player
                List<Player> notSelectablePlayers = round.getNotSelectablePlayers(player);
                if (players.size() > 1 && notSelectablePlayers != null) {
                    players.removeIf(notSelectablePlayers::contains);
                    notSelectablePlayers.clear();
                }

                Player targetedPlayer = choosePlayer(player, players);
                view.showPlayerAction(player.getName(), targetedPlayer.getName());

                passToNextPlayer(player, targetedPlayer);
                askPlayerForAction(targetedPlayer, getStandardActions(targetedPlayer));

                //If the player is a witch, its identity card is deleted, so if null the player was revealed as a witch
                if (round.getPlayerIdentityCard(targetedPlayer) == null) {
                    player.addToScore(1);
                }
            }
            case USE_CARD -> {
                boolean cardUsedSuccessfully;
                do {
                    RumourCard chosenRumourCard = chooseCard(player, round.getUsableCards(player, player.getSelectableCardsFromHand()));
                    view.showPlayerAction(player.getName(), chosenRumourCard.getCardName());
                    cardUsedSuccessfully = player.revealRumourCard(chosenRumourCard);
                } while (!cardUsedSuccessfully);
            }
        }
        if (action != PlayerAction.ACCUSE) passToNextPlayer(player, round.getNextPlayer());
    }

    /**
     * If the next player isn't the same player and both players aren't AIs, demand to pass to next player.
     *
     * @param player     the player currently playing
     * @param nextPlayer the next player
     */
    private void passToNextPlayer(Player player, Player nextPlayer) {
        boolean atLeast2RealPlayers = round.getIdentityCards().stream().filter(identityCard -> !(identityCard.player instanceof AI)).count() > 1;
        if (!(player == nextPlayer || nextPlayer instanceof AI) && atLeast2RealPlayers) {
            view.promptForPlayerSwitch(nextPlayer.getName());
        }
    }

    /**
     * Reveal identity.
     *
     * @param player the player
     */
    private void revealIdentity(Player player) {
        IdentityCard playerIdentityCard = round.getPlayerIdentityCard(player);
        playerIdentityCard.setIdentityRevealed(true);
        if (playerIdentityCard.isWitch()) {
            //If a player is revealed as a witch, we exclude him from the round
            round.getIdentityCards().removeIf(identityCard -> identityCard.player == player);
            round.setNextPlayer(Round.getCurrentPlayer());
        } else {
            round.setNextPlayer(player);
        }
    }

    /**
     * Distribute Rumour cards.
     * This method distribute the Rumour cards at the start of a round based on the number of players.
     */
    private void distributeRumourCards() {
        gameController.deck.shuffle();
        int nbOfExcessCards = CardName.values().length % round.getIdentityCards().size();

        //Take care of excess cards
        for (int i = 0; i < nbOfExcessCards; i++) {
            round.getDiscardPile().add(gameController.deck.removeTopCard());
        }

        //Distribute the rest equally
        int numberOfCardsPerPlayer = CardName.values().length / round.getIdentityCards().size();
        for (PlayerController player : gameController.players) {
            for (int i = 0; i < numberOfCardsPerPlayer; i++) {
                player.addCardToHand(gameController.deck.removeTopCard());
            }
        }
    }

    /**
     * Ask players for their chosen identity.
     * This method will call the selectIdentity() method to prompt players to choose a role for the round.
     */
    private void askPlayersForIdentity() {
        boolean previousWasPlayer = false;
        for (IdentityCard identityCard : round.getIdentityCards()) {
            if (identityCard.player instanceof AI ai) {
                ai.selectIdentity();
            } else {
                if (previousWasPlayer) view.promptForPlayerSwitch(identityCard.player.getName());

                int identity = view.promptForPlayerIdentity(identityCard.player.getName());
                identityCard.setWitch(identity > 0);
                previousWasPlayer = true;
            }
        }
    }

    /**
     * Select the first player.
     * This method will only be used on the first round to select a random player to start.
     */
    private void selectFirstPlayer() {
        List<PlayerController> playerList = gameController.players;
        Round.setCurrentPlayer(playerList.get(randomInInterval(playerList.size() - 1)).getPlayer());
    }

    /**
     * Set up the round.
     * This method will do everything necessary to set up a round (select 1st player, create identity cards, distribute Rumour cards, ask players for identity).
     */
    private void startRound() {
        Round.addNumberOfRound();
        view.showStartOfRound(Round.getNumberOfRound());

        if (Round.getCurrentPlayer() == null) selectFirstPlayer();
        //Fill up the list of active players at the start
        gameController.players.forEach(player -> round.getIdentityCards().add(new IdentityCard(player.getPlayer())));

        distributeRumourCards();
        askPlayersForIdentity();
    }

    /**
     * Round playing loop.
     * This method will prompt the current player for action, then set the current player to the next and loop while there is more than 1 not revealed player.
     */
    private void playRound() {
        do {
            askPlayerForAction(Round.getCurrentPlayer(), getStandardActions(Round.getCurrentPlayer()));
            Round.setCurrentPlayer(round.getNextPlayer());
        } while (round.getNumberOfNotRevealedPlayers() > 1);
    }

    /**
     * Wrap up the round.
     * This method will do everything necessary to wrap up a round (reveal last player and give him points, gather all cards).
     */
    private void endRound() {
        //We search the last not revealed player, reveal is identity and give him points
        IdentityCard identityCard = round.getIdentityCards().stream()
                .filter(card -> !card.isIdentityRevealed())
                .findFirst()
                .orElseThrow();
        view.showRoundWinner(identityCard.player.getName());
        view.showPlayerIdentity(identityCard.player.getName(), identityCard.isWitch());
        identityCard.player.addToScore(identityCard.isWitch() ? 2 : 1);
        //Set him to be first for the next round
        Round.setCurrentPlayer(identityCard.player);

        //Gather all players cards
        gameController.players.forEach(player -> {
            for (Iterator<CardState> iterator = player.getHand().iterator(); iterator.hasNext(); ) {
                gameController.deck.returnCardToDeck(player.removeCardFromHand(player.getHand().get(0).rumourCard));
            }
        });
        //Gather the discarded cards
        for (Iterator<RumourCard> iterator = round.getDiscardPile().iterator(); iterator.hasNext(); ) {
            gameController.deck.returnCardToDeck(round.getDiscardPile().get(0));
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
