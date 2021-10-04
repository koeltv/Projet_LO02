import java.util.ArrayList;
import java.util.List;

public class Game {
    private Player lastPlayer;

    public List<Player> player = new ArrayList<Player> ();

    public DiscardPile discardPile;

    public Round round;

    public List<RumourCard> rumourCard = new ArrayList<RumourCard> ();

    public void askForPlayerRepartition() {
    }

    private void startRound() {
    }

    public void endRound() {
    }

    private void verifyScores() {
    }

    public Player getLastPlayer() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.lastPlayer;
    }

    public void setLastPlayer(final Player value) {
        // Automatically generated method. Please delete this comment before entering specific code.
        this.lastPlayer = value;
    }

    private void settleTie() {
    }

    public void setupGame() {
    }

}
