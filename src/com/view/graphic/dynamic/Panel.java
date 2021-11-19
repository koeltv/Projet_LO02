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
import java.util.List;

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
     * The Identity card when the player isn't revealed.
     */
    private final Image identityCardNotRevealed;
    /**
     * The Identity card when the player is revealed as Villager.
     */
    private final Image identityCardRevealed;

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
     * The next action to display.
     */
    private PrintableAction action;
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
        this.identityCardNotRevealed = getToolkit().getImage("data/IdentityCard.png");
        this.identityCardRevealed = getToolkit().getImage("data/RevealedVillager.png");

        this.mainPlayer = RoundController.getCurrentPlayer();
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
     * Set next action to display.
     *
     * @param action the action
     */
    void setAction(String action) {
        this.action = new PrintableAction(action);
    }

    /**
     * Gets waiting time.
     *
     * @return the waiting time
     */
    public int getWaitingTime() {
        return action == null ? 0 : action.displayTime;
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
            for (String s : array) {
                int lengthOfCurrentString = g2D.getFontMetrics(font).stringWidth(s);
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
    private void drawCard(int x, int y, RumourCard rumourCard) {
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
     * @param x            the x coordinate
     * @param y            the y coordinate
     * @param identityCard the identity card to display
     */
    private void drawIdentityCard(int x, int y, IdentityCard identityCard) {
        g2D.drawImage(!identityCard.isIdentityRevealed() ? identityCardNotRevealed : identityCardRevealed, x, y, cardWidth, cardHeight, this);
    }

    /**
     * Draw a card list.
     * This can take a list of either CardStates or RumourCards.
     *
     * @param cardList     the card list
     * @param size         the size of the card list
     * @param isMainPlayer whether this is for the main player or not
     */
    private void drawCardList(List<?> cardList, int size, boolean isMainPlayer) { // TODO: 19/11/2021 Correct card positioning
        boolean even = size % 2 == 0;

        for (int i = 0; i < size; i++) {
            int centerFactor = getWidth() / 2;

            //Position of the card in the cardList, centered around 0
            int relativePosition = i - size / 2;

            //margin to the center as multiple of 10px
            int margin = 10, nbOfMargin = 1;
            if (even) {
                margin *= relativePosition == 0 ? 1 : (int) Math.signum(relativePosition);

                int value = relativePosition;
                if (i > 0) value = relativePosition + 1 == 0 ? 1 : relativePosition + 1;
                nbOfMargin = Math.abs(value);

                //Settle 2x margin at center for even number of cards
                if (relativePosition == -1 || relativePosition == 0) margin /= 2;
            } else {
                margin *= Math.abs(relativePosition) * (int) Math.signum(relativePosition);

                //we subtract half a card width to the center position for odd number of card
                centerFactor -= cardWidth / 2;
            }

            //Check the list, only return card from rumour card list or revealed ones from card state list
            RumourCard rumourCard = null;
            if (cardList != null) {
                if (cardList.get(0) instanceof RumourCard) {
                    rumourCard = (RumourCard) cardList.get(i);
                } else if (cardList.get(0) instanceof CardState && (((CardState) cardList.get(i)).isRevealed() || isMainPlayer)) {
                    rumourCard = ((CardState) cardList.get(i)).rumourCard;
                }
            }

            //Show the card
            if (rumourCard != null) {
                //If it is a revealed card of the main player, add a green border
                if (isMainPlayer && ((CardState) cardList.get(i)).isRevealed()) {
                    Stroke stroke = g2D.getStroke();
                    g2D.setStroke(new BasicStroke(10));
                    g2D.setColor(new Color(51, 153, 51));
                    g2D.drawRect(
                            centerFactor + relativePosition * cardWidth + margin * nbOfMargin,
                            getHeight() - (10 + getHeight() / SIZE_FACTOR),
                            cardWidth, cardHeight
                    );
                    g2D.setStroke(stroke);
                }
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
    private void drawPlayer(IdentityCard identityCard) {
        if (identityCard != null) {
            drawCardList(identityCard.player.getHand(), identityCard.player.getHand().size(), identityCard.player == mainPlayer);

            int XCenter = getWidth() / 2 - cardWidth / 2;
            int YPos = getHeight() - (10 * 2 + getHeight() / SIZE_FACTOR) - cardHeight;
            drawIdentityCard(XCenter, YPos, identityCard);

            //Display player name and score
            g2D.setColor(Color.WHITE);
            g2D.setFont(g2D.getFont().deriveFont((float) (getWidth() / 100)).deriveFont(Font.BOLD));
            String score = "Score : " + identityCard.player.getScore();
            XCenter -= g2D.getFontMetrics().stringWidth(score) + cardWidth / 2;
            YPos += cardHeight - g2D.getFontMetrics().getHeight();
            g2D.drawString(identityCard.player.getName(), XCenter, YPos);
            g2D.drawString(score, XCenter, YPos + g2D.getFontMetrics().getHeight());
        }
    }

    /**
     * Display the current action if there is one.
     */
    private void drawAction() {
        if (action != null) {
            int padding = getWidth() / 20;

            //We adapt the size to the current screen size
            Font font = getFont().deriveFont((float) padding);
            g2D.setFont(font);
            int width = g2D.getFontMetrics().stringWidth(action.text);
            int height = g2D.getFontMetrics().getAscent();

            //We do a background on which we put the text
            Graphics2D tempGraph = (Graphics2D) g2D.create();
            tempGraph.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.75f));
            tempGraph.setColor(new Color(68, 88, 129));
            tempGraph.fillRect(getWidth() / 2 - width / 2 - padding, getHeight() / 2 - height / 2, width + 2 * padding, height);
            tempGraph.dispose();

            //We add the text
            g2D.setColor(Color.WHITE);
            drawXCenteredString(action.text, getWidth() / 2 - width / 2, getHeight() / 2 - height / 2 + (int) (height * 0.875), width);
        }
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponents(graphics);

        //Actualise object values
        g2D = (Graphics2D) graphics;
        RoundController roundController = RoundController.getInstance();

        cardHeight = getHeight() / SIZE_FACTOR;
        cardWidth = (int) (cardHeight / 1.35);

        //Draw background
        graphics.drawImage(background, 0, 0, getWidth(), getHeight(), this);

        //Draw content
        if (roundController != null && roundController.getIdentityCards().size() > 0) {
            AffineTransform oldRotation = g2D.getTransform();

            //Draw the discard pile
            List<RumourCard> discardPile = RoundController.getInstance().getDiscardPile();
            if (discardPile.size() > 0) {
                g2D.translate(0, (-getHeight() / 2) + cardHeight / 3);
                drawCardList(discardPile, discardPile.size(), false);
                g2D.setTransform(oldRotation);
            }

            //Draw hand of the current player at the bottom
            drawPlayer(roundController.getPlayerIdentityCard(mainPlayer));

            //Draw the hand of each other player hidden
            double currentAngle = 0;
            double angle = (double) 360 / roundController.getIdentityCards().size();

            for (IdentityCard card : roundController.getIdentityCards()) {
                if (card.player != mainPlayer) {
                    //Rotation to make all players on a circle
                    g2D.rotate(Math.toRadians(angle), (float) getWidth() / 2, (float) getHeight() / 2);

                    //The players near the diagonals are set back a little to have some more room
                    AffineTransform temp = g2D.getTransform();

                    currentAngle += angle;
                    double yTranslationFactor = Math.sin(Math.toRadians(currentAngle));
                    double xTranslationFactor = Math.cos(Math.toRadians(currentAngle));
                    double ellipseFactor = ((float) getHeight() / 3) * Math.abs(yTranslationFactor);
                    double y0Translation = 0;
                    //We only apply it to players near the diagonals
                    if (
                            roundController.getIdentityCards().size() < 6 && (
                                    (currentAngle > 15 && currentAngle < 65)
                                            || (currentAngle > 105 && currentAngle < 135)
                                            || (currentAngle > 225 && currentAngle < 255)
                                            || (currentAngle > 295 && currentAngle < 345)
                            )
                    ) {
                        //Translation gets bigger when we approach the top and bottom
                        y0Translation = -Math.cos(Math.toRadians(currentAngle)) * getHeight() / 3;
                    }
                    g2D.translate(y0Translation * yTranslationFactor, ellipseFactor + y0Translation * xTranslationFactor);

                    //We draw the player hand
                    drawPlayer(card);

                    //We reset change to the transform matrix, in case a translation was made
                    g2D.setTransform(temp);
                }
            }
            //We reset the transform matrix to its original value
            g2D.setTransform(oldRotation);
        }
        drawAction();
    }
}
