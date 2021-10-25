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
     *
     * @param name the AI name
     */
    public AI(final String name) {
        super(name);
        this.strategy = switch (GameController.randomInInterval(0, 1)) {
            case 0 -> new Agressive();
            default -> new Defensive();
        };
    }

    /**
     * Use the AI strategy to select action.
     *
     * @param possibleActions the possible actions
     * @return the player action
     */
    public PlayerAction play(List<PlayerAction> possibleActions) {
        return strategy.use(this, possibleActions);
    }

    /**
     * Use the AI strategy to select identity.
     */
    public void selectIdentity() {
        strategy.selectIdentity(this);
    }

    /**
     * Select the target player.
     *
     * @param players the players
     * @return the chosen player
     */
    public Player selectPlayer(List<Player> players) {
        return strategy.selectPlayer(players);
    }

    /**
     * Select card rumour card.
     *
     * @param rumourCards the rumour cards
     * @return the chosen rumour card
     */
    public RumourCard selectCard(List<RumourCard> rumourCards) {
        return strategy.selectCard(rumourCards);
    }
}
