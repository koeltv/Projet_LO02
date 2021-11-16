package com.model.player;

/**
 * The enum Player action.
 */
public enum PlayerAction {
    /**
     * Accuse player action.
     */
    ACCUSE,
    /**
     * Reveal identity player action.
     */
    REVEAL_IDENTITY,
    /**
     * Use card player action.
     */
    USE_CARD,
    /**
     * Look at identity player action.
     */
    LOOK_AT_IDENTITY,
    /**
     * Discard player action.
     */
    DISCARD;

    @Override
    public String toString() {
        char[] charArray = super.toString().replace("_", " ").toCharArray();
        for (int i = 1; i < charArray.length; i++) {
            if (charArray[i - 1] != ' ') charArray[i] = Character.toLowerCase(charArray[i]);
        }
        return new String(charArray);
    }
}
