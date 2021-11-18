package com.model.player;

import com.model.card.RumourCard;

import java.util.List;

/**
 * The interface Strategy.
 */
public interface Strategy {
    /**
     * Use the strategy.
     *
     * @param possibleActions the possible actions
     * @return the player action
     */
    PlayerAction use(List<PlayerAction> possibleActions);

    /**
     * Select identity.
     */
    void selectIdentity();

    /**
     * Select target player.
     *
     * @param players the players
     * @return the player
     */
    Player selectPlayer(List<Player> players);

    /**
     * Select a rumour card.
     *
     * @param rumourCards the rumour cards
     * @return the rumour card
     */
    RumourCard selectCard(List<RumourCard> rumourCards);

    /**
     * Select a rumour card blindly.
     *
     * @param listSize the size of the list
     * @return the chosen index
     */
    int selectCard(int listSize);
}
