package com.model.card.effect;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * The type Effect list.
 */
public class EffectList {
    /**
     * The Effects of the list.
     */
    public final List<Effect> effects;

    /**
     * Instantiates a new Effect list with the default turn effect (Take next turn).
     */
    public EffectList() {
        this.effects = new ArrayList<>();
        add(new TakeNextTurnEffect());
    }

    /**
     * Instantiates a new Effect list using the given turn effect
     *
     * @param turnEffect the turn effect
     */
    public EffectList(TurnEffect turnEffect) {
        this.effects = new ArrayList<>();
        add(turnEffect);
    }

    /**
     * Instantiates a new Effect list with the given effect and the default turn effect.
     *
     * @param effect the effect
     */
    public EffectList(Effect effect) {
        this.effects = new ArrayList<>();
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
        this.effects = new ArrayList<>();
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
        this.effects = new ArrayList<>();
        add(turnEffect);
        add(effect);
    }

    /**
     * Add an effect to the list.
     *
     * @param effect the effect
     */
    public void add(Effect effect) {
        if (effect instanceof TurnEffect && effects.stream().anyMatch(effect1 -> effect1 instanceof TurnEffect)) {
            ListIterator<Effect> iterator = effects.listIterator();
            while (iterator.hasNext()) {
                if (iterator.next() instanceof TurnEffect) iterator.set(effect);
            }
        } else {
            effects.add(effect);
        }
    }
}
