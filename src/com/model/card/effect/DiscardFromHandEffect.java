package com.model.card.effect;

import com.controller.RoundController;
import com.model.card.CardName;
import com.model.card.RumourCard;
import com.model.player.Player;

import java.util.List;

public class DiscardFromHandEffect extends Effect {
    @Override
    public String toString() {
        return "Discard a card from your hand.";
    }

    @Override
    public boolean applyEffect(final Player cardUser, final Player target) {
        
    	List<RumourCard> hand = cardUser.getSelectableCardsFromHand(); // cf Player
    	
    	if(hand.size() >= 2) {
    		RumourCard chosenCard = RoundController.getRoundController().chooseCard(cardUser, hand);
        	RoundController.getRoundController().discardPile.add(cardUser.removeCardFromHand(chosenCard));
            return true;
    	} else {
    		return false;
    	}
    }

    @Override
    public Player chooseTarget(final CardName cardName, Player cardUser) {
        return cardUser;
    }

}
