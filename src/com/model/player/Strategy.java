package com.model.player;

import com.controller.PlayerAction;
import com.model.card.RumourCard;
import com.model.game.IdentityCard;
import com.model.game.Round;

import java.io.Serializable;
import java.util.List;

import static com.util.GameUtil.randomInInterval;

/**
 * The interface Strategy.
 */
public abstract class Strategy implements Serializable {
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
    public void selectIdentity() {
        IdentityCard identityCard = Round.getInstance().getPlayerIdentityCard(ai);
        identityCard.setWitch(randomInInterval(1) > 0);
    }

    /**
     * Select target player.
     *
     * @param players the players
     * @return the player
     */
    public Player selectPlayer(List<Player> players) {
        return players.get(randomInInterval(players.size() - 1));
    }

    /**
     * Select a rumour card.
     *
     * @param rumourCards the rumour cards
     * @return the rumour card
     */
    public RumourCard selectCard(List<RumourCard> rumourCards) {
        return rumourCards.get(randomInInterval(rumourCards.size() - 1));
    }

    /**
     * Select a rumour card blindly.
     *
     * @param listSize the size of the list
     * @return the chosen index
     */
    public int selectCard(int listSize) {
        return randomInInterval(listSize - 1);
    }
}
