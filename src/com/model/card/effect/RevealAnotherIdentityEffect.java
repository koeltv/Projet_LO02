package com.model.card.effect;

import com.controller.RoundController;
import com.model.card.CardName;
import com.model.game.IdentityCard;
import com.model.player.Player;
import com.model.player.PlayerAction;

import java.util.List;
import java.util.stream.Collectors;

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

		if (round.getPlayerIdentityCard(target).isWitch()) {
			cardUser.addToScore(2);
			round.setNextPlayer(cardUser);
		} else {
			cardUser.addToScore(-2);
			round.setNextPlayer(target);
		}

		round.applyPlayerAction(target, PlayerAction.REVEAL_IDENTITY);
		return true;
	}

	@Override
	public Player chooseTarget(final CardName cardName, Player cardUser) {
		List<Player> selectablePlayers = RoundController.getRoundController().getNotRevealedPlayers(cardUser);
		if (cardName == CardName.ANGRY_MOB) {
			selectablePlayers = selectablePlayers.stream()
					.filter(player -> player.getRevealedCards().stream().allMatch(rumourCard -> rumourCard.getCardName() != CardName.BROOMSTICK))
					.collect(Collectors.toList());
		}
		return RoundController.getRoundController().choosePlayer(cardUser, selectablePlayers);
	}

	@Override
	public boolean isApplicable(Player cardUser, CardName cardName) {
		IdentityCard identityCard = RoundController.getRoundController().getPlayerIdentityCard(cardUser);
		if (!identityCard.isIdentityNotRevealed() && !identityCard.isWitch()) {
			if (cardName == CardName.ANGRY_MOB) {
				List<Player> players = RoundController.getRoundController().getSelectablePlayers(cardUser)
						.stream()
						.filter(player -> player.getRevealedCards().stream().allMatch(rumourCard -> rumourCard.getCardName() != CardName.BROOMSTICK))
						.collect(Collectors.toList());
				return players.size() > 0;
			}
			return true;
		}
		return false;
	}

}
