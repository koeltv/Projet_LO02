package com.model.player;

import com.controller.GameController;
import com.model.card.RumourCard;

import java.util.List;

/**
 * The type AI.
 */
public class AI extends Player {
    /**
     * The Strategy.
     */
    private final Strategy strategy;

    /**
     * Instantiates a new AI.
     *
     * @param name the AI name
     */
    public AI(final String name) {
        super(name);
        this.strategy = GameController.randomInInterval(1) > 0 ? new Agressive(this) : new Defensive(this);
    }

    /**
     * Use the AI strategy to select action.
     *
     * @param possibleActions the possible actions
     * @return the player action
     */
    public PlayerAction play(List<PlayerAction> possibleActions) {
        return strategy.use(possibleActions);
    }

    /**
     * Use the AI strategy to select identity.
     */
    public void selectIdentity() {
        strategy.selectIdentity();
    }

    /**
     * Use the AI strategy to select the target player.
     *
     * @param players the players
     * @return the chosen player
     */
    public Player selectPlayer(List<Player> players) {
        return strategy.selectPlayer(players);
    }

    /**
     * Use the AI strategy to select a rumour card.
     *
     * @param rumourCards the rumour cards
     * @return the chosen rumour card
     */
    public RumourCard selectCard(List<RumourCard> rumourCards) {
        return strategy.selectCard(rumourCards);
    }

    /**
     * Use the AI strategy to select a rumour card blindly.
     *
     * @param listSize the size of the list
     * @return the chosen index
     */
    public int selectCard(int listSize) {
        return strategy.selectCard(listSize);
    }
}
