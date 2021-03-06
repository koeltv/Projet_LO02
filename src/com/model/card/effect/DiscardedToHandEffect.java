package com.model.card.effect;

import com.controller.RoundController;
import com.model.card.CardName;
import com.model.card.RumourCard;
import com.model.game.CardState;
import com.model.game.Round;
import com.model.player.Player;

import java.util.List;

/**
 * Add one discarded card to your hand, and then discard this card. The type Discarded to hand effect.
 * 
 * @see com.model.card.effect.Effect
 * @see com.model.card.effect.EffectList
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
        List<RumourCard> discardPile = Round.getInstance().getDiscardPile();

        if(discardPile.size() > 0) {
            RumourCard chosenCard = RoundController.getInstance().chooseCard(cardUser, discardPile);
            cardUser.addCardToHand(discardPile.remove(discardPile.indexOf(chosenCard)));
            discardPile.add(cardUser.removeCardFromHand(takenRumourCard));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Player chooseTarget(final CardName cardName, Player cardUser) {
        for (CardState card : cardUser.getHand()) {
            if (card.rumourCard.getCardName() == cardName) {
                takenRumourCard = card.rumourCard;
                break;
            }
        }
        return cardUser;
    }

    @Override
    public boolean isApplicable(Player cardUser, CardName cardName) {
        return Round.getInstance().getDiscardPile().size() > 0;
    }

}
