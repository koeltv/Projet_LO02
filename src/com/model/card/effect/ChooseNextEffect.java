package com.model.card.effect;

import com.controller.RoundController;
import com.model.card.CardName;
import com.model.game.Round;
import com.model.player.Player;

/**
 * Choose next player. The type Choose next effect.
 * 
 * @see com.model.card.effect.Effect
 * @see com.model.card.effect.EffectList
 */
public class ChooseNextEffect extends TurnEffect {
    @Override
    public String toString() {
        return "Choose next player.";
    }

    @Override
    public boolean applyEffect(final Player cardUser, final Player target) {
        Round.getInstance().setNextPlayer(target);
        return true;
    }

    @Override
    public Player chooseTarget(final CardName cardName, Player cardUser) {
        return RoundController.getInstance().choosePlayer(cardUser, Round.getInstance().getSelectablePlayers(cardUser));
    }

    @Override
    public boolean isApplicable(Player cardUser, CardName cardName) {
        return true;
    }

}
