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
        return applyEffects(cardUser, cardUser == RoundController.getCurrentPlayer() ? huntEffects : witchEffects);
    }

    private Player chooseTarget(Player cardUser, Effect effect) {
        Player target;
        do {
            target = effect.chooseTarget(this.cardName, cardUser);
        } while (target == null);
        return target;
    }

    /**
     * Apply the needed effects of a card.
     *
     * @param cardUser the player that used this card
     * @return whether the effects have been used successfully or not
     */
    private boolean applyEffects(Player cardUser, List<Effect> effectList) {
        for (Effect effect : effectList) {
            if (!effect.applyEffect(cardUser, chooseTarget(cardUser, effect))) return false;
        }
        return true;
    }

}
