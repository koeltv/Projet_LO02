
public class Round {
    private static int numberOfRound;

    private int numberOfTurns;

    private Player currentPlayer;

    private Player nextPlayer;

    public static int getNumberOfRound() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return numberOfRound;
    }

    public static void setNumberOfRound(final int value) {
        // Automatically generated method. Please delete this comment before entering specific code.
        numberOfRound = value;
    }

    public int getNumberOfTurns() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.numberOfTurns;
    }

    public void setNumberOfTurns(final int value) {
        // Automatically generated method. Please delete this comment before entering specific code.
        this.numberOfTurns = value;
    }

    public Player getCurrentPlayer() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.currentPlayer;
    }

    public void setCurrentPlayer(final Player value) {
        // Automatically generated method. Please delete this comment before entering specific code.
        this.currentPlayer = value;
    }

    public Player getNextPlayer() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.nextPlayer;
    }

    public void setNextPlayer(final Player value) {
        // Automatically generated method. Please delete this comment before entering specific code.
        this.nextPlayer = value;
    }

    public void askCurrentPlayerForAction() {
    }

}
