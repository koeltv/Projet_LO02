package com.model.game;

import com.model.player.Player;

/**
 * The type Identity card.
 */
public class IdentityCard {
    /**
     * The Player linked to this card.
     */
    public final Player player;

    /**
     * The Witch.
     */
    private boolean witch;

    /**
     * The Identity revealed.
     */
    private boolean identityRevealed;

    /**
     * Instantiates a new Identity card.
     *
     * @param player the linked player
     */
    public IdentityCard(final Player player) {
        this.player = player;
    }

    /**
     * Sets whether the player is a witch or not.
     *
     * @param witch player identity
     */
    public void setWitch(final boolean witch) {
        this.witch = witch;
    }

    /**
     * Is the player a witch.
     *
     * @return whether the player is a witch or not
     */
    public boolean isWitch() {
        return this.witch;
    }

    /**
     * Is identity not revealed boolean.
     *
     * @return whether the identity is revealed or not
     */
    public boolean isIdentityNotRevealed() {
        return !this.identityRevealed;
    }

    /**
     * Sets identity revealed.
     *
     * @param identityRevealed whether the identity is revealed or not
     */
    public void setIdentityRevealed(final boolean identityRevealed) {
        this.identityRevealed = identityRevealed;
    }

}
