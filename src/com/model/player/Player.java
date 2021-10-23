package com.model.player;

import com.controller.GameController;
import com.model.card.RumourCard;
import com.model.game.CardState;
import com.model.game.IdentityCard;
import com.controller.RoundController;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * The type Player.
 */
public class Player {
    private int score;

    private final String name;

    /**
     * The Identity card.
     * Identity card of the player attributed at the start of each round.
     */
    public IdentityCard identityCard; //TODO Check if necessary

    /**
     * The Hand.
     * Hand of the player including revealed cards
     */
    public final List<CardState> hand = new ArrayList<>();

    /**
     * Instantiates a new Player.
     *
     * @param name the player name
     */
    public Player(final String name) {
        this.name = name;
    }

    /**
     * Gets score.
     *
     * @return the score
     */
    public int getScore() {
        return this.score;
    }

    /**
     * Add to score.
     *
     * @param value the value
     */
    public void addToScore(int value) {
        this.score += value;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Add card to hand.
     *
     * @param rumourCard the rumour card
     */
    public void addCardToHand(RumourCard rumourCard) {
        this.hand.add(new CardState(rumourCard));
    }

    /**
     * Remove card from hand.
     *
     * @param rumourCard the rumour card to remove
     */
    public void removeCardFromHand(RumourCard rumourCard) {
        this.hand.removeIf(cardState -> cardState.rumourCard == rumourCard);
    }

    /**
     * Accuse the chosen player
     *
     * @param accusedPlayer player to accuse
     */
    public void accuse(Player accusedPlayer) {
        accusedPlayer.play();
        if (accusedPlayer.identityCard.isWitch()) {
            this.addToScore(1);
        }
    }

    /**
     * Reveal a Rumour card
     * This method reveal the chosen card from the player hand and call its effects
     *
     * @param cardToReveal card to reveal
     * @return whether the card has been used successfully or not
     */
    public boolean revealRumourCard(RumourCard cardToReveal) {
        for (CardState cardState : this.hand) {
            if (cardToReveal == cardState.rumourCard) {
                cardState.setRevealed(true);
                break;
            }
        }
        return cardToReveal.useCard(this);
    }

    /**
     * Reveal the player identity
     */
    public void revealIdentity() {
        this.identityCard.setIdentityRevealed(true);
        System.out.print("Player " + this.getName() + " is a ");
        if (this.identityCard.isWitch()) {
            System.out.println("witch !");
            //If a player is revealed as a witch, we exclude him from the round
            GameController.getGame().roundController.identityCards.removeIf(identityCard -> identityCard.player == this);
            GameController.getGame().roundController.setNextPlayer(RoundController.getCurrentPlayer());
        } else {
            System.out.println("villager !");
            GameController.getGame().roundController.setNextPlayer(this);
        }
        RoundController.getRound().setNumberOfNotRevealedPlayers(RoundController.getRound().getNumberOfNotRevealedPlayers() - 1);
    }

    /**
     * Prompt player for action
     * This method will prompt the player to choose an action; accuse or reveal a card if he is the current player, reveal his identity or reveal a card otherwise
     * If there was a problem with the resolution of the action, we loop until we can apply correctly an action
     */
    public void play() {
        boolean actionNotTaken = false;
        do {
            if (this == RoundController.getCurrentPlayer()) {
                System.out.print(this.getName() + ", ");
                //hunt effect or accuse
                if (this.hand.size() < 1) {
                    System.out.println("You don't have any cards left, who do you want to accuse ?");
                    Player accusedPlayer = this.choosePlayer();
                    this.accuse(accusedPlayer);
                } else {
                    System.out.println("Do you want to accuse someone or use a Rumour Card ? (0 or 1)");
                    Scanner scanner = new Scanner(System.in);
                    int action = scanner.nextInt();
                    if (action == 0) {
                        System.out.println("Who do you want to accuse ?");
                        Player accusedPlayer = this.choosePlayer();
                        this.accuse(accusedPlayer);
                    } else {
                        System.out.println("Which card do you want to use ?");
                        RumourCard chosenRumourCard = this.chooseCard();
                        actionNotTaken = this.revealRumourCard(chosenRumourCard);
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
                        RumourCard chosenRumourCard = this.chooseCard();
                        actionNotTaken = this.revealRumourCard(chosenRumourCard);
                    }
                }
            }
        } while (actionNotTaken);
    }

    /**
     * Get the list of not revealed card from the player's hand
     *
     * @return selectable cards
     */
    public List<CardState> getSelectableCards() {
        List<CardState> selectableCards = new ArrayList<>();
        for (CardState cardState : this.hand) {
            if (!cardState.isRevealed()) selectableCards.add(cardState);
        }
        return selectableCards;
    }

    /**
     * Prompt the player to choose a card
     *
     * @return chosen card
     */
    RumourCard chooseCard() { //TODO Temporary, to choose a card
        List<CardState> selectableCards = this.getSelectableCards();
        //Printing selectable cards
        for (int i = 0; i < selectableCards.size(); i++) {
            System.out.println(i + "- " + selectableCards.get(i).rumourCard.getCardName());
        }
        Scanner scanner = new Scanner(System.in);
        return this.hand.get(scanner.nextInt()).rumourCard;
    }

    /**
     * Get the list of not revealed player in the current round
     *
     * @return selectable players
     */
    public List<IdentityCard> getSelectablePlayers() {
        List<IdentityCard> selectablePlayers = new ArrayList<>();
        for (IdentityCard identityCard : GameController.getGame().roundController.identityCards) {
            if (identityCard.player != this && !identityCard.isIdentityRevealed()) {
                selectablePlayers.add(identityCard);
            }
        }
        return selectablePlayers;
    }

    /**
     * Prompt the player to choose another player
     *
     * @return chosen player
     */
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

    /**
     * Prompt the user to choose an identity
     */
    public void selectIdentity() {
        System.out.println(this.getName() + ", type 0 for villager and 1 for witch");
        Scanner scanner = new Scanner(System.in);
        this.identityCard.setWitch(scanner.nextInt() > 0);
    }

}
