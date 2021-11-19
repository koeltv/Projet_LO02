package com.view;

import com.controller.PlayerAction;
import com.model.card.RumourCard;

import java.util.List;

/**
 * The interface Active View.
 * All Controller should have 1 active view. The active view is the view that take care of player entries.
 */
public interface ActiveView extends View { // TODO: 19/11/2021 Make something to wait between players

    /**
     * Prompt for player name.
     *
     * @param playerIndex the player index
     * @return chosen player name
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
     * @param rumourCards the rumour card descriptions
     * @return the chosen card index
     */
    int promptForCardChoice(List<RumourCard> rumourCards);

    /**
     * Prompt for blind card choice.
     *
     * @param listSize the number of cards to choose from
     * @return the chosen card index
     */
    int promptForCardChoice(int listSize);

    /**
     * Prompt for repartition.
     *
     * @return chosen player/AI repartition
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
