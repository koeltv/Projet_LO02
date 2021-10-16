package player;

import card.RumourCard;
import game.CardState;
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

    public void setScore(int value) {
        this.score = value;
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
    }

    public void revealRumorCard(RumourCard cardToReveal) {
    }

    public void revealIdentity() {
    }

    public void play() {
        if (this == Round.getCurrentPlayer()) {
            //hunt effect or accuse
            if (this.hand.size() < 1) {
                System.out.println("You don't have any cards left, who do you want to accuse ?");
                Player accusedPlayer = null; //TODO Make a choice list for the user
                this.accuse(accusedPlayer);
            } else {
                System.out.println("Do you want to accuse someone or use a Rumour Card ? (0 or 1)");
                Scanner scanner = new Scanner(System.in);
                int action = scanner.nextInt();
                if (action == 0) {
                    System.out.println("Who do you want to accuse ?");
                    Player accusedPlayer = null; //TODO Make a choice list for the user
                    this.accuse(accusedPlayer);
                } else {
                    System.out.println("Which card do you want to use ?");
                    RumourCard chosenRumourCard = null; //TODO Make a choice list for the user
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
                    RumourCard chosenRumourCard = null; //TODO Make a choice list for the user
                    this.revealRumorCard(chosenRumourCard);
                }
            }
        }
    }

}
