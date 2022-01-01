package com.model.player;

import com.controller.PlayerAction;
import com.model.card.RumourCard;

import java.util.List;

import static com.util.GameUtil.randomInInterval;

/**
 * The type AI.
 * 
 * Gives all the methods related to the AI.
 */
public class AI extends Player {
    
	/**
     * The Strategy.
     * 
     * @see com.model.player.Strategy
     */
    private final Strategy strategy;

    /**
     * Instantiates a new AI.
     *
     * @param name the AI name
     * @see com.model.player.CardUser
     */
    public AI(final String name) {
        super(name);
        this.strategy = switch (randomInInterval(3)) {
            case 0 -> new CardUser(this);
            case 1 -> new Agressive(this);
            case 2 -> new Defensive(this);
            default -> new Random(this);
        };
    }

    /**
     * Use the AI strategy to select action.
     *
     * @param possibleActions the possible actions
     * @return the player action
     * @see com.controller.PlayerAction
     * @see com.model.player.Strategy
     */
    public PlayerAction play(List<PlayerAction> possibleActions) {
        return strategy.use(possibleActions);
    }

    /**
     * Use the AI strategy to select identity.
     * 
     * @see com.model.player.Strategy
     */
    public void selectIdentity() {
        strategy.selectIdentity();
    }

    /**
     * Use the AI strategy to select the target player.
     *
     * @param players the players
     * @return the chosen player
     * @see com.model.player.Player
     * @see com.model.player.Strategy
     */
    public Player selectPlayer(List<Player> players) {
        return strategy.selectPlayer(players);
    }

    /**
     * Use the AI strategy to select a rumour card.
     *
     * @param rumourCards the rumour cards
     * @return the chosen rumour card
     * @see com.model.card.RumourCard
     * @see com.model.player.Strategy
     */
    public RumourCard selectCard(List<RumourCard> rumourCards) {
        return strategy.selectCard(rumourCards);
    }

    /**
     * Use the AI strategy to select a rumour card blindly.
     *
     * @param listSize the size of the list
     * @return the chosen index
     * @see com.model.player.Strategy
     */
    public int selectCard(int listSize) {
        return strategy.selectCard(listSize);
    }
}
