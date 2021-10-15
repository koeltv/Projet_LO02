
public class IdentityCard {
    public final Player player;

    private boolean witch;

    private boolean identityRevealed;

    public IdentityCard(final Player player) {
        this.player = player;
    }

    public void setWitch(final boolean value) {
        // Automatically generated method. Please delete this comment before entering specific code.
        this.witch = value;
    }

    public boolean isWitch() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.witch;
    }

    public boolean isIdentityRevealed() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.identityRevealed;
    }

    public void setIdentityRevealed(final boolean value) {
        // Automatically generated method. Please delete this comment before entering specific code.
        this.identityRevealed = value;
    }

}
