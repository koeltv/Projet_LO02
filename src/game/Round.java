package game;

import card.CardName;
import card.RumourCard;
import player.AI;
import player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Round {
    private static Round round = new Round();

    private static int numberOfRound;

    private static Player currentPlayer;

    private static int numberOfNotRevealedPlayers;

    private Player nextPlayer;

    public final List<RumourCard> discardPile = new ArrayList<>();

    public final List<IdentityCard> activePlayers = new ArrayList<>();

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
        Round.getCurrentPlayer().play();
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
        List<Player> playerList = Game.getGame().players;
        Round.currentPlayer = playerList.get(Game.randomInInterval(0, playerList.size()));
    }

    public void startRound() {
        if (currentPlayer == null) selectFirstPlayer();
        //Fill up the list of active players at the start
        for (Player player: Game.getGame().players) {
            IdentityCard playerIdentityCard = new IdentityCard(player);
            round.activePlayers.add(playerIdentityCard);
            player.identityCard = playerIdentityCard;
        }
        this.distributeRumourCards();
        this.askPlayersForIdentity();
        numberOfRound++;
        playRound();
    }

    private void playRound() {
        do {
            askCurrentPlayerForAction();
            Round.currentPlayer = this.nextPlayer;
            //TODO Playing loop, currently there is a risk of not setting next player
        } while (Game.getGame().players.size() - numberOfNotRevealedPlayers > 1);

        //We search the last not revealed player, reveal is identity and give him points
        Player roundWinner = null;
        for (IdentityCard identityCard : this.activePlayers) {
            if (!identityCard.isIdentityRevealed()) {
                roundWinner = identityCard.player;
                break;
            }
        }
        if (roundWinner != null) {
            roundWinner.revealIdentity();
            roundWinner.setScore(roundWinner.getScore() + (roundWinner.identityCard.isWitch() ? 2 : 1));
        }

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
