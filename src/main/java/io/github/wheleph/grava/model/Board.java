package io.github.wheleph.grava.model;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Represents board state.
 */
public class Board {
    /**
     * Internal index in the pit list where grava hal stone count it stored. Should not be explicitly used by clients. The only notable
     * exclusion is {@link io.github.wheleph.grava.logic.GameLogic}
     */
    public static final int GRAVA_HAL_PIT_INDEX = 0;

    private Map<Player, List<Integer>> playerPits = new EnumMap<>(Player.class);

    public Board(Map<Player, List<Integer>> playerPits) {
        this.playerPits = playerPits;
    }

    public int getGravaHalStoneCount(Player player) {
        return playerPits.get(player).get(GRAVA_HAL_PIT_INDEX);
    }

    /**
     * @param pitIndex 1-based pit index
     */
    public int getPitStoneCount(Player player, int pitIndex) {
        return playerPits.get(player).get(pitIndex);
    }

    @Override
    public String toString() {
        return String.format("GameLogic (playerPits=%s)", playerPits);
    }

}
