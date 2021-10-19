package player;

import card.RumourCard;
import game.CardState;
import game.Game;
import game.IdentityCard;
import game.Round;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Player {
    private int score;

    private final String name;

    public IdentityCard identityCard; //TODO Check if necessary

    public final List<CardState> hand = new ArrayList<>();

    public Player(final String name) {
        this.name = name;
    }

    public int getScore() {
        return this.score;
    }

    public void addToScore(int value) {
        this.score += value;
    }

    public String getName() {
        return this.name;
    }

    public void addCardToHand(RumourCard rumourCard) {
        this.hand.add(new CardState(rumourCard));
    }

    public void removeCardFromHand(RumourCard rumourCard) {
        this.hand.removeIf(cardState -> cardState.rumourCard == rumourCard);
    }

    public void accuse(Player accusedPlayer) {
        accusedPlayer.play();
        if (accusedPlayer.identityCard.isWitch()) {
            this.addToScore(1);
        }
    }

    public void revealRumorCard(RumourCard cardToReveal) {
        cardToReveal.useCard(this);
    }

    public void revealIdentity() {
        this.identityCard.setIdentityRevealed(true);
        System.out.print("Player " + this.getName() + " is a ");
        if (this.identityCard.isWitch()) {
            System.out.println("witch !");
            Game.getGame().round.setNextPlayer(Round.getCurrentPlayer());
        } else {
            System.out.println("villager !");
            Game.getGame().round.setNextPlayer(this);
        }
        Round.getRound().setNumberOfNotRevealedPlayers(Round.getRound().getNumberOfNotRevealedPlayers() - 1);
    }

    public void play() {
        if (this == Round.getCurrentPlayer()) {
            System.out.print(this.getName() + ", ");
            //hunt effect or accuse
            if (this.hand.size() < 1) {
                System.out.println("You don't have any cards left, who do you want to accuse ?");
                Player accusedPlayer = this.choosePlayer(); //TODO Make a choice list for the user
                this.accuse(accusedPlayer);
            } else {
                System.out.println("Do you want to accuse someone or use a Rumour Card ? (0 or 1)");
                Scanner scanner = new Scanner(System.in);
                int action = scanner.nextInt();
                if (action == 0) {
                    System.out.println("Who do you want to accuse ?");
                    Player accusedPlayer = this.choosePlayer(); //TODO Make a choice list for the user
                    this.accuse(accusedPlayer);
                } else {
                    System.out.println("Which card do you want to use ?");
                    RumourCard chosenRumourCard = this.chooseCard(); //TODO Make a choice list for the user
                    this.revealRumorCard(chosenRumourCard);
                }
            }
        } else {
            //witch effect or reveal identity
            if (this.hand.size() < 1) {
                System.out.println("You don't have any cards left, revealing your identity:");
                this.revealIdentity();
            } else {
                System.out.println("Do you want to reveal your identity or use a Rumour Card ? (0 or 1)");
                Scanner scanner = new Scanner(System.in);
                int action = scanner.nextInt();
                if (action == 0) {
                    System.out.println("Revealing your identity:");
                    this.revealIdentity();
                } else {
                    System.out.println("Which card do you want to use ?");
                    RumourCard chosenRumourCard = this.chooseCard(); //TODO Make a choice list for the user
                    this.revealRumorCard(chosenRumourCard);
                }
            }
        }
    }

    public List<CardState> getSelectableCards() {
        List<CardState> selectableCards = new ArrayList<>();
        for (CardState cardState : this.hand) {
            if (!cardState.isRevealed()) selectableCards.add(cardState);
        }
        return selectableCards;
    }

    RumourCard chooseCard() { //TODO Temporary, to choose a card
        List<CardState> selectableCards = this.getSelectableCards();
        //Printing selectable cards
        for (int i = 0; i < selectableCards.size(); i++) {
            System.out.println(i + "- " + selectableCards.get(i).rumourCard.getCardName());
        }
        Scanner scanner = new Scanner(System.in);
        return this.hand.get(scanner.nextInt()).rumourCard;
    }

    public List<IdentityCard> getSelectablePlayers() {
        List<IdentityCard> selectablePlayers = new ArrayList<>();
        for (IdentityCard identityCard : Game.getGame().round.activePlayers) {
            if (identityCard.player != this && !identityCard.isIdentityRevealed()) {
                selectablePlayers.add(identityCard);
            }
        }
        return selectablePlayers;
    }

    public Player choosePlayer() { //TODO Temporary to choose a player
        List<IdentityCard> selectablePlayers = this.getSelectablePlayers();
        //Printing selectable players
        for (int i = 0; i < selectablePlayers.size(); i++) {
            System.out.println(i + "- " + selectablePlayers.get(i).player.getName());
        }
        //Registering choice
        Scanner scanner = new Scanner(System.in);
        return selectablePlayers.get(scanner.nextInt()).player;
    }

    public void selectIdentity() {
        System.out.println(this.getName() + ", type 0 for villager and 1 for witch");
        Scanner scanner = new Scanner(System.in);
        this.identityCard.setWitch(scanner.nextInt() > 0);
    }

}
