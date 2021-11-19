package com.model.card.effect;

import com.controller.GameController;
import com.controller.RoundController;
import com.model.card.CardName;
import com.model.card.RumourCard;
import com.model.player.Player;

/**
 * The type Accuser discard random effect.
 */
public class AccuserDiscardRandomEffect extends Effect {
    @Override
    public String toString() {
        return """
                The player who accused you discards
                a random card from their hand.""";
    }

    @Override
    public boolean applyEffect(Player cardUser, Player target) {
        if (target.hand.size() > 0) {
            RumourCard chosenCard = target.hand.get(GameController.randomInInterval(target.hand.size() - 1)).rumourCard;
            RoundController.getRoundController().discardPile.add(RoundController.getCurrentPlayer().removeCardFromHand(chosenCard));
            return true;
        } else
            return false;
    }

    @Override
    public Player chooseTarget(CardName cardName, Player cardUser) {
        return RoundController.getCurrentPlayer();
    }

    @Override
    public boolean isApplicable(Player cardUser, CardName cardName) {
        return RoundController.getCurrentPlayer().getSelectableCardsFromHand().size() > 0;
    }

}
