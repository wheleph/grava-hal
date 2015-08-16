package io.github.wheleph.grava.logic;

import io.github.wheleph.grava.model.Board;
import io.github.wheleph.grava.model.GameState;
import io.github.wheleph.grava.model.GameStatus;
import io.github.wheleph.grava.model.Player;

public class BoardDealer {
    static final int BOARD_SIZE = 6;
    static final int INITIAL_NUMBER_OF_STONES = 6;

    public GameState move(Board board, Player player, int pitIndex) {
        Player nextPlayer = Player.nextPlayer(player);

        int numberOfStones = board.clearAndGetCount(player, pitIndex);
        int currPitIndex = pitIndex + 1;
        for (int i = 0; i < numberOfStones; i++) {
            if (currPitIndex <= board.getSize()) {
                int currNumberOfStones = board.getPitStoneCount(player, currPitIndex);
                board.setPitStoneCount(player, currPitIndex, currNumberOfStones + 1);
                currPitIndex++;
            } else {
                int currNumberOfStones = board.getGravaHalStoneCount(player);
                board.setGravaHalStoneCount(player, currNumberOfStones + 1);
                currPitIndex = 1;
            }
        }

        return new GameState(board, nextPlayer, GameStatus.IN_PROGRESS);
    }

    public Board createBoard() {
        return new Board(BOARD_SIZE, INITIAL_NUMBER_OF_STONES);
    }
}
