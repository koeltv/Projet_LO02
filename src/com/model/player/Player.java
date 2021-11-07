package com.model.player;

import com.model.card.RumourCard;
import com.model.game.CardState;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Player.
 */
public class Player {
    private int score;

    private final String name;

    /**
     * The Hand.
     * Hand of the player including revealed cards
     */
    public final List<CardState> hand;

    /**
     * Instantiates a new Player.
     *
     * @param name the player name
     */
    public Player(final String name) {
        this.hand = new ArrayList<>();
        this.name = name;
    }

    /**
     * Gets score.
     *
     * @return the score
     */
    public int getScore() {
        return this.score;
    }

    /**
     * Reset score.
     */
    public void resetScore() {
        this.score = 0;
    }

    /**
     * Add to score.
     *
     * @param value the value to add
     */
    public void addToScore(int value) {
        this.score += value;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Add card to hand.
     *
     * @param rumourCard the rumour card
     */
    public void addCardToHand(RumourCard rumourCard) {
        this.hand.add(new CardState(rumourCard));
    }

    /**
     * Remove card from hand.
     *
     * @param rumourCard the rumour card to remove
     * @return removed rumour card or null if the card wasn't found
     */
    public RumourCard removeCardFromHand(RumourCard rumourCard) {
        if (this.hand.removeIf(cardState -> cardState.rumourCard == rumourCard)) {
            return rumourCard;
        } else {
            return null;
        }
    }

    /**
     * Reveal a Rumour card.
     * This method reveal the chosen card from the player hand and call its effects.
     *
     * @param cardToReveal card to reveal
     * @return whether the card has been used successfully or not
     */
    public boolean revealRumourCard(RumourCard cardToReveal) {
        for (CardState cardState : hand) {
            if (cardToReveal == cardState.rumourCard) {
                cardState.setRevealed(true);
                break;
            }
        }
        return cardToReveal.useCard(this);
    }

}
