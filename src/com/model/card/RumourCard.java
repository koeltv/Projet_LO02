package com.model.card;

import com.controller.RoundController;
import com.model.card.effect.Effect;
import com.model.player.Player;

import java.util.List;

/**
 * The type Rumour card.
 */
public class RumourCard {
    private final CardName cardName;

    private final List<Effect> witchEffects;

    private final List<Effect> huntEffects;

    /**
     * Instantiates a new Rumour card.
     *
     * @param name         the card name
     * @param witchEffects the witch effects
     * @param huntEffects  the hunt effects
     */
    public RumourCard(CardName name, List<Effect> witchEffects, List<Effect> huntEffects) {
        this.cardName = name;
        this.witchEffects = witchEffects;
        this.huntEffects = huntEffects;
    }

    /**
     * Gets card name.
     *
     * @return the card name
     */
    public CardName getCardName() {
        return this.cardName;
    }

    /**
     * Apply the wanted effects of the card.
     * This method check which effects of the card to use (witch? effects or hunt! effects) and apply them.
     *
     * @param cardUser the player that used this card
     * @return whether the card has been used successfully or not
     */
    public boolean useCard(Player cardUser) {
        return cardUser == RoundController.getCurrentPlayer() ? applyHuntEffects(cardUser) : applyWitchEffects(cardUser);
    }

    private Player chooseTarget(Player cardUser, Effect witchEffect) {
        Player target;
        do {
            target = witchEffect.chooseTarget(this.cardName, cardUser);
        } while (target == null);
        return target;
    }

    /**
     * Apply the witch? effects of a card.
     *
     * @param cardUser the player that used this card
     * @return whether the effects have been used successfully or not
     */
    public boolean applyWitchEffects(Player cardUser) {
        for (Effect witchEffect : this.witchEffects) {
            if (!witchEffect.applyEffect(cardUser, chooseTarget(cardUser, witchEffect))) return false;
        }
        return true;
    }

    /**
     * Apply the hunt! effects of a card.
     *
     * @param cardUser the player that used this card
     * @return whether the effects have been used successfully or not
     */
    public boolean applyHuntEffects(Player cardUser) {
        for (Effect huntEffect : this.huntEffects) {
            if (!huntEffect.applyEffect(cardUser, chooseTarget(cardUser, huntEffect))) return false;
        }
        return true;
    }

}
