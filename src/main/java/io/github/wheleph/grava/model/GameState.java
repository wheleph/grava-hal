package io.github.wheleph.grava.model;

public class GameState {
    private Board board;
    private GamePhase gamePhase;
    private Player currentPlayer;

    public GameState(Board board, Player currentPlayer, GamePhase gamePhase) {
        this.board = board;
        this.currentPlayer = currentPlayer;
        this.gamePhase = gamePhase;
    }

    public Board getBoard() {
        return board;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public GamePhase getGamePhase() {
        return gamePhase;
    }
}
