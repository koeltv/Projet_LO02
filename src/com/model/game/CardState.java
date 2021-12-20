package com.model.game;

import com.model.card.RumourCard;

import java.io.Serializable;

/**
 * The type Card state.
 */
public class CardState implements Serializable {
	/**
	 * The linked Rumour card.
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
