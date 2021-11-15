package com.model.card.effect;

import com.model.card.CardName;
import com.model.player.Player;

public class TakeRandomCardFromNextEffect extends Effect {
    @Override
    public String toString() {
        return """
                Before their turn, take a random card
                from their hand and add it to your hand.""";
    }

    @Override
    public boolean applyEffect(final Player cardUser, final Player target) {
        // TODO Auto-generated return
        return false;
    }

    @Override
    public Player chooseTarget(final CardName cardName, Player cardUser) {
        // TODO Auto-generated return
        return null;
    }

}
