package com.view.graphic;

import com.controller.RoundController;
import com.model.card.RumourCard;
import com.model.game.CardState;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Panel extends JPanel {
    //Definition of the starting position of all elements
    private final Font font = new Font("Courier", Font.BOLD, 40);

    private final Image background = getToolkit().getImage("data/Tabletop.jpg");
    private final Image cardFront = getToolkit().getImage("data/CardFrontEmpty.png");
    private final Image cardBack = getToolkit().getImage("data/CardBack.png");

    private RoundController roundController = RoundController.getRoundController();

    int mouseXPos, mouseYPos;

    private void drawCard(Graphics graphics, RumourCard rumourCard, int x, int y) {
        int cardWidth = getWidth() / 12, cardHeight = getHeight() / 5;

        graphics.drawImage(cardFront, x, y, cardWidth, cardHeight, this);
        graphics.setColor(Color.BLACK);
        graphics.drawString(rumourCard.getCardName().toString(), x, y + cardHeight / 4);

        //Witch effects
        graphics.setColor(Color.BLACK);
        graphics.drawString("Witch Effects", x, y + cardHeight / 4);
        graphics.setColor(Color.ORANGE);
        graphics.fillRect(x, y + cardHeight / 3, cardWidth, cardHeight / 4);

        //Hunt effects
        graphics.setColor(Color.BLACK);
        graphics.drawString("Hunt Effects", x, y + cardHeight / 4);
        graphics.setColor(Color.GREEN);
        graphics.fillRect(x, (int) (y + (cardHeight / 1.5)), cardWidth, cardHeight / 4);
    }

    /**
     * Draw contained objects
     *
     * @param graphics basis needed for basic rendering operations
     */
    public void paintComponent(Graphics graphics) {
        super.paintComponents(graphics);

        if (roundController == null) roundController = RoundController.getRoundController();

        //Draw background
        graphics.drawImage(background, 0, 0, getWidth(), getHeight(), this);

        if (roundController != null && roundController.identityCards.size() > 0) {
            int nbOfPlayers = roundController.identityCards.size();
            //If the player is the current player, display at the bottom
            List<CardState> hand = RoundController.getCurrentPlayer().hand;

            int xCenter = getWidth() / 2;
            for (int i = 0; i < hand.size(); i++) {
                CardState cardState = hand.get(i);
                drawCard(graphics, cardState.rumourCard, getWidth() / 2 + (i - hand.size() / 2) * getWidth() / 10, getHeight() - 200);
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
