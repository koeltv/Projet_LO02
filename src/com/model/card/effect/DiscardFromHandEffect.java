package com.model.card.effect;

import com.controller.RoundController;
import com.model.card.CardName;
import com.model.card.RumourCard;
import com.model.player.Player;

import java.util.ArrayList;
import java.util.List;

public class DiscardFromHandEffect extends Effect {

    private List<RumourCard> selectableCards = new ArrayList<>();

    @Override
    public String toString() {
        return "Discard a card from your hand.";
    }

    @Override
    public boolean applyEffect(final Player cardUser, final Player target) {
        if (selectableCards.size() > 0) {
            RumourCard chosenCard = RoundController.getRoundController().chooseCard(cardUser, selectableCards);
            RoundController.getRoundController().discardPile.add(cardUser.removeCardFromHand(chosenCard));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Player chooseTarget(final CardName cardName, Player cardUser) {
        List<RumourCard> hand = cardUser.getSelectableCardsFromHand();
        hand.removeIf(rumourCard -> rumourCard.getCardName() == cardName);
        selectableCards = hand;
        return cardUser;
    }

}
