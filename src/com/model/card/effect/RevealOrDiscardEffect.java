package com.model.card.effect;

import com.controller.RoundController;
import com.model.card.CardName;
import com.model.player.Player;
import com.model.player.PlayerAction;

import java.util.ArrayList;
import java.util.List;

public class RevealOrDiscardEffect extends Effect {
	@Override
	public String toString() {
		return """
				Choose a player. They must reveal their
				identity or discard a card from their hand.
				Witch: You gain 1pt. You take next turn.
				Villager: You lose 1pt. They take next turn.
				If they discard: They take next turn.""";
    }

	@Override
	public boolean applyEffect(final Player cardUser, final Player target) {
		RoundController round = RoundController.getRoundController();

		List<PlayerAction> actions = new ArrayList<>(List.of(PlayerAction.REVEAL_IDENTITY));
		if (target.getSelectableCardsFromHand().size() > 0) actions.add(PlayerAction.DISCARD);
		round.askPlayerForAction(target, actions);

		if (!round.getPlayerIdentityCard(target).isIdentityNotRevealed()) {
			if (round.getPlayerIdentityCard(target).isWitch()) {
				cardUser.addToScore(1);
				round.setNextPlayer(cardUser);
			} else {
				cardUser.addToScore(-1);
				round.setNextPlayer(target);
			}
		}
		return true;
	}

    @Override
    public Player chooseTarget(final CardName cardName, Player cardUser) {
    	return RoundController.getRoundController().choosePlayer(cardUser, RoundController.getRoundController().getNotRevealedPlayers(cardUser));
    }

}
