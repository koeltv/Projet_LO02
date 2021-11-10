package com.view.graphic;

import com.controller.RoundController;
import com.model.card.RumourCard;
import com.model.card.effect.Effect;
import com.model.game.CardState;
import com.model.game.IdentityCard;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

/**
 * The type Panel.
 * Graphical container where all 2D Graphics are drawn.
 */
public class Panel extends JPanel {
    //Constantes
    private static final int SIZE_FACTOR = 5; //Inversement proportionnel, + grand ==> - grandes cartes

    //Images
    private final Image background;
    private final Image cardFront;
    private final Image cardBack;
    private final Image identityCard;

    //Panel 2D converted graphics
    private Graphics2D g2D;

    //Card dimensions, based on window size
    private int cardWidth, cardHeight;

    /**
     * The Mouse x pos.
     */
    public int mouseXPos;
    /**
     * The Mouse y pos.
     */
    public int mouseYPos;

    /**
     * The queue containing all actions to display
     */
    private final Queue<PrintableAction> actions;

    private static class PrintableAction {
        String action;
        int displayTime;

        PrintableAction(String action) {
            this.action = action;
            this.displayTime = action.length() / 10 + 5;
        }
    }

    /**
     * The enum Gradient.
     * Used for title texts
     */
    enum Gradient {
        NAME,
        WITCH,
        HUNT
    }

    /**
     * Instantiates a new Panel.
     */
    Panel() {
        this.background = getToolkit().getImage("data/Tabletop.jpg");
        this.cardFront = getToolkit().getImage("data/CardFrontEmpty.png");
        this.cardBack = getToolkit().getImage("data/CatBack.jpg");
        this.identityCard = getToolkit().getImage("data/IdentityCard.png");

        this.actions = new LinkedList<>();
    }

    /**
     * Display action.
     * Set the action to be displayed at the center of the screen. The time it will stay there depend on the length of the String l.
     * With rf being the refresh rate, the time on screen t is t = (l/2 + 4) * rf.
     *
     * @param action the action
     */
    public void displayAction(String action) {
        actions.add(new PrintableAction(action));
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
        Color firstColor = Color.BLACK, secondColor = new Color(99, 125, 157);
        switch (gradient) {
            case HUNT -> {
                firstColor = new Color(255, 159, 64);
                secondColor = Color.BLACK;
            }
            case WITCH -> {
                firstColor = new Color(51, 153, 51);
                secondColor = Color.BLACK;
            }
        }
        return new GradientPaint(0, y - 2 * fontMetrics.getHeight(), firstColor, 0, y + fontMetrics.getHeight(), secondColor);
    }

    //Draw a centered string on X axis within [x; x + width]
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

