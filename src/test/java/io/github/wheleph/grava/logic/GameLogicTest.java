package io.github.wheleph.grava.logic;

import io.github.wheleph.grava.model.Board;
import io.github.wheleph.grava.model.GamePhase;
import io.github.wheleph.grava.model.GameState;
import io.github.wheleph.grava.model.Player;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GameLogicTest {
    @Test
    public void testGameInitialization() {
        final int boardSize = 2;
        final int initialStoneCount = 3;
        final Player initialPlayer = Player.PLAYER_1;

        // Methods under test
        GameLogic gameLogic = new GameLogic(boardSize, initialStoneCount, initialPlayer);
        GameState gameState = gameLogic.getGameState();

        // Verify game state
        assertNotNull(gameState);

        assertEquals(initialPlayer, gameState.getCurrentPlayer());
        assertEquals(GamePhase.IN_PROGRESS, gameState.getGamePhase());

        // Verify board state
        Board board = gameState.getBoard();
        assertNotNull(board);

        assertEquals(initialStoneCount, board.getPitStoneCount(Player.PLAYER_1, 1));
        assertEquals(initialStoneCount, board.getPitStoneCount(Player.PLAYER_1, 2));
        assertEquals(initialStoneCount, board.getPitStoneCount(Player.PLAYER_2, 1));
        assertEquals(initialStoneCount, board.getPitStoneCount(Player.PLAYER_2, 2));

        assertEquals(0, board.getGravaHalStoneCount(Player.PLAYER_1));
        assertEquals(0, board.getGravaHalStoneCount(Player.PLAYER_2));
    }

    @Test
    public void testDefaultGameInitialization() {
        // Method under test
        GameLogic gameLogic = new GameLogic();

        // Verify board state
        GameState gameState = gameLogic.getGameState();
        assertNotNull(gameState);

        Board board = gameState.getBoard();
        assertNotNull(board);

        assertEquals(GameLogic.INITIAL_NUMBER_OF_STONES, board.getPitStoneCount(Player.PLAYER_1, 1));
        assertEquals(GameLogic.INITIAL_NUMBER_OF_STONES, board.getPitStoneCount(Player.PLAYER_2, 1));
    }

    @Test
    public void testCustomGameInitialization() {
        Map<Player, List<Integer>> playerPits = new HashMap<>();
        playerPits.put(Player.PLAYER_1, Arrays.asList(1, 2, 3));
        playerPits.put(Player.PLAYER_2, Arrays.asList(4, 5, 6));

        // Method under test
        GameLogic gameLogic = new GameLogic(playerPits, Player.PLAYER_1, GamePhase.IN_PROGRESS);
        GameState gameState = gameLogic.getGameState();

        // Verify board state
        assertNotNull(gameState);
        Board board = gameState.getBoard();
        assertNotNull(board);

        assertEquals(1, board.getGravaHalStoneCount(Player.PLAYER_1));
        assertEquals(2, board.getPitStoneCount(Player.PLAYER_1, 1));
        assertEquals(3, board.getPitStoneCount(Player.PLAYER_1, 2));

        assertEquals(4, board.getGravaHalStoneCount(Player.PLAYER_2));
        assertEquals(5, board.getPitStoneCount(Player.PLAYER_2, 1));
        assertEquals(6, board.getPitStoneCount(Player.PLAYER_2, 2));
    }

    @Test
    public void testClearAndGet() {
        int initialStoneCount = 3;
        int pitIndex = 1;
        GameLogic gameLogic = new GameLogic(2, initialStoneCount, Player.PLAYER_1);

        // Method under test
        int oldCount = gameLogic.clearAndGetCount(Player.PLAYER_1, pitIndex);

        GameState gameState = gameLogic.getGameState();
        assertNotNull(gameState);

        Board board = gameState.getBoard();
        assertNotNull(board);

        assertEquals(initialStoneCount, oldCount);
        assertEquals(0, board.getPitStoneCount(Player.PLAYER_1, pitIndex));
    }

    @Test
    public void testBasicMove() {
        GameLogic gameLogic = new GameLogic(4, 6, Player.PLAYER_1);

        // method under test
        GameState gameState = gameLogic.move(Player.PLAYER_1, 1);

        // Verify game state
        assertNotNull(gameState);
        assertEquals(GamePhase.IN_PROGRESS, gameState.getGamePhase());
        assertEquals(Player.PLAYER_2, gameState.getCurrentPlayer());

        // Verify board state
        Board board = gameState.getBoard();
        assertNotNull(board);

        assertEquals(1, board.getPitStoneCount(Player.PLAYER_1, 1));
        assertEquals(8, board.getPitStoneCount(Player.PLAYER_1, 2));
        assertEquals(7, board.getPitStoneCount(Player.PLAYER_1, 3));
        assertEquals(7, board.getPitStoneCount(Player.PLAYER_1, 4));
        assertEquals(1, board.getGravaHalStoneCount(Player.PLAYER_1));

        assertEquals(6, board.getPitStoneCount(Player.PLAYER_2, 1));
        assertEquals(6, board.getPitStoneCount(Player.PLAYER_2, 2));
        assertEquals(6, board.getPitStoneCount(Player.PLAYER_2, 3));
        assertEquals(6, board.getPitStoneCount(Player.PLAYER_2, 4));
        assertEquals(0, board.getGravaHalStoneCount(Player.PLAYER_2));
    }

    @Test
    public void testExtraMove() {
        GameLogic gameLogic = new GameLogic(3, 2, Player.PLAYER_1);

        // method under test
        GameState gameState = gameLogic.move(Player.PLAYER_1, 2);

        assertEquals(GamePhase.IN_PROGRESS, gameState.getGamePhase());
        // Player 1 continues to be the current player because the last stone fell into the Grava Hal
        assertEquals(Player.PLAYER_1, gameState.getCurrentPlayer());
    }

    @Test
    public void testEndGameWin() {
        Map<Player, List<Integer>> playerPits = new HashMap<>();
        playerPits.put(Player.PLAYER_1, Arrays.asList(0, 0, 0, 1));
        playerPits.put(Player.PLAYER_2, Arrays.asList(0, 1, 1, 1));
        GameLogic gameLogic = new GameLogic(playerPits, Player.PLAYER_1, GamePhase.IN_PROGRESS);

        // method under test
        GameState gameState = gameLogic.move(Player.PLAYER_1, 3);

        assertEquals(GamePhase.WIN, gameState.getGamePhase());
        assertEquals(Player.PLAYER_2, gameState.getCurrentPlayer());
    }

    @Test
    public void testEndGameDraw() {
        GameLogic gameLogic = new GameLogic(1, 1, Player.PLAYER_1);

        // method under test
        GameState gameState = gameLogic.move(Player.PLAYER_1, 1);

        assertEquals(GamePhase.DRAW, gameState.getGamePhase());
    }

    @Test
    public void testStoneCapture() {
        Player player = Player.PLAYER_1;
        Player otherPlayer = Player.PLAYER_2;

        Map<Player, List<Integer>> playerPits = new HashMap<>();
        playerPits.put(Player.PLAYER_1, Arrays.asList(0, 2, 2, 0));
        playerPits.put(Player.PLAYER_2, Arrays.asList(0, 2, 2, 2));
        GameLogic gameLogic = new GameLogic(playerPits, Player.PLAYER_1, GamePhase.IN_PROGRESS);

        // method under test
        GameState gameState = gameLogic.move(player, 1);

        // Verify game state
        assertEquals(GamePhase.IN_PROGRESS, gameState.getGamePhase());
        assertEquals(otherPlayer, gameState.getCurrentPlayer());

        // Verify board state
        Board updatedGameLogic = gameState.getBoard();
        assertEquals(3, updatedGameLogic.getGravaHalStoneCount(player));
        assertEquals(0, updatedGameLogic.getPitStoneCount(player, 3));
        assertEquals(0, updatedGameLogic.getPitStoneCount(otherPlayer, 3));
        assertEquals(0, updatedGameLogic.getGravaHalStoneCount(otherPlayer));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMoveEmptyPit() {
        Map<Player, List<Integer>> playerPits = new HashMap<>();
        playerPits.put(Player.PLAYER_1, Arrays.asList(0, 0));
        playerPits.put(Player.PLAYER_2, Arrays.asList(0, 1));
        GameLogic gameLogic = new GameLogic(playerPits, Player.PLAYER_1, GamePhase.IN_PROGRESS);

        gameLogic.move(Player.PLAYER_1, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMoveWrongPlayer() {
        GameLogic gameLogic = new GameLogic(3, 2, Player.PLAYER_1);

        gameLogic.move(Player.PLAYER_2, 1);
    }
}