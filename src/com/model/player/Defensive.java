package com.model.player;

import com.controller.GameController;
import com.controller.RoundController;
import com.model.card.RumourCard;
import com.model.game.IdentityCard;

import java.util.List;

public class Defensive implements Strategy {

    private final AI ai;

    public Defensive(AI ai) {
        this.ai = ai;
    }

    @Override
    public PlayerAction use(List<PlayerAction> possibleActions) { //TODO Temporary implementation, need to be developed
        if (RoundController.getCurrentPlayer() == ai) {
            return PlayerAction.ACCUSE;
        } else {
            return PlayerAction.REVEAL_IDENTITY;
        }
    }

    @Override
    public void selectIdentity() {
        IdentityCard identityCard = RoundController.getRoundController().getPlayerIdentityCard(ai);
        identityCard.setWitch(GameController.randomInInterval(1) > 0);
    }

    @Override
    public Player selectPlayer(List<Player> players) {
        return players.get(GameController.randomInInterval(players.size() - 1));
    }

    @Override
    public RumourCard selectCard(List<RumourCard> rumourCards) {
        return rumourCards.get(GameController.randomInInterval(rumourCards.size() - 1));
    }

    @Override
    public int selectCard(int listSize) {
        return GameController.randomInInterval(listSize - 1);
    }

}
