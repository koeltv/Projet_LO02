package com.view.graphic.dynamic;

/**
 * The type Printable action.
 */
class PrintableAction {
    /**
     * The average number of character to be displayed per second.
     * This is obtained by multiplying the average number of character in a word in the english language (5) to the number of seconds in a minute (60)
     * and dividing by the average number of thoughts per minutes (2000).
     */
    private static final double AVG_CHAR_PER_SECOND = (double) (5 * 60) / 2500;

    /**
     * The Text.
     */
    final String text;
    /**
     * The Display time.
     * This is the time the text will be displayed. It depends on the length of the String.
     */
    int displayTime;

    /**
     * Instantiates a new Printable action.
     *
     * @param text the text to be displayed
     */
    PrintableAction(String text) {
        this.text = text;
        this.displayTime = (int) (text.length() * AVG_CHAR_PER_SECOND) * 1000;
    }
}
