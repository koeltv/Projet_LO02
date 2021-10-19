package game;

import card.CardName;
import card.RumourCard;
import player.Player;

import java.util.ArrayList;
import java.util.List;

public class Round {
    private static Round round = new Round();

    private static int numberOfRound;

    private static Player currentPlayer;

    private int numberOfNotRevealedPlayers;

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

    public int getNumberOfNotRevealedPlayers() {
        return numberOfNotRevealedPlayers;
    }

    public void setNumberOfNotRevealedPlayers(int value) {
        numberOfNotRevealedPlayers = value;
    }

    public static Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setNextPlayer(Player value) {
        this.nextPlayer = value;
    }

    /**
     * Ask the current player for his next action
     * This method will call the play method of the current player
     */
    private void askCurrentPlayerForAction() {
        Round.getCurrentPlayer().play();
    }

    /**
     * Distribute Rumour cards
     * This method distribute the Rumour cards at the start of a round based on the number of players
     */
    private void distributeRumourCards() {
        int nbOfPlayers = this.activePlayers.size();
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
                int index = Game.randomInInterval(0, Game.getGame().deck.size() - 1);
                player.addCardToHand(Game.getGame().deck.get(index));
                Game.getGame().deck.remove(index);
            }
        }
    }

    /**
     * Ask players for their chosen identity
     * This method will call the selectIdentity() method to prompt players to choose a role for the round
     */
    private void askPlayersForIdentity() {
        for (IdentityCard identityCard : Game.getGame().round.activePlayers) {
            identityCard.player.selectIdentity();
        }
    }

    /**
     * Select the first player
     * This method will only be used on the first round to select a random player to start
     */
    private void selectFirstPlayer() {
        List<Player> playerList = Game.getGame().players;
        Round.currentPlayer = playerList.get(Game.randomInInterval(0, playerList.size() - 1));
    }

    /**
     * Set up the round
     * This method will do everything necessary to set up a round (select 1st player, create identity cards, distribute Rumour cards, ask players for identity)
     */
    public void startRound() {
        System.out.println("===============================");
        if (currentPlayer == null) selectFirstPlayer();
        //Fill up the list of active players at the start
        for (Player player: Game.getGame().players) {
            IdentityCard playerIdentityCard = new IdentityCard(player);
            round.activePlayers.add(playerIdentityCard);
            player.identityCard = playerIdentityCard;
        }
        this.numberOfNotRevealedPlayers = this.activePlayers.size();

        this.distributeRumourCards();
        this.askPlayersForIdentity();
        numberOfRound++;
        playRound();
    }

    /**
     * Round playing loop
     * This method will prompt the current player for action, then set the current player to the next and loop while there is more than 1 not revealed player
     */
    private void playRound() {
        do {
            askCurrentPlayerForAction();
            if (this.numberOfNotRevealedPlayers > 1) Round.currentPlayer = this.nextPlayer;
            //TODO Playing loop, currently there is a risk of not setting next player
        } while (this.numberOfNotRevealedPlayers > 1);
        endRound();
    }

    /**
     * Wrap up the round
     * This method will do everything necessary to wrap up a round (reveal last player and give him points, gather all cards)
     */
    private void endRound() {
        //We search the last not revealed player, reveal is identity and give him points
        for (IdentityCard identityCard : this.activePlayers) {
            if (!identityCard.isIdentityRevealed()) {
                //Reveal player identity and give points
                System.out.println(identityCard.player.getName() + " won this round !");
                identityCard.player.revealIdentity();
                identityCard.player.addToScore(identityCard.player.identityCard.isWitch() ? 2 : 1);
                //Set him to be first for the next round
                Round.currentPlayer = identityCard.player;
                break;
            }
        }
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
        Round.round = new Round();
    }

}
