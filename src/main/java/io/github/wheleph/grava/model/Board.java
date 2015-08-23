package io.github.wheleph.grava.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {
    public static final int GRAVA_HAL_PIT_INDEX = 0;

    private Map<Player, List<Integer>> playerPits = new HashMap<>();

    public Board(Map<Player, List<Integer>> playerPits) {
        this.playerPits = playerPits;
    }

    public int getGravaHalStoneCount(Player player) {
        return playerPits.get(player).get(GRAVA_HAL_PIT_INDEX);
    }

    public int getPitStoneCount(Player player, int pitIndex) {
        return playerPits.get(player).get(pitIndex);
    }

    @Override
    public String toString() {
        return String.format("GameLogic (playerPits=%s)", playerPits);
    }

}
