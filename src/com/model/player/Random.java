package com.model.player;

import com.controller.PlayerAction;
import com.model.game.Round;

import java.util.List;

/**
 * The type Random.
 */
public class Random extends Strategy {
    /**
     * Instantiates a new Random.
     *
     * @param ai the linked AI
     */
    Random(AI ai) {
        super(ai);
    }

    @Override
    public PlayerAction use(List<PlayerAction> possibleActions) {
        if (Round.getCurrentPlayer() == ai) {
            return PlayerAction.ACCUSE;
        } else {
            return PlayerAction.REVEAL_IDENTITY;
        }
    }
}
