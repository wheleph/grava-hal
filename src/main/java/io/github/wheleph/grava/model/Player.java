package io.github.wheleph.grava.model;

public enum Player {
    PLAYER_1, PLAYER_2;

    public static Player nextPlayer(Player player) {
        if (player == PLAYER_1) {
            return PLAYER_2;
        } else {
            return PLAYER_1;
        }
    }
}
