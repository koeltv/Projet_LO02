package com.view.graphic;

import com.controller.RoundController;
import com.model.game.CardState;
import com.model.game.IdentityCard;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Panel extends JPanel {
    //Definition of the starting position of all elements
    private final Font font = new Font("Courier", Font.BOLD, 40);

    private final Image background = getToolkit().getImage("data/Tabletop.jpg");
    private final Image cardFront = getToolkit().getImage("data/CardFrontEmpty.png");
    private final Image cardBack = getToolkit().getImage("data/CardBack.png");

    private final RoundController roundController = RoundController.getRoundController();

    int mouseXPos, mouseYPos;

    /**
     * Draw contained objects
     *
     * @param graphics basis needed for basic rendering operations
     */
    public void paintComponent(Graphics graphics) {
        super.paintComponents(graphics);

        //Draw background
        graphics.drawImage(background, 0, 0, getWidth(), getHeight(), this);

        if (roundController != null && roundController.identityCards.size() > 0) {
            int nbOfPlayers = roundController.identityCards.size();
            for (IdentityCard identityCard : roundController.identityCards) {
                //If the player is the current player, display at the bottom
                if (identityCard.player == RoundController.getCurrentPlayer()) {
                    int nbOfCards = identityCard.player.hand.size();
                    List<CardState> hand = identityCard.player.hand;

                    for (int i = 0; i < hand.size(); i++) {
                        CardState cardState = hand.get(i);
                        graphics.drawImage(cardBack, getWidth() / 2 - (i) * getWidth() / 12, getWidth() - 100, getWidth() / 12, getHeight() / 5, this);
                        graphics.drawString(cardState.rumourCard.toString(), getWidth() / 2 - (i) * getWidth() / 12, getWidth() - 100 + 10);
                    }
                }
            }
        }

        //            super.paintComponent(graphics);
        //            graphics.setColor(Color.white);
        //            graphics.fillRect(0, 0, this.getWidth(), this.getHeight());
        //
        //            graphics.setColor(Color.black);
        //            graphics.fillRect(20, posYJ, 25, 180);
        //            graphics.fillRect(1440, posYA, 25, 180);
        //            graphics.setColor(Color.red);
        //            graphics.fillOval(xBall, yBall, 50, 50);
        //            graphics.setFont(font);
        //            graphics.setColor(Color.black);
        //
        //            graphics.drawString(scoreJ + " : " + scoreA, 680, 30);
        //        }
    }
}
