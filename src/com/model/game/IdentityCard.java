package com.model.game;

import com.model.player.Player;

import java.io.Serializable;

/**
 * The type Identity card.
 *
 * Gives all the methods related to identity cards.
 */
public class IdentityCard implements Serializable {

    /**
	 * The Player linked to this card.
     *
     * @see com.model.player.Player
	 */
	public final Player player;

    /**
     * The Witch (2 possible identities depending on the player's choice)
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
     * @see com.model.player.Player
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
     * Is identity revealed boolean.
     *
     * @return whether the identity is revealed or not
     */
    public boolean isIdentityRevealed() {
        return this.identityRevealed;
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
