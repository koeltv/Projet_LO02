package com.model.card;

import com.model.card.effect.*;

import java.util.Collections;
import java.util.LinkedList;

/**
 * The type Deck.
 * 
 * Gives all the methods related to the deck.
 */
public class Deck {
    /**
     * The Cards.
     * 
     * @see com.model.card.RumourCard
     */
    private final LinkedList<RumourCard> cards;

    /**
     * Instantiates a new Deck.
     * 
     * @see com.model.card.CardName
     * @see com.model.card.RumourCard
     * @see com.model.card.effect.EffectList
     * @see com.model.card.effect.DiscardFromHandEffect
     * @see com.model.card.effect.TakeRevealedCardEffect
     * @see com.model.card.effect.TakeFromAccuserHandEffect
     * @see com.model.card.effect.ChooseNextEffect
     * @see com.model.card.effect.AccuserDiscardRandomEffect
     * @see com.model.card.effect.NextMustAccuseOtherEffect
     * @see com.model.card.effect.TakeNextTurnEffect
     * @see com.model.card.effect.RevealAnotherIdentityEffect
     * @see com.model.card.effect.SecretlyReadIdentityEffect
     * @see com.model.card.effect.TakeRandomCardFromNextEffect
     * @see com.model.card.effect.RevealOrDiscardEffect
     * @see com.model.card.effect.RevealOwnIdentityEffect
     * @see com.model.card.effect.DiscardedToHandEffect
     * @see com.model.card.effect.TakeRevealedFromOtherEffect
     */
    public Deck() {
        this.cards = new LinkedList<>();

        for (CardName cardName : CardName.values()) {
        	
            //Witch? effects
            EffectList witchEffects = switch (cardName) {
                case THE_INQUISITION -> new EffectList(new DiscardFromHandEffect());
                case POINTED_HAT -> new EffectList(new TakeRevealedCardEffect());
                case HOOKED_NOSE -> new EffectList(new TakeFromAccuserHandEffect());
                case DUCKING_STOOL -> new EffectList(new ChooseNextEffect());
                case CAULDRON -> new EffectList(new AccuserDiscardRandomEffect());
                case EVIL_EYE -> new EffectList(new ChooseNextEffect(), new NextMustAccuseOtherEffect());
                default -> new EffectList(new TakeNextTurnEffect());
            };

            //Hunt! Effects
            EffectList huntEffect = switch (cardName) {
                case ANGRY_MOB -> new EffectList(new RevealAnotherIdentityEffect());
                case THE_INQUISITION -> new EffectList(new ChooseNextEffect(), new SecretlyReadIdentityEffect());
                case POINTED_HAT -> new EffectList(new TakeRevealedCardEffect(), new ChooseNextEffect());
                case HOOKED_NOSE -> new EffectList(new ChooseNextEffect(), new TakeRandomCardFromNextEffect());
                case DUCKING_STOOL -> new EffectList(new RevealOrDiscardEffect());
                case CAULDRON, TOAD -> new EffectList(new RevealOwnIdentityEffect());
                case EVIL_EYE -> new EffectList(new ChooseNextEffect(), new NextMustAccuseOtherEffect());
                case BLACK_CAT -> new EffectList(new DiscardedToHandEffect());
                case PET_NEWT -> new EffectList(new TakeRevealedFromOtherEffect(), new ChooseNextEffect());
                default -> new EffectList(new ChooseNextEffect());
            };

            cards.add(new RumourCard(cardName, witchEffects, huntEffect));
        }
        shuffle();
    }

    /**
     * Shuffle the cards.
     * 
     */
    public void shuffle() {
        Collections.shuffle(cards);
    }

    /**
     * Remove top card rumour card.
     *
     * @return the rumour card
     * @see com.model.card.RumourCard
     */
    public RumourCard removeTopCard() {
        return cards.poll();
    }

    /**
     * Return card to deck.
     *
     * @param playingCard the playing card
     * @see com.model.card.RumourCard
     */
    public void returnCardToDeck(RumourCard playingCard) {
        cards.addLast(playingCard);
    }
}
