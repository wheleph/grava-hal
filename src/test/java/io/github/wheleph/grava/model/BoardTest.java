package io.github.wheleph.grava.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class BoardTest {
    @Test
    public void testBoardCreation() {
        final int boardSize = 2;
        final int initialStoneCount = 3;
        Board board = new Board(boardSize, initialStoneCount, Player.PLAYER_1);

        // Test pit initialization
        assertEquals(initialStoneCount, board.getPitStoneCount(Player.PLAYER_1, 1));
        assertEquals(initialStoneCount, board.getPitStoneCount(Player.PLAYER_1, 2));
        assertEquals(initialStoneCount, board.getPitStoneCount(Player.PLAYER_2, 1));
        assertEquals(initialStoneCount, board.getPitStoneCount(Player.PLAYER_2, 2));

        // Test grava hal initialization
        assertEquals(0, board.getGravaHalStoneCount(Player.PLAYER_1));
        assertEquals(0, board.getGravaHalStoneCount(Player.PLAYER_2));
    }

    @Test
    public void testSetPitStoneCount() {
        final int boardSize = 2;
        final int initialStoneCount = 3;
        Board board = new Board(boardSize, initialStoneCount, Player.PLAYER_1);

        Player player = Player.PLAYER_1;
        int pitIndex = 2;
        int newStoneCount = 10;
        assertNotEquals(initialStoneCount, newStoneCount);

        board.setPitStoneCount(player, pitIndex, newStoneCount);

        assertEquals(newStoneCount, board.getPitStoneCount(player, pitIndex));
    }

    @Test
    public void testSetGravaHalStoneCount() {
        Board board = new Board(2, 3, Player.PLAYER_1);

        Player player = Player.PLAYER_1;
        int newStoneCount = 10;
        int initialStoneCount = board.getGravaHalStoneCount(player);
        assertNotEquals(initialStoneCount, newStoneCount);

        board.setGravaHalStoneCount(player, newStoneCount);

        assertEquals(newStoneCount, board.getGravaHalStoneCount(player));
    }

    @Test
    public void testClearAndGet() {
        int initialStoneCount = 3;
        int pitIndex = 1;
        Board board = new Board(2, initialStoneCount, Player.PLAYER_1);
        int oldCount = board.clearAndGetCount(Player.PLAYER_1, pitIndex);

        assertEquals(initialStoneCount, oldCount);
        assertEquals(0, board.getPitStoneCount(Player.PLAYER_1, pitIndex));
    }

    @Test
    public void testCreateDefaultBoard() {
        Board board = new Board();

        assertNotNull(board);
        assertEquals(Board.INITIAL_NUMBER_OF_STONES, board.getPitStoneCount(Player.PLAYER_1, 1));
        assertEquals(Board.INITIAL_NUMBER_OF_STONES, board.getPitStoneCount(Player.PLAYER_2, 1));
    }

    @Test
    public void testBasicMove() {
        Board board = new Board(4, 6, Player.PLAYER_1);

        GameState gameState = board.move(Player.PLAYER_1, 1);

        assertNotNull(gameState);
        assertSame(GameStatus.IN_PROGRESS, gameState.getGameStatus());
        assertSame(Player.PLAYER_2, gameState.getCurrentPlayer());

        // Assert board
        Board expectedBoard = new Board(4, 6, Player.PLAYER_1);
        expectedBoard.setPitStoneCount(Player.PLAYER_1, 1, 1);
        expectedBoard.setPitStoneCount(Player.PLAYER_1, 2, 8);
        expectedBoard.setPitStoneCount(Player.PLAYER_1, 3, 7);
        expectedBoard.setPitStoneCount(Player.PLAYER_1, 4, 7);
        expectedBoard.setGravaHalStoneCount(Player.PLAYER_1, 1);
        assertEquals(expectedBoard, gameState.getBoard());
    }

    @Test
    public void testAdditionalMove() {
        Board board = new Board(3, 2, Player.PLAYER_1);

        GameState gameState = board.move(Player.PLAYER_1, 2);

        assertSame(GameStatus.IN_PROGRESS, gameState.getGameStatus());
        // Player 1 continues to be the current player because the last stone fell into the Grava Hal
        assertSame(Player.PLAYER_1, gameState.getCurrentPlayer());
    }

    @Test
    public void testEndGameWin() {
        Board board = new Board(3, 1, Player.PLAYER_1);
        board.setPitStoneCount(Player.PLAYER_1, 1, 0);
        board.setPitStoneCount(Player.PLAYER_1, 2, 0);

        GameState gameState = board.move(Player.PLAYER_1, 3);

        assertSame(GameStatus.WON, gameState.getGameStatus());
        assertSame(Player.PLAYER_2, gameState.getCurrentPlayer());
    }

    @Test
    public void testEndGameDraw() {
        Board board = new Board(1, 1, Player.PLAYER_1);

        GameState gameState = board.move(Player.PLAYER_1, 1);

        assertSame(GameStatus.DRAW, gameState.getGameStatus());
    }

    @Test
    public void testStoneCapture() {
        Player player = Player.PLAYER_1;
        Player otherPlayer = Player.PLAYER_2;
        Board board = new Board(3, 2, Player.PLAYER_1);
        // make one of the pits empty
        board.setPitStoneCount(player, 3, 0);

        GameState gameState = board.move(player, 1);

        assertSame(GameStatus.IN_PROGRESS, gameState.getGameStatus());
        assertSame(otherPlayer, gameState.getCurrentPlayer());

        Board updatedBoard = gameState.getBoard();
        assertEquals(3, updatedBoard.getGravaHalStoneCount(player));
        assertEquals(0, updatedBoard.getPitStoneCount(player, 3));
        assertEquals(0, updatedBoard.getPitStoneCount(otherPlayer, 3));
        assertEquals(0, updatedBoard.getGravaHalStoneCount(otherPlayer));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMoveEmptyPit() {
        Board board = new Board(1, 1, Player.PLAYER_1);
        int pitIndex = 1;
        board.setPitStoneCount(Player.PLAYER_1, pitIndex, 0);

        board.move(Player.PLAYER_1, pitIndex);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMoveWrongPlayer() {
        Board board = new Board(3, 2, Player.PLAYER_1);

        board.move(Player.PLAYER_1, 1);
        board.move(Player.PLAYER_1, 2);
    }
}