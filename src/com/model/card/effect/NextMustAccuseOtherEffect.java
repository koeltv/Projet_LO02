package com.model.card.effect;

import com.model.card.CardName;
import com.model.game.Round;
import com.model.player.Player;

/**
 * On their turn they must accuse a player other than you, if possible. The type Next must accuse other effect.
 * 
 * @see com.model.card.effect.Effect
 * @see com.model.card.effect.EffectList
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
        Round.getInstance().addNotSelectablePlayer(target, cardUser);
        return true;
    }

    @Override
    public Player chooseTarget(final CardName cardName, Player cardUser) {
        return Round.getInstance().getNextPlayer();
    }

    @Override
    public boolean isApplicable(Player cardUser, CardName cardName) {
        return true;
    }

}
