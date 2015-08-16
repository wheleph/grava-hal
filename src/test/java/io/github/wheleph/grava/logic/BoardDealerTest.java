package io.github.wheleph.grava.logic;

import io.github.wheleph.grava.model.Board;
import io.github.wheleph.grava.model.GameState;
import io.github.wheleph.grava.model.GameStatus;
import io.github.wheleph.grava.model.Player;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

public class BoardDealerTest {
    private BoardDealer boardDealer = new BoardDealer();

    @Test
    public void testCreateDefaultBoard() {
        Board board = boardDealer.createBoard();

        assertNotNull(board);
        assertEquals(BoardDealer.INITIAL_NUMBER_OF_STONES, board.getPitStoneCount(Player.PLAYER_1, 1));
        assertEquals(BoardDealer.INITIAL_NUMBER_OF_STONES, board.getPitStoneCount(Player.PLAYER_2, 1));
    }

    @Test
    public void testBasicMove() {
        Board board = new Board(4, 6);

        GameState gameState = boardDealer.move(board, Player.PLAYER_1, 1);

        assertNotNull(gameState);
        assertSame(GameStatus.IN_PROGRESS, gameState.getGameStatus());
        assertSame(Player.PLAYER_2, gameState.getCurrentPlayer());

        // Assert board
        Board expectedBoard = new Board(4, 6);
        expectedBoard.setPitStoneCount(Player.PLAYER_1, 1, 1);
        expectedBoard.setPitStoneCount(Player.PLAYER_1, 2, 8);
        expectedBoard.setPitStoneCount(Player.PLAYER_1, 3, 7);
        expectedBoard.setPitStoneCount(Player.PLAYER_1, 4, 7);
        expectedBoard.setGravaHalStoneCount(Player.PLAYER_1, 1);
        assertEquals(expectedBoard, gameState.getBoard());
    }

    @Test
    public void testAdditionalMove() {
        Board board = new Board(3, 2);

        GameState gameState = boardDealer.move(board, Player.PLAYER_1, 2);

        assertSame(GameStatus.IN_PROGRESS, gameState.getGameStatus());
        // Player 1 continues to be the current player because the last stone fell into the Grava Hal
        assertSame(Player.PLAYER_1, gameState.getCurrentPlayer());
    }

    @Ignore
    @Test
    public void testStoneCapture() {
        Player player = Player.PLAYER_1;
        Player otherPlayer = Player.PLAYER_1;
        Board board = new Board(3, 2);
        // make one of the pits empty
        board.setPitStoneCount(player, 3, 0);

        GameState gameState = boardDealer.move(board, player, 1);

        assertSame(GameStatus.IN_PROGRESS, gameState.getGameStatus());
        assertSame(Player.PLAYER_2, gameState.getCurrentPlayer());

        Board updatedBoard = gameState.getBoard();
        assertEquals(3, updatedBoard.getGravaHalStoneCount(player));
        assertEquals(0, updatedBoard.getPitStoneCount(player, 3));
        assertEquals(0, updatedBoard.getPitStoneCount(otherPlayer, 3));
        assertEquals(0, updatedBoard.getGravaHalStoneCount(otherPlayer));
    }
}