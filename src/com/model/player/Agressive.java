package com.model.player;

import com.controller.GameController;
import com.controller.RoundController;
import com.model.card.RumourCard;

import java.util.List;

public class Agressive implements Strategy {
    @Override
    public PlayerAction use(AI ai, List<PlayerAction> possibleActions) { //TODO Temporary implementation, need to be developed
        if (RoundController.getCurrentPlayer() == ai) {
            return PlayerAction.ACCUSE;
        } else {
            return PlayerAction.REVEAL_IDENTITY;
        }
    }

    @Override
    public void selectIdentity(AI ai) {
        ai.identityCard.setWitch(GameController.randomInInterval(0, 1) > 0);
    }

    @Override
    public Player selectPlayer(List<Player> players) {
        return players.get(GameController.randomInInterval(0, players.size() - 1));
    }

    @Override
    public RumourCard selectCard(List<RumourCard> rumourCards) {
        return rumourCards.get(GameController.randomInInterval(0, rumourCards.size() - 1));
    }

}
