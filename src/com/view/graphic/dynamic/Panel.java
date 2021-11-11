package com.view.graphic.dynamic;

import com.controller.RoundController;
import com.model.card.RumourCard;
import com.model.card.effect.Effect;
import com.model.game.CardState;
import com.model.game.IdentityCard;
import com.model.player.Player;

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
    /**
     * The constant SIZE_FACTOR.
     * Proportional inversion : a bigger number means smaller cards.
     */
    private static final int SIZE_FACTOR = 5;

    /**
     * The Background.
     */
    private final Image background;
    /**
     * The Card front.
     */
    private final Image cardFront;
    /**
     * The Card back.
     */
    private final Image cardBack;
    /**
     * The Identity card.
     */
    private final Image identityCard;

    /**
     * The 2D graphics used in the whole class.
     */
    private Graphics2D g2D;

    /**
     * The Card width.
     */
    private int cardWidth;
    /**
     * The Card height.
     */
    private int cardHeight;
    /**
     * The queue containing all actions to display
     */
    private final Queue<PrintableAction> actions;
    /**
     * The Main player.
     */
    private Player mainPlayer;

    /**
     * Instantiates a new Panel.
     */
    Panel() {
        this.background = getToolkit().getImage("data/Tabletop.jpg");
        this.cardFront = getToolkit().getImage("data/CardFrontEmpty.png");
        this.cardBack = getToolkit().getImage("data/CatBack.jpg");
        this.identityCard = getToolkit().getImage("data/IdentityCard.png");
        this.mainPlayer = RoundController.getCurrentPlayer();

        this.actions = new LinkedList<>();
    }

    /**
     * Sets main player.
     *
     * @param mainPlayer the main player
     */
    void setMainPlayer(Player mainPlayer) {
        this.mainPlayer = mainPlayer;
    }

    /**
     * Gets next action.
     * Does all the processing necessary of the queue before returning the next action.
     * This includes making sure that the queue isn't empty, decreasing the action lifetime and trimming the queue if it becomes too long.
     *
     * @return the next action
     */
    private PrintableAction getNextAction() {
        if (actions.size() > 0) {
            if (actions.peek().displayTime < 0) actions.remove();
            PrintableAction action = actions.peek();
            if (action != null) {
                //We reduce the lifetime of the action message
                action.displayTime--;

                //If too many actions are in the queue, skip some
                while (actions.size() > 8) actions.remove();

                return action;
            }
        }
        return null;
    }

    /**
     * Display action.
     * Set the action to be displayed at the center of the screen. The time it will stay there depend on the length of the String l.
     * With rf being the refresh rate, the time on screen t is t = (l/2 + 4) * rf.
     *
     * @param action the action
     */
    void displayAction(String action) {
        actions.add(new PrintableAction(action));
    }

    /**
     * Reset all actions.
     */
    void resetActions() {
        actions.clear();
    }

    /**
     * Draw x centered string within [x; x + width].
     * We need to forcefully split lines since the drawString() method doesn't apply the new line feed.
     *
     * @param string the string
     * @param x      the x coordinate used as center
     * @param y      the y coordinate
     * @param width  the width in which to fit the string
     * @return the additional number of lines (0 if only 1 line)
     */
    private int drawXCenteredString(String string, int x, int y, int width) {
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

    /**
     * Draw a list of effects.
     *
     * @param x       the x coordinate
     * @param y       the y coordinate
     * @param effects the effects to draw
     */
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

    /**
     * Draw effects container.
     *
     * @param x       the x coordinate
     * @param y       the y coordinate
     * @param effects the effects to draw
     * @param witch   whether those effects are witch effects or hunt effects
     */
    private void drawEffectsContainer(int x, int y, List<Effect> effects, boolean witch) {
        g2D.setColor(Color.decode(witch ? "#ffebcc" : "#d6ebd6"));
        g2D.fillRect(x, y, cardWidth, cardHeight / 4);

        g2D.setFont(g2D.getFont().deriveFont((float) (getWidth() / 110)).deriveFont(Font.BOLD));
        g2D.setPaint(Gradient.getGradient(g2D, y, witch ? Gradient.WITCH : Gradient.HUNT));
        drawXCenteredString(witch ? "Witch?" : "Hunt!", x, y, cardWidth);

        g2D.setColor(Color.BLACK);
        g2D.setFont(g2D.getFont().deriveFont((float) (getWidth() / 200)));
        drawEffects(x, y, effects);
    }

    /**
     * Draw a card facing up.
     *
     * @param x          the x coordinate
     * @param y          the y coordinate
     * @param rumourCard the rumour card to draw
     */
    private void drawCard(int x, int y, RumourCard rumourCard) { // TODO: 11/11/2021 add border for revealed cards of main player
        //The card itself
        g2D.drawImage(cardFront, x, y, cardWidth, cardHeight, this);

        //Card name
        g2D.setFont(g2D.getFont().deriveFont((float) (getWidth() / 100)).deriveFont(Font.BOLD));
        g2D.setPaint(Gradient.getGradient(g2D, y + cardHeight / 5, Gradient.NAME));
        drawXCenteredString(rumourCard.getCardName().toString(), x, y + cardHeight / 5, cardWidth);

        //Witch effects
        drawEffectsContainer(x, y + cardHeight / 3, rumourCard.witchEffects, true);

        //Hunt effects
        drawEffectsContainer(x, y + (int) (cardHeight / 1.5), rumourCard.huntEffects, false);
    }

    /**
     * Draw a card facing down.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     */
    private void drawCard(int x, int y) {
        g2D.drawImage(cardBack, x, y, cardWidth, cardHeight, this);
    }

    /**
     * Draw an identity card.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     */
    private void drawIdentityCard(int x, int y) {
        g2D.drawImage(identityCard, x, y, cardWidth, cardHeight, this);
    }

    /**
     * Draw a card list.
     * This can take a list of either CardStates or RumourCards.
     *
     * @param cardList the card list
     * @param size     the size of the card list
     */
    private void drawCardList(List<?> cardList, int size) {
        boolean even = size % 2 == 0;

        for (int i = 0; i < size; i++) {
            //Center position, subtract half a card width for odd number of card
            int centerFactor = getWidth() / 2;
            if (!even) centerFactor -= cardWidth / 2;

            //Position of the card in the cardList
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
            if (cardList != null) {
                if (cardList.get(0) instanceof RumourCard) {
                    rumourCard = (RumourCard) cardList.get(i);
                } else if (cardList.get(0) instanceof CardState && ((CardState) cardList.get(i)).isRevealed()) {
                    rumourCard = ((CardState) cardList.get(i)).rumourCard;
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

    /**
     * Draw a player.
     *
     * @param identityCard the identityCard of the player
     */
    private void drawPlayer(IdentityCard identityCard) { // TODO: 11/11/2021 Add score and name
        if (identityCard != null) {
            if (identityCard.player == mainPlayer) {
                List<RumourCard> hand = identityCard.player.hand.stream().map(cardState -> cardState.rumourCard).collect(Collectors.toList());
                drawCardList(hand, hand.size());
            } else {
                drawCardList(identityCard.player.hand, identityCard.player.hand.size());
            }

            int XCenter = getWidth() / 2 - cardWidth / 2;
            int YPos = getHeight() - (10 * 2 + getHeight() / SIZE_FACTOR) - cardHeight;
            drawIdentityCard(XCenter, YPos);
        }
    }

    /**
     * Display the current action if there is one.
     */
    private void drawAction() {
        PrintableAction currentAction = getNextAction();
        if (currentAction != null) {
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
            tempGraph.fillRect(getWidth() / 2 - width / 2 - padding, getHeight() / 2 - height / 2, width + 2 * padding, height);
            tempGraph.dispose();

            //We add the text
            g2D.setColor(Color.WHITE);
            drawXCenteredString(currentAction.action, getWidth() / 2 - width / 2, getHeight() / 2 - height / 2 + (int) (height * 0.875), width);
        }
    }

    @Override
    public void paintComponent(Graphics graphics) { // TODO: 11/11/2021 Modify positioning to always fit
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
            if (mainPlayer == null) mainPlayer = RoundController.getCurrentPlayer();
            drawPlayer(roundController.getPlayerIdentityCard(mainPlayer));

            //Draw the hand of each other player hidden
            AffineTransform oldRotation = g2D.getTransform();
            double currentAngle = 0;

            for (IdentityCard card : roundController.identityCards) {
                if (card.player != mainPlayer) {
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

                    //We reset change to the transform matrix, in case a translation was made
                    g2D.setTransform(temp);
                }
            }
            //We reset the transform matrix to its original value
            g2D.setTransform(oldRotation);

            //Draw the discard pile
            List<RumourCard> discardPile = RoundController.getRoundController().discardPile;
            if (discardPile.size() > 0) {
                g2D.translate(0, (-getHeight() / 2) + cardHeight / 2);
                drawCardList(discardPile, discardPile.size());
                g2D.setTransform(oldRotation);
            }
        }

        drawAction();
    }
}

// TODO: 11/11/2021 Eventually, add the rules somewhere (just a png)