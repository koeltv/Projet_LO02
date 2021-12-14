package com.model.card.effect;

import com.model.card.CardName;
import com.model.game.Round;
import com.model.player.Player;

/**
 * Take next turn. The type Take next turn effect.
 * 
 * @see com.model.card.effect.Effect
 * @see com.model.card.effect.EffectList
 */
public class TakeNextTurnEffect extends TurnEffect {
    @Override
    public String toString() {
        return "Take next turn.";
    }

    @Override
    public boolean applyEffect(final Player cardUser, final Player target) {
        Round.getInstance().setNextPlayer(target);
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
