package com.model.card.effect;

import com.controller.RoundController;
import com.model.card.CardName;
import com.model.card.RumourCard;
import com.model.game.CardState;
import com.model.player.Player;

/**
 * The type Take revealed card effect.
 */
public class TakeRevealedCardEffect extends Effect {
    @Override
    public String toString() {
        return """
                Take one of your own revealed
                Rumour cards into your hand.""";
    }

    @Override
    public boolean applyEffect(final Player cardUser, final Player target) {
        if (cardUser.getRevealedCards().size() > 0) {
            RumourCard chosenCard = RoundController.getInstance().chooseCard(cardUser, cardUser.getRevealedCards());
            for (CardState card : cardUser.getHand()) {
                if (card.rumourCard == chosenCard) {
                    card.setRevealed(false);    //change the status of the Rumour Card
                    break;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Player chooseTarget(final CardName cardName, Player cardUser) {
        return cardUser;
    }

    @Override
    public boolean isApplicable(Player cardUser, CardName cardName) {
        return cardUser.getRevealedCards().size() > 0;
    }

}
