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
import java.util.LinkedList;
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

    private final List<PlayerController> playerControllers;

    private static RoundController instance;

    /**
     * The View.
     */
    private final ActiveView view;

    private final Round round;

    ///////////////////////////////////////////////////////////////////////////
    // Constructor, getters and setters
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Instantiates a new Round controller.
     *
     * @param gameController the game controller
     * @param view           the view
     */
    RoundController(GameController gameController, ActiveView view) {
        this.view = view;
        this.gameController = gameController;
        this.playerControllers = gameController.players;

        this.round = new Round();
        instance = this;
    }

    public static RoundController getInstance() {
        return instance;
    }

    /**
     * Reset static attributes.
     */
    public static void reset() {
        Round.reset();
    }

    public static int getNumberOfRound() {
        return Round.getNumberOfRound();
    }

    public void setNextPlayer(Player nextPlayer) {
        round.setNextPlayer(nextPlayer);
    }

    public Player getNextPlayer() {
        return round.getNextPlayer();
    }

    public LinkedList<RumourCard> getDiscardPile() {
        return round.getDiscardPile();
    }

    public List<IdentityCard> getIdentityCards() {
        return round.getIdentityCards();
    }

    public IdentityCard getPlayerIdentityCard(Player targetedPlayer) {
        return round.getPlayerIdentityCard(targetedPlayer);
    }

    public List<RumourCard> getUsableCards(Player player, List<RumourCard> cards) {
        return round.getUsableCards(player, cards);
    }

    public List<Player> getNotRevealedPlayers(Player player) {
        return round.getNotRevealedPlayers(player);
    }

    public List<Player> getNotSelectablePlayers(Player player) {
        return round.getNotSelectablePlayers(player);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Controller methods
    ///////////////////////////////////////////////////////////////////////////

    public PlayerController getPlayerController(Player player) {
        return playerControllers.stream()
                .filter(playerController -> playerController.getPlayer() == player)
                .findFirst()
                .orElseThrow();
    }

    /**
     * Reveal identity.
     *
     * @param toReveal player to reveal
     */
    void revealIdentity(Player toReveal) {
        IdentityCard identityCard = Round.getInstance().getPlayerIdentityCard(toReveal);
        identityCard.setIdentityRevealed(true);
        if (identityCard.isWitch()) {
            //If a player is revealed as a witch, we exclude him from the round
            getIdentityCards().removeIf(card -> card.player == identityCard.player);
            setNextPlayer(Round.getCurrentPlayer());
        } else {
            setNextPlayer(identityCard.player);
        }
    }

    /**
     * Gets standard actions.
     *
     * @param player the player
     * @return the standard actions
     */
    List<PlayerAction> getStandardActions(Player player) {
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
     * If the next player isn't the same player and both players aren't AIs, demand to pass to next player.
     *
     * @param player     the player currently playing
     * @param nextPlayer the next player
     */
    void passToNextPlayer(Player player, Player nextPlayer) {
        boolean atLeast2RealPlayers = round.getIdentityCards().stream().filter(identityCard -> !(identityCard.player instanceof AI)).count() > 1;
        if (!(player == nextPlayer || nextPlayer instanceof AI) && atLeast2RealPlayers) {
            view.promptForPlayerSwitch(nextPlayer.getName());
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
            if (previousWasPlayer && !(identityCard.player instanceof AI))
                view.promptForPlayerSwitch(identityCard.player.getName());
            getPlayerController(identityCard.player).chooseIdentity();
            previousWasPlayer = !(identityCard.player instanceof AI);
        }
    }

    /**
     * Select the first player.
     * This method will only be used on the first round to select a random player to start.
     */
    private void selectFirstPlayer() {
        List<Player> playerList = gameController.players.stream()
                .map(PlayerController::getPlayer)
                .collect(Collectors.toList());
        Round.setCurrentPlayer(playerList.get(randomInInterval(playerList.size() - 1)));
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
            PlayerController playerController = playerControllers.stream()
                    .filter(playerController1 -> playerController1.getPlayer() == Round.getCurrentPlayer())
                    .findFirst()
                    .orElseThrow();
            playerController.askPlayerForAction(getStandardActions(Round.getCurrentPlayer()));
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
        for (PlayerController player : gameController.players) {
            for (Iterator<CardState> iterator = player.getHand().iterator(); iterator.hasNext(); ) {
                gameController.deck.returnCardToDeck(player.removeCardFromHand(player.getHand().get(0).rumourCard));
            }
        }
        //Gather the discarded cards
        for (Iterator<RumourCard> iterator = round.getDiscardPile().iterator(); iterator.hasNext(); ) {
            gameController.deck.returnCardToDeck(round.getDiscardPile().poll());
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
