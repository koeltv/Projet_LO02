package com.controller;

import com.model.card.RumourCard;
import com.model.game.CardState;
import com.model.game.Round;
import com.model.player.Player;
import com.view.ActiveView;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Player controller.
 */
public class PlayerController {
    /**
     * The Player.
     */
    protected Player player;

    /**
     * The View.
     */
    protected final ActiveView view;

    ///////////////////////////////////////////////////////////////////////////
    // Constructor, getter and setters
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Instantiates a new Player controller.
     *
     * @param playerName the player name
     * @param view       the view
     */
    PlayerController(String playerName, ActiveView view) {
        this.player = new Player(playerName);
        this.view = view;
    }

    /**
     * Instantiates a new Player controller.
     *
     * @param view the view
     */
    PlayerController(ActiveView view) {
        this.view = view;
    }

    /**
     * Gets score.
     *
     * @return the score
     */
    public int getScore() {
        return player.getScore();
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return player.getName();
    }

    /**
     * Gets hand.
     *
     * @return the hand
     */
    public List<CardState> getHand() {
        return player.getHand();
    }

    /**
     * Add card to hand.
     *
     * @param rumourCard the rumour card
     */
    public void addCardToHand(RumourCard rumourCard) {
        player.addCardToHand(rumourCard);
    }

    /**
     * Remove card from hand rumour card.
     *
     * @param rumourCard the rumour card
     * @return the rumour card
     */
    public RumourCard removeCardFromHand(RumourCard rumourCard) {
        return player.removeCardFromHand(rumourCard);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Controller methods
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Gets player.
     *
     * @return the player
     */
    Player getPlayer() {
        return player;
    }

    /**
     * Choose identity.
     */
    public void chooseIdentity() {
        int identity = view.promptForPlayerIdentity(player.getName());
        Round.getInstance().getPlayerIdentityCard(player).setWitch(identity > 0);
    }

    /**
     * Ask the player to choose a card.
     *
     * @param rumourCardList the list of card to select from
     * @return chosen card
     */
    public RumourCard chooseCard(List<RumourCard> rumourCardList) {
        return rumourCardList.get(view.promptForCardChoice(rumourCardList));
    }

    /**
     * Choose card blindly rumour card.
     *
     * @param rumourCardList the rumour card list
     * @return the rumour card
     */
    public RumourCard chooseCardBlindly(List<RumourCard> rumourCardList) {
        int index = view.promptForCardChoice(rumourCardList.size());
        return rumourCardList.get(index);
    }

    /**
     * Ask a player to choose another player.
     *
     * @param playerList the player list to choose from
     * @return chosen player
     */
    public Player choosePlayer(List<Player> playerList) {
        return playerList.get(view.promptForPlayerChoice(playerList.stream().map(Player::getName).collect(Collectors.toList())));
    }

    /**
     * Ask the current player for his next action.
     * This method will call the play method of the current player.
     *
     * @param possibleActions the possible actions
     */
    public void askPlayerForAction(List<PlayerAction> possibleActions) {
        //Ask the player to choose his next action
        applyPlayerAction(view.promptForAction(player.getName(), possibleActions));
    }

    /**
     * Apply player action.
     *
     * @param action the action
     */
    public void applyPlayerAction(PlayerAction action) {
        RoundController round = RoundController.getInstance();

        switch (action) {
            case DISCARD -> {
                RumourCard chosenCard = chooseCard(player.getSelectableCardsFromHand());
                RoundController.getInstance().getDiscardPile().add(player.removeCardFromHand(chosenCard));
            }
            case LOOK_AT_IDENTITY -> view.showPlayerIdentity(player.getName(), round.getPlayerIdentityCard(player).isWitch());
            case REVEAL_IDENTITY -> {
                view.showPlayerAction(player.getName());
                view.showPlayerIdentity(player.getName(), round.getPlayerIdentityCard(player).isWitch());

                RoundController.getInstance().revealIdentity(player);
            }
            case ACCUSE -> {
                List<Player> players = round.getNotRevealedPlayers(player);
                //In case an effect forbid a player from accusing a certain other player
                List<Player> notSelectablePlayers = round.getNotSelectablePlayers(player);
                if (players.size() > 1 && notSelectablePlayers != null) {
                    players.removeIf(notSelectablePlayers::contains);
                    notSelectablePlayers.clear();
                }

                Player targetedPlayer = choosePlayer(players);
                view.showPlayerAction(player.getName(), targetedPlayer.getName());

                RoundController.getInstance().passToNextPlayer(player, targetedPlayer);

                PlayerController targetedPlayerController = RoundController.getInstance().getPlayerController(targetedPlayer);
                targetedPlayerController.askPlayerForAction(RoundController.getInstance().getStandardActions(targetedPlayer));

                //If the player is a witch, its identity card is deleted, so if null the player was revealed as a witch
                if (round.getPlayerIdentityCard(targetedPlayer) == null) {
                    player.addToScore(1);
                }
            }
            case USE_CARD -> {
                boolean cardUsedSuccessfully;
                do {
                    RumourCard chosenRumourCard = chooseCard(round.getUsableCards(player, player.getSelectableCardsFromHand()));
                    view.showPlayerAction(player.getName(), chosenRumourCard.getCardName());
                    cardUsedSuccessfully = player.revealRumourCard(chosenRumourCard);
                } while (!cardUsedSuccessfully);
            }
        }
        if (action != PlayerAction.ACCUSE)
            RoundController.getInstance().passToNextPlayer(player, round.getNextPlayer());
    }
}
