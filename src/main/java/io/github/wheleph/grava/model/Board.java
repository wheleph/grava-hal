package io.github.wheleph.grava.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Board {
    private Map<Player, List<Integer>> playerPits = new HashMap<>();

    public Board(Map<Player, List<Integer>> playerPits) {
        this.playerPits = playerPits;
    }

    public int getGravaHalStoneCount(Player player) {
        return playerPits.get(player).get(0);
    }

    // TODO check that pit index is correct
    public int getPitStoneCount(Player player, int pitIndex) {
        return playerPits.get(player).get(pitIndex);
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
        return String.format("GameLogic (playerPits=%s)", playerPits);
    }

}
