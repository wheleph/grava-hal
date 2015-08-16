package io.github.wheleph.grava.model;

public enum Player {
    PLAYER_1, PLAYER_2;

    public static Player fromIndex(int playerIndex) {
        if (playerIndex == 1) {
            return PLAYER_1;
        } else if (playerIndex == 2) {
            return PLAYER_2;
        } else {
            throw new IllegalArgumentException(String.format("Illegal player index: %d", playerIndex));
        }
    }

    public static Player nextPlayer(Player player) {
        if (player == PLAYER_1) {
            return PLAYER_2;
        } else {
            return PLAYER_1;
        }
    }
}
