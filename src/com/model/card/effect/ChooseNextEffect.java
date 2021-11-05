package com.model.card.effect;

import com.controller.RoundController;
import com.model.card.CardName;
import com.model.player.Player;

public class ChooseNextEffect implements Effect {
    public boolean applyEffect(final Player cardUser, final Player target) {
        if (cardUser != target) {
            RoundController.getRoundController().setNextPlayer(target);
            return true;
        } else
            return false;
    }

    public Player chooseTarget(final CardName cardName, Player cardUser) {
        System.out.println("Choose the next player");
        return RoundController.getRoundController().choosePlayer(cardUser);
    }

}
