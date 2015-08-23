package io.github.wheleph.grava.logic;

import io.github.wheleph.grava.model.Board;
import io.github.wheleph.grava.model.GameState;
import io.github.wheleph.grava.model.GamePhase;
import io.github.wheleph.grava.model.Player;

import java.util.*;

/**
 * This class encapsulates game rules.
 * IT IS NOT THREAD SAFE.
 */
public class GameLogic {
    static final int BOARD_SIZE = 6;
    static final int INITIAL_NUMBER_OF_STONES = 6;

    private int size;
    private Map<Player, List<Integer>> playerPits = new EnumMap<>(Player.class);
    private Player currentPlayer;
    private GamePhase gamePhase;

    /**
     * Initializes the game with standard board
     */
    public GameLogic() {
        this(BOARD_SIZE, INITIAL_NUMBER_OF_STONES, Player.PLAYER_1);
    }

    GameLogic(int numberOfPits, int initialStoneCount, Player initialPlayer) {
        Map<Player, List<Integer>> initialPlayerPits = new EnumMap<>(Player.class);
        for (Player player : Player.values()) {
            initialPlayerPits.put(player, initPlayerPits(numberOfPits, initialStoneCount));
        }

        init(initialPlayerPits, initialPlayer, GamePhase.IN_PROGRESS);
    }

    GameLogic(Map<Player, List<Integer>> playerPits, Player currentPlayer, GamePhase currentGamePhase) {
        init(playerPits, currentPlayer, currentGamePhase);
    }

    /**
     * @param pitIndex 1-based pit index
     * @return state of the game after this move is done
     */
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
        if (lastSeededPitIndex == Board.GRAVA_HAL_PIT_INDEX) {
            nextPlayer = player;
        } else {
            nextPlayer = Player.nextPlayer(player);

            // Determine if opposite player pit is captured
            if (getPitStoneCount(player, lastSeededPitIndex) == 1) {
                int oldGravaHalCount = getGravaHalStoneCount(player);
                int oppositePitIndex = size + 1 - lastSeededPitIndex;
                int oppositePitStoneCount = getPitStoneCount(otherPlayer, oppositePitIndex);
                setGravaHalStoneCount(player, 1 + oppositePitStoneCount + oldGravaHalCount);

                clearAndGetCount(player, lastSeededPitIndex);
                clearAndGetCount(otherPlayer, oppositePitIndex);
            }
        }

        // Determine if the game ended
        GamePhase gamePhase = GamePhase.IN_PROGRESS;
        int playerNumberOfStones = getNumberOfStonesInPits(player);
        if (playerNumberOfStones == 0) {
            int playerTotalNumberOfStones = getTotalNumberOfStones(player);
            int otherPlayerTotalNumberOfStones = getTotalNumberOfStones(otherPlayer);

            // Put all the other player's stones into grava hal
            setGravaHalStoneCount(otherPlayer, otherPlayerTotalNumberOfStones);
            for (int i = 1; i <= size; i++) {
                clearAndGetCount(otherPlayer, i);
            }

            if (otherPlayerTotalNumberOfStones > playerTotalNumberOfStones) {
                nextPlayer = otherPlayer;
                gamePhase = GamePhase.VICTORY;
            } else if (otherPlayerTotalNumberOfStones < playerTotalNumberOfStones) {
                nextPlayer = player;
                gamePhase = GamePhase.VICTORY;
            } else {
                gamePhase = GamePhase.DRAW;
            }
        }

        this.gamePhase = gamePhase;
        this.currentPlayer = nextPlayer;
        return getGameState();
    }

    /**
     * @return current state of the game
     */
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
        playerPits.get(player).set(Board.GRAVA_HAL_PIT_INDEX, stoneCount);
    }

    private int getGravaHalStoneCount(Player player) {
        return playerPits.get(player).get(Board.GRAVA_HAL_PIT_INDEX);
    }

    private void setPitStoneCount(Player player, int pitIndex, int stoneCount) {
        playerPits.get(player).set(pitIndex, stoneCount);
    }

    private int getPitStoneCount(Player player, int pitIndex) {
        return playerPits.get(player).get(pitIndex);
    }

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
