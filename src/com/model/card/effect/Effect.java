package com.model.card.effect;

import com.model.card.CardName;
import com.model.player.Player;

/**
 * The abstract class Effect.
 * 
 * Gives all the methods related to card effects.
 */
public abstract class Effect {
    
	/**
     * Get effect description.
     *
     * @return effect description.
     */
    @Override
    public abstract String toString();

    /**
     * Apply effect.
     *
     * @param cardUser the card user
     * @param target   the target
     * @return whether the effect was successfully applied or not
     * @see com.model.player.Player
     */
    public abstract boolean applyEffect(Player cardUser, Player target);

    /**
     * Choose target player.
     *
     * @param cardName the card name
     * @param cardUser the player using the card and applying its effects
     * @return the chosen player
     * @see com.model.card.CardName
     * @see com.model.player.Player
     */
    public abstract Player chooseTarget(CardName cardName, Player cardUser);

    /**
     * Check if applicable.
     *
     * @param cardUser the card user
     * @param cardName the card name
     * @return true if the effect can be used, false otherwise
     * @see com.model.card.CardName
     * @see com.model.player.Player
     */
    public abstract boolean isApplicable(Player cardUser, CardName cardName);
}
