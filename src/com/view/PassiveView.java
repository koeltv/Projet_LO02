package com.view;

import com.controller.PlayerAction;
import com.model.card.RumourCard;

import java.util.List;

/**
 * The interface Passive View.
 * All Controller can have as many passive view as wanted. A passive view display the game state but doesn't take any player entry.
 */
public interface PassiveView extends View {
    /**
     * Wait for player name.
     *
     * @param playerIndex the player index
     */
    void waitForPlayerName(int playerIndex);

    /**
     * Wait for new game.
     */
    void waitForNewGame();

    /**
     * Wait for player choice.
     *
     * @param playerNames the player names
     */
    void waitForPlayerChoice(List<String> playerNames);

    /**
     * Wait for card choice.
     *
     * @param rumourCards the rumour card names
     */
    void waitForCardChoice(List<RumourCard> rumourCards);

    /**
     * Wait for repartition.
     */
    void waitForRepartition();

    /**
     * Wait for player identity.
     *
     * @param name the name
     */
    void waitForPlayerIdentity(String name);

    /**
     * Wait for player action.
     *
     * @param playerName      the player name
     * @param possibleActions the possible actions
     */
    void waitForAction(String playerName, List<PlayerAction> possibleActions);

    /**
     * Wait for player switch.
     * Wait for the current player to pass to the next player which name is displayed.
     *
     * @param name the name
     */
    void waitForPlayerSwitch(String name);
}
