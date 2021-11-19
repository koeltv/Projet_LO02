package com.model.card.effect;

import com.controller.RoundController;
import com.model.card.CardName;
import com.model.card.RumourCard;
import com.model.player.Player;

import java.util.List;

import static com.util.GameUtil.randomInInterval;

/**
 * The type Take random card from next effect.
 */
public class TakeRandomCardFromNextEffect extends Effect {
    @Override
    public String toString() {
        return """
                Before their turn, take a random card
                from their hand and add it to your hand.""";
    }

    @Override
    public boolean applyEffect(final Player cardUser, final Player target) {
        List<RumourCard> selectableCards = target.getSelectableCardsFromHand();

        if(selectableCards.size() >= 1) {
            RumourCard chosenCard = selectableCards.get(randomInInterval(selectableCards.size() - 1));
            cardUser.addCardToHand(target.removeCardFromHand(chosenCard));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Player chooseTarget(final CardName cardName, Player cardUser) {
        return RoundController.getInstance().getNextPlayer();
    }

    @Override
    public boolean isApplicable(Player cardUser, CardName cardName) {
        return RoundController.getInstance().getSelectablePlayers(cardUser)
                .stream().anyMatch(player -> player.getSelectableCardsFromHand().size() > 0);
    }

}
