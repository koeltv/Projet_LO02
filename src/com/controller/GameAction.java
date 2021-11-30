package com.controller;

/**
 * Action choice at the end of a game.
 */
enum GameAction {
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
