package com.view.graphic;

import com.controller.RoundController;
import com.model.card.RumourCard;
import com.model.card.effect.Effect;
import com.model.game.CardState;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class Panel extends JPanel {
    //Constantes
    private static final int SIZE_FACTOR = 3;

    //Images
    private final Image background;
    private final Image cardFront;
    private final Image cardBack;

    private Graphics2D g2D;

    int mouseXPos, mouseYPos;

    enum Gradient {
        NAME,
        WITCH,
        HUNT
    }

    Panel() {
        this.background = getToolkit().getImage("data/Tabletop.jpg");
        this.cardFront = getToolkit().getImage("data/CardFrontEmpty.png");
        this.cardBack = getToolkit().getImage("data/CardBack.png");

        this.g2D = (Graphics2D) this.getGraphics();
    }

    /**
     * Get correct gradient to draw
     *
     * @param y        position of the text
     * @param gradient the gradient to pick
     * @return created gradient
     */
    private GradientPaint getGradient(int y, Gradient gradient) {
        FontMetrics fontMetrics = getFontMetrics(g2D.getFont());
        return switch (gradient) {
            case WITCH -> new GradientPaint(
                    0, y - fontMetrics.getHeight(),
                    new Color(255, 159, 64),
                    0, y + fontMetrics.getHeight(),
                    Color.BLACK
            );
            case HUNT -> new GradientPaint(
                    0, y - fontMetrics.getHeight(),
                    new Color(51, 153, 51),
                    0, y + fontMetrics.getHeight(),
                    Color.BLACK
            );
            default -> new GradientPaint(
                    0, y - 2 * fontMetrics.getHeight(),
                    Color.BLACK,
                    0, y + fontMetrics.getHeight(),
                    new Color(99, 125, 157)
            );
        };
    }

    //Draw a centered string on X axis
    private int drawXCenteredString(String string, int x, int y, int width) {
        //We need to forcefully split lines. The method drawString() doesn't apply \n
        if (string.contains("\n")) {
            String[] array = string.split("\n");
            for (int i = 0; i < array.length; i++) {
                drawXCenteredString(array[i], x, y + i * g2D.getFontMetrics(g2D.getFont()).getHeight(), width);
            }
            return array.length - 1;
        }
        g2D.drawString(string, x + (width - getFontMetrics(g2D.getFont()).stringWidth(string)) / 2, y);
        return 0;
    }

    private void drawEffects(int x, int y, int cardWidth, List<Effect> effects) {
        //Find good font
        Font font = g2D.getFont();

        //We compare each effect to make it so that the longest one fit in width
        int longestString = 0;
        for (Effect effect : effects) {
            String[] array = effect.toString().split("\n");
            for (int i = 0; i < array.length; i++) {
                int lengthOfCurrentString = g2D.getFontMetrics(font).stringWidth(array[i]);
                if (lengthOfCurrentString > longestString) {
                    longestString = lengthOfCurrentString;
                }
            }
        }
        //We set the font so that each effect fit
        g2D.setFont(font.deriveFont((float) (font.getSize() * cardWidth) / longestString));

        //Draw effects
        int yDelta = 0;
        int stringHeight = getFontMetrics(g2D.getFont()).getHeight();
        for (int i = 1; i <= effects.size(); i++) {
            int yPosition = y + (i + yDelta) * stringHeight;
            yDelta += drawXCenteredString(effects.get(i - 1).toString(), x, yPosition, cardWidth);
            //Draw line to separate effects
            if (i < effects.size()) {
                yPosition = y + (i + yDelta) * stringHeight;
                g2D.drawRect(x + cardWidth / 12, yPosition + stringHeight / 3, cardWidth - 2 * cardWidth / 12, 0);
            }
        }
    }

    //Draw a complete card
    private void drawCard(RumourCard rumourCard, int x, int y) {
        int cardHeight = getHeight() / SIZE_FACTOR, cardWidth = (int) (cardHeight / 1.35);

        //The card itself
        g2D.drawImage(cardFront, x, y, cardWidth, cardHeight, this);

        //Card name
        g2D.setFont(g2D.getFont().deriveFont((float) (getWidth() / 100)).deriveFont(Font.BOLD));
        g2D.setPaint(getGradient(y + cardHeight / 5, Gradient.NAME));
        drawXCenteredString(rumourCard.getCardName().toString(), x, y + cardHeight / 5, cardWidth);

        //Witch effects
        int effectRectangleY = y + cardHeight / 3;
        g2D.setColor(Color.decode("#ffebcc"));
        g2D.fillRect(x, effectRectangleY, cardWidth, cardHeight / 4);

        g2D.setFont(g2D.getFont().deriveFont((float) (getWidth() / 110)).deriveFont(Font.BOLD));
        g2D.setPaint(getGradient(effectRectangleY, Gradient.WITCH));
        drawXCenteredString("Witch?", x, effectRectangleY, cardWidth);

        g2D.setColor(Color.BLACK);
        g2D.setFont(g2D.getFont().deriveFont((float) (getWidth() / 200)));
        drawEffects(x, effectRectangleY, cardWidth, rumourCard.witchEffects);

        //Hunt effects
        effectRectangleY = (int) (y + (cardHeight / 1.5));
        g2D.setColor(Color.decode("#d6ebd6"));
        g2D.fillRect(x, effectRectangleY, cardWidth, cardHeight / 4);

        g2D.setFont(g2D.getFont().deriveFont((float) (getWidth() / 110)).deriveFont(Font.BOLD));
        g2D.setPaint(getGradient(effectRectangleY, Gradient.HUNT));
        drawXCenteredString("Hunt!", x, effectRectangleY, cardWidth);

        g2D.setColor(Color.BLACK);
        g2D.setFont(g2D.getFont().deriveFont((float) (getWidth() / 200)));
        drawEffects(x, effectRectangleY, cardWidth, rumourCard.huntEffects);
    }

    private void drawHand(List<CardState> hand) {
        for (int i = 0; i < hand.size(); i++) {
            drawCard(hand.get(i).rumourCard, (getWidth() / 2) + (i - hand.size() / 2) * (int) (getWidth() / (SIZE_FACTOR * 2.5)), getHeight() - (10 + getHeight() / SIZE_FACTOR));
        }
    }

    /**
     * Draw contained objects
     *
     * @param graphics basis needed for basic rendering operations
     */
    public void paintComponent(Graphics graphics) { //TODO Place action buttons somewhere
        super.paintComponents(graphics);

        g2D = (Graphics2D) graphics;
        RoundController roundController = RoundController.getRoundController();

        //Draw background
        graphics.drawImage(background, 0, 0, getWidth(), getHeight(), this);

        //Draw content
        if (roundController != null && roundController.identityCards.size() > 0) {
            //Draw hand of the current player at the bottom
            drawHand(RoundController.getCurrentPlayer().hand);

            //Draw the hand of each player hidden
            roundController.identityCards
                    .stream()
                    .map(identityCard -> identityCard.player)
                    .collect(Collectors.toList())
                    .forEach(player -> {
                        //TODO Draw each hidden hand
                    });

            //Draw the discard pile
        }
    }
}
