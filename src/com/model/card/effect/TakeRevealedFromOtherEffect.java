package com.model.card.effect;

import com.model.card.CardName;
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
        // TODO Auto-generated return
        return false;
    }

    @Override
    public Player chooseTarget(final CardName cardName, Player cardUser) {
        // TODO Auto-generated return
        return null;
    }

}
