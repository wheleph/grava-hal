package io.github.wheleph.grava.web;

import io.github.wheleph.grava.model.GameState;

public class GameModelViewBean {
    private GameState gameState;
    private String message;

    public GameModelViewBean(GameState gameState, String message) {
        this.gameState = gameState;
        this.message = message;
    }

    public GameState getGameState() {
        return gameState;
    }

    public String getMessage() {
        return message;
    }
}
