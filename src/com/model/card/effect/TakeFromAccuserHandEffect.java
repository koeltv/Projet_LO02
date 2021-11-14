package com.model.card.effect;

import com.model.card.CardName;
import com.model.player.Player;

public class TakeFromAccuserHandEffect extends Effect {
    @Override
    public String toString() {
        return """
                Take one card from the hand of
                the player who accused you.""";
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
