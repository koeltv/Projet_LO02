package com.model.player;

import com.controller.PlayerAction;

import java.util.List;

import static com.util.GameUtil.randomInInterval;

/**
 * The type Defensive.
 * 
 * Gives all the methods related to the Defensive strategy.
 */
public class Defensive extends Strategy {
    /**
     * Instantiates a new Defensive.
     *
     * @param ai the linked AI
     * @see com.model.player.AI
     */
    Defensive(AI ai) {
        super(ai);
    }

    @Override
    public PlayerAction use(List<PlayerAction> possibleActions) {
        if (possibleActions.contains(PlayerAction.ACCUSE)) {
            return PlayerAction.ACCUSE;
        } else if (possibleActions.contains(PlayerAction.USE_CARD)) {
            return PlayerAction.USE_CARD;
        } else {
            return possibleActions.get(randomInInterval(possibleActions.size() - 1));
        }
    }

}
