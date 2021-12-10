package com.view.graphic.dynamic;

import com.model.card.RumourCard;
import com.model.card.effect.Effect;
import com.model.game.CardState;
import com.model.game.IdentityCard;
import com.model.game.Round;
import com.model.player.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.jar.JarFile;

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
     * The player currently playing that will be displayed at the bottom.
     */
    private Player mainPlayer;

    /**
     * Instantiates a new Panel.
     */
    Panel() {
        Image background, cardFront, cardBack, identityCardNotRevealed, identityCardRevealed;
        try {
            File executable = new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
            JarFile jar = new JarFile(executable);

            background = loadImage("Tabletop.jpg", jar);
            cardFront = loadImage("CardFrontEmpty.png", jar);
            cardBack = loadImage("CardBack.jpg", jar);
            identityCardNotRevealed = loadImage("IdentityCard.PNG", jar);
            identityCardRevealed = loadImage("RevealedVillager.png", jar);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            background = null;
            cardFront = null;
            cardBack = null;
            identityCardNotRevealed = null;
            identityCardRevealed = null;
        }

        this.background = background;
        this.cardFront = cardFront;
        this.cardBack = cardBack;
        this.identityCardNotRevealed = identityCardNotRevealed;
        this.identityCardRevealed = identityCardRevealed;
        this.mainPlayer = Round.getCurrentPlayer();

        ZoomPanel.template = cardFront;
        ZoomPanel.hiddenCard = cardBack;
    }

    /**
     * Load buffered image.
     *
     * @param fileName the file name
     * @param jar      the .jar file to load from
     * @return the buffered image
     * @throws IOException exception thrown if the file wasn't found in the .jar file
     */
    private BufferedImage loadImage(String fileName, JarFile jar) throws IOException {
        InputStream fileInputStreamReader = jar.getInputStream(jar.getJarEntry(fileName));
        return ImageIO.read(fileInputStreamReader);
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
        int lengthOfLongestString = g2D.getFontMetrics().stringWidth(
                effects.stream()
                        .flatMap(effect -> Arrays.stream(effect.toString().split("\n")))
                        .max(Comparator.comparing(String::length))
                        .orElseThrow()
        );

        //We set the font so that each effect fit
        int margin = getWidth() / 200;
        Font font = g2D.getFont();
        g2D.setFont(font.deriveFont((float) (font.getSize() * (cardWidth - 2 * margin)) / lengthOfLongestString));

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
        drawEffectsContainer(x, y + cardHeight / 3, rumourCard.getWitchEffects(), true);

        //Hunt effects
        drawEffectsContainer(x, y + (int) (cardHeight / 1.5), rumourCard.getHuntEffects(), false);
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
     * Draw a card list with their states.
     * This method will also display the state of the cards.
     * For exemple the card are visible if they are revealed and also have a green border if they are in the main player hand.
     *
     * @param cardList     the card list
     * @param size         the size of the card list
     * @param isMainPlayer whether this is for the main player or not
     */
    private void drawCardList(List<CardState> cardList, int size, boolean isMainPlayer) {
        int margin = 10, xf = (size - 1) * (cardWidth + margin), offset = (getWidth() - (xf + cardWidth)) / 2;
        int y = getHeight() - (margin + getHeight() / SIZE_FACTOR);

        for (int i = 0; i < size; i++) {
            int xi = offset + i * (cardWidth + margin);

            //Check the list, only return revealed cards from card state list, or display all for main player
            RumourCard rumourCard = null;
            if (cardList != null && cardList.size() > 0) {
                if ((cardList.get(i).isRevealed() || isMainPlayer)) {
                    rumourCard = cardList.get(i).rumourCard;
                }
            }

            //Show the card
            if (rumourCard != null) {
                //If it is a revealed card of the main player, add a green border
                if (isMainPlayer) {
                    CardState cardState = cardList.get(i);
                    if (cardState.isRevealed() || cardState.rumourCard.isUsable(mainPlayer)) {
                        g2D.setColor(cardState.isRevealed() ? new Color(51, 153, 51) : new Color(200, 50, 30));
                        Stroke stroke = g2D.getStroke();
                        g2D.setStroke(new BasicStroke(margin));
                        g2D.drawRect(xi, y, cardWidth, cardHeight);
                        g2D.setStroke(stroke);
                    }
                    //Add the possibility to zoom on cards
                    add(new Card2DDisplay(xi, y, cardWidth, cardHeight, rumourCard));
                }
                drawCard(xi, y, rumourCard);
            } else {
                drawCard(xi, y);
            }
        }
    }

    /**
     * Draw a card list.
     *
     * @param cardList the card list
     * @param size     the size of the card list
     */
    private void drawCardList(List<RumourCard> cardList, int size) {
        int margin = 10, xf = (size - 1) * (cardWidth + margin), offset = (getWidth() - (xf + cardWidth)) / 2;
        int y = getHeight() - (margin + getHeight() / SIZE_FACTOR);

        for (int i = 0; i < size; i++) {
            int xi = offset + i * (cardWidth + margin);

            //Check the list, only return card from rumour card list or revealed ones from card state list
            RumourCard rumourCard = null;
            if (cardList != null && cardList.size() > 0) rumourCard = cardList.get(i);

            //Show the card
            if (rumourCard != null) drawCard(xi, y, rumourCard);
            else drawCard(xi, y);
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
        Round round = Round.getInstance();

        for (Component component : this.getComponents()) {
            if (component instanceof Card2DDisplay card) this.remove(card);
        }

        cardHeight = getHeight() / SIZE_FACTOR;
        cardWidth = (int) (cardHeight / 1.35);

        //Draw background
        graphics.drawImage(background, 0, 0, getWidth(), getHeight(), this);

        //Draw content
        if (round != null && round.getIdentityCards().size() > 0) {
            List<IdentityCard> identityCards = round.getIdentityCards();
            AffineTransform oldRotation = g2D.getTransform();

            //Draw the discard pile
            List<RumourCard> discardPile = round.getDiscardPile();
            if (discardPile.size() > 0) {
                g2D.translate(0, (-getHeight() / 2) + cardHeight / 3);
                drawCardList(discardPile, discardPile.size());
                g2D.setTransform(oldRotation);
            }

            //Draw hand of the current player at the bottom
            drawPlayer(round.getPlayerIdentityCard(mainPlayer));

            //Draw the hand of each other player hidden
            double currentAngle = 0;
            double angle = (double) 360 / identityCards.size();

            for (IdentityCard card : identityCards) {
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
                            identityCards.size() < 6 && (
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
