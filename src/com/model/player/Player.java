package com.model.player;

import com.model.card.RumourCard;
import com.model.game.CardState;
import com.model.game.IdentityCard;
import com.controller.RoundController;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Player.
 */
public class Player {
    private int score;

    private final String name;

    /**
     * The Identity card.
     * Identity card of the player attributed at the start of each round.
     */
    public IdentityCard identityCard; //TODO Check if necessary

    /**
     * The Hand.
     * Hand of the player including revealed cards
     */
    public final List<CardState> hand = new ArrayList<>();

    /**
     * Instantiates a new Player.
     *
     * @param name the player name
     */
    public Player(final String name) {
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

    public void resetScore() {
        this.score = 0;
    }

    /**
     * Add to score.
     *
     * @param value the value
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
     */
    public void removeCardFromHand(RumourCard rumourCard) {
        this.hand.removeIf(cardState -> cardState.rumourCard == rumourCard);
    }

    /**
     * Accuse the chosen player
     *
     * @param accusedPlayer player to accuse
     */
    public void accuse(Player accusedPlayer) {
        if (accusedPlayer.identityCard.isIdentityRevealed() && accusedPlayer.identityCard.isWitch()) {
            addToScore(1);
        }
    }

    /**
     * Reveal a Rumour card
     * This method reveal the chosen card from the player hand and call its effects
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

    /**
     * Reveal the player identity
     */
    public void revealIdentity() {
        this.identityCard.setIdentityRevealed(true);
        if (this.identityCard.isWitch()) {
            //If a player is revealed as a witch, we exclude him from the round
            RoundController.getRoundController().identityCards.removeIf(identityCard -> identityCard.player == this);
            RoundController.getRoundController().setNextPlayer(RoundController.getCurrentPlayer());
        } else {
            RoundController.getRoundController().setNextPlayer(this);
        }
    }

}
