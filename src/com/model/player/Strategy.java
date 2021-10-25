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
     * @param ai the AI using the strategy
     */
    PlayerAction use(AI ai, List<PlayerAction> possibleActions);

    /**
     * Select identity.
     *
     * @param ai the AI using the strategy
     */
    void selectIdentity(AI ai);

    Player selectPlayer(List<Player> players);

    RumourCard selectCard(List<RumourCard> rumourCards);
}
