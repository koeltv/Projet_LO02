package com.model.player;

import com.controller.PlayerAction;

import java.util.List;

import static com.util.GameUtil.randomInInterval;

/**
 * The type Card user.
 */
public class CardUser extends Strategy {
    /**
     * Instantiates a new CardUser.
     *
     * @param ai the linked AI
     */
    CardUser(AI ai) {
        super(ai);
    }

    @Override
    public PlayerAction use(List<PlayerAction> possibleActions) {
        if (possibleActions.contains(PlayerAction.USE_CARD)) {
            return PlayerAction.USE_CARD;
        } else if (possibleActions.contains(PlayerAction.ACCUSE)) {
            return PlayerAction.ACCUSE;
        } else {
            return possibleActions.get(randomInInterval(possibleActions.size() - 1));
        }
    }
}
