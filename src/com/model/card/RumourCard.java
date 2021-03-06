package com.model.card;

import com.model.card.effect.Effect;
import com.model.card.effect.EffectList;
import com.model.game.Round;
import com.model.player.Player;

import java.io.Serializable;
import java.util.List;

/**
 * The type Rumour card.
 *
 * Gives all the methods related to Rumour Cards.
 */
public class RumourCard implements Serializable {
    /**
     * The Card name.
     *
     * @see com.model.card.CardName
     */
    private final CardName cardName;

    /**
     * The Witch effects.
     *
     * @see com.model.card.effect.EffectList
     */
    private final EffectList witchEffects;

    /**
     * The Hunt effects.
     *
     * @see com.model.card.effect.EffectList
     */
    private final EffectList huntEffects;

    /**
     * Instantiates a new Rumour card.
     *
     * @param name         the card name
     * @param witchEffects the witch effects
     * @param huntEffects  the hunt effects
     * @see com.model.card.CardName
     * @see com.model.card.effect.EffectList
     */
    public RumourCard(CardName name, EffectList witchEffects, EffectList huntEffects) {
        this.cardName = name;
        this.witchEffects = witchEffects;
        this.huntEffects = huntEffects;
    }

    /**
     * Gets Hunt Effects
     *
     * @return hunt effects
     * @see com.model.card.effect.EffectList
     */
    public EffectList getHuntEffects() {
        return huntEffects;
    }

    /**
     * Gets Witch Effects
     *
     * @return witch effects
     * @see com.model.card.effect.EffectList
     */
    public EffectList getWitchEffects() {
        return witchEffects;
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
     * @see com.model.card.CardName
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
     * @see com.model.game.Round
     * @see com.model.player.Player
     */
    public boolean useCard(Player cardUser) {
        return applyEffects(cardUser, cardUser == Round.getCurrentPlayer() ? huntEffects : witchEffects);
    }

    /**
     * Choose target player.
     *
     * @param cardUser the card user
     * @param effect   the effect
     * @return the player
     * @see com.model.player.Player
     * @see com.model.card.effect.Effect
     */
    private Player chooseTarget(Player cardUser, Effect effect) {
        Player target;
        do {
            target = effect.chooseTarget(cardName, cardUser);
        } while (target == null);
        return target;
    }

    /**
     * Apply the needed effects of a card.
     *
     * @param cardUser   the player that used this card
     * @param effectList the effect list
     * @return whether the effects have been used successfully or not
     * @see com.model.player.Player
     * @see com.model.card.effect.Effect
     */
    private boolean applyEffects(Player cardUser, List<Effect> effectList) {
        for (Effect effect : effectList) {
            if (!effect.applyEffect(cardUser, chooseTarget(cardUser, effect))) return false;
        }
        return true;
    }

    /**
     * Check if the card is usable.
     *
     * @param cardUser the card user
     * @return true if it is, false otherwise
     * @see com.model.game.Round
     * @see com.model.player.Player
     * @see com.model.card.effect.Effect
     */
    public boolean isUsable(Player cardUser) {
        List<Effect> effects = cardUser == Round.getCurrentPlayer() ? huntEffects : witchEffects;
        return effects.stream().allMatch(effect -> effect.isApplicable(cardUser, cardName));
    }

}
