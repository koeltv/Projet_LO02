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

    public final List<Effect> witchEffects;

    public final List<Effect> huntEffects;

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
     * Get card description.
     *
     * @return card description
     */
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("==========").append(cardName).append("==========\n");
        stringBuilder.append("----------Witch Effects----------\n");
        witchEffects.forEach(effect -> stringBuilder.append(effect).append("\n"));
        stringBuilder.append("----------Hunt Effects----------\n");
        huntEffects.forEach(effect -> stringBuilder.append(effect).append("\n"));
        return stringBuilder.toString();
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
