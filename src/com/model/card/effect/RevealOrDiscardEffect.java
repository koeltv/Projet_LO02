package com.model.card.effect;

import com.controller.PlayerAction;
import com.controller.RoundController;
import com.model.card.CardName;
import com.model.game.IdentityCard;
import com.model.game.Round;
import com.model.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Reveal or discard effect.
 */
public class RevealOrDiscardEffect extends TurnEffect {
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
		Round round = Round.getInstance();

		List<PlayerAction> actions = new ArrayList<>(List.of(PlayerAction.REVEAL_IDENTITY));
		if (target.getSelectableCardsFromHand().size() > 0) actions.add(PlayerAction.DISCARD);
		RoundController.getInstance().getPlayerController(target).askPlayerForAction(actions);

		IdentityCard identityCard = round.getPlayerIdentityCard(target);
		if (identityCard == null) {
			cardUser.addToScore(1);
			round.setNextPlayer(cardUser);
		} else if (identityCard.isIdentityRevealed() && !identityCard.isWitch()) {
			cardUser.addToScore(-1);
			round.setNextPlayer(target);
		}
		return true;
	}

	@Override
	public Player chooseTarget(final CardName cardName, Player cardUser) {
		List<Player> selectablePlayers = Round.getInstance().getSelectablePlayers(cardUser);
		if (cardName == CardName.DUCKING_STOOL) {
			selectablePlayers = selectablePlayers.stream()
					.filter(player -> player.getRevealedCards().stream().anyMatch(rumourCard -> rumourCard.getCardName() != CardName.WART))
					.collect(Collectors.toList());
		}
		return RoundController.getInstance().getPlayerController(cardUser).choosePlayer(selectablePlayers);
	}

	@Override
	public boolean isApplicable(Player cardUser, CardName cardName) {
		if (cardName == CardName.DUCKING_STOOL) {
			return Round.getInstance().getSelectablePlayers(cardUser).stream()
					.anyMatch(player -> player.getRevealedCards().stream().anyMatch(rumourCard -> rumourCard.getCardName() != CardName.WART));
		}
		return true;
	}

}
