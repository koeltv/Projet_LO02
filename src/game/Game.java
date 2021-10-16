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

public class Game extends Observable {
    private static final Game game = new Game();

    public final List<Player> players = new ArrayList<>();

    public final List<RumourCard> deck = new ArrayList<>();

    public final Round round = Round.getRound();

    private Game() {}

    public static Game getGame() {
        return game;
    }

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
