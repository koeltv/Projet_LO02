package com.model.card.effect;

import com.controller.RoundController;
import com.model.card.CardName;
import com.model.player.Player;

public class ChooseNextEffect implements Effect {
    @Override
    public boolean applyEffect(final Player cardUser, final Player target) {
        if (cardUser != target) {
            RoundController.getRoundController().setNextPlayer(target);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Player chooseTarget(final CardName cardName, Player cardUser) {
        return RoundController.getRoundController().choosePlayer(cardUser, RoundController.getRoundController().getSelectablePlayers(cardUser));
    }

}
