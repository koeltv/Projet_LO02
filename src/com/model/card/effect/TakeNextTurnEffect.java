package com.model.card.effect;

import com.controller.RoundController;
import com.model.card.CardName;
import com.model.player.Player;

public class TakeNextTurnEffect implements Effect {
    public boolean applyEffect(final Player cardUser, final Player target) {
        RoundController.getRoundController().setNextPlayer(cardUser);
        return true;
    }

    public Player chooseTarget(final CardName cardName) {
        // TODO Auto-generated return
        return null;
    }

}
