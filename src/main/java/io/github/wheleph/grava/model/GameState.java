package io.github.wheleph.grava.model;

public class GameState {
    private Board board;
    private GamePhase gamePhase;
    /**
     * Meaning of this field during different game phases:
     * {@link GamePhase#IN_PROGRESS} the next player to make a move
     * {@link GamePhase#VICTORY} the winning player
     */
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
