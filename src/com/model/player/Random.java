package com.model.player;

import com.controller.PlayerAction;
import com.controller.RoundController;
import com.model.card.RumourCard;
import com.model.game.IdentityCard;

import java.util.List;

import static com.util.GameUtil.randomInInterval;

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
        if (RoundController.getCurrentPlayer() == ai) {
            return PlayerAction.ACCUSE;
        } else {
            return PlayerAction.REVEAL_IDENTITY;
        }
    }

    @Override
    public void selectIdentity() {
        IdentityCard identityCard = RoundController.getInstance().getPlayerIdentityCard(ai);
        identityCard.setWitch(randomInInterval(1) > 0);
    }

    @Override
    public Player selectPlayer(List<Player> players) {
        return players.get(randomInInterval(players.size() - 1));
    }

    @Override
    public RumourCard selectCard(List<RumourCard> rumourCards) {
        return rumourCards.get(randomInInterval(rumourCards.size() - 1));
    }

    @Override
    public int selectCard(int listSize) {
        return randomInInterval(listSize - 1);
    }

}
