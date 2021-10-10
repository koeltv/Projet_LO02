
public class CardState {
    public final RumourCard rumourCard;

    private boolean revealed;

    CardState(RumourCard rumourCard) {
        this.rumourCard = rumourCard;
    }

    public boolean isRevealed() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.revealed;
    }

    public void setRevealed(final boolean value) {
        // Automatically generated method. Please delete this comment before entering specific code.
        this.revealed = value;
    }

}
