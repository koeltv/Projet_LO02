package com.controller;

/**
 * Action choice at the end of a game.
 * 
 * @see com.controller.GameController
 */
public enum GameAction {
    /**
     * Restart a new game with existing players.
     */
    RESTART_GAME,
    /**
     * Restart a new game with new players.
     */
    RESET_GAME,
    /**
     * End the program.
     */
    STOP
}
