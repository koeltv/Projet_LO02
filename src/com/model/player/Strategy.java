package com.model.player;

import com.controller.PlayerAction;
import com.model.card.RumourCard;

import java.util.List;

/**
 * The interface Strategy.
 */
public abstract class Strategy {
    /**
     * The linked AI.
     */
    final AI ai;

    /**
     * Instantiates a new Strategy.
     *
     * @param ai the linked AI
     */
    Strategy(AI ai) {
        this.ai = ai;
    }

    /**
     * Use the strategy.
     *
     * @param possibleActions the possible actions
     * @return the player action
     */
    abstract PlayerAction use(List<PlayerAction> possibleActions);

    /**
     * Select identity.
     */
    abstract void selectIdentity();

    /**
     * Select target player.
     *
     * @param players the players
     * @return the player
     */
    abstract Player selectPlayer(List<Player> players);

    /**
     * Select a rumour card.
     *
     * @param rumourCards the rumour cards
     * @return the rumour card
     */
    abstract RumourCard selectCard(List<RumourCard> rumourCards);

    /**
     * Select a rumour card blindly.
     *
     * @param listSize the size of the list
     * @return the chosen index
     */
    abstract int selectCard(int listSize);
}
