package com.model.card.effect;

import com.model.card.CardName;
import com.model.card.RumourCard;
import com.model.player.Player;

public class TakeRevealedCardEffect extends Effect {
    @Override
    public String toString() {
        return "Take one of your own revealed Rumour cards into your hand.";
    }

    @Override
    public boolean applyEffect(final Player cardUser, final Player target) { //TODO
        RumourCard chosenCard;
        //        cardUser.hand.get(chosenCard).
        return true;
    }

    @Override
    public Player chooseTarget(final CardName cardName, Player cardUser) {
        return null;
    }

}
