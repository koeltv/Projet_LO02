package com.model.game;

import com.model.card.RumourCard;

/**
 * The type Card state.
 * <p>
 * Gives all the methods related to card state.
 */
public class CardState {
    /**
     * The linked Rumour card.
     * 
     * @see com.model.card.RumourCard
     */
    public final RumourCard rumourCard;

    /**
     * Is Revealed boolean.
     */
    private boolean revealed;

    /**
     * Instantiates a new Card state.
     *
     * @param rumourCard the rumour card
     * @see com.model.card.RumourCard
     */
    public CardState(RumourCard rumourCard) {
        this.rumourCard = rumourCard;
    }

    /**
     * Is revealed boolean.
     *
     * @return true if it is revealed, false otherwise
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
