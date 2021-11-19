package com.model.card.effect;

import com.controller.RoundController;
import com.model.card.CardName;
import com.model.card.RumourCard;
import com.model.player.Player;

/**
 * The type Take revealed from other effect.
 */
public class TakeRevealedFromOtherEffect extends Effect {
    @Override
    public String toString() {
        return """
                Take a revealed Rumour card from
                any other player into your hand.""";
    }

    @Override
    public boolean applyEffect(final Player cardUser, final Player target) {
        if(target.getRevealedCards().size() > 0) {
            RumourCard chosenCard = RoundController.getInstance().chooseCard(target, target.getRevealedCards());
            cardUser.addCardToHand(target.removeCardFromHand(chosenCard));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Player chooseTarget(final CardName cardName, Player cardUser) {
        return RoundController.getInstance().choosePlayer(
                cardUser,
                RoundController.getInstance().getSelectablePlayers(cardUser)
                        .stream()
                        .filter(player -> player.getRevealedCards().size() > 0)
                        .toList()
        );
    }

    @Override
    public boolean isApplicable(Player cardUser, CardName cardName) {
        return RoundController.getInstance().getSelectablePlayers(cardUser).stream().anyMatch(player -> player.getRevealedCards().size() > 0);
    }

}
