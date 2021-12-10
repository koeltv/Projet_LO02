package com.controller;

import com.model.card.RumourCard;
import com.model.game.CardState;
import com.model.game.IdentityCard;
import com.model.game.Round;
import com.model.player.AI;
import com.model.player.Player;
import com.view.ActiveView;

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
    private static RoundController instance;

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

        this.round = new Round(gameController.deck, gameController.players);

        RoundController.instance = this;
    }

    /**
     * Gets round controller.
     * Return the single available instance of round controller (Singleton).
     *
     * @return the round controller
     */
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
     * Ask the current player for his next action.
     * This method will call the play method of the current player.
     *
     * @param player          the player
     * @param possibleActions the possible actions
     */
    public void askPlayerForAction(Player player, List<PlayerAction> possibleActions) {
        //Ask the player to choose his next action
        PlayerAction action;
    	do {
        	action = player instanceof AI ai ?
                    ai.play(possibleActions) :
                    view.promptForAction(player.getName(), possibleActions);
        	applyPlayerAction(player, action);
        } while (action == PlayerAction.VIEW_HAND || action == PlayerAction.VIEW_REVEALED || action == PlayerAction.VIEW_DISCARD_PILE);
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

                round.revealIdentity(player);
            }
            case ACCUSE -> {
                List<Player> players = round.getNotRevealedPlayers(player);
                //In case an effect forbid a player from accusing a certain other player
                List<Player> notSelectablePlayersOfPlayer = round.getNotSelectablePlayers(player);
                if (players.size() > 1 && notSelectablePlayersOfPlayer != null) {
                    players.removeIf(notSelectablePlayersOfPlayer::contains);
                    notSelectablePlayersOfPlayer.clear();
                }

                Player targetedPlayer = choosePlayer(player, players);
                view.showPlayerAction(player.getName(), targetedPlayer.getName());

                passToNextPlayer(player, targetedPlayer);
                askPlayerForAction(targetedPlayer, round.getStandardActions(targetedPlayer));

                //If the player is a witch, its identity card is deleted, so if null the player was revealed as a witch
                if (round.getPlayerIdentityCard(targetedPlayer) == null) player.addToScore(1);
            }
            case USE_CARD -> {
                boolean cardUsedSuccessfully;
                do {
                    RumourCard chosenRumourCard = chooseCard(player, round.getUsableCards(player, player.getSelectableCardsFromHand()));
                    view.showPlayerAction(player.getName(), chosenRumourCard.getCardName());
                    cardUsedSuccessfully = player.revealRumourCard(chosenRumourCard);
                } while (!cardUsedSuccessfully);
            }
            case VIEW_HAND -> {
                view.showCardList("Hand", player.getSelectableCardsFromHand().stream().map(RumourCard::toString).toList());
                round.setNextPlayer(player);
            }
            case VIEW_REVEALED -> {
                for (IdentityCard card : round.getIdentityCards()) {
                    view.showCardList("Revealed cards of " + card.player.getName(), card.player.getRevealedCards().stream().map(RumourCard::toString).toList());
                    round.setNextPlayer(player);
                }
            }
            case VIEW_DISCARD_PILE -> {
                view.showCardList("Discard Pile", round.getDiscardPile().stream().map(RumourCard::toString).toList());
                round.setNextPlayer(player);
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
        if (player != nextPlayer) view.promptForPlayerSwitch(nextPlayer.getName());
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
     * Set up the round.
     * This method will do everything necessary to set up a round (select 1st player, create identity cards, distribute Rumour cards, ask players for identity).
     */
    private void startRound() {
        view.showStartOfRound(Round.getNumberOfRound());

        round.distributeRumourCards();
        askPlayersForIdentity();
    }

    /**
     * Round playing loop.
     * This method will prompt the current player for action, then set the current player to the next and loop while there is more than 1 not revealed player.
     */
    private void playRound() {
        do {
            Player currentPlayer = Round.getCurrentPlayer();
            askPlayerForAction(currentPlayer, round.getStandardActions(currentPlayer));
            round.actualiseCurrentPlayer();
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
        round.setNextPlayer(identityCard.player);
        round.actualiseCurrentPlayer();

        //Gather all players cards
        gameController.players.forEach(player -> {
            for (Iterator<CardState> iterator = player.getHand().iterator(); iterator.hasNext(); ) {
                gameController.deck.returnCardToDeck(player.removeCardFromHand(player.getHand().get(0).rumourCard));
            }
        });
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
