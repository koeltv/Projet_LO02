package com.model.game;

import com.model.card.RumourCard;

/**
 * The type Card state.
 */
public class CardState {
    /**
     * The linked Rumour card.
     */
    public final RumourCard rumourCard;

    private boolean revealed;

    /**
     * Instantiates a new Card state.
     *
     * @param rumourCard the rumour card
     */
    public CardState(RumourCard rumourCard) {
        this.rumourCard = rumourCard;
    }

    /**
     * Is revealed boolean.
     *
     * @return the boolean
     */
    public boolean isRevealed() {
        return this.revealed;
    }

    /**
     * Sets revealed state.
     *
     * @param revealed the revealed state
     */
    public void setRevealed(boolean revealed) {
        this.revealed = revealed;
    }

}
