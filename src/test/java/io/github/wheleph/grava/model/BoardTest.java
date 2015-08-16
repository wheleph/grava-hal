package io.github.wheleph.grava.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class BoardTest {
    @Test
    public void testBoardCreation() {
        final int boardSize = 2;
        final int initialStoneCount = 3;
        Board board = new Board(boardSize, initialStoneCount);

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
        Board board = new Board(boardSize, initialStoneCount);

        Player player = Player.PLAYER_1;
        int pitIndex = 2;
        int newStoneCount = 10;
        assertNotEquals(initialStoneCount, newStoneCount);

        board.setPitStoneCount(player, pitIndex, newStoneCount);

        assertEquals(newStoneCount, board.getPitStoneCount(player, pitIndex));
    }

    @Test
    public void testSetGravaHalStoneCount() {
        Board board = new Board(2, 3);

        Player player = Player.PLAYER_1;
        int newStoneCount = 10;
        int initialStoneCount = board.getGravaHalStoneCount(player);
        assertNotEquals(initialStoneCount, newStoneCount);

        board.setGravaHalStoneCount(player, newStoneCount);

        assertEquals(newStoneCount, board.getGravaHalStoneCount(player));
    }

}