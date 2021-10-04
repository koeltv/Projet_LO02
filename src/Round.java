import java.util.Scanner;

public class Round {
    private static int numberOfRound;

    private static Player currentPlayer;

    private Player nextPlayer;

    private static int numberOfNotRevealedPlayers;

    public static int getNumberOfRound() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return numberOfRound;
    }

    public static Player getCurrentPlayer() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return currentPlayer;
    }

    public Player getNextPlayer() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.nextPlayer;
    }

    public void setNextPlayer(final Player value) {
        // Automatically generated method. Please delete this comment before entering specific code.
        this.nextPlayer = value;
    }

    private void askCurrentPlayerForAction() {
    }

    private void distributeRumourCards() {
        int numberOfCardsPerPlayer = switch (Game.player.size()) {
            case 3 -> 4;
            case 4 -> 3;
            case 5 -> {
                for (int i = 0; i < 2; i++) {
                    int index = Game.randomInInterval(0, Game.rumourCard.size());
                    DiscardPile.rumourCard.add(Game.rumourCard.get(index));
                    Game.rumourCard.remove(index);
                }
                yield 2;
            }
            case 6 -> 2;
            default -> throw new IllegalStateException("Unexpected value: " + Game.player.size());
        };
        
                for (Player player : Game.player) {
                    for (int i = 0; i < numberOfCardsPerPlayer; i++) {
                        int index = Game.randomInInterval(0, Game.rumourCard.size());
                        player.rumourCard.add(Game.rumourCard.get(index));
                        Game.rumourCard.remove(index);
                    }
                }
    }

    private void askPlayersForIdentity() {
        for (Player player : Game.player) {
            if (!(player instanceof AI)) {
                System.out.println(player.getName() + ", type 0 for villager and 1 for witch");
                Scanner scanner = new Scanner(System.in);
                player.setWitch(scanner.nextInt() > 0);
            }
        }
    }

    private void selectFirstPlayer() {
        if (currentPlayer == null) currentPlayer = Game.player.get(Game.randomInInterval(0, Game.player.size()));
    }

    public static int getNumberOfNotRevealedPlayers() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return numberOfNotRevealedPlayers;
    }

    public static void setNumberOfNotRevealedPlayers(final int value) {
        // Automatically generated method. Please delete this comment before entering specific code.
        numberOfNotRevealedPlayers = value;
    }

    Round() {
        if (currentPlayer == null) selectFirstPlayer();
        this.distributeRumourCards();
        this.askPlayersForIdentity();
        numberOfRound++;
        do {
        } while (Game.player.size() - numberOfNotRevealedPlayers > 1);
    }

}
