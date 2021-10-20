package game;

import player.Player;

/**
 * The type Identity card.
 */
public class IdentityCard {
    /**
     * The Player linked to this card.
     */
    public final Player player;

    private boolean witch;

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
        // Automatically generated method. Please delete this comment before entering specific code.
        this.witch = witch;
    }

    /**
     * Is the player a witch.
     *
     * @return whether the player is a witch or not
     */
    public boolean isWitch() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.witch;
    }

    /**
     * Is identity revealed boolean.
     *
     * @return whether the identity is revealed or not
     */
    public boolean isIdentityRevealed() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.identityRevealed;
    }

    /**
     * Sets identity revealed.
     *
     * @param identityRevealed whether the identity is revealed or not
     */
    public void setIdentityRevealed(final boolean identityRevealed) {
        // Automatically generated method. Please delete this comment before entering specific code.
        this.identityRevealed = identityRevealed;
    }

}
