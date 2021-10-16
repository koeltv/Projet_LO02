package game;

import card.RumourCard;

public class CardState {
    public final RumourCard rumourCard;

    private boolean revealed;

    public CardState(RumourCard rumourCard) {
        this.rumourCard = rumourCard;
    }

    public boolean isRevealed() {
        return this.revealed;
    }

    public void setRevealed(boolean value) {
        this.revealed = value;
    }

}
