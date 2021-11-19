package com.model.card.effect;

import com.controller.RoundController;
import com.model.card.CardName;
import com.model.card.RumourCard;
import com.model.player.Player;

import static com.util.GameUtil.randomInInterval;

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
        if (target.getHand().size() > 0) {
            RumourCard chosenCard = target.getHand().get(randomInInterval(target.getHand().size() - 1)).rumourCard;
            RoundController.getInstance().getDiscardPile().add(RoundController.getCurrentPlayer().removeCardFromHand(chosenCard));
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
