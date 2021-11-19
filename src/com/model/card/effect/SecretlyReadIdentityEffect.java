package com.model.card.effect;

import com.controller.RoundController;
import com.model.card.CardName;
import com.model.player.Player;
import com.model.player.PlayerAction;

/**
 * The type Secretly read identity effect.
 */
public class SecretlyReadIdentityEffect extends Effect {
    @Override
    public String toString() {
        return """
                Before their turn, secretly
                look at their identity.""";
    }

    @Override
    public boolean applyEffect(final Player cardUser, final Player target) {
        RoundController.getRoundController().applyPlayerAction(target, PlayerAction.LOOK_AT_IDENTITY);
        return true;
    }

    @Override
    public Player chooseTarget(final CardName cardName, Player cardUser) {
        return RoundController.getRoundController().getNextPlayer();
    }

    @Override
    public boolean isApplicable(Player cardUser, CardName cardName) {
        return true;
    }

}
