package com.model.card.effect;

import com.controller.RoundController;
import com.model.card.CardName;
import com.model.card.RumourCard;
import com.model.game.Round;
import com.model.player.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Discard from hand effect.
 */
public class DiscardFromHandEffect extends Effect {
    /**
     * The Selectable cards.
     */
    private List<RumourCard> selectableCards = new ArrayList<>();

    @Override
    public String toString() {
        return "Discard a card from your hand.";
    }

    @Override
    public boolean applyEffect(final Player cardUser, final Player target) {
        if (selectableCards.size() > 0) {
            RumourCard chosenCard = RoundController.getInstance().getPlayerController(cardUser).chooseCard(selectableCards);
            Round.getInstance().getDiscardPile().add(cardUser.removeCardFromHand(chosenCard));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Player chooseTarget(final CardName cardName, Player cardUser) {
        selectableCards = cardUser.getSelectableCardsFromHand();
        selectableCards.removeIf(rumourCard -> rumourCard.getCardName() == cardName);
        return cardUser;
    }

    @Override
    public boolean isApplicable(Player cardUser, CardName cardName) {
        return cardUser.getSelectableCardsFromHand().size() > 1;
    }

}
