package com.view;

import com.controller.GameController;
import com.model.card.CardName;
import com.model.player.PlayerAction;

import java.util.List;

/**
 * The interface View.
 */
public interface View {
    /**
     * Sets controller.
     *
     * @param gameController the game controller
     */
    void setController(GameController gameController);

    /**
     * Prompt for player name.
     *
     * @param playerIndex the player index
     */
    void promptForPlayerName(int playerIndex);

    /**
     * Prompt for new game.
     *
     * @return the boolean
     */
    boolean promptForNewGame();

    /**
     * Prompt for player choice.
     *
     * @param playerNames the player names
     * @return the chosen player index
     */
    int promptForPlayerChoice(List<String> playerNames);

    /**
     * Prompt for card choice.
     *
     * @param rumourCardNames the rumour card names
     * @return the chosen card index
     */
    int promptForCardChoice(List<CardName> rumourCardNames);

    /**
     * Prompt for repartition.
     */
    void promptForRepartition();

    /**
     * Show game winner.
     *
     * @param name          the name of the winner
     * @param numberOfRound the number of round
     */
    void showGameWinner(String name, int numberOfRound);

    /**
     * Show round winner.
     *
     * @param name the name
     */
    void showRoundWinner(String name);

    /**
     * Show start of round.
     *
     * @param numberOfRound the number of the round
     */
    void showStartOfRound(int numberOfRound);

    /**
     * Prompt for player identity.
     *
     * @param name the name
     * @return the chosen identity (0 or less for villager, 1 or more for witch)
     */
    int promptForPlayerIdentity(String name);

    /**
     * Prompt for player action.
     *
     * @param playerName      the player name
     * @param possibleActions the possible actions
     * @return the chosen player action
     */
    PlayerAction promptForAction(String playerName, List<PlayerAction> possibleActions);

    /**
     * Show player identity.
     *
     * @param name  the name
     * @param witch the identity
     */
    void showPlayerIdentity(String name, boolean witch);

    /**
     * Show current player.
     *
     * @param name the player name
     */
    void showCurrentPlayer(String name);

    /**
     * Show player action (revealing identity).
     *
     * @param name the player name
     */
    void showPlayerAction(String name);

    /**
     * Show player action (accusing).
     *
     * @param name               the player name
     * @param targetedPlayerName the targeted player name
     */
    void showPlayerAction(String name, String targetedPlayerName);

    /**
     * Show player action (using card).
     *
     * @param name           the player name
     * @param chosenCardName the chosen card name
     */
    void showPlayerAction(String name, CardName chosenCardName);
}
