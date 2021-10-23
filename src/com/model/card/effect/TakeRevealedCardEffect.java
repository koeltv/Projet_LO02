package com.model.card.effect;

import com.model.card.CardName;
import com.model.card.RumourCard;
import com.model.player.Player;

public class TakeRevealedCardEffect implements Effect {
    public boolean applyEffect(final Player cardUser, final Player target) { //TODO
        RumourCard chosenCard;
//        cardUser.hand.get(chosenCard).
        return true;
    }

    public Player chooseTarget(final CardName cardName) {
        return null;
    }

}
