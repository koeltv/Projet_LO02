package com.model.player;

import com.model.card.RumourCard;
import com.model.game.CardState;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Player.
 * 
 * Gives all the methods related to the Player.
 */
public class Player {
    
	/**
     * The Score.
     */
    private int score;

    /**
     * The Name.
     */
    private final String name;

    /**
     * The Hand.
     * Hand of the player including revealed cards
     */
    private final List<CardState> hand;

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
     * Add to score.
     *
     * @param value the value to add
     */
    public void addToScore(int value) {
        this.score += value;
    }

    /**
     * Reset score.
     */
    public void resetScore() {
        this.score = 0;
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
     * Gets hand.
     *
     * @return the hand
     * @see com.model.game.CardState
     */
    public List<CardState> getHand() {
        return this.hand;
    }

    /**
     * Add card to hand.
     * This method adds the chosen card to the player's hand.
     *
     * @param rumourCard the rumour card
     * @see com.model.game.CardState
     * @see com.model.card.RumourCard
     */
    public void addCardToHand(RumourCard rumourCard) {
        this.hand.add(new CardState(rumourCard));
    }

    /**
     * Remove card from hand.
     * This method removes the chosen card from the player's hand.
     *
     * @param rumourCard the rumour card to remove
     * @return removed rumour card or null if the card wasn't found
     * @see com.model.card.RumourCard
     */
    public RumourCard removeCardFromHand(RumourCard rumourCard) {
        return this.hand.removeIf(card -> card.rumourCard == rumourCard) ? rumourCard : null;
    }

    /**
     * Reveal a Rumour card.
     * This method reveals the chosen card from the player's hand and call its effects.
     *
     * @param cardToReveal card to reveal
     * @return whether the card has been used successfully or not
     * @see com.model.card.RumourCard
     * @see com.model.game.CardState
     */
    public boolean revealRumourCard(RumourCard cardToReveal) {
        boolean cardUsedSuccessfully = cardToReveal.useCard(this);
        if (cardUsedSuccessfully) {
            hand.stream()
                    .filter(cardState -> cardToReveal == cardState.rumourCard)
                    .findFirst()
                    .ifPresent(cardState -> cardState.setRevealed(true));
        }
        return cardUsedSuccessfully;
    }

    /**
     * Get the list of not revealed cards from the player's hand.
     *
     * @return selectable cards
     * @see com.model.card.RumourCard
     * @see com.model.game.CardState
     */
    public List<RumourCard> getSelectableCardsFromHand() {
        return hand.stream()
                .filter(cardState -> !cardState.isRevealed())
                .map(cardState -> cardState.rumourCard)
                .collect(Collectors.toList());
    }

    /**
     * Get the list of revealed cards from the player's hand.
     *
     * @return revealed cards
     * @see com.model.card.RumourCard
     * @see com.model.game.CardState
     */
    public List<RumourCard> getRevealedCards() {
        return hand.stream()
                .filter(CardState::isRevealed)
                .map(cardState -> cardState.rumourCard)
                .collect(Collectors.toList());
    }
}
