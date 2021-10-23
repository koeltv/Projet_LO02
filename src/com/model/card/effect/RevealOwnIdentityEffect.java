package com.model.card.effect;

import com.model.card.CardName;
import com.model.player.Player;

public class RevealOwnIdentityEffect implements Effect {
    public boolean applyEffect(final Player cardUser, final Player target) {
        cardUser.identityCard.setIdentityRevealed(true);
        System.out.print("The player is a ");
        if (cardUser.identityCard.isWitch()) {
            System.out.print("witch");
        } else {
            System.out.print("villager");
        }
        return true;
    }

    public Player chooseTarget(final CardName cardName) {
        // TODO Auto-generated return
        return null;
    }

}
