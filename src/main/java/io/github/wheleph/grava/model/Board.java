package io.github.wheleph.grava.model;

import java.util.*;

public class Board {
    static final int BOARD_SIZE = 6;
    static final int INITIAL_NUMBER_OF_STONES = 6;

    private int size;
    private Map<Player, List<Integer>> playerPits = new HashMap<>();
    private Player currentPlayer;

    public Board() {
        this(BOARD_SIZE, INITIAL_NUMBER_OF_STONES, Player.PLAYER_1);
    }

    public Board(int size, int initialStoneCount, Player initialPlayer) {
        this.size = size;
        for (Player player : Player.values()) {
            playerPits.put(player, initPlayerPits(size, initialStoneCount));
        }
        this.currentPlayer = initialPlayer;
    }

    public int getGravaHalStoneCount(Player player) {
        return playerPits.get(player).get(0);
    }

    public int getPitStoneCount(Player player, int pitIndex) {
        return playerPits.get(player).get(pitIndex);
    }

    public void setGravaHalStoneCount(Player player, int stoneCount) {
        playerPits.get(player).set(0, stoneCount);
    }

    public void setPitStoneCount(Player player, int pitIndex, int stoneCount) {
        playerPits.get(player).set(pitIndex, stoneCount);
    }

    public int clearAndGetCount(Player player, int pitIndex) {
        int numberOfStones = getPitStoneCount(player, pitIndex);
        setPitStoneCount(player, pitIndex, 0);
        return numberOfStones;
    }

    private List<Integer> initPlayerPits(int boardSize, int initialStoneCount) {
        List<Integer> playerPits = new ArrayList<>(boardSize + 1);
        playerPits.add(0);
        for (int i = 1; i <= boardSize; i++) {
            playerPits.add(initialStoneCount);
        }
        return playerPits;
    }

    public int getSize() {
        return size;
    }

    public GameState move(Player player, int pitIndex) {
        if (player != currentPlayer) {
            throw new IllegalArgumentException("Wrong player");
        }

        int numberOfStones = clearAndGetCount(player, pitIndex);
        if (numberOfStones == 0) {
            throw new IllegalArgumentException("Cannot sow stones from empty pit");
        }
        int currPitIndex = pitIndex + 1;
        for (int i = 0; i < numberOfStones; i++) {
            if (currPitIndex <= getSize()) {
                int currNumberOfStones = getPitStoneCount(player, currPitIndex);
                setPitStoneCount(player, currPitIndex, currNumberOfStones + 1);
                currPitIndex++;
            } else {
                int currNumberOfStones = getGravaHalStoneCount(player);
                setGravaHalStoneCount(player, currNumberOfStones + 1);
                currPitIndex = 1;
            }
        }

        Player nextPlayer;
        if (currPitIndex == 1) {
            nextPlayer = player;
        } else {
            nextPlayer = Player.nextPlayer(player);
            int lastPit = currPitIndex - 1;
            if (getPitStoneCount(player, lastPit) == 1) {
                int otherPlayerCount = getPitStoneCount(nextPlayer, lastPit);
                int oldGravaHalCount = getGravaHalStoneCount(player);
                setGravaHalStoneCount(player, 1 + otherPlayerCount + oldGravaHalCount);

                clearAndGetCount(player, lastPit);
                clearAndGetCount(nextPlayer, lastPit);
            }
        }

        int playerNumberOfStones = getNumberOfStonesInPits(player);
        GameStatus gameStatus = GameStatus.IN_PROGRESS;
        if (playerNumberOfStones == 0) {
            Player otherPlayer = Player.nextPlayer(player);
            int playerTotalNumberOfStones = getTotalNumberOfStones(player);
            int otherPlayerTotalNumberOfStones = getTotalNumberOfStones(otherPlayer);
            if (otherPlayerTotalNumberOfStones > playerTotalNumberOfStones) {
                nextPlayer = otherPlayer;
                gameStatus = GameStatus.WON;
            } else if (otherPlayerTotalNumberOfStones < playerTotalNumberOfStones) {
                nextPlayer = player;
                gameStatus = GameStatus.WON;
            } else {
                gameStatus = GameStatus.DRAW;
            }
        }

        currentPlayer = nextPlayer;
        return new GameState(this, nextPlayer, gameStatus);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Board board = (Board) o;
        return Objects.equals(playerPits, board.playerPits);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerPits);
    }

    @Override
    public String toString() {
        return String.format("Board (playerPits=%s)", playerPits);
    }
}
