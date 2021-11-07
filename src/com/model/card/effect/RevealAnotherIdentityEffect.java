package com.model.card.effect;

import com.model.card.CardName;
import com.model.player.Player;

public class RevealAnotherIdentityEffect extends Effect {
    @Override
    public String toString() {
        return """
                Reveal another player's identity.
                Witch: You gain 2pts. You take next turn.
                Villager: You lose 2pts. They take next turn.""";
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
