package com.model.card.effect;

import com.controller.RoundController;
import com.model.card.CardName;
import com.model.player.Player;

/**
 * The type Take next turn effect.
 */
public class TakeNextTurnEffect extends Effect {
    @Override
    public String toString() {
        return "Take next turn.";
    }

    @Override
    public boolean applyEffect(final Player cardUser, final Player target) {
        RoundController.getRoundController().setNextPlayer(target);
        return true;
    }

    @Override
    public Player chooseTarget(final CardName cardName, Player cardUser) {
        return cardUser;
    }

    @Override
    public boolean isApplicable(Player cardUser, CardName cardName) {
        return true;
    }

}
