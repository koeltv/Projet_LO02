package com.model.card.effect;

import com.controller.RoundController;
import com.model.card.CardName;
import com.model.card.RumourCard;
import com.model.player.Player;

public class DiscardedToHandEffect extends Effect {
    @Override
    public String toString() {
        return """
                Add one discarded card to your
                hand, and then discard this card.""";
    }

    @Override
    public boolean applyEffect(final Player cardUser, final Player target) {
        RumourCard chosenCard = RoundController.getRoundController().chooseCard(cardUser, RoundController.getRoundController().discardPile);
        // TODO Auto-generated return
        return false;
    }

    @Override
    public Player chooseTarget(final CardName cardName, Player cardUser) {
        // TODO Auto-generated return
        return null;
    }

}
