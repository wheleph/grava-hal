package io.github.wheleph.grava.web;

import io.github.wheleph.grava.model.GamePhase;
import io.github.wheleph.grava.model.GameState;
import io.github.wheleph.grava.model.Player;
import io.github.wheleph.grava.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class GameController {

    static final String VIEW_INIT = "init";
    static final String VIEW_GAME = "game";
    static final String GAME_MODEL_VIEW_BEAN = "gameModelViewBean";

    @Autowired
    private GameService gameService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return VIEW_INIT;
    }

    // TODO make url not updated in browser url box
    @RequestMapping(value = "/start_game", method = RequestMethod.POST)
    public ModelAndView startGame() {
        GameState gameState = gameService.startGame();
        return new ModelAndView(VIEW_GAME, GAME_MODEL_VIEW_BEAN, createViewBean(gameState));
    }

    @RequestMapping(value = "/move", method = RequestMethod.POST)
    public ModelAndView move(Player player, int pitIndex) {
        GameState oldGameState = gameService.getGameState();
        try {
            GameState gameState = gameService.move(player, pitIndex);
            return new ModelAndView(VIEW_GAME, GAME_MODEL_VIEW_BEAN, createViewBean(gameState));
        } catch (Exception e) {
            String errorMessage = "Error: " + e.getMessage();
            return new ModelAndView(VIEW_GAME, GAME_MODEL_VIEW_BEAN, new GameModelViewBean(oldGameState, errorMessage));
        }
    }

    @RequestMapping(value = "/end_game", method = RequestMethod.POST)
    public String endGame() {
        gameService.endGame();
        return VIEW_INIT;
    }

    private GameModelViewBean createViewBean(GameState gameState) {
        String message; // TODO move string to resource file
        GamePhase gamePhase = gameState.getGamePhase();
        Player currentPlayer = gameState.getCurrentPlayer();
        if (gamePhase == GamePhase.IN_PROGRESS) {
            if (currentPlayer == Player.PLAYER_1) {
                message = "Player 1 to move";
            } else {
                message = "Player 2 to move";
            }
        } else if (gamePhase == GamePhase.DRAW) {
            message = "Draw game";
        } else if (gamePhase == GamePhase.WIN) {
            if (currentPlayer == Player.PLAYER_1) {
                message = "Player 1 won";
            } else {
                message = "Player 2 won";
            }
        } else {
            throw new IllegalArgumentException("Cannot understand game state");
        }

        return new GameModelViewBean(gameState, message);
    }
}
