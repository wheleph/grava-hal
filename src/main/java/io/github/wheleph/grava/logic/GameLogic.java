package io.github.wheleph.grava.logic;

import io.github.wheleph.grava.model.Board;
import io.github.wheleph.grava.model.GameState;
import io.github.wheleph.grava.model.GamePhase;
import io.github.wheleph.grava.model.Player;

import java.util.*;

public class GameLogic {
    static final int BOARD_SIZE = 6;
    static final int INITIAL_NUMBER_OF_STONES = 6;
    private static final int GRAVA_HAL_PIT_INDEX = 0;

    private int size;
    // TODO MAKE THIS IMPLEMENTATION FASTER BY USING ARRAYS
    private Map<Player, List<Integer>> playerPits = new HashMap<>();
    private Player currentPlayer;
    private GamePhase gamePhase;

    public GameLogic() {
        this(BOARD_SIZE, INITIAL_NUMBER_OF_STONES, Player.PLAYER_1);
    }

    public GameLogic(int numberOfPits, int initialStoneCount, Player initialPlayer) {
        Map<Player, List<Integer>> initialPlayerPits = new HashMap<>();
        for (Player player : Player.values()) {
            initialPlayerPits.put(player, initPlayerPits(numberOfPits, initialStoneCount));
        }

        init(initialPlayerPits, initialPlayer, GamePhase.IN_PROGRESS);
    }

    GameLogic(Map<Player, List<Integer>> playerPits, Player currentPlayer, GamePhase currentGamePhase) {
        init(playerPits, currentPlayer, currentGamePhase);
    }

    public GameState move(Player player, int pitIndex) {
        if (player != currentPlayer) {
            throw new IllegalArgumentException("Wrong player");
        }

        Player otherPlayer = Player.nextPlayer(player);

        int numberOfStones = clearAndGetCount(player, pitIndex);
        if (numberOfStones == 0) {
            throw new IllegalArgumentException("Cannot sow stones from empty pit");
        }

        // seed stones from the pit
        int lastSeededPitIndex = pitIndex;
        for (int i = 1; i <= numberOfStones; i++) {
            List<Integer> currentPlayerPits = playerPits.get(currentPlayer);
            lastSeededPitIndex = (pitIndex + i) % (size + 1);
            int currNumberOfStones = currentPlayerPits.get(lastSeededPitIndex);
            currentPlayerPits.set(lastSeededPitIndex, currNumberOfStones + 1);
        }

        Player nextPlayer;
        if (lastSeededPitIndex == GRAVA_HAL_PIT_INDEX) {
            nextPlayer = player;
        } else {
            nextPlayer = Player.nextPlayer(player);

            // Determine if opposite player pit is captured
            if (getPitStoneCount(player, lastSeededPitIndex) == 1) {
                int oldGravaHalCount = getGravaHalStoneCount(player);
                int oppositePlayerPitCount = getPitStoneCount(otherPlayer, lastSeededPitIndex);
                setGravaHalStoneCount(player, 1 + oppositePlayerPitCount + oldGravaHalCount);

                clearAndGetCount(player, lastSeededPitIndex);
                clearAndGetCount(otherPlayer, lastSeededPitIndex);
            }
        }

        // Determine if the game ended
        GamePhase gamePhase = GamePhase.IN_PROGRESS;
        int playerNumberOfStones = getNumberOfStonesInPits(player);
        if (playerNumberOfStones == 0) {
            // TODO put stones of another player into his Grava hal
            int playerTotalNumberOfStones = getTotalNumberOfStones(player);
            int otherPlayerTotalNumberOfStones = getTotalNumberOfStones(otherPlayer);
            if (otherPlayerTotalNumberOfStones > playerTotalNumberOfStones) {
                nextPlayer = otherPlayer;
                gamePhase = GamePhase.WIN;
            } else if (otherPlayerTotalNumberOfStones < playerTotalNumberOfStones) {
                nextPlayer = player;
                gamePhase = GamePhase.WIN;
            } else {
                gamePhase = GamePhase.DRAW;
            }
        }

        this.gamePhase = gamePhase;
        this.currentPlayer = nextPlayer;
        return getGameState();
    }

    public GameState getGameState() {
        return new GameState(new Board(playerPits), currentPlayer, gamePhase);
    }

    private void init(Map<Player, List<Integer>> playerPits, Player currentPlayer, GamePhase currentGamePhase) {
        this.playerPits = playerPits;
        this.size = playerPits.get(currentPlayer).size() - 1;
        this.currentPlayer = currentPlayer;
        this.gamePhase = currentGamePhase;
    }

    private List<Integer> initPlayerPits(int boardSize, int initialStoneCount) {
        List<Integer> playerPits = new ArrayList<>(boardSize + 1);
        playerPits.add(0);
        for (int i = 1; i <= boardSize; i++) {
            playerPits.add(initialStoneCount);
        }
        return playerPits;
    }

    private void setGravaHalStoneCount(Player player, int stoneCount) {
        playerPits.get(player).set(GRAVA_HAL_PIT_INDEX, stoneCount);
    }

    private int getGravaHalStoneCount(Player player) {
        return playerPits.get(player).get(GRAVA_HAL_PIT_INDEX);
    }

    // TODO check that pit index is correct
    private void setPitStoneCount(Player player, int pitIndex, int stoneCount) {
        playerPits.get(player).set(pitIndex, stoneCount);
    }

    // TODO check that pit index is correct
    private int getPitStoneCount(Player player, int pitIndex) {
        return playerPits.get(player).get(pitIndex);
    }

    // TODO check that pit index is correct
    int clearAndGetCount(Player player, int pitIndex) {
        int numberOfStones = getPitStoneCount(player, pitIndex);
        setPitStoneCount(player, pitIndex, 0);
        return numberOfStones;
    }

    private int getNumberOfStonesInPits(Player player) {
        List<Integer> pits = playerPits.get(player);
        int totalNumberOfStones = 0;
        for (int i = 1; i < pits.size(); i++) {
            totalNumberOfStones += pits.get(i);
        }
        return totalNumberOfStones;
    }

    private int getTotalNumberOfStones(Player player) {
        return getNumberOfStonesInPits(player) + getGravaHalStoneCount(player);
    }
}
