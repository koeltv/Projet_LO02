package com.model.card.effect;

import com.controller.RoundController;
import com.model.card.CardName;
import com.model.player.Player;

/**
 * The type Next must accuse other effect.
 */
public class NextMustAccuseOtherEffect extends Effect {
    @Override
    public String toString() {
        return """
                On their turn they must accuse a
                player other than you, if possible.""";
    }

    @Override
    public boolean applyEffect(final Player cardUser, final Player target) {
        RoundController.getInstance().addNotSelectablePlayer(target, cardUser);
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
