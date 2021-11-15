package com.model.card.effect;

import java.util.List;

import com.controller.RoundController;
import com.model.card.CardName;
import com.model.card.RumourCard;
import com.model.game.CardState;
import com.model.player.Player;

public class DiscardedToHandEffect extends Effect {
	
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
        	cardUser.addCardToHand(chosenCard);
        	discardPile.add(cardUser.removeCardFromHand(takenRumourCard));
        	return true;
        } else {
        	return false;
        }
    }

    @Override
    public Player chooseTarget(final CardName cardName, Player cardUser) {
        for(CardState card : cardUser.hand) {
        	if(card.rumourCard.getCardName() == cardName) {
        		takenRumourCard = card.rumourCard;
        		break;
        	}
        }
        return null;
    }

}
