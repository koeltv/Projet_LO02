import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public abstract class Game {
    public static List<Player> player = new ArrayList<Player> ();

    public static List<RumourCard> rumourCard = new ArrayList<RumourCard> ();

    public static Round round;

    private static void askForPlayerRepartition() {
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
                    player.add(i, new Player(sc.next()));
                }
                for (int i = nbPlayers; i < nbPlayers + nbAIs; i++) {
                    System.out.println("Which difficulty for AI n°" + (i + 1 - nbPlayers) + " (0, 1, 2) ?");
                    player.add(i, new AI(sc.nextInt()));
                }
    }

    private static void startRound() {
        round = new Round();
    }

    private static void endRound() {
        //Gather all players cards
        for (Player player : Game.player) {
            if (player.rumourCard != null) {
                int startingNumberOfCard = player.rumourCard.size();
                for (int i = 0; i < startingNumberOfCard; i++) {
                    RumourCard removedCard = player.rumourCard.get(0);
                    player.rumourCard.remove(removedCard);
                    Game.rumourCard.add(removedCard);
                }
            }
        }
        //Gather the discarded cards
        if (DiscardPile.rumourCard != null) {
            int startingNumberOfCard = DiscardPile.rumourCard.size();
            for (int i = 0; i < startingNumberOfCard; i++) {
                RumourCard removedCard = DiscardPile.rumourCard.get(0);
                DiscardPile.rumourCard.remove(removedCard);
                Game.rumourCard.add(removedCard);
            }
        }
        //Remember winning player
        round = null;
    }

    private static boolean verifyScores() {
        for (Player player : player)
            if (player.getScore() >= 5) return true;
        return false;
    }

    private static void settleTie() {
    }

    private static void setupGame() {
        for (CardName cardName : CardName.values()) {
            rumourCard.add(new RumourCard(cardName));
        }
    }

    private static void wrapUpGame() {
        List<Player> winners = new ArrayList<>();
        
                for (Player player : player) {
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

    public static int randomInInterval(int min, int max) {
        Random random = new Random();
        return random.nextInt(max-min) + min;
    }

    public static void main(String[] args) {
        askForPlayerRepartition();
        setupGame();
        do {
            startRound();
            endRound();
        } while (verifyScores());
        wrapUpGame();
    }
}
