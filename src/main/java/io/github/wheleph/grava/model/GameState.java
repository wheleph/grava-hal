package io.github.wheleph.grava.model;

// TODO think about better name for this class
public class GameState {
    private Board board;
    private Player currentPlayer;
    private GameStatus gameStatus;

    public GameState(Board board, Player currentPlayer, GameStatus gameStatus) {
        this.board = board;
        this.currentPlayer = currentPlayer;
        this.gameStatus = gameStatus;
    }

    public Board getBoard() {
        return board;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }
}
