package com.model.card.effect;

import com.controller.PlayerAction;
import com.controller.RoundController;
import com.model.card.CardName;
import com.model.player.Player;

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
        RoundController.getInstance().applyPlayerAction(target, PlayerAction.LOOK_AT_IDENTITY);
        return true;
    }

    @Override
    public Player chooseTarget(final CardName cardName, Player cardUser) {
        return RoundController.getInstance().getNextPlayer();
    }

    @Override
    public boolean isApplicable(Player cardUser, CardName cardName) {
        return true;
    }

}
