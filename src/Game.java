import java.util.ArrayList;
import java.util.List;

public class Game {
    private Player lastPlayer;

    public List<Player> player = new ArrayList<Player> ();

    public List<RumourCard> rumourCard = new ArrayList<RumourCard> ();

    public DiscardPile discardPile;

    public Round round;

    public void selectFirstPlayer() {
    }

    public void askForPlayerRepartition() {
    }

    public void startRound() {
    }

    public void endRound() {
    }

    public void verifyScores() {
    }

    public Player getLastPlayer() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.lastPlayer;
    }

    public void setLastPlayer(final Player value) {
        // Automatically generated method. Please delete this comment before entering specific code.
        this.lastPlayer = value;
    }

    public void settleTie() {
    }

    public void setupGame() {
    }

}
