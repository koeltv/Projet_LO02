package com.model.card.effect;

import com.model.card.CardName;
import com.controller.GameController;
import com.model.player.Player;

public class TakeNextTurnEffect implements Effect {
    public boolean applyEffect(final Player cardUser, final Player target) {
        GameController.getGame().roundController.setNextPlayer(cardUser);
        return true;
    }

    public Player chooseTarget(final CardName cardName) {
        // TODO Auto-generated return
        return null;
    }

}
