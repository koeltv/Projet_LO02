package com.model.card.effect;

import com.controller.RoundController;
import com.model.card.CardName;
import com.model.player.Player;
import com.model.player.PlayerAction;

public class RevealAnotherIdentityEffect extends Effect {
    @Override
    public String toString() {
        return """
                Reveal another player's identity.
                Witch: You gain 2pts. You take next turn.
                Villager: You lose 2pts. They take next turn.""";
    }

    @Override
    public boolean applyEffect(final Player cardUser, final Player target) {
        
    	RoundController round = RoundController.getRoundController();
    	round.applyPlayerAction(target, PlayerAction.REVEAL_IDENTITY);
    	
    	if(round.getPlayerIdentityCard(target).isWitch()) {
    		cardUser.addToScore(2);
    		round.setNextPlayer(cardUser);
    	} else {
    		cardUser.addToScore(-2);
    		round.setNextPlayer(target);
    	}
    	return true;
    }

    @Override
    public Player chooseTarget(final CardName cardName, Player cardUser) {
    	return RoundController.getRoundController().choosePlayer(cardUser, RoundController.getRoundController().getNotRevealedPlayers(cardUser));
    }

}
