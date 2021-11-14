package com.view.graphic.dynamic;

import java.awt.*;

/**
 * The enum Gradient.
 * Used for title texts
 */
enum Gradient {
    NAME,
    WITCH,
    HUNT;

    /**
     * Get correct gradient to draw
     *
     * @param g2D      graphics where the gradient will be used
     * @param y        position of the text
     * @param gradient the gradient to pick
     * @return created gradient
     */
    static GradientPaint getGradient(Graphics2D g2D, int y, Gradient gradient) {
        FontMetrics fontMetrics = g2D.getFontMetrics(g2D.getFont());
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
}
