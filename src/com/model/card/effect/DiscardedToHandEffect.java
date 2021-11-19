package com.model.card.effect;

import com.controller.RoundController;
import com.model.card.CardName;
import com.model.card.RumourCard;
import com.model.game.CardState;
import com.model.player.Player;

import java.util.List;

/**
 * The type Discarded to hand effect.
 */
public class DiscardedToHandEffect extends Effect {
    /**
     * The Taken rumour card.
     */
    private RumourCard takenRumourCard;

    @Override
    public String toString() {
        return """
                Add one discarded card to your
                hand, and then discard this card.""";
    }

    @Override
    public boolean applyEffect(final Player cardUser, final Player target) {
        List<RumourCard> discardPile = RoundController.getRoundController().discardPile;

        if(discardPile.size() > 0) {
            RumourCard chosenCard = RoundController.getRoundController().chooseCard(cardUser, discardPile);
            cardUser.addCardToHand(discardPile.remove(discardPile.indexOf(chosenCard)));
            discardPile.add(cardUser.removeCardFromHand(takenRumourCard));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Player chooseTarget(final CardName cardName, Player cardUser) {
        for (CardState card : cardUser.hand) {
            if (card.rumourCard.getCardName() == cardName) {
                takenRumourCard = card.rumourCard;
                break;
            }
        }
        return cardUser;
    }

    @Override
    public boolean isApplicable(Player cardUser, CardName cardName) {
        return RoundController.getRoundController().discardPile.size() > 0;
    }

}
