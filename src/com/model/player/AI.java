package com.model.player;

import com.controller.GameController;
import com.model.card.RumourCard;

import java.util.List;

/**
 * The type AI.
 */
public class AI extends Player {
    private final Strategy strategy;

    /**
     * Instantiates a new AI.
     */
    public AI() {
        super(GameController.randomAIName());
        this.strategy = switch (GameController.randomInInterval(0, 1)) {
            case 0 -> new Agressive();
            default -> new Defensive();
        };
    }

    /**
     * Use the AI strategy to select action
     */
    public PlayerAction play(List<PlayerAction> possibleActions) {
        return strategy.use(this, possibleActions);
    }

    /**
     * Use the AI strategy to select identity
     */
    public void selectIdentity() {
        strategy.selectIdentity(this);
    }

    public Player selectPlayer(List<Player> players) {
        return strategy.selectPlayer(players);
    }

    public RumourCard selectCard(List<RumourCard> rumourCards) {
        return strategy.selectCard(rumourCards);
    }
}
