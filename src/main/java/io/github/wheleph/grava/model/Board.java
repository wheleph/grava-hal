package io.github.wheleph.grava.model;

import java.util.*;

/**
 * Simple POJO that represents Grava Hal board.
 */
public class Board {
    private int size;
    private Map<Player, List<Integer>> playerPits = new HashMap<>();

    public Board(int size, int initialStoneCount) {
        this.size = size;
        for (Player player : Player.values()) {
            playerPits.put(player, initPlayerPits(size, initialStoneCount));
        }
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
