package com.model.card.effect;

import com.model.card.CardName;
import com.model.player.Player;

public class RevealOrDiscardEffect extends Effect {
    @Override
    public String toString() {
        return """
                Choose a player. They must reveal their
                identity or discard a card from their hand.
                Witch: You gain 1pt. You take next turn.
                Villager: You lose 1pt. They take next turn.
                If they discard: They take next turn.""";
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
