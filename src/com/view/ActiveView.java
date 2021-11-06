package com.view;

import com.model.card.CardName;
import com.model.player.PlayerAction;

import java.util.List;

/**
 * The interface Active View.
 * All Controller should have 1 active view. The active view is the view that take care of player entries.
 */
public interface ActiveView extends View {

    /**
     * Prompt for player name.
     *
     * @param playerIndex the player index
     */
    String promptForPlayerName(int playerIndex);

    /**
     * Prompt for new game.
     *
     * @return the boolean
     */
    String promptForNewGame();

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
    int[] promptForRepartition();

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
}
