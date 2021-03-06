package com.model.card.effect;

import com.controller.PlayerAction;
import com.controller.RoundController;
import com.model.card.CardName;
import com.model.game.IdentityCard;
import com.model.game.Round;
import com.model.player.Player;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Reveal another player's identity. Witch: You gain 2pts. You take next turn. Villager: You lose 2pts. They take next turn. The type Reveal another identity effect.
 * 
 * @see com.model.card.effect.Effect
 * @see com.model.card.effect.EffectList
 */
public class RevealAnotherIdentityEffect extends TurnEffect {
	
	@Override
	public String toString() {
		return """
				Reveal another player's identity.
				Witch: You gain 2pts. You take next turn.
				Villager: You lose 2pts. They take next turn.""";
	}

	@Override
	public boolean applyEffect(final Player cardUser, final Player target) {
		Round round = Round.getInstance();

		if (round.getPlayerIdentityCard(target).isWitch()) {
			cardUser.addToScore(2);
			round.setNextPlayer(cardUser);
		} else {
			cardUser.addToScore(-2);
			round.setNextPlayer(target);
		}

		RoundController.getInstance().applyPlayerAction(target, PlayerAction.REVEAL_IDENTITY);
		return true;
	}

	@Override
	public Player chooseTarget(final CardName cardName, Player cardUser) {
		List<Player> selectablePlayers = Round.getInstance().getNotRevealedPlayers(cardUser);
		if (cardName == CardName.ANGRY_MOB) {
			selectablePlayers = selectablePlayers.stream()
					.filter(player -> player.getRevealedCards().stream().allMatch(rumourCard -> rumourCard.getCardName() != CardName.BROOMSTICK))
					.collect(Collectors.toList());
		}
		return RoundController.getInstance().choosePlayer(cardUser, selectablePlayers);
	}

	@Override
	public boolean isApplicable(Player cardUser, CardName cardName) {
		IdentityCard identityCard = Round.getInstance().getPlayerIdentityCard(cardUser);
		if (identityCard.isIdentityRevealed() && !identityCard.isWitch()) {
			if (cardName == CardName.ANGRY_MOB) {
				List<Player> players = Round.getInstance().getSelectablePlayers(cardUser)
						.stream()
						.filter(player -> player.getRevealedCards().stream().allMatch(rumourCard -> rumourCard.getCardName() != CardName.BROOMSTICK))
						.toList();
				return players.size() > 0;
			}
			return true;
		}
		return false;
	}

}
