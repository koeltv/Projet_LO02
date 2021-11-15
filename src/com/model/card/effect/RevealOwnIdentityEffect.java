package com.model.card.effect;

import com.controller.RoundController;
import com.model.card.CardName;
import com.model.player.Player;
import com.model.player.PlayerAction;

public class RevealOwnIdentityEffect extends Effect {
    @Override
    public String toString() {
        return """
                Reveal your identity.
                Witch: Player to your left takes next turn.
                Villager: Choose next player.""";
    }

    @Override
    public boolean applyEffect(final Player cardUser, final Player target) {
        
    	RoundController round = RoundController.getRoundController();
    	round.applyPlayerAction(cardUser, PlayerAction.REVEAL_IDENTITY);
        return true;
    }

    @Override
    public Player chooseTarget(final CardName cardName, Player cardUser) {
        return null;
    }

}
