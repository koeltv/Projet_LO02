import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class Round {
    private static Round round = new Round();

    private static int numberOfRound;

    private static Player currentPlayer;

    private static int numberOfNotRevealedPlayers;

    private Player nextPlayer;

    public List<RumourCard> discardPile = new ArrayList<>();

    public List<IdentityCard> activePlayers = new ArrayList<>();

    private Round() {}

    public static Round getRound() {
        return round;
    }

    public static int getNumberOfRound() {
        return numberOfRound;
    }

    public static int getNumberOfNotRevealedPlayers() {
        return numberOfNotRevealedPlayers;
    }

    public static void setNumberOfNotRevealedPlayers(int value) {
        numberOfNotRevealedPlayers = value;
    }

    public static Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Player getNextPlayer() {
        return this.nextPlayer;
    }

    public void setNextPlayer(Player value) {
        this.nextPlayer = value;
    }

    private void askCurrentPlayerForAction() {
        int action = 0;
        Scanner scanner = new Scanner(System.in);
        //Choosing action
        if (Round.getCurrentPlayer().hand.size() < 1) {
            System.out.println("You don't have any card to play");
        } else { //TODO Take care of IA
            System.out.println(Round.getCurrentPlayer().getName() + ", what do you want to do ? (0 to Accuse someone, 1 to Use a card)");
            action = scanner.nextInt();
        }
        //Executing action
        if (action > 0) { //Use card action
            boolean cardHasBeenUsedCorrectly;
            if (Round.getCurrentPlayer().hand.size() > 1) { //If the player has at least 2 cards
                do {
                    System.out.println("Which card do you want to use");
                    //TODO Display hand
                    cardHasBeenUsedCorrectly = Round.getCurrentPlayer().hand.get(scanner.nextInt()).rumourCard.useCard(Round.getCurrentPlayer());
                } while (!cardHasBeenUsedCorrectly); //Used to verify that the card condition where applicable
            } else {
                do {
                    cardHasBeenUsedCorrectly = Round.getCurrentPlayer().hand.get(0).rumourCard.useCard(Round.getCurrentPlayer());
                } while (!cardHasBeenUsedCorrectly);
            }
        } else { //Accusing action
            System.out.println("Who do you want to accuse");
            Player accusedPlayer = null; //TODO Get accused player
            Round.getCurrentPlayer().accuse(accusedPlayer);
        }
    }

    private void distributeRumourCards() {
        int nbOfPlayers = Game.getGame().players.size();
        int nbOfExcessCards = CardName.values().length % nbOfPlayers;

        //Take care of excess cards
        if (nbOfExcessCards != 0) {
            for (int i = 0; i < nbOfExcessCards; i++) {
                int index = Game.randomInInterval(0, Game.getGame().deck.size());
                this.discardPile.add(Game.getGame().deck.get(index));
                Game.getGame().deck.remove(index);
            }
        }

        //Distribute the rest equally
        int numberOfCardsPerPlayer = CardName.values().length / nbOfPlayers;
        for (Player player : Game.getGame().players) {
            for (int i = 0; i < numberOfCardsPerPlayer; i++) {
                int index = Game.randomInInterval(0, Game.getGame().deck.size());
                player.addCardToHand(Game.getGame().deck.get(index));
                Game.getGame().deck.remove(index);
            }
        }
    }

    private void askPlayersForIdentity() {
        for (IdentityCard identityCard : Game.getGame().round.activePlayers) {
            if (!(identityCard.player instanceof AI)) {
                System.out.println(identityCard.player.getName() + ", type 0 for villager and 1 for witch");
                Scanner scanner = new Scanner(System.in);
                identityCard.setWitch(scanner.nextInt() > 0);
            } else {
                //TODO Use AI Strategy
            }
        }
    }

    private void selectFirstPlayer() {
        Round.currentPlayer = Game.getGame().players.get(Game.randomInInterval(0, Game.getGame().players.size()));
    }

    public void startRound() {
        for (Player player: Game.getGame().players) {
            round.activePlayers.add(new IdentityCard(player));
        }
        if (currentPlayer == null) selectFirstPlayer();
        this.distributeRumourCards();
        this.askPlayersForIdentity();
        numberOfRound++;
        playRound();
    }

    private void playRound() { //TODO Integrate
        do {
            askCurrentPlayerForAction();
            Round.currentPlayer = this.nextPlayer;
            //TODO Playing loop, right now the loop is endless
        } while (Game.getGame().players.size() - numberOfNotRevealedPlayers > 1);
        //TODO Add to score of winner
        endRound();
    }

    private void endRound() {
        //Gather all players cards
        for (Player player : Game.getGame().players) {
            int startingNumberOfCard = player.hand.size();
            for (int i = 0; i < startingNumberOfCard; i++) {
                RumourCard removedCard = player.hand.get(0).rumourCard;
                player.removeCardFromHand(removedCard);
                Game.getGame().deck.add(removedCard);
            }
        }
        //Gather the discarded cards
        int startingNumberOfCard = this.discardPile.size();
        for (int i = 0; i < startingNumberOfCard; i++) {
            RumourCard removedCard = this.discardPile.get(0);
            this.discardPile.remove(removedCard);
            Game.getGame().deck.add(removedCard);
        }
        round = new Round();
    }

}
