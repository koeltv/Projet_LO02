package game;

import card.CardName;
import card.RumourCard;
import card.effect.*;
import player.AI;
import player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * The type Game.
 * Game is the main class that supervise the whole game. It is a singleton.
 */
public class Game extends Observable {
    private static final Game game = new Game();

    /**
     * The Players.
     * List of all the players in the game
     */
    public final List<Player> players = new ArrayList<>();

    /**
     * The Deck.
     */
    public final List<RumourCard> deck = new ArrayList<>();

    /**
     * The Round.
     */
    public Round round = Round.getRound();

    private Game() {}

    /**
     * Gets game instance.
     *
     * @return the game instance
     */
    public static Game getGame() {
        return game;
    }

    /**
     * Set up the game
     * This is where the cards are instantiated before a new game
     */
    private void setupGame() {
        for (CardName cardName : CardName.values()) {
            List<Effect> witchEffects = new ArrayList<>();
            List<Effect> huntEffect = new ArrayList<>();

            //Witch? effects
            switch (cardName) {
                case THE_INQUISITION -> {
                    witchEffects.add(new DiscardFromHandEffect());
                    witchEffects.add(new TakeNextTurnEffect());
                }
                case POINTED_HAT -> {
                    witchEffects.add(new TakeRevealedCardEffect());
                    witchEffects.add(new TakeNextTurnEffect());
                }
                case HOOKED_NOSE -> {
                    witchEffects.add(new TakeFromAccuserHandEffect());
                    witchEffects.add(new TakeNextTurnEffect());
                }
                case DUCKING_STOOL -> witchEffects.add(new ChooseNextEffect());
                case CAULDRON -> {
                    witchEffects.add(new AccuserDiscardRandomEffect());
                    witchEffects.add(new TakeNextTurnEffect());
                }
                case EVIL_EYE -> {
                    witchEffects.add(new ChooseNextEffect());
                    witchEffects.add(new NextMustAccuseOtherEffect());
                }
                default -> witchEffects.add(new TakeNextTurnEffect());
            }
            //Hunt! Effects
            switch (cardName) {
                case ANGRY_MOB -> huntEffect.add(new RevealAnotherIdentityEffect());
                case THE_INQUISITION -> {
                    huntEffect.add(new ChooseNextEffect());
                    huntEffect.add(new SecretlyReadIdentityEffect());
                }
                case POINTED_HAT -> {
                    huntEffect.add(new TakeRevealedCardEffect());
                    huntEffect.add(new ChooseNextEffect());
                }
                case HOOKED_NOSE -> {
                    huntEffect.add(new ChooseNextEffect());
                    huntEffect.add(new TakeRandomCardFromNextEffect());
                }
                case BROOMSTICK, WART -> huntEffect.add(new ChooseNextEffect());
                case DUCKING_STOOL -> huntEffect.add(new RevealOrDiscardEffect());
                case CAULDRON, TOAD -> huntEffect.add(new RevealOwnIdentityEffect());
                case EVIL_EYE -> {
                    huntEffect.add(new ChooseNextEffect());
                    huntEffect.add(new NextMustAccuseOtherEffect());
                }
                case BLACK_CAT -> huntEffect.add(new DiscardedToHandEffect());
                case PET_NEWT -> huntEffect.add(new TakeRevealedFromOtherEffect());
            }

            this.deck.add(new RumourCard(cardName, witchEffects, huntEffect));
        }
    }

    /**
     * Wrap up the game
     * This is where we congratulate the winner and settle ties if needed
     */
    private void wrapUpGame() {
        List<Player> winners = new ArrayList<>();

        for (Player player : this.players) {
            if (player.getScore() >= 5) winners.add(player);
        }

        if (winners.size() > 1) {
            settleTie();
        } else if (winners.size() == 1){
            System.out.println("Congratulations " + winners.get(0).getName() + ", you won this game !");
        } else {
            System.out.println("No winner ? Oh wait...");
        }
    }

    /**
     * Ask for player repartition
     * This function asks for the repartition of players and AIs, along with the players name
     */
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
            this.players.add(new Player(sc.next()));
        }
        for (int i = nbPlayers; i < nbPlayers + nbAIs; i++) {
            this.players.add(new AI());
        }
    }

    /**
     * Verify the scores
     * This function verify the score of each player to know the current state of the game
     * @return true if at least 1 player has at least 5 points, false otherwise
     */
    private boolean verifyScores() {
        for (Player player : this.players)
            if (player.getScore() >= 5) return true;
        return false;
    }

    /**
     * Settle ties
     * This function is used when more than 1 player has at least 5 points in order to decide a single winner
     */
    private void settleTie() {
    }

    /**
     * Get random integer in a given interval
     * This function is a utility function used to get a random integer between 2 limits (included)
     *
     * @param min the minimum value to be returned (included)
     * @param max the maximum value to be returned (included)
     * @return random integer in the interval
     */
    public static int randomInInterval(int min, int max) {
        Random random = new Random();
        return random.nextInt((max + 1)-min) + min;
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        Game game = Game.getGame();

        Console console = new Console();
        game.addObserver(console);

        game.askForPlayerRepartition();
        game.setupGame();
        do {
            game.round.startRound();
            game.round = Round.getRound();
        } while (!game.verifyScores());
        game.wrapUpGame();
    }

}
