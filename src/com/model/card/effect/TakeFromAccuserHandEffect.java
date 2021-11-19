package com.model.card.effect;

import com.controller.RoundController;
import com.model.card.CardName;
import com.model.card.RumourCard;
import com.model.player.Player;

/**
 * The type Take from accuser hand effect.
 */
public class TakeFromAccuserHandEffect extends Effect {
    @Override
    public String toString() {
        return """
                Take one card from the hand of
                the player who accused you.""";
    }

    @Override
    public boolean applyEffect(final Player cardUser, final Player target) {
        if(target.getSelectableCardsFromHand().size() >= 1) {
            RumourCard chosenCard = RoundController.getInstance().chooseCardBlindly(cardUser, target.getSelectableCardsFromHand());
            cardUser.addCardToHand(target.removeCardFromHand(chosenCard));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Player chooseTarget(final CardName cardName, Player cardUser) {
        return RoundController.getCurrentPlayer();
    }

    @Override
    public boolean isApplicable(Player cardUser, CardName cardName) {
        return RoundController.getCurrentPlayer().getSelectableCardsFromHand().size() > 0;
    }

}
