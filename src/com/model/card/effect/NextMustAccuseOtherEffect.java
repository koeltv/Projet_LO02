package com.model.card.effect;

import com.model.card.CardName;
import com.model.player.Player;

public class NextMustAccuseOtherEffect extends Effect {
    @Override
    public String toString() {
        return "On their turn they must accuse a player other than you, if possible.";
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
