package io.github.wheleph.grava.service;

import io.github.wheleph.grava.logic.GameLogic;
import io.github.wheleph.grava.model.GameState;
import io.github.wheleph.grava.model.Player;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

@Service
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class GameService {
    private GameLogic gameLogic;

    public GameState startGame() {
        gameLogic = new GameLogic();
        return gameLogic.getGameState();
    }

    public GameState move(Player player, int pitIndex) {
        return gameLogic.move(player, pitIndex);
    }

    public void endGame() {
        gameLogic = null;
    }

    public GameState getGameState() {
        return gameLogic.getGameState();
    }
}
