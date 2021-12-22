package com.view;

import com.controller.PlayerAction;
import com.model.card.RumourCard;

import java.util.List;

/**
 * The interface Active View.
 * 
 * Gives all the methods related to the active view. All Controller should have 1 active view. The active view is the view that take care of player entries.
 */
public interface ActiveView extends View {

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
     * @param playerName  the choosing player name
     * @param playerNames the player names
     * @return the chosen player index
     */
    int promptForPlayerChoice(String playerName, List<String> playerNames);

    /**
     * Prompt for card choice.
     *
     * @param playerName  the choosing player name
     * @param rumourCards the rumour card descriptions
     * @return the chosen card index
     */
    int promptForCardChoice(String playerName, List<RumourCard> rumourCards);

    /**
     * Prompt for blind card choice.
     *
     *
     * @param playerName the choosing player name
     * @param listSize the number of cards to choose from
     * @return the chosen card index
     */
    int promptForCardChoice(String playerName, int listSize);

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

    /**
     * Prompt for player switch.
     * Prompt the current player to pass to the next player which name is displayed.
     *
     * @param name the name
     */
    void promptForPlayerSwitch(String name);
}
