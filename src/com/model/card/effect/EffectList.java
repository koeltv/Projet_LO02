package com.model.card.effect;

import java.util.ArrayList;
import java.util.ListIterator;

/**
 * The type Effect list.
 */
public class EffectList extends ArrayList<Effect> {
    /**
     * Instantiates a new Effect list using the given turn effect
     *
     * @param turnEffect the turn effect
     */
    public EffectList(TurnEffect turnEffect) {
        add(turnEffect);
    }

    /**
     * Instantiates a new Effect list with the given effect and the default turn effect.
     *
     * @param effect the effect
     */
    public EffectList(Effect effect) {
        add(effect);
        add(new TakeNextTurnEffect());
    }

    /**
     * Instantiates a new Effect list with the given effect and the given turn effect.
     *
     * @param effect     the effect
     * @param turnEffect the turn effect
     */
    public EffectList(Effect effect, TurnEffect turnEffect) {
        add(effect);
        add(turnEffect);
    }

    /**
     * Instantiates a new Effect list with the given turn effect and the given effect (in that order).
     *
     * @param turnEffect the turn effect
     * @param effect     the effect
     */
    public EffectList(TurnEffect turnEffect, Effect effect) {
        add(turnEffect);
        add(effect);
    }

    /**
     * Add an effect to the list.
     *
     * @param effect the effect
     */
    @Override
    public boolean add(Effect effect) {
        if (effect instanceof TurnEffect && super.stream().anyMatch(existingEffect -> existingEffect instanceof TurnEffect)) {
            ListIterator<Effect> iterator = super.listIterator();
            while (iterator.hasNext()) {
                if (iterator.next() instanceof TurnEffect) {
                    iterator.set(effect);
                    break;
                }
            }
        } else super.add(effect);
        return true;
    }
}
