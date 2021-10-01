import java.util.ArrayList;
import java.util.List;

public class Player {
    private int score;

    private String name;

    private boolean witch;

    private boolean identityRevealed;

    public static int numberOfNotRevealedPlayers;

    private static int numberOfPlayers;

    public List<RumourCard> rumourCard = new ArrayList<RumourCard> ();

    public int getScore() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.score;
    }

    public void setScore(final int value) {
        // Automatically generated method. Please delete this comment before entering specific code.
        this.score = value;
    }

    public String getName() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.name;
    }

    public void setName(final String value) {
        // Automatically generated method. Please delete this comment before entering specific code.
        this.name = value;
    }

    public boolean isWitch() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.witch;
    }

    public void setWitch(final boolean value) {
        // Automatically generated method. Please delete this comment before entering specific code.
        this.witch = value;
    }

    public boolean isIdentityRevealed() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.identityRevealed;
    }

    public void setIdentityRevealed(final boolean value) {
        // Automatically generated method. Please delete this comment before entering specific code.
        this.identityRevealed = value;
    }

    public void accuse(final Player accusedPlayer) {
    }

    public void revealRumorCard(final RumourCard cardToReveal) {
    }

    public void revealIdentity() {
    }

    public static int getNumberOfPlayers() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return numberOfPlayers;
    }

    public static void setNumberOfPlayers(final int value) {
        // Automatically generated method. Please delete this comment before entering specific code.
        numberOfPlayers = value;
    }

}
