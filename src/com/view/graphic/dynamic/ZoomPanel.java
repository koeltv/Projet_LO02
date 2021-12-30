package com.view.graphic.dynamic;

import com.model.card.RumourCard;
import com.model.card.effect.Effect;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class ZoomPanel extends JPanel {
    
	/**
	 * The Template
	 */
	static Image template;
    
	/**
	 * The Hidden Card
	 */
	static Image hiddenCard;

	/**
	 * The Rumour Card
	 */
    private final RumourCard rumourCard;

    /**
     * Instantiates a new Zoom Panel.
     * 
     * @param rumourCard the rumour card
     * @see com.model.card.RumourCard
     */
    ZoomPanel(RumourCard rumourCard) {
        this.rumourCard = rumourCard;
    }

    //TODO : Description des méthodes suivantes
    /**
     * Draw X centered string.
     * 
     * @param g2D     
     * @param string
     * @param y
     * @param width
     * @return
     */
    private int drawXCenteredString(Graphics2D g2D, String string, int y, int width) {
        if (string.contains("\n")) {
            String[] array = string.split("\n");
            for (int i = 0; i < array.length; i++) {
                drawXCenteredString(g2D, array[i], y + i * g2D.getFontMetrics(g2D.getFont()).getHeight(), width);
            }
            return array.length - 1;
        }
        g2D.drawString(string, (width - getFontMetrics(g2D.getFont()).stringWidth(string)) / 2, y);
        return 0;
    }

    /**
     * Draw effects
     * 
     * @param g2D
     * @param y
     * @param effects
     */
    private void drawEffects(Graphics2D g2D, int y, java.util.List<Effect> effects) {
        int lengthOfLongestString = g2D.getFontMetrics().stringWidth(
                effects.stream()
                        .flatMap(effect -> Arrays.stream(effect.toString().split("\n")))
                        .max(Comparator.comparing(String::length))
                        .orElseThrow()
        );

        //We set the font so that each effect fit
        int margin = getWidth() / 16;
        Font font = g2D.getFont();
        g2D.setFont(font.deriveFont((float) (font.getSize() * (getWidth() - 2 * margin)) / lengthOfLongestString));

        //Draw effects
        int yDelta = 0;
        int stringHeight = getFontMetrics(g2D.getFont()).getHeight();

        for (int i = 1; i <= effects.size(); i++) {
            int yPosition = y + (i + yDelta) * stringHeight;
            yDelta += drawXCenteredString(g2D, effects.get(i - 1).toString(), yPosition, getWidth());
            //Draw line to separate effects
            if (i < effects.size()) {
                yPosition = y + (i + yDelta) * stringHeight;
                g2D.drawRect(getWidth() / 12, yPosition + stringHeight / 3, getWidth() - 2 * getWidth() / 12, 0);
            }
        }
    }

    /**
     * Draw effects container.
     * 
     * @param g2D
     * @param y
     * @param effects
     * @param witch
     */
    private void drawEffectsContainer(Graphics2D g2D, int y, List<Effect> effects, boolean witch) {
        g2D.setColor(Color.decode(witch ? "#ffebcc" : "#d6ebd6"));
        g2D.fillRect(0, y, getWidth(), getHeight() / 4);

        g2D.setFont(g2D.getFont().deriveFont((float) (getWidth() / 10)).deriveFont(Font.BOLD));
        g2D.setPaint(Gradient.getGradient(g2D, y, witch ? Gradient.WITCH : Gradient.HUNT));
        drawXCenteredString(g2D, witch ? "Witch?" : "Hunt!", y, getWidth());

        g2D.setColor(Color.BLACK);
        g2D.setFont(g2D.getFont().deriveFont((float) (getWidth() / 200)));
        drawEffects(g2D, y, effects);
    }

    /**
     * Draw card.
     * 
     * @param g2D
     */
    void drawCard(Graphics2D g2D) {
        if (rumourCard != null) {
            //The card itself
            g2D.drawImage(template, 0, 0, getWidth(), getHeight(), this);

            //Card name
            g2D.setFont(g2D.getFont().deriveFont((float) (getWidth() / 10)).deriveFont(Font.BOLD));
            g2D.setPaint(Gradient.getGradient(g2D, getHeight() / 5, Gradient.NAME));
            drawXCenteredString(g2D, rumourCard.getCardName().toString(), getHeight() / 5, getWidth());

            //Witch effects
            drawEffectsContainer(g2D, getHeight() / 3, rumourCard.getWitchEffects(), true);

            //Hunt effects
            drawEffectsContainer(g2D, (int) (getHeight() / 1.5), rumourCard.getHuntEffects(), false);
        } else {
            g2D.drawImage(hiddenCard, 0, 0, getWidth(), getHeight(), this);
        }
    }

    /**
     * Pain the component.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawCard((Graphics2D) g);
    }
}
