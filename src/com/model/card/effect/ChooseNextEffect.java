package com.model.card.effect;

import com.controller.RoundController;
import com.model.card.CardName;
import com.model.player.Player;

/**
 * The type Choose next effect.
 */
public class ChooseNextEffect extends Effect {
    @Override
    public String toString() {
        return "Choose next player.";
    }

    @Override
    public boolean applyEffect(final Player cardUser, final Player target) {
        RoundController.getInstance().setNextPlayer(target);
        return true;
    }

    @Override
    public Player chooseTarget(final CardName cardName, Player cardUser) {
        return RoundController.getInstance().choosePlayer(cardUser, RoundController.getInstance().getSelectablePlayers(cardUser));
    }

    @Override
    public boolean isApplicable(Player cardUser, CardName cardName) {
        return true;
    }

}
