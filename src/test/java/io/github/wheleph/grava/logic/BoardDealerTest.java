package io.github.wheleph.grava.logic;

import io.github.wheleph.grava.model.Board;
import io.github.wheleph.grava.model.GameState;
import io.github.wheleph.grava.model.GameStatus;
import io.github.wheleph.grava.model.Player;
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

}