package com.model.card.effect;

import com.controller.GameController;
import com.controller.RoundController;
import com.model.card.CardName;
import com.model.card.RumourCard;
import com.model.player.Player;

public class AccuserDiscardRandomEffect implements Effect {
    @Override
    public boolean applyEffect(Player cardUser, Player target) {
        if (target.hand.size() > 0) {
            RumourCard chosenCard = target.hand.get(GameController.randomInInterval(0, target.hand.size() - 1)).rumourCard;
            RoundController.getCurrentPlayer().removeCardFromHand(chosenCard);
            RoundController.getRoundController().discardPile.add(chosenCard);
            return true;
        } else
            return false;
    }

    @Override
    public Player chooseTarget(CardName cardName, Player cardUser) {
        return RoundController.getCurrentPlayer();
    }

}