    //Draw a list of effects within a card X-axis bounds at y position
    private void drawEffects(int x, int y, List<Effect> effects) {
        //Find good font
        Font font = g2D.getFont();

        //We compare each effect to make it so that the longest one fit in width
        int longestString = 0;
        for (Effect effect : effects) {
            String[] array = effect.toString().split("\n");
            for (int i = 0; i < array.length; i++) {
                int lengthOfCurrentString = g2D.getFontMetrics(font).stringWidth(array[i]);
                if (lengthOfCurrentString > longestString) longestString = lengthOfCurrentString;
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
    private void drawCard(int x, int y, RumourCard rumourCard) {
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
        drawEffects(x, effectRectangleY, rumourCard.witchEffects);

        //Hunt effects
        effectRectangleY = (int) (y + (cardHeight / 1.5));
        g2D.setColor(Color.decode("#d6ebd6"));
        g2D.fillRect(x, effectRectangleY, cardWidth, cardHeight / 4);

        g2D.setFont(g2D.getFont().deriveFont((float) (getWidth() / 110)).deriveFont(Font.BOLD));
        g2D.setPaint(getGradient(effectRectangleY, Gradient.HUNT));
        drawXCenteredString("Hunt!", x, effectRectangleY, cardWidth);

        g2D.setColor(Color.BLACK);
        g2D.setFont(g2D.getFont().deriveFont((float) (getWidth() / 200)));
        drawEffects(x, effectRectangleY, rumourCard.huntEffects);
    }

    //Draw a turned card
    private void drawCard(int x, int y) {
        g2D.drawImage(cardBack, x, y, cardWidth, cardHeight, this);
    }

    //Draw an identity card
    private void drawIdentityCard(int x, int y) {
        g2D.drawImage(identityCard, x, y, cardWidth, cardHeight, this);
    }

    //Draw a complete hand
    private void drawCardList(List<?> hand, int size) {
        boolean even = size % 2 == 0;

        for (int i = 0; i < size; i++) {
            //Center position, subtract half a card width for odd number of card
            int centerFactor = getWidth() / 2;
            if (!even) centerFactor -= cardWidth / 2;

            //Position of the card in the hand
            int relativePosition = i - size / 2;

            //margin to the center as multiple of 10px
            int margin = 10;
            if (even) {
                margin *= relativePosition == 0 ? 1 : (int) Math.signum(relativePosition);
            } else {
                margin *= Math.abs(relativePosition) * (int) Math.signum(relativePosition);
            }

            //used for even numbers
            int nbOfMargin = 1;
            if (even) {
                int value = relativePosition;
                if (i > 0) value = relativePosition + 1 == 0 ? 1 : relativePosition + 1;
                nbOfMargin = Math.abs(value);
            }

            //Settle 2x margin at center for even number of cards
            if (even && (relativePosition == -1 || relativePosition == 0)) margin /= 2;

            //Check the list, we only display card from rumour card list or revealed ones from card state list
            RumourCard rumourCard = null;
            if (hand != null) {
                if (hand.get(0) instanceof RumourCard) {
                    rumourCard = (RumourCard) hand.get(i);
                } else if (hand.get(0) instanceof CardState && ((CardState) hand.get(i)).isRevealed()) {
                    rumourCard = ((CardState) hand.get(i)).rumourCard;
                }
            }

            //Show the card
            if (rumourCard != null) {
                drawCard(
                        centerFactor + relativePosition * cardWidth + margin * nbOfMargin,
                        getHeight() - (10 + getHeight() / SIZE_FACTOR),
                        rumourCard
                );
            } else {
                drawCard(
                        centerFactor + relativePosition * cardWidth + margin * nbOfMargin,
                        getHeight() - (10 + getHeight() / SIZE_FACTOR)
                );
            }
        }
    }

    //Draw player
    private void drawPlayer(IdentityCard card) {
        if (card.player == RoundController.getCurrentPlayer()) {
            List<RumourCard> hand = card.player.hand.stream().map(cardState -> cardState.rumourCard).collect(Collectors.toList());
            drawCardList(hand, hand.size());
        } else {
            drawCardList(card.player.hand, card.player.hand.size());
        }

        int XCenter = getWidth() / 2 - cardWidth / 2;
        int YPos = getHeight() - (10 * 2 + getHeight() / SIZE_FACTOR) - cardHeight;
        drawIdentityCard(XCenter, YPos);
    }

    //Display the current action if there is one
    private void drawAction() {
        if (actions.size() > 0) {
            PrintableAction currentAction = actions.peek();
            if (currentAction != null) {
                int x = getWidth() / 2;
                int y = getHeight() / 2;
                int padding = getWidth() / 20;

                //We adapt the size to the current screen size
                Font font = getFont().deriveFont((float) padding);
                g2D.setFont(font);
                int width = g2D.getFontMetrics().stringWidth(currentAction.action);
                int height = g2D.getFontMetrics().getAscent();

                //We do a background on which we put the text
                Graphics2D tempGraph = (Graphics2D) g2D.create();
                tempGraph.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.75f));
                tempGraph.setColor(new Color(68, 88, 129));
                tempGraph.fillRect(x - width / 2 - padding, y - height / 2, width + 2 * padding, height);
                tempGraph.dispose();

                //We add the text
                g2D.setColor(Color.WHITE);
                drawXCenteredString(currentAction.action, x - width / 2, y - height / 2 + (int) (height * 0.875), width);

                //We reduce the lifetime of the action message
                currentAction.displayTime--;
                if (currentAction.displayTime <= 0) actions.remove();
            }
        }
    }

    /**
     * Draw contained objects
     *
     * @param graphics basis needed for basic rendering operations
     */
    @Override
    public void paintComponent(Graphics graphics) { //TODO Place action buttons somewhere
        super.paintComponents(graphics);

        //Actualise object values
        g2D = (Graphics2D) graphics;
        RoundController roundController = RoundController.getRoundController();

        cardHeight = getHeight() / SIZE_FACTOR;
        cardWidth = (int) (cardHeight / 1.35);

        //Draw background
        graphics.drawImage(background, 0, 0, getWidth(), getHeight(), this);

        //Draw content
        if (roundController != null && roundController.identityCards.size() > 0) {
            //Draw hand of the current player at the bottom
            drawPlayer(roundController.getPlayerIdentityCard(RoundController.getCurrentPlayer()));

            //Draw the hand of each other player hidden
            AffineTransform oldRotation = g2D.getTransform();
            double currentAngle = 0;

            for (IdentityCard card : roundController.identityCards) {
                if (card.player != RoundController.getCurrentPlayer()) {
                    double angle = (double) 360 / roundController.identityCards.size();
                    currentAngle += angle;

                    //Rotation to make all players on a circle
                    g2D.rotate(Math.toRadians(angle), (float) getWidth() / 2, (float) getHeight() / 2);

                    //The players on the side are set back a little to have some more room
                    AffineTransform temp = g2D.getTransform();
                    if (currentAngle % 180 != 0) {
                        int XTranslation = currentAngle % 90 == 0 ? 0 : getWidth() / 40;
                        //Reverse in lower-left and upper-right cadrans
                        if ((currentAngle > 0 && currentAngle < 90) || (currentAngle > 180 && currentAngle < 270)) {
                            XTranslation = -XTranslation;
                        }
                        g2D.translate(XTranslation, getHeight() / 3);
                    }
                    //We draw the player hand
                    drawPlayer(card);

                    //We reset change to the transform matrix
                    g2D.setTransform(temp);
                }
            }
            g2D.setTransform(oldRotation);

            //Draw the discard pile
            List<RumourCard> discardPile = RoundController.getRoundController().discardPile;
            if (discardPile.size() > 0) {
                g2D.translate(0, (-getHeight() / 2) + cardHeight / 2);
                drawCardList(discardPile, discardPile.size());
                g2D.setTransform(oldRotation);
            }

            drawAction();
        }
    }
}
