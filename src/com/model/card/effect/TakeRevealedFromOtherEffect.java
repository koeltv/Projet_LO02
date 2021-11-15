package com.model.card.effect;

import com.controller.RoundController;
import com.model.card.CardName;
import com.model.card.RumourCard;
import com.model.player.Player;

public class TakeRevealedFromOtherEffect extends Effect {
    @Override
    public String toString() {
        return """
                Take a revealed Rumour card from
                any other player into your hand.""";
    }

    @Override
    public boolean applyEffect(final Player cardUser, final Player target) {
        
    	if(target.getRevealedCards().size() > 0) {
    		RumourCard chosenCard = RoundController.getRoundController().chooseCard(target, target.getRevealedCards());
    		cardUser.addCardToHand(target.removeCardFromHand(chosenCard));
    		return true;
    	} else {
    		return false;
    	}
    }

    @Override
    public Player chooseTarget(final CardName cardName, Player cardUser) {
        return RoundController.getRoundController().choosePlayer(cardUser, RoundController.getRoundController().getSelectablePlayers(cardUser));
    }

}
