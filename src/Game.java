import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class Game extends Observable {
    private static final Game game = new Game();

    public final List<Player> players = new ArrayList<>();

    public final List<RumourCard> deck = new ArrayList<>();

    public Round round = Round.getRound();

    private Game() {}

    public static Game getGame() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return game;
    }

    private void askForPlayerRepartition() {
        Scanner sc = new Scanner(System.in);
        int nbPlayers, nbAIs;
        do {
            System.out.println("Number of players ?");
            nbPlayers = sc.nextInt();
            System.out.println("Number of AI ?");
            nbAIs = sc.nextInt();
        } while (nbPlayers + nbAIs < 3 || nbPlayers + nbAIs > 6);
        
                for (int i = 0; i < nbPlayers; i++) {
                    System.out.println("Name of player " + (i+1) + " ?");
                    this.players.add(i, new Player(sc.next()));
                }
                for (int i = nbPlayers; i < nbPlayers + nbAIs; i++) {
                    this.players.add(i, new AI());
                }
    }

    private boolean verifyScores() {
        for (Player player : this.players)
            if (player.getScore() >= 5) return true;
        return false;
    }

    private void settleTie() {
    }

    private void setupGame() { //TODO Add the effects to the cards
        for (CardName cardName : CardName.values()) {
            RumourCard rumourCard = new RumourCard(
                    cardName,
                    //Witch? effects
                    switch (cardName) {
                        case ANGRY_MOB -> null;
                        case THE_INQUISITION -> null;
                        case POINTED_HAT -> null;
                        case HOOKED_NOSE -> null;
                        case BROOMSTICK -> null;
                        case WART -> null;
                        case DUCKING_STOOL -> null;
                        case CAULDRON -> null;
                        case EVIL_EYE -> null;
                        case TOAD -> null;
                        case BLACK_CAT -> null;
                        case PET_NEWT -> null;
                    },
                    //Hunt! Effects
                    switch (cardName) {
                        case ANGRY_MOB -> null;
                        case THE_INQUISITION -> null;
                        case POINTED_HAT -> null;
                        case HOOKED_NOSE -> null;
                        case BROOMSTICK -> null;
                        case WART -> null;
                        case DUCKING_STOOL -> null;
                        case CAULDRON -> null;
                        case EVIL_EYE -> null;
                        case TOAD -> null;
                        case BLACK_CAT -> null;
                        case PET_NEWT -> null;
                    }
            );
            this.deck.add(rumourCard);
        }
    }

    private void wrapUpGame() {
        List<Player> winners = new ArrayList<>();
        
                for (Player player : this.players) {
                    if (player.getScore() >= 5) winners.add(player);
                }
        
                if (winners.size() > 1) {
                    settleTie();
                } else if (winners.size() == 1){
                    System.out.println("Congratulations " + winners.get(0) + ", you won this game !");
                } else {
                    System.out.println("No winner ? Oh wait...");
                }
    }

    public void sendGameState() {
    }

    public static int randomInInterval(int min, int max) {
        Random random = new Random();
        return random.nextInt(max-min) + min;
    }

    public static void main(String[] args) {
        Game game = Game.getGame();
        game.askForPlayerRepartition();
        game.setupGame();
        do {
            game.round.startRound();
        } while (game.verifyScores());
        game.wrapUpGame();
    }

}
